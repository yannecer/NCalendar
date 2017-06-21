package necer.ncalendardemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.necer.ncalendar.calendar.MonthCalendar;
import com.necer.ncalendar.calendar.WeekCalendar;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/6/14.
 */

public class MWCalendar extends LinearLayout implements NestedScrollingParent {

    private WeekCalendar weekCalendar;
    private MonthCalendar monthCalendar;
    private RecyclerView recyclerView;
    private OverScroller mScroller;

    public static final int OPEN = 100;
    public static final int CLOSE = 200;
    private static int STATE = 100;//默认开
    private int rowHeigh;



    public MWCalendar(Context context) {
        this(context, null);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MWCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onStopNestedScroll(View target) {
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == rowHeigh * 5) {
            return;
        }

        if (STATE == OPEN) {
            if (scrollY > 100) {
                startScroll(scrollY,rowHeigh * 5 - scrollY,300);
            } else {
                startScroll(scrollY, -scrollY, 300);
            }
        }
        if (STATE == CLOSE) {
            if (scrollY < rowHeigh * 5 - 100) {
                startScroll(scrollY, -scrollY, 300);
            } else {
                startScroll(scrollY, rowHeigh * 5 - scrollY, 300);
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        boolean hiddenMonthCalendar = dy > 0 && getScrollY() < rowHeigh * 5;
        boolean showMonthCalendar = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenMonthCalendar || showMonthCalendar) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= rowHeigh * 5) return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rowHeigh = monthCalendar.getRowHeigh();
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = getMeasuredHeight() - rowHeigh;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        monthCalendar = (MonthCalendar) findViewById(R.id.monthCalendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        monthCalendar.setOnClickMonthCalendarListener(new OnClickMonthCalendarListener() {
            @Override
            public void onClickMonthCalendar(DateTime dateTime) {
                weekCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
                if (onClickCalendarListener != null) {
                    onClickCalendarListener.onClickCalendar(dateTime);
                }
            }
        });

        monthCalendar.setOnMonthCalendarPageChangeListener(new OnMonthCalendarPageChangeListener() {
            @Override
            public void onMonthCalendarPageSelected(DateTime dateTime) {
                if (weekCalendar != null) {
                    weekCalendar.jumpDate(dateTime,true);
                }
                if (onClickCalendarListener != null) {
                    onClickCalendarListener.onCalendarPageChange(dateTime);
                }
            }
        });

    }

    private void startScroll(int startY,int dy,int duration) {
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, rowHeigh * 6);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > rowHeigh * 5) {
            y = rowHeigh * 5;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        int scrollY = getScrollY();
        if (scrollY == 0) {
            STATE = OPEN;
            weekCalendar.setVisibility(INVISIBLE);
        } else if (scrollY == 5 * rowHeigh) {
            STATE = CLOSE;
            weekCalendar.setVisibility(VISIBLE);
        } else {
            int weekRow = monthCalendar.getCurrentMothView().getWeekRow();
            weekCalendar.setVisibility(scrollY >= weekRow  * rowHeigh ? VISIBLE : INVISIBLE);
        }

        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void setWeekCalendar(WeekCalendar weekCalendar) {
        this.weekCalendar = weekCalendar;
        weekCalendar.setOnClickWeekCalendarListener(new OnClickWeekCalendarListener() {
            @Override
            public void onClickWeekCalendar(DateTime dateTime) {
                monthCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
                if (onClickCalendarListener != null) {
                    onClickCalendarListener.onClickCalendar(dateTime);
                }
            }
        });
        weekCalendar.setOnWeekCalendarPageChangeListener(new OnWeekCalendarPageChangeListener() {
            @Override
            public void onWeekCalendarPageSelected(DateTime dateTime) {
                monthCalendar.jumpDate(dateTime,true);
                if (onClickCalendarListener != null) {
                    onClickCalendarListener.onCalendarPageChange(dateTime);
                }

            }
        });
    }


    public void setDate(int year, int month, int day) {
        monthCalendar.setDate(year,month,day);
        weekCalendar.setDate(year,month,day);
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public interface OnCalendarChangeListener {
        void onClickCalendar(DateTime dateTime);

        void onCalendarPageChange(DateTime dateTime);
    }

    private OnCalendarChangeListener onClickCalendarListener;
    public void setOnClickCalendarListener(OnCalendarChangeListener onClickCalendarListener) {
        this.onClickCalendarListener = onClickCalendarListener;
    }


    public void open() {
        if (STATE == CLOSE) {
            startScroll(rowHeigh * 5, -rowHeigh * 5, 300);
        }
    }

    public void close() {
        if (STATE == OPEN) {
            startScroll(0, rowHeigh * 5 , 300);
        }
    }

}
