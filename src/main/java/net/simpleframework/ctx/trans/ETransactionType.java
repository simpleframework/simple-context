package net.simpleframework.ctx.trans;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum ETransactionType {

	/**
	 * jdbc事务
	 */
	JDBC,

	/**
	 * 分布式事务
	 */
	JTA
}
