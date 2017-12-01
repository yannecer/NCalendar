# NCalendar
一款仿miui日历，月视图，周视图滑动切换，时间从1901-01-01到2099-12-31

支持自定义日期区间

支持农历，节假日，指示圆点，默认视图，周的第一天设置等

支持单一月日历、周日历设置默认选中和默认不选中


## 效果图

![](https://github.com/yannecer/NCalendar/blob/master/app/ncalendar3.gif)

## 下载demo：
http://fir.im/7lv4

## 使用方法

#### Gradle
```
compile 'com.necer.ncalendar:ncalendar:2.4.5'
```


#### 布局文件

```
<com.necer.ncalendar.calendar.NCalendar
        android:id="@+id/ncalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultCalendar="Month"
        app:firstDayOfWeek="Sunday"
        app:selectCircleColor="#3388ff">
        
        <!-- 内部需要 RecyclerView 、NestedScrollView 等实现了 NestedScrollingChild 的子类-->

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/white"/>

 </com.necer.ncalendar.calendar.NCalendar>
       

```
#### 注意

```NCalendar```内部的布局需要加上背景颜色，只要是不透明的颜色都可以

```ncalendar:2.x.x```包含一个月日历```MonthCalendar```，一个周日历```WeekCalendar```和一个滑动切换不同视图的```NCalendar```，
单一日历请使用```MonthCalendar```或者```WeekCalendar```。

```NCalendar```日历包含了周日历和月日历，通过滑动切换不同的视图，交互效果仿miui日历，尽可能的实现miui的交互逻辑。

```NCalendar```内部需要一个实现了```NestedScrollingChild```的子类，```RecyclerView```，```NestedScrollView```都可以。

单个的周日历和月日历可以设置默认不选中（即是点击才选中，不点击不选中），但是月周切换必须每页都选中，这样才能体现出月周日期无缝切换的特点，
该日历不支持月周切换的不选中设置



### 交流群

技术交流QQ群：127278900<br/>请添加备注：github、NCalendar、安卓....





### 主要Api


##### 1、监听
```
ncalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChanged(DateTime dateTime) {
                //日历变化回调
            }
        });
```

##### 2、跳转日期
```
参数为 yyyy-MM-dd 格式的日期

ncalendar.setDate("2017-12-31"); 
```
##### 3、回到今天
```
ncalendar.toToday(); 
```

##### 4、日历切换，月-->周  周-->月
```
ncalendar.toWeek();
ncalendar.toMonth();
```
##### 5、上一月、下一月、上一周、下一周
```
ncalendar.toNextPager();
ncalendar.toLastPager();

```

##### 6、添加指示圆点
```
List<String> list = new ArrayList<>();
list.add("2017-09-21");
list.add("2017-10-21");
list.add("2017-10-1");
list.add("2017-10-15");
list.add("2017-10-18");
list.add("2017-10-26");
list.add("2017-11-21");
ncalendar.setPoint(list);

```
##### 7、支持自定义属性，设置NCalendar默认视图、一周的第一天是周日还是周一等
```
NCalendar默认视图,Month 或者 Week，默认是 Month

app:defaultCalendar="Month"
app:defaultCalendar="Week"


设置一周开始是周一还是周日，Sunday 或者 Monday ，默认是周日Sunday

app:firstDayOfWeek="Sunday"
app:firstDayOfWeek="Monday" 

```

##### 8、支持自定义日期区间
```
app:startDate="2010-10-01"
app:endDate="2018-10-31"

或者代码设置

ncalendar.setDateInterval("2017-04-02","2018-01-01");
```


##### 9、单一月日历、周日历设置默认不选中
```
false为不选中，只有点击或者跳转日期才会选中，默认为true

monthcalendar.setDefaultSelect(false);
```



### 支持的属性：


| 属性| 描述|
|:---|:---|
| solarTextColor| 公历日期的文本颜色 |
| lunarTextColor| 农历日期的文本颜色 |
| solarTextSize| 公历日期的文本大小 |
| lunarTextSize| 农历日期的文本大小 |
| hintColor|不是本月的日期文本颜色 |
| selectCircleColor| 选中日期和当天的圆颜色 |
| selectCircleRadius| 选中和当天圆环半径 |
| isShowLunar| 是否显示农历 |
| hollowCircleColor| 选中空心圆中间的颜色|
| hollowCircleStroke| 选中空心圆圆环粗细 |
| calendarHeight|月日历高度 |
| defaultCalendar|NCalendar日历默认视图|
| firstDayOfWeek|每周第一天是周日还是周一|
| duration|自动折叠时间|
| isShowHoliday|是否显示节假日|
| holidayColor|节假日“休”字颜色|
| workdayColor|工作日日“班”字颜色|
| pointSize|指示圆点大小|
| pointColor|指示圆点颜色|
| startDate|日期开始时间|
| endDate|日期结束时间|
| backgroundColor|日历背景颜色|


#### View绘制：http://blog.csdn.net/y12345654321/article/details/73331253
#### 滑动处理：http://blog.csdn.net/y12345654321/article/details/77978148

