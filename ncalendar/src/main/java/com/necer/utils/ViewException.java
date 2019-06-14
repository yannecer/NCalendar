package com.necer.utils;

import android.view.View;

public class ViewException {
    private View view;

    public ViewException(View view) {
        this.view = view;
    }

    public View getExceptionView() {
        return view;
    }

}
