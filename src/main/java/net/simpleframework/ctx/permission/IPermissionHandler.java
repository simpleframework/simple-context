package net.simpleframework.ctx.permission;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IPermissionHandler extends IPermissionConst {

	/**
	 * 得到权限用户对象
	 * 
	 * @param user
	 * @return
	 */
	PermissionUser getUser(Object user);

	/**
	 * 获取指定角色名的所有用户
	 * 
	 * @param role
	 * @param deptId
	 * @param variables
	 * @return
	 */
	Iterator<ID> users(Object role, ID deptId, Map<String, Object> variables);

	/**
	 * 得到权限角色对象
	 * 
	 * @param role
	 * @return
	 */
	PermissionRole getRole(Object role);

	/**
	 * 获取指定用户的所有角色
	 * 
	 * @param user
	 * @param variables
	 * @return
	 */
	Iterator<ID> roles(Object user, Map<String, Object> variables);
}
