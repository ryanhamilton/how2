package io.jpad.how2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Data;

@Data class StackOverflowAnswerPage {
	
	private static final String NL = "\r\n";
	private final String title;
	private final String question;
	private final List<StackOverflowAnswerPage.StackAnswer> answers;
	
	
	private StackOverflowAnswerPage(Document document) {
		if(document == null) {
			throw new IllegalArgumentException("You must specify document");
		}
		title = document.title(); 
		question = document.select("div.post-text").text();
		answers = new ArrayList<StackAnswer>();
		for(Element answerElement : document.select("div.answer")) {
			
			String allText = getText(answerElement, "code,p");
			String code = getText(answerElement, "code");
			String text = getText(answerElement, "p");

			answers.add(new StackAnswer(code, text, allText));
		}
	}

	private String getText(Element answerElement, String cssQuery) {
		StringBuilder sb = new StringBuilder();
		for(Element codeElement : answerElement.select(cssQuery)) {
			sb.append(codeElement.text() + NL);
		}
		String code = sb.toString();
		return code;
	}

	public static StackOverflowAnswerPage fetch(String url) throws IOException {
		String quest = "//stackoverflow.com/questions/";
		if(url.contains(quest)) {
			String num = TextUtil.between(url, quest, "/");
			try {
				long l = Long.parseLong(num);
			} catch(NumberFormatException nfe) {
				return null; // doesn't seem to be a SO page
			}
		}
		Document document = Browser.get(url);
		return new StackOverflowAnswerPage(document);
	}

	@Data
	static class StackAnswer {
		private final String code;
		private final String text;
		private final String allText;
	}	
}