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

	public static Code genCode() {
		return genCode(0);
	}

	public static Code genCode(final int timeout) {
		final Code oCode = new Code();
		final char[] text = new char[4];
		for (int i = 0; i < 4; i++) {
			text[i] = CONST_CODE.charAt(random.nextInt(CONST_CODE_LENGTH));
		}
		oCode.code = new String(text);
		if (timeout > 0) {
			oCode.timeout = timeout;
		}
		return oCode;
	}

	public static Code verifyCode(final String key, final String code) {
		if (!StringUtils.hasText(code)) {
			throw ModuleContextException.of($m("MValidateCode.1"));
		}
		final Code oCode = _codes.get(key);
		if (oCode == null) {
			throw ModuleContextException.of($m("MValidateCode.2"));
		}
		if (!oCode.code.equalsIgnoreCase(code)) {
			throw ModuleContextException.of($m("MValidateCode.3"));
		}
		return oCode;
	}

	static Random random = new Random();
	static final String CONST_CODE = "0123456789"; // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
	static final int CONST_CODE_LENGTH = CONST_CODE.length();

	static Map<String, Code> _codes = new HashMap<String, Code>();

	public static class Code {
		String code;

		Date createDate = new Date();

		int timeout = 5 * 60; // 5m

		boolean isExpaired() {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.SECOND, -timeout);
			return createDate.before(cal.getTime());
		}
	}

	static {
		((IApplicationContext) ApplicationContextFactory.ctx()).getTaskExecutor().addScheduledTask(
				new ExecutorRunnableEx("mvalidate_code", $m("MValidateCode.0")) {
					@Override
					public int getPeriod() {
						return 30;
					}

					@Override
					protected void task(final Map<String, Object> cache) throws Exception {
						final List<String> removes = new ArrayList<String>();
						for (final Map.Entry<String, Code> e : _codes.entrySet()) {
							if (e.getValue().isExpaired()) {
								removes.add(e.getKey());
							}
						}
						for (final String k : removes) {
							_codes.remove(k);
						}
					}
				});
	}
}