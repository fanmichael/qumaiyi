package com.yshstudio.originalproduct.pages.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yshstudio.originalproduct.R;
import com.yshstudio.originalproduct.tools.ValidData;
import java.util.List;


/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BuyGoodsShopAdapter extends BaseAdapter {
    private Context context;
    private List<ContentValues> contentValues;
    private LayoutInflater mInflater;
    private setOnClickLoction setOnClickLoction;

    public BuyGoodsShopAdapter(Context context, List<ContentValues> contentValues, setOnClickLoction setOnClickLoction) {
        this.context = context;
        this.contentValues = contentValues;
        this.setOnClickLoction = setOnClickLoction;
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
            convertView = mInflater.inflate(R.layout.collect_buy_item_list, null);
            holder.comm_layout = (LinearLayout) convertView.findViewById(R.id.buy_details_layout);
            holder.goodIcon = (SimpleDraweeView) convertView.findViewById(R.id.buy_details_icon);
            holder.goodImages = (SimpleDraweeView) convertView.findViewById(R.id.buy_details_images);
            holder.goodNick = (TextView) convertView.findViewById(R.id.buy_details_nick);
            holder.goodTitle = (TextView) convertView.findViewById(R.id.buy_details_title);
            holder.goodTime = (TextView) convertView.findViewById(R.id.buy_details_grd);
            holder.goodPrice = (TextView) convertView.findViewById(R.id.buy_details_price);
            holder.number = (TextView) convertView.findViewById(R.id.buy_details_time);
            holder.allPrice = (TextView) convertView.findViewById(R.id.buy_details_all_price);
            holder.buySta = (TextView) convertView.findViewById(R.id.buy_details_nick_sta);//状态
            holder.cal = (Button) convertView.findViewById(R.id.buy_details_cal_btn);
            holder.buy = (Button) convertView.findViewById(R.id.buy_details_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (contentValues == null || contentValues.size() <= position) {
            return convertView;
        }
        final ContentValues good = contentValues.get(position);
        Uri imageIcon = Uri.parse(good.getAsString("icon"));
        ValidData.load(imageIcon, holder.goodIcon, 30, 30);
        Uri imageUri = Uri.parse(good.getAsString("good_image"));
        ValidData.load(imageUri, holder.goodImages, 100, 80);
        holder.goodNick.setText(good.getAsString("nick"));
        holder.goodTitle.setText(good.getAsString("trade_name"));
        holder.goodTime.setText("工期：" + good.getAsInteger("maf_time") + "天");
        holder.goodPrice.setText("￥" + good.getAsString("money"));
        holder.number.setText("x" + good.getAsInteger("num"));
        Double price = Double.parseDouble(good.getAsString("money"));
        Double numAll = good.getAsInteger("num") * price;
        holder.allPrice.setText("合计：" + numAll);

        if (good.getAsInteger("state") == 0 && good.getAsInteger("status") == 0) {
            holder.buySta.setVisibility(View.GONE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("等待支付...");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }
        if (good.getAsInteger("state") == 0 && good.getAsInteger("status") == 1) {
            holder.buySta.setVisibility(View.GONE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("发货");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.bd_top));
            holder.buy.setTextColor(context.getResources().getColor(R.color.white));
            holder.buy.setClickable(true);
            holder.buy.setEnabled(true);
        }
        if (good.getAsInteger("state") == 1 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("物流配送中...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("物流信息");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.bd_top));
            holder.buy.setTextColor(context.getResources().getColor(R.color.white));
            holder.buy.setClickable(true);
            holder.buy.setEnabled(true);
        }
        if (good.getAsInteger("state") == 2 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("钱已转到账户余额");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }
        if (good.getAsInteger("state") == 3 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("申请退款...");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }

        if (good.getAsInteger("state") == 4 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("退款成功");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }

        if (good.getAsInteger("state") == 5 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("退款失败");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }

        if (good.getAsInteger("state") == 7 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("钱已转到账户余额");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }

        if (good.getAsInteger("state") == 8 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("钱已转到账户余额");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }
        if (good.getAsInteger("state") == 9 && good.getAsInteger("status") == 1) {
            holder.buySta.setText("已签收...");
            holder.buySta.setVisibility(View.VISIBLE);
            holder.cal.setVisibility(View.INVISIBLE);
            holder.buy.setText("钱已转到账户余额");
            holder.buy.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.buy.setTextColor(context.getResources().getColor(R.color.col_bg));
            holder.buy.setClickable(false);
            holder.buy.setEnabled(false);
        }

        holder.comm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.lin(position, good.getAsInteger("id"), good.getAsInteger("state"), good.getAsInteger("status"), good.getAsString("ddid"), good.getAsString("logistics"), good.getAsString("logistics_num"));
            }
        });

        holder.cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.cal(position, good.getAsInteger("id"), good.getAsString("ddid"));
            }
        });
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickLoction.buy(position, good.getAsInteger("id"), good.getAsString("ddid"));
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public LinearLayout comm_layout;
        public SimpleDraweeView goodIcon;//头像
        public SimpleDraweeView goodImages;
        public TextView goodNick;//昵称
        public TextView goodTitle;
        public TextView goodTime;
        public TextView goodPrice;
        public TextView number;
        private TextView buySta;
        public TextView allPrice;
        public Button cal;
        public Button buy;
    }

    public interface setOnClickLoction {
        public void lin(int posit, int id, int state, int status, String ddid, String logistics, String logistics_num);

        public void cal(int posit, int id, String ddid);

        public void buy(int posit, int id, String ddid);
    }
}
