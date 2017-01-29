package io.jpad.how2;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Browser {

	static final String UA = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
	
	public static Document get(String url) throws IOException {
		return Jsoup.connect(url).userAgent(UA).ignoreHttpErrors(true).timeout(0).get();
	}
}
