package net.com.diagnose.bean;

import java.io.Serializable;

public class LogDetail implements Serializable {

    private volatile String serverIPAddress;
    private volatile String runiingTime;
    private volatile String networkType;
    private volatile String dns;
    private volatile String url;

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    public String getRuniingTime() {
        return runiingTime;
    }

    public void setRuniingTime(String runiingTime) {
        this.runiingTime = runiingTime;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
