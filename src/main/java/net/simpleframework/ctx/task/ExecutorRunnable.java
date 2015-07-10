package net.simpleframework.ctx.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ExecutorRunnable extends ObjectEx implements Runnable {

	protected abstract void task(Map<String, Object> cache) throws Exception;

	private long period;

	private String taskname, tasktext;

	public ExecutorRunnable() {
	}

	public ExecutorRunnable(final String taskname, final String tasktext) {
		this.taskname = taskname;
		this.tasktext = tasktext;
	}

	public String getTaskname() {
		if (taskname == null) {
			taskname = getClass().getName();
		}
		return taskname;
	}

	public ExecutorRunnable setTaskname(final String taskname) {
		this.taskname = taskname;
		return this;
	}

	public String getTasktext() {
		return StringUtils.text(tasktext, getTaskname());
	}

	public ExecutorRunnable setTasktext(final String tasktext) {
		this.tasktext = tasktext;
		return this;
	}

	public long getPeriod() {
		return period;
	}

	public ExecutorRunnable setPeriod(final long period) {
		this.period = period;
		return this;
	}

	protected boolean isRun(final Map<String, Object> cache) throws Exception {
		return true;
	}

	@Override
	public void run() {
		try {
			final Map<String, Object> cache = new HashMap<String, Object>();
			if (!isRun(cache)) {
				return;
			}
			print(cache, new Date());
			task(cache);
		} catch (final Throwable ex) {
			getLog().warn(ex);
		}
	}

	protected void print(final Map<String, Object> cache, final Date n) {
	}
}
