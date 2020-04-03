package com.necer;
import android.util.Log;

public class MyLog {


	public static void d(String str) {

		int index = 0; // 当前位置
		int max = 3800;// 需要截取的最大长度,别用4000
		String sub;    // 进行截取操作的string
		while (index < str.length()) {
			if (str.length() < max) { // 如果长度比最大长度小
				max = str.length();   // 最大长度设为length,全部截取完成.
				sub = str.substring(index, max);
			} else {
				sub = str.substring(index, max);
			}
			Log.e("NECER", sub);       // 进行输出
			index = max;
			max += 3800;
		}
	}
}
