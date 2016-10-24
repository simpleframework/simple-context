package net.simpleframework.ctx;

/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public enum EPlatform {
	pc {
		@Override
		public String toString() {
			return "电脑端";
		}
	},

	mobile {
		@Override
		public String toString() {
			return "移动端";
		}
	},

	mobile_android {
		@Override
		public String toString() {
			return "android";
		}
	},

	mobile_ios {
		@Override
		public String toString() {
			return "ios";
		}
	},

	weixin {
		@Override
		public String toString() {
			return "weixin";
		}
	};

	public static EPlatform get() {
		return pc;
	}
}