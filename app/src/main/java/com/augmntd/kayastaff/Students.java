package com.augmntd.kayastaff;

public class Students {

    public String name;
    public String rollid;
    public String image;

    public Students(){

    }

    public Students(String name, String rollid, String image) {
        this.name = name;
        this.rollid = rollid;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRollid() {
        return rollid;
    }

    public void setRollid(String rollid) {
        this.rollid = rollid;
    }
}
