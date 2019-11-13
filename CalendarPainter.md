



### CalendarPainter


```
日历绘制接口，绘制的所有内容通过这个接口完成，实现这个类可实现自定义的日历界面，
参数中的 rectF 是文字位置的矩形对象
日历内部内置了一个 InnerPainter ，各个属性也是这个绘制类的，如果自定义 CalendarPainter ，则这些属性都不适用
InnerPainter 实现了设置圆点、替换农历等方法，还可以实现更多方法，如多选，多标记等，


   
    /**
     * 绘制月日历或这日历背景，如数字背景等
     *
     * @param iCalendarView   ICalendarView 日历页面，可判断是月日历或者周日历
     * @param canvas
     * @param rectF
     * @param localDate
     * @param totalDistance   滑动的全部距离
     * @param currentDistance 当前位置的距离
     */
    void onDrawCalendarBackground(ICalendarView iCalendarView, Canvas canvas, RectF rectF, LocalDate localDate, int totalDistance, int currentDistance);

    /**
     * 绘制今天的日期
     *
     * @param canvas
     * @param rectF
     * @param localDate
     * @param selectedDateList 全部选中的日期集合
     */
    void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    /**
     * 绘制当前月或周的日期
     *
     * @param canvas
     * @param rectF
     * @param localDate
     * @param selectedDateList 全部选中的日期集合
     */
    void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    /**
     * 绘制上一月，下一月的日期，周日历不用实现
     *
     * @param canvas
     * @param rectF
     * @param localDate
     * @param selectedDateList 全部选中的日期集合
     */
    void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    /**
     * 绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应,
     * 如果没有使用setDateInterval设置日期范围 此方法不用实现
     *
     * @param canvas
     * @param rectF
     * @param localDate
     */
    void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate);




实现接口 CalendarPainter，分别重写以上几个方法，setCalendarPainter(calendarPainter)即可实现自定义日历界面

```
