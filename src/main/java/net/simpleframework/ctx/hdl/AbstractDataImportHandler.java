package net.simpleframework.ctx.hdl;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.ctx.IApplicationContext;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDataImportHandler extends AbstractScanHandler
		implements IDataImportHandler {

	@Override
	public void onScan(final IApplicationContext application) throws Exception {
		doImport(application);
	}

	@Override
	public String toString() {
		return $m("AbstractDataImportHandler.0") + " - " + getClass().getName();
	}
}
