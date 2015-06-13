package org.moflon.ide.core.admin;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.moflon.Activator;
import org.moflon.admin.BasicClasspathContainer;
import org.moflon.ide.core.CoreActivator;

public class MoflonClasspathContainer extends BasicClasspathContainer
{
   private static final String LOG4J = Activator.PLUGIN_ID;

   private static final String CODEGEN2 = Activator.PLUGIN_ID;

   private static final String DEPENDENCIES = Activator.PLUGIN_ID;

   private static final String MOFLON = CoreActivator.PLUGIN_ID;

   private static final String COMMONS_IO = Activator.PLUGIN_ID;

   private static final String COMMONS_LANG = Activator.PLUGIN_ID;

   private static final String VALIDATION = "org.moflon.validation";

   public MoflonClasspathContainer(IPath containerPath, IJavaProject project)
   {
      super(containerPath, project);
   }

   @Override
   public IClasspathEntry[] getClasspathEntries()
   {
      ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();

      // create CPE_LIBRARYs of type cp entry with an attached source archive if it exists

      URL log4j = Activator.getPathRelToPlugIn("/lib/log4j.jar", LOG4J);
      entryList.add(getEntryFor(log4j));

      URL codegen2 = Activator.getPathRelToPlugIn("/lib/RuntimeTools.jar", CODEGEN2);
      entryList.add(getEntryFor(codegen2));

      URL dependencies = Activator.getPathRelToPlugIn("/", DEPENDENCIES);
      entryList.add(getEntryFor(dependencies));

      URL moflon = Activator.getPathRelToPlugIn("/", MOFLON);
      entryList.add(getEntryFor(moflon));

      URL commons_lang = Activator.getPathRelToPlugIn("/lib/commons-lang3-3.0-beta.jar", COMMONS_IO);
      entryList.add(getEntryFor(commons_lang));

      URL commons_io = Activator.getPathRelToPlugIn("/lib/commons-io-2.0.jar", COMMONS_LANG);
      entryList.add(getEntryFor(commons_io));

      URL validation = Activator.getPathRelToPlugIn("/", VALIDATION);
      entryList.add(getEntryFor(validation));

      // convert the list to an array and return it
      IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
      return (IClasspathEntry[]) entryList.toArray(entryArray);
   }

   @Override
   public String getDescription()
   {
      return "Moflon Libraries";
   }

}
