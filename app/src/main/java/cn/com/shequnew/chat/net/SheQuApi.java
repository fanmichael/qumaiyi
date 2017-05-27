package cn.com.shequnew.chat.net;


import com.hyphenate.easeui.model.GroupImageBean;

import java.util.Map;

import cn.com.shequnew.chat.bean.AddGroupMember;
import cn.com.shequnew.chat.bean.AddGroupMmberResponse;
import cn.com.shequnew.chat.bean.DeleteGroup;
import cn.com.shequnew.chat.bean.GroupMember;
import cn.com.shequnew.chat.bean.GroupMemberRequest;
import cn.com.shequnew.chat.bean.LoadUpResponse;
import cn.com.shequnew.chat.bean.RequestGroupImage;
import cn.com.shequnew.chat.bean.SearchUser;
import cn.com.shequnew.chat.bean.SearchUserResponse;
import cn.com.shequnew.chat.bean.UpdateRequest;
import cn.com.shequnew.chat.bean.UpdateResponse;
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
