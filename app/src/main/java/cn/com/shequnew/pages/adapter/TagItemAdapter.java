package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class TagItemAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;
    private int mSelect = -1;
    private setOnclick setOnclick;

    public TagItemAdapter(List<ContentValues> contentValues, Context context, setOnclick setOnclick) {
        this.context = context;
        this.contentValues = contentValues;
        this.setOnclick = setOnclick;
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
            convertView = mInflater.inflate(R.layout.tag_itme, null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.tag_chose);
            holder.title = (TextView) convertView.findViewById(R.id.text_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv = contentValues.get(position);
        holder.title.setText(cv.getAsString("name"));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnclick.chose(position);
            }
        });

        if (mSelect == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
        public CheckBox checkBox;
    }

    public interface setOnclick {
        void chose(int pos);
    }

}
