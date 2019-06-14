package com.necer.utils;

import android.support.v4.view.NestedScrollingChild;
import android.view.View;
import android.view.ViewGroup;

public class ViewException extends Throwable{
    private View view;

    public ViewException(View view) {
        this.view = view;
    }

    public View getExceptionView() {
        return view;
    }


    //递归，异常中断递归
    public static void traverseView(View view) throws ViewException {
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
}
