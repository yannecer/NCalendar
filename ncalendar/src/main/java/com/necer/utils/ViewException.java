package com.necer.utils;

import android.graphics.Rect;
import android.support.v4.view.NestedScrollingChild;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.necer.calendar.NCalendar;

public class ViewException extends Throwable {
    private View view;

    public ViewException(View view) {
        this.view = view;
    }

    public View getExceptionView() {
        return view;
    }


    //递归，异常中断递归
    public static void traverseView(View view) throws ViewException {
        if (view instanceof NestedScrollingChild && isViewVisible(view)) {
            throw new ViewException(view);
        } else if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt instanceof NestedScrollingChild && isViewVisible(view)) {
                    throw new ViewException(childAt);
                } else {
                    traverseView(childAt);
                }
            }
        }
    }
    //是否在屏幕上面显示
    public static boolean isViewVisible(View view) {

        ViewParent parent = view.getParent();

        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            int parentWidth = viewGroup.getWidth();
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            int viewVisibleWidth = rect.width();

            return viewVisibleWidth > parentWidth / 2;

        }



        return view.getLocalVisibleRect(new Rect());
    }
}
