package net.simpleframework.ctx.service.ado;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ITreeBeanServiceAware<T extends ITreeBeanAware> {

	/**
	 * 获取孩子列表
	 * 
	 * @param parent
	 * @param domainId
	 * @param orderColumns
	 * @return
	 */
	IDataQuery<T> queryChildren(T parent, ID domainId, ColumnData... orderColumns);

	IDataQuery<T> queryChildren(T parent, ColumnData... orderColumns);

	boolean hasChild(T parent);

	/**
	 * 获取树节点的层次
	 * 
	 * @param node
	 * @return
	 */
	int getLevel(T node);
}
