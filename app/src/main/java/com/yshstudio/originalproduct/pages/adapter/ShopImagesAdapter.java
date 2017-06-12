package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;

import java.util.List;


/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class ShopImagesAdapter extends BaseAdapter {

    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;

    public ShopImagesAdapter(Context context, List<ContentValues> contentValues) {
        this.context = context;
        this.contentValues = contentValues;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (contentValues == null) {
            return convertView;
        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.shop_item_imagse, null);
            holder.image = (ImageView) convertView.findViewById(R.id.imagse_shop_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues good = contentValues.get(position);
        ImageLoader.getInstance().displayImage(good.getAsString("imgs"), holder.image);
        return convertView;
    }

    public final class ViewHolder {
        public ImageView image;
    }

}
