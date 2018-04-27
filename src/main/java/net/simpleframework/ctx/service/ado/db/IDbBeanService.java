package net.simpleframework.ctx.service.ado.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.IParamsValue;
import net.simpleframework.ado.db.DbTableColumn;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.ado.IADOBeanService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IDbBeanService<T extends Serializable> extends IADOBeanService<T> {

	/**
	 * 按指定条件查询
	 * 
	 * @param params
	 * @param orderColumns
	 * @return
	 */
	IDataQuery<T> queryByParams(FilterItems params, ColumnData... orderColumns);

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
	 * 更新多个属性
	 * 
	 * @param bean
	 * @param attris
	 */
	void update(T bean, Map<String, Object> attris);

	void update(T bean, String attri, Object val);

	/**
	 * 插入
	 * 
	 * @param bean
	 */
	void insert(T... beans);

	int count(CharSequence expr, Object... params);

	int count();

	Number sum(String column, CharSequence expr, Object... params);

	Number sum(String column);

	Number max(String column, CharSequence expr, Object... params);

	Number max(String column);

	Number avg(String column);

	Number avg(String column, CharSequence expr, Object... params);

	/**
	 * 交换位置
	 * 
	 * @param order
	 * @param beans
	 */
	void exchange(DbTableColumn order, T... beans);

	void exchange(T... beans);

	/**
	 * 获取实体表管理器
	 * 
	 * @return
	 */
	IDbEntityManager<T> getEntityManager();

	boolean inTrans();

	/**
	 * 获取ADO模块的实体bean管理器
	 * 
	 * @param beanClass
	 * @return
	 */
	<M> IDbEntityManager<M> getEntityManager(Class<M> beanClass);

	/**
	 * 获取查询管理器
	 * 
	 * @return
	 */
	IDbQueryManager getQueryManager();

	String getTablename(Class<?> beanClass);

	String getTablename();

	Class<?> getBeanClass();

	// / sql

	T getBean(CharSequence expr, Object... params);

	IDataQuery<T> query(CharSequence expr, Object... params);

	int delete(IParamsValue paramsValue);

	int deleteWith(String expr, Object... params);

	/**
	 * 设置当前查询禁止使用cache，具体看实现
	 */
	void setQueryNoCache();
}
