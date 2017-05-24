package cn.com.shequnew.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.list_mesg)
    RecyclerView listMesg;
    @BindView(R.id.activity_admin)
    LinearLayout activityAdmin;
    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        titleBar.setTitle("系统消息");
        titleBar.setLeftImageResource(R.drawable.arrowleft);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(AdminActivity.this);
        listMesg.setLayoutManager(manager);
        adapter.setMessages(getIntent().getBundleExtra("MESG").<EMMessage>getParcelableArrayList("MESG"));
        listMesg.setAdapter(adapter);
        /*final RecycleAdapter*/

    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.HolderView> {
        public List<EMMessage> getMessages() {
            return messages;
        }

        public void setMessages(List<EMMessage> messages) {
            this.messages = messages;
        }

        private List<EMMessage> messages;

        @Override
        public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_list, parent, false);
            return new HolderView(view);
        }

        @Override
        public void onBindViewHolder(HolderView holder, int position) {
            holder.title.setText("系统消息");
            holder.time.setText(DateUtils.getTimestampString(new Date(messages.get(position).getMsgTime())));
            holder.content.setText(((EMTextMessageBody) messages.get(position).getBody()).getMessage());
        }

        @Override
        public int getItemCount() {
            if (messages != null) {
                return messages.size();
            }
            return 0;
        }

        public static class HolderView extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView content;
            public TextView time;

            public HolderView(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                content = (TextView) itemView.findViewById(R.id.content);
                time = (TextView) itemView.findViewById(R.id.time);
            }
        }
    }
}
