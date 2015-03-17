package net.simpleframework.ctx;

import net.simpleframework.common.Version;
import net.simpleframework.ctx.permission.PermissionConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Module extends AbstractModule<Module> {
	/* 模块的缺省功能 */
	private String defaultFunction;

	private Version version;

	public Module() {
		setRole(PermissionConst.ROLE_ALL_ACCOUNT);
		setManagerRole(PermissionConst.ROLE_MANAGER);
	}

	public String getDefaultFunction() {
		return defaultFunction;
	}

	public Module setDefaultFunction(final String defaultFunction) {
		this.defaultFunction = defaultFunction;
		return this;
	}

	public Version getVersion() {
		return version != null ? version : DEFAULT_VERSION;
	}

	public Module setVersion(final Version version) {
		this.version = version;
		return this;
	}

	private static Version DEFAULT_VERSION = new Version(1, 0, 0);
}
