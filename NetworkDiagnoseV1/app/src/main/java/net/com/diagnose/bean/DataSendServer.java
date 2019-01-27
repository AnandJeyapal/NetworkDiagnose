package net.com.diagnose.bean;

import net.com.diagnose.bean.LogDetail;

import java.util.List;

public class DataSendServer {
    private String status;
    private List<LogDetail> logDetailList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LogDetail> getLogDetailList() {
        return logDetailList;
    }

    public void setLogDetailList(List<LogDetail> logDetailList) {
        this.logDetailList = logDetailList;
    }
}
