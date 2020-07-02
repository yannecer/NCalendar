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
 - 支持内部TargetView为任意View
 - 支持日历拉伸功能
 - 支持适配器模式自定义日历

## 效果图 
|Miui9Calendar|Miui10Calendar|EmuiCalendar|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/miui9_gif.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/miui10_gif.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/emui_gif.gif)|

|周固定，下拉刷新|日历和子view添加其他view|自定义日历界面（LigaturePainter）|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/week_hold.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/addview.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/LigaturePainter.png)|

|默认不选中|默认多选|自定义日历界面（TicketPainter）|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/111.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/222.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/TicketPainter.png)|


|ViewPager|普通View|demo功能预览|
|:---:|:---:|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/viewpager.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/general.gif)|![](https://github.com/yannecer/NCalendar/blob/master/app/demo.png)|


|日历拉伸|
|:---:|
|![](https://github.com/yannecer/NCalendar/blob/master/app/Stretch.gif)|

# 下载demo：
[下载demo](https://github.com/yannecer/NCalendar/releases/download/4.3.0/4.3.0.apk)


## 使用方法


#### 项目build文件

```
    android {

       compileOptions {
           sourceCompatibility JavaVersion.VERSION_1_8
           targetCompatibility JavaVersion.VERSION_1_8
       }
    ...
    }

```

#### Gradle
```
implementation 'com.necer.ncalendar:ncalendar:5.0.0'   

```

#### 简单使用

```
    miui9 和 钉钉日历
    <com.necer.calendar.Miui9Calendar
        android:id="@+id/miui9Calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
        <<androidx.recyclerview.widget.RecyclerView
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
        android:layout_height="300dp" />

   周日历
   <com.necer.calendar.WeekCalendar
        android:layout_width="match_parent"
        android:layout_height="50dp" />

```

### [详细用法](https://github.com/yannecer/NCalendar/wiki/%E8%AF%A6%E7%BB%86%E7%94%A8%E6%B3%95)

***

## 自定义属性
```
5.x版本更新了大量的自定义属性
```
### [自定义属性](https://github.com/yannecer/NCalendar/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E5%B1%9E%E6%80%A7)

***

## 日历Api
```
日历提供了各种可能用到的方法、回调，5.x版本增加了日历日期变化的行为参数，区分了各种引起日历变化的不同的操作
```
### [日历Api](https://github.com/yannecer/NCalendar/wiki/%E6%97%A5%E5%8E%86Api)
### [日历Api相关类说明](https://github.com/yannecer/NCalendar/wiki/Api%E7%9B%B8%E5%85%B3%E7%B1%BB%E8%AF%B4%E6%98%8E)

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
2、继承CalendarAdapter抽象类，和ListView的BaseAdapter用法相似
```
### [自定义日历UI](https://github.com/yannecer/NCalendar/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E6%97%A5%E5%8E%86UI)

***


### 交流群

技术交流QQ群：127278900 (请先仔细看文档，然后再进群发问，上方加粗字体有下载demo链接)



***




## 感谢：

项目中日期计算使用  [joda-time](https://github.com/JodaOrg/joda-time)<br/>
感谢同事 **魏昌琳**   提出的优化建议<br/>
感觉农历和节气数据工具类的作者<br/>


***

## 更新日志
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
   
   

