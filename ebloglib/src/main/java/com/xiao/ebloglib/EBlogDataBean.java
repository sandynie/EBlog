package com.xiao.ebloglib;

public class EBlogDataBean {

    private String mTitle;

    private String mContent;

    private int mFollowTimes;

    private int mZanTimes;

    public EBlogDataBean(String title,String content,int follow,int zan){
        this.mTitle = title;
        this.mContent = content;
        this.mFollowTimes = follow;
        this.mZanTimes = zan;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public void setmFollowTimes(int mFollowTimes) {
        this.mFollowTimes = mFollowTimes;
    }

    public void setmZanTimes(int mZanTimes) {
        this.mZanTimes = mZanTimes;
    }



    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public int getmFollowTimes() {
        return mFollowTimes;
    }

    public int getmZanTimes() {
        return mZanTimes;
    }
}
