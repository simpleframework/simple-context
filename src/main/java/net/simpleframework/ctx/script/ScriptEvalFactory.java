package net.simpleframework.ctx.script;

import java.util.Map;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ScriptEvalFactory {

	public static IScriptEval createDefaultScriptEval(final Map<String, Object> variables) {
		return new MVEL2ScriptEval(variables);
	}
}
