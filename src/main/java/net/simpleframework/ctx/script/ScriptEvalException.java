package net.simpleframework.ctx.script;

import net.simpleframework.common.th.RuntimeExceptionEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ScriptEvalException extends RuntimeExceptionEx {
	public ScriptEvalException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException of(final Throwable throwable) {
		return _of(ScriptEvalException.class, null, throwable);
	}

	private static final long serialVersionUID = -4496578242304451585L;
}