package net.simpleframework.ctx;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleFunction extends AbstractModule<ModuleFunction> {

	protected final IModuleContext moduleContext;

	public ModuleFunction(final IModuleContext moduleContext) {
		this.moduleContext = moduleContext;
	}

	public IModuleContext getModuleContext() {
		return moduleContext;
	}

	@Override
	public String getRole(final KVMap vars) {
		String role = super.getRole(vars);
		if (role == null) {
			role = getModuleContext().getModule().getRole(vars);
		}
		return role;
	}

	@Override
	public String getManagerRole(final KVMap vars) {
		String role = super.getManagerRole(vars);
		if (role == null) {
			role = getModuleContext().getModule().getManagerRole(vars);
		}
		return role;
	}

	private static Map<String, ModuleFunction> namesCache = new ConcurrentHashMap<>();
	private static Set<String> NULL_MODULES = new HashSet<>();

	public static ModuleFunction getFunctionByName(final String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		if ("-".equals(name)) {
			return null;
		}
		ModuleFunction function = namesCache.get(name);
		if (function != null) {
			return function;
		}
		if (!NULL_MODULES.contains(name)) {
			for (final IModuleContext ctx : ModuleContextFactory.allModules()) {
				function = getFunctionByName(ctx, ctx.getFunctions(null), name);
				if (function != null) {
					namesCache.put(name, function);
					return function;
				}
			}
			NULL_MODULES.add(name);
		}
		return null;
	}

	static ModuleFunction getFunctionByName(final IModuleContext ctx,
			final ModuleFunctions functions, final String name) {
		if (functions == null) {
			return null;
		}
		for (final ModuleFunction function : functions) {
			if (name.equals(function.getName())) {
				return function;
			}
			final ModuleFunction function2 = getFunctionByName(ctx, ctx.getFunctions(function), name);
			if (function2 != null) {
				return function2;
			}
		}
		return null;
	}

	public static ModuleFunction getDefaultFunction(final String module) {
		final IModuleContext ctx = ModuleContextFactory.get(module);
		Module oModule;
		if (ctx != null && (oModule = ctx.getModule()) != null) {
			return getFunctionByName(oModule.getDefaultFunction());
		}
		return null;
	}
}
