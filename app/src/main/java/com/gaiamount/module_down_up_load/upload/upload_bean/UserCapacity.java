package com.gaiamount.module_down_up_load.upload.upload_bean;

import com.gaiamount.module_down_up_load.upload.UploadActivity;

/**
 * Created by haiyang-lu on 16-4-13.
 * @see UploadActivity
 */
public class UserCapacity {
    private UserSpace userSpace;
    private Space space;

    public class UserSpace{
        long id;
        int videoCount;
        double weekSpace;
        double yearSpace;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(int videoCount) {
            this.videoCount = videoCount;
        }

        public double getWeekSpace() {
            return weekSpace;
        }

        public void setWeekSpace(double weekSpace) {
            this.weekSpace = weekSpace;
        }

        public double getYearSpace() {
            return yearSpace;
        }

        public void setYearSpace(double yearSpace) {
            this.yearSpace = yearSpace;
        }

        @Override
        public String toString() {
            return "UserSpace{" +
                    "id=" + id +
                    ", videoCount=" + videoCount +
                    ", weekSpace=" + weekSpace +
                    ", yearSpace=" + yearSpace +
                    '}';
        }
    }

    public class Space {
        double week;
        double year;

        public double getWeek() {
            return week;
        }

        public void setWeek(double week) {
            this.week = week;
        }

        public double getYear() {
            return year;
        }

        public void setYear(double year) {
            this.year = year;
        }

        @Override
        public String toString() {
            return "Space{" +
                    "week=" + week +
                    ", year=" + year +
                    '}';
        }
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    @Override
    public String toString() {
        return "UserCapacity{" +
                "userSpace=" + userSpace +
                ", space=" + space +
                '}';
    }
}

