package it.io.openliberty.deepdive.rest;

public class SystemData {

    private int id;
    private String hostname;
    private String osName;
    private String javaVersion;
    private Long heapSize;

    public SystemData() {
    }

    public int getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getOsName() {
        return osName;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public Long getHeapSize() {
        return heapSize;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public void setHeapSize(Long heapSize) {
        this.heapSize = heapSize;
    }
}
