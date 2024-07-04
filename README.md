# 安卓日历 NCalendar

## 特点:

- 月日历、周日历、月周切换
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
- 支持内部TargetView为任意View
- 支持日历拉伸功能


## 效果图
|                                       效果1                                        |  效果2|  效果3|
|:--------------------------------------------------------------------------------:|:--:|:---:|
| ![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/miui9_gif.gif) |![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/miui10_gif.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/emui_gif.gif)|

|周固定，下拉刷新|日历和子view添加其他view|自定义日历界面（LigaturePainter）|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/week_hold.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/addview.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/LigaturePainter.png)|

|默认不选中|默认多选|自定义日历界面（TicketPainter）|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/111.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/222.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/TicketPainter.png)|


|ViewPager|普通View|demo功能预览|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/viewpager.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/general.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/demo.png)|


|日历拉伸|
|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/doc/pic/Stretch.gif)|

# 下载demo：
[下载demo](https://github.com/yannecer/NCalendar/releases/download/6.0.0/app-debug6.0.apk)


## 使用方法


#### 项目 build.gradle 文件, Gradle7.0以上为 settings.gradle , 添加 jitpack.io

```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

```

#### Gradle
```
implementation 'com.github.yannecer：NCalendar：6.0.0'

```

#### 简单使用

```
     月周切换日历
     <com.necer.calendar.NCalendar
        android:id="@+id/monthCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    
```

#### 单个月日历，单个周日历


```
   月日历
       <com.necer.calendar.NCalendar
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultCalendar="month"/>

   周日历
       <com.necer.calendar.NCalendar
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultCalendar="week"/>
        
   单独月日历和周日里，通过属性 defaultCalendar 控制，默认为月，日历高度通过 calendarHeight 控制，默认 300dp 
   周日历高度为 calendarHeight/5 默认为60dp，通过修改 calendarHeight 来修改月日历和周日历的高度 

```



***

## 自定义属性
```
6.0版本 自定义属性  示例 app:defaultCalendar="week"

defaultCalendar -- 默认展示的日历 月->month 周->week  默认月
calendarHeight -- 日历中 月日历的高度 默认300dp 周日历高度为 calendarHeight/5
firstDayOfWeek -- 日历一周开始是周日或周一 周日->monday 周一->monday  默认周日
weekBarHeight -- 日历顶部星期的高度
stretchCalendarEnable -- 日历是否可拉伸 
showNumberBackground -- 是否显示数字背景 
todayCheckedBackground -- 今天选中的背景 shape drawable
todayCheckedSolarTextColor -- 今天选中时的公历字体颜色
......
```
#### [更多自定义属性](https://github.com/yannecer/NCalendar/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E5%B1%9E%E6%80%A7)

***

## 日历Api
```
日历提供了各种可能用到的方法、回调，6.x版本增加了日历日期变化的行为参数，区分了各种引起日历变化的不同的操作

设置选中模式 
void setCheckMode(CheckModel checkModel);

跳转日期
void jumpDate(String formatDate);

上一页 上一周 上一月
void toLastPager();

下一页 下一周 下一月
void toNextPager();

回到今天
void toToday();

回到周状态
void toWeek();

......

```
#### [更多日历Api](https://github.com/yannecer/NCalendar/wiki/%E6%97%A5%E5%8E%86Api)

```
日历回调
1、单选回调

        nCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
               
            }
        });
        
2、多选回调
 nCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
            }

        });
        
 3、日历状态回调 周日历，月日历 状态变化
  nCalendar.setOnCalendarStateChangedListener(new OnCalendarStateChangedListener() {
            @Override
            public void onCalendarStateChange(CalendarState calendarState) {
                
            }
        });

```
### [日历回调](https://github.com/yannecer/NCalendar/tree/master/ncalendar/src/main/java/com/necer/listener)


***
## 日历设置时间标记、替换文字等Api

```
日历设置时间标记、替换文字的Api，只对InnerPainter有效，如果是自定义日历UI，则这些方法需要自己实现，
可参考InnerPainter相关代码

```
### [日历设置时间标记、替换文字](https://github.com/yannecer/NCalendar/wiki/%E6%97%A5%E5%8E%86%E8%AE%BE%E7%BD%AE%E6%A0%87%E8%AE%B0%E3%80%81%E6%9B%BF%E6%8D%A2%E6%96%87%E5%AD%97)
***

## 自定义日历UI
```
如果自定义属性不能满足日历ui要求，可以使用自定义页面实现个性化需求，项目提供了两种自定义UI的方式，
1、实现CalendarPainter接口，通过Canvas绘制
```
### [自定义日历UI](https://github.com/yannecer/NCalendar/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E6%97%A5%E5%8E%86UI)

***


### 交流群

技术交流QQ群：127278900 (请先仔细看文档，然后再进群发问，上方加粗字体有下载demo链接)



***
### 常见问题
```
1、Activity初始化onCreate中调用jumpDate闪退
   页面初始化时，日历还未完成初始化，可以使用 setInitializeDate 方法完成初始页面的日期选定，或者使用日历对象post方法设置jumpdate
           miui10Calendar.post(new Runnable() {
            @Override
            public void run() {
                miui10Calendar.jumpDate("2021-01-01");
            }
        });
        
2、日历UI问题，请使用自定义CalendarPainter，简单的做法是，复制库中InnerPainter，修改绘制的部分，然后给日历设置CalendarPainter
   nCalendar.setCalendarPainter(myCalendarPainter);

```

## 感谢：

农历和节气数据工具类来自 [Hutool](https://github.com/dromara/hutool)<br/>


***

## 更新日志

* 6.0.0<br/> 1、日历中日期使用```java.time.LocalDate```，因此```minSdkVersion```最低版本必须为```26```<br/>
             2、```NCalendar```改为```kotlin```编写
             3、去除```MonthCalendar```和```WeekCalendar```,单独月日历和周日历整合进```NCalendar```<br/>
             4、动画切换改为```Draw```绘制<br/>
             5、加入阻断动画和快速滑动处理<br/>
             6、重构减少近半的代码量<br/>
             7、农历、节气等数据改为```Hutool```工具来<br/>
             8、去除```miui9```等几种动画效果，保留了最普遍的```Miui10```的效果<br/>
             9、```WeekBar```改为日历内部

* 5.0.2<br/> 修复Android9日期变化回调多次的bug，增加2021年法定休班日
* 5.0.1<br/> 修复2020年腊月二十九为除夕的描述
* 5.0.0<br/> 重写InnerPainter，增加大量属性、优化跳转逻辑等
* 4.4.1<br/> 新增跳转月份的方法
* 4.4.0<br/> 新增适配器模式自定义页面
* 4.3.8<br/> 新增月日历上下月是否可点击的属性 isLastNextMonthClickEnable
* 4.3.7<br/> 修复选中月初月末，实际月份回调bug
* 4.3.6<br/> 增加数字背景以及渐变效果
* 4.3.4<br/> 修复周状态下滑动卡顿的bug
* 4.3.3<br/> 修复莫名跳转2099年的bug，增加是否每个月都是6行的属性
* 4.3.2<br/> 增加日历拉伸功能
* 4.2.0<br/> 支持任意非滑动的View，ViewPger等
* 4.1.2<br/> 完善LigaturePainter
* 4.1.1<br/> 修改选中模式为枚举，demo增加了两种自定义CalendarPainter
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
   
   

