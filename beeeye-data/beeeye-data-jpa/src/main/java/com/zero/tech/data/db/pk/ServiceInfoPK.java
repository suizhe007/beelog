package com.zero.tech.data.db.pk;

import javax.persistence.Column;

public class ServiceInfoPK {
    // 服务的接口名
    @Column(name = "iface", nullable = false)
    private String iface;
    // 服务的方法名
    @Column(name = "method", nullable = false)
    private String method;

    public ServiceInfoPK() {

    }

    public ServiceInfoPK(String iface, String method) {
        this.iface = iface;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceInfoPK that = (ServiceInfoPK) o;

        if (iface != null ? !iface.equals(that.iface) : that.iface != null) return false;
        return method != null ? method.equals(that.method) : that.method == null;
    }

    @Override
    public int hashCode() {
        int result = iface != null ? iface.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    public String getIface() {
        return iface;
    }

    public ServiceInfoPK setIface(String iface) {
        this.iface = iface;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ServiceInfoPK setMethod(String method) {
        this.method = method;
        return this;
    }
}
