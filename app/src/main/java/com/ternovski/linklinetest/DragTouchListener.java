package com.ternovski.linklinetest;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vadim on 2017.
 */

public class DragTouchListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View v) {
        ClipData dragData = null;
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(dragData, shadowBuilder, v, 0);
        return true;
    }

}
