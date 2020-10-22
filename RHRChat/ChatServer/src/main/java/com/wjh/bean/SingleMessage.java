package com.wjh.bean;


public class SingleMessage {

  private long sender;
  private long receiver;
  private String message;
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


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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
