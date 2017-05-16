package cn.com.shequnew.tools;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class TextContent {

    public  String content="公司拥有强大的市场研究团队和专业电子商务运营队伍，目前在编员工数量超过500人，并且具备丰富的打造电子商务交易平台的实战经验。公司汇聚了大量B2C电子商务行业精英，已组成专业化的市场调研、分析、推广和客户服务团队，同时公司专注于技术实力的投资，以打造全国一流的电子商务交易平台另外，拥有一流的工作环境及完备的管理体系是公司始终坚持的团队原则。运用全新而独特的理念，致力为包括但不限于有意电子商务市场的厂家商户以及个人网民，提供专业优质的B2C交易服务。";
    public  String qontect_li="我们的理念：一切服从于市场，一切源于客户 我们的精神：创新，务实，合作，共赢";
    public  String qontect_phone="客服热线：400-12345678";
    public  String qontect_emi="邮箱: shequshangcheng@163.com";
    public  String qontect_web="公司网站：www.shequshangcheng.com";

    public  List<ContentValues> setData(){
        List<ContentValues> as=new ArrayList<>();
        ContentValues one=new ContentValues();
        one.put("a","广告合作");
        one.put("b","2326895861@myee.net.cn");
        one.put("c","");
        as.add(one);
        ContentValues one1=new ContentValues();
        one1.put("a","投资合作");
        one1.put("b","3222808584@myee.net.cn");
        one1.put("c","");
        as.add(one);
        ContentValues one2=new ContentValues();
        one2.put("a","加入我们");
        one2.put("b","2326895861@myee.net.cn");
        one2.put("c","欢迎有识之士");
        as.add(one2);
        ContentValues one3=new ContentValues();
        one3.put("a","战略合作");
        one3.put("b","2326895861@myee.net.cn");
        one3.put("c","运营、媒体、鉴定中心、机构等战略姓合作");
        as.add(one3);

        ContentValues one4=new ContentValues();
        one4.put("a","商务合作");
        one4.put("b","2326895861@myee.net.cn");
        one4.put("c","博客、自媒体、视频栏目等有关入驻、资源置换 内容输出等合作");
        as.add(one4);
        ContentValues one5=new ContentValues();
        one5.put("a","市场合作");
        one5.put("b","2326895861@myee.net.cn");
        one5.put("c","品牌合作、联合推广、渠道合作、活动推广、ap p换量合作");
        as.add(one5);
        return as;
    }



}
