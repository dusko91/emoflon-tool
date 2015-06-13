package org.moflon.ide.core.admin;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.moflon.Activator;
import org.moflon.admin.BasicClasspathContainer;

public class MoslClasspathContainer extends BasicClasspathContainer
{

   private static final String TGGLANGUAGE = "org.moflon.TGGLanguage";

   private static final String SDMLANGUAGE = "org.moflon.SDMLanguage";

   private static final String TGGRUNTIME = "org.moflon.TGGRuntime";

   public MoslClasspathContainer(IPath containerPath, IJavaProject project)
   {
      super(containerPath, project);
   }

   @Override
   public IClasspathEntry[] getClasspathEntries()
   {
      ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();

      // create CPE_LIBRARYs of type cp entry with an attached source archive if it exists

      URL tggLanguage = Activator.getPathRelToPlugIn("/", TGGLANGUAGE);
      entryList.add(getEntryFor(tggLanguage));

      URL sdmLanguage = Activator.getPathRelToPlugIn("/", SDMLANGUAGE);
      entryList.add(getEntryFor(sdmLanguage));

      URL tggRuntime = Activator.getPathRelToPlugIn("/", TGGRUNTIME);
      entryList.add(getEntryFor(tggRuntime));

      URL democles = Activator.getPathRelToPlugIn("/lib/democles.jar", TGGLANGUAGE);
      entryList.add(getEntryFor(democles));

      URL javabdd = Activator.getPathRelToPlugIn("/lib/javabdd-1.0b2.jar", TGGLANGUAGE);
      entryList.add(getEntryFor(javabdd));

      // convert the list to an array and return it
      IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
      return (IClasspathEntry[]) entryList.toArray(entryArray);
   }

   @Override
   public String getDescription()
   {
      return "Moflon Languages";
   }

}
