package net.simpleframework.ctx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IDataRowCallback<T> {

	/**
	 * 处理 DataQuery 的每一行
	 * 
	 * @param t
	 * @throws Exception
	 */
	void doRow(T t) throws Exception;
}
