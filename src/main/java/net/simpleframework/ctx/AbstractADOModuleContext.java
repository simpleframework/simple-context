package net.simpleframework.ctx;

import net.simpleframework.ado.IADOManagerFactory;
import net.simpleframework.ado.IADOManagerFactoryAware;
import net.simpleframework.ado.db.DbEntityTable;
import net.simpleframework.ado.db.DbManagerFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractADOModuleContext extends AbstractModuleContext
		implements IModuleContext, IADOManagerFactoryAware {

	private IADOManagerFactory adoManagerFactory;

	@Override
	public void onInit(final IApplicationContext application) throws Exception {
		super.onInit(application);

		final Object mFactory = getADOManagerFactory();
		if (mFactory instanceof DbManagerFactory) {
			final DbEntityTable[] tbls = createEntityTables();
			if (tbls != null) {
				((DbManagerFactory) mFactory).regist(tbls);
			}
		}
	}

	@Override
	public IADOManagerFactory getADOManagerFactory() {
		if (adoManagerFactory == null) {
			adoManagerFactory = application.getADOManagerFactory(getDataSource());
		}
		return adoManagerFactory;
	}

	protected DbEntityTable[] createEntityTables() {
		return null;
	}
}
