package com.wjh.bean;


public class ChatGroup {

  private long groupId;
  private long user1;
  private long user2;
  private long user3 = -1;
  private long user4 = -1;
  private long user5 = -1;


  public long getGroupId() {
    return groupId;
  }

  public void setGroupId(long groupId) {
    this.groupId = groupId;
  }


  public long getUser1() {
    return user1;
  }

  public void setUser1(long user1) {
    this.user1 = user1;
  }


  public long getUser2() {
    return user2;
  }

  public void setUser2(long user2) {
    this.user2 = user2;
  }


  public long getUser3() {
    return user3;
  }

  public void setUser3(long user3) {
    this.user3 = user3;
  }


  public long getUser4() {
    return user4;
  }

  public void setUser4(long user4) {
    this.user4 = user4;
  }


  public long getUser5() {
    return user5;
  }

  public void setUser5(long user5) {
    this.user5 = user5;
  }

  @Override
  public String toString() {
    return groupId+","+user1+","+user2+","+user3+","+user4+","+user5;
  }
}
