package net.simpleframework.ctx.permission;

import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PermissionRole extends PermissionEntity<PermissionRole> {

	public static String toUniqueRolename(final String chart, final String role) {
		return chart + ":" + role;
	}

	public static String[] split(final String role) {
		return StringUtils.split(role, ":");
	}

	private static final long serialVersionUID = 8929394091976218058L;
}
