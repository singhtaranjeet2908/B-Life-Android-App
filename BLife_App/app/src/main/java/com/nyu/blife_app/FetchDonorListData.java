package com.nyu.blife_app;

/**
 * Created by Pranav Gunner on 4/18/2015.
 */
public class FetchDonorListData {

    private String title;
    private int imageUrl;

    public FetchDonorListData(String title,int imageUrl) {

        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }


}
