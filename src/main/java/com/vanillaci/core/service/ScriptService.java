package com.vanillaci.core.service;

import com.vanillaci.core.exceptions.*;
import com.vanillaci.scriptbundles.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;

/**
 * @author Joel Johnson
 */
@Service
public class ScriptService {
	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	public void installScriptFromZip(String name, File newScriptZip, boolean overwrite) throws IOException, ZipException {
		File scriptDirectory = applicationConfiguration.getScriptDirectory();

		File explodedScript = new File(scriptDirectory, name);
		if(!overwrite && explodedScript.exists()) {
			throw new ScriptAlreadyExistsException(name);
		}

		explodeScript(newScriptZip, explodedScript);
		validateScript(explodedScript);
		// I don't delete the script after attempting to explode it and it fails,
		// because I want to give the uploader an opportunity to look at it to debug what happened.
	}

	private void validateScript(File scriptDirectory) throws IOException {
		Script script;
		try {
			script = Script.forDirectory(scriptDirectory);
		} catch (IOException e) {
			throw new IOException("invalid script structure", e);
		}

		Manifest manifest = script.getManifest();
		String name = manifest.getName();
		String version = manifest.getVersion();

		String givenFileName = scriptDirectory.getName();
		String expectedFileName = name + "_" + version;

		if(!givenFileName.equals(expectedFileName)) {
			throw new IOException("Expected the script to have a file name that matches the manifest file.");
		}
	}

	private void explodeScript(File newScript, File explodedScript) throws IOException, ZipException {
		if(!explodedScript.mkdirs()) {
			throw new IOException("Unable to create script directory: " + explodedScript.getAbsolutePath());
		}

		ZipFile zipFile = new ZipFile(newScript);
		zipFile.extractAll(explodedScript.getAbsolutePath());
	}
}
