package cn.yshstudio.originalproduct.pages.prompt;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.yshstudio.originalproduct.R;

/**
 * 实现loading加载
 * */

public class Loading {

    private Context mContext;
    private View mParentViwe;
    private PopupWindow mWindow = null;
    private String mText = "正在加载";
    private String mFix = "请稍等...";

    public Loading(Context context, View pView) {

        mContext = context;
        mParentViwe = pView;
    }

    public void setText(String pText) {
        mText = pText;
    }

    public void setFix(String pFix) {
        mFix = pFix;
    }
    private void createWinsow(final Boolean isCanBack) {
        // 引入窗口配置文件
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout lLoading = (LinearLayout) inflater.inflate(
                R.layout.loading, null);

        // 创建PopupWindow对象
        mWindow = new PopupWindow(lLoading, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置动画弹出效果
        mWindow.setAnimationStyle(R.style.PopupAnimation);
        // 需要设置一下此参数，点击外边可消失
        // mWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置半透明灰色
        if (isCanBack) {
            mWindow.setBackgroundDrawable(new ColorDrawable(0x7AC0C0C0));
        }
        // 设置点击窗口外边窗口消失
        mWindow.setOutsideTouchable(false);
        mWindow.setClippingEnabled(true);
        // 设置此参数获得焦点，否则无法点击
        // mWindow.setFocusable(true);

        lLoading.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return !isCanBack && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        ((TextView) lLoading.findViewById(R.id.Loading_text)).setText(mText
                + mFix);
    }

    public void show(Boolean isCanBack) {

        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if ((((Activity) mContext).getCurrentFocus() instanceof View)
                && inputMethodManager.isActive())
            inputMethodManager.hideSoftInputFromWindow(((Activity) mContext)
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        // 或
        // InputMethodManager imm =
        // (InputMethodManager)getSystemServic(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(editTextField.getWindowToken(), 0);

        if (!(mWindow instanceof PopupWindow)) {
            createWinsow(isCanBack);
        }

        if (mContext == null || mContext == null
                || mParentViwe == null) {
            return;
        }

        // 显示窗口
        mWindow.showAtLocation(mParentViwe, Gravity.LEFT | Gravity.TOP, 0, 0);
    }

    public void show() {
        show(true);
    }

    public void updateText(String text) {
        if (mWindow != null) {
            ((TextView) mWindow.getContentView()
                    .findViewById(R.id.Loading_text)).setText(text + mFix);
        }
    }

    public void dismiss() {
        if (mWindow != null) {
            mWindow.dismiss();
        }
        mWindow = null;
    }


}
