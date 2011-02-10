package com.orange.maven2bundle.example.defaultartifact;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

public class DirectoryLimiter {

	private long maximumSize;

	public DirectoryLimiter(long maximumSize){
		this.maximumSize = maximumSize;
	}
	
	public void deleteIfTooBig(File directory) throws IOException{
		long size = FileUtils.sizeOfDirectory(directory);
		if (size > maximumSize){
			Files.deleteRecursively(directory);
		}
	}
	
}
