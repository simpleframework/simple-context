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
public interface IPermissionHandler {

	/**
	 * 得到权限用户对象
	 * 
	 * @param user
	 * @return
	 */
	PermissionUser getUser(Object user);

	/**
	 * 获取权限部门对象
	 * 
	 * @param dept
	 * @return
	 */
	PermissionDept getDept(Object dept);

	/**
	 * 得到权限角色对象
	 * 
	 * @param role
	 * @return
	 */
	PermissionRole getRole(Object role, Map<String, Object> variables);

	PermissionRole getRole(Object role);

	/**
	 * 获取指定角色名的所有用户
	 * 
	 * @param role
	 * @param deptId
	 * @param variables
	 * @return
	 */
	Iterator<PermissionUser> users(Object role, ID deptId, Map<String, Object> variables);

	Iterator<PermissionUser> users(Object role, Map<String, Object> variables);
}
