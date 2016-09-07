package net.simpleframework.ctx.task;

import static net.simpleframework.common.I18n.$m;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ExecutorRunnableEx extends ExecutorRunnable {

	public ExecutorRunnableEx(final String taskname, final String tasktext) {
		super(taskname, tasktext);
	}

	public ExecutorRunnableEx(final String taskname) {
		this(taskname, null);
	}

	@Override
	public int getPeriod() {
		final String period = settings.getProperty("task." + getTaskname() + ".period");
		if (StringUtils.hasText(period)) {
			return Convert.toInt(period);
		}
		return super.getPeriod();
	}

	@Override
	protected boolean isRun(final Map<String, Object> cache) throws Exception {
		final String disabled = settings.getProperty("task." + getTaskname() + ".disabled");
		if (StringUtils.hasText(disabled)) {
			return !Convert.toBool(disabled);
		}
		return super.isRun(cache);
	}

	@Override
	protected void prints(final Map<String, Object> cache, final Date n) {
		final boolean disabled = Convert.toBool(settings.getProperty("task." + getTaskname()
				+ ".print.disabled"));
		if (disabled) {
			cache.put("disabled", Boolean.TRUE);
			return;
		}
		System.out.println();
		System.out.println("======TASK START======");
		final StringBuilder sb = new StringBuilder("[").append(Convert.toDateString(n, "HH:mm:ss"))
				.append("] ").append(getTaskname()).append(" - ").append(getTasktext());
		final long period = getPeriod();
		if (period > 0) {
			sb.append(" - ").append($m("ExecutorRunnableEx.0")).append(period).append("s");
		}
		System.out.println(sb);
	}

	@Override
	protected void printe(final Map<String, Object> cache, final Date n) {
		if (cache.get("disabled") != null) {
			return;
		}
		final Date n2 = new Date();
		final StringBuilder sb = new StringBuilder("[").append(Convert.toDateString(n2, "HH:mm:ss"))
				.append("] ").append($m("ExecutorRunnableEx.1")).append(n2.getTime() - n.getTime())
				.append("ms");
		final long period = getPeriod();
		if (period > 0) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, Long.valueOf(period).intValue());
			sb.append(", ").append($m("ExecutorRunnableEx.2"))
					.append(Convert.toDateString(cal.getTime(), "HH:mm:ss"))
					.append($m("ExecutorRunnableEx.3"));
		}
		System.out.println(sb);
		System.out.println("======TASK END========");
	}
}
