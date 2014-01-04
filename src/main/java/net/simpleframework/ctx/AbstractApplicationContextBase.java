package net.simpleframework.ctx;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ClassUtils.IScanResourcesCallback;
import net.simpleframework.common.Convert;
import net.simpleframework.common.I18n;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.Version;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.object.ObjectEx;
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

	protected abstract Class<? extends IPermissionHandler> getPagePermissionHandler();

	protected void doScanResources(final String[] packageNames) throws Exception {
		// i18n
		final IScanResourcesCallback callback = I18n.getBasenamesCallback();
		for (final String packageName : packageNames) {
			ClassUtils.scanResources(packageName, callback);
		}
	}

	private static String[] DEFAULT_SCANPACKAGENAMES = new String[] { "net.simpleframework" };
	private String[] scanPackageNames;

	@Override
	public String[] getScanPackageNames() {
		return !ArrayUtils.isEmpty(scanPackageNames) ? scanPackageNames : DEFAULT_SCANPACKAGENAMES;
	}

	@Override
	public void setScanPackageNames(final String[] scanPackageNames) {
		this.scanPackageNames = scanPackageNames;
	}

	@Override
	public String getThrowableMessage(final Throwable th) {
		String message = null;
		Throwable th0 = th;
		while (th0 != null) {
			message = th0.getMessage();
			if (StringUtils.hasText(message)) {
				break;
			}
			th0 = th0.getCause();
		}
		if (!StringUtils.hasText(message)) {
			message = Convert.toString(th);
			int pos = message.indexOf("\r");
			if (pos < 0) {
				pos = message.indexOf("\n");
			}
			if (pos > 0) {
				message = message.substring(0, pos);
			}
		}
		return message.trim();
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
