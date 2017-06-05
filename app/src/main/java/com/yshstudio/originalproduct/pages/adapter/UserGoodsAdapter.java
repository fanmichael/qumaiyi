package com.yshstudio.originalproduct.pages.adapter;

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

import java.util.List;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import com.yshstudio.originalproduct.pages.activity.ShopDetailsActivity;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class UserGoodsAdapter extends BaseAdapter {
    private Context context;
    private List<ContentValues> contentValues;
    private ContentValues values;
    private LayoutInflater mInflater;

    public UserGoodsAdapter(Context context, List<ContentValues> contentValues, ContentValues values) {
        this.context = context;
        this.contentValues = contentValues;
        this.values = values;
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
            convertView = mInflater.inflate(R.layout.commend_item_list, null);
            holder.comm_layout = (LinearLayout) convertView.findViewById(R.id.comm_layout);
            holder.goodIcon = (SimpleDraweeView) convertView.findViewById(R.id.comm_commodity_images);
            holder.goodTitle = (TextView) convertView.findViewById(R.id.comm_commodity_title);
            holder.goodTime = (TextView) convertView.findViewById(R.id.comm_commodity_grd);
            holder.goodImages = (SimpleDraweeView) convertView.findViewById(R.id.comm_commodity_icon);
            holder.goodNick = (TextView) convertView.findViewById(R.id.comm_commodity_nick);
            holder.goodPrice = (TextView) convertView.findViewById(R.id.comm_commodity_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues good = contentValues.get(position);
        if (values == null || values.size() < 0) {
            Uri imageIcon = Uri.parse(good.getAsString("icon"));
            ValidData.load(imageIcon, holder.goodImages, 30, 30);
            holder.goodNick.setText(good.getAsString("nick"));
        } else {
            Uri imageIcon = Uri.parse(values.getAsString("icon"));
            ValidData.load(imageIcon, holder.goodImages, 30, 30);
            holder.goodNick.setText(values.getAsString("nick"));
        }
        Uri imageUri = Uri.parse(good.getAsString("good_image"));
        ValidData.load(imageUri, holder.goodIcon, 100, 80);
        holder.goodTitle.setText(good.getAsString("good_name"));
        holder.goodTime.setText("工期：" + good.getAsInteger("maf_time") + "天");
        holder.goodPrice.setText("￥" + good.getAsString("price"));
        holder.comm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", good.getAsInteger("id"));
                bundle.putInt("uid", good.getAsInteger("uid"));
                intent.putExtras(bundle);
                intent.setClass(context, ShopDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public LinearLayout comm_layout;
        public SimpleDraweeView goodIcon;//头像
        public TextView goodNick;//昵称
        public TextView goodTitle;
        public SimpleDraweeView goodImages;//
        public TextView goodPrice;
        public TextView goodTime;
    }


}
