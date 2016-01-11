package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.th.NotImplementedException;

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

	/**
	 * 获取指定角色名的所有用户
	 * 
	 * @param deptId
	 * @param variables
	 * @return
	 */
	public Iterator<ID> users(final ID deptId, final Map<String, Object> variables) {
		throw NotImplementedException.of(getClass(), "users");
	}

	public Iterator<ID> users(final Map<String, Object> variables) {
		return users(null, variables);
	}

	private static final long serialVersionUID = 8929394091976218058L;
}
