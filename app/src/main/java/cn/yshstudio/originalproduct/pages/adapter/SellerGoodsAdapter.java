package cn.yshstudio.originalproduct.pages.adapter;

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

import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.pages.activity.ShopDetailsActivity;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class SellerGoodsAdapter extends BaseAdapter {
    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;

    public SellerGoodsAdapter(Context context, List<ContentValues> contentValues) {
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
            convertView = mInflater.inflate(R.layout.seller_item_list, null);
            holder.comm_layout = (LinearLayout) convertView.findViewById(R.id.seller_lin);
            holder.goodIcon = (SimpleDraweeView) convertView.findViewById(R.id.seller_commodity_icon);
            holder.goodTitle = (TextView) convertView.findViewById(R.id.seller_commodity_nick);
            holder.goodTime = (TextView) convertView.findViewById(R.id.seller_commodity_time);
            holder.goodImages = (SimpleDraweeView) convertView.findViewById(R.id.seller_commodity_images);
            holder.goodNick = (TextView) convertView.findViewById(R.id.seller_item_name);
            holder.goodPrice = (TextView) convertView.findViewById(R.id.seller_itme_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues good = contentValues.get(position);
        Uri imageIcon = Uri.parse(good.getAsString("good_image"));
        ValidData.load(imageIcon, holder.goodImages, 100, 80);
        Uri imageUri = Uri.parse(good.getAsString("user_icon"));
        ValidData.load(imageUri, holder.goodIcon, 45, 45);
        holder.goodTitle.setText(good.getAsString("user_nick"));

        holder.goodTime.setText(good.getAsString("time"));
        holder.goodNick.setText(good.getAsString("good_name"));
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
        public SimpleDraweeView goodImages;
        public TextView goodPrice;
        public TextView goodTime;
    }


}
