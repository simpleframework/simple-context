package net.simpleframework.ctx.permission;

import java.io.Serializable;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings({ "unchecked", "serial" })
public abstract class PermissionEntity<T extends PermissionEntity<T>> extends ObjectEx implements
		Serializable {
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
		return name != null ? name.trim() : null;
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

	public Object getProperty(final String property) {
		return null;
	}

	@Override
	public int hashCode() {
		final ID id = getId();
		return id != null ? id.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final ID id = getId();
		if (id != null && obj instanceof PermissionEntity) {
			return id.equals(((PermissionEntity<?>) obj).getId());
		}
		return super.equals(obj);
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
