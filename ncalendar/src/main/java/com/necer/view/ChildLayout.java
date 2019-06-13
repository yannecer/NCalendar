package com.necer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.necer.R;

/**
 * Created by necer on 2018/11/9.
 */
public class ChildLayout extends FrameLayout {

    protected View targetView;//实际滑动的view，即RecyclerView等
    private int monthHeight;
    private int weekHeight;


    public ChildLayout(@NonNull Context context, AttributeSet attrs, int monthHeight) {
        super(context, attrs);
        this.monthHeight = monthHeight;
        this.weekHeight = monthHeight / 5;
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        ViewGroup parent = (ViewGroup) child.getParent();
        if (parent != null) {
            parent.removeView(child);
        }
        super.addView(child, params);

        //通过tag查找，找不到递归查找，再找不到抛异常
//        targetView = findViewWithTag(getResources().getString(R.string.factual_scroll_view));
//        if (targetView == null) {
//            try {
//                traverseView(child);
//            } catch (ViewException e) {
//                e.printStackTrace();
//                targetView = e.getExceptionView();
//            }
//        }
//        if (targetView == null) {
//            throw new RuntimeException("NCalendar需要实现了NestedScrollingChild的子类");
//        }
    }


    //递归，异常中断递归
    private void traverseView(View view) throws ViewException {
        if (view instanceof NestedScrollingChild) {
            throw new ViewException(view);
        } else if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt instanceof NestedScrollingChild) {
                    throw new ViewException(childAt);
                } else {
                    traverseView(childAt);
                }
            }
        }
    }


    public boolean canScrollVertically(int direction) {
        if (targetView == null) {
            try {
                traverseView(getChildAt(0));
            } catch (ViewException e) {
                e.printStackTrace();
                targetView = e.getExceptionView();
            }
        }

        return targetView.canScrollVertically(-1);
    }


    public boolean isMonthState() {
        return ((int) getY()) >= monthHeight;
    }

    public boolean isWeekState() {
        return ((int) getY()) <= weekHeight;
    }


    public int getChildLayoutOffset() {
        return monthHeight - weekHeight;
    }


    public static class ViewException extends Exception {
        private View view;

        public ViewException(View view) {
            this.view = view;
        }

        public View getExceptionView() {
            return view;
        }
    }


}
