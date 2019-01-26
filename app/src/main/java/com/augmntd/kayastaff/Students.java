package com.augmntd.kayastaff;

public class Students {

    public String name;
    public String roll_no;
    public String image;
    public String thumb_image;

    public Students(){

    }

    public Students(String name, String roll_no, String image, String thumb_image) {
        this.name = name;
        this.roll_no = roll_no;
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

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getThumbImage() {
        return thumb_image;
    }

    public void setThumbImage(String image) {
        this.thumb_image = thumb_image;
    }

}
