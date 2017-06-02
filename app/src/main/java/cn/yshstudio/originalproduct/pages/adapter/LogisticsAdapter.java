package cn.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yshstudio.originalproduct.R;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class LogisticsAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public LogisticsAdapter(Context context, List<ContentValues> contentValues) {
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
            convertView = mInflater.inflate(R.layout.logistics_item, null);
            holder.content = (TextView) convertView.findViewById(R.id.contect_logis);
            holder.time = (TextView) convertView.findViewById(R.id.time_logis);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv = contentValues.get(position);
        holder.content.setText(cv.getAsString("remark"));
        holder.time.setText(cv.getAsString("datetime"));
        return convertView;
    }

    public final class ViewHolder {
        public TextView content;
        public TextView time;

    }

}
