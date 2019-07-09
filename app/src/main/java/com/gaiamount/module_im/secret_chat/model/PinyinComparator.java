package com.gaiamount.module_im.secret_chat.model;

import com.gaiamount.module_im.secret_chat.bean.ContactInfo;

import java.util.Comparator;


public class PinyinComparator implements Comparator<ContactInfo>
{

	public int compare(ContactInfo o1, ContactInfo o2) {
			if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#"))
				return -1;
			else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@"))
				return 1;
			else
				return o1.getSortLetters().compareTo(o2.getSortLetters());
	}

}
