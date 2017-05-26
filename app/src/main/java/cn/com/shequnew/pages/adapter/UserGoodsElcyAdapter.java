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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.com.shequnew.R;
import cn.com.shequnew.tools.ValidData;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class UserGoodsElcyAdapter extends BaseAdapter {
    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;
    private setBoolChose setBoolChose;

    public UserGoodsElcyAdapter(Context context, List<ContentValues> contentValues, setBoolChose setBoolChose) {
        this.context = context;
        this.contentValues = contentValues;
        this.setBoolChose = setBoolChose;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (contentValues == null) {
            return convertView;
        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.elcy_shop_item_list, null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.elcy_tag);
            holder.goodIcon = (SimpleDraweeView) convertView.findViewById(R.id.elcy_images);
            holder.goodTitle = (TextView) convertView.findViewById(R.id.elcy_title);
            holder.goodTime = (TextView) convertView.findViewById(R.id.elcy_grd);
            holder.goodImages = (SimpleDraweeView) convertView.findViewById(R.id.elcy_icon);
            holder.goodNick = (TextView) convertView.findViewById(R.id.elcy_nick);
            holder.goodPrice = (TextView) convertView.findViewById(R.id.elcy_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues good = contentValues.get(position);
        Uri imageIcon = Uri.parse(good.getAsString("icon"));
        ValidData.load(imageIcon, holder.goodImages, 30, 30);
        holder.goodNick.setText(good.getAsString("nick"));
        Uri imageUri = Uri.parse(good.getAsString("good_image"));
        ValidData.load(imageUri, holder.goodIcon, 100, 80);
        holder.goodTitle.setText(good.getAsString("good_name"));
        holder.goodTime.setText("工期：" + good.getAsInteger("maf_time") + "天");
        holder.goodPrice.setText("￥" + good.getAsString("price"));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBoolChose.onClick(position, isChecked);
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public SimpleDraweeView goodIcon;//头像
        public CheckBox checkBox;
        public TextView goodNick;//昵称
        public TextView goodTitle;
        public SimpleDraweeView goodImages;//
        public TextView goodPrice;
        public TextView goodTime;
    }

    public interface setBoolChose {
        void onClick(int pos, boolean is);
    }

}
