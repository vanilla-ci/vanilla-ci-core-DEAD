package com.vanillaci.core.exceptions;

import java.io.*;

/**
 * @author Joel Johnson
 */
public class ScriptAlreadyExistsException extends IOException {
	public ScriptAlreadyExistsException(String name) {
		super("Script already exists with name " + name + ". You must delete it before re-uploading or include the 'overwrite=true' parameter.");
	}
}
