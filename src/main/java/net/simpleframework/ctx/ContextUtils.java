package net.simpleframework.ctx;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContextUtils {

	public static void scanModuleContext(final IApplicationContext application) throws Exception {
		scanModuleContext(application, null);
	}

	public static void scanModuleContext(final IApplicationContext application,
			final IModuleContextCallback callback) throws Exception {
		final String[] packageNames = application.getScanPackageNames();
		if (packageNames == null || packageNames.length == 0) {
			return;
		}
		System.out.println($m("ContextUtils.0"));
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
				@Override
				public void doResources(final String filepath, final boolean isDirectory)
						throws Exception {
					final IModuleContext ctx = newInstance(loadClass(filepath), IModuleContext.class);
					if (ctx != null) {
						ctx.onCreated(application);
						ModuleContextFactory.registered(ctx);
					}
				}
			});
		}

		for (final IModuleContext ctx : ModuleContextFactory.allModules()) {
			if (callback != null) {
				callback.doModuleContext(ctx);
			}
			ctx.onInit(application);
			final Module module = ctx.getModule();
			System.out.println($m("ContextUtils.1", module.getText(), module.getName(),
					module.getOrder()));
		}
	}
}
