package com.example.android.complaintapp;

public class Complain {


    private String complain_text;
    private String mobile_num;
    private String name;
    private String room_num;
    private long timestamp;
    private String key;



    public String getComplain_text() {
        return complain_text;
    }

    public void setComplain_text(String complain_text) {
        this.complain_text = complain_text;
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }


    public Complain(){

    }

    public Complain(String bhawan_name,String complain_text,String mobile_num,String name, String room_num,String key,long timestamp) {

        this.complain_text = complain_text;
        this.mobile_num = mobile_num;
        this.name = name;
        this.room_num = room_num;
        this.timestamp = timestamp;
        this.key=key;
    }



    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
