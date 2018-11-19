package com.necer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Sun_LeiLei
 * 来源：CSDN
 * 原文：https://blog.csdn.net/sun_leilei/article/details/50148297
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 * Created by necer on 2018/11/16.
 */
public class SolarTermUtil {


    private static final double D = 0.2422;
    private final static Map<String, Integer[]> INCREASE_OFFSETMAP = new HashMap<String, Integer[]>();// +1偏移
    private final static Map<String, Integer[]> DECREASE_OFFSETMAP = new HashMap<String, Integer[]>();// -1偏移

    /**
     * 24节气
     **/
    private static enum SolarTermsEnum {
        LICHUN, // --立春
        YUSHUI, // --雨水
        JINGZHE, // --惊蛰
        CHUNFEN, // 春分
        QINGMING, // 清明
        GUYU, // 谷雨
        LIXIA, // 立夏
        XIAOMAN, // 小满
        MANGZHONG, // 芒种
        XIAZHI, // 夏至
        XIAOSHU, // 小暑
        DASHU, // 大暑
        LIQIU, // 立秋
        CHUSHU, // 处暑
        BAILU, // 白露
        QIUFEN, // 秋分
        HANLU, // 寒露
        SHUANGJIANG, // 霜降
        LIDONG, // 立冬
        XIAOXUE, // 小雪
        DAXUE, // 大雪
        DONGZHI, // 冬至
        XIAOHAN, // 小寒
        DAHAN;// 大寒
    }

    static {
        DECREASE_OFFSETMAP.put(SolarTermsEnum.YUSHUI.name(),
                new Integer[]{2026});// 雨水
        INCREASE_OFFSETMAP.put(SolarTermsEnum.CHUNFEN.name(),
                new Integer[]{2084});// 春分
        INCREASE_OFFSETMAP.put(SolarTermsEnum.XIAOMAN.name(),
                new Integer[]{2008});// 小满
        INCREASE_OFFSETMAP.put(SolarTermsEnum.MANGZHONG.name(),
                new Integer[]{1902});// 芒种
        INCREASE_OFFSETMAP.put(SolarTermsEnum.XIAZHI.name(),
                new Integer[]{1928});// 夏至
        INCREASE_OFFSETMAP.put(SolarTermsEnum.XIAOSHU.name(), new Integer[]{
                1925, 2016});// 小暑
        INCREASE_OFFSETMAP.put(SolarTermsEnum.DASHU.name(),
                new Integer[]{1922});// 大暑
        INCREASE_OFFSETMAP.put(SolarTermsEnum.LIQIU.name(),
                new Integer[]{2002});// 立秋
        INCREASE_OFFSETMAP.put(SolarTermsEnum.BAILU.name(),
                new Integer[]{1927});// 白露
        INCREASE_OFFSETMAP.put(SolarTermsEnum.QIUFEN.name(),
                new Integer[]{1942});// 秋分
        INCREASE_OFFSETMAP.put(SolarTermsEnum.SHUANGJIANG.name(),
                new Integer[]{2089});// 霜降
        INCREASE_OFFSETMAP.put(SolarTermsEnum.LIDONG.name(),
                new Integer[]{2089});// 立冬
        INCREASE_OFFSETMAP.put(SolarTermsEnum.XIAOXUE.name(),
                new Integer[]{1978});// 小雪
        INCREASE_OFFSETMAP.put(SolarTermsEnum.DAXUE.name(),
                new Integer[]{1954});// 大雪
        DECREASE_OFFSETMAP.put(SolarTermsEnum.DONGZHI.name(), new Integer[]{
                1918, 2021});// 冬至

        INCREASE_OFFSETMAP.put(SolarTermsEnum.XIAOHAN.name(),
                new Integer[]{1982});// 小寒
        DECREASE_OFFSETMAP.put(SolarTermsEnum.XIAOHAN.name(),
                new Integer[]{2019});// 小寒

        INCREASE_OFFSETMAP.put(SolarTermsEnum.DAHAN.name(),
                new Integer[]{2082});// 大寒
    }

    // 定义一个二维数组，第一维数组存储的是20世纪的节气C值，第二维数组存储的是21世纪的节气C值,0到23个，依次代表立春、雨水...大寒节气的C值
    private static final double[][] CENTURY_ARRAY = {
            {4.6295, 19.4599, 6.3826, 21.4155, 5.59, 20.888, 6.318, 21.86,
                    6.5, 22.2, 7.928, 23.65, 8.35, 23.95, 8.44, 23.822, 9.098,
                    24.218, 8.218, 23.08, 7.9, 22.6, 6.11, 20.84},
            {3.87, 18.73, 5.63, 20.646, 4.81, 20.1, 5.52, 21.04, 5.678, 21.37,
                    7.108, 22.83, 7.5, 23.13, 7.646, 23.042, 8.318, 23.438,
                    7.438, 22.36, 7.18, 21.94, 5.4055, 20.12}};

    /**
     * @param year 年份
     * @param name 节气的名称
     * @return 返回节气是相应月份的第几天
     */
    public static int getSolarTermNum(int year, String name) {

        double centuryValue = 0;// 节气的世纪值，每个节气的每个世纪值都不同
        name = name.trim().toUpperCase();
        int ordinal = SolarTermsEnum.valueOf(name).ordinal();

        int centuryIndex = -1;
        if (year >= 1901 && year <= 2000) {// 20世纪
            centuryIndex = 0;
        } else if (year >= 2001 && year <= 2100) {// 21世纪
            centuryIndex = 1;
        } else {
            throw new RuntimeException("不支持此年份：" + year
                    + "，目前只支持1901年到2100年的时间范围");
        }
        centuryValue = CENTURY_ARRAY[centuryIndex][ordinal];
        int dateNum = 0;
        /**
         * 计算 num =[Y*D+C]-L这是传说中的寿星通用公式
         * 公式解读：年数的后2位乘0.2422加C(即：centuryValue)取整数后，减闰年数
         */
        int y = year % 100;// 步骤1:取年分的后两位数
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {// 闰年
            if (ordinal == SolarTermsEnum.XIAOHAN.ordinal()
                    || ordinal == SolarTermsEnum.DAHAN.ordinal()
                    || ordinal == SolarTermsEnum.LICHUN.ordinal()
                    || ordinal == SolarTermsEnum.YUSHUI.ordinal()) {
                // 注意：凡闰年3月1日前闰年数要减一，即：L=[(Y-1)/4],因为小寒、大寒、立春、雨水这两个节气都小于3月1日,所以
                // y = y-1
                y = y - 1;// 步骤2
            }
        }
        dateNum = (int) (y * D + centuryValue) - (int) (y / 4);// 步骤3，使用公式[Y*D+C]-L计算
        dateNum += specialYearOffset(year, name);// 步骤4，加上特殊的年分的节气偏移量
        return dateNum;
    }

    /**
     * 特例,特殊的年分的节气偏移量,由于公式并不完善，所以算出的个别节气的第几天数并不准确，在此返回其偏移量
     *
     * @param year 年份
     * @param name 节气名称
     * @return 返回其偏移量
     */
    private static int specialYearOffset(int year, String name) {
        int offset = 0;
        offset += getOffset(DECREASE_OFFSETMAP, year, name, -1);
        offset += getOffset(INCREASE_OFFSETMAP, year, name, 1);

        return offset;
    }

    private static int getOffset(Map<String, Integer[]> map, int year,
                                 String name, int offset) {
        int off = 0;
        Integer[] years = map.get(name);
        if (null != years) {
            for (int i : years) {
                if (i == year) {
                    off = offset;
                    break;
                }
            }
        }
        return off;
    }

    private static int mYear;
    private static List<String> mSolarData = new ArrayList<String>();
    private static List<String> mSolarName = new ArrayList<String>();

    /**
     * 判断一天是什么节气  公历日期
     *
     * @param year
     * @param data 月份占两位，日不确定，如一月一日为：011，五月十日为0510
     * @return
     * @data2015-12-2下午2:49:32
     */
    public static String getSolatName(int year, String data) {
        if (year != mYear) {
            solarTermToString(year);
        }
        if (mSolarData.contains(data)) {
            return mSolarName.get(mSolarData.indexOf(data));
        } else {
            return null;
        }
    }

    private static void solarTermToString(int year) {
        mYear = year;
        if (mSolarData != null) {
            mSolarData.clear();
        } else {
            mSolarData = new ArrayList<String>();
        }
        if (mSolarName != null) {
            mSolarName.clear();
        } else {
            mSolarName = new ArrayList<String>();
        }
        // 1
        mSolarName.add("立春");
        mSolarData.add("02" + getSolarTermNum(year, SolarTermsEnum.LICHUN.name()));
        // 2
        mSolarName.add("雨水");
        mSolarData.add("02" + getSolarTermNum(year, SolarTermsEnum.YUSHUI.name()));
        // 3
        mSolarName.add("惊蛰");
        mSolarData.add("03" + getSolarTermNum(year, SolarTermsEnum.JINGZHE.name()));
        // 4
        mSolarName.add("春分");
        mSolarData.add("03" + getSolarTermNum(year, SolarTermsEnum.CHUNFEN.name()));
        // 5
        mSolarName.add("清明");
        mSolarData.add("04" + getSolarTermNum(year, SolarTermsEnum.QINGMING.name()));
        // 6
        mSolarName.add("谷雨");
        mSolarData.add("04" + getSolarTermNum(year, SolarTermsEnum.GUYU.name()));
        // 7
        mSolarName.add("立夏");
        mSolarData.add("05" + getSolarTermNum(year, SolarTermsEnum.LIXIA.name()));
        // 8
        mSolarName.add("小满");
        mSolarData.add("05" + getSolarTermNum(year, SolarTermsEnum.XIAOMAN.name()));
        // 9
        mSolarName.add("芒种");
        mSolarData.add("06" + getSolarTermNum(year, SolarTermsEnum.MANGZHONG.name()));
        // 10
        mSolarName.add("夏至");
        mSolarData.add("06" + getSolarTermNum(year, SolarTermsEnum.XIAZHI.name()));
        // 11
        mSolarName.add("小暑");
        mSolarData.add("07" + getSolarTermNum(year, SolarTermsEnum.XIAOSHU.name()));
        // 12
        mSolarName.add("大暑");
        mSolarData.add("07" + getSolarTermNum(year, SolarTermsEnum.DASHU.name()));
        // 13
        mSolarName.add("立秋");
        mSolarData.add("08" + getSolarTermNum(year, SolarTermsEnum.LIQIU.name()));
        // 14
        mSolarName.add("处暑");
        mSolarData.add("08" + getSolarTermNum(year, SolarTermsEnum.CHUSHU.name()));
        // 15
        mSolarName.add("白露");
        mSolarData.add("09" + getSolarTermNum(year, SolarTermsEnum.BAILU.name()));
        // 16
        mSolarName.add("秋分");
        mSolarData.add("09" + getSolarTermNum(year, SolarTermsEnum.QIUFEN.name()));
        // 17
        mSolarName.add("寒露");
        mSolarData.add("10" + getSolarTermNum(year, SolarTermsEnum.HANLU.name()));
        // 18
        mSolarName.add("霜降");
        mSolarData.add("10" + getSolarTermNum(year, SolarTermsEnum.SHUANGJIANG.name()));
        // 19
        mSolarName.add("立冬");
        mSolarData.add("11" + getSolarTermNum(year, SolarTermsEnum.LIDONG.name()));
        // 20
        mSolarName.add("小雪");
        mSolarData.add("11" + getSolarTermNum(year, SolarTermsEnum.XIAOXUE.name()));
        // 21
        mSolarName.add("大雪");
        mSolarData.add("12" + getSolarTermNum(year, SolarTermsEnum.DAXUE.name()));
        // 22
        mSolarName.add("冬至");
        mSolarData.add("12" + getSolarTermNum(year, SolarTermsEnum.DONGZHI.name()));
        // 23
        mSolarName.add("小寒");
        mSolarData.add("01" + getSolarTermNum(year, SolarTermsEnum.XIAOHAN.name()));
        // 24
        mSolarName.add("大寒");
        mSolarData.add("01" + getSolarTermNum(year, SolarTermsEnum.DAHAN.name()));

    }


}
