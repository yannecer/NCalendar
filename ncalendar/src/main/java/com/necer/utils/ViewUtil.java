package com.necer.utils;

import android.content.Context;
import android.graphics.Rect;
import androidx.core.view.NestedScrollingChild;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.necer.R;

public class ViewUtil {

    public static View getTargetView(Context context, View view) {
        View targetView = view.findViewWithTag(context.getString(R.string.N_factual_scroll_view));

        if (targetView != null && isViewVisible(targetView)) {
            return targetView;
        } else {
            try {
                traverseView(view);
            } catch (ViewUtil.ViewException e) {
                e.printStackTrace();
                return e.getExceptionView();
            }
        }
        return null;
    }


    private static void traverseView(View view) throws ViewException {
        if (view instanceof NestedScrollingChild && isViewVisible(view)) {
            throw new ViewException(view);
        } else if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                if (childAt instanceof NestedScrollingChild && isViewVisible(childAt)) {
                    throw new ViewException(childAt);
                } else {
                    traverseView(childAt);
                }
            }
        }
    }

    //是否在屏幕上面显示  targetView需要显示并且宽度大于父view宽度的一半
    private static boolean isViewVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }

        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            int width = viewGroup.getWidth();
            Rect rect = new Rect();
            boolean globalVisibleRect = view.getGlobalVisibleRect(rect);
            int visibleWidth = rect.width();
            return globalVisibleRect && visibleWidth >= width / 2;
        }
        return view.getGlobalVisibleRect(new Rect());
    }


    public static class ViewException extends Throwable {

        private View view;

        public ViewException(View view) {
            this.view = view;
        }

        public View getExceptionView() {
            return view;
        }
    }


}
