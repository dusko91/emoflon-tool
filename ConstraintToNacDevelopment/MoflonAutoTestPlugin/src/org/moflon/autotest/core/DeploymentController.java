package org.moflon.autotest.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.exports.SiteBuildOperation;
import org.eclipse.pde.internal.core.ifeature.IFeatureModel;
import org.eclipse.pde.internal.core.isite.ISiteFeature;
import org.eclipse.pde.internal.core.isite.ISiteModel;
import org.eclipse.pde.internal.core.site.WorkspaceSiteModel;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.moflon.Activator;
import org.moflon.ide.core.console.MoflonConsole;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class DeploymentController
{
   private static final Logger logger = Logger.getLogger(DeploymentController.class);
   
   private static final String INFO_MESSAGE_DEPLOYMENT_SUCCESSFUL = "Successfully deployed eMoflon ...";

   private static final String ERROR_MESSAGE_RESTRICTED_ACCESS = "Are you sure you have access to all required folders (beta and release destinations have restricted access!)?";

   private static final String ERROR_MESSAGE_UNABLE_TO_DEPLOY = "I was unable to deploy eMoflon ...";

   private static final String CONFIRMATION_CANNOT_BE_REVERTED = "Are you sure you want to deploy eMoflon?  This cannot be reverted!";

   private static final String CONFIRMATION_REALLY_DEPLOY = "Really Deploy eMoflon";

   private static final String INFO_MESSAGE_DEPLOYMOFLON = "Trying to deploy eMoflon as requested to our update site on our server ...";

   private static final String ERROR_MESSAGE_ABORT = "Ok - then I'll abort.";

   private static final String INFO_MESSAGE_SELECT_LOCAL_DEST = "Select the local deployment destination";

   private static final String INFO_MESSAGE_DEPLOYMOFLON_LOCAL = "Trying to deploy eMoflon as requested to a local destination ...";

   public static final String betaPath = "//fg/ES/mitarbeiter/eclipse-plugin/beta";

   public static final String releasePath = "//fg/ES/mitarbeiter/eclipse-plugin";

   private IWorkbenchWindow window;

   public DeploymentController(IWorkbenchWindow window)
   {
      this.window = window;
   }

   public void deployLocal()
   {
      logger.info( INFO_MESSAGE_DEPLOYMOFLON_LOCAL);

      Display.getDefault().asyncExec(new Runnable() {
         @Override
         public void run()
         {
            DirectoryDialog dirDialog = new DirectoryDialog(window.getShell());
            dirDialog.setText(INFO_MESSAGE_SELECT_LOCAL_DEST);
            String selectedDir = dirDialog.open();

            if (selectedDir != null)
               startDeployment(selectedDir);
            else
               logger.error(ERROR_MESSAGE_ABORT);
         }
      });
   }

   public void deployBeta()
   {
      logger.info( INFO_MESSAGE_DEPLOYMOFLON);

      Display.getDefault().asyncExec(new Runnable() {
         @Override
         public void run()
         {
            if (MessageDialog.openConfirm(window.getShell(), CONFIRMATION_REALLY_DEPLOY + " (Beta)?", CONFIRMATION_CANNOT_BE_REVERTED))
               startDeployment(betaPath);
         }
      });
   }

   public void deployRelease()
   {
      logger.info( INFO_MESSAGE_DEPLOYMOFLON);

      if (MessageDialog.openConfirm(window.getShell(), CONFIRMATION_REALLY_DEPLOY + " (Release)?", CONFIRMATION_CANNOT_BE_REVERTED))
         startDeployment(releasePath);
   }

   public static void startDeployment(final String path)
   {
      // Set up Fujaba Workspace
      Job job = new Job("eMoflon: Deploying ...") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            /* (I) Deploy EAEcoreAddin */
            try
            {
               // 1. Replace /MoflonAutoTestPlugin/commandLineExeAndJunitTest/EAEcoreAddin.dll with
               // /MOFLON2EAEcoreAddin/EAEcoreAddin/bin/Debug/EAEcoreAddin.dll
               replaceDLL();

               // 2. Build ea-ecore-addin.zip containing
               // /MOFLON2EAEcoreAddinInstaller/EAEcoreAddinInstaller/Debug/EAEcoreAddinInstaller.msi and
               // /MOFLON2EAEcoreAddinInstaller/EAEcoreAddinInstaller/Debug/setup.exe
               IFile ea_ecore_addin = createAddinArchive();

               // 4. Delete contents of target
               File target = clearFlatFolder(path + "/ea-ecore-addin");

               // 5. Copy created ea-ecore-addin.zip to target
               copyNewTarget(ea_ecore_addin, target);

               // 6. Clear temporary data
               if (ea_ecore_addin != null)
                  ea_ecore_addin.delete(true, null);

               /* (II) Deploy MoflonIdeUpdateSite */
               deployPlugin("MoflonIdeUpdateSite", path + "/update-site2");

               /* (III) Deploy MoflonDevToolsUpdateSite */
               deployPlugin("MoflonDevToolsUpdateSite", path + "/devtools");
            } catch (CoreException e)
            {
               logger.error(ERROR_MESSAGE_UNABLE_TO_DEPLOY);
               logger.error(ERROR_MESSAGE_RESTRICTED_ACCESS);
               logger.error(Activator.displayExceptionAsString(e));

               return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, e.getMessage(), e);
            }

            logger.info( INFO_MESSAGE_DEPLOYMENT_SUCCESSFUL);

            return new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, INFO_MESSAGE_DEPLOYMENT_SUCCESSFUL, null);
         }
      };
 
      job.schedule();
   }

   private static void replaceDLL()
   {
      IFile oldDLL = getProjectRoot("MoflonAutoTestPlugin").getFolder("resources").getFolder("commandLineEAExport").getFile("EAEcoreAddin.dll");
      IFile newDLL = getProjectRoot("MOFLON2EAEcoreAddin").getFolder("EAEcoreAddin").getFolder("bin").getFolder("Debug").getFile("EAEcoreAddin.dll");

      try
      {
         oldDLL.setContents(newDLL.getContents(), true, true, null);
      } catch (CoreException e)
      {
         e.printStackTrace();
      }
   }

   private static File clearFlatFolder(String targetPath)
   {
      File target = new File(targetPath);
      if (!target.exists())
      {
         target.mkdirs();
         return target;
      }

      File[] files = target.listFiles();

      for (File file : files)
         file.delete();
      
      return target;
   }

   private static File clearUpdateSite(String targetPath)
   {
      File target = new File(targetPath);
      if (!target.exists())
      {
         target.mkdirs();
         return target;
      }

      File[] files = target.listFiles();
      for (File file : files)
      {
         if (file.isDirectory())
         {
            if (file.getName().equals("features") || file.getName().equals("plugins"))
            {
               try
               {
                  clearFlatFolder(file.getCanonicalPath().toString());
               } catch (IOException e)
               {
                  e.printStackTrace();
               }
            } 
         } else
            file.delete();
      }
      
      return target;
   }

   private static File clearUpdateSiteProject(String sourcePath)
   {
      File target = new File(sourcePath);
      File[] files = target.listFiles();

      for (File file : files)
      {
         if (file.isDirectory())
         {
            if (file.getName().equals("features") || file.getName().equals("plugins"))
            {
               try
               {
                  clearFlatFolder(file.getCanonicalPath().toString());
               } catch (IOException e)
               {
                  e.printStackTrace();
               }
            }
         } else if (!fileToBeRetained(file))
            file.delete();
      }

      return target;
   }

   private static boolean fileToBeRetained(File file)
   {
      return     file.getName().equals(".project") 
              || file.getName().equals(".svn") 
              || file.getName().equals("site.xml")
              || file.getName().equals("associateSites.xml")
              || file.getName().equals("index.html");
   }

   private static void copyNewTarget(IResource source, File target)
   {
      try
      {
         if (source instanceof IProject)
         {
            IProject sourceProject = (IProject) source;
            IFolder features = sourceProject.getFolder("features");
            IFolder plugins = sourceProject.getFolder("plugins");
            IFile site = sourceProject.getFile("site.xml");
            IFile associatedSites = sourceProject.getFile("associateSites.xml");
            IFile indexHtml = sourceProject.getFile("index.html");
            
            File featuresFolder = new File(target + File.separator + "features");
            if (!featuresFolder.exists())
               featuresFolder.mkdir();
            while (!featuresFolder.exists())
               ;
            for (IResource file : features.members())
            {
               copyFile(file.getLocation().toFile(), new File(featuresFolder.getCanonicalPath().toString() + File.separator + file.getName()));
            }

            File pluginsFolder = new File(target + File.separator + "plugins");
            if (!pluginsFolder.exists())
               pluginsFolder.mkdir();
            while (!pluginsFolder.exists())
               ;
            for (IResource file : plugins.members())
            {
               copyFile(file.getLocation().toFile(), new File(pluginsFolder.getCanonicalPath().toString() + File.separator + file.getName()));
            }
            
            copyFile(site.getLocation().toFile(), new File(target.getCanonicalPath().toString() + File.separator + site.getName()));
            copyFile(associatedSites.getLocation().toFile(), new File(target.getCanonicalPath().toString() + File.separator + associatedSites.getName()));
            copyFile(indexHtml.getLocation().toFile(), new File(target.getCanonicalPath().toString() + File.separator + indexHtml.getName()));

         } else if (source instanceof IFolder)
         {
            for (IResource file : ((IFolder) source).members())
            {
               copyFile(file.getLocation().toFile(), new File(target.getCanonicalPath().toString() + File.separator + file.getName()));
            }
         } else if (source instanceof IFile)
         {
            copyFile(source.getLocation().toFile(), new File(target.getCanonicalPath().toString() + File.separator + source.getName()));
         }
      } catch (CoreException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }

   }

   private static void deployPlugin(String updateSite, String targetPlugin) throws CoreException
   {
      // 1. Delete all jars in project MoflonIdeUpdateSite and make copy of site.xml
      clearUpdateSiteProject(getProjectRoot(updateSite).getLocation().toString());
      getProjectRoot(updateSite).getFile("site.xml.temp").create(getSiteXML(updateSite).getContents(), true, null);

      // 2. Build MoflonIdeUpdateSite
      IProject source = build(updateSite);

      if (source == null)
         throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Plugin <" + updateSite + "> could not be built."));

      // 3. Delete contents of target
      File target = clearUpdateSite(targetPlugin);

      // 4. Copy folder structure of MoflonIdeUpdateSite to target
      copyNewTarget(source, target);

      // 5. Replace site.xml with copy from (II).2
      getSiteXML(updateSite).setContents(getProjectRoot(updateSite).getFile("site.xml.temp").getContents(), true, true, null);
      getProjectRoot(updateSite).getFile("site.xml.temp").delete(true, null);
   }

   private static IProject build(String projectName)
   {
      ISiteModel buildSiteModel = new WorkspaceSiteModel(getSiteXML(projectName));
      try
      {
         buildSiteModel.load();
      } catch (CoreException e)
      {
         PDEPlugin.logException(e);
         return null;
      }

      Job job = new SiteBuildOperation(getFeatureModels(buildSiteModel.getSite().getFeatures()), buildSiteModel, PDEUIMessages.BuildSiteJob_name);
      job.setUser(true);
      job.schedule();
      try
      {
         job.join();
         if (job.getResult().isOK())
            return getProjectRoot(projectName);
         else
            return null;
      } catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      return null;
   }

   private static IFile getSiteXML(String projectName)
   {
      return getProjectRoot(projectName).getFile("site.xml");
   }

   private static IProject getProjectRoot(String projectName)
   {
      return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
   }

   private static IFeatureModel[] getFeatureModels(ISiteFeature[] sFeatures)
   {
      ArrayList list = new ArrayList();
      for (int i = 0; i < sFeatures.length; i++)
      {
         IFeatureModel model = PDECore.getDefault().getFeatureModelManager().findFeatureModelRelaxed(sFeatures[i].getId(), sFeatures[i].getVersion());
         if (model != null)
            list.add(model);
      }
      return (IFeatureModel[]) list.toArray(new IFeatureModel[list.size()]);
   }

   private static IFile createAddinArchive()
   {
      IFile ea_ecore_addin = getProjectRoot("MOFLON2EAEcoreAddin").getFolder("EAEcoreAddinInstaller").getFolder("Release").getFile("ea-ecore-addin.zip");
      IFolder debugFolder = getProjectRoot("MOFLON2EAEcoreAddin").getFolder("EAEcoreAddinInstaller").getFolder("Debug");

      return getZipArchive(debugFolder, ea_ecore_addin);
   }

   private static void copyFile(File source, File target)
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
      } catch (FileNotFoundException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   private static IFile getZipArchive(IFolder folder, IFile archive)
   {
      byte[] buffer = new byte[1024];
      BufferedInputStream origin = null;

      try
      {
         // Check if previous version of archive exists and delete
         if (archive.exists())
            archive.delete(true, null);
         // Create empty archive
         archive.create(null, true, null);

         // Open streams
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archive.getLocation().toFile()));

         // Iterate over all entries of the folder
         IResource files[] = folder.members();
         for (int i = 0; i < files.length; i++)
         {
            // Skip subfolders (should not exist)
            if (!(files[i] instanceof IFile))
               break;

            // Read content of file and add to archive
            IFile file = (IFile) files[i];
            origin = new BufferedInputStream(new FileInputStream(file.getLocation().toFile()));
            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(buffer)) > 0)
            {
               out.write(buffer, 0, count);
            }

            // Close entry and added file
            out.closeEntry();
            origin.close();
         }

         // Close zip archive
         out.close();
         return archive;
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      return null;
   }

}