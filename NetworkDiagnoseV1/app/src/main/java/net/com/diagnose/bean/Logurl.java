package net.com.diagnose.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect
public class Logurl implements Serializable {
    private volatile Date startedDateTime;
    private volatile String url;
    public volatile  LogDetail logDetail ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(Date startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LogDetail getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(LogDetail logDetail) {
        this.logDetail = logDetail;
    }


}
