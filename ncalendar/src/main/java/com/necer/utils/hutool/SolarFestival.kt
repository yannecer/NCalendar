package com.necer.utils.hutool

/**
 * 节假日（公历）封装
 *
 */
object SolarFestival {

    private val S_FTV: HashMap<Pair<Int, Int>, String> = HashMap()

    init {
        // 节日
        S_FTV[Pair(1, 1)] = "元旦"

        // 二月
        S_FTV[Pair(2, 14)] = "情人节"

        // 三月
       S_FTV[Pair(3, 8)] = "妇女节"
       S_FTV[Pair(3, 12)] = "植树节"
       S_FTV[Pair(3, 15)] = "消费者"

        // 四月
        S_FTV[Pair(4, 1)] = "愚人节"

        // 五月
        S_FTV[Pair(5, 1)] = "劳动节"
        S_FTV[Pair(5, 4)] = "青年节"

        // 六月
        S_FTV[Pair(6, 1)] = "儿童节"

        // 七月
        S_FTV[Pair(7, 1)] = "建党节"

        // 八月
        S_FTV[Pair(8, 1)] = "建军节"

        // 九月
        S_FTV[Pair(9, 10)] = "教师节"

        // 十月
        S_FTV[Pair(10, 1)] = "国庆节"
    }



    /**
     * 获得节日
     *
     * @param month 月
     * @param day   日
     * @return 获得公历节日
     */
    fun getFestivals(month: Int, day: Int): String? {
        return S_FTV[Pair(month, day)]
    }
}