package net.simpleframework.ctx;

import java.util.Collection;

import javax.sql.DataSource;

import net.simpleframework.ado.IADOManagerFactory;
import net.simpleframework.ctx.settings.ContextSettings;
import net.simpleframework.ctx.task.ITaskExecutor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IApplicationContext extends IApplicationContextBase {

	/**
	 * 获取数据源
	 * 
	 * @param key
	 * @return
	 */
	DataSource getDataSource(String key);

	DataSource getDataSource();

	/**
	 * 获取上下文环境的配置信息
	 * 
	 * @return
	 */
	ContextSettings getContextSettings();

	/**
	 * 由类获取模块实例
	 * 
	 * @param mClass
	 * @return
	 */
	<T extends IModuleContext> T getModuleContext(Class<T> mClass);

	/**
	 * 由名称获取模块实例
	 * 
	 * @param module
	 * @return
	 */
	IModuleContext getModuleContext(String module);

	Collection<IModuleContext> allModules();

	/**
	 * 创建缺省的ADO工厂对象,ctx模块可有自己的实现
	 * 
	 * @param dataSource
	 * @return
	 */
	IADOManagerFactory getADOManagerFactory(DataSource dataSource);

	IADOManagerFactory getADOManagerFactory(String key);

	IADOManagerFactory getADOManagerFactory();

	/**
	 * 任务池
	 * 
	 * @return
	 */
	ITaskExecutor getTaskExecutor();

	/**
	 * 获取应用所在的域
	 * 
	 * @return
	 */
	int getDomain();
}
