package cn.com.shequnew.chat.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kongqing on 2017/3/23.
 */

public class BaseActiviy extends AppCompatActivity {
    ProgressDialog dialog;
    protected void dimissProg() {
        dialog.dismiss();
    }

    protected void showProg(String s) {
        if (dialog==null){
            dialog = new ProgressDialog(BaseActiviy.this);
        }
        dialog.setMessage(s);
        dialog.show();
    }
}
