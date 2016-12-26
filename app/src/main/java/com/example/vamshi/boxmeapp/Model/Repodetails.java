package com.example.vamshi.boxmeapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vamshi on 15-12-2016.
 */

public class Repodetails implements Parcelable{

    String title;
    String description;
    String repourl;
    int color;
    String avatarurl;
    int watchers;
    int forks;
    int stars;
    String owner;

    public Repodetails() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepourl() {
        return repourl;
    }

    public void setRepourl(String repourl) {
        this.repourl = repourl;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static final Parcelable.Creator<Repodetails> CREATOR = new Creator<Repodetails>() {
        public Repodetails createFromParcel(Parcel source) {
            Repodetails mRepo = new Repodetails();
            mRepo.title = source.readString();
            mRepo.description = source.readString();
            mRepo.repourl = source.readString();
            mRepo.color = source.readInt();
            mRepo.avatarurl = source.readString();
            mRepo.watchers = source.readInt();
            mRepo.forks = source.readInt();
            mRepo.stars = source.readInt();
            mRepo.owner = source.readString();

            return mRepo;
        }

        public Repodetails[] newArray(int size) {
            return new Repodetails[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(repourl);
        parcel.writeInt(color);
        parcel.writeString(avatarurl);
        parcel.writeInt(watchers);
        parcel.writeInt(forks);
        parcel.writeInt(stars);
        parcel.writeString(owner);
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        description = in.readString();
        repourl = in.readString();
        color = in.readInt();
        watchers = in.readInt();
        avatarurl = in.readString();
        forks = in.readInt();
        stars = in.readInt();
        owner = in.readString();
    }
}
