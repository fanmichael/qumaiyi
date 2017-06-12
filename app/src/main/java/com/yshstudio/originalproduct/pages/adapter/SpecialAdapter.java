package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshstudio.originalproduct.R;

import java.util.List;


/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class SpecialAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context mContext;
    private LayoutInflater mInflater;
    private String title;

    public SpecialAdapter(List<ContentValues> contentValues, Context mContext, String title) {
        this.mContext = mContext;
        this.contentValues = contentValues;
        this.title = title;
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
            convertView = mInflater.inflate(R.layout.special_item_list, null);
            holder.content = (TextView) convertView.findViewById(R.id.special_content);
            holder.title = (TextView) convertView.findViewById(R.id.special_title);
            holder.imagseTets = (ImageView) convertView.findViewById(R.id.imagse_Test);
//            holder.draweeView=(SimpleDraweeView)convertView.findViewById(R.id.special_simle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv = contentValues.get(position);
        ImageLoader.getInstance().displayImage(cv.getAsString("img"), holder.imagseTets);
//        holder.draweeView.setAspectRatio(0.5F);
//        Uri image = Uri.parse(cv.getAsString("img"));
//        ValidData.load(image,holder.draweeView,200,100);
        if (position == 0) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
        } else {
            holder.title.setVisibility(View.INVISIBLE);
        }
        holder.content.setText(cv.getAsString("content"));
        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
        public TextView content;
        public ImageView imagseTets;
//        public SimpleDraweeView draweeView;


    }

}
