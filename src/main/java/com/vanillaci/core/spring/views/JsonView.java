package com.vanillaci.core.spring.views;

import com.vanillaci.util.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * @author Joel Johnson
 */
public class JsonView<T> implements View {
	private final T serializable;

	public static JsonView<Success> success() {
		return new JsonView<>(new Success());
	}

	public JsonView(T serializable) {
		this.serializable = serializable;
	}

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonUtil.serialize(serializable, response.getOutputStream());
	}
}

class Success {
	boolean success = true;

	public boolean isSuccess() {
		return success;
	}
}