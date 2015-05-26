package net.simpleframework.ctx.service.ado;

import java.util.Collection;
import java.util.Map;

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
	 * @param orderColumns
	 * @return
	 */
	IDataQuery<T> queryChildren(T parent, ColumnData... orderColumns);

	boolean hasChild(T parent);

	/**
	 * 用parentId和集合缓存树结构
	 * 
	 * @return
	 */
	Map<ID, Collection<T>> queryAllTree();

	/**
	 * 获取树节点的层次
	 * 
	 * @param node
	 * @return
	 */
	int getLevel(T node);
}
