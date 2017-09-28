package necer.ncalendardemo.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 闫彬彬 on 2017/9/28.
 * QQ:619008099
 */

public class Utils {

    /**
     * 获取当前版本信息.
     *
     * @param context
     */
    public static String getCurrentVersion(Context context) {
        String versionName = "";
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


}
