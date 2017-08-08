# NCalendar
一款安卓日历，月视图，周视图滑动切换，时间从1901-01-01到2099-12-31


## 效果图
![](https://github.com/yannecer/NCalendar/blob/master/app/ncalendar.gif)



## 使用方法


### Gradle
```
compile 'com.necer.ncalendar:ncalendar:1.0.8'
```


### 月日历
```
<com.necer.ncalendar.calendar.MonthCalendar
        android:id="@+id/monthCalendar"
        android:layout_width="match_parent"
        android:layout_height="240dp"
        android:background="@color/white"
        app:selectCircleColor= "@android:color/holo_red_light"
        app:pointcolor="#00c8aa"
        app:pointSize="1dp"
        app:solarTextSize= "15sp"/>
        
 //设置监听
 monthCalendar.setOnClickMonthCalendarListener(new OnClickMonthCalendarListener() {
            @Override
            public void onClickMonthCalendar(DateTime dateTime) {
                Toast.makeText(MonthCalendarActivity.this, "选择了：：" + dateTime.toLocalDate(), Toast.LENGTH_SHORT).show();
            }
        });

 monthCalendar.setOnMonthCalendarPageChangeListener(new OnMonthCalendarPageChangeListener() {
            @Override
            public void onMonthCalendarPageSelected(DateTime dateTime) {
                tv_title.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月");
            }
        });
        
        
  //设置表示圆点
  List<String> pointList = new ArrayList<>();
  pointList.add("2017-06-15");
  pointList.add("2017-06-20");
  pointList.add("2017-06-07");
  pointList.add("2017-07-07");
  monthCalendar.setPointList(pointList);
  
  //跳转日期
  monthCalendar.setDate(2017, 10, 1);
```

### 周日历

```
<com.necer.ncalendar.calendar.WeekCalendar
        android:id="@+id/weekCalendar"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        app:selectCircleColor= "@android:color/holo_red_light"
        app:pointcolor=  "#00ffff"
        app:pointSize="1dp"
        app:solarTextSize= "15sp"/>
 //设置监听
 weekCalendar.setOnClickWeekCalendarListener(new OnClickWeekCalendarListener() {
            @Override
            public void onClickWeekCalendar(DateTime dateTime) {
                Toast.makeText(WeekCalendarActivity.this, "选择了：：" + dateTime.toLocalDate(), Toast.LENGTH_SHORT).show();
            }
        });
 weekCalendar.setOnWeekCalendarPageChangeListener(new OnWeekCalendarPageChangeListener() {
            @Override
            public void onWeekCalendarPageSelected(DateTime dateTime) {
                tv_title.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月");

            }
        });
       
  //设置表示圆点
  List<String> pointList = new ArrayList<>();
  pointList.add("2017-06-15");
  pointList.add("2017-06-20");
  pointList.add("2017-06-07");
  pointList.add("2017-07-07");
  weekCalendar.setPointList(pointList);
  
  
  //跳转日期
  weekCalendar.setDate(2017, 10, 1);
  
```

### 周月视图切换

```
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/white">
    <com.necer.ncalendar.calendar.MWCalendar
        android:id="@+id/mWCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:calendarHeight="300dp"
        app:selectCircleColor="#3388ff">
        
        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
            
     </com.necer.ncalendar.calendar.MWCalendar>
 </RelativeLayout>
   
 日历切换类MWCalendar，需要父View为RelativeLayout，子View为NestedScrollingChild的实现类
 
 //设置监听
 mwCalendar.setOnClickCalendarListener(new OnCalendarChangeListener() {
            @Override
            public void onClickCalendar(DateTime dateTime) {

            }

            @Override
            public void onCalendarPageChanged(DateTime dateTime) {

            }
        });
        
        
        
 //小圆点设置
 List<String> pointList = new ArrayList<>();
 pointList.add("2017-05-15");
 pointList.add("2017-06-20");
 pointList.add("2017-06-07"); 
 mwCalendar.setPointList(pointList);
 
 
 
 //跳转日期    
 mwCalendar.setDate(2018, 1, 1);
 
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
| pointSize| 圆点指示器大小 |
| pointcolor| 圆点指示器颜色 |
| hollowCircleColor| 选中空心圆中间的颜色|
| hollowCircleStroke| 选中空心圆圆环粗细 |
| startDateTime| 日历开始时间 "yyyy-MM-dd" |
| endDateTime| 日历截止时间 "yyyy-MM-dd" |
| calendarHeight|日历高度，在MWCalendar中使用 |
| isMultiple|是否多选|
| duration|自动折叠时间|


### 联系我加qq群 ：521039620 

### View的绘制过程参见：http://blog.csdn.net/y12345654321/article/details/73331253 
### 滑动处理参见：http://blog.csdn.net/y12345654321/article/details/73801681


