package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.List;

import net.simpleframework.common.ID;
import net.simpleframework.common.th.NotImplementedException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionDept extends PermissionEntity<PermissionDept> {
	public static PermissionDept NULL_DEPT = new PermissionDept();

	/* 用户数 */
	public int getUsers() {
		return 0;
	}

	public Iterator<PermissionUser> users() {
		throw NotImplementedException.of(getClass(), "users");
	}

	public ID getDomainId() {
		return null;
	}

	public String getDomainText() {
		return null;
	}

	public boolean isOrg() {
		return false;
	}

	public ID getParentId() {
		return null;
	}

	public List<PermissionDept> getAllChildren() {
		throw NotImplementedException.of(getClass(), "getAllChildren");
	}

	public List<PermissionDept> getChildren() {
		throw NotImplementedException.of(getClass(), "getChildren");
	}

	public List<PermissionDept> getOrgChildren() {
		throw NotImplementedException.of(getClass(), "getOrgChildren");
	}

	private static final long serialVersionUID = -7302087646469559706L;
}
