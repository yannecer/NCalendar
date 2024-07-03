package com.necer.utils

import android.graphics.drawable.Drawable

/**
 * @author necer
 * @date 2017/6/13
 */
object NAttrs {


    /* 日历通用属性 */

    /**
     * 默认展示的日历 月->month 周->week  默认月
     */
    var defaultCalendar = 0


    /**
     * 日历一周开始是周日或周一 周日->monday 周一->monday  默认周日
     */
    var firstDayOfWeek = 0

    /**
     * 日历中 月日历的高度 默认300dp 周日历高度为 calendarHeight/5
     */
    var calendarHeight = 0

    /**
     * 日历顶部星期的高度 默认 wrap_content
     */
    var weekBarHeight = 0

    /**
     * 日历是否可拉伸   拉伸是月状态下继续下拉到达的状态
     */
    var stretchCalendarEnable = false

    /**
     * 拉伸后日历的高度 必须比calendarHeight高度高
     */
    var stretchCalendarHeight = 0

    /**
     * 日历 月周状态变化时松手后的动画时间 默认 200毫秒
     */
    var animationDuration = 0

    /**
     * 月日历是否全部6行
     */
    var allMonthSixLine = false

    /**
     * 日历背景 可设置Drawable背景，设置了这个数字背景失效
     */
    var calendarBackground: Drawable? = null

    /**
     * 是否显示数字背景 显示当前月
     */
    var showNumberBackground = false

    /**
     * 数字背景字体大小 默认 240sp
     */
    var numberBackgroundTextSize = 0f

    /**
     * 数字背景字体颜色
     */
    var numberBackgroundTextColor = 0

    /**
     * 背景透明度 取值0-225  默认50
     */
    var backgroundAlphaColor = 0

    /**
     * 月日历上下月是否可点击
     */
    var lastNextMonthClickEnable = false

    /**
     * 日历左右是否可滑动
     */
    var horizontalScrollEnable = false


    /**
     * 顶部周文字颜色
     */
    var weekBarTextColor=0

    /**
     * 顶部周文字大小
     */
    var weekBarTextSize=0f

    /**
     * 顶部周背景色
     */
    var weekBarBackgroundColor = 0



    /* 日历UI属性 UI属性仅针对内置的InnerPainter，如果使用自定义CalendarPainter不生效，需自行实现需要的功能 */


    /**
     * 今天选中的背景 shape drawable
     */
    var todayCheckedBackground = 0

    /**
     * 其他日期选中的背景 shape drawable
     */
    var defaultCheckedBackground = 0

    /**
     * 公历日期属性
     * 今天选中时的公历字体颜色 默认白色
     */
    var todayCheckedSolarTextColor = 0

    /**
     * 公历日期属性
     * 今天不选中的公历字体颜色
     */
    var todayUnCheckedSolarTextColor = 0

    /**
     * 公历日期属性
     * 默认选中的公历字体颜色
     */
    var defaultCheckedSolarTextColor = 0

    /**
     * 公历日期属性
     * 默认不选中公历字体颜色
     */
    var defaultUnCheckedSolarTextColor = 0

    /**
     * 默认每页选中时，翻页选中第一个日期
     */
    var defaultCheckedFirstDate = false

    /**
     * 公历日期属性
     * 公历字体大小  默认18sp
     */
    var solarTextSize = 0f

    /**
     * 公历日期属性
     * 公历字体是否加粗
     */
    var solarTextBold = false

    /**
     * 标记日期属性
     * 今天选中时标记
     */
    var todayCheckedPoint = 0

    /**
     * 标记日期属性
     * 今天不选中时标记
     */
    var todayUnCheckedPoint = 0

    /**
     * 标记日期属性
     * 默认选中时标记
     */
    var defaultCheckedPoint = 0

    /**
     * 标记日期属性
     * 默认不选中时标记
     */
    var defaultUnCheckedPoint = 0

    /**
     * 标记日期属性
     * up在上面 down在下面 默认上up
     */
    var pointLocation = 0

    /**
     * 标记日期属性
     * 标记点到文字中心的距离  默认20dp
     */
    var pointDistance = 0f

    /**
     * 节假日日期属性  drawable
     * 今天选中时节假日
     */
    var todayCheckedHoliday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 今天不选中时节假日
     */
    var todayUnCheckedHoliday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 默认选中时节假日
     */
    var defaultCheckedHoliday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 默认不选中时节假日
     */
    var defaultUnCheckedHoliday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 今天选中时工作日
     */
    var todayCheckedWorkday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 今天不选中工作日
     */
    var todayUnCheckedWorkday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 默认选中时工作日
     */
    var defaultCheckedWorkday: Drawable? = null

    /**
     * 节假日日期属性  drawable
     * 默认不选中时工作日
     */
    var defaultUnCheckedWorkday: Drawable? = null

    /**
     * 节假日日期属性  text
     * 是否显示节假日和工作日标记 默认true
     */
    var showHolidayWorkday = true

    /**
     * 节假日日期属性  text
     * 节假日文字  默认 休
     */
    var holidayText: String? = null

    /**
     * 节假日日期属性  text
     * 工作日文字  默认 班
     */
    var workdayText: String? = null

    /**
     * 节假日日期属性  text
     * 字体大小  默认 10sp
     */
    var holidayWorkdayTextSize = 0f

    /**
     * 节假日日期属性  text
     * 是否加粗
     */
    var holidayWorkdayTextBold = false

    /**
     * 节假日日期属性  text
     * 文字距离中心距离 默认 15dp
     */
    var holidayWorkdayDistance = 0f

    /**
     * 节假日日期属性  text
     * 文字的位置 分别为 top_right、top_left、bottom_right、bottom_left，默认为 top_right 右上角
     */
    var holidayWorkdayLocation = 0

    /**
     * 节假日日期属性  text
     * 今天选中时节假日字体颜色 默认白色
     */
    var todayCheckedHolidayTextColor = 0

    /**
     * 节假日日期属性  text
     * 今天未选中时节假日字体颜色
     */
    var todayUnCheckedHolidayTextColor = 0

    /**
     * 节假日日期属性  text
     * 默认选中时节假日字体颜色
     */
    var defaultCheckedHolidayTextColor = 0

    /**
     * 节假日日期属性  text
     * 默认未选中时节假日字体颜色
     */
    var defaultUnCheckedHolidayTextColor = 0

    /**
     * 节假日日期属性  text
     * 今天选中时工作日字体颜色
     */
    var todayCheckedWorkdayTextColor = 0

    /**
     * 节假日日期属性  text
     * 今天选中时工作日颜色
     */
    var todayUnCheckedWorkdayTextColor = 0

    /**
     * 节假日日期属性  text
     * 默认选中时工作日字体颜色
     */
    var defaultCheckedWorkdayTextColor = 0

    /**
     * 节假日日期属性  text
     * 默认选中时工作日字体颜色
     */
    var defaultUnCheckedWorkdayTextColor = 0

    /**
     * 农历属性
     * 是否显示农历
     */
    var showLunar = false

    /**
     * 农历属性
     * 今天选中时农历颜色
     */
    var todayCheckedLunarTextColor = 0

    /**
     * 农历属性
     * 今天不选中时农历颜色
     */
    var todayUnCheckedLunarTextColor = 0

    /**
     * 农历属性
     * 默认选中时农历颜色
     */
    var defaultCheckedLunarTextColor = 0

    /**
     * 农历属性
     * 默认不选中时农历颜色
     */
    var defaultUnCheckedLunarTextColor = 0

    /**
     * 农历属性
     * 农历字体大小 默认 10sp
     */
    var lunarTextSize = 0f

    /**
     * 农历属性
     * 农历字体是否加粗
     */
    var lunarTextBold = false

    /**
     * 农历属性
     * 农历文字到文字中心的距离 默认15dp
     */
    var lunarDistance = 0f

    /**
     * 上下月月的颜色透明度 默认 90 取值范围 0-225
     */
    var lastNextMothAlphaColor = 0


    /**
     * 不可用的日期颜色透明度 默认 50 取值范围 0-225
     */
    var disabledAlphaColor = 0

    /**
     * 点击不可用的日期提示语
     */
    var disabledString: String? = null

    /**
     * 拉伸显示的字体大小
     */
    var stretchTextSize = 0f

    /**
     * 拉伸字体加粗
     */
    var stretchTextBold = false

    /**
     * 拉伸显示的字体颜色
     */
    var stretchTextColor = 0

    /**
     * 拉伸显示的字体距离矩形中心的距离
     */
    var stretchTextDistance = 0f




    /**
     * 指示圆点的位置
     * 在公历日期上面
     */
    const val UP = 200

    /**
     * 指示圆点的位置
     * 在公历日期下面
     */
    const val DOWN = 201

    /**
     * 周的第一天
     * 周日
     */
    const val SUNDAY = 300

    /**
     * 周的第一天
     * 周一
     */
    const val MONDAY = 301

    /**
     * 节假日的位置
     * 右上方
     */
    const val TOP_RIGHT = 400

    /**
     * 节假日的位置
     * 左上方
     */
    const val TOP_LEFT = 401

    /**
     * 节假日的位置
     * 右下方
     */
    const val BOTTOM_RIGHT = 402

    /**
     * 节假日的位置
     * 左下方
     */
    const val BOTTOM_LEFT = 403
}