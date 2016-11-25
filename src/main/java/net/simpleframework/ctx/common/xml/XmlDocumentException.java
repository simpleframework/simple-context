package net.simpleframework.ctx.common.xml;

import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlDocumentException extends RuntimeExceptionEx {
	private static final long serialVersionUID = 3959123626087714493L;

	public XmlDocumentException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static XmlDocumentException of(final Throwable throwable) {
		return _of(XmlDocumentException.class, null, throwable);
	}
}