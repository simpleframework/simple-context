package net.simpleframework.ctx.script;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IScriptEval {

	/**
	 * 定义脚本中的变量
	 * 
	 * @param key
	 * @param value
	 */
	void putVariable(String key, Object value);

	/**
	 * 执行脚本,并返回
	 * 
	 * @param script
	 * @return
	 */
	Object eval(String script);
}
