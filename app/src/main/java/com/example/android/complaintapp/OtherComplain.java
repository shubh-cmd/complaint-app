package com.example.android.complaintapp;

public class OtherComplain {


    private String ComplainDes;
    private String image;
    private String key;

    private long timestamp;



    public String getComplainDes() {
        return ComplainDes;
    }

    public void setComplainDes(String complainDes) {
        ComplainDes = complainDes;
    }



    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public OtherComplain(){

    }

    public OtherComplain(String ComplainDes,String image,String key,long timestamp) {


        this.ComplainDes = ComplainDes;
        this.image = image;
        this.timestamp = timestamp;
        this.key=key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
