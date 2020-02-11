package shopinpager.wingstud.shopinpagerseller.model;

/**
 * Created by KHEMRAJ on 9/5/2018.
 */
public class UserBean {

    /**
     * recordId : 891ed644-0461-4143-87ad-dfb44a6204f5
     * active : true
     * createdAt : Thu Apr 18 18:33:21 IST 2019
     * modifiedAt : Thu Apr 18 18:33:21 IST 2019
     * name : Rocy
     * email : rocy@rsdf.com
     * mobile : 12
     * latitude : 0
     * longitude : 0
     * driver_imageUrl : https://s3.ap-south-1.amazonaws.com/crypto-pro-bucket/1555592601891_aboutus.jpg
     * activeMode : true
     * busyWithOrder : true
     * username : RSDRI00008
     */

    private String recordId;
    private boolean active;
    private String createdAt;
    private String modifiedAt;
    private String name;
    private String email;
    private String mobile;
    private int latitude;
    private int longitude;
    private String driver_imageUrl;
    private boolean activeMode;
    private boolean busyWithOrder;
    private String username;
    private String password;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getDriver_imageUrl() {
        return driver_imageUrl;
    }

    public void setDriver_imageUrl(String driver_imageUrl) {
        this.driver_imageUrl = driver_imageUrl;
    }

    public boolean isActiveMode() {
        return activeMode;
    }

    public void setActiveMode(boolean activeMode) {
        this.activeMode = activeMode;
    }

    public boolean isBusyWithOrder() {
        return busyWithOrder;
    }

    public void setBusyWithOrder(boolean busyWithOrder) {
        this.busyWithOrder = busyWithOrder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
