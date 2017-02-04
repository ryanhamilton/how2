package io.jpad.how2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchEngine {

	static Document getDoc(String query) throws IOException {
		return Browser.get("https://www.google.com/search?q=" + query  + " site:stackoverflow.com&num=20");
	}

	static List<SearchResult> search(Document doc) throws IOException {
		Elements links = doc.select("div[class=g]");
		List<SearchResult> res = new ArrayList<SearchResult>();
		
		for (Element link : links) {
			Elements titles = link.select("h3[class=r]");
			Element a = link.select("a").first();
			
			String url = a.attr("abs:href");
			url = TextUtil.between(url, "?q=", "&");
			
			String title = titles.text().replace(" - Stack Overflow", "");

			Elements bodies = link.select("span[class=st]");
			String body = bodies.text();
			res.add(new SearchResult(title, url, body));
		}
		return res;
	}
	
	
}
