package com.example.wikisearch.model;

public class WikiModel {
    int pageid = 0;
    String title = "0";
    int index = 0;
    ThumbnailModel thumbnail = null;
    TermslModel terms = null;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ThumbnailModel getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailModel thumbnail) {
        this.thumbnail = thumbnail;
    }

    public TermslModel getTerms() {
        return terms;
    }

    public void setTerms(TermslModel terms) {
        this.terms = terms;
    }
}
