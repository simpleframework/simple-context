package net.simpleframework.ctx;

import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.settings.ContextSettings;
import net.simpleframework.ctx.task.ITaskExecutor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IModuleContext {

	/**
	 * 实例创建后执行
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	void onCreated(IApplicationContext ctx) throws Exception;

	/**
	 * 初始化
	 * 
	 * 异常抛出，不处理
	 * 
	 * @throws Exception
	 */
	void onInit(IApplicationContext ctx) throws Exception;

	/**
	 * 获取配置信息
	 * 
	 * @return
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
	 * 关闭时触发
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	void onShutdown(IApplicationContext application) throws Exception;

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
