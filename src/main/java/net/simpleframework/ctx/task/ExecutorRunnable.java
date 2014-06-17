package net.simpleframework.ctx.task;

import java.util.Date;

import net.simpleframework.common.Convert;
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
			final Date n = new Date();
			final long l1 = n.getTime();
			System.out.println("[" + Convert.toDateString(n, "yyyy-MM-dd HH:mm:ss") + "] - ["
					+ getTaskname() + "] - task start.");
			task();
			System.out.println("[" + (System.currentTimeMillis() - l1) + "ms] - task end.");
		} catch (final Throwable ex) {
			log.warn(ex);
		}
	}
}
