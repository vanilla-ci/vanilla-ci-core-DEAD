package com.vanillaci.core.service;

import com.vanillaci.core.exceptions.*;
import com.vanillaci.scriptbundles.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.exception.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * @author Joel Johnson
 */
@Service
public class ScriptService {
	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	@Autowired
	private ScriptRepository scriptRepository;

	public Script getScript(String name, String version) {
		return scriptRepository.get(name, version);
	}

	public void installScriptFromZip(String name, File newScriptZip, boolean overwrite) throws IOException, ZipException {
		File scriptDirectory = applicationConfiguration.getScriptDirectory();

		File explodedScriptDirectory = new File(scriptDirectory, name);
		if(explodedScriptDirectory.exists() || scriptRepository.contains(name)) {
			if(!overwrite) {
				throw new ScriptAlreadyExistsException(name);
			} else {
				scriptRepository.remove(name);
				FileUtils.deleteDirectory(explodedScriptDirectory);
			}
		}

		explodeScript(newScriptZip, explodedScriptDirectory);

		Script script;
		try {
			script = Script.forDirectory(explodedScriptDirectory);
		} catch (IOException e) {
			throw new IOException("invalid script structure", e);
		}

		validateScript(script, explodedScriptDirectory);
		// I don't delete the script after attempting to explode it and it fails,
		// because I want to give the uploader an opportunity to look at it to debug what happened.

		scriptRepository.addScript(script);
	}

	private void validateScript(Script script, File explodedScriptDirectory) throws IOException {
		Manifest manifest = script.getManifest();
		String name = manifest.getName();
		String version = manifest.getVersion();

		String givenFileName = explodedScriptDirectory.getName();
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
