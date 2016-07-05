package com.owen.appplus.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owen on 2016/7/1.
 */
public class BeanApp implements Parcelable {

    private Bitmap icon;
    private String name;
    private String packageName;
    private String versionName;
    private int versionCode;
    private String srcPath;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.icon, flags);
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeString(this.srcPath);
    }

    public BeanApp() {
    }

    protected BeanApp(Parcel in) {
        this.icon = in.readParcelable(Bitmap.class.getClassLoader());
        this.name = in.readString();
        this.packageName = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.srcPath = in.readString();
    }

    public static final Creator<BeanApp> CREATOR = new Creator<BeanApp>() {
        @Override
        public BeanApp createFromParcel(Parcel source) {
            return new BeanApp(source);
        }

        @Override
        public BeanApp[] newArray(int size) {
            return new BeanApp[size];
        }
    };

    @Override
    public String toString() {
        return "BeanApp{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", srcPath='" + srcPath + '\'' +
                '}';
    }
}
