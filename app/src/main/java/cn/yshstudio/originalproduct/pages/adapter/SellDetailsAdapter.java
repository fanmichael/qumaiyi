package cn.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class SellDetailsAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;

    public SellDetailsAdapter(List<ContentValues> contentValues, Context context){
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
            convertView=mInflater.inflate(R.layout.seller_image,null);
            holder.image=(SimpleDraweeView)convertView.findViewById(R.id.sell_details);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv=contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("image"));
        ValidData.load(imageIcon, holder.image,80,80);
        return convertView;
    }


    public final class ViewHolder{
        public SimpleDraweeView image;
    }


}
