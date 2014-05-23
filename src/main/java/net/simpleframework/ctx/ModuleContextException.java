package net.simpleframework.ctx;

import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleContextException extends RuntimeExceptionEx {

	public ModuleContextException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static ModuleContextException of(final String msg) {
		return _of(ModuleContextException.class, msg);
	}

	public static ModuleContextException of(final Throwable throwable) {
		return _of(ModuleContextException.class, null, throwable);
	}

	private static final long serialVersionUID = 4778304178451841308L;
}
