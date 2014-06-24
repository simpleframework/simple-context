package net.simpleframework.ctx.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.simpleframework.common.NumberUtils;
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
	public void addScheduledTask(final long initialDelay, final long period,
			final ExecutorRunnable task) {
		final String taskname = task.getTaskname();
		Collection<ScheduledFuture<?>> coll = scheduledTasksCache.get(taskname);
		if (coll == null) {
			scheduledTasksCache.put(taskname, coll = new ArrayList<ScheduledFuture<?>>());
		}
		coll.add(getExecutorService().scheduleAtFixedRate(task.setPeriod(period), initialDelay,
				period, TimeUnit.SECONDS));
	}

	@Override
	public void addScheduledTask(final long period, final ExecutorRunnable task) {
		addScheduledTask(NumberUtils.randomInt(0, 60 * 30), period, task);
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
