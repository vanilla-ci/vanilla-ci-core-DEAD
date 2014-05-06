package com.vanillaci.core.exceptions;

import java.io.*;

/**
 * @author Joel Johnson
 */
public class PluginAlreadyExistsException extends IOException {
	public PluginAlreadyExistsException(String name) {
		super("Plugin already exists with name " + name + ". You must delete it before re-uploading or include the 'overwrite=true' parameter.");
	}
}
