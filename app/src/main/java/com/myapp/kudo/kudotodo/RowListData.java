package com.myapp.kudo.kudotodo;

/**
 * Created by ross on 2015/04/13.
 */
public class RowListData {

    private String title;
    private String subTitle;
    private String id;
    private boolean isFinish;

    public RowListData(){

    }

    public RowListData(String title, String subTitle, String id) {
        this.title = title;
        this.subTitle = subTitle;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }
}
