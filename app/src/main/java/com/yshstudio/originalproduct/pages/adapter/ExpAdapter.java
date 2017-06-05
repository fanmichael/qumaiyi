package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import java.util.List;


/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class ExpAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;
    private setonClick setonClick;
    private int mSelect = -1;

    public ExpAdapter(List<ContentValues> contentValues, Context context, setonClick setonClick) {
        this.context = context;
        this.contentValues = contentValues;
        this.setonClick = setonClick;
        mInflater = LayoutInflater.from(context);

    }

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
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
            convertView = mInflater.inflate(R.layout.exp_item, null);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.lin_exp_item);
            holder.icon = (ImageView) convertView.findViewById(R.id.exp_chose);
            holder.title = (TextView) convertView.findViewById(R.id.exp_com);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        holder.title.setText(cv.getAsString("com"));
        if (mSelect == position) {
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setonClick.onClick(position,cv.getAsString("com"),cv.getAsString("no"));
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        private LinearLayout linearLayout;
        public ImageView icon;
        public TextView title;
    }

    public interface setonClick {
        void onClick(int pos, String com, String no);
    }

}
