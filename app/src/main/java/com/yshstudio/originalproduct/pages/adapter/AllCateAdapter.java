package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;

import java.util.List;


/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class AllCateAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public AllCateAdapter(List<ContentValues> contentValues,Context context){
        this.context=context;
        this.contentValues=contentValues;
        mInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {return contentValues != null ? contentValues.size() : 0;
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
        if(contentValues==null){
            return convertView;
        }
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.gridview_item_cate,null);
            holder.icon=(SimpleDraweeView)convertView.findViewById(R.id.gridView_image);
            holder.title=(TextView)convertView.findViewById(R.id.gridView_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv=contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("image"));
        ValidData.load(imageIcon,holder.icon,60,60);
        holder.title.setText(cv.getAsString("name"));
        return convertView;
    }

    public final class ViewHolder{
        public SimpleDraweeView icon;
        public TextView title;
    }

}
