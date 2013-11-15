package net.simpleframework.ctx.permission;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IPermissionConst {
	/**
	 * 系统管理员帐号
	 */
	static final String ADMIN = "admin";

	static final String ROLECHART_SYSTEM = "syschart";

	/**
	 * 匿名用户
	 */
	static final String ROLE_ANONYMOUS = PermissionRole.toUniqueRolename(ROLECHART_SYSTEM,
			"anonymous");

	/**
	 * 正常的注册用户
	 */
	static final String ROLE_ALL_ACCOUNT = PermissionRole.toUniqueRolename(ROLECHART_SYSTEM,
			"account_all");

	/**
	 * 已锁定的注册用户
	 */
	static final String ROLE_LOCK_ACCOUNT = PermissionRole.toUniqueRolename(ROLECHART_SYSTEM,
			"account_lock");

	/**
	 * 系统管理员
	 */
	static final String ROLE_MANAGER = PermissionRole.toUniqueRolename(ROLECHART_SYSTEM, "manager");
}
