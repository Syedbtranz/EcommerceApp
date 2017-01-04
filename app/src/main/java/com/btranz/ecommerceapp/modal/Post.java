package com.btranz.ecommerceapp.modal;

/**
 * Created by nirav on 21/02/16.
 */
public class Post {

    private String postTitle;

    private String postSubTitle;
    private String url;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostSubTitle() {
        return postSubTitle;
    }

    public void setPostSubTitle(String postSubTitle) {
        this.postSubTitle = postSubTitle;
    }

    public Post(String postTitle, String postSubTitle, String url) {
        this.postTitle = postTitle;
        this.postSubTitle = postSubTitle;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
