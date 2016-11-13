package net.simpleframework.ctx;

import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.settings.ContextSettings;
import net.simpleframework.ctx.task.ITaskExecutor;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractModuleContext extends ObjectEx implements IModuleContext {

	private ContextSettings settings;

	protected IApplicationContext application;

	private DataSource dataSource;

	private ITaskExecutor taskExecutor;

	@Override
	public void onCreated(final IApplicationContext application) throws Exception {
		this.application = application;
	}

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		// 初始化IModuleRef
		ModuleRefUtils.doRefInit(this);
	}

	private Module module;

	protected Module createModule() {
		final AbstractModuleContext ctx = this;
		return new Module() {
			@Override
			public String getRole(final KVMap vars) {
				final String role = ctx.getRole(vars);
				return role != null ? role : super.getRole(vars);
			}

			@Override
			public String getManagerRole(final KVMap vars) {
				final String managerRole = ctx.getManagerRole(vars);
				return managerRole != null ? managerRole : super.getManagerRole(vars);
			}
		};
	}

	protected String getRole(final KVMap vars) {
		return null;
	}

	protected String getManagerRole(final KVMap vars) {
		return null;
	}

	@Override
	public Module getModule() {
		if (module == null) {
			module = createModule();
		}
		return module;
	}

	@Override
	public ModuleFunctions getFunctions(final ModuleFunction parent) {
		if (parent == null) {
			return getFunctions();
		}
		return null;
	}

	protected ModuleFunctions getFunctions() {
		return null;
	}

	@Override
	public ModuleFunction getFunctionByName(final String name) {
		return ModuleFunction.getFunctionByName(this, getFunctions(), name);
	}

	@Override
	public IApplicationContext getApplicationContext() {
		return application;
	}

	@Override
	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = application.getDataSource();
		}
		return dataSource;
	}

	@Override
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public ContextSettings getContextSettings() {
		if (settings == null) {
			settings = application.getContextSettings();
		}
		return settings;
	}

	@Override
	public void setContextSettings(final ContextSettings settings) {
		this.settings = settings;
	}

	@Override
	public IPermissionHandler getPermission() {
		return application.getPermission();
	}

	@Override
	public ITaskExecutor getTaskExecutor() {
		if (taskExecutor == null) {
			taskExecutor = application.getTaskExecutor();
		}
		return taskExecutor;
	}

	public void setTaskExecutor(final ITaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public IScriptEval createScriptEval(final Map<String, Object> variables) {
		return application.createScriptEval(variables);
	}

	@Override
	public void onShutdown(final IApplicationContext application) throws Exception {
		getTaskExecutor().close();
	}

	@Override
	public String getDomain() {
		return application.getDomain();
	}

	@Override
	public String toString() {
		return getModule().getText();
	}
}
