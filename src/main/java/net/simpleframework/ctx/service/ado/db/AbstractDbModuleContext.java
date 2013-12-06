package net.simpleframework.ctx.service.ado.db;

import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.DbManagerFactory;
import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.IDbQueryManager;
import net.simpleframework.ctx.AbstractModuleContext;
import net.simpleframework.ctx.IApplicationContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbModuleContext extends AbstractModuleContext implements
		IDbModuleContext {

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);

		final DbManagerFactory dbFactory = getADOManagerFactory();
		final DbEntityTable[] tables = getEntityTables();
		if (tables != null) {
			for (final DbEntityTable tbl : tables) {
				dbFactory.regist(tbl);
			}
		}
	}

	protected DbEntityTable[] getEntityTables() {
		return null;
	}

	@Override
	public DbManagerFactory getADOManagerFactory() {
		return (DbManagerFactory) application.getADOManagerFactory(getDataSource());
	}

	@Override
	public <M> IDbEntityManager<M> getEntityManager(final Class<M> beanClass) {
		return getADOManagerFactory().getEntityManager(beanClass);
	}

	@Override
	public IDbQueryManager getQueryManager() {
		return getADOManagerFactory().getQueryManager();
	}
}
