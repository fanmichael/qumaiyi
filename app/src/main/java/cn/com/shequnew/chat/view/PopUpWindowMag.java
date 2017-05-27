package cn.com.shequnew.chat.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import cn.com.shequnew.R;


/**
 * Created by kongqing on 17-3-14.
 */

public class PopUpWindowMag {
    public interface DeleteCallback {
        void cancle();

        void delete();
    }

    public static void popView(Context context, final DeleteCallback callback, View opition) {
        View   view   = LayoutInflater.from(context).inflate(R.layout.popview, null);
        Button cancle = (Button) view.findViewById(R.id.cancel);
        Button delete = (Button) view.findViewById(R.id.delete);

        final PopupWindow popupWindow = new PopupWindow(view, 320, 120);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.cancle();
                popupWindow.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.delete();
                popupWindow.dismiss();
            }
        });
        int[] location = new int[2];
        opition.getLocationOnScreen(location);
        popupWindow.showAtLocation(opition, Gravity.NO_GRAVITY, location[0] + opition.getWidth() / 2 - popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight());

    }
}
