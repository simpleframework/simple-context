package net.simpleframework.ctx.task;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITaskExecutor {

	/**
	 * 执行任务
	 * 
	 * @param task
	 */
	void execute(ExecutorRunnable task);

	void addScheduledTask(long period, ExecutorRunnable task);

	/**
	 * 
	 * @param initialDelay
	 *           延迟
	 * @param period
	 *           周期,秒
	 * @param task
	 */
	void addScheduledTask(long initialDelay, long period, ExecutorRunnable task);

	void removeScheduledTask(ExecutorRunnable task);

	void close();
}
