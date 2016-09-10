package net.simpleframework.ctx.service;

import net.simpleframework.ctx.AbstractModuleContextAware;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.task.ITaskExecutor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractBaseService extends AbstractModuleContextAware
		implements IBaseService {

	@Override
	public void onInit() throws Exception {
	}

	protected IApplicationContext getApplicationContext() {
		return getModuleContext().getApplicationContext();
	}

	protected IPermissionHandler getPermission() {
		return getModuleContext().getPermission();
	}

	protected ITaskExecutor getTaskExecutor() {
		return getModuleContext().getTaskExecutor();
	}
}
