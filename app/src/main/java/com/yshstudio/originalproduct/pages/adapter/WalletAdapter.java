package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;

import java.util.List;


/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class WalletAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public WalletAdapter(Context context, List<ContentValues> contentValues) {
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
            convertView = mInflater.inflate(R.layout.wallet_item, null);
            holder.wallName = (TextView) convertView.findViewById(R.id.wall_type);
            holder.wallTime = (TextView) convertView.findViewById(R.id.wall_time);
            holder.wallPrice = (TextView) convertView.findViewById(R.id.wall_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv = contentValues.get(position);
        if (cv.getAsInteger("type") == 0) {
            if (cv.getAsInteger("state") == 0) {
                holder.wallName.setText("提现--未处理");
            } else if (cv.getAsInteger("state") == 1) {
                holder.wallName.setText("提现--审核通过");
            } else if (cv.getAsInteger("state") == 2) {
                holder.wallName.setText("提现--审核未通过");
            } else if (cv.getAsInteger("state") == 3) {
                holder.wallName.setText("提现--支付宝报错");
            }
        } else if (cv.getAsInteger("type") == 1) {
            holder.wallName.setText("消费");
        } else if (cv.getAsInteger("type") == 2) {
            holder.wallName.setText("营业所得");
        }
        holder.wallTime.setText(cv.getAsString("time"));
        holder.wallPrice.setText(cv.getAsString("money"));
        return convertView;
    }

    public final class ViewHolder {
        public TextView wallName;
        public TextView wallTime;
        public TextView wallPrice;
    }


}
