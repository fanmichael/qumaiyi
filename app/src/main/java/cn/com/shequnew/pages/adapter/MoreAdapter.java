package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import cn.com.shequnew.pages.activity.LocalVideoActivity;
import cn.com.shequnew.tools.Util;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class MoreAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public MoreAdapter(List<ContentValues> contentValues, Context mContext) {
        this.contentValues = contentValues;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (contentValues == null) {
            return convertView;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.pages_item_chose, null);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.page_item);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.pages_item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.pages_item_nick_text);
            holder.imageF = (ImageView) convertView.findViewById(R.id.pages_file_f);
            holder.imageM = (ImageView) convertView.findViewById(R.id.pages_file_m);
            holder.tags = (TextView) convertView.findViewById(R.id.pages_title_text);
            holder.content = (TextView) convertView.findViewById(R.id.pages_tags_item_text);
            holder.image = (SimpleDraweeView) convertView.findViewById(R.id.pages_item_subject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("icon"));
        ValidData.load(imageIcon, holder.icon, 30, 30);
        holder.title.setText(cv.getAsString("nick"));
        holder.tags.setText(cv.getAsString("title"));
        holder.content.setText(cv.getAsString("tags"));
        if (cv.getAsInteger("file_type") == 1) {
            holder.imageF.setVisibility(View.GONE);
            holder.imageM.setVisibility(View.VISIBLE);
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Util.createVideoThumbnail("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", 200, 150);
//                    holder.image.dynamicImages.setImageBitmap(bitmap);
                }
            }, 100);
        } else {
            holder.imageF.setVisibility(View.VISIBLE);
            holder.imageM.setVisibility(View.GONE);
            Uri image = Uri.parse(cv.getAsString("subject"));
            ValidData.load(image, holder.image, 100, 80);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cv.getAsInteger("file_type") == 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", cv.getAsInteger("id"));
                    bundle.putInt("uid", cv.getAsInteger("uid"));
                    intent.putExtras(bundle);
                    intent.setClass(mContext, ContentFileDetailsActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", cv.getAsInteger("id"));
                    bundle.putInt("uid", cv.getAsInteger("uid"));
                    intent.putExtras(bundle);
                    intent.setClass(mContext, LocalVideoActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });


        return convertView;
    }

    public final class ViewHolder {
        public SimpleDraweeView icon;
        public TextView title;
        public ImageView imageF;
        public ImageView imageM;
        public TextView tags;
        public TextView content;
        public SimpleDraweeView image;
        public LinearLayout linearLayout;
    }


}
