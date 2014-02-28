package net.simpleframework.ctx;

import java.io.Serializable;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.IObjectOrderAware;
import net.simpleframework.common.object.TextNamedObject;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.permission.LoginUser;
import net.simpleframework.ctx.permission.PermissionFactory;
import net.simpleframework.ctx.permission.PermissionUser;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings({ "unchecked", "serial" })
public abstract class AbstractModule<T extends AbstractModule<T>> extends TextNamedObject<T>
		implements IObjectOrderAware, Serializable {

	/* 功能描述 */
	private String description;

	/* 排序 */
	private int order;

	/* 是否有效 */
	private boolean disabled;

	@Override
	public String getText() {
		final String txt = super.getText();
		return StringUtils.hasText(txt) ? txt : getName();
	}

	public String getDescription() {
		return description;
	}

	public T setDescription(final String description) {
		this.description = description;
		return (T) this;
	}

	@Override
	public int getOrder() {
		return order;
	}

	public T setOrder(final int order) {
		this.order = order;
		return (T) this;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public T setDisabled(final boolean disabled) {
		this.disabled = disabled;
		return (T) this;
	}

	/**
	 * 获取当前的登录用户
	 * 
	 * @return
	 */
	public PermissionUser getLogin() {
		return LoginUser.get().getUser();
	}

	/**
	 * 获取权限对象
	 * 
	 * @return
	 */
	public IPermissionHandler getPermission() {
		return PermissionFactory.get();
	}
}
