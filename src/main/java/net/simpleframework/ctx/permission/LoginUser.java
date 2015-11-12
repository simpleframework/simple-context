package net.simpleframework.ctx.permission;

import net.simpleframework.common.ID;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class LoginUser {

	private static final ThreadLocal<LoginWrapper> USER_LOCAL;
	static {
		USER_LOCAL = new ThreadLocal<LoginWrapper>();
	}

	public static LoginWrapper get() {
		return USER_LOCAL.get();
	}

	public static void set(final LoginWrapper user) {
		if (user != null) {
			USER_LOCAL.set(user);
		} else {
			USER_LOCAL.remove();
		}
	}

	public static PermissionUser user() {
		return get().getUser();
	}

	public static void setAdmin() {
		set(new LoginWrapper(PermissionFactory.getAdmin()).setIp("127.0.0.1"));
	}

	public static void isAdmin() {
	}

	public static class LoginWrapper {
		private PermissionUser user;

		private String ip;

		public LoginWrapper(final PermissionUser user) {
			this.user = user;
		}

		public ID getUserId() {
			return user != null ? user.getId() : null;
		}

		public PermissionUser getUser() {
			return user;
		}

		public LoginWrapper setUser(final PermissionUser user) {
			this.user = user;
			return this;
		}

		public String getIp() {
			return ip;
		}

		public LoginWrapper setIp(final String ip) {
			this.ip = ip;
			return this;
		}

		@Override
		public String toString() {
			return user != null ? user.toString() + " [" + getIp() + "]" : super.toString();
		}
	}
}
