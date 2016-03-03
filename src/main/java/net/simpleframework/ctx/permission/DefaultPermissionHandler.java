package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.CollectionUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultPermissionHandler extends ObjectEx implements IPermissionHandler {

	@Override
	public PermissionUser getUser(final Object user) {
		return PermissionUser.NULL_USER;
	}

	public PermissionUser getAdmin() {
		return getUser(PermissionConst.ADMIN);
	}

	@Override
	public PermissionUser getLogin() {
		return LoginUser.get().getUser();
	}

	@Override
	public PermissionRole getRole(final Object role, final Map<String, Object> variables) {
		return PermissionRole.NULL_ROLE;
	}

	@Override
	public PermissionRole getRole(final Object role) {
		return getRole(role, new KVMap());
	}

	@Override
	public PermissionDept getDept(final Object dept) {
		return PermissionDept.NULL_DEPT;
	}

	@Override
	public List<PermissionDept> getRootChildren() {
		return CollectionUtils.EMPTY_LIST();
	}

	/*---------------------------wrapper---------------------------*/

	@Override
	public Iterator<PermissionUser> users(final Object role, final ID deptId,
			final Map<String, Object> variables) {
		return getRole(role, variables).users(deptId, variables);
	}

	@Override
	public Iterator<PermissionUser> users(final Object role, final Map<String, Object> variables) {
		return getRole(role, variables).users(variables);
	}

	@Override
	public Iterator<PermissionUser> allUsers() {
		return CollectionUtils.EMPTY_ITERATOR();
	}
}
