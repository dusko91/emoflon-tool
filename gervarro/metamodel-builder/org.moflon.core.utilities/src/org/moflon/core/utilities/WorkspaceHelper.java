package org.moflon.core.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

/**
 * A collection of useful helper methods when dealing with a workspace in an eclipse plugin.
 */
@SuppressWarnings("restriction")
public class WorkspaceHelper
{

   private static final Logger logger = Logger.getLogger(WorkspaceHelper.class);

   public final static String PATH_SEPARATOR = "/";

   public final static String MODEL_FOLDER = "model";

   public final static String DEBUG_FOLDER = "debug";

   public static final String LIB_FOLDER = "lib";

   public static final String GEN_FOLDER = "gen";

   public final static String TEMP_FOLDER = ".temp";

   public static final String MOCA_XMI_FILE_EXTENSION = ".moca.xmi";

   public static final String ECORE_FILE_EXTENSION = ".ecore";

   public static final String PRE_ECORE_FILE_EXTENSION = ".pre.ecore";

   public static final String TGG_FILE_EXTENSION = ".tgg.xmi";

   public static final String PRE_TGG_FILE_EXTENSION = ".pre.tgg.xmi";

   public static final String INJECTION_FOLDER = "injection";

   public static final String INJECTION_FILE_EXTENSION = "inject";

   public static final String JAVA_FILE_EXTENSION = "java";

   public static final String INSTANCES_FOLDER = "instances";

   public static final String GEN_MODEL_EXT = ".genmodel";

   public static final String REPOSITORY_NATURE_ID = "org.moflon.ide.core.runtime.natures.RepositoryNature";

   public static final String INTEGRATION_NATURE_ID = "org.moflon.ide.core.runtime.natures.IntegrationNature";

   public static final String METAMODEL_NATURE_ID = "org.moflon.ide.core.runtime.natures.MetamodelNature";

   public static final String PLUGIN_NATURE_ID = "org.eclipse.pde.PluginNature"; // PDE.NATURE_ID

   public static final String ANTLR_3 = "/lib/antlr-3.5.2-complete.jar";

   public static final String PLUGIN_ID_ECORE = "org.eclipse.emf.ecore";

   public static final String PLUGIN_ID_EMF_COMMON = "org.eclipse.emf.common";

   public static final String PLUGIN_ID_ECORE_XMI = "org.eclipse.emf.ecore.xmi";

   public static final String PLUGIN_ID_ECLIPSE_RUNTIME = "org.eclipse.core.runtime";

   public static final String PLUGIN_ID_LOG4J = "org.apache.log4j";

   public static final String DEFAULT_LOG4J_DEPENDENCY = PLUGIN_ID_LOG4J + ";bundle-version=\"1.2.15\"";

   public static final String PLUGIN_ID_APACHE_COMMONS_LANG3 = "org.apache.commons.lang3;bundle-version=\"3.1.0\"";

   public static final String PLUGIN_ID_APACHE_COMMONS_IO = "org.apache.commons.io;bundle-version=\"[2.2.0,3)\"";

   public static final String ISSUE_TRACKER_URL = "https://github.com/eMoflon/emoflon-issue-tracking-system/issues";

   public final static String MOFLON_PROBLEM_MARKER_ID = "org.moflon.ide.marker.MOFLONProblem";

   public static final String INJECTION_PROBLEM_MARKER_ID = "org.moflon.ide.marker.InjectionProblem";

   /**
    * Checks if given name is a valid name for a new project in the current workspace.
    * 
    * @param projectName
    *           Name of project to be created in current workspace
    * @param pluginId
    *           ID of bundle
    * @return A status object indicating success or failure and a relevant message.
    */
   public static IStatus validateProjectName(final String projectName, final String pluginId)
   {
      // Check if anything was entered at all
      if (projectName.length() == 0)
         return new Status(IStatus.ERROR, pluginId, "Name must be specified");

      // Check if name is a valid path for current platform
      IStatus validity = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
      if (!validity.isOK())
         return new Status(IStatus.ERROR, pluginId, validity.getMessage());

      // Check if no other project with the same name already exists in workspace
      IProject[] workspaceProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
      for (IProject project : workspaceProjects)
      {
         if (project.getName().equals(projectName))
         {
            return new Status(IStatus.ERROR, pluginId, "A project with this name exists already.");
         }
      }

      // Everything was fine
      return new Status(IStatus.OK, pluginId, "Project name is valid");
   }

   /**
    * Creates a new project in current workspace
    * 
    * @param projectName
    *           name of the new project
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @param location
    *           the file system location where the project should be placed
    * @return handle to newly created project
    * @throws CoreException
    */
   public static IProject createProject(final String projectName, final String pluginId, final IPath location, final IProgressMonitor monitor)
         throws CoreException
   {
      try
      {
         monitor.beginTask("", 2);

         // Get project handle
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         IProject newProject = root.getProject(projectName);

         // Use default location (in workspace)
         final IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(newProject.getName());
         description.setLocation(location);

         // Complain if project already exists
         if (newProject.exists())
         {
            throw new CoreException(new Status(IStatus.ERROR, pluginId, projectName + " exists already!"));
         }

         // Create project
         newProject.create(description, createSubmonitorWith1Tick(monitor));
         newProject.open(createSubmonitorWith1Tick(monitor));

         return newProject;
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Creates a new project in current workspace
    * 
    * @param projectName
    *           name of the new project
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @return handle to newly created project
    * @throws CoreException
    */
   public static IProject createProject(final String projectName, final String pluginId, final IProgressMonitor monitor) throws CoreException
   {
      return createProject(projectName, pluginId, null, monitor);
   }

   /**
    * Adds a new folder with name 'folderName' to project
    * 
    * @param project
    *           the project on which the folder will be added
    * @param folderName
    *           name of the new folder
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @return newly created folder
    * @throws CoreException
    */
   public static IFolder addFolder(final IProject project, final String folderName, final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("", 1);

         IFolder projFolder = project.getFolder(folderName);
         if (!projFolder.exists())
            projFolder.create(true, true, createSubmonitorWith1Tick(monitor));
         return projFolder;
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Adds a file to project root, retrieving its contents from the specified location
    * 
    * @param project
    *           the project to which the file will be added
    * @param fileName
    *           name of the new file relative to the project
    * @param pathToContent
    *           path to a file relative to the project, that contains the contents of the new file to be created
    * @param pluginID
    *           id of plugin that is adding the file
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @throws CoreException
    * @throws URISyntaxException
    * @throws IOException
    */
   public static void addFile(final IProject project, final String fileName, final URL pathToContent, final String pluginID, final IProgressMonitor monitor)
         throws CoreException, URISyntaxException, IOException
   {
      try
      {
         monitor.beginTask("", 1);

         IFile projectFile = project.getFile(fileName);
         InputStream contents = pathToContent.openStream();
         projectFile.create(contents, true, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   public static void clearFolder(final IProject project, final String folder, final IProgressMonitor monitor)
         throws CoreException, URISyntaxException, IOException
   {
      try
      {
         IFolder folderInProject = project.getFolder(folder);
         monitor.beginTask("", folderInProject.members().length);

         for (IResource member : folderInProject.members())
            member.delete(true, createSubmonitorWith1Tick(monitor));

      } finally
      {
         monitor.done();
      }
   }

   /**
    * Adds a file to project root, containing specified contents as a string
    * 
    * @param project
    *           Name of project the file should be added to
    * @param fileName
    *           Name of file to add to project
    * @param contents
    *           What the file should contain as a String
    * @param monitor
    *           Monitor to indicate progress
    * @throws CoreException
    */
   public static void addFile(final IProject project, final String fileName, final String contents, final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("", 1);
         IFile projectFile = project.getFile(fileName);
         ByteArrayInputStream source = new ByteArrayInputStream(contents.getBytes());
         projectFile.create(source, true, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Sets the folderNames as source-folders in java build path of java project javaProject
    * 
    * @param javaProject
    *           java project whose build path will be modified
    * @param folderNames
    *           source folder names to add to build path of project javaProject
    * @param extraAttributes
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @throws JavaModelException
    */
   public static void setAsSourceFolderInBuildpath(final IJavaProject javaProject, final IFolder[] folderNames, final IClasspathAttribute[] extraAttributes,
         final IProgressMonitor monitor) throws JavaModelException
   {
      try
      {
         monitor.beginTask("", 2);

         Collection<IClasspathEntry> entries = getClasspathEntries(javaProject);

         // Add new entries for the classpath
         if (folderNames != null)
         {
            for (IFolder folder : folderNames)
            {
               if (folder != null)
               {
                  IClasspathEntry prjEntry = JavaCore.newSourceEntry(folder.getFullPath(), null, null, null, extraAttributes);
                  entries.add(prjEntry);
               }
            }
         }
         monitor.worked(1);

         setBuildPath(javaProject, entries, monitor);
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Returns the set of classpath entries of the given Java project
    */
   private static Collection<IClasspathEntry> getClasspathEntries(final IJavaProject javaProject) throws JavaModelException
   {
      return new HashSet<>(Arrays.asList(javaProject.getRawClasspath()));
   }

   /**
    * Returns the description of the given project with the given nature ID added to the project's list of natures
    */
   public static IProjectDescription getDescriptionWithAddedNature(final IProject project, final String natureId, final IProgressMonitor monitor)
         throws CoreException
   {
      try
      {
         monitor.beginTask("Create description with added natures", 1);

         IProjectDescription description = project.getDescription();

         List<String> natures = new ArrayList<>(Arrays.asList(description.getNatureIds()));

         if (!natures.contains(natureId))
         {
            natures.add(natureId);
            description.setNatureIds(natures.toArray(new String[natures.size()]));
         }

         monitor.worked(1);

         return description;
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Adds natureId to project
    * 
    * @param project
    *           Handle to existing project
    * @param natureId
    *           ID of nature to be added
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @throws CoreException
    *            if unable to add nature
    */
   public static void addNature(final IProject project, final String natureId, final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("Add nature to project", 2);

         IProjectDescription description = getDescriptionWithAddedNature(project, natureId, createSubmonitorWith1Tick(monitor));
         project.setDescription(description, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Set up the project to a consistent java project
    * 
    * @param project
    *           project to set up as java project
    * @param monitor
    *           a progress monitor, or null if progress reporting is not desired
    * @return
    */
   public static IJavaProject setUpAsJavaProject(final IProject project, final IProgressMonitor monitor)
   {
      try
      {
         monitor.beginTask("Set up Java project", 1);

         final JavaCapabilityConfigurationPage jcpage = new JavaCapabilityConfigurationPage();
         final IJavaProject javaProject = JavaCore.create(project);

         PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            @Override
            public void run()
            {
               jcpage.init(javaProject, null, null, true);
               try
               {
                  jcpage.configureJavaProject(createSubmonitorWith1Tick(monitor));
               } catch (final Exception e)
               {
                  logger.error("Exception during setup of Java project", e);
               }
            }
         });

         return javaProject;
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Reversed list of {@link #getProjectsOnBuildPath(IProject)}
    */
   public static List<IProject> getProjectsOnBuildPathInReversedOrder(final IProject project)
   {
      List<IProject> result = getProjectsOnBuildPath(project);
      Collections.reverse(result);
      return result;
   }

   /**
    * Returns a list of all classpath entries of type {@link IClasspathEntry#CPE_PROJECT} of the given project.
    */
   public static List<IProject> getProjectsOnBuildPath(final IProject project)
   {
      // Fetch or create java project view of the given project
      IJavaProject javaProject = JavaCore.create(project);

      // Get current entries on the classpath
      ArrayList<IProject> projectsOnBuildPath = new ArrayList<>();
      try
      {
         for (IClasspathEntry entry : javaProject.getRawClasspath())
         {
            if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT)
            {
               projectsOnBuildPath.add(ResourcesPlugin.getWorkspace().getRoot().getProject(entry.getPath().lastSegment()));
            }
         }
      } catch (JavaModelException e)
      {
         LogUtils.error(logger, e, "Unable to determine projects on buildpath for: " + project.getName());
      }

      return projectsOnBuildPath;
   }

   /**
    * Adds the given container to the list of build path entries (if not included, yet)
    */
   private static void addContainerToBuildPath(final Collection<IClasspathEntry> classpathEntries, final String container)
   {
      IClasspathEntry entry = JavaCore.newContainerEntry(new Path(container));
      for (IClasspathEntry iClasspathEntry : classpathEntries)
      {
         if (iClasspathEntry.getPath().equals(entry.getPath()))
         {
            // No need to add variable - already on classpath
            return;
         }
      }

      classpathEntries.add(entry);
   }

   /**
    * Adds the given container to the build path of the given project if it contains no entry with the same name, yet.
    */
   public static void addContainerToBuildPath(final IProject project, final String container)
   {
      addContainerToBuildPath(JavaCore.create(project), container);
   }

   /**
    * Adds the given container to the build path of the given java project.
    */
   private static void addContainerToBuildPath(final IJavaProject iJavaProject, final String container)
   {
      try
      {
         // Get current entries on the classpath
         Collection<IClasspathEntry> classpathEntries = new ArrayList<>(Arrays.asList(iJavaProject.getRawClasspath()));

         addContainerToBuildPath(classpathEntries, container);

         setBuildPath(iJavaProject, classpathEntries);
      } catch (JavaModelException e)
      {
         LogUtils.error(logger, e, "Unable to set classpath variable");
      }
   }

   private static void setBuildPath(final IJavaProject javaProject, final Collection<IClasspathEntry> entries, final IProgressMonitor monitor)
         throws JavaModelException
   {
      try
      {
         monitor.beginTask("Set build path", 1);
         // Create new buildpath
         IClasspathEntry[] newEntries = new IClasspathEntry[entries.size()];
         entries.toArray(newEntries);

         // Set new classpath with added entries
         javaProject.setRawClasspath(newEntries, monitor != null ? createSubmonitorWith1Tick(monitor) : null);
      } finally
      {
         monitor.done();
      }
   }

   private static void setBuildPath(final IJavaProject javaProject, final Collection<IClasspathEntry> entries) throws JavaModelException
   {
      setBuildPath(javaProject, entries, new NullProgressMonitor());
   }

   /**
    * Creates a folder denoted by the path inside the given project.
    * 
    * @param project
    * @param path
    *           the path, separated with {@link WorkspaceHelper#PATH_SEPARATOR}
    * @param monitor
    * @throws CoreException
    */
   public static void addAllFolders(final IProject project, final String path, final IProgressMonitor monitor) throws CoreException
   {
      String[] folders = path.split(PATH_SEPARATOR);
      try
      {
         monitor.beginTask("Add folders", folders.length);
         StringBuilder currentFolder = new StringBuilder();
         for (String folder : folders)
         {
            currentFolder.append(PATH_SEPARATOR).append(folder);
            addFolder(project, currentFolder.toString(), createSubmonitorWith1Tick(monitor));
         }
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Creates the given file and stores the given contents in it.
    * 
    * @param file
    * @param contents
    * @param monitor
    *           the monitor that reports on the progress
    * @throws CoreException
    */
   private static void addFile(final IFile file, final String contents, final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("Add file", 1);
         ByteArrayInputStream source = new ByteArrayInputStream(contents.getBytes());
         file.create(source, true, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Creates a file at pathToFile with specified contents fileContent. All folders in the path are created if
    * necessary.
    * 
    * @param project
    *           Project containing file to be created
    * @param pathToFile
    *           Project relative path to file to be created
    * @param fileContent
    *           String content of file to be created
    * @param monitor
    * @throws CoreException
    */
   public static void addAllFoldersAndFile(final IProject project, final IPath pathToFile, final String fileContent, final IProgressMonitor monitor)
         throws CoreException
   {
      try
      {
         monitor.beginTask("Add file", 2);
         // Remove file segment
         IPath folders = pathToFile.removeLastSegments(1);

         // Create all necessary folders
         addAllFolders(project, folders.toString(), createSubmonitorWith1Tick(monitor));

         // Create file
         addFile(project.getFile(pathToFile), fileContent, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   /**
    * @deprecated See {@link #createSubMonitor(IProgressMonitor, int)}
    */
   public static IProgressMonitor createSubmonitorWith1Tick(final IProgressMonitor monitor)
   {
      return createSubMonitor(monitor, 1);
   }

   /**
    * Creates a sub-monitor of the given monitor with the given number of ticks.
    * 
    * @deprecated The API suggests to to switch to {@link SubMonitor}. Instructions are given {@link SubMonitor}.
    */
   @Deprecated
   public static IProgressMonitor createSubMonitor(final IProgressMonitor monitor, final int ticks)
   {
      return new SubProgressMonitor(monitor, ticks);
   }

   /**
    * Checks whether the given project has the {@link #PLUGIN_NATURE_ID}. If the check throws an exception,
    * <code>false</code> is returned gracefully.
    */
   public static boolean isPluginProjectNoThrow(final IProject project)
   {
      try
      {
         return project.hasNature(PLUGIN_NATURE_ID);
      } catch (Exception e)
      {
         return false;
      }
   }

   /**
    * Returns whether the given project is (1) a repository project or (2) an integration project
    */
   public static boolean isMoflonProject(final IProject project) throws CoreException
   {
      return isRepositoryProject(project) || isIntegrationProject(project);
   }

   /**
    * Returns whether the given project is (1) a repository project or (2) an integration project.
    * 
    * Returns also false if an exception would be thrown.
    */
   public static boolean isMoflonProjectNoThrow(final IProject project)
   {
      return isRepositoryProjectNoThrow(project) || isIntegrationProjectNoThrow(project);
   }

   /**
    * Returns whether the given project is (1) a repository project, (2) an integration project, or (3) a metamodel
    * project
    */
   public static boolean isMoflonOrMetamodelProject(final IProject project) throws CoreException
   {
      return isMoflonProject(project) || isMetamodelProject(project);
   }

   /**
    * Returns whether the project is a an integration project, that is, if it contains generated code of a TGG project.
    * 
    * @param project
    *           the project. May be null.
    */
   public static boolean isIntegrationProject(final IProject project) throws CoreException
   {
      return project.hasNature(INTEGRATION_NATURE_ID);
   }

   /**
    * A wrapper around {@link #isIntegrationProject(IProject)}, which returns false if the original method throws an
    * exception
    */
   public static boolean isIntegrationProjectNoThrow(IProject project)
   {
      try
      {
         return isIntegrationProject(project);
      } catch (Exception e)
      {
         return false;
      }
   }

   /**
    * Returns whether the project is a a repository project.
    * 
    * @param project
    *           the project. May be null.
    */
   public static boolean isRepositoryProject(final IProject project) throws CoreException
   {
      return project != null && project.hasNature(REPOSITORY_NATURE_ID);
   }

   /**
    * Returns whether the project is a a repository project, if an exception is thrown, the method return false.
    * 
    * @param project
    *           the project. May be null.
    */
   public static boolean isRepositoryProjectNoThrow(IProject project)
   {
      try
      {
         return isRepositoryProject(project);
      } catch (Exception e)
      {
         return false;
      }
   }

   /**
    * Returns whether the project is a a meta-model project, that is, if it contains a meta-model
    * 
    * @param project
    *           the project. May be null.
    */
   public static boolean isMetamodelProject(final IProject project) throws CoreException
   {
      return project != null && project.hasNature(METAMODEL_NATURE_ID);
   }

   /**
    * Same as {@link #isMetamodelProject(IProject)} but catches {@link Exception}s, returning false.
    * 
    * @param project
    * @return
    */
   public static boolean isMetamodelProjectNoThrow(final IProject project)
   {
      try
      {
         return isMetamodelProject(project);
      } catch (Exception e)
      {
         return false;
      }
   }

   public static boolean isInjectionFile(final IResource resource)
   {
      return resource != null && isFile(resource) && resource.getName().endsWith(INJECTION_FILE_EXTENSION);
   }

   public static boolean isJavaFile(final IResource resource)
   {
      return resource != null && isFile(resource) && resource.getName().endsWith(".java");
   }

   /**
    * Returns the project in the workspace with the given project name.
    * 
    * The returned project has to be checked for existence
    * 
    * @deprecated Use {@link #getProjectByName(String)}
    */
   // TODO Remove on next release
   @Deprecated
   public static IProject getProjectRoot(final String projectName)
   {
      return getProjectByName(projectName);
   }

   public static IProject getProjectByName(final String projectName)
   {
      return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
   }

   /**
    * Returns the project with the given plugin id in the workspace.
    * 
    * If no such project can be found, null is returned.
    * 
    * @param pluginId
    *           the plugin id
    * @return the project with the plugin id or null if not such project exists
    */
   public static IProject getProjectByPluginId(final String pluginId)
   {
      return getAllProjectsInWorkspace().stream().filter(project -> {
         return doesProjectHavePluginId(project, pluginId);
      }).findAny().orElse(null);
   }

   private static boolean doesProjectHavePluginId(final IProject project, final String desiredPluginId)
   {
      IPluginModelBase pluginModel = PluginRegistry.findModel(project);
      if (pluginModel != null && pluginModel.getBundleDescription() != null)
      {
         final String actualPluginId = pluginModel.getBundleDescription().getSymbolicName();
         return desiredPluginId.equals(actualPluginId);
      } else
      {
         return false;
      }
   }

   /**
    * Returns the list of all projects in the workspace
    */
   public static List<IProject> getAllProjectsInWorkspace()
   {
      return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects());
   }

   /**
    * Returns whether the given file has a name that ends with 'pdf'
    */
   public static boolean isPdfFile(final IResource resource)
   {
      return isFile(resource) && resource.getName().endsWith(".pdf");
   }

   /**
    * Returns whether the given resource is of type {@link IResource#FILE}
    */
   public static boolean isFile(final IResource resource)
   {
      return resource != null && resource.getType() == IResource.FILE;
   }

   /**
    * Returns whether the given resource is of type {@link IResource#FOLDER}
    */
   public static boolean isFolder(final IResource resource)
   {
      return resource.getType() == IResource.FOLDER;
   }

   /**
    * Returns the file name of the injection file for a given Java file.
    * 
    * This method assumes that the first segment in the path is the source folder (e.g.,"/src"). The injection file name
    * is obtained by replacing the first segment of the input file name with {@link WorkspaceHelper#INJECTION_FOLDER}
    * and by replacing the file extension with {@link WorkspaceHelper#INJECTION_FILE_EXTENSION}.
    * 
    * The resulting path needs to be resolved against a project via {@link IProject#getFile(IPath)}.
    * 
    * @param javaFile
    *           the Java file
    * @return the path to the injection file
    */
   public static IPath getPathToInjection(final IFile javaFile)
   {
      final IPath packagePath = javaFile.getProjectRelativePath().removeFirstSegments(1);
      final IPath pathToInjection = packagePath.removeFileExtension().addFileExtension(INJECTION_FILE_EXTENSION);
      final IFolder injectionFolder = javaFile.getProject().getFolder(WorkspaceHelper.INJECTION_FOLDER);
      final IPath fullInjectionPath = injectionFolder.getProjectRelativePath().append(pathToInjection);
      return fullInjectionPath;
   }

   /**
    * Returns the file name of the Java file for a given injection file.
    * 
    * This method assumes that the first segment in the path is the injection folder (
    * {@link WorkspaceHelper#INJECTION_FOLDER}). The injection file name is obtained by replacing the first segment of
    * the input file name with {@link WorkspaceHelper#GEN_FOLDER} and by replacing the file extension with
    * {@link WorkspaceHelper#JAVA_FILE_EXTENSION}.
    * 
    * The resulting path needs to be resolved against a project via {@link IProject#getFile(IPath)}.
    * 
    * @param file
    *           the injection file
    * @return the path to the Java file
    */
   public static IPath getPathToJavaFile(final IFile file)
   {
      final IPath packagePath = file.getProjectRelativePath().removeFirstSegments(1);
      final IPath pathToJavaFile = packagePath.removeFileExtension().addFileExtension(JAVA_FILE_EXTENSION);
      final IFolder genFolder = file.getProject().getFolder(WorkspaceHelper.GEN_FOLDER);
      final IPath fullJavaPath = genFolder.getProjectRelativePath().append(pathToJavaFile);
      return fullJavaPath;
   }

   public static String getFullyQualifiedClassName(final IFile javaFile)
   {
      final IPath packagePath = javaFile.getProjectRelativePath().removeFirstSegments(1);
      final IPath pathToJavaFile = packagePath.removeFileExtension();
      final String fullyQualifiedClassName = pathToJavaFile.toPortableString().replace("/", ".");
      return fullyQualifiedClassName;
   }

   /**
    * Replaces all "." in a package path by "/".
    * 
    * @param packageName
    *           the name of the package in xyz.xyz.xyz format
    * @return the name of the package in xyz/xyz/xyz format
    */
   public static String formatPackagePath(String packageName)
   {
      String packagePath = "/" + packageName.replaceAll("\\.", "/") + "/";
      return packagePath;
   }

   public static void moveProjectToWorkingSet(final IProject project, final String workingSetName)
   {
      // Move project to appropriate working set
      IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
      IWorkingSet workingSet = workingSetManager.getWorkingSet(workingSetName);
      if (workingSet == null)
      {
         workingSet = workingSetManager.createWorkingSet(workingSetName, new IAdaptable[] { project });
         workingSet.setId(IWorkingSetIDs.JAVA);
         workingSetManager.addWorkingSet(workingSet);
      } else
      {
         // Add current contents of WorkingSet
         ArrayList<IAdaptable> newElements = new ArrayList<IAdaptable>();
         for (IAdaptable element : workingSet.getElements())
            newElements.add(element);

         // Add newly created project
         newElements.add(project);

         // Set updated contents
         IAdaptable[] newElementsArray = new IAdaptable[newElements.size()];
         workingSet.setElements(newElements.toArray(newElementsArray));
      }
   }

   public static IFile getManifestFile(final IProject project)
   {
      return project.getFolder("META-INF").getFile("MANIFEST.MF");
   }

   public static boolean allProjectsExist(final Collection<IProject> projects)
   {
      return projects.stream().allMatch(project -> project.exists());
   }

   public static Collection<IProject> getProjectsByNatureID(final String natureID) throws CoreException
   {
      return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()).stream()//
            .filter(p -> p.isOpen())//
            .filter(p -> hasNatureSafe(p, natureID))//
            .collect(Collectors.toList());
   }

   // This method ignores the checked CoreException to make it usable inside streams
   public static boolean hasNatureSafe(final IProject project, final String natureId)
   {
      try
      {
         return project.hasNature(natureId);
      } catch (CoreException e)
      {
         return false;
      }
   }

   /**
    * Checks whether the given monitor has been canceled and throws an OperationCancelledException to signal the cancellation.
    * 
    * @throws OperationCancelledException
    *            if the monitor has been cancelled
    */
   public static void checkCanceledAndThrowException(final IProgressMonitor monitor) 
   {
      if (monitor.isCanceled()) {
    	  throw new OperationCanceledException();
      }
   }

   /**
    * Copies the content of the source file into the target file.
    * 
    * @param source
    *           source file
    * @param target
    *           target file
    * 
    * @deprecated Use {@link FileUtils#copyFile(File, File)}
    */
   @Deprecated
   public static void copyFile(final File source, final File target)
   {
      try
      {
         byte[] buffer = new byte[1024];
         BufferedInputStream origin = new BufferedInputStream(new FileInputStream(source));
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
         int count;

         while ((count = origin.read(buffer)) > 0)
         {
            out.write(buffer, 0, count);
         }

         origin.close();
         out.close();
      } catch (IOException e)
      {
         LogUtils.error(logger, e);
      }
   }

   /**
    * Returns a file handle for the EAP file in the given metamodel project.
    * 
    * The file need not exist and needs to be checked using {@link IFile#exists()}.
    * 
    * @param metamodelProject
    * @return the file handle. Never null.
    */
   public static IFile getEapFileFromMetamodelProject(final IProject metamodelProject)
   {
      return metamodelProject.getFile(metamodelProject.getName().concat(".eap"));
   }

   /**
    * Returns the file handle of the MOCA tree of a metamodel project.
    * 
    * The MOCA tree may not exist and has to be checked using {@link IFile#exists()}.
    * 
    * @param metamodelProject
    * @return the file handle. Never null.
    */
   public static IFile getExportedMocaTree(final IProject metamodelProject)
   {
      Function<String, IFile> loadMocaTree = name -> metamodelProject.getFolder(TEMP_FOLDER).getFile(name + MOCA_XMI_FILE_EXTENSION);

      IFile mocaTreeFile = loadMocaTree.apply(metamodelProject.getName());

      if (!mocaTreeFile.exists())
         mocaTreeFile = loadMocaTree.apply(metamodelProject.getName().toUpperCase());

      if (!mocaTreeFile.exists())
         mocaTreeFile = loadMocaTree.apply(metamodelProject.getName().toLowerCase());

      return mocaTreeFile;
   }

   /**
    * Returns the file handle of the changes MOCA tree of a metamodel project.
    * 
    * The MOCA tree may not exist and has to be checked using {@link IFile#exists()}.
    * 
    * @param metamodelProject
    * @return the file handle. Never null.
    */
   public static IFile getChangesMocaTree(final IProject metamodelProject)
   {
      Function<String, IFile> loadMocaTree = name -> metamodelProject.getFolder(TEMP_FOLDER).getFile(name + ".changes" + MOCA_XMI_FILE_EXTENSION);

      IFile mocaTreeFile = loadMocaTree.apply(metamodelProject.getName());

      if (!mocaTreeFile.exists())
         mocaTreeFile = loadMocaTree.apply(metamodelProject.getName().toUpperCase());

      if (!mocaTreeFile.exists())
         mocaTreeFile = loadMocaTree.apply(metamodelProject.getName().toLowerCase());

      return mocaTreeFile;
   }

   /**
    * Returns a handle to the default location of a metamodel file ("ecore file") of a repository or integration
    * project.
    * 
    * @param project
    *           the project of which to extract the ecore file
    */
   public static IFile getDefaultEcoreFile(final IProject project)
   {
      String ecoreFileName = MoflonUtil.lastCapitalizedSegmentOf(project.getName());
      return project.getFolder(MODEL_FOLDER).getFile(ecoreFileName + ECORE_FILE_EXTENSION);
   }

   /**
    * Returns a handle to the gen folder of the project
    * 
    * @see WorkspaceHelper#GEN_FOLDER
    */
   public static IFolder getGenFolder(final IProject project)
   {
      return project.getFolder(GEN_FOLDER);
   }

   /**
    * Returns a handle to the model folder of the project
    * 
    * @see WorkspaceHelper#MODEL_FOLDER
    */
   public static IFolder getModelFolder(final IProject workspaceProject)
   {
      return workspaceProject.getFolder(MODEL_FOLDER);
   }

   /**
    * Returns a handle to the instances folder of the project
    * 
    * @see WorkspaceHelper#INSTANCES_FOLDER
    */
   public static IFolder getInstancesFolder(final IProject workspaceProject)
   {
      return workspaceProject.getFolder(INSTANCES_FOLDER);
   }

   /**
    * Returns a handle to the lib folder of the project
    * 
    * @see WorkspaceHelper#LIB_FOLDER
    */
   public static IFolder getLibFolder(final IProject workspaceProject)
   {
      return workspaceProject.getFolder(LIB_FOLDER);
   }

   /**
    * Returns a handle to the injection folder of the project
    * 
    * @see WorkspaceHelper#INJECTION_FOLDER
    */
   public static IFolder getInjectionFolder(final IProject project)
   {
      return project.getFolder(INJECTION_FOLDER);
   }

   /**
    * Creates the given folder if it does not exist yet.
    * 
    * @param folder
    * @param monitor
    * @throws CoreException
    */
   public static void createFolderIfNotExists(final IFolder folder, final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("Creating " + folder, 1);
         if (!folder.exists())
            folder.create(true, true, createSubmonitorWith1Tick(monitor));
      } finally
      {
         monitor.done();
      }
   }

   /**
    * Prints the stacktrace of the given {@link Throwable} to a string.
    * 
    * If t is null, then the result is the empty string.
    */
   public static String printStacktraceToString(final Throwable t)
   {
      if (null == t)
         return "";

      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      t.printStackTrace(new PrintStream(stream));
      return new String(stream.toByteArray());
   }
}
