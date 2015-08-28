package net.simpleframework.ctx.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TaskExecutor extends ObjectEx implements ITaskExecutor {

	private int threadPoolSize = 20;

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public TaskExecutor setThreadPoolSize(final int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
		return this;
	}

	private ScheduledExecutorService executorService;

	public ScheduledExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newScheduledThreadPool(getThreadPoolSize());
		}
		return executorService;
	}

	@Override
	public void execute(final ExecutorRunnable task) {
		getExecutorService().execute(task);
	}

	@Override
	public void addScheduledTask(final int initialDelay, final int period,
			final ExecutorRunnable task) {
		final String taskname = task.getTaskname();
		Collection<ScheduledFuture<?>> coll = scheduledTasksCache.get(taskname);
		if (coll == null) {
			scheduledTasksCache.put(taskname, coll = new ArrayList<ScheduledFuture<?>>());
		}

		coll.add(getExecutorService().scheduleAtFixedRate(
				task.setPeriod(period).setInitialDelay(initialDelay), initialDelay,
				period > 0 ? period : getDefaultPeriod(), TimeUnit.SECONDS));
	}

	protected int getDefaultPeriod() {
		return 60 * 5;
	}

	@Override
	public void addScheduledTask(final ExecutorRunnable task) {
		addScheduledTask(task.getInitialDelay(), task.getPeriod(), task);
	}

	@Override
	public void removeScheduledTask(final ExecutorRunnable task) {
		if (task == null) {
			return;
		}
		final Collection<ScheduledFuture<?>> coll = scheduledTasksCache.remove(task.getTaskname());
		if (coll != null) {
			for (final ScheduledFuture<?> future : coll) {
				future.cancel(false);
			}
		}
	}

	private final Map<String, Collection<ScheduledFuture<?>>> scheduledTasksCache;
	{
		scheduledTasksCache = new ConcurrentHashMap<String, Collection<ScheduledFuture<?>>>();
	}

	@Override
	public void close() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}
}
