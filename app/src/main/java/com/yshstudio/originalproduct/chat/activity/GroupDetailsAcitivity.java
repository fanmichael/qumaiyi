package com.yshstudio.originalproduct.chat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tencent.mm.opensdk.utils.Log;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.chat.RXbus.RxBus;
import com.yshstudio.originalproduct.chat.bean.DeleteGroup;
import com.yshstudio.originalproduct.chat.bean.GroupMember;
import com.yshstudio.originalproduct.chat.bean.GroupMemberRequest;
import com.yshstudio.originalproduct.chat.eventtype.JSEvent;
import com.yshstudio.originalproduct.chat.net.ComUnityRequest;
import com.yshstudio.originalproduct.pages.view.LoadingDialog;

import retrofit2.http.Body;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupDetailsAcitivity extends AppCompatActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.groupMub)
    RecyclerView groupMub;
    @BindView(R.id.group_name)
    TextView groupName;
    @BindView(R.id.group_public)
    TextView groupPublic;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.activity_group_details_acitivity)
    LinearLayout activityGroupDetailsAcitivity;
    String groupid;
    RecyclerViewAdapter adapter;
    private LoadingDialog mDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details_acitivity);
        ButterKnife.bind(this);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupid = getIntent().getStringExtra("GROUPID");
        adapter = new RecyclerViewAdapter();
        adapter.setGroupListener(new RecyclerViewAdapter.GroupListener() {
            @Override
            public void add() {
                EMGroup group = EMClient.getInstance().groupManager().getGroup(groupid);
                if (group.getOwner().equals(UserLodingInFo.getInstance().getMobile().toLowerCase())) {
                    startActivity(new Intent(GroupDetailsAcitivity.this, GroupAddMemberActivity.class).putExtra("GROUPID", groupid));
                } else {
                    Toast.makeText(GroupDetailsAcitivity.this, "无此权限", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void delete() {
                EMGroup group = EMClient.getInstance().groupManager().getGroup(groupid);
                if (group.getOwner().equals(UserLodingInFo.getInstance().getMobile().toLowerCase())) {
                    startActivity(new Intent(GroupDetailsAcitivity.this, GroupDeleteMember.class).putExtra("GROUPID", groupid));
                } else {
                    Toast.makeText(GroupDetailsAcitivity.this, "无此权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
        GridLayoutManager manager = new GridLayoutManager(GroupDetailsAcitivity
                .this, 4);
        groupMub.setLayoutManager(manager);
        groupMub.setAdapter(adapter);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    delete.setClickable(false);
//                    EMGroup group = EMClient.getInstance().groupManager().getGroup(groupid);
//                    if (group.getOwner().equals(UserLodingInFo.getInstance().getMobile().toLowerCase())) {
//                        EMClient.getInstance().groupManager().destroyGroup(groupid);
//                    } else {
//                        EMClient.getInstance().groupManager().leaveGroup(groupid);
//                    }
                    appendLoading();
                    ComUnityRequest.getAPI().deleteGroup(new DeleteGroup().setUid(UserLodingInFo.getInstance().getId()).setGroupid(groupid)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Body>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            delete.setClickable(true);
                        }

                        @Override
                        public void onNext(Body body) {
                        }
                    });
                    handler.sendEmptyMessageDelayed(0, 3000);
                } catch (Exception e) {
                    e.printStackTrace();
                    delete.setClickable(true);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            removeLoadings();
            Toast.makeText(GroupDetailsAcitivity.this, "退出成功", Toast.LENGTH_SHORT).show();
            finish();
            RxBus.getDefault().post(new JSEvent().setEventType("FINISH"));
            super.handleMessage(msg);
        }
    };

    private void appendLoading() {
        mDialog = new LoadingDialog(GroupDetailsAcitivity.this);
        mDialog.setText("正在删除");
        mDialog.show();
    }

    // 关闭loading
    private void removeLoadings() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ComUnityRequest.getAPI().
                getGroupMember(new GroupMemberRequest().setGroupid(groupid)).
                subscribeOn(Schedulers.io()).
                subscribe(new Observer<GroupMember>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final GroupMember groupImageBean) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setGroupMemList(groupImageBean.getData().getList());
                                adapter.notifyDataSetChanged();
                                groupName.setText(groupImageBean.getData().getGroup_info().getGroup_name());
                                groupPublic.setText(groupImageBean.getData().getGroup_info().getGroup_public());
                            }
                        });
                    }
                });
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holer> {
        public interface GroupListener {
            void add();

            void delete();
        }

        public GroupListener getG() {
            return g;
        }

        public void setGroupListener(GroupListener g) {
            this.g = g;
        }

        private GroupListener g;

        public List<GroupMember.DataBean.ListBean> getGroupMemList() {
            return groupMemList;
        }

        public void setGroupMemList(List<GroupMember.DataBean.ListBean> groupMemList) {
            this.groupMemList = groupMemList;
        }

        List<GroupMember.DataBean.ListBean> groupMemList;

        @Override
        public Holer onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_mub_item, null);
            return new Holer(view);
        }

        @Override
        public void onBindViewHolder(Holer holder, int position) {
            if (groupMemList == null) {
                switch (position) {
                    case 0:
                        holder.nick.setVisibility(View.GONE);
                        holder.avatar.setImageURI(Uri.parse("res://" +
                                holder.avatar.getContext().getPackageName()
                                + "/" + R.drawable.add));
                        holder.avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (g != null) {
                                    g.add();
                                }
                            }
                        });
                        break;
                    case 1:
                        holder.nick.setVisibility(View.GONE);
                        holder.avatar.setImageURI(Uri.parse("res://" +
                                holder.avatar.getContext().getPackageName()
                                + "/" + R.drawable.delete));
                        holder.avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (g != null) {
                                    g.delete();
                                }
                            }
                        });
                        break;
                }
            } else {
                if (position == groupMemList.size()) {
                    holder.nick.setVisibility(View.GONE);
                    holder.avatar.setImageURI(Uri.parse("res://" +
                            holder.avatar.getContext().getPackageName()
                            + "/" + R.drawable.add));
                    holder.avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (g != null) {
                                g.add();
                            }
                        }
                    });
                } else if (position == groupMemList.size() + 1) {
                    holder.nick.setVisibility(View.GONE);
                    holder.avatar.setImageURI(Uri.parse("res://" +
                            holder.avatar.getContext().getPackageName()
                            + "/" + R.drawable.del));
                    holder.avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (g != null) {
                                g.delete();
                            }
                        }
                    });
                }
                if (position < groupMemList.size()) {
                    holder.nick.setText(groupMemList.get(position).getNick());
                    holder.nick.setVisibility(View.VISIBLE);
                    holder.nick.setSingleLine();
                    holder.nick.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.nick.setMarqueeRepeatLimit(-1);
                    holder.avatar.setImageURI(groupMemList.get(position).getIcon());
                    holder.avatar.setClickable(false);
                }
            }

        }

        private void doMember(SimpleDraweeView s, GroupListener g, int type) {

        }

        @Override
        public int getItemCount() {
            if (groupMemList != null) {
                return groupMemList.size() + 2;
            }
            return 2;
        }

        public static class Holer extends RecyclerView.ViewHolder {
            SimpleDraweeView avatar;
            TextView nick;

            public Holer(View itemView) {
                super(itemView);
                avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
                nick = (TextView) itemView.findViewById(R.id.nick);
            }
        }
    }
}
