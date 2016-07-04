package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @date 2016年3月5日 PropertyUtil.java
 * @author CZP
 * @parameter
 */
public class PropertiesUtil {
	public static String getValue(String key) {
		Properties properties = new Properties();
		// 获取src目录下的properties文件输入流
		InputStream input = new PropertiesUtil().getClass().getResourceAsStream("/rbps.properties");
		try {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = String.valueOf(properties.get(key));
		return value;
	}
}
