package io.jpad.how2;

class TextUtil {

	static String between(String st, String start, String end) {
		int p = st.indexOf(start);
		if(p > 0) {
			int q = st.indexOf(end, p + start.length());
			if(q > p && p > 0) {
				return st.substring(p + start.length(), q);
			}
		}
		return st;
	}

	static String limitLines(String c, int lines) {
		int idx = 0;
		for(int i=0; i<lines; i++) {
			int p = c.indexOf('\n', idx);
			if(p == -1) {
				return c;
			} else {
				idx = p + 1;
			}
		}
		return c.substring(0, idx) + "\n...";
	}
	
	static int count(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

		    lastIndex = str.indexOf(findStr,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += findStr.length();
		    }
		}
		return count;
	}
}
