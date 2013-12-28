package net.simpleframework.ctx;

import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleRefException extends RuntimeExceptionEx {

	public ModuleRefException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static ModuleRefException of(final String msg) {
		return _of(ModuleRefException.class, msg);
	}

	public static ModuleRefException of(final Throwable throwable) {
		return _of(ModuleRefException.class, null, throwable);
	}

	private static final long serialVersionUID = -4892690738644084723L;
}
