package net.simpleframework.ctx;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ApplicationContextFactory extends ObjectEx {

	public static IApplicationContextBase ctx() {
		return get().getApplicationContextBase();
	}

	public static ApplicationContextFactory get() {
		return singleton(ApplicationContextFactory.class);
	}

	private IApplicationContextBase applicationContextBase;

	public IApplicationContextBase getApplicationContextBase() {
		return applicationContextBase;
	}

	public void setApplicationContextBase(final IApplicationContextBase applicationContextBase) {
		this.applicationContextBase = applicationContextBase;
	}
}
