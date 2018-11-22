package com.qcx.mini.share;

/**
 * Created by Administrator on 2018/1/27.
 */

public class ShareParmas {
    private ShareType shareType;

    private String title;//标题
    private String description;//描述信息
    private String transaction;//唯一标识
    private int bitmap;

    //小程序
    private String webpageUrl;//pageUrl
    private String userName;//userName
    private String path;//path

    private ShareParmas(){}

    public static ShareParmas buildWXMiniProgramParams(String webpageUrl,String userName,String path,String title, String description,int bitmap){
        ShareParmas parmas=new ShareParmas();
        parmas.webpageUrl=webpageUrl;
        parmas.userName=userName;
        parmas.path=path;
        parmas.title=title;
        parmas.description=description;
        parmas.bitmap=bitmap;
        parmas.shareType=ShareType.WX_CHAT;
        return parmas;
    }


    public static ShareParmas buildImgParams(String path){
        ShareParmas parmas=new ShareParmas();
        parmas.path=path;
        return parmas;
    }

    public static ShareParmas buildWebParams(String webpageUrl,String title, String description,int bitmap,ShareType type){
        ShareParmas parmas=new ShareParmas();
        parmas.webpageUrl=webpageUrl;
        parmas.title=title;
        parmas.description=description;
        parmas.bitmap=bitmap;
        parmas.shareType=type;
        return parmas;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public int getBitmap() {
        return bitmap;
    }

    public void setBitmap(int bitmap) {
        this.bitmap = bitmap;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public void setWebpageUrl(String webpageUrl) {
        this.webpageUrl = webpageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
