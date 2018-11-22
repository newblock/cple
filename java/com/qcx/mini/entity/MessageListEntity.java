package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class MessageListEntity extends Entity{
    private int commentNum;
    private List<MessageEntity> Comments;

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public List<MessageEntity> getComments() {
        return Comments;
    }

    public void setComments(List<MessageEntity> comments) {
        Comments = comments;
    }

    public static class MessageEntity extends Entity {
        private String picture;
        private String nickName;
        private CommentQ comment;

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

        public CommentQ getComment() {
            return comment;
        }

        public void setComment(CommentQ comment) {
            this.comment = comment;
        }
    }

    public static class CommentQ extends Entity{
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
