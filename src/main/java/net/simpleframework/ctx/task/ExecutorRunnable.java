package net.simpleframework.ctx.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.common.DateUtils;
import net.simpleframework.common.NumberUtils;
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

	/* 延迟，单位秒 */
	private int initialDelay;
	/* 周期，单位秒 */
	private int period;

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

	public int getPeriod() {
		return period > 0 ? period : 60 * 5;
	}

	public ExecutorRunnable setPeriod(final int period) {
		this.period = period;
		return this;
	}

	public int getInitialDelay() {
		return initialDelay > 0 ? initialDelay : NumberUtils.randomInt(0, getPeriod());
	}

	public ExecutorRunnable setInitialDelay(final int initialDelay) {
		this.initialDelay = initialDelay;
		return this;
	}

	protected int getZeroDelay() {
		return getZeroDelay(NumberUtils.randomInt(0, 60 * 60));
	}

	protected int getZeroDelay(final int delay) {
		final Calendar cal = DateUtils.getZeroPoint();
		cal.add(Calendar.HOUR_OF_DAY, 24);
		return (int) ((cal.getTimeInMillis() - System.currentTimeMillis()) / 1000) + delay;
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
			final Date s = new Date();
			prints(cache, s);
			task(cache);
			printe(cache, s);
		} catch (final Throwable ex) {
			getLog().warn(ex);
		}
	}

	protected void prints(final Map<String, Object> cache, final Date n) {
	}

	protected void printe(final Map<String, Object> cache, final Date n) {
	}

	protected String toTime(final int t) {
		if (t > 3600) {
			return t / 3600 + "h";
		} else if (t > 60) {
			return t / 60 + "m";
		}
		return t + "s";
	}

	@Override
	public String toString() {
		return new StringBuilder("[task: ").append(getTaskname()).append("] ").append(getTasktext())
				.append(", initialDelay: ").append(toTime(initialDelay)).append(", period: ")
				.append(toTime(period)).append(".").toString();
	}
}
