package com.qc.audit.project;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.ensoftcorp.atlas.core.log.Log;

public class JODE {
	
	/**
	 * Launches the JODE decompiler swing window with the proper classpath
	 * @param projectName
	 * @throws Exception
	 */
	public static void launch(String projectName) throws Exception {
		IJavaProject project = getProject(projectName);
		File classesJar = project.getProject().getFile("classes.jar").getLocation().toFile();
		jode.swingui.Main gui = new jode.swingui.Main((classesJar.getAbsolutePath() + "," + getClasspath(project)));
		gui.show();
	}
	
	/**
	 * Runs the JODE decompiler over the classes in the classes.jar file in the project
	 * @param projectName
	 * @throws Exception
	 */
	public static void run(String projectName) throws Exception {
		IJavaProject project = getProject(projectName);
		File classesJar = project.getProject().getFile("classes.jar").getLocation().toFile();
		String javaOutputDir = project.getProject().getLocation().toFile().getAbsolutePath() + File.separator + projectName + File.separator + "java";
		String[] decompileArgs = new String[]{"--dest", javaOutputDir, (classesJar.getAbsolutePath() + "," + getClasspath(project))};
		try {
			jode.decompiler.Main.decompile(decompileArgs);
		} catch (ExceptionInInitializerError ex) {
		    Log.error("Failed to decompile classes to java", ex);
		} catch (Throwable ex) {
			Log.error("Failed to decompile classes to java", ex);
		}
	}

	/**
	 * Returns the Eclipse workspace Java project matching the given name
	 * @param projectName
	 * @return
	 */
	private static IJavaProject getProject(String projectName) {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName));
	}

	/**
	 * Returns a comma separate classpath string for the Eclipse workspace Java project
	 * @param project
	 * @return
	 * @throws IOException
	 * @throws JavaModelException
	 */
	private static String getClasspath(IJavaProject project) throws IOException, JavaModelException {
		String classpath = "";
		String prefix = "";
		final IClasspathEntry[] resolvedClasspath = project.getResolvedClasspath(true);
		for (IClasspathEntry classpathEntry : resolvedClasspath) {
			classpath += prefix + classpathEntry.getPath().makeAbsolute().toFile().getCanonicalPath();
			prefix = ",";
		}
		return classpath;
	}
	
}
