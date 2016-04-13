package net.simpleframework.ctx;

/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum EPlatform {
	pc,

	mobile,

	mobile_android,

	mobile_ios,

	weixin;

	public static EPlatform get() {
		return pc;
	}
}