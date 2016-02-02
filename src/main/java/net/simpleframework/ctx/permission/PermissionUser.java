package net.simpleframework.ctx.permission;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.CollectionUtils;
import net.simpleframework.common.coll.KVMap;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionUser extends PermissionEntity<PermissionUser> {

	public static PermissionUser NULL_USER = new PermissionUser();

	public String getEmail() {
		return null;
	}

	public String getMobile() {
		return null;
	}

	public String getSex() {
		return null;
	}

	public Date getBirthday() {
		return null;
	}

	public String getNick() {
		return null;
	}

	public String getDescription() {
		return null;
	}

	public InputStream getPhotoStream() {
		return null;
	}

	public int getOorder() {
		return 0;
	}

	/**
	 * 指定用户是否为某一指定角色的成员
	 * 
	 * @param role
	 * @param variables
	 * @return
	 */
	public boolean isMember(final Object role, final Map<String, Object> variables) {
		return PermissionConst.ROLE_ANONYMOUS.equals(role);
	}

	/**
	 * 指定用户是否为某一指定角色的成员，默认环境变量=null
	 *
	 * @param role
	 * @return
	 */
	public boolean isMember(final Object role) {
		return isMember(role, new KVMap());
	}

	/**
	 * 是否为超级用户
	 * 
	 * @param user
	 * @param variables
	 * @return
	 */
	public boolean isManager(final Map<String, Object> variables) {
		return false;
	}

	public boolean isManager() {
		return isManager(new KVMap());
	}

	public boolean isAdmin() {
		return PermissionConst.ADMIN.equals(getName());
	}

	/* 当前用户的角色 */
	private PermissionRole _role;

	public PermissionRole getRole() {
		return _role != null ? _role : PermissionRole.NULL_ROLE;
	}

	public PermissionUser setRole(final PermissionRole role) {
		_role = role;
		return this;
	}

	/* 当前用户所在部门, 按角色 */
	private PermissionDept _dept;

	public PermissionDept getDept() {
		return _dept != null ? _dept : PermissionDept.NULL_DEPT;
	}

	public PermissionUser setDept(final PermissionDept dept) {
		_dept = dept;
		return this;
	}

	public ID getDomainId() {
		return getDept().getDomainId();
	}

	/**
	 * 获取指定用户的所有角色
	 * 
	 * @param variables
	 * @return
	 */
	public List<PermissionRole> roles(final Map<String, Object> variables) {
		return CollectionUtils.EMPTY_LIST();
	}

	public List<PermissionRole> roles(final ID roleId, final Map<String, Object> variables) {
		if (roleId == null) {
			return roles(variables);
		}
		final ArrayList<PermissionRole> l = new ArrayList<PermissionRole>();
		for (final PermissionRole r : roles(variables)) {
			if (roleId.equals(r.getId())) {
				l.add(r);
			}
		}
		return l;
	}

	private static final long serialVersionUID = -7880069050882902556L;
}
