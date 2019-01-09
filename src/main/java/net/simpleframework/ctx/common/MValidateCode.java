package net.simpleframework.ctx.common;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.ApplicationContextFactory;
import net.simpleframework.ctx.IApplicationContext;
import net.simpleframework.ctx.ModuleContextException;
import net.simpleframework.ctx.task.ExecutorRunnableEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MValidateCode {

	public static Code genCode(final String key) {
		return genCode(key, 0);
	}

	public static Code getCode(final String key) {
		return _codes.get(key);
	}

	public static void removeCode(final String key) {
		_codes.remove(key);
	}

	public static Code genCode(final String key, final int timeout) {
		// 1分钟间隔内不允许重复发送
		Code oCode = getCode(key);
		if (oCode != null) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -1);
			if (oCode.createDate.after(cal.getTime())) {
				throw ModuleContextException.of($m("MValidateCode.4"));
			}
		}

		oCode = new Code();
		final char[] val = new char[4];
		for (int i = 0; i < 4; i++) {
			val[i] = CONST_CODE.charAt(random.nextInt(CONST_CODE_LENGTH));
		}
		oCode.val = new String(val);
		if (timeout > 0) {
			oCode.timeout = timeout;
		}
		_codes.put(key, oCode);
		System.out.println("genCode: " + key + ", " + oCode.val);
		return oCode;
	}

	public static Code verifyCode(final String key, final String val) {
		if (!StringUtils.hasText(val)) {
			throw ModuleContextException.of($m("MValidateCode.1"));
		}
		final Code oCode = getCode(key);
		if (oCode == null) {
			throw ModuleContextException.of($m("MValidateCode.2"));
		}
		if (!oCode.val.equalsIgnoreCase(val)) {
			throw ModuleContextException.of($m("MValidateCode.3"));
		}
		oCode.verify = true;
		return oCode;
	}

	static Random random = new Random();
	static final String CONST_CODE = "0123456789"; // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
	static final int CONST_CODE_LENGTH = CONST_CODE.length();

	static Map<String, Code> _codes = new HashMap<>();

	public static class Code {
		boolean verify = false;

		String val;

		Date createDate = new Date();

		int timeout = 5 * 60; // 5m

		boolean isExpaired() {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.SECOND, -timeout);
			return createDate.before(cal.getTime());
		}

		public String val() {
			return val;
		}

		public int timeout() {
			return timeout;
		}

		public boolean verify() {
			return verify;
		}

		@Override
		public String toString() {
			return val;
		}
	}

	static {
		((IApplicationContext) ApplicationContextFactory.ctx()).getTaskExecutor()
				.addScheduledTask(new ExecutorRunnableEx("mvalidate_code", $m("MValidateCode.0")) {
					@Override
					public int getPeriod() {
						return 30;
					}

					@Override
					protected void task(final Map<String, Object> cache) throws Exception {
						final List<String> removes = new ArrayList<>();
						for (final Map.Entry<String, Code> e : _codes.entrySet()) {
							if (e.getValue().isExpaired()) {
								removes.add(e.getKey());
							}
						}
						for (final String k : removes) {
							removeCode(k);
						}
					}
				});
	}
}