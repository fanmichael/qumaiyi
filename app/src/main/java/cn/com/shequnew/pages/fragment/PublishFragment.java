package cn.com.shequnew.pages.fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.shequnew.R;
import cn.com.shequnew.pages.view.SlideShowView;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class PublishFragment extends BasicFragment{

    private SlideShowView slideshowView;


    private String[] imageUrls = new String[]{
            "http://d.hiphotos.baidu.com/image/pic/item/9f2f070828381f30b2bd028fac014c086e06f074.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/2934349b033b5bb55e73afd833d3d539b600bc74.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/ac345982b2b7d0a2bdfa8bbbceef76094b369ae1.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/2e2eb9389b504fc213023f23e0dde71190ef6db3.jpg" } ;
    private String[] imageUris = new String[]{
            "http://www.baidu.com",
            "http://www.sina.com.cn",
            "http://www.taobao.com",
            "http://www.tudou.com" };

    private List<Map<String,String>> imageList = new ArrayList<Map<String,String>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.publish_layout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        slideshowView=(SlideShowView)getActivity().findViewById(R.id.slideshowView);
        for (int i = 0; i < 4; i++) {
            Map<String,String> image_uri = new HashMap<String,String>();
            image_uri.put("imageUrls", imageUrls[i]);
            image_uri.put("imageUris", imageUris[i]);
            imageList.add(image_uri);
        }
        slideshowView.setImageUrls(imageList);
    }


}
