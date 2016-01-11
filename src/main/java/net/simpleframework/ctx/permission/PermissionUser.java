package net.simpleframework.ctx.permission;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionUser extends PermissionEntity<PermissionUser> {

	public static PermissionUser NULL_USER = new PermissionUser();

	/* 头像 */
	private InputStream photoStream;

	/* 部门 */
	private PermissionDept dept;

	public InputStream getPhotoStream() {
		return photoStream;
	}

	public PermissionUser setPhotoStream(final InputStream photoStream) {
		this.photoStream = photoStream;
		return this;
	}

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

	public String getDescription() {
		return null;
	}

	public PermissionDept getDept() {
		return dept != null ? dept : PermissionDept.NULL_DEPT;
	}

	public PermissionUser setDept(final PermissionDept dept) {
		this.dept = dept;
		return this;
	}

	public ID getDeptId() {
		return getDept().getId();
	}

	public ID getDomainId() {
		return getDept().getDomainId();
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

	/**
	 * 用户存在多个角色，此函数返回用户的默认角色
	 * 
	 * @param user
	 * @return
	 */
	public ID getRoleId() {
		throw NotImplementedException.of(getClass(), "getRoleId");
	}

	/**
	 * 获取指定用户的所有角色
	 * 
	 * @param variables
	 * @return
	 */
	public Iterator<ID> roles(final Map<String, Object> variables) {
		throw NotImplementedException.of(getClass(), "roles");
	}

	private static final long serialVersionUID = -7880069050882902556L;
}
