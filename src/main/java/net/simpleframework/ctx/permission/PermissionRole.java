package net.simpleframework.ctx.permission;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionRole extends PermissionEntity<PermissionRole> {

	public static PermissionRole NULL_ROLE = new PermissionRole();

	public PermissionDept getDept(final Object user) {
		// 获取该用户在此角色的部门
		return PermissionDept.NULL_DEPT;
	}

	private static final long serialVersionUID = 8929394091976218058L;
}
