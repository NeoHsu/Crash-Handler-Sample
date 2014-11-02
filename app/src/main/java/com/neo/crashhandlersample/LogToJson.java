package com.neo.crashhandlersample;

/**
 * Created by Neo on 14/11/2. Copyright (c) 2014 Neo Hsu. All rights reserved.
 */
public class LogToJson {

  private String Id = "";
  private String OS = "";
  private String Type = "";
  private String Status = "";
  private String Time = "";
  private String Version = "";
  private String UserId = "";
  private String AccessToken = "";
  private String Remark = "";
  private LogDetail logDetail = new LogDetail();

  public class LogDetail {

    private String LogDeviceInfo = "";
    private String LogDeviceToken = "";
    private String LogExecutionTime = "";
    private String LogUrl = "";
    private String LogType = "";
    private String LogParameter = "";
    private String LogResponse = "";
    private String LogException = "";
    private String LogResponseCode = "";

    public void setLogDeviceInfo(String _logDeviceInfo) {
      this.LogDeviceInfo = _logDeviceInfo;
    }

    public String getLogDeviceInfo() {
      return this.LogDeviceInfo;
    }

    public void setLogDeviceToken(String _logDeviceToken) {
      this.LogDeviceToken = _logDeviceToken;
    }

    public String getLogDeviceToken() {
      return this.LogDeviceToken;
    }

    public void setLogExecutionTime(String _logExecutionTime) {
      this.LogExecutionTime = _logExecutionTime;
    }

    public String getLogExecutionTime() {
      return this.LogExecutionTime;
    }

    public void setLogUrl(String _logUrl) {
      this.LogUrl = _logUrl;
    }

    public String getLogUrl() {
      return this.LogUrl;
    }

    public void setLogType(String _logType) {
      this.LogType = _logType;
    }

    public String getLogType() {
      return this.LogType;
    }

    public void setLogParameter(String _logParameter) {
      this.LogParameter = _logParameter;
    }

    public String getLogParameter() {
      return this.LogParameter;
    }

    public void setLogResponse(String _logResponse) {
      this.LogResponse = _logResponse;
    }

    public String getLogResponse() {
      return this.LogResponse;
    }

    public void setLogException(String _logException) {
      this.LogException = _logException;
    }

    public String getLogException() {
      return this.LogException;
    }

    public void setLogResponseCode(String _logResponseCode) {
      this.LogResponseCode = _logResponseCode;
    }

    public String getLogResponseCode() {
      return this.LogResponseCode;
    }

  }

  public LogDetail getLogDetail() {
    return this.logDetail;
  }

  public void setId(String _id) {
    this.Id = _id;
  }

  public String getId() {
    return this.Id;
  }

  public void setOS(String _os) {
    this.OS = _os;
  }

  public String getOS() {
    return this.OS;
  }

  public void setType(String _type) {
    this.Type = _type;
  }

  public String getType() {
    return this.Type;
  }

  public void setStatus(String _status) {
    this.Status = _status;
  }

  public String getStatus() {
    return this.Status;
  }

  public void setTime(String _time) {
    this.Time = _time;
  }

  public String getTime() {
    return this.Time;
  }

  public void setVersion(String _Version) {
    this.Version = _Version;
  }

  public String getVersion() {
    return this.Version;
  }

  public void setUserId(String _userId) {
    this.UserId = _userId;
  }

  public String getUserId() {
    return this.UserId;
  }

  public void setAccessToken(String _accessToken) {
    this.AccessToken = _accessToken;
  }

  public String getAccessToken() {
    return this.AccessToken;
  }

  public void setRemark(String _remark) {
    this.Remark = _remark;
  }

  public String getRemark() {
    return this.Remark;
  }
}
