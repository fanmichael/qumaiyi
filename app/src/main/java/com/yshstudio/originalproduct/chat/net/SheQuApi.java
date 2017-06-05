package com.yshstudio.originalproduct.chat.net;


import com.hyphenate.easeui.model.GroupImageBean;

import java.util.Map;

import com.yshstudio.originalproduct.chat.bean.AddGroupMember;
import com.yshstudio.originalproduct.chat.bean.AddGroupMmberResponse;
import com.yshstudio.originalproduct.chat.bean.DeleteGroup;
import com.yshstudio.originalproduct.chat.bean.GroupMember;
import com.yshstudio.originalproduct.chat.bean.GroupMemberRequest;
import com.yshstudio.originalproduct.chat.bean.LoadUpResponse;
import com.yshstudio.originalproduct.chat.bean.RequestGroupImage;
import com.yshstudio.originalproduct.chat.bean.SearchUser;
import com.yshstudio.originalproduct.chat.bean.SearchUserResponse;
import com.yshstudio.originalproduct.chat.bean.UpdateRequest;
import com.yshstudio.originalproduct.chat.bean.UpdateResponse;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by jiang_ruicheng on 16/10/27.
 */
public interface SheQuApi {
    @POST("index.php/app/")
    Observable<GroupImageBean> downloadImage(@Body RequestGroupImage image);

    @POST("index.php/app/")
    Observable<SearchUserResponse> searchUser(@Body SearchUser user);

    @POST("index.php/app/")
    Observable<AddGroupMmberResponse> addGroupMember(@Body AddGroupMember user);

    @POST("index.php/app/")
    Observable<Body> deleteGroup(@Body DeleteGroup user);

    @POST("index.php/app/")
    Observable<GroupMember> getGroupMember(@Body GroupMemberRequest request);

    @POST("index.php/app/")
    Observable<UpdateResponse> update(@Body UpdateRequest request);

    @Multipart
    @POST("index.php/app/")
    Observable<LoadUpResponse> uploadImage(@PartMap Map<String, RequestBody> pa);
}