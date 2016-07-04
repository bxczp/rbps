package Util;

/**
 * @date 2016年3月5日 StringUtil.java
 * @author CZP
 * @parameter
 */
public class StringUtil {

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	// 判断取出的authId在不在传入的authIds中
	public static boolean isInAuthIds(String authId, String[] authIds) {
		for (String str : authIds) {
			if (str.equals(authId)) {
				return true;
			}
		}
		return false;
	}

}
