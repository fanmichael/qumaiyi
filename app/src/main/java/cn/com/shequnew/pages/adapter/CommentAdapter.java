package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ShopDetailsActivity;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class CommentAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private List<List<ContentValues>> lists;
    private Context context;
    private LayoutInflater mInflater;
    private setOnClickLoction setOnClickLoction;

    public CommentAdapter(Context context, List<ContentValues> contentValues, List<List<ContentValues>> lists,setOnClickLoction setOnClickLoction) {
        this.context = context;
        this.contentValues = contentValues;
        this.lists = lists;
        this.setOnClickLoction=setOnClickLoction;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contentValues != null ? contentValues.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (contentValues == null) {
            return convertView;
        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.comment_item, null);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.comm_user_info_icon);
            holder.nick = (TextView) convertView.findViewById(R.id.comm_name);
            holder.time = (TextView) convertView.findViewById(R.id.comm_time);
            holder.tags = (TextView) convertView.findViewById(R.id.comm_title);
            holder.content = (LinearLayout) convertView.findViewById(R.id.comm_content);
            holder.comContent = (LinearLayout) convertView.findViewById(R.id.comm_content_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        Uri imageUri = Uri.parse(cv.getAsString("icon"));
        ValidData.load(imageUri, holder.icon, 60, 60);
        holder.nick.setText(cv.getAsString("nick"));
        holder.time.setText(cv.getAsString("createtime"));
        holder.tags.setText(cv.getAsString("content"));
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.content(position,cv.getAsInteger("nid"), AppContext.cv.getAsInteger("id"),cv.getAsInteger("id"));
            }
        });
        if (lists.get(position).size() > 0) {
            for (int i = 0; i < lists.get(position).size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.text_comm_item, null);
                TextView textView = (TextView) view.findViewById(R.id.text_name);
                TextView textContent = (TextView) view.findViewById(R.id.text_content);
                textView.setText(lists.get(position).get(i).getAsString("nick")+": ");
                textContent.setText(lists.get(position).get(i).getAsString("content"));
                holder.comContent.addView(view);
            }
        }
        return convertView;
    }


    public final class ViewHolder {
        public SimpleDraweeView icon;
        public TextView nick;
        public TextView time;
        public TextView tags;
        public LinearLayout content;
        public LinearLayout comContent;

    }

    public   interface  setOnClickLoction
    {
        public  void  content(int posit, int nid ,int uid,int parent);
    }
}
