package net.simpleframework.ctx;

import net.simpleframework.common.coll.AbstractArrayListEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ModuleFunctions extends AbstractArrayListEx<ModuleFunctions, ModuleFunction> {

	public static ModuleFunctions of(final ModuleFunction... functions) {
		return new ModuleFunctions().append(functions);
	}

	@Override
	public boolean add(final ModuleFunction element) {
		for (final ModuleFunction _element : this) {
			if (_element.getName().equals(element.getName())) {
				remove(_element);
				break;
			}
		}
		return super.add(element);
	}

	private static final long serialVersionUID = -2278834911275737189L;
}
