package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.activity.ContentFileDetailsActivity;
import cn.com.shequnew.pages.activity.LocalVideoActivity;
import cn.com.shequnew.pages.activity.ShopDetailsActivity;
import cn.com.shequnew.tools.Util;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/21 0021.
 * 动态数据加载
 */

public class DynamicAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    List<ContentValues> mDynamichList;

    public DynamicAdapter(Context mContext, List<ContentValues> mPublishList) {
        this.mContext = mContext;
        this.mDynamichList = mPublishList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDynamichList != null ? mDynamichList.size() : 0;
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
        if (mDynamichList == null) {
            return convertView;
        }
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.dynamic_list_item, null);
            holder.dynamicLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_layout);
            holder.dynamicIcon = (SimpleDraweeView) convertView.findViewById(R.id.dynamic_item_icon);
            holder.dynamicNick = (TextView) convertView.findViewById(R.id.dynamic_item_nick_text);
            holder.dynamicF = (ImageView) convertView.findViewById(R.id.dynamic_file_f);
            holder.dynamicM = (ImageView) convertView.findViewById(R.id.dynamic_file_m);
            holder.dynamicSign = (TextView) convertView.findViewById(R.id.dynamic_title_text);
            holder.dynamicTags = (TextView) convertView.findViewById(R.id.dynamic_tags_item_text);
            holder.dynamicImages = (SimpleDraweeView) convertView.findViewById(R.id.dynamic_item_subject);
            holder.dynamicTime = (TextView) convertView.findViewById(R.id.dynamic_time);
            holder.playVideo = (ImageView) convertView.findViewById(R.id.play_video_dy);
            holder.commodityLayout = (LinearLayout) convertView.findViewById(R.id.dynamic_commodity);
            holder.commodityImages = (SimpleDraweeView) convertView.findViewById(R.id.dynamic_commodity_images);
            holder.commodityName = (TextView) convertView.findViewById(R.id.dynamic_commodity_title);
            holder.commodityDate = (TextView) convertView.findViewById(R.id.dynamic_commodity_grd);
            holder.commodityTitle = (TextView) convertView.findViewById(R.id.dynamic_commodity_nick);
            holder.commodityPrice = (TextView) convertView.findViewById(R.id.dynamic_commodity_price);
            holder.commodityTime = (TextView) convertView.findViewById(R.id.dynamic_commodity_time);
            holder.commodityIcon = (SimpleDraweeView) convertView.findViewById(R.id.dynamic_commodity_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDynamichList == null || mDynamichList.size() <= position) {
            return convertView;
        }
        final ContentValues dy = mDynamichList.get(position);
        if (dy.getAsInteger("status") == 0) {
            holder.dynamicLayout.setVisibility(View.VISIBLE);
            holder.commodityLayout.setVisibility(View.GONE);
            Uri imageIcon = Uri.parse(dy.getAsString("icon"));
            holder.dynamicIcon.setImageURI(imageIcon);
            holder.dynamicNick.setText(dy.getAsString("nick"));
            if (dy.getAsInteger("file_type") == 0) {
                holder.dynamicF.setVisibility(View.VISIBLE);
                holder.dynamicM.setVisibility(View.GONE);
                Uri image = Uri.parse(dy.getAsString("subject"));
                ValidData.load(image, holder.dynamicImages, 100, 80);
                holder.playVideo.setVisibility(View.GONE);
            } else {
                if(dy.containsKey("video_img")){
                    holder.dynamicF.setVisibility(View.GONE);
                    holder.dynamicM.setVisibility(View.VISIBLE);
                    holder.playVideo.setVisibility(View.VISIBLE);
                    Uri image = Uri.parse(dy.getAsString("video_img"));
                    ValidData.load(image, holder.dynamicImages, 100, 80);
                }

            }
            holder.dynamicSign.setText(dy.getAsString("title"));
            holder.dynamicTags.setText(dy.getAsString("tags"));
            holder.dynamicTime.setText(dy.getAsString("push_time"));
        } else if (dy.getAsInteger("status") == 1) {
            holder.dynamicLayout.setVisibility(View.GONE);
            holder.commodityLayout.setVisibility(View.VISIBLE);
            Uri image = Uri.parse(dy.getAsString("good_image"));
            ValidData.load(image, holder.commodityImages, 100, 80);
            holder.commodityName.setText(dy.getAsString("good_name"));
            holder.commodityDate.setText("工期：" + dy.getAsInteger("maf_time") + "天");
            holder.commodityTitle.setText(dy.getAsString("nick"));
            holder.commodityPrice.setText("￥" + dy.getAsString("price"));
            holder.commodityTime.setText(dy.getAsString("push_time"));
            Uri imageIcon = Uri.parse(dy.getAsString("icon"));
            ValidData.load(imageIcon, holder.commodityIcon, 30, 30);
        }

        holder.dynamicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dy.getAsInteger("file_type") == 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", dy.getAsInteger("id"));
                    bundle.putInt("uid", dy.getAsInteger("uid"));
                    intent.putExtras(bundle);
                    intent.setClass(mContext, ContentFileDetailsActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", dy.getAsInteger("id"));
                    bundle.putInt("uid", dy.getAsInteger("uid"));
                    intent.putExtras(bundle);
                    intent.setClass(mContext, LocalVideoActivity.class);
                    mContext.startActivity(intent);
                }


            }
        });
        holder.commodityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dy.getAsInteger("id"));
                bundle.putInt("uid", dy.getAsInteger("uid"));
                intent.putExtras(bundle);
                intent.setClass(mContext, ShopDetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public LinearLayout dynamicLayout;//动态
        public SimpleDraweeView dynamicIcon;//头像
        public TextView dynamicNick;//昵称
        public ImageView dynamicF;//文件
        public ImageView dynamicM;//视频文件
        public TextView dynamicSign;//签名
        public TextView dynamicTags;//标签呢
        public SimpleDraweeView dynamicImages;//
        public TextView dynamicTime;//时间
        public LinearLayout commodityLayout;
        public SimpleDraweeView commodityImages;
        public TextView commodityName;
        public TextView commodityDate;
        public TextView commodityTitle;
        public TextView commodityTime;
        public TextView commodityPrice;
        public SimpleDraweeView commodityIcon;
        private ImageView playVideo;
    }


}
