package com.vanillaci.core.controller;

import com.vanillaci.core.exceptions.*;
import com.vanillaci.core.service.*;
import com.vanillaci.core.spring.views.*;
import com.vanillaci.scriptbundles.*;
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
public class ScriptController {
	@Autowired
	private ScriptService scriptService;

	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	@RequestMapping("/script/download")
	public View downloadScript(
		@RequestParam(value="name") String name,
		@RequestParam(value="version") String version
	) throws IOException, ZipException {
		Script script = scriptService.getScript(name, version);
		if(script == null) {
			throw new FileNotFoundException(name + " " + version);
		}

		File compressedScript = scriptService.getCompressedScript(script);
		FileInputStream fileInputStream = new FileInputStream(compressedScript);
		return new StreamView("application/zip", fileInputStream);
	}

	@RequestMapping(value="/script/upload", method=RequestMethod.POST)
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

		File zippedScripts = applicationConfiguration.getArchivesDirectory();

		File newScript = new File(zippedScripts, fileName);
		if(!overwrite && newScript.exists()) {
			throw new ScriptAlreadyExistsException(name);
		}

		file.transferTo(newScript);

		scriptService.installScriptFromZip(name, newScript, overwrite);
		return JsonView.success();
	}
}
