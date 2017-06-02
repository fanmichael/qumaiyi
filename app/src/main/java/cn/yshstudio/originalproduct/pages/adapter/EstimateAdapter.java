package cn.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class EstimateAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private List<List<ContentValues>> lists;
    private Context context;
    private LayoutInflater mInflater;

    public EstimateAdapter(Context context, List<ContentValues> contentValues, List<List<ContentValues>> lists) {
        this.context = context;
        this.contentValues = contentValues;
        this.lists = lists;
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
//        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.estimate_layout, null);
            holder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.estimes_details_icon);
            holder.nick = (TextView) convertView.findViewById(R.id.estimes__details_price);
            holder.tag = (TextView) convertView.findViewById(R.id.estimes__details_time);
            holder.sta = (LinearLayout) convertView.findViewById(R.id.estimes_sta_lin);
            holder.content = (TextView) convertView.findViewById(R.id.estimes_content);
            holder.images = (LinearLayout) convertView.findViewById(R.id.estimes_images_lin);
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        Uri imageUri = Uri.parse(cv.getAsString("icon"));
        ValidData.load(imageUri, holder.simpleDraweeView, 60, 60);
        holder.nick.setText(cv.getAsString("nick"));
        holder.tag.setText(cv.getAsString("time"));
        holder.content.setText(cv.getAsString("comment"));
        if (cv.getAsInteger("num") > 0) {
            for (int i = 0; i < cv.getAsInteger("num"); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.star_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.itme_blue);
                holder.sta.addView(view);
            }
        }

        if (lists.get(position).size() > 0) {
            for (int i = 0; i < lists.get(position).size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.estimate_image_item, null);
                SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.estimes_itme_images);
                Uri image = Uri.parse(lists.get(position).get(i).getAsString("image"));
                ValidData.load(image, img, 80, 80);
                holder.images.addView(view);
            }
        }

        return convertView;
    }


    public final class ViewHolder {
        public SimpleDraweeView simpleDraweeView;
        public TextView nick;
        public TextView tag;
        public LinearLayout sta;
        public TextView content;
        public LinearLayout images;
    }


}
