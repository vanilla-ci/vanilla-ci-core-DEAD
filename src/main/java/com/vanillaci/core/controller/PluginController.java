package com.vanillaci.core.controller;

import com.vanillaci.core.exceptions.*;
import com.vanillaci.core.json.*;
import com.vanillaci.core.service.*;
import com.vanillaci.scriptbundles.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.*;

import javax.jms.*;
import java.io.*;

/**
 * @author Joel Johnson
 */
@Controller
public class PluginController {
	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	//@Autowired
	//private JmsTemplate producerTemplate;

	@RequestMapping(value="/plugin/upload", method=RequestMethod.POST)
	public View viewTest(
		@RequestParam(value="file", required = false) MultipartFile file,
		@RequestParam(value="overwrite", required = false, defaultValue = "false") boolean overwrite
	) throws JMSException, IOException, ZipException {
		String name = file.getOriginalFilename();
		String fileName;
		if(name.toLowerCase().endsWith(".zip")) {
			fileName = name;
			name = name.substring(0, name.length() - ".zip".length());
		} else {
			fileName = name + ".zip";
		}

		File zippedPlugins = applicationConfiguration.getArchivesDirectory();

		File newPlugin = new File(zippedPlugins, fileName);
		if(!overwrite && newPlugin.exists()) {
			throw new PluginAlreadyExistsException(name);
		}

		file.transferTo(newPlugin);

		File pluginDirectory = applicationConfiguration.getPluginDirectory();

		File explodedPlugin = new File(pluginDirectory, name);
		if(!overwrite && explodedPlugin.exists()) {
			throw new PluginAlreadyExistsException(name);
		}

		explodePlugin(newPlugin, explodedPlugin);
		validatePlugin(explodedPlugin);

		broadcastNewPlugin(newPlugin);

		return JsonView.success();
	}

	private void validatePlugin(File pluginDirectory) throws IOException {
		Script script;
		try {
			script = Script.forDirectory(pluginDirectory);
		} catch (IOException e) {
			throw new IOException("invalid plugin structure", e);
		}

		Manifest manifest = script.getManifest();
		String name = manifest.getName();
		String version = manifest.getVersion();

		String givenFileName = pluginDirectory.getName();
		String expectedFileName = name + "_" + version;

		if(!givenFileName.equals(expectedFileName)) {
			throw new IOException("Expected the plugin to have a file name that matches the manifest file.");
		}
	}

	private void explodePlugin(File newPlugin, File explodedPlugin) throws IOException, ZipException {
		if(!explodedPlugin.mkdirs()) {
			throw new IOException("Unable to create plugin directory: " + explodedPlugin.getAbsolutePath());
		}

		ZipFile zipFile = new ZipFile(newPlugin);
		zipFile.extractAll(explodedPlugin.getAbsolutePath());
	}

	private void broadcastNewPlugin(File zippedPlugin) {

	}
}
