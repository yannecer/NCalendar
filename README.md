# 安卓日历 NCalendar

## 特点:

 - 3种常见日历交互方式，MIUI系统日历：miui9、miui10、华为emui，miui9和钉钉日历类似，华为emui和365日历类似
 - 月周滑动切换，月周不选中
 - 支持多选，设置多选的数量
 - 支持设置默认视图，默认周日历或者月日历
 - 支持周状态固定，下拉刷新等
 - 支持设置一周开始的是周一还是周日
 - 可设置日期区间，默认区间从1901-01-01到2099-12-31 
 - 支持农历，节气、法定节假日等
 - 支持添加指示点及设置指示点位置
 - 支持各种颜色、距离、位置等属性
 - 支持日历和列表之间添加view
 - 支持替换农历、颜色等
 - 支持自定义日历页面


## 效果图 
|miui9|miui10|emui|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/miui9_gif.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/miui10_gif.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/emui_gif.gif)|

|周固定，下拉刷新|日历和子view添加其他view|自定义日历界面|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/week_hold.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/addview.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/custom.gif)|

|默认不选中|默认多选|
|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/111.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/222.gif)|
## 下载demo：
[下载demo](https://github.com/yannecer/NCalendar/releases/download/4.0.1/app-debug.apk)

## 旧版文档：
[旧版文档](https://github.com/yannecer/NCalendar/blob/master/README_OLD.md)

## 使用方法

#### Gradle
```
implementation 'com.necer.ncalendar:ncalendar:4.1.0'

```

#### 月周切换

```
    miui9 和 钉钉日历
    <com.necer.calendar.Miui9Calendar
        android:id="@+id/miui9Calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </com.necer.calendar.Miui9Calendar>
    
    Miui10Calendar EmuiCalendar 用法类似
    
```

#### 单个月日历，单个周日历


```
   月日历
   <com.necer.calendar.MonthCalendar
        android:layout_width="match_parent"
        app:todaySolarTextColor="#ff00ff"
        app:selectCircleColor="#00c3aa"
        app:hollowCircleColor="#00c3aa"
        android:layout_height="300dp" />

   周日历
   <com.necer.calendar.WeekCalendar
        android:layout_width="match_parent"
        app:lunarTextColor="#00aa00"
        android:layout_height="50dp" />

```


#### 注意

- NCalendar（Miui9Calendar、Miui10Calendar、EmuiCalendar）内部只能有一个子view，需要一个实现了```NestedScrollingChild```的子类，
如```RecyclerView```，```NestedScrollView```等，不必是直接子类，可以使用其他布局嵌套一个```NestedScrollingChild```
- 如果布局文件中，内部的子view有多个父view，恰好也有实现了```NestedScrollingChild```的父view，则需要给真实滑动的子view设置tag（“@string/factual_scroll_view”），不然可能会出现滑动异常，此种情况在下拉刷新中比较常见
- 新版的代码已经将NestedScrollingChild2改成了NestedScrollingChild，如果出现滑动异常，则需要给实际滑动的view设置tag（“@string/factual_scroll_view”）



### 交流群

技术交流QQ群：127278900
### 主要Api

#### 月日历、周日历、折叠日历共同拥有的api


    //设置默认选中
    void setDefaultSelect(boolean isDefaultSelect);

    //默认选中时，是否翻页选中第一个，前提必须默认选中
    void setDefaultSelectFitst(boolean isDefaultSelectFitst);

    //是否多选
    void setMultipleSelset(boolean isMultipleSelset);
    
    //多选个数和模式 FULL_CLEAR-超过清除所有  FULL_REMOVE_FIRST-超过清除第一个
    void setMultipleNum(int multipleNum, MultipleModel multipleModel);

    //跳转日期
    void jumpDate(String formatDate);

    //上一页 上一周 上一月
    void toLastPager();

    //下一页 下一周 下一月
    void toNextPager();

    //回到今天
    void toToday();

    //设置自定义绘制类
    void setCalendarPainter(CalendarPainter calendarPainter);

    //刷新日历
    void notifyCalendar();

    //设置初始化日期
    void setInitializeDate(String formatInitializeDate);

    //设置初始化日期和可用区间
    void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate);

    //设置可用区间
    void setDateInterval(String startFormatDate, String endFormatDate);

    //单选日期变化监听
    void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener);

    //多选日期变化监听
    void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener);

    //设置点击了不可用日期监听
    void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener);

    //获取xml参数
    Attrs getAttrs();

    //获取绘制类
    CalendarPainter getCalendarPainter();

    //获取全部选中的日期集合
    List<LocalDate> getAllSelectDateList();

    //获取当前页面选中的日期集合
    List<LocalDate> getCurrectSelectDateList();


#### 折叠日历miui9，miui10，emui 拥有的api


    //折叠回到周状态
    void toWeek();

    //展开回到月状态
    void toMonth();

    //日历月周状态变化回调
    void setOnCalendarStateChangedListener(OnCalendarStateChangedListener onCalendarStateChangedListener);

     //设置日历状态
    void setCalendarState(CalendarState calendarState);

    //获取当前日历的状态  CalendarState.MONTH==月视图     CalendarState.WEEK==周视图
    CalendarState getCalendarState();



#### 添加指示圆点
```
此功能为默认 CalendarPainter 类 InnerPainter 的功能，如果设置了自定义 CalendarPainter ，没有此方法，需要自己实现

List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01");
InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
innerPainter.setPointList(pointList);

```
#### 设置法定节假日
```
此功能为默认 CalendarPainter 类 InnerPainter 的功能，如果设置了自定义 CalendarPainter ，没有此方法，需要自己实现

List<String> holidayList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20");
List<String> holidayList = Arrays.asList("2019-10-01", "2019-11-19", "2019-11-20");

InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
innerPainter.setLegalHolidayList(holidayList,workdayList);

```
#### 替换农历文字及颜色
```
此功能为默认 CalendarPainter 类 InnerPainter 的功能，如果设置了自定义 CalendarPainter ，没有此方法，需要自己实现

InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();

Map<String, String> strMap = new HashMap<>();
strMap.put("2019-01-25", "测试");
strMap.put("2019-01-23", "测试1");
strMap.put("2019-01-24", "测试2");
innerPainter.setReplaceLunarStrMap(strMap);

Map<String, Integer> colorMap = new HashMap<>();
colorMap.put("2019-01-25", Color.RED);
colorMap.put("2019-01-23", Color.GREEN);
colorMap.put("2019-01-24", Color.parseColor("#000000"));
innerPainter.setReplaceLunarColorMap(colorMap);
```

### CalendarPainter


```
日历绘制接口，绘制的所有内容通过这个接口完成，实现这个类可实现自定义的日历界面，
参数中的 rect 是文字位置的矩形对象
日历内部内置了一个 InnerPainter ，各个属性也是这个绘制类的，如果自定义 CalendarPainter ，则这些属性都不适用
InnerPainter 实现了设置圆点、替换农历等方法，还可以实现更多方法，如多选，多标记等，


    //绘制今天的日期
    void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制当前月或周的日期
    void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制上一月，下一月的日期，周日历不须实现
    void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> selectedDateList);

    //绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应
    void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate);



实现接口 CalendarPainter，分别重写以上几个方法，setCalendarPainter(calendarPainter)即可实现自定义日历界面

```

### CalendarDate
```
CalendarDate 日历中存放日期各种参数的类，包含公历、农历、节气、节日、属相、天干地支等

    public LocalDate localDate;//公历日期
    public Lunar lunar;//农历
    public String solarHoliday;//公历节日
    public String lunarHoliday;//农历节日
    public String solarTerm;//节气
   
其中Lunar为农历信息的对象

    public boolean isLeap; //是否闰年
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;
    public int leapMonth;

    public String lunarOnDrawStr;//农历位置需要绘制的文字
    public String lunarDayStr;
    public String lunarMonthStr;
    public String lunarYearStr;
    public String animals;//生肖
    public String chineseEra;//天干地支


CalendarDate对象通过 CalendarUtil 获取

CalendarDate calendarDate = CalendarUtil.getCalendarDate(LocalDate localDate);

```




## 感谢：

项目中日期计算使用  [joda-time](https://github.com/JodaOrg/joda-time)<br/>农历和节气数据是工具类，多谢



## 支持的属性：

|Attributes|forma|describe
|---|---|---|
|solarTextColor| color|公历日期的颜色
|lunarTextColor| color|农历日期的颜色
|solarHolidayTextColor| color|公历节假日的颜色
|lunarHolidayTextColor| color|农历节假日的颜色
|solarTermTextColor| color|节气颜色
|selectCircleColor| color|选中圈的颜色
|holidayColor|color| 法定节休息日颜色
|workdayColor|color| 法定节调休工作日颜色
|bgCalendarColor|color| 日历的背景
|bgChildColor|color| 日历包含子view的背景
|todaySelectContrastColor|color| 今天被选中是其他元素的对比色，比如 农历，圆点等
|pointColor| color |小圆点的颜色
|startDate| string |日期区间开始日期
|endDate| string |日期区间结束日期
|isDefaultSelect| boolean |是否默认选中
|isDefaultSelectFitst| boolean |是否默认翻页选中第一天
|isMultipleSelect| boolean |是否多选
|alphaColor| integer |不是本月的日期颜色的透明度0-255
|disabledAlphaColor| integer |日期区间之外的地日颜色的透明度0-255
|disabledString| string |点击日期区间之外的日期提示语
|todaySolarTextColor| color|今天不选中的颜色
|todaySolarSelectTextColor| color|今天选中的颜色
|selectCircleRadius| dimension | 选中圈的半径
|solarTextSize| dimension|公历日期字体大小
|lunarTextSize| dimension|农历日期字体大小
|lunarDistance| dimension|农历日期到公历字体的距离
|holidayTextSize| dimension|法定节假日字体的大小
|holidayDistance| dimension |法定节假日到公历的距离
|pointDistance| dimension |小圆点到公历的距离
|hollowCircleStroke| dimension |空心圆的宽度
|calendarHeight| dimension |日历的高度
|duration|integer| 日历自动滑动的时间
|isShowLunar| boolean |是否显示农历
|isShowHoliday|boolean| 是否显示法定节假日
|isWeekHold|boolean| 周状态是否固定，默认不固定
|isDefaultSelect|boolean| 是否默认选中（只对单个月日历或者周日历有效）
|defaultCalendar|enum| 默认视图 week 或者 month
|pointLocation|enum| 指示点的文职 up（在公历的上方） 或者 down（在公历的下方） 默认是up
|firstDayOfWeek|enum| 一周开始的星期天还是星期一 sunday 或者 monday 默认是sunday
|holidayLocation|enum| 法定节假日相对公历日期的位置 top_right（右上方）、top_left（左上方）、bottom_right（右下方）、bottom_left（左下方）



## 更新日志
* 4.1.0<br/> 优化onDraw效率、修改CalendarPainter回调参数、新增多选日期数量
* 4.0.4<br/> 修复某些情况下选中回调返回null的bug
* 4.0.2<br/> 修复节气不显示的bug
* 4.0.1<br/> 1、新增月周切换日历多选<br/>2、新增默认不选中折叠<br/>3、新增翻页默认选中每月1号<br/>4、修复设置左右padding偏差



License
-------


     Copyright 2018 necer

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
   
   

