package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.List;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.CollectionUtils;

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
		return CollectionUtils.EMPTY_ITERATOR();
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
		return CollectionUtils.EMPTY_LIST();
	}

	public List<PermissionDept> getChildren() {
		return CollectionUtils.EMPTY_LIST();
	}

	public List<PermissionDept> getOrgChildren() {
		return CollectionUtils.EMPTY_LIST();
	}

	private static final long serialVersionUID = -7302087646469559706L;
}
