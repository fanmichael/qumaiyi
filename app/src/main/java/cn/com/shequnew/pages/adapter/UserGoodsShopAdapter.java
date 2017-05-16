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

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ShopDetailsActivity;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class UserGoodsShopAdapter extends BaseAdapter {
    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;
    private setOnClickLoction setOnClickLoction;
    public UserGoodsShopAdapter(Context context, List<ContentValues> contentValues,setOnClickLoction setOnClickLoction) {
        this.context = context;
        this.contentValues = contentValues;
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
            convertView = mInflater.inflate(R.layout.commend_item_list, null);
            holder.comm_layout=(LinearLayout)convertView.findViewById(R.id.comm_layout);
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
        Uri imageUri = Uri.parse(good.getAsString("good_image"));
        ValidData.load(imageUri, holder.goodIcon, 100, 80);
        Uri imageIcon = Uri.parse(good.getAsString("icon"));
        ValidData.load(imageIcon, holder.goodImages, 30, 30);
        holder.goodNick.setText(good.getAsString("nick"));
        holder.goodTitle.setText(good.getAsString("good_name"));
        holder.goodTime.setText("工期：" + good.getAsInteger("maf_time") + "天");
        holder.goodPrice.setText("￥" + good.getAsString("price"));
        holder.comm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setOnClickLoction.shopDetails(position,good.getAsInteger("id"),good.getAsInteger("uid"));

            }
        });

        return convertView;
    }

    public final class ViewHolder {
        private LinearLayout comm_layout;
        public SimpleDraweeView goodIcon;//头像
        public TextView goodNick;//昵称
        public TextView goodTitle;
        public SimpleDraweeView goodImages;//
        public TextView goodPrice;
        public TextView goodTime;
    }

    public   interface  setOnClickLoction
    {
        public  void  shopDetails(int posit,int id,int uid);
    }

}
