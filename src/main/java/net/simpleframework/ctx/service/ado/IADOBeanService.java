package net.simpleframework.ctx.service.ado;

import java.io.Serializable;

import net.simpleframework.ado.db.event.IDbEntityListener;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.IBaseService;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IADOBeanService<T extends Serializable> extends IBaseService {
	/**
	 * 由id获取对象
	 * 
	 * @param id
	 * @return
	 */
	T getBean(Object id);

	/**
	 * 创建bean的实例，不执行序列化操作
	 * 
	 * @return
	 */
	T createBean();

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	IDataQuery<T> queryAll();

	void addListener(IDbEntityListener<T> listener);
}
