package com.gaiamount.module_charge.account_book;

import java.util.List;

/**
 * Created by luxiansheng on 16/5/31.
 */
public class OutProfit {

    /**
     * a : [{"amount":0.01,"contentId":3,"channel":"alipay_pc_direct","type":0,"userName":null,"contentName":"sunset2.avi","number":"20160527173635985948","createTime":{"date":27,"day":5,"hours":17,"minutes":37,"month":4,"nanos":0,"seconds":2,"time":1464341822000,"timezoneOffset":-480,"year":116},"id":43,"account":null,"status":1,"contentExtra":4},{"amount":0.01,"contentId":3,"channel":"alipay_pc_direct","type":0,"userName":null,"contentName":"sunset2.avi","number":"20160527173635985948","createTime":{"date":27,"day":5,"hours":17,"minutes":37,"month":4,"nanos":0,"seconds":2,"time":1464341822000,"timezoneOffset":-480,"year":116},"id":44,"account":null,"status":1,"contentExtra":0}]
     * b : 1
     * o : {"works":5,"material":0,"amounts":0.02,"course":0,"page":{"beginNum":0,"pageIndex":1,"pageSize":20,"total":2},"profitCreateTime":{"date":17,"day":4,"hours":11,"minutes":9,"month":11,"nanos":0,"seconds":12,"time":1450321752000,"timezoneOffset":-480,"year":115}}
     */

    private int b;
    /**
     * works : 5
     * material : 0
     * amounts : 0.02
     * course : 0
     * page : {"beginNum":0,"pageIndex":1,"pageSize":20,"total":2}
     * profitCreateTime : {"date":17,"day":4,"hours":11,"minutes":9,"month":11,"nanos":0,"seconds":12,"time":1450321752000,"timezoneOffset":-480,"year":115}
     */

    private OBean o;
    /**
     * amount : 0.01
     * contentId : 3
     * channel : alipay_pc_direct
     * type : 0
     * userName : null
     * contentName : sunset2.avi
     * number : 20160527173635985948
     * createTime : {"date":27,"day":5,"hours":17,"minutes":37,"month":4,"nanos":0,"seconds":2,"time":1464341822000,"timezoneOffset":-480,"year":116}
     * id : 43
     * account : null
     * status : 1
     * contentExtra : 4
     */

    private List<ABean> a;

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

    public List<ABean> getA() {
        return a;
    }

    public void setA(List<ABean> a) {
        this.a = a;
    }

    public static class OBean {
        private int works;
        private int material;
        private double amounts;
        private int course;
        /**
         * beginNum : 0
         * pageIndex : 1
         * pageSize : 20
         * total : 2
         */

        private PageBean page;
        /**
         * date : 17
         * day : 4
         * hours : 11
         * minutes : 9
         * month : 11
         * nanos : 0
         * seconds : 12
         * time : 1450321752000
         * timezoneOffset : -480
         * year : 115
         */

        private ProfitCreateTimeBean profitCreateTime;

        public int getWorks() {
            return works;
        }

        public void setWorks(int works) {
            this.works = works;
        }

        public int getMaterial() {
            return material;
        }

        public void setMaterial(int material) {
            this.material = material;
        }

        public double getAmounts() {
            return amounts;
        }

        public void setAmounts(double amounts) {
            this.amounts = amounts;
        }

        public int getCourse() {
            return course;
        }

        public void setCourse(int course) {
            this.course = course;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ProfitCreateTimeBean getProfitCreateTime() {
            return profitCreateTime;
        }

        public void setProfitCreateTime(ProfitCreateTimeBean profitCreateTime) {
            this.profitCreateTime = profitCreateTime;
        }

        public static class PageBean {
            private int beginNum;
            private int pageIndex;
            private int pageSize;
            private int total;

            public int getBeginNum() {
                return beginNum;
            }

            public void setBeginNum(int beginNum) {
                this.beginNum = beginNum;
            }

            public int getPageIndex() {
                return pageIndex;
            }

            public void setPageIndex(int pageIndex) {
                this.pageIndex = pageIndex;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }

        public static class ProfitCreateTimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }

    public static class ABean {
        private double amount;
        private int contentId;
        private String channel;
        private int type;
        private String userName;
        private String contentName;
        private String number;
        /**
         * date : 27
         * day : 5
         * hours : 17
         * minutes : 37
         * month : 4
         * nanos : 0
         * seconds : 2
         * time : 1464341822000
         * timezoneOffset : -480
         * year : 116
         */

        private CreateTimeBean createTime;
        private int id;
        private Object account;
        private int status;
        private int contentExtra;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getContentId() {
            return contentId;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public CreateTimeBean getCreateTime() {
            return createTime;
        }

        public void setCreateTime(CreateTimeBean createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getAccount() {
            return account;
        }

        public void setAccount(Object account) {
            this.account = account;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getContentExtra() {
            return contentExtra;
        }

        public void setContentExtra(int contentExtra) {
            this.contentExtra = contentExtra;
        }

        public static class CreateTimeBean {
            private int date;
            private int day;
            private int hours;
            private int minutes;
            private int month;
            private int nanos;
            private int seconds;
            private long time;
            private int timezoneOffset;
            private int year;

            public int getDate() {
                return date;
            }

            public void setDate(int date) {
                this.date = date;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHours() {
                return hours;
            }

            public void setHours(int hours) {
                this.hours = hours;
            }

            public int getMinutes() {
                return minutes;
            }

            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getNanos() {
                return nanos;
            }

            public void setNanos(int nanos) {
                this.nanos = nanos;
            }

            public int getSeconds() {
                return seconds;
            }

            public void setSeconds(int seconds) {
                this.seconds = seconds;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getTimezoneOffset() {
                return timezoneOffset;
            }

            public void setTimezoneOffset(int timezoneOffset) {
                this.timezoneOffset = timezoneOffset;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }
        }
    }
}
