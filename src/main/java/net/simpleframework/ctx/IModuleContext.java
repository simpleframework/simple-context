package net.simpleframework.ctx;

import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.settings.ContextSettings;
import net.simpleframework.ctx.task.ITaskExecutor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IModuleContext {

	/**
	 * 模块实例化时触发
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	void onCreated(IApplicationContext application) throws Exception;

	/**
	 * 模块被应用容器加载时触发，一般情况下，实现此方法即可
	 * 
	 * 异常抛出，不处理
	 * 
	 * @throws Exception
	 */
	void onInit(IApplicationContext application) throws Exception;

	/**
	 * 关闭时触发
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	void onShutdown(IApplicationContext application) throws Exception;

	/**
	 * 获取环境参数的配置
	 * 
	 * @return 单例模式
	 */
	ContextSettings getContextSettings();

	/**
	 * 
	 * @param settings
	 */
	void setContextSettings(ContextSettings settings);

	/**
	 * 获取ApplicationContext对象
	 * 
	 * @return
	 */
	IApplicationContext getApplicationContext();

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	DataSource getDataSource();

	void setDataSource(DataSource dataSource);

	/**
	 * 获取系统管理员的角色名字
	 * 
	 * @return
	 */
	String getManagerRole();

	/**
	 * 获取Module定义
	 * 
	 * @return
	 */
	Module getModule();

	/**
	 * 获取模块的功能列表
	 * 
	 * @param parent
	 * @return
	 */
	ModuleFunctions getFunctions(ModuleFunction parent);

	/**
	 * 任务池
	 * 
	 * @return
	 */
	ITaskExecutor getTaskExecutor();

	IScriptEval createScriptEval(Map<String, Object> variables);

	/**
	 * 获取模块的临时路径
	 * 
	 * @return
	 */
	String getTmpdir();

	/**
	 * 获取模块所在的域
	 * 
	 * @return
	 */
	int getDomain();
}
