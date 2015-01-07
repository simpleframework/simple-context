package net.simpleframework.ctx;

import net.simpleframework.common.Version;
import net.simpleframework.ctx.permission.IPermissionConst;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Module extends AbstractModule<Module> {
	/* 模块的缺省功能 */
	private ModuleFunction defaultFunction;

	private Version version;

	/* 定义模块使用者的角色名字 */
	private String moduleRole = IPermissionConst.ROLE_ALL_ACCOUNT;
	/* 定义模块管理员的角色名字 */
	private String managerRole = IPermissionConst.ROLE_MANAGER;

	public Module() {
	}

	public ModuleFunction getDefaultFunction() {
		return defaultFunction;
	}

	public Module setDefaultFunction(final ModuleFunction defaultFunction) {
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

	public String getModuleRole() {
		return moduleRole;
	}

	public Module setModuleRole(final String moduleRole) {
		this.moduleRole = moduleRole;
		return this;
	}

	public String getManagerRole() {
		return managerRole;
	}

	public Module setManagerRole(final String managerRole) {
		this.managerRole = managerRole;
		return this;
	}

	private static Version DEFAULT_VERSION = new Version(1, 0, 0);

	private static final long serialVersionUID = -1782660713880740440L;
}
