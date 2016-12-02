package net.simpleframework.ctx.permission;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class PermissionFactory extends ObjectEx {

	private static final IPermissionHandler defaultPermission = new DefaultPermissionHandler();

	private static IPermissionHandler _rolePermission;

	public static IPermissionHandler get() {
		return _rolePermission != null ? _rolePermission : defaultPermission;
	}

	public static void set(final String permissionClass) {
		_rolePermission = (IPermissionHandler) singleton(permissionClass);
	}

	public static void set(final IPermissionHandler rolePermission) {
		_rolePermission = rolePermission;
	}

	public static PermissionUser getAdmin() {
		return get().getUser(PermissionConst.ADMIN);
	}
}
