package ua.com.qbee.smscrypt;

import android.app.Activity;
public class GlobMethods extends Activity {
	public static String onlyNumb(String number) {
		if(number==null) return "";
		String res="";
		//System.out.println("here "+number);
		for(char a:number.toCharArray()){
			if((a<='9'&&a>='0') || a=='+')res+=a;
		}
		
		return res;
	}

}
