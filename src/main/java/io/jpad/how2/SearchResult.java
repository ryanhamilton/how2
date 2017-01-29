package io.jpad.how2;

import java.io.Serializable;

import lombok.Data;

@Data class SearchResult implements Serializable {
	private final String title;
	private final String url;
	private final String body;
}