package com.gdcp.wuziqiview;

import android.graphics.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by asus- on 2017/6/17.
 */

public class Match extends BmobObject implements Serializable{
    private String userA;
    private String userB;
    private List<Point>blackArray=new ArrayList<>();
    private List<Point>whiteArray=new ArrayList<>();
    //下棋结果，0代表白棋获胜，1代表黑棋获胜
    private Integer result;
    private Integer room;
    private String phone;

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    public List<Point> getBlackArray() {
        return blackArray;
    }

    public void setBlackArray(List<Point> blackArray) {
        this.blackArray = blackArray;
    }

    public List<Point> getWhiteArray() {
        return whiteArray;
    }

    public void setWhiteArray(List<Point> whiteArray) {
        this.whiteArray = whiteArray;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
