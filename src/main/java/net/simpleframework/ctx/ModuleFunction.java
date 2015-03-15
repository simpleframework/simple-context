package net.simpleframework.ctx;

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
}
