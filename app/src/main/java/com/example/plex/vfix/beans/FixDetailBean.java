package com.example.plex.vfix.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Plex on 2017/9/10.
 */

public class FixDetailBean {

    public final static int FIX_STATUS_NOT_ACCEPTED = 0;
    public final static int FIX_STATUS_PENDDING     = 1;
    public final static int FIX_STATUS_SUCCESS      = 2;

    private int id;

    @SerializedName(value = "problem")
    private String title;

    @SerializedName(value = "user_id")
    private int userId;

    @SerializedName(value = "desc")
    private String description;

    @SerializedName(value = "build_type")
    private String buildType;

    @SerializedName(value = "build_number")
    private String buildNumber;

    private String address;

    private String resolve; //解决反馈

    @SerializedName(value = "repairer_id")
    private int repairerId;

    @SerializedName(value = "repairer_name")
    private String repairerName;

    @SerializedName(value = "repairer_mobile")
    private String repairerMobile;

    @SerializedName(value = "start_time")
    private String startTime;

    @SerializedName(value = "update_time")
    private String updateTime;

    private int status = FIX_STATUS_NOT_ACCEPTED;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResolve() {
        return resolve;
    }

    public void setResolve(String resolve) {
        this.resolve = resolve;
    }

    public int getRepairerId() {
        return repairerId;
    }

    public void setRepairerId(int repairerId) {
        this.repairerId = repairerId;
    }

    public String getRepairerName() {
        return repairerName;
    }

    public void setRepairerName(String repairerName) {
        this.repairerName = repairerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRepairerMobile() {
        return repairerMobile;
    }

    public void setRepairerMobile(String repairerMobile) {
        this.repairerMobile = repairerMobile;
    }
}
