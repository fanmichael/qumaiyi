package cn.com.shequnew.pages.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.config.AppContext;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.tools.ValidData;

/**
 * 新建地址
 */
public class NewSiteActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_regit_title)
    TextView topRegitTitle;
    @BindView(R.id.site_pop)
    EditText sitePop;
    @BindView(R.id.site_phone)
    EditText sitePhone;
    @BindView(R.id.site_address)
    TextView siteAddress;
    @BindView(R.id.lin_address)
    LinearLayout linAddress;
    @BindView(R.id.site_dailes_address)
    EditText siteDailesAddress;

    private OptionsPickerView pvOptionsAddress;
    private List<String> province = new ArrayList<>();
    private List<List<String>> city = new ArrayList<>();
    private List<List<List<String>>> region = new ArrayList<>();
    private Context context;
    private String address;
    private int error;
    private int id;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_site);
        ButterKnife.bind(this);
        context = this;
        initData();
    }

    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        if (type.equals("add")) {
            topTitle.setText("新建地址");
        } else if (type.equals("edit")) {
            topTitle.setText("编辑地址");
            id = bundle.getInt("id");
            sitePop.setText(bundle.getString("name"));
            sitePhone.setText(bundle.getString("mobile"));
//            String address=bundle.getString("address");
//
//            siteAddress.setText(address.split(" ",3).toString());

        }
        topRegitTitle.setText("保存");
        topRegitTitle.setVisibility(View.VISIBLE);
        siteAddress.setText("广东省 深圳市 宝安区");
        jsonTxt();
        initOptionPicker();
    }

    private void jsonTxt() {
        AssetManager manager = getAssets();
        try {
            String data = ValidData.readStream(manager.open("address.txt"));
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                province.add(jsonObject.getString("name"));
                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("subArea"));
                List<String> ss = new ArrayList<>();
                List<List<String>> gg = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObjectj = jsonArray1.getJSONObject(j);
                    ss.add(jsonObjectj.getString("name"));
                    List<String> vv = new ArrayList<>();
                    JSONArray jsonArray2 = new JSONArray(jsonObjectj.getString("subArea"));
                    for (int f = 0; f < jsonArray2.length(); f++) {
                        JSONObject jsonObjectf = jsonArray2.getJSONObject(f);
                        vv.add(jsonObjectf.getString("name"));
                    }
                    gg.add(j, vv);
                }
                city.add(i, ss);
                region.add(i, gg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptionsAddress = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                address = "";
                address = province.get(options1) + " " + city.get(options1).get(options2) + " " + region.get(options1).get(options2).get(options3);
                siteAddress.setText(address);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                // .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setSubmitColor(getResources().getColor(R.color.bd_top))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.col_bg))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.bg))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                // .setLabels("省", "市", "区")//设置选择的三级单位
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        pvOptionsAddress.setPicker(province, city, region);

    }

    @OnClick(R.id.image_back)
    void back() {
        destroyActitity();
    }

    @OnClick(R.id.lin_address)
    void setAddress() {
        pvOptionsAddress.show();
    }

    @OnClick(R.id.top_regit_title)
    void sumit() {
        initViewData();
    }

    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            switch (params[0]) {
                case 1:
                    httpAddress();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    httpEditAddress();
                    bundle.putInt("what", 1);
                    break;
            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            removeLoading();
            switch (what) {
                case 1:
                    if (error == 0) {
                        setResult(1);
                        destroyActitity();
                    } else {
                        return;
                    }
                    break;


            }

        }
    }


    private void initViewData() {
        String mess = "";
        Boolean is = true;

        if (sitePop.getText().toString().trim().equals("")) {
            mess = "收货人不能为空";
            is = false;
        }

        if (sitePhone.getText().toString().trim().equals("")) {
            mess = "手机号不能为空";
            is = false;
        }
        if (siteDailesAddress.getText().toString().trim().equals("")) {
            mess = "详细地址不能为空";
            is = false;
        }
        if (is == true) {
            if (type.equals("add")) {
                new asyncTask().execute(1);
            } else if (type.equals("edit")) {
                new asyncTask().execute(2);
            }


        } else {
            Toast.makeText(context, "" + mess, Toast.LENGTH_SHORT).show();
        }
    }


    private void httpAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "Address.action");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        map.put("name", sitePop.getText().toString().trim());
        map.put("type", "add");
        map.put("mobile", sitePhone.getText().toString().trim());
        map.put("address", siteAddress.getText().toString().trim() + siteDailesAddress.getText().toString().trim());
        String json = HttpConnectTool.post(map);
        if (!json.equals("")) {
            xmlComm(json);
        }
    }

    private void httpEditAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "Address.action");
        map.put("uid", AppContext.cv.getAsInteger("id") + "");
        map.put("name", sitePop.getText().toString().trim());
        map.put("type", "edit");
        map.put("type", id + "");
        map.put("mobile", sitePhone.getText().toString().trim());
        map.put("address", siteAddress.getText().toString().trim() + siteDailesAddress.getText().toString().trim());
        String json = HttpConnectTool.post(map);
        if (!json.equals("")) {
            xmlComm(json);
        }
    }


    private void xmlComm(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            error = obj.getInt("error");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
