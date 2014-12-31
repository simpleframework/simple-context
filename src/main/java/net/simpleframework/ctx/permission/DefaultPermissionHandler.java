package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.common.th.NotImplementedException;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultPermissionHandler extends ObjectEx implements IPermissionHandler {

	private static PermissionUser NULL_USER = new PermissionUser();

	@Override
	public PermissionUser getUser(final Object user) {
		return NULL_USER;
	}

	@Override
	public Iterator<ID> users(final Object role, final ID deptId, final Map<String, Object> variables) {
		throw NotImplementedException.of(getClass(), "users");
	}

	private static PermissionRole NULL_ROLE = new PermissionRole();

	@Override
	public PermissionRole getRole(final Object role) {
		return NULL_ROLE;
	}

	private static PermissionDept NULL_DEPT = new PermissionDept();

	@Override
	public PermissionDept getDept(final Object dept) {
		return NULL_DEPT;
	}

	@Override
	public Iterator<ID> roles(final Object user, final Map<String, Object> variables) {
		throw NotImplementedException.of(getClass(), "roles");
	}
}
