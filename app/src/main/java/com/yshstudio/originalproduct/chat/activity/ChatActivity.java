package com.yshstudio.originalproduct.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.RXbus.RxBus;
import com.yshstudio.originalproduct.chat.eventtype.JSEvent;
import rx.Observer;
import rx.Subscription;

public class ChatActivity extends AppCompatActivity {
    EaseChatFragment fragment;
    Subscription finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        finish = RxBus.getDefault().toObservable(JSEvent.class).subscribe(new Observer<JSEvent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JSEvent jsEvent) {
                if (jsEvent.getEventType().equals("FINISH")) {
                    finish();
                }
            }
        });
        fragment = new EaseChatFragment();
        fragment.setOnRightTitleBarClick(new EaseChatFragment.RightTitleBarClick() {
            @Override
            public void onclik(View v) {
                startActivity(new Intent(ChatActivity.this, GroupDetailsAcitivity.class).putExtra("GROUPID", getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID)));
            }
        });

        Intent intent = getIntent();
        Bundle arg = new Bundle();
        arg.putInt(EaseConstant.EXTRA_CHAT_TYPE, intent.getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, 0));
        /*arg.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);*/
        arg.putString(EaseConstant.EXTRA_USER_ID, intent.getStringExtra(EaseConstant.EXTRA_USER_ID));
        fragment.setArguments(arg);
        getFragmentManager().beginTransaction().add(R.id.frame, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finish != null && finish.isUnsubscribed()) {
            finish.unsubscribe();
        }
    }
}
