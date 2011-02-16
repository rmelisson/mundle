package com.orange.maven2bundle.installer.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.codehaus.plexus.interpolation.os.Os;
import org.osgi.framework.BundleException;

import com.google.common.io.Files;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;

public class Deployer {

	private OSGiFacilities oSGiFacilities;

	public Deployer(OSGiFacilities oSGiFacilities) {
		this.oSGiFacilities = oSGiFacilities;
	}
	
	public void installNode(DependencyNode parentNode) throws BundleException, IOException{
		for (DependencyNode node : parentNode.getDependencies()){
			installNode(node);
		}
		File f = createMumbleJar(parentNode.getManifest()); 
		oSGiFacilities.deployMundle(f);
	}
	
	public File createMumbleJar(MundleOSGiManifest manifest) throws IOException{
		String dirPath = ( System.getProperty("java.io.tmpdir")) + 
			File.separator + manifest.getSymbolicName() + File.separator;
		String filePath = dirPath + 
			File.separator + "META-INF" + 
			File.separator + "MANIFEST.MF";
		String jarPath = dirPath + manifest.getSymbolicName() + ".jar";
		
		File f = new File(filePath);
		Files.createParentDirs(f);
		FileOutputStream fOS = new FileOutputStream(f);
		manifest.getManifest().write(fOS);
		fOS.close();
		
		File jar = createJar(jarPath, f);
		
		return jar;
	}
	
	public File createJar(String jarPath, File manifest) throws IOException {
		
		File jarFile = new File(jarPath);
		
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarFile));
		out.setMethod(ZipOutputStream.DEFLATED);
		
		ZipEntry ze = new ZipEntry("META-INF" + File.separator + "MANIFEST.MF");
		out.putNextEntry(ze);
		byte[] buffer = new byte[8 * 1024];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				manifest), buffer.length);

		try {
			int count = 0;
			while ((count = in.read(buffer, 0, buffer.length)) >= 0) {
				if (count != 0) {
					out.write(buffer, 0, count);
				}
			}
		} finally {
			in.close();
		}
		
		out.close();
		return jarFile;
	}


}
