package com.qc.audit.project;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ReorgJimple {

	private static final String JIMPLE_EXTENSION = ".jimple";
	
	public static void organize(String projectName) throws IllegalArgumentException {
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if(!project.exists()){
			throw new IllegalArgumentException("The project " + projectName + " does not exist.");
		}
		
		File srcFolder = project.getFolder("src").getLocation().toFile();
		for(File jimpleFile : srcFolder.listFiles(new JimpleFileFilter())){
			String packageName = jimpleFile.getName();
			packageName = packageName.substring(0, packageName.length() - JIMPLE_EXTENSION.length()); // trim off the jimple extension
			packageName = packageName.substring(0, packageName.lastIndexOf('.')); // trim off the class name
			File packageDirectory = new File(srcFolder.getAbsolutePath() + File.separatorChar 
					+ packageName.replace('.', File.separatorChar) + File.separatorChar);
			if(!packageDirectory.exists()){ 
				packageDirectory.mkdirs(); 
			}
			jimpleFile.renameTo(new File(packageDirectory.getAbsolutePath() + File.separatorChar + jimpleFile.getName()));
		}
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (Exception e){
			// just eat the exception, not being able to refresh isn't that big of a deal
		}
	}
	
	private static class JimpleFileFilter implements FileFilter {
		@Override
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(JIMPLE_EXTENSION);
		}
	}

}
