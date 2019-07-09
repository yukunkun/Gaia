package com.gaiamount.module_workpool;

/**
 * Created by haiyang-lu on 16-3-17.
 * 作品信息
 */
public class WorkInfo {
    String workName="";//作品名称
    String photoGrapher="";//摄影师
    String cutter="";//剪辑师
    String colorist="";//调色师
    String director="";//导演
    String cameraType="";
    String lens="";//镜头
    String location="";//地点

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getPhotoGrapher() {
        return photoGrapher;
    }

    public void setPhotoGrapher(String photoGrapher) {
        this.photoGrapher = photoGrapher;
    }

    public String getCutter() {
        return cutter;
    }

    public void setCutter(String cutter) {
        this.cutter = cutter;
    }

    public String getColorist() {
        return colorist;
    }

    public void setColorist(String colorist) {
        this.colorist = colorist;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "WorkInfo{" +
                "workName='" + workName + '\'' +
                ", photoGrapher='" + photoGrapher + '\'' +
                ", cutter='" + cutter + '\'' +
                ", colorist='" + colorist + '\'' +
                ", director='" + director + '\'' +
                ", cameraType='" + cameraType + '\'' +
                ", lens='" + lens + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
