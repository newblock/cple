package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class CommentEntity extends Entity {
    private DriverAndTravelEntity selfTravel;
    private List<CommentContent> Comments;
    private int status;
    private int commentNum;

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public DriverAndTravelEntity getSelfTravel() {
        return selfTravel;
    }

    public void setSelfTravel(DriverAndTravelEntity selfTravel) {
        this.selfTravel = selfTravel;
    }

    public List<CommentContent> getComments() {
        return Comments;
    }

    public void setComments(List<CommentContent> comments) {
        Comments = comments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class CommentContent{
        private String picture;
        private String nickName;
        private Comment comment;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Comment getComment() {
            return comment;
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }
    }

    public class Comment{
        private long id;
        private long travelId;
        private long ownerPhone;
        private long targetPhone;
        private String content;
        private long parentID;
        private int level;
        private int status;
        private long createTime;
        private long sortTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public long getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(long ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public long getTargetPhone() {
            return targetPhone;
        }

        public void setTargetPhone(long targetPhone) {
            this.targetPhone = targetPhone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getParentID() {
            return parentID;
        }

        public void setParentID(long parentID) {
            this.parentID = parentID;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getSortTime() {
            return sortTime;
        }

        public void setSortTime(long sortTime) {
            this.sortTime = sortTime;
        }
    }

}
