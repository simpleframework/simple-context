package net.simpleframework.ctx.permission;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class Domain {
	private static final ThreadLocal<Domain> DOMAIN_LOCAL;
	static {
		DOMAIN_LOCAL = new ThreadLocal<Domain>();
	}

	public static Domain get() {
		return DOMAIN_LOCAL.get();
	}

	public static void set(final Domain domain) {
		if (domain != null) {
			DOMAIN_LOCAL.set(domain);
		} else {
			DOMAIN_LOCAL.remove();
		}
	}

	private String name;

	public Domain(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
