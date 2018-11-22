package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class PushHistorEntity extends Entity {
    private List<PushContent> pushHistory;
    private boolean isHaveNewComment;
    private int status;

    public boolean isHaveNewComment() {
        return isHaveNewComment;
    }

    public void setHaveNewComment(boolean haveNewComment) {
        isHaveNewComment = haveNewComment;
    }

    public List<PushContent> getPushHistory() {
        return pushHistory;
    }

    public void setPushHistory(List<PushContent> pushHistory) {
        this.pushHistory = pushHistory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class PushContent{
        private String pushId;
        private String msg;
        private String pushValueOfType;
        private String createTime;
        private String title;
        private String pushType;
        private boolean haveRead;

        public String getPushId() {
            return pushId;
        }

        public void setPushId(String pushId) {
            this.pushId = pushId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPushValueOfType() {
            return pushValueOfType;
        }

        public void setPushValueOfType(String pushValueOfType) {
            this.pushValueOfType = pushValueOfType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPushType() {
            return pushType;
        }

        public void setPushType(String pushType) {
            this.pushType = pushType;
        }

        public boolean isHaveRead() {
            return haveRead;
        }

        public void setHaveRead(boolean haveRead) {
            this.haveRead = haveRead;
        }
    }
}
