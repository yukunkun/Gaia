package com.gaiamount.module_charge.account_book;

import java.util.List;

/**
 * Created by haiyang-lu on 16-5-20.
 */
public class GetProfit {


    /**
     * a : [{"number":"20160530150821996097","wname":"James Blunt - If Time Is All I Have.mp4","amount":0,"wid":4616,"bankCard":"6212111111111111111","createTime":{"date":30,"day":1,"hours":15,"minutes":8,"month":4,"nanos":0,"seconds":42,"time":1464592122000,"timezoneOffset":-480,"year":116},"description":null,"bankName":"中国光大银行","type":0,"userName":"卢海洋","contentExtra":0,"status":0}]
     * b : 1
     * o : {"works":32,"material":1,"amounts":300,"course":2,"page":{"beginNum":0,"pageIndex":1,"pageSize":20,"total":1},"profitCreateTime":{"date":17,"day":4,"hours":11,"minutes":9,"month":11,"nanos":0,"seconds":12,"time":1450321752000,"timezoneOffset":-480,"year":115}}
     */

    private int b;
    /**
     * works : 32
     * material : 1
     * amounts : 300
     * course : 2
     * page : {"beginNum":0,"pageIndex":1,"pageSize":20,"total":1}
     * profitCreateTime : {"date":17,"day":4,"hours":11,"minutes":9,"month":11,"nanos":0,"seconds":12,"time":1450321752000,"timezoneOffset":-480,"year":115}
     */

    private OBean o;
    /**
     * number : 20160530150821996097
     * wname : James Blunt - If Time Is All I Have.mp4
     * amount : 0
     * wid : 4616
     * bankCard : 6212111111111111111
     * createTime : {"date":30,"day":1,"hours":15,"minutes":8,"month":4,"nanos":0,"seconds":42,"time":1464592122000,"timezoneOffset":-480,"year":116}
     * description : null
     * bankName : 中国光大银行
     * type : 0
     * userName : 卢海洋
     * contentExtra : 0
     * status : 0
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
        private int amounts;
        private int course;
        /**
         * beginNum : 0
         * pageIndex : 1
         * pageSize : 20
         * total : 1
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

        public int getAmounts() {
            return amounts;
        }

        public void setAmounts(int amounts) {
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
        private String number;
        private String wname;
        private int amount;
        private int wid;
        private String bankCard;
        /**
         * date : 30
         * day : 1
         * hours : 15
         * minutes : 8
         * month : 4
         * nanos : 0
         * seconds : 42
         * time : 1464592122000
         * timezoneOffset : -480
         * year : 116
         */

        private CreateTimeBean createTime;
        private Object description;
        private String bankName;
        private int type;
        private String userName;
        private int contentExtra;
        private int status;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getWname() {
            return wname;
        }

        public void setWname(String wname) {
            this.wname = wname;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getWid() {
            return wid;
        }

        public void setWid(int wid) {
            this.wid = wid;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public CreateTimeBean getCreateTime() {
            return createTime;
        }

        public void setCreateTime(CreateTimeBean createTime) {
            this.createTime = createTime;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
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

        public int getContentExtra() {
            return contentExtra;
        }

        public void setContentExtra(int contentExtra) {
            this.contentExtra = contentExtra;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
