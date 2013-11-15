package net.simpleframework.ctx;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.IScanResourcesCallback;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.object.ObjectFactory;
import net.simpleframework.common.object.ObjectFactory.IObjectCreator;
import net.simpleframework.common.object.ObjectFactory.IObjectCreatorListener;
import net.simpleframework.common.object.ObjectInstanceException;
import net.simpleframework.common.object.ProxyUtils;
import net.simpleframework.common.th.ClassException;
import net.simpleframework.ctx.common.ConsoleThread;
import net.simpleframework.ctx.common.IDataImportHandler;
import net.simpleframework.ctx.common.db.DeploySqlUtils;
import net.simpleframework.ctx.service.IBaseService;
import net.simpleframework.ctx.trans.TransactionUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleContextFactory {
	private static final Log log = LogFactory.getLogger(ModuleContextFactory.class);

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
		// 定义代理对象
		ObjectFactory.get().set(new IObjectCreator() {
			@Override
			public Object create(final Class<?> oClass) {
				return ProxyUtils.create(oClass);
			}
		}).addListener(new IObjectCreatorListener() {
			@Override
			public void onBefore(final Class<?> oClass) {
				ProxyUtils.registDefaults(oClass);
				TransactionUtils.registTransaction(oClass);
			}

			@Override
			public void onCreated(final Object o) {
				final Field[] allFields = ClassUtils.getAllFields(o.getClass());

				doInjectCtx(o, allFields);

				if (o instanceof IBaseService) {
					try {
						((IBaseService) o).onInit();
					} catch (final Exception e) {
						throw ObjectInstanceException.of(e);
					}
				}
			}
		});

		DeploySqlUtils.executeSqlScript(application);

		ContextUtils.scanModuleContext(application);

		final IScanResourcesCallback callback = new ScanClassResourcesCallback() {
			@Override
			public void doResources(final String filepath, final boolean isDirectory) throws Exception {
				final IDataImportHandler hdl = newInstance(loadClass(filepath),
						IDataImportHandler.class);
				if (hdl != null) {
					hdl.doImport(application);
				}
			}
		};

		for (final String packageName : application.getScanPackageNames()) {
			ClassUtils.scanResources(packageName, callback);
		}

		ConsoleThread.doInit();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					for (final IModuleContext ctx : allModules()) {
						ctx.onShutdown(application);
					}
				} catch (final Exception e) {
					log.warn(e);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private static void doInjectCtx(final Object o, final Field[] allFields) {
		for (final Field f : allFields) {
			Class<?> mType;
			if (f.getAnnotation(InjectCtx.class) != null
					&& IModuleContext.class.isAssignableFrom(mType = f.getType())) {
				f.setAccessible(true);
				try {
					final IModuleContext ctx = get((Class<? extends IModuleContext>) mType);
					f.set(o, ctx);
					if (o instanceof IBaseService) {
						((IBaseService) o).setModuleContext(ctx);
					}
				} catch (final Exception e) {
					throw ClassException.of(e);
				}
			}
		}
	}
}
