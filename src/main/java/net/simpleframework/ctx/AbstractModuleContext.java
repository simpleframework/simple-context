package net.simpleframework.ctx;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.common.ClassUtils;
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
		for (final Method method : getClass().getMethods()) {
			final Class<?> type = method.getReturnType();
			if (IModuleRef.class.isAssignableFrom(type) && method.getParameterTypes().length == 0) {
				try {
					final IModuleRef ref = ((IModuleRef) method.invoke(this));
					if (ref != null) {
						ref.onInit(this);
					}
				} catch (final Exception e) {
					throw ModuleRefException.of(e);
				}
			}
		}
	}

	private Module module;

	protected abstract Module createModule();

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
		return ModuleFunctions.getFunctionByName(this, getFunctions(), name);
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

	private String tmpdir;

	@Override
	public String getTmpdir() {
		if (tmpdir == null) {
			final StringBuilder sb = new StringBuilder();
			final String fs = File.separator;
			sb.append(getContextSettings().getTmpFiledir().getAbsolutePath());
			sb.append(fs).append(getModule().getName()).append(fs);
			tmpdir = sb.toString();
		}
		return tmpdir;
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

	private final Map<String, Boolean> ERRs = new HashMap<String, Boolean>();

	protected IModuleRef getRef(final String refClass) {
		try {
			return (IModuleRef) singleton(ClassUtils.forName(refClass));
		} catch (final ClassNotFoundException e) {
			logGetRef(refClass, e);
		} catch (final NoClassDefFoundError e) {
			logGetRef(refClass, e);
		}
		return null;
	}

	protected void logGetRef(final String refClass, final Throwable e) {
		if (ERRs.get(refClass) == null) {
			getLog().warn($m("AbstractModuleContext.0", refClass));
			ERRs.put(refClass, Boolean.TRUE);
		}
	}
}
