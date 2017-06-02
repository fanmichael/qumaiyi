package cn.yshstudio.originalproduct.pages.adapter;

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

import cn.yshstudio.originalproduct.R;
import cn.yshstudio.originalproduct.tools.ValidData;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class FansAdapter  extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context context;
    private LayoutInflater mInflater;
    public FansAdapter(List<ContentValues> contentValues,Context context){
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
            convertView=mInflater.inflate(R.layout.fans_item_layout,null);
            holder.image=(SimpleDraweeView)convertView.findViewById(R.id.fans_item_icon);
            holder.nick=(TextView)convertView.findViewById(R.id.fans_nick_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        ContentValues cv=contentValues.get(position);
        Uri imageIcon = Uri.parse(cv.getAsString("icon"));
        ValidData.load(imageIcon, holder.image,150,150);
        holder.nick.setText(cv.getAsString("nick"));
        return convertView;
    }

    public final class ViewHolder{
        public SimpleDraweeView image;
        public TextView nick;
    }


}
