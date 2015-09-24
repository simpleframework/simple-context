package net.simpleframework.ctx;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.common.db.DbUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleContextFactory extends ObjectEx {

	public static IModuleContext get(final String module) {
		return moduleCache.get(module);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IModuleContext> T get(final Class<T> mClass) {
		for (final IModuleContext context : moduleCache.values()) {
			if (mClass.isAssignableFrom(context.getClass())) {
				return (T) context;
			}
		}
		return null;
	}

	static Map<String, IModuleContext> moduleCache;
	static {
		moduleCache = new ConcurrentHashMap<String, IModuleContext>();
	}

	public static Collection<IModuleContext> allModules() {
		final ArrayList<IModuleContext> modules = new ArrayList<IModuleContext>(moduleCache.values());
		Collections.sort(modules, new Comparator<IModuleContext>() {
			@Override
			public int compare(final IModuleContext ctx1, final IModuleContext ctx2) {
				return ctx1.getModule().getOrder() - ctx2.getModule().getOrder();
			}
		});
		return modules;
	}

	public static void registered(final IModuleContext context) {
		if (context == null) {
			return;
		}
		final Module module = context.getModule();
		if (!module.isDisabled()) {
			final String key = module.getName();
			final IModuleContext context2 = moduleCache.get(key);
			if (context2 == null || context2.getClass().isAssignableFrom(context.getClass())) {
				moduleCache.put(key, context);
			}
		}
	}

	public static void doInit(final IApplicationContext application) throws Exception {
		final String[] packageNames = application.getScanPackageNames();
		if (packageNames == null) {
			return;
		}

		System.out.println();
		ObjectEx.oprintln($m("ModuleContextFactory.0"));
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
				@Override
				public void doResources(final String filepath, final boolean isDirectory)
						throws Exception {
					final IModuleContext ctx = getInstance(loadClass(filepath), IModuleContext.class);
					if (ctx != null) {
						try {
							ctx.onCreated(application);
						} catch (final Exception e) {
							throw ModuleContextException.of(e);
						}
						registered(ctx);
					}
				}
			});
		}

		// 注册EntityTable
		DbUtils.doEntityTable(application);

		for (final IModuleContext ctx : allModules()) {
			try {
				ctx.onInit(application);
			} catch (final Exception e) {
				throw ModuleContextException.of(e);
			}
			final Module module = ctx.getModule();
			oprintln($m("ModuleContextFactory.1", module.getOrder(), module.getText(),
					module.getName(), ctx.getClass().getName()));
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					for (final IModuleContext ctx : allModules()) {
						ctx.onShutdown(application);
					}
				} catch (final Exception e) {
					log.warn(e);
					throw ModuleContextException.of(e);
				}
			}
		});
	}

	private static final Log log = LogFactory.getLogger(ModuleContextFactory.class);
}
