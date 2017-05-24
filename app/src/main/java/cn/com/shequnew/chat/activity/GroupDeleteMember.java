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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.UserLodingInFo;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.shequnew.R;
import cn.com.shequnew.chat.bean.GroupMember;
import cn.com.shequnew.chat.bean.GroupMemberRequest;
import cn.com.shequnew.chat.net.ComUnityRequest;
import cn.com.shequnew.chat.view.SelectView;
import rx.Observer;
import rx.schedulers.Schedulers;

public class GroupDeleteMember extends AppCompatActivity {

    @BindView(R.id.title_bar)
    EaseTitleBar titleBar;
    @BindView(R.id.group_member)
    RecyclerView groupMember;
    @BindView(R.id.activity_group_delete_member)
    LinearLayout activityGroupDeleteMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_delete_member);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        titleBar.setTitle("群组成员");
        titleBar.setRightText("删除");
        titleBar.setLeftImageResource(R.drawable.arrowleft);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getDeleteList() != null && adapter.getDeleteList().size() != 0) {
                    for (GroupMember.DataBean.ListBean m : adapter.getDeleteList()) {
                        try {
                            EMClient.getInstance().groupManager().removeUserFromGroup(groupid, m.getMobile());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }

                    }
                    getGroupMember();
                } else {
                    Toast.makeText(GroupDeleteMember.this, "请选择要删除的成员", Toast.LENGTH_SHORT).show();
                }
            }
        });
        groupid = getIntent().getStringExtra("GROUPID");
        adapter = new RecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        groupMember.setLayoutManager(manager);
        groupMember.setAdapter(adapter);
        getGroupMember();
    }

    private String              groupid;
    private RecyclerViewAdapter adapter;


    private void getGroupMember() {
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
                                adapter.addMember(groupImageBean.getData().getList());
                            }
                        });
                    }
                });
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holer> {
        public interface GroupListener {
            void addMember(String id);
        }

        public GroupListener getG() {
            return g;
        }

        public void addMemberListener(GroupListener g) {
            this.g = g;
        }

        public List<GroupMember.DataBean.ListBean> getDeleteList() {
            return deleteList;
        }

        private List<GroupMember.DataBean.ListBean> deleteList;
        private GroupListener                       g;

        public List<GroupMember.DataBean.ListBean> getMember() {
            return member;
        }

        public void addMember(List<GroupMember.DataBean.ListBean> member) {

            this.member = member;
            notifyDataSetChanged();
        }

        List<GroupMember.DataBean.ListBean> member;

        @Override
        public Holer onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member_delete_item, parent, false);
            return new Holer(view);
        }

        @Override
        public void onBindViewHolder(final Holer holder, final int position) {
            if (!member.get(position).getMobile().equals(UserLodingInFo.getInstance().getMobile())) {
                holder.avatar.setImageURI(member.get(position).getIcon());
                holder.nick.setText(member.get(position).getNick());
                holder.deleteMember.setSelect(false);
                holder.deleteMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deleteList == null) {
                            deleteList = new ArrayList<GroupMember.DataBean.ListBean>();
                        }
                        if (holder.deleteMember.isSelect()) {
                            deleteList.add(member.get(position));
                        } else {
                            deleteList.remove(member.get(position));
                        }
                    }
                });
            }
        }

        private void doMember(SimpleDraweeView s, GroupAddMemberActivity.RecyclerViewAdapter.GroupListener g) {

        }

        @Override
        public int getItemCount() {
            if (member != null) {
                return member.size() - 1;
            }
            return 0;
        }

        public static class Holer extends RecyclerView.ViewHolder {
            SimpleDraweeView avatar;
            TextView         nick;
            SelectView deleteMember;

            public Holer(View itemView) {
                super(itemView);
                avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
                nick = (TextView) itemView.findViewById(R.id.nick);
                deleteMember = (SelectView) itemView.findViewById(R.id.delete_member);
            }
        }
    }
}
