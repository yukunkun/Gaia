package com.gaiamount.module_down_up_load.upload.upload_bean;

/**
 * Created by haiyang-lu on 16-4-13.
 */
public class UserCapacity2 {

    /**
     * b : 1
     * o : {"space":{"year":40960,"week":800},"userSpace":{"videoCount":440,"id":1688,"yearSpace":56127.2,"weekSpace":56127.2}}
     */

    private int b;
    /**
     * space : {"year":40960,"week":800}
     * userSpace : {"videoCount":440,"id":1688,"yearSpace":56127.2,"weekSpace":56127.2}
     */

    private OBean o;

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public OBean getO() {
        return o;
    }

    public void setO(OBean o) {
        this.o = o;
    }

    public static class OBean {
        /**
         * year : 40960
         * week : 800
         */

        private SpaceBean space;
        /**
         * videoCount : 440
         * id : 1688
         * yearSpace : 56127.2
         * weekSpace : 56127.2
         */

        private UserSpaceBean userSpace;

        public SpaceBean getSpace() {
            return space;
        }

        public void setSpace(SpaceBean space) {
            this.space = space;
        }

        public UserSpaceBean getUserSpace() {
            return userSpace;
        }

        public void setUserSpace(UserSpaceBean userSpace) {
            this.userSpace = userSpace;
        }

        public static class SpaceBean {
            private int year;
            private int week;

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getWeek() {
                return week;
            }

            public void setWeek(int week) {
                this.week = week;
            }
        }

        public static class UserSpaceBean {
            private int videoCount;
            private int id;
            private double yearSpace;
            private double weekSpace;

            public int getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(int videoCount) {
                this.videoCount = videoCount;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getYearSpace() {
                return yearSpace;
            }

            public void setYearSpace(double yearSpace) {
                this.yearSpace = yearSpace;
            }

            public double getWeekSpace() {
                return weekSpace;
            }

            public void setWeekSpace(double weekSpace) {
                this.weekSpace = weekSpace;
            }
        }
    }
}
