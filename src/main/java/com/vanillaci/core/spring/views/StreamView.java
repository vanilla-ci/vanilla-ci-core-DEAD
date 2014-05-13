package com.vanillaci.core.spring.views;

import org.springframework.util.*;
import org.springframework.web.servlet.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * Writes the given input stream to the response and then closes it.
 * Useful for file downloads.
 *
 * @author Joel Johnson
 */
public class StreamView implements View {
	private final String contentType;
	private final InputStream stream;

	public StreamView(String contentType, InputStream object) {
		this.contentType = contentType;
		this.stream = object;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			OutputStream outputStream = response.getOutputStream();
			StreamUtils.copy(stream, outputStream);
		} finally {
			stream.close();
		}
	}
}
