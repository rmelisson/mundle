package com.orange.maven2bundle.installer.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.osgi.framework.BundleException;

import com.google.common.io.Files;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;

public class Deployer {

	private OSGiFacilities oSGiFacilities;

	public Deployer(OSGiFacilities oSGiFacilities) {
		this.oSGiFacilities = oSGiFacilities;
	}

	public void installNode(DependencyNode parentNode) throws BundleException,
			IOException {
		String sN = parentNode.getManifest().getSymbolicName();
		System.out.println(sN);
		for (DependencyNode node : parentNode.getDependencies()) {
			installNode(node);
		}
		File f = createMumbleJar(parentNode.getManifest());
		oSGiFacilities.deployMundle(f);
	}

	private File createMumbleJar(MundleOSGiManifest manifest)
			throws IOException {
		String dirPath = (System.getProperty("java.io.tmpdir"))
				+ File.separator + manifest.getSymbolicName() + File.separator;

		// we create the manifest file
		File manifestFile = createManifest(dirPath, manifest);

		// then we copy the maven artifact in order to embed it
		String libPath = dirPath
				+ oSGiFacilities.getInsideLibPath(manifest.getArtifactFile()
						.getName());
		File libFile = new File(libPath);
		Files.createParentDirs(libFile);
		Files.copy(manifest.getArtifactFile(), libFile);

		// and finally we create a new jar with the manifest and the artifact
		// jar
		String jarPath = (System.getProperty("java.io.tmpdir"))
				+ File.separator + manifest.getSymbolicName() + ".jar";

		File[] files = { manifestFile, libFile };
		File jarFile = new File(jarPath);
		Ziper.zip(jarFile, new File(dirPath), files, File.separatorChar);

		return jarFile;
	}

	private File createManifest(String rootPath, MundleOSGiManifest manifest)
			throws IOException {
		String filePath = rootPath + File.separator + "META-INF"
				+ File.separator + "MANIFEST.MF";

		File f = new File(filePath);
		Files.createParentDirs(f);
		FileOutputStream fOS = new FileOutputStream(f);
		manifest.getManifest().write(fOS);
		fOS.close();
		return f;
	}

	private File createJar(String jarPath, File manifest, File lib)
			throws IOException {

		File jarFile = new File(jarPath);

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarFile));
		out.setMethod(ZipOutputStream.DEFLATED);

		ZipEntry zManifest = new ZipEntry("META-INF" + File.separator
				+ "MANIFEST.MF");
		out.putNextEntry(zManifest);
		copyFileInZip(manifest, out);

		ZipEntry zLib = new ZipEntry(oSGiFacilities.getInsideLibPath(lib
				.getName()));
		out.putNextEntry(zLib);
		copyFileInZip(lib, out);
		out.close();

		return jarFile;
	}

	private void copyFileInZip(File file, ZipOutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file), buffer.length);

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
	}

	private static int BUFFER = 1024;

	public void createJar2(String jarPath, String fileName)
			throws FileNotFoundException {
		try {

			FileOutputStream dest = new FileOutputStream(jarPath);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			// out.setMethod(ZipOutputStream.DEFLATED);

			// get a list of files from current directory
			String lib = "lib" + File.separator + fileName;
			blabla(lib, out);

			String manifest = "META-INF" + File.separator + "MANIFEST.MF";
			blabla(manifest, out);

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void blabla(String file, ZipOutputStream out) throws IOException {
		byte data[] = new byte[BUFFER];
		System.out.println("Adding: " + file);
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
		ZipEntry entry = new ZipEntry(file);
		out.putNextEntry(entry);
		int count;
		while ((count = origin.read(data, 0, BUFFER)) != -1) {
			out.write(data, 0, count);
		}
		origin.close();
	}

	/*
	 * private static void zipFile( ZipOutputStream out, String stripPath, File
	 * file, char pathSeparator) throws IOException { ZipEntry ze = new
	 * ZipEntry(processPath(file.getPath(), stripPath, pathSeparator));
	 * ze.setTime(file.lastModified()); out.putNextEntry(ze);
	 * 
	 * byte[] buffer = new byte[8 * 1024]; BufferedInputStream in = new
	 * BufferedInputStream(new FileInputStream(file), buffer.length);
	 * 
	 * try { int count = 0; while ((count = in.read(buffer, 0, buffer.length))
	 * >= 0) { if (count != 0) { out.write(buffer, 0, count); } } } finally {
	 * in.close(); } }
	 */

}
