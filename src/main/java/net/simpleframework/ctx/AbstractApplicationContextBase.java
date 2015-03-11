package net.simpleframework.ctx;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.IScanResourcesCallback;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.I18n;
import net.simpleframework.common.Version;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.th.ThrowableUtils;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.permission.PermissionFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractApplicationContextBase extends ObjectEx implements
		IApplicationContextBase {

	@Override
	public void onInit() throws Exception {
		ApplicationContextFactory.get().setApplicationContextBase(this);

		// 初始化app的配置信息
		if (this instanceof IApplicationContext) {
			final IApplicationContext ctx = (IApplicationContext) this;
			ctx.getContextSettings().onInit(ctx);
		}

		// 设置权限的实现类，子类需要覆盖
		PermissionFactory.set(getPagePermissionHandler().getName());

		// 初始化资源
		doScanResources(getScanPackageNames());

		// 初始化应用程序
		onApplicationInit();
	}

	protected void onApplicationInit() throws Exception {
		if (this instanceof IApplicationContext) {
			ContextUtils.doInit((IApplicationContext) this);
		}
	}

	@Override
	public IPermissionHandler getPermission() {
		return PermissionFactory.get();
	}

	protected abstract Class<? extends IPermissionHandler> getPagePermissionHandler();

	protected void doScanResources(final String[] packageNames) throws Exception {
		// i18n
		final IScanResourcesCallback i18nCallback = I18n.getBasenamesCallback();
		// 启动类
		final IApplicationContext application = (IApplicationContext) this;
		final IScanResourcesCallback startupCallback = new ScanClassResourcesCallback() {
			@Override
			public void doResources(final String filepath, final boolean isDirectory) throws Exception {
				final IApplicationStartup startupHdl = getInstance(loadClass(filepath),
						IApplicationStartup.class);
				if (startupHdl != null) {
					startupHdl.onStartup(application);
				}
			}
		};

		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, i18nCallback);
			ClassUtils.scanResources(packageName, startupCallback);
		}
	}

	private String[] scanPackageNames;

	@Override
	public String[] getScanPackageNames() {
		final Set<String> set = new LinkedHashSet<String>();
		set.add("net.simpleframework");
		if (scanPackageNames != null) {
			for (final String packageName : scanPackageNames) {
				set.add(packageName);
			}
		}
		return set.toArray(new String[set.size()]);
	}

	@Override
	public void setScanPackageNames(final String[] scanPackageNames) {
		this.scanPackageNames = scanPackageNames;
	}

	@Override
	public String getThrowableMessage(final Throwable th) {
		return ThrowableUtils.getThrowableMessage(th, msgs, true);
	}

	private Map<Class<? extends Throwable>, String> msgs;

	protected void addThrowableMessage(final Class<? extends Throwable> cls, final String message) {
		if (msgs == null) {
			msgs = new HashMap<Class<? extends Throwable>, String>();
		}
		if (message != null) {
			msgs.put(cls, message);
		}
	}

	@Override
	public String getTitle() {
		return "simpleframework.net";
	}

	private Version version;

	@Override
	public Version getVersion() {
		if (version == null) {
			version = Version.getVersion("4.0");
		}
		return version;
	}
}
