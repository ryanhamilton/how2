package io.jpad.how2;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

import io.jpad.how2.StackOverflowAnswerPage.StackAnswer;
import lombok.Data;

public class How2 {
	
	private static final int ROWS_SHOWN = 20;
	private static final int RESULTS_SHOWN = 7;
	private static final int FULL_LINES = 150;
	private static final int SNIPPET_LINES = 20;
	private static final String NL = "\n";
	
	public static void main(String args[]) throws IOException {
		if(args.length == 1) {
			display(query(args[0]));
		} else if(args.length > 1) {
			try {
				int num = Integer.parseInt(args[1]);
				List<SearchResult> searchResults = query(args[0]);
				if(searchResults.size() > num) {
					SearchResult sr = searchResults.get(num);
					if(args.length == 3 && args[2].equalsIgnoreCase("open")) {
						if(Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().browse(new URI(sr.getUrl()));
							} catch (URISyntaxException e) {
								System.out.println(sr.getUrl());
							}
						} else {
							System.out.println(sr.getUrl());
						}
					}
					
					StackOverflowAnswerPage sop = StackOverflowAnswerPage.fetch(sr.getUrl());
					if(sop == null) {
						System.err.println("cant show that page");
					} else {
						display(sop, FULL_LINES);
					}
				} else {
					System.out.println("Number is outside known result range");
				}
			} catch(NumberFormatException nfe) {
				System.err.println("Expected result number as second argument");
			}
		} else {
			System.out.println("how2 \"search query\" [numberOfResultToDisplay] [open]" + NL + NL
					+ "One argument  e.g. how2 \"java hello world\" = Searches google for stackoverflow results." + NL
					+ "Two arguments e.g. how2 \"java hello world\" 0 = Show the first result" + NL
					+ "Three arguments e.g. how2 \"java hello world\" 0 open = Open that page in browser");
		}
	}

	private static File getCacheFile() { return getFile("cache"); }

	public static File getFile(String name) {
		String home = System.getProperty("user.home");
		File how2 = new File(home, ".how2");
		File cache = new File(how2, name);
		how2.mkdir();
		return cache;
	}
	
	private static List<SearchResult> retrieveCache(String query) {
		File cache = getCacheFile();
		if(cache.exists()) {
			try {
				FileInputStream streamIn = new FileInputStream(cache);
			    ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
			    CacheEntry ce = (CacheEntry) objectinputstream.readObject();
			    if(ce.equals(query)) {
			    	return ce.getSearchResults();
			    }
			} catch(ClassNotFoundException e) {
			} catch(ClassCastException c) {
			} catch(IOException c) {
			}
		}
		return null;
	}

	private static void cache(String query, List<SearchResult> searchResults) {
		CacheEntry cacheEntry = new CacheEntry(query, searchResults);
		File cache = getCacheFile();
		try {
			FileOutputStream fout = new FileOutputStream(cache);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(cacheEntry);	
		} catch (IOException e) {
			// cache fails bad luck
			System.err.println("couldnt cache result");
		}
	}
	
	private static List<SearchResult> query(String query) throws IOException {
		List<SearchResult> r = retrieveCache(query);
		if(r != null) {
			return r;
		} else {
			Document doc = SearchEngine.getDoc(query);
			try {
				List<SearchResult> searchResults = SearchEngine.search(doc);
				cache(query, searchResults);
				return searchResults;
			} catch(Exception e) {
				// any problems parsing search display to user
				File f = File.createTempFile("problem-query", ".html");
				try {
					PrintWriter out = new PrintWriter(f);
				    out.println(doc.outerHtml());
				    if(Desktop.isDesktopSupported()) {
				    	Desktop.getDesktop().browse(f.toURI());
				    } else {
				    	System.err.println("Couldnt parse HTML. See " + f.getAbsolutePath());
				    }
				} catch (IOException ioe) {
				}
				throw new IOException();
			}
		}
	}
	
	private static void display(List<SearchResult> searchResults) throws IOException {
		for(int i=0; i<RESULTS_SHOWN && i<searchResults.size(); i++) {
			SearchResult sr = searchResults.get(i);
			System.out.println(i + ". " + sr.getTitle());
		}
		
		StackOverflowAnswerPage sop = null;
		for(SearchResult sr : searchResults) {
			sop = StackOverflowAnswerPage.fetch(sr.getUrl());
			if(sop != null) {
				break;
			}
		}

		System.out.println();
		if(sop == null) {
			System.out.println("No stackoverflow solution found.");
		} else {
			display(sop, SNIPPET_LINES);
		}
		System.out.println();
	}

	private static void display(StackOverflowAnswerPage sop, int lineLimit) {
		if(sop == null) { throw new IllegalArgumentException(); }

		System.out.println(sop.getTitle());
		System.out.println("----------------------------------");
		List<StackAnswer> answers = sop.getAnswers();
		if(answers.size() > 0) {
			StackAnswer a = answers.get(0);
			// if possible show all text, else show code and some text, else show what code fits 
			if(TextUtil.count(a.getAllText(), "\n") < lineLimit) {
				System.out.println(a.getAllText());
			} else if (TextUtil.count(a.getCode(), "\n") < lineLimit / 2) {
				System.out.println(TextUtil.limitLines(a.getCode(), lineLimit / 2));
				System.out.println("----------------------------------");
				System.out.println(TextUtil.limitLines(a.getText(), lineLimit / 2));
			} else {
				System.out.println(TextUtil.limitLines(a.getCode(), lineLimit));
			}
		} else {
			System.out.println("No Answer");
		}
		System.out.println();
	}
	
	@Data
	private static class CacheEntry implements Serializable {
		private static final long serialVersionUID = 1L;
		private final String query;
		private final List<SearchResult> searchResults;
	}
}
