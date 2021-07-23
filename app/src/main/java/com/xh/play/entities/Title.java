package com.xh.play.entities;

public class Title {
    public String title;
    public String url;

    public Title() {
    }

    public Title(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Title{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
