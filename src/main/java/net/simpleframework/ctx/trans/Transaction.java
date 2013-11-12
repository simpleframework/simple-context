package net.simpleframework.ctx.trans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.simpleframework.ctx.IModuleContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transaction {

	/**
	 * 
	 * @return
	 */
	ETransactionType type() default ETransactionType.JDBC;

	/**
	 * 获取执行事务的context，context中有不同的数据源
	 * 
	 * @return
	 */
	Class<? extends IModuleContext> context() default IModuleContext.class;
}
