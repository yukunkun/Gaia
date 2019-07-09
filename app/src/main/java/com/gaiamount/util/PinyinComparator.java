package com.gaiamount.util;

import com.gaiamount.module_user.bean.CountryCode;

import java.util.Comparator;

/**
 * Pinyin Comparator for sort address list
 *
 */
public class PinyinComparator implements Comparator<CountryCode> {

	public int compare(CountryCode o1, CountryCode o2) {
		if (o1.getSortKey().equals("@")
				|| o2.getSortKey().equals("#")) {
			return -1;
		} else if (o1.getSortKey().equals("#")
				|| o2.getSortKey().equals("@")) {
			return 1;
		} else {
			return o1.getSortKey().compareTo(o2.getSortKey());
		}
	}

}
