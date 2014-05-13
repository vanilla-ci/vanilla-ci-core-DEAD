package com.vanillaci.core.service;

import com.vanillaci.scriptbundles.*;
import org.apache.commons.lang.exception.*;
import org.jetbrains.annotations.*;
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
public class ScriptRepository {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	private volatile Map<String, Script> scriptMap;
	private final Object $scriptMap = new Object();

	public void addScript(@NotNull Script script) {
		Map<String, Script> scriptMap = getScriptMap();
		scriptMap.put(script.getManifest().getName() + "_" + script.getManifest().getVersion(), script);
	}

	public Script get(String name, String version) {
		Map<String, Script> scriptMap = getScriptMap();
		return scriptMap.get(name + "_" + version);
	}

	public void remove(String fullName) {
		Map<String, Script> scriptMap = getScriptMap();
		scriptMap.remove(fullName);
	}

	public boolean contains(String fullName) {
		return getScriptMap().containsKey(fullName);
	}

	@NotNull
	private Map<String, Script> getScriptMap() {
		if(scriptMap == null) {
			synchronized ($scriptMap) {
				if(scriptMap == null) {
					logger.info("Initializing Script Repository");
					scriptMap = new ConcurrentHashMap<>();

					File scriptDirectory = applicationConfiguration.getScriptDirectory();
					File[] files = scriptDirectory.listFiles();
					if(files != null) {
						for (File file : files) {
							if(file.isDirectory()) {
								try {
									Script script = Script.forDirectory(file);
									scriptMap.put(script.getManifest().getName() + "_" + script.getManifest().getVersion(), script);
								} catch (IOException e) {
									logger.severe("invalid script for " +file.getAbsolutePath() + "\n" + ExceptionUtils.getFullStackTrace(e));
								}
							}
						}
					}
				}
			}
		}

		return scriptMap;
	}
}
