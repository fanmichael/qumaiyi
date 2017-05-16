package cn.com.shequnew.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class AllcateDetailsAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public AllcateDetailsAdapter(List<ContentValues> contentValues,Context context){
        this.context=context;
        this.contentValues=contentValues;
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
        if(contentValues==null){
            return convertView;
        }
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.allcate_details_item,null);
            holder.image=(SimpleDraweeView)convertView.findViewById(R.id.sim_details);
            holder.title=(TextView)convertView.findViewById(R.id.allCate_details_title);
            holder.content=(TextView)convertView.findViewById(R.id.allCate_details_content);
            holder.time=(TextView)convertView.findViewById(R.id.allCate_details_time);
            holder.price=(TextView)convertView.findViewById(R.id.allCate_details_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv=contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("good_image"));
        ValidData.load(imageIcon, holder.image,150,150);
        holder.title.setText(cv.getAsString("good_name"));
        holder.content.setText(cv.getAsString("good_intro"));
        holder.time.setText("工期："+cv.getAsInteger("maf_time")+"天");
        holder.price.setText("￥"+cv.getAsString("price"));
        return convertView;
    }


    public final class ViewHolder{
        public SimpleDraweeView image;
        public TextView title;
        public TextView content;
        public TextView time;
        public TextView price;
    }


}
