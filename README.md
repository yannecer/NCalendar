# NCalendar


#### 日历改版，新增加了 miui10，华为日历 两种月周交互 ，现在一共有 miui9、miui10、华为日历 三种，
#### 仿miui10的日历不是太完美，这次新增了不少属性，修复了一些bug，初版更新，后续完善中，README文件后续更新
#### 本次更新把 NCalendar 写成了抽象类，负责主要逻辑，Miui9Calendar、Miui10Calendar、EmuiCalendar根据不同的交互，返回一些必要的值








一款仿miui日历，月视图，周视图滑动切换，时间从1901-01-01到2099-12-31

支持自定义日期区间

支持农历，节假日，指示圆点，默认视图，周的第一天设置等

支持单一月日历、周日历设置默认选中和默认不选中


## 效果图

![](https://github.com/yannecer/NCalendar/blob/master/app/new_.gif)

## 下载demo：
https://github.com/yannecer/NCalendar/blob/master/app/app-debug.apk

## 使用方法


#### 布局文件

```
miui9 和 钉钉日历
     android:id="@+id/miui9Calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:calendarHeight="300dp">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.necer.calendar.Miui9Calendar>
    
 miui10（不完美）
    
    <com.necer.calendar.Miui10Calendar
        android:id="@+id/miui10Calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:bgCalendarColor="#f5f5f5"
        app:holidayColor="#519EDC"
        app:solarHolidayTextColor="#519EDC"
        app:solarTermTextColor="#519EDC"
        app:lunarHolidayTextColor="#519EDC"
        app:todaySolarTextColor="#398FE9"
        app:selectCircleColor="#398FE9"
        app:bgChildColor="#F5f5f5">

        <android.support.v4.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">
           
        </android.support.v4.widget.NestedScrollView>
    </com.necer.calendar.Miui10Calendar>


华为 和 365日历
    <com.necer.calendar.EmuiCalendar
        android:id="@+id/emuiCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:bgCalendarColor="#ffffff"
        app:holidayColor="#F29B38"
        app:solarHolidayTextColor="#F29B38"
        app:solarTermTextColor="#F29B38"
        app:lunarHolidayTextColor="#F29B38"
        app:todaySolarTextColor="#F29B38"
        app:selectCircleColor="#F29B38"
        app:bgChildColor="#F5f5f5">

        <android.support.v4.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            <TextView
                android:id="@+id/tv_lunar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="13sp"
                android:layout_margin="15dp"
                android:background="#f5f5f5"
                android:textColor="#333333" />

        </android.support.v4.widget.NestedScrollView>
    </com.necer.calendar.EmuiCalendar>

```
#### 注意


```NCalendar```内部需要一个实现了```NestedScrollingChild```的子类，```RecyclerView```，```NestedScrollView```都可以。

单个的周日历和月日历可以设置默认不选中（即是点击才选中，不点击不选中），但是月周切换必须每页都选中，这样才能体现出月周日期无缝切换的特点，
该日历不支持月周切换的不选中设置



### 交流群

技术交流QQ群：127278900<br/>请添加备注：github、NCalendar、安卓....





### 主要Api


##### 1、监听
```
nCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarDateChanged(NDate date) {
               //日历回调 NDate包含公历、农历、节气、节假日、闰年等信息
            }
               
            @Override
            public void onCalendarStateChanged(boolean isMonthSate) {
               //日历状态回调， 月->周 isMonthSate返回false ，反之返回true   
            }
        });
```

##### 2、跳转日期
```
参数为 yyyy-MM-dd 格式的日期

ncalendar.jumpDate("2017-12-31"); 
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
List<String> pointList = Arrays.asList("2018-10-01", "2018-11-19", "2018-11-20", "2018-05-23", "2019-01-01");
ncalendar.setPointList(list);

```





### 支持的属性：
新增了不少属性，待整理

#### View绘制：http://blog.csdn.net/y12345654321/article/details/73331253
#### 滑动处理：http://blog.csdn.net/y12345654321/article/details/77978148

