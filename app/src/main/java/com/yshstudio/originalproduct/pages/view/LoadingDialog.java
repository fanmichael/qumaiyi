package com.yshstudio.originalproduct.pages.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yshstudio.originalproduct.R;

/**
 * Created by Administrator on 2017/6/3 0003.
 */

public class LoadingDialog extends Dialog {
    private TextView mLoadTextView;
    private String mText = "正在加载";
    private String mFix = "请稍等...";
    public LoadingDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        initView(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    private void initView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null);
        mLoadTextView = (TextView)contentView.findViewById(R.id.Loading_text);
        setContentView(contentView, new LinearLayout.LayoutParams(ScreenSize.dp2px(100), ScreenSize.dp2px(80)));
        setCanceledOnTouchOutside(false);
    }

    public void setText(String text) {
        mLoadTextView.setText(text+mFix);
    }

    private void setmFix(String fix) {
        mLoadTextView.setText(mText+fix);
    }
}
