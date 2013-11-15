package net.simpleframework.ctx.permission;

import java.io.Serializable;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings({ "unchecked", "serial" })
public abstract class PermissionEntity<T extends PermissionEntity<T>> implements Serializable {
	private ID id;

	private String text = "";

	private String name;

	public ID getId() {
		return id;
	}

	public T setId(final ID id) {
		this.id = id;
		return (T) this;
	}

	public boolean exists() {
		return getId() != null;
	}

	public String getName() {
		return name;
	}

	public T setName(final String name) {
		this.name = name;
		return (T) this;
	}

	public String getText() {
		return text;
	}

	public T setText(final String text) {
		this.text = text;
		return (T) this;
	}

	@Override
	public String toString() {
		final String txt = getText();
		if (!StringUtils.hasText(txt)) {
			return "?";
		}
		return txt;
	}
}
