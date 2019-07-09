package com.gaiamount.apis;

/**
 * APP 配置类
 */
public class Configs {
	/**
	 * 客户端类型：web
	 */
	public static final int CLIENT_WEB = 0;
	/**
	 * 客户端类型：移动应用
	 */
	public static final int CLIENT_APP = 1;

	public static final int PICK_COUNTRY_CODE_REQUEST = 10000;

	public static final String PICK_COUNTRY_CODE_KEY = "code";


	public static final String BASE_URL = "https://gaiamount.com/web";
//	public static final String BASE_URL = "https://test.gaiamount.com:10443/web";
//	public static final String BASE_URL = "http://192.168.1.21:8080/web";
//	public static final String BASE_URL = "http://192.168.1.41:8080/web";


	/**
	 * 获取存储空间信息
	 */
//	public static final String CAPACITY_URL = BASE_URL + "/file/myspace";
	/**
	 * contentType：作品池
	 */
	public static final int TYPE_WORK_POOL = 0;

	/**
	 * 线下的Ts地址
	 */
//	public static final String Ts_URL ="https://qvst.gaiamount.com" ;
	/**
	 * 线上的Ts地址
	 */
	public static final String Ts_URL ="https://qv.gaiamount.com" ;
	/**
     * 测试图片前缀
     */
//    public static final String COVER_PREFIX = "https://qwst.gaiamount.com/";
	/**
     * 公网图片前缀 当切换到公网时，此商量打开
     */
    public static final String COVER_PREFIX = "https://qw.gaiamount.com/";
	/**
	 * 内存缓存的最大值
	 */
	public static final int MEMARY_MAX_SIZE = 10 * 1024 * 1024;
	/**
	 * 本地缓存的最大值
	 */
	public static final int DIKS_MAX_SIZE = 200 * 1024 * 1024;
	/**
     * 本地缓存的路径
     */
    public static String MNT_SDCARD_GAIA = "mnt/sdcard/gaiamount";
	/**
	 * 图文的线下地址
	 */
//	public static String TUWEN_URI = "http://192.168.1.99:3001/#!/learnmb?";

	/**
	 * 图文的公网地址
	 */
	public static String TUWEN_URI = "https://gaiamount.com/learnmb?";
	/**
	 * 豌豆夹下载地址
	 */
	public static String WanDouJiaLoad="http://www.wandoujia.com/search?key=gaiamount";
}
