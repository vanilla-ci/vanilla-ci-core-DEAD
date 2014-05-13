package com.vanillaci.core.service;

import org.jetbrains.annotations.*;
import org.springframework.stereotype.*;

import java.io.*;

/**
 * @author Joel Johnson
 */
@Service
public class ApplicationConfiguration {


	@NotNull
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public File getApplicationRoot() {
		// TODO: make this configurable
		File file = new File("/Users/joeljohnson/Development/java/vanilla-ci/vanilla-ci-core/work");
		file.mkdirs();
		return file;
	}

	@NotNull
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public File getScriptDirectory() {
		File result = new File(getApplicationRoot(), "scripts");
		result.mkdirs();
		return result;
	}

	@NotNull
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public File getArchivesDirectory() {
		File result = new File(getScriptDirectory(), "archives");
		result.mkdirs();
		return result;
	}
}
