package cn.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yshstudio.originalproduct.R;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class TagsAdapter extends BaseAdapter {
    private List<ContentValues> contentValues;
    private Context context;
    private int type;
    private LayoutInflater mInflater;
    private setOnclick setOnclick;

    public TagsAdapter(List<ContentValues> contentValues, Context context, int type, setOnclick setOnclick) {
        this.context = context;
        this.contentValues = contentValues;
        this.type = type;
        this.setOnclick = setOnclick;
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
            convertView = mInflater.inflate(R.layout.tags_chose_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.delete_img);
            holder.title = (TextView) convertView.findViewById(R.id.tags_chose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        if (type == 1) {
            ContentValues cv = contentValues.get(position);
            holder.title.setText(cv.getAsString("name"));
        } else if (type == 2) {
            ContentValues cv = contentValues.get(position);
            holder.title.setText(cv.getAsString("name"));
            holder.title.setTextColor(context.getResources().getColor(R.color.white));
            holder.title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dynamic_title));
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnclick.onDelete(position);
                }
            });
        }


        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
        private ImageView imageView;
    }

    public interface setOnclick {
        void onDelete(int pos);
    }


}
