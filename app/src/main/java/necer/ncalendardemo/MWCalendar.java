package necer.ncalendardemo;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.necer.ncalendar.calendar.MonthCalendar;
import com.necer.ncalendar.calendar.WeekCalendar;
import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.utils.Utils;
import com.necer.ncalendar.view.MonthView;

/**
 * Created by necer on 2017/6/13.
 */

public class MWCalendar extends FrameLayout{


  //  private RelativeLayout rlCalendar;
    private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;




    public MWCalendar(@NonNull Context context) {
        this(context, null);

    }

    public MWCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MWCalendar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_wm, this);

      //  rlCalendar = (RelativeLayout) findViewById(R.id.rlCalendar);
        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);
        monthCalendar = (MonthCalendar) findViewById(R.id.monthCalendar);








    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            onCalendarScroll(distanceY);

            return true;
        }
    });

    private void onCalendarScroll(float distanceY) {







        MonthView monthView = monthCalendar.getCurrentMothView();
       // distanceY = Math.min(distanceY, mAutoScrollDistance);
        float calendarDistanceY = distanceY / 5.0f;

       // int row = monthView.getWeekRow() ;
        int row = 3;
        int rowHeigh = (int) Utils.dp2px(getContext(), 50);

        int calendarTop = -row * rowHeigh;

       // MyLog.d("calendarTop:::" + calendarTop);

        float calendarY = monthCalendar.getY() - calendarDistanceY * row;

        calendarY = Math.min(calendarY, 0);
        MyLog.d("calendarY:111:" + calendarY);



        calendarY = Math.max(calendarY, calendarTop);

        MyLog.d("calendarY:222:" + calendarY);

      //  MyLog.d("calendarY::" + calendarY);
        monthCalendar.setY(calendarY);
       /* float scheduleY = recycler.getY() - distanceY;
        scheduleY = Math.min(scheduleY, monthCalendar.getHeight());
        scheduleY = Math.max(scheduleY, scheduleTop);
        recycler.setY(scheduleY);*/





    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        int action = ev.getAction();


       /* switch (action) {
            case MotionEvent.ACTION_DOWN:
                MyLog.d("ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                MyLog.d("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                MyLog.d("ACTION_UP");
                break;
        }*/






        return mGestureDetector.onTouchEvent(ev);
    }


    /*   @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {





        return super dispatchTouchEvent();
    }*/


    /*@Override
    public boolean onInterceptHoverEvent(MotionEvent event) {





        return false;
    }*/
}
