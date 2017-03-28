package net.simpleframework.ctx.permission;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionConst {
	/* 系统管理员帐号 */
	public static final String ADMIN = "admin";

	/* 匿名用户 */
	public static String ROLE_ANONYMOUS;
	/* 正常的注册用户 */
	public static String ROLE_ALL_ACCOUNT;
	/* 已锁定的注册用户 */
	public static String ROLE_LOCK_ACCOUNT;

	/* 系统管理员 */
	public static String ROLE_MANAGER;
	/* 域管理员角色 */
	public static String ROLE_DOMAIN_MANAGER;
	/* 缺省的模块管理员角色 */
	public static String ROLE_MODULE_MANAGER;
	// ROLE_MANAGER > ROLE_ORGANIZATION_MANAGER > ROLE_MODULE_MANAGER

	/* 当前部门用户 */
	public static String ROLE_INDEPT;
	/* 当前机构用户 */
	public static String ROLE_INORG;

	/* 环境变量key定义 */
	public static final String VAR_DISABLE_MANAGER = "disableManager";
	public static final String VAR_DISABLE_DOMAIN_MANAGER = "disableDomainManager";
	public static final String VAR_USERID = "userId";
	public static final String VAR_ROLEID = "roleId";
	public static final String VAR_DEPTID = "deptId";
	public static final String VAR_ORGID = "orgId";
}
