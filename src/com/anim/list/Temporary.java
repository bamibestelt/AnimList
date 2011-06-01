package com.anim.list;

import android.database.Cursor;

public class Temporary {
	static String[]pre = new String[20];
	static String[]list = new String[30];
	static String[]ren = new String[30];
	
	public static void Temp(Cursor re){
		re.moveToFirst(); int c=0;
		while(re.isAfterLast()==false){
			pre[c] = re.getString(0);
			c++;
			re.moveToNext();
		}
	}
	
	public static void Tempst(Cursor li){
		li.moveToFirst(); int d=0;
		while(li.isAfterLast()==false){
			list[d] = li.getString(0);
			d++;
			li.moveToNext();
		}
	}
	
	public static void Tempo(Cursor le){
		le.moveToFirst(); int e=0;
		while(le.isAfterLast()==false){
			ren[e] = le.getString(0);
			e++;
			le.moveToNext();
		}
	}
}
