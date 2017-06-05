package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import com.yshstudio.originalproduct.pages.config.AppContext;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class ContentFileDetailsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ContentValues> contentValues;
    private List<List<ContentValues>> lists;
    private setOnClickLoction setOnClickLoction;

    public ContentFileDetailsAdapter(Context context, List<ContentValues> contentValues, List<List<ContentValues>> lists, setOnClickLoction setOnClickLoction) {
        this.context = context;
        this.contentValues = contentValues;
        this.lists = lists;
        this.setOnClickLoction = setOnClickLoction;
    }


    @Override
    public int getGroupCount() {
        return contentValues.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lists.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return contentValues.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lists.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (contentValues == null) {
            return convertView;
        }
        GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.comm_user_info_icon);
            holder.nick = (TextView) convertView.findViewById(R.id.comm_name);
            holder.time = (TextView) convertView.findViewById(R.id.comm_time);
            holder.tags = (TextView) convertView.findViewById(R.id.comm_title);
            holder.content = (LinearLayout) convertView.findViewById(R.id.comm_content);
            holder.comContent = (LinearLayout) convertView.findViewById(R.id.comm_content_details);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= groupPosition) {
            return convertView;
        }

        final ContentValues cv = contentValues.get(groupPosition);
        Uri imageUri = Uri.parse(cv.getAsString("icon"));
        ValidData.load(imageUri, holder.icon, 60, 60);
        holder.nick.setText(cv.getAsString("nick"));
        holder.time.setText(cv.getAsString("createtime"));
        holder.tags.setText(cv.getAsString("content"));
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.content(groupPosition, cv.getAsInteger("nid"), AppContext.cv.getAsInteger("id"), cv.getAsInteger("id"));
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (lists == null) {
//            return convertView;
//        }
        ItemHolder itemHolder;
        if (convertView == null) {
            itemHolder = new ItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.text_comm_item, null);
            itemHolder.textContent = (TextView) convertView.findViewById(R.id.text_content);
            itemHolder.txt = (TextView) convertView.findViewById(R.id.text_name);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
//        if (lists == null || lists.get(groupPosition).size() < 0) {
//            return convertView;
//        }
        itemHolder.txt.setText(lists.get(groupPosition).get(childPosition).getAsString("nick") + ": ");
        itemHolder.textContent.setText(lists.get(groupPosition).get(childPosition).getAsString("content"));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public final class GroupHolder {
        public SimpleDraweeView icon;
        public TextView nick;
        public TextView time;
        public TextView tags;
        public LinearLayout content;
        public LinearLayout comContent;

    }

    class ItemHolder {
        public TextView textContent;
        public TextView txt;
    }


    public interface setOnClickLoction {
        public void content(int posit, int nid, int uid, int parent);
    }
}
