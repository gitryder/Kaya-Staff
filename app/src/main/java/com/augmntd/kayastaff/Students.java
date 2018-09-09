package com.augmntd.kayastaff;

public class Students {

    public String name;
    public String rollid;
    public String image;
    public String thumb_image;

    public Students(){

    }

    public Students(String name, String rollid, String image, String thumb_image) {
        this.name = name;
        this.rollid = rollid;
        this.image = image;
        this.thumb_image = thumb_image;
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

    public String getThumbImage() {
        return thumb_image;
    }

    public void setThumbImage(String image) {
        this.thumb_image = thumb_image;
    }

}
