package com.gaiamount.module_down_up_load.upload.upload_bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haiyang-lu on 16-6-6.
 */
public class UpdateWorksBean implements Serializable {

    /**
     * requirePassword : 0
     * colorist : null
     * address : null
     * keywords : 深圳 航拍
     * director : null
     * cutter : null
     * description : null
     * screenshot : origin/u1688/v4532_screen.png
     * lens : null
     * type : 12,
     * cover :
     * price720 : 0
     * priceOriginal : 0
     * machine : null
     * name : mv
     * price4K : 0
     * photographer : null
     * price1080 : 0
     * price2K : 0
     * id : 4532
     * allowCharge : 0
     * allowDownload : 1
     */

    private ABean a = new ABean();
    /**
     * a : {"requirePassword":0,"colorist":null,"address":null,"keywords":"深圳 航拍","director":null,"cutter":null,"description":null,"screenshot":"origin/u1688/v4532_screen.png","lens":null,"type":"12,","cover":"","price720":0,"priceOriginal":0,"machine":null,"name":"mv","price4K":0,"photographer":null,"price1080":0,"price2K":0,"id":4532,"allowCharge":0,"allowDownload":1}
     * b : 1
     * o : {"mygroup":[{"name":"czxvc","description":"zxc dasfv ","isExamine":1,"id":2,"keywords":"daswdf ,szc ,zcv ,,","domain":null},{"name":"121321","description":"sdfdsc ","isExamine":1,"id":3,"keywords":"qwrewfr,dfvdx,,,","domain":null},{"name":"1eswf设为分","description":"2222222222222222222","isExamine":0,"id":4,"keywords":"dddd顶顶顶顶,xiaozumingcheng小组名称,xiaozumingcheng小组名称,,","domain":null},{"name":"1222211","description":"莎莎陈","isExamine":0,"id":5,"keywords":"chuangjixa创继续啊,chuangjian创建,chuangjian创建,,","domain":null},{"name":"xiaozuming小组民   xiaozumingcheng小组名称","description":"萨乌丁","isExamine":1,"id":6,"keywords":"asd爱上d,111,,,","domain":null},{"name":"asdcas萨达擦拭","description":"阿迪王","isExamine":1,"id":7,"keywords":"ads敖德萨,,,,","domain":null},{"name":"小组已","description":"修改","isExamine":1,"id":8,"keywords":"关键子1,关键子2,挂你来,,","domain":null},{"name":"爱上d","description":"捱三顶四","isExamine":0,"id":9,"keywords":"水水水水,搜索,,,","domain":null},{"name":"asdfg","description":"萨芬的","isExamine":1,"id":10,"keywords":"爱上d,,,,","domain":null},{"name":"新建11","description":"萨芬的","isExamine":0,"id":11,"keywords":"啥东西啊,啊谁知道从,,,","domain":null},{"name":"adASdf","description":"asdfg","isExamine":1,"id":12,"keywords":"搜索,爱上d,,,","domain":null},{"name":"swaddle","description":"奥迪","isExamine":0,"id":13,"keywords":"爱上d,萨顶顶,,,","domain":null},{"name":"哇去死都","description":"是否飞","isExamine":0,"id":14,"keywords":"兽性大发,涨幅达,,,","domain":null},{"name":"萨芬的","description":"打分vedwszf","isExamine":1,"id":15,"keywords":"星战飞船,找媳妇,需支付,,","domain":null},{"name":"小组","description":"你好你好你好  ","isExamine":1,"id":16,"keywords":"1,2,3,4,5","domain":null},{"name":"小组1111","description":"萨芬的","isExamine":0,"id":17,"keywords":"12,222,222,13,","domain":null},{"name":"小组12345","description":"王企鹅是额外砂放入","isExamine":0,"id":18,"keywords":"1,2,3,3,","domain":null},{"name":"哈哈哈哈","description":"其味无穷恶趣味","isExamine":0,"id":19,"keywords":"1212,1,,,","domain":null}],"joined":[]}
     */

    private int b;
    private OBean o;

    public ABean getA() {
        return a;
    }

    public void setA(ABean a) {
        this.a = a;
    }

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

    public static class ABean implements Serializable{
        private int requirePassword;
        private String colorist;
        private String address;
        private String keywords;
        private String director;
        private String cutter;
        private String description;
        private String screenshot;
        private String lens;
        private String type;
        private String cover;
        private float price720;
        private float priceOriginal;
        private String machine;
        private String name;
        private float price4K;
        private String photographer;
        private float price1080;
        private float price2K;
        private int id;
        private int allowCharge;
        private int allowDownload;
        private String password;

        private int have4k;
        private int have2k;
        private int have1080;
        private int have720;

        private int choose;
        private int avc;
        private int yuv;
        private int crf;
        private int bit;
        private long fid;
        private String typeStr;

        public String getTypeStr() {
            return typeStr;
        }

        public void setTypeStr(String typeStr) {
            this.typeStr = typeStr;
        }

        public long getFid() {
            return fid;
        }

        public void setFid(long fid) {
            this.fid = fid;
        }

        public int getBit() {
            return bit;
        }

        public void setBit(int bit) {
            this.bit = bit;
        }

        public int getAvc() {
            return avc;
        }

        public void setAvc(int avc) {
            this.avc = avc;
        }

        public int getYuv() {
            return yuv;
        }

        public void setYuv(int yuv) {
            this.yuv = yuv;
        }

        public int getCrf() {
            return crf;
        }

        public void setCrf(int crf) {
            this.crf = crf;
        }

        private String backStory;

        public String getBackStory() {
            return backStory;
        }

        public void setBackStory(String backStory) {
            this.backStory = backStory;
        }

        public int getChoose() {
            return choose;
        }

        public void setChoose(int choose) {
            this.choose = choose;
        }

        public int getHave4k() {
            return have4k;
        }

        public void setHave4k(int have4k) {
            this.have4k = have4k;
        }

        public int getHave2k() {
            return have2k;
        }

        public void setHave2k(int have2k) {
            this.have2k = have2k;
        }

        public int getHave1080() {
            return have1080;
        }

        public void setHave1080(int have1080) {
            this.have1080 = have1080;
        }

        public int getHave720() {
            return have720;
        }

        public void setHave720(int have720) {
            this.have720 = have720;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getRequirePassword() {
            return requirePassword;
        }

        public void setRequirePassword(int requirePassword) {
            this.requirePassword = requirePassword;
        }

        public String getColorist() {
            return colorist;
        }

        public void setColorist(String colorist) {
            this.colorist = colorist;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getCutter() {
            return cutter;
        }

        public void setCutter(String cutter) {
            this.cutter = cutter;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getLens() {
            return lens;
        }

        public void setLens(String lens) {
            this.lens = lens;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public float getPrice720() {
            return price720;
        }

        public void setPrice720(float price720) {
            this.price720 = price720;
        }

        public float getPriceOriginal() {
            return priceOriginal;
        }

        public void setPriceOriginal(float priceOriginal) {
            this.priceOriginal = priceOriginal;
        }

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getPrice4K() {
            return price4K;
        }

        public void setPrice4K(float price4K) {
            this.price4K = price4K;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public float getPrice1080() {
            return price1080;
        }

        public void setPrice1080(float price1080) {
            this.price1080 = price1080;
        }

        public float getPrice2K() {
            return price2K;
        }

        public void setPrice2K(float price2K) {
            this.price2K = price2K;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAllowCharge() {
            return allowCharge;
        }

        public void setAllowCharge(int allowCharge) {
            this.allowCharge = allowCharge;
        }

        public int getAllowDownload() {
            return allowDownload;
        }

        public void setAllowDownload(int allowDownload) {
            this.allowDownload = allowDownload;
        }

        @Override
        public String toString() {
            return "ABean{" +
                    "requirePassword=" + requirePassword +
                    ", colorist='" + colorist + '\'' +
                    ", address='" + address + '\'' +
                    ", keywords='" + keywords + '\'' +
                    ", director='" + director + '\'' +
                    ", cutter='" + cutter + '\'' +
                    ", description='" + description + '\'' +
                    ", screenshot='" + screenshot + '\'' +
                    ", lens='" + lens + '\'' +
                    ", type='" + type + '\'' +
                    ", cover='" + cover + '\'' +
                    ", price720=" + price720 +
                    ", priceOriginal=" + priceOriginal +
                    ", machine='" + machine + '\'' +
                    ", name='" + name + '\'' +
                    ", price4K=" + price4K +
                    ", photographer='" + photographer + '\'' +
                    ", price1080=" + price1080 +
                    ", price2K=" + price2K +
                    ", id=" + id +
                    ", allowCharge=" + allowCharge +
                    ", allowDownload=" + allowDownload +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class OBean implements Serializable{
        /**
         * name : czxvc
         * description : zxc dasfv
         * isExamine : 1
         * id : 2
         * keywords : daswdf ,szc ,zcv ,,
         * domain : null
         */

        private List<MygroupBean> mygroup;
        private List<?> joined;

        public List<MygroupBean> getMygroup() {
            return mygroup;
        }

        public void setMygroup(List<MygroupBean> mygroup) {
            this.mygroup = mygroup;
        }

        public List<?> getJoined() {
            return joined;
        }

        public void setJoined(List<?> joined) {
            this.joined = joined;
        }

        public static class MygroupBean implements Serializable{
            private String name;
            private String description;
            private int isExamine;
            private int id;
            private String keywords;
            private Object domain;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getIsExamine() {
                return isExamine;
            }

            public void setIsExamine(int isExamine) {
                this.isExamine = isExamine;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public Object getDomain() {
                return domain;
            }

            public void setDomain(Object domain) {
                this.domain = domain;
            }
        }
    }
}
