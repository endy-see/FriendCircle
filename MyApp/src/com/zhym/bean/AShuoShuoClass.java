package com.zhym.bean;

/**
 * Created by lenovo on 2015/11/2.
 */
public class AShuoShuoClass {
    private String id;             //存储在服务端对应ID
    private String avatar;      //服务端的头像存的是头像名字（真正的头像经过上传服务器会存在服务端的upload文件夹中）
    private String nickName;    //昵称
    private String shuoshuo;
    private String photos;      //这里是指上传的所有图片用分隔符连接组成的一个字符串（当从服务端向客户端加载时，在split即可）

    //下面的暂时先不实现：点赞、评论、（点赞者、评论者）昵称、回复（回复者与被回复者）、表情
    public AShuoShuoClass(String id, String avatar, String nickName, String shuoshuo, String photos) {
        this.id = id;
        this.avatar = avatar;
        this.nickName = nickName;
        this.shuoshuo = shuoshuo;
        this.photos = photos;
    }

    public AShuoShuoClass() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getShuoshuo() {
        return shuoshuo;
    }

    public void setShuoshuo(String shuoshuo) {
        this.shuoshuo = shuoshuo;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "AShuoShuo[ id="+this.id+", nickname="+this.nickName+", shuoshuo="+this.shuoshuo+", photos="+this.photos+" ]";
    }
}
