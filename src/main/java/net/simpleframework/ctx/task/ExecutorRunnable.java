package net.simpleframework.ctx.task;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ExecutorRunnable extends ObjectEx implements Runnable {

	protected abstract void task() throws Exception;

	protected String getTaskname() {
		return getClass().getName();
	}

	@Override
	public void run() {
		try {
			task();
		} catch (final Exception ex) {
			log.warn(ex);
		}
	}
}
