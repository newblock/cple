package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/3/1.
 */

public class VersionInfoEntity extends Entity{
    private Version version;
    private int status;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Version{
        private int versionNumberMax;
        private int versionNumberMin;
        private String fileUrl;
        private String content;

        public int getVersionNumberMax() {
            return versionNumberMax;
        }

        public void setVersionNumberMax(int versionNumberMax) {
            this.versionNumberMax = versionNumberMax;
        }

        public int getVersionNumberMin() {
            return versionNumberMin;
        }

        public void setVersionNumberMin(int versionNumberMin) {
            this.versionNumberMin = versionNumberMin;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
