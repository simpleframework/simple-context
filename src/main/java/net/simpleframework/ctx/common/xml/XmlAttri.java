package net.simpleframework.ctx.common.xml;

import org.w3c.dom.Attr;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlAttri {
	private final Attr _attr;

	public XmlAttri(final Attr attr) {
		this._attr = attr;
	}

	public Attr getAttr() {
		return _attr;
	}

	public String getName() {
		return _attr.getName();
	}

	public String getValue() {
		return _attr.getValue();
	}

	public void setValue(final String value) {
		_attr.setValue(value);
	}
}
