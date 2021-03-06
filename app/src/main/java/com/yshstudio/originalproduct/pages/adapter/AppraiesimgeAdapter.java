package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import java.util.List;


/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class AppraiesimgeAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;
    private int type;
    private deleteFile deleteFile;
    private boolean le;

    public AppraiesimgeAdapter(List<ContentValues> contentValues, Context context, int type, boolean le,deleteFile deleteFile) {
        this.context = context;
        this.contentValues = contentValues;
        this.deleteFile=deleteFile;
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

        if (le == false) {
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
                        deleteFile.deleteFile(position);
                    }
                });
            }
        } else {
            if (position == 0) {
                holder.icon.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.addimages));
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
                            deleteFile.deleteFile(position);
                        }
                    });
                }
            }
        }


        return convertView;
    }

    public final class ViewHolder {
        public SimpleDraweeView icon;
        public ImageView imgDelete;
    }

    public interface deleteFile{
        void deleteFile(int pos);
    }

}
