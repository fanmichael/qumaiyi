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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ContentFileDetailsActivity;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/5/8 0008.
 */


public class ContentApapter extends BaseAdapter {

    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;

    public ContentApapter(Context context, List<ContentValues> contentValues) {
        this.context = context;
        this.contentValues = contentValues;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contentValues.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (contentValues == null) {
            return convertView;
        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.pages_item_chose, null);
            holder.page_item = (LinearLayout) convertView.findViewById(R.id.page_item);
            holder.dynamicIcon = (SimpleDraweeView) convertView.findViewById(R.id.pages_item_icon);//头像
            holder.dynamicImages = (SimpleDraweeView) convertView.findViewById(R.id.pages_item_subject);//大图
            holder.dynamicTags = (TextView) convertView.findViewById(R.id.pages_tags_item_text); //标签
            holder.dynamicTitle = (TextView) convertView.findViewById(R.id.pages_title_text);//标题
            holder.dynamicM = (ImageView) convertView.findViewById(R.id.pages_file_m);//视频
            holder.dynamicF = (ImageView) convertView.findViewById(R.id.pages_file_f);//文件
            holder.dynamicNick = (TextView) convertView.findViewById(R.id.pages_item_nick_text);//昵称
            holder.dynamicSign = (TextView) convertView.findViewById(R.id.pages_sign_item_text);//签名
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues user = contentValues.get(position);
        holder.dynamicSign.setVisibility(View.GONE);

        Uri imageUri = Uri.parse(user.getAsString("icon"));
        ValidData.load(imageUri, holder.dynamicIcon, 30, 30);
        holder.dynamicNick.setText(user.getAsString("nick"));
        if (user.getAsInteger("file_type") == 0) {
            Uri image = Uri.parse(user.getAsString("subject"));
            ValidData.load(image, holder.dynamicImages, 100, 80);
            holder.dynamicF.setVisibility(View.VISIBLE);
            holder.dynamicM.setVisibility(View.GONE);
        } else {
            holder.dynamicF.setVisibility(View.GONE);
            holder.dynamicM.setVisibility(View.VISIBLE);
        }

        holder.dynamicTitle.setText(user.getAsString("title"));
        holder.dynamicTags.setText(user.getAsString("tags"));
        return convertView;
    }

    public final class ViewHolder {
        public LinearLayout page_item;
        public SimpleDraweeView dynamicIcon;//头像
        public TextView dynamicNick;//昵称
        public ImageView dynamicF;//文件
        public ImageView dynamicM;//视频文件
        public TextView dynamicSign;//签名
        public TextView dynamicTitle;
        public TextView dynamicTags;//标签呢
        public SimpleDraweeView dynamicImages;//

    }

}
