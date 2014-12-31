package net.simpleframework.ctx.permission;

import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PermissionDept extends PermissionEntity<PermissionDept> {
	/* 域id */
	private ID domainId;

	public ID getDomainId() {
		return domainId;
	}

	public PermissionDept setDomainId(final ID domainId) {
		this.domainId = domainId;
		return this;
	}

	public String getDomainText() {
		return null;
	}

	private static final long serialVersionUID = -7302087646469559706L;
}
