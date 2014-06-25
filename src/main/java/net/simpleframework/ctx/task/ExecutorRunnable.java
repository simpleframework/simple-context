package net.simpleframework.ctx.task;

import java.util.Calendar;
import java.util.Date;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ExecutorRunnable extends ObjectEx implements Runnable {

	protected abstract void task() throws Exception;

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
		return tasktext;
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

	@Override
	public void run() {
		try {
			final Date n = new Date();
			final long l1 = n.getTime();
			task();
			System.out.println("====================================================================");
			System.out.println("= " + Convert.toDateString(n, "yyyy-MM-dd HH:mm:ss") + " ["
					+ (System.currentTimeMillis() - l1) + "ms] - Task: [" + getTaskname() + "] "
					+ StringUtils.blank(getTasktext()));
			final long period = getPeriod();
			if (period > 0) {
				final Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, Long.valueOf(period).intValue());
				System.out.println("= " + Convert.toDateString(cal.getTime(), "yyyy-MM-dd HH:mm:ss")
						+ " [" + period + "s] - run again.");
			}
			System.out.println("====================================================================");
		} catch (final Throwable ex) {
			log.warn(ex);
		}
	}
}
