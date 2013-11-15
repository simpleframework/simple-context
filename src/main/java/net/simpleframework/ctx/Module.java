package net.simpleframework.ctx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Module extends AbstractModule<Module> {

	/* 模块的缺省功能 */
	private ModuleFunction defaultFunction;

	public Module() {
	}

	public ModuleFunction getDefaultFunction() {
		return defaultFunction;
	}

	public Module setDefaultFunction(final ModuleFunction defaultFunction) {
		this.defaultFunction = defaultFunction;
		return this;
	}

	private static final long serialVersionUID = -1782660713880740440L;
}
