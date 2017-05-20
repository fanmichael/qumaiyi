package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class AppraiesimgeAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;
    private int type;
    private boolean le;

    public AppraiesimgeAdapter(List<ContentValues> contentValues, Context context, int type, boolean le) {
        this.context = context;
        this.contentValues = contentValues;
        mInflater = LayoutInflater.from(context);
        this.type = type;
        this.le = le;
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
            convertView = mInflater.inflate(R.layout.app_item_imagse, null);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.app_image);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.image_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }

        if (position == 0) {
            if (le) {
                holder.icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.addimages));
            } else {
                holder.icon.setVisibility(View.GONE);
            }
        } else {
            final ContentValues cv = contentValues.get(position);
            Uri imageIcon = Uri.parse(cv.getAsString("image"));
            ValidData.load(imageIcon, holder.icon, 150, 150);
            if (type == 2) {
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contentValues.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }

    public final class ViewHolder {
        public SimpleDraweeView icon;
        public ImageView imgDelete;
    }

}
