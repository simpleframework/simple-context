package net.simpleframework.ctx.service.ado.db;

import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ctx.service.ado.IADOModuleContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IDbModuleContext extends IADOModuleContext {

	/**
	 * 获取ADO模块的查询服务
	 * 
	 * @return
	 */
	IDbQueryManager getQueryManager();

	/**
	 * 获取ADO模块的实体bean服务
	 * 
	 * @param beanClass
	 * @return
	 */
	<M> IDbEntityManager<M> getEntityManager(Class<M> beanClass);
}
