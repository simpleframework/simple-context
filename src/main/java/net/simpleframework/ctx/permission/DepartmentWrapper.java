package net.simpleframework.ctx.permission;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("rawtypes")
public class DepartmentWrapper {
	private Collection users;

	private Collection<DepartmentWrapper> children;

	private final ID id;

	private final Object dept;

	public DepartmentWrapper(final Object dept) {
		this((ID) BeanUtils.getProperty(dept, "id"), dept);
	}

	public DepartmentWrapper(final ID id, final Object dept) {
		this.id = id;
		this.dept = dept;
	}

	public ID getId() {
		return id;
	}

	public Object getDept() {
		return dept;
	}

	public Collection<DepartmentWrapper> getChildren() {
		if (children == null) {
			children = new ArrayList<DepartmentWrapper>();
		}
		return children;
	}

	public Collection getUsers() {
		if (users == null) {
			users = new ArrayList();
		}
		return users;
	}

	public boolean hasUser() {
		if (getUsers().size() > 0) {
			return true;
		}
		for (final DepartmentWrapper wrapper : getChildren()) {
			if (wrapper.hasUser()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return dept.toString();
	}
}
