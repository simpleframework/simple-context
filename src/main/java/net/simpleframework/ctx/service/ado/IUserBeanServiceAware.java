package net.simpleframework.ctx.service.ado;

import net.simpleframework.ado.bean.IUserAwareBean;
import net.simpleframework.ado.query.IDataQuery;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IUserBeanServiceAware<T extends IUserAwareBean> {

	/**
	 * 获取指定用户的beans
	 * 
	 * @param userId
	 * @return
	 */
	IDataQuery<T> queryByUser(Object userId);
}
