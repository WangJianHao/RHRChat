package com.wjh.bean;


public class SingleFile {

  private long sender;
  private long receiver;
  private String path;
  private java.sql.Timestamp time;
  private long status;


  public long getSender() {
    return sender;
  }

  public void setSender(long sender) {
    this.sender = sender;
  }


  public long getReceiver() {
    return receiver;
  }

  public void setReceiver(long receiver) {
    this.receiver = receiver;
  }


  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  public java.sql.Timestamp getTime() {
    return time;
  }

  public void setTime(java.sql.Timestamp time) {
    this.time = time;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }

}
