package com.yshstudio.originalproduct.chat.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.yshstudio.originalproduct.R;


/**
 * Created by kongqing on 17-3-14.
 */

@SuppressLint("AppCompatCustomView")
public class SelectView extends ImageView {
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    private boolean isSelect;

    public SelectView(Context context) {
        super(context);
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelect) {
            setBackgroundResource(R.drawable.select);
        } else {
            setBackgroundResource(R.drawable.unselect);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isSelect) {
                    setSelect(false);
                } else {
                    setSelect(true);
                }
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);


    }
}
