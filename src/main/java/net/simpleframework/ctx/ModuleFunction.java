package net.simpleframework.ctx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.StringUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
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
	public String getRole() {
		String role = super.getRole();
		if (role == null) {
			role = getModuleContext().getModule().getRole();
		}
		return role;
	}

	@Override
	public String getManagerRole() {
		String role = super.getManagerRole();
		if (role == null) {
			role = getModuleContext().getModule().getManagerRole();
		}
		return role;
	}

	private static Map<String, ModuleFunction> namesCache;
	static {
		namesCache = new ConcurrentHashMap<String, ModuleFunction>();
	}

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
		for (final IModuleContext ctx : ModuleContextFactory.allModules()) {
			function = getFunctionByName(ctx, ctx.getFunctions(null), name);
			if (function == null) {
				final ModuleFunction function2 = getFunctionByName(ctx.getModule().getDefaultFunction());
				if (function2 != null && name.equals(function2.getName())) {
					function = function2;
				}
			}
			if (function != null) {
				namesCache.put(name, function);
				return function;
			}
		}
		return null;
	}

	public static ModuleFunction getFunctionByName(final IModuleContext ctx,
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
