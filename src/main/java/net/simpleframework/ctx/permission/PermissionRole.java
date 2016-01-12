package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.CollectionUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionRole extends PermissionEntity<PermissionRole> {

	public static PermissionRole NULL_ROLE = new PermissionRole();

	/* 该角色当前的用户所在部门 */
	private PermissionDept _dept;

	public PermissionDept getDept() {
		return _dept != null ? _dept : PermissionDept.NULL_DEPT;
	}

	public PermissionRole setDept(final PermissionDept dept) {
		_dept = dept;
		return this;
	}

	/* 该角色当前的用户 */
	private PermissionUser _user;

	public PermissionUser getUser() {
		return _user != null ? _user : PermissionUser.NULL_USER;
	}

	public PermissionRole setUser(final PermissionUser user) {
		_user = user;
		return this;
	}

	/**
	 * 获取指定角色名的所有用户
	 * 
	 * @param deptId
	 * @param variables
	 * @return
	 */
	public Iterator<PermissionUser> users(final ID deptId, final Map<String, Object> variables) {
		return CollectionUtils.EMPTY_ITERATOR();
	}

	public Iterator<PermissionUser> users(final Map<String, Object> variables) {
		return users(null, variables);
	}

	private static final long serialVersionUID = 8929394091976218058L;
}
