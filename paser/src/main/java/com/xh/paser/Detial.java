package com.xh.paser;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Detial implements Parcelable {
    public static final Creator<Detial> CREATOR
            = new Creator<Detial>() {
        public Detial createFromParcel(Parcel in) {
            return new Detial(in);
        }

        public Detial[] newArray(int size) {
            return new Detial[size];
        }
    };
    public String img = "";
    public String detialUrl = "";
    public String score = "";
    public String time = "";
    public String state = "";
    public String name = "";
    public String text = "";
    public String type = "";
    public String area = "";
    public String daoyan = "";
    public String jianjie = "";
    public List<DetailPlayUrl> playUrls;
    public String source = "";
    public String platformas = "";

    private Detial(Parcel in) {
        img = in.readString();
        detialUrl = in.readString();
        score = in.readString();
        time = in.readString();
        state = in.readString();
        name = in.readString();
        text = in.readString();
        type = in.readString();
        area = in.readString();
        daoyan = in.readString();
        jianjie = in.readString();
        source = in.readString();
        platformas = in.readString();
    }

    public Detial() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(detialUrl);
        dest.writeString(score);
        dest.writeString(time);
        dest.writeString(state);
        dest.writeString(name);
        dest.writeString(text);
        dest.writeString(type);
        dest.writeString(area);
        dest.writeString(daoyan);
        dest.writeString(jianjie);
        dest.writeString(source);
        dest.writeString(platformas);
    }


    public static class DetailPlayUrl implements Parcelable {

        public static final Creator<DetailPlayUrl> CREATOR
                = new Creator<DetailPlayUrl>() {
            public DetailPlayUrl createFromParcel(Parcel in) {
                return new DetailPlayUrl(in);
            }

            public DetailPlayUrl[] newArray(int size) {
                return new DetailPlayUrl[size];
            }
        };

        public DetailPlayUrl() {
        }

        private DetailPlayUrl(Parcel in) {
            title = in.readString();
            href = in.readString();
            vid = in.readString();
            duration = in.readString();
            cid = in.readString();

        }


        public String title = "";
        public String href = "";
        public String vid = "";
        public String duration = "";
        public String cid = "";

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(href);
            dest.writeString(vid);
            dest.writeString(duration);
            dest.writeString(cid);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

}
