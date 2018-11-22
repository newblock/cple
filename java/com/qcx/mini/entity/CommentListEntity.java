package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class CommentListEntity extends Entity {
    private List<Comment> commentsTravelList;
    private int status;

    public List<Comment> getCommentsTravelList() {
        return commentsTravelList;
    }

    public void setCommentsTravelList(List<Comment> commentsTravelList) {
        this.commentsTravelList = commentsTravelList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Comment{
        private String creatTime;
        private String nickName;
        private long travelId;
        private String content;
        private String picture;
        private int commentStatus;
        private long commentId;

        public long getCommentId() {
            return commentId;
        }

        public void setCommentId(long commentId) {
            this.commentId = commentId;
        }

        public int getCommentStatus() {
            return commentStatus;
        }

        public void setCommentStatus(int commentStatus) {
            this.commentStatus = commentStatus;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
