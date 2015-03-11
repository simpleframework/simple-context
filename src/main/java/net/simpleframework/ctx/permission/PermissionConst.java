package net.simpleframework.ctx.permission;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
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

	/* 当前用户直接的角色 */
	public static final String CTX_ROLEID = "roleId";
	public static final String CTX_DEPTID = "deptId";
}
