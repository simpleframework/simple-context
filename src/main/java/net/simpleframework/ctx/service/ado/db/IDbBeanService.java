package net.simpleframework.ctx.service.ado.db;

import java.util.List;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ado.db.common.TableColumn;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.ado.IADOBeanService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IDbBeanService<T> extends IADOBeanService<T> {

	/**
	 * 按指定条件查询
	 * 
	 * @param params
	 * @param orderColumns
	 * @return
	 */
	IDataQuery<T> queryByParams(FilterItems params, ColumnData... orderColumns);

	IDataQuery<T> queryByParams(FilterItems params);

	List<Object> list(String column);

	/**
	 * 
	 * @param ids
	 * @return
	 */
	int delete(Object... ids);

	/**
	 * 更新
	 * 
	 * @param bean
	 */
	void update(T... beans);

	void update(String[] columns, T... beans);

	/**
	 * 插入
	 * 
	 * @param bean
	 */
	void insert(T... beans);

	int count();

	int sum(String column);

	int max(String column);

	/**
	 * @param bean1
	 * @param bean2
	 * @param order
	 * @param up
	 * @return
	 */
	Object exchange(T bean1, T bean2, TableColumn order, boolean up);

	/**
	 * 获取实体表管理器
	 * 
	 * @return
	 */
	IDbEntityManager<T> getEntityManager();

	<M> IDbEntityManager<M> getEntityManager(Class<M> beanClass);

	/**
	 * 获取查询管理器
	 * 
	 * @return
	 */
	IDbQueryManager getQueryManager();
}
