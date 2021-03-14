package net.simpleframework.ctx.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
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
	public void schedule(final int initialDelay, final ExecutorRunnable task) {
		getExecutorService().schedule(task, initialDelay, TimeUnit.SECONDS);
	}

	@Override
	public void execute(final ExecutorRunnable task) {
		getExecutorService().execute(task);
	}

	@Override
	public void addScheduledTask(final int initialDelay, final int period,
			final ExecutorRunnable task) {
		final String taskname = task.getTaskname();
		Collection<ScheduledTask> coll = scheduledTasksCache.get(taskname);
		if (coll == null) {
			scheduledTasksCache.put(taskname, coll = new ArrayList<>());
		}

		coll.add(new ScheduledTask(task,
				getExecutorService().scheduleAtFixedRate(
						task.setPeriod(period).setInitialDelay(initialDelay), initialDelay, period,
						TimeUnit.SECONDS)));
		oprintln(task);
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
		final Collection<ScheduledTask> coll = scheduledTasksCache.remove(task.getTaskname());
		if (coll != null) {
			for (final ScheduledTask stask : coll) {
				stask.future.cancel(false);
			}
		}
	}

	private final Map<String, Collection<ScheduledTask>> scheduledTasksCache;
	{
		scheduledTasksCache = new ConcurrentHashMap<>();
	}

	@Override
	public Map<String, Collection<ScheduledTask>> getScheduledTasksCache() {
		return scheduledTasksCache;
	}

	@Override
	public void close() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}
}
