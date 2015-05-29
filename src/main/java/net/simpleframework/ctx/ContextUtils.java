package net.simpleframework.ctx;

import java.lang.reflect.Field;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.Convert;
import net.simpleframework.common.object.ObjectFactory;
import net.simpleframework.common.object.ObjectFactory.IObjectCreator;
import net.simpleframework.common.object.ObjectFactory.IObjectCreatorListener;
import net.simpleframework.common.object.ObjectInstanceException;
import net.simpleframework.common.object.ProxyUtils;
import net.simpleframework.common.th.ClassException;
import net.simpleframework.ctx.common.ConsoleThread;
import net.simpleframework.ctx.common.IDataImportHandler;
import net.simpleframework.ctx.common.db.DbUtils;
import net.simpleframework.ctx.service.IBaseService;
import net.simpleframework.ctx.settings.IContextSettingsConst;
import net.simpleframework.ctx.trans.TransactionUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContextUtils {

	public static void doInit(final IApplicationContext application) throws Exception {
		// 启动控制台线程
		ConsoleThread.doInit();
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

		// 执行数据库相关初始化工作
		if (Convert.toBool(application.getContextSettings().getProperty(
				IContextSettingsConst.CTX_DEPLOY_DB))) {
			DbUtils.doExecuteSql(application);
		}

		// 模块初始化
		ModuleContextFactory.doInit(application);
		// others
		doUtils(application);
	}

	@SuppressWarnings("unchecked")
	private static void doInjectCtx(final Object o, final Field[] allFields) {
		for (final Field f : allFields) {
			Class<?> mType;
			if (f.getAnnotation(InjectCtx.class) != null
					&& IModuleContext.class.isAssignableFrom(mType = f.getType())) {
				f.setAccessible(true);
				try {
					final IModuleContext ctx = ModuleContextFactory
							.get((Class<? extends IModuleContext>) mType);
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

	public static void doUtils(final IApplicationContext application) throws Exception {
		final String[] packageNames = application.getScanPackageNames();
		if (packageNames != null) {
			for (final String packageName : packageNames) {
				// IDataImportHandler
				ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
					@Override
					public void doResources(final String filepath, final boolean isDirectory)
							throws Exception {
						final IDataImportHandler iHandler = getInstance(loadClass(filepath),
								IDataImportHandler.class);
						if (iHandler != null && iHandler.isEnable()) {
							iHandler.doImport(application);
						}
					}
				});
			}
		}
	}
}
