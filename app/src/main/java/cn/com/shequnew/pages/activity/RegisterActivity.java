package cn.com.shequnew.pages.activity;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.shequnew.R;
import cn.com.shequnew.pages.http.HttpConnectTool;
import cn.com.shequnew.tools.ValidData;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {


    @BindView(R.id.regs_back)
    ImageView regsBack;
    @BindView(R.id.regs_title)
    TextView regsTile;
    @BindView(R.id.regs_phone)
    EditText regsPhone;
    @BindView(R.id.regs_paw)
    EditText regsPaw;
    @BindView(R.id.regs_newPaw)
    EditText regsNewPaw;
    @BindView(R.id.regs_issue)
    EditText regsIssue;
    @BindView(R.id.btn_issue)
    Button btnIssue;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.p_trade)
    Button pTrade;
    @BindView(R.id.t_trade)
    Button tTrade;
    @BindView(R.id.layout_regs)
    LinearLayout layoutRegs;
    @BindView(R.id.imaghe_layout)
    LinearLayout imagheLayout;
    @BindView(R.id.tags_layout)
    LinearLayout tagsLayout;

    private Context mContext;
    private String phone;
    private String pwd;
    private String newPwd;
    private String tag;
    private List<ContentValues> dv;
    private List<ContentValues> tags;
    private List<Integer> tagPopList = new ArrayList<>();
    private List<Integer> tagThList = new ArrayList<>();
    private String type;
    private int error;
    private String desc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        initDelay();
        init();

    }

    /**
     * 延迟线程消息
     */
    private void initDelay() {
        mDelay = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        new asyncTask().execute(1);
                        break;
                    case 2:
                        new asyncTask().execute(2);
                        break;
                    case 3:
                        new asyncTask().execute(3);
                        break;
                }
            }
        };
    }

    private void init() {
        phone = regsPhone.getText().toString().trim();
        pwd = regsPaw.getText().toString().trim();
        newPwd = regsNewPaw.getText().toString().trim();
        tag = regsIssue.getText().toString().trim();

        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");

        Toast.makeText(mContext, "" + type, Toast.LENGTH_SHORT);

        if (type.equals("register")) {
            layoutRegs.setVisibility(View.VISIBLE);
            setDelayMessage(1, 100);
        } else if (type.equals("paw")) {
            layoutRegs.setVisibility(View.GONE);
            regsTile.setText("密码找回");
            register.setText("确定");
        }
    }


    @OnClick(R.id.p_trade)
    void tradeP() {
        setDelayMessage(1, 100);
    }

    @OnClick(R.id.t_trade)
    void tradeT() {
        setDelayMessage(1, 100);
    }

    @OnClick(R.id.btn_issue)
    void issue() {
        phone = regsPhone.getText().toString().trim();
        if (ValidData.validMobile(phone)) {
            setDelayMessage(2, 100);
        } else {
            Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.register)
    void regs() {
        phone = regsPhone.getText().toString().trim();
        pwd = regsPaw.getText().toString().trim();
        newPwd = regsNewPaw.getText().toString().trim();
        tag = regsIssue.getText().toString().trim();
        boolean is = true;
        String msg = "";
        if (!ValidData.validMobile(phone)) {
            msg = "请输入正确的手机号";
            is = false;
        } else if (!pwd.equals(newPwd)) {
            msg = "两次密码输入不一样";
            is = false;
        } else if (pwd.isEmpty() || newPwd.isEmpty() || tag.isEmpty()) {
            msg = "输入不能为空";
            is = false;
        }
        if (is) {
            setDelayMessage(3, 100);
        } else {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.regs_back)
    void back() {
        destroyActitity();
    }


    public void httpSendCode() {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("action", "User.sendCode");
            map.put("mobile", phone);
            String json = HttpConnectTool.post(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 感性趣的人
     */
    private void imaghe() {
        for (int i = 0; i < dv.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.reges_item_layout, null);
            SimpleDraweeView imagesItem = (SimpleDraweeView) view.findViewById(R.id.images_item);
            TextView nameItem = (TextView) view.findViewById(R.id.name_item);
            final CheckBox boxItem = (CheckBox) view.findViewById(R.id.box_item);
            Uri imageUri = Uri.parse(dv.get(i).getAsString("icon"));
            ValidData.load(imageUri, imagesItem, 60, 60);
            nameItem.setText(dv.get(i).getAsString("nick"));
            boxItem.setTag(dv.get(i).getAsInteger("id"));

            boxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        tagPopList.add(Integer.valueOf(boxItem.getTag().toString()));
                    } else {
                        if (tagPopList.size() > 0) {
                            Iterator<Integer> it = tagPopList.iterator();
                            while (it.hasNext()) {
                                Integer value = it.next();
                                if (Integer.valueOf(boxItem.getTag().toString()) == value) {
                                    it.remove();
                                }
                            }
                        }
                    }

                }
            });
            imagheLayout.addView(view);
        }
    }

    /**
     * 感兴趣的事
     */
    private void tagsLay() {
        for (int i = 0; i < tags.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chose_tags, null);
            TextView tag = (TextView) view.findViewById(R.id.text_tag);
            final CheckBox trag = (CheckBox) view.findViewById(R.id.box_tag);
            tag.setText(tags.get(i).getAsString("name"));
            trag.setTag(tags.get(i).getAsInteger("id"));

            trag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        tagThList.add(Integer.valueOf(trag.getTag().toString()));
                    } else {
                        if (tagThList.size() > 0) {
                            Iterator<Integer> it = tagThList.iterator();
                            while (it.hasNext()) {
                                Integer value = it.next();
                                if (Integer.valueOf(trag.getTag().toString()) == value) {
                                    it.remove();
                                }
                            }
                        }
                    }

                }
            });
            tagsLayout.addView(view);
        }
    }

    private void httpRecommendInfo() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.recommendInfo");
            String json = HttpConnectTool.post(hashMap);
            listXml(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void httpRegister() {
        StringBuffer stringBufferPop = new StringBuffer();
        StringBuffer stringBufferThink = new StringBuffer();
        if (tagPopList.size() > 0) {
            for (int i = 0; i < tagPopList.size(); i++) {
                stringBufferPop.append(tagPopList.get(i) + ",");
            }
        }
        if (tagThList.size() > 0) {
            for (int i = 0; i < tagThList.size(); i++) {
                stringBufferPop.append(tagThList.get(i) + ",");
            }

        }
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.register");
            hashMap.put("mobile", phone);
            hashMap.put("password", pwd);
            hashMap.put("persons", stringBufferPop.toString());
            hashMap.put("think", stringBufferThink.toString());
            hashMap.put("code", tag);
            String json = HttpConnectTool.post(hashMap);
            if (!json.equals("")) {
                listData(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listData(String data) {
        try {
            JSONObject obj = new JSONObject(data);

            if (obj.has("error")) {
                if (obj.getInt("error") == 0) {
                    error = obj.getInt("error");
                } else {
                    desc = obj.getString("desc");
                }
            } else {
                Toast.makeText(mContext, "数据有误！", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void httpUpdatePwd() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "User.forgotPassword");
            hashMap.put("mobile", phone);
            hashMap.put("password", pwd);
            hashMap.put("code", tag);
            String json = HttpConnectTool.post(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Bundle> {

        @Override
        protected Bundle doInBackground(Integer... params) {
            Bundle bundle = new Bundle();
            if (dv == null) {
                dv = new ArrayList<>();
            } else {
                dv.clear();
            }

            if (tags == null) {
                tags = new ArrayList<>();
            } else {
                tags.clear();
            }

            switch (params[0]) {
                case 1:
                    //首次加载数据
                    httpRecommendInfo();
                    bundle.putInt("what", 1);
                    break;
                case 2:
                    //获取验证码
                    httpSendCode();
                    bundle.putInt("what", 2);
                    break;
                case 3:
                    //注册
                    httpRegister();
                    bundle.putInt("what", 3);
                    break;
                case 4:
                    //找回密码
                    httpUpdatePwd();
                    bundle.putInt("what", 4);
                    break;

            }
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            int what = bundle.containsKey("what") ? bundle.getInt("what") : -1;
            // removeLoading();
            switch (what) {
                case 1:
                    //初始加载数据
                    if (dv.size() > 0) {
                        imaghe();
                    }
                    if (tags.size() > 0) {
                        tagsLay();
                    }
                    break;
                case 2:

                    break;
                case 3:
                    if (error == 0) {
                        destroyActitity();
                    } else {
                        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:

                    break;

            }

        }
    }


    public void listXml(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject obj1 = new JSONObject(obj.getString("data"));
            JSONArray jsonArr = new JSONArray(obj1.getString("users"));
            JSONArray jsonArrtags = new JSONArray(obj1.getString("tags"));
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                ContentValues cv = new ContentValues();
                cv.put("icon", jsonObj.getString("icon"));
                cv.put("id", jsonObj.getInt("id"));
                cv.put("nick", jsonObj.getString("nick"));
                dv.add(cv);
            }

            for (int i = 0; i < jsonArrtags.length(); i++) {
                JSONObject jsonObjtag = jsonArrtags.getJSONObject(i);
                ContentValues cvtag = new ContentValues();
                cvtag.put("id", jsonObjtag.getInt("id"));
                cvtag.put("name", jsonObjtag.getString("name"));
                cvtag.put("status", jsonObjtag.getInt("status"));
                tags.add(cvtag);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
