package net.simpleframework.ctx;

import static net.simpleframework.common.I18n.$m;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.ScanClassResourcesCallback;
import net.simpleframework.common.I18n;
import net.simpleframework.common.Version;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.th.ThrowableUtils;
import net.simpleframework.ctx.hdl.IApplicationStartupHandler;
import net.simpleframework.ctx.hdl.IScanHandlerAware;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.permission.PermissionFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractApplicationContextBase extends ObjectEx
		implements IApplicationContextBase {

	@Override
	public void onInit() throws Exception {
		ApplicationContextFactory.get().setApplicationContextBase(this);

		// i18n
		final String[] packageNames = getScanPackageNames();
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, I18n.getBasenamesCallback());
		}

		// 初始化应用程序
		onBeforeInit();
		// 初始化资源
		doInternalInit(packageNames);
		// 初始化应用程序
		onAfterInit();

		// 处理扫描类
		oprintln();
		oprintln($m("AbstractApplicationContextBase.0"));
		final IApplicationContext application = (IApplicationContext) this;
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
				@Override
				public void doResources(final String filepath, final boolean isDirectory)
						throws Exception {
					final IScanHandlerAware hAware = getInstance(loadClass(filepath),
							IScanHandlerAware.class);
					if (hAware != null) {
						hAware.onScan(application);
						oprintln("[" + $m("AbstractApplicationContextBase.1") + "] " + hAware);
					}
				}
			});
		}
	}

	protected void onBeforeInit() throws Exception {
	}

	protected void onAfterInit() throws Exception {
	}

	protected void doInternalInit(final String[] packageNames) throws Exception {
		final IApplicationContext application = (IApplicationContext) this;
		// 处理扫描类
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, new ScanClassResourcesCallback() {
				@Override
				public void doResources(final String filepath, final boolean isDirectory)
						throws Exception {
					final IApplicationStartupHandler sHandler = getInstance(loadClass(filepath),
							IApplicationStartupHandler.class);
					if (sHandler != null) {
						sHandler.onStartup(application);
					}
				}
			});
		}

		// 设置权限的实现类
		PermissionFactory.set(getPagePermissionHandler().getName());
	}

	@Override
	public IPermissionHandler getPermission() {
		return PermissionFactory.get();
	}

	protected abstract Class<? extends IPermissionHandler> getPagePermissionHandler();

	private String[] scanPackageNames;

	@Override
	public String[] getScanPackageNames() {
		final Set<String> set = new LinkedHashSet<>();
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
			msgs = new HashMap<>();
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
			version = Version.getVersion("4.0.0");
		}
		return version;
	}
}
