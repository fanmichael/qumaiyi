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
import cn.yshstudio.originalproduct.pages.config.AppContext;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class ConGoodsAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public ConGoodsAdapter(List<ContentValues> contentValues, Context context) {
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
            convertView = mInflater.inflate(R.layout.con_goods_item_list, null);
            holder.goodsLin = (LinearLayout) convertView.findViewById(R.id.con_goods_lin);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.goods_con_im);
            holder.title = (TextView) convertView.findViewById(R.id.goods_con_title);
            holder.price = (TextView) convertView.findViewById(R.id.goods_con_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("good_image"));
        ValidData.load(imageIcon, holder.icon, 100, 80);
        holder.title.setText(cv.getAsString("good_name"));
        holder.price.setText("￥" + cv.getAsString("price"));
        holder.goodsLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", cv.getAsInteger("id"));
                bundle.putInt("uid", AppContext.cv.getAsInteger("id"));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public SimpleDraweeView icon;
        public TextView title;
        public TextView price;
        public LinearLayout goodsLin;
    }

}
