package com.necer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by necer on 2018/11/9.
 */
public class ChildLayout extends FrameLayout {


    protected View targetView;//嵌套滑动的目标view，即RecyclerView等


    public ChildLayout(@NonNull Context context) {
        super(context);
    }


    @Override
    public void addView(View child) {
        ViewGroup parent = (ViewGroup) child.getParent();
        if (parent != null) {
            parent.removeView(child);
        }
        super.addView(child);

        targetView = getNestedScrollingChild(child);

    }

    private View getNestedScrollingChild(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt instanceof NestedScrollingChild) {
                    return childAt;
                } else {
                    getNestedScrollingChild(((ViewGroup) view).getChildAt(i));
                }
            }
        }
        return null;
    }


    public boolean canScrollVertically(int direction) {
        return ViewCompat.canScrollVertically(targetView, direction);
    }
}
