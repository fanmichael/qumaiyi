package cn.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.yshstudio.originalproduct.R;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class SiteDetailsAdapter extends BaseAdapter {

    private List<ContentValues> contentValues;
    private Context mContext;
    private LayoutInflater mInflater;
    private setOnClickLoction setOnClickLoction;

    public SiteDetailsAdapter(List<ContentValues> contentValues, Context mContext, setOnClickLoction setOnClickLoction) {
        this.mContext = mContext;
        this.contentValues = contentValues;
        this.setOnClickLoction = setOnClickLoction;
        mInflater = LayoutInflater.from(mContext);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.site_item_address, null);
            holder.name = (TextView) convertView.findViewById(R.id.address_name);
            holder.phone = (TextView) convertView.findViewById(R.id.address_phone);
            holder.address = (TextView) convertView.findViewById(R.id.address_details);
            holder.choseAddress = (CheckBox) convertView.findViewById(R.id.address_box);
            holder.edit = (LinearLayout) convertView.findViewById(R.id.address_edit);
            holder.delete = (LinearLayout) convertView.findViewById(R.id.address_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues cv = contentValues.get(position);
        holder.name.setText(cv.getAsString("name"));
        holder.phone.setText(cv.getAsString("mobile"));
        holder.address.setText(cv.getAsString("address"));
        if (cv.getAsInteger("state") == 1) {
            holder.choseAddress.setChecked(true);
        } else {
            holder.choseAddress.setChecked(false);
        }

        holder.choseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.radio(position, cv.getAsInteger("id"));
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.update(position, cv.getAsInteger("id"),cv.getAsInteger("uid"), cv.getAsString("name"), cv.getAsString("mobile"), cv.getAsString("address"));

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.delete(position, cv.getAsInteger("id"));
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView phone;
        public TextView address;
        public CheckBox choseAddress;
        public LinearLayout edit;
        public LinearLayout delete;
    }

    public interface setOnClickLoction {
        public void radio(int posit, int id);

        public void delete(int posit, int id);

        public void update(int posit, int id, int uid, String name, String mobile, String address);

    }


}
