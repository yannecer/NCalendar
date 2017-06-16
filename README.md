# NCalendar
一款安卓日历，月视图，周视图滑动切换


## 效果图
![](https://github.com/yannecer/NCalendar/blob/master/app/ncalendar.gif)



## 使用方法
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
1.xml设置属性时，只需在后一个初始化的日历中设置属性即可，属性数据保存在静态变量中，后初始化的会把前面的覆盖掉，所以设置后面一个即可。

2.对日历操作，比如跳转日期，需要对WeekCalendar和MonthCalendar同时设置，不然下次切换会出现错位。

3.具体使用设置可参见demo

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
| pageSize| 日历可翻页数量，默认左右各1000 |
| pointSize| 圆点指示器大小 |
| pointcolor| 圆点指示器颜色 |
| hollowCircleColor| 选中空心圆中间的颜色|
| hollowCircleStroke| 选中空心圆圆环粗细 |




