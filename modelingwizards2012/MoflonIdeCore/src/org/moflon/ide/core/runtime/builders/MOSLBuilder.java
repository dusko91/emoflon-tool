package org.moflon.ide.core.runtime.builders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.moflon.moca.sch.parser.SchParserAdapter;
import org.moflon.moca.tgg.parser.TggParserAdapter;
import org.moflon.util.eMoflonEMFUtil;

import Moca.CodeAdapter;
import Moca.MocaFactory;
import TGGLanguage.TripleGraphGrammar;
import TextualTGGCodeAdapter.Transformer;
import TextualTGGCodeAdapter.impl.TextualTGGCodeAdapterFactoryImpl;

public class MOSLBuilder extends AbstractBuilder
{

   /**
    * TODO MOSLBuilder Better Delta Build
    * 
    * MOSLBuilder should only build the integration where a change was made, not all integrations.
    * 
    */
   
   private static final Logger logger = Logger.getLogger(MOSLBuilder.class);

   @Override
   public boolean visit(IResource resource) throws CoreException
   {
      // Test if resource is tgg or sch file
      if (resource.getName().endsWith(".tgg") || resource.getName().endsWith(".sch"))
      {
         logger.debug("Found sch- or tgg-file");
         processResource(subMonitor);
         return true;
      }

      return false;
   }

   @Override
   public boolean visit(IResourceDelta delta) throws CoreException
   {
      // Get changes and call visit on *.sch and *.tgg files
      IResourceDelta[] changes = delta.getAffectedChildren();
      for (int i = 0; i < changes.length; i++)
      {
         IResource resource = changes[i].getResource();
         if (resource.getName().endsWith(".tgg") || resource.getName().endsWith(".sch")
               && (changes[i].getKind() == IResourceDelta.CHANGED || changes[i].getKind() == IResourceDelta.ADDED))
         {
            return visit(resource);
         } else
            visit(changes[i]);
      }

      return false;
   }

   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      logger.debug("Build MOSL for " + getProject().getName());

      // Register parsers and unparsers
      CodeAdapter codeAdapter = MocaFactory.eINSTANCE.createCodeAdapter();
      codeAdapter.getParser().add(new SchParserAdapter());
      codeAdapter.getParser().add(new TggParserAdapter());

      IProject project = getProject();

      IFolder moslFolder = project.getFolder("MOSL");

      for (IResource folder : moslFolder.members())
      {
         if (folder.getType() == IResource.FOLDER)
         {

            File dir = folder.getLocation().toFile();

            // Perform text to tree
            MocaTree.Folder tree = codeAdapter.parse(dir);

            // Perform tree to model
            Transformer transformer = TextualTGGCodeAdapterFactoryImpl.eINSTANCE.createTransformer();

            TripleGraphGrammar tgg = transformer.build(tree);

            // No tgg created or no package found
            if (tgg == null || transformer.getSrcPackage() == null || transformer.getTrgPackage() == null)
            {
               continue;
            }

            // Persist models
            IFolder temp = project.getFolder(".temp");
            if (temp.exists())
               temp.delete(true, monitor);
            temp.create(true, true, monitor);

            // Fill resource set with source and target metamodels
            ResourceSet resourceSet = new ResourceSetImpl();

            // Add source mm to resource set
            Resource src = resourceSet.createResource(URI.createURI(transformer.getSrcPackage().getNsURI()));
            src.getContents().add(transformer.getSrcPackage());

            // Add trg mm to resource set
            Resource trg = resourceSet.createResource(URI.createURI(transformer.getTrgPackage().getNsURI()));
            trg.getContents().add(transformer.getTrgPackage());

            try
            {
               // Persist corr mm with resource set
               Resource corr = resourceSet.createResource(URI.createURI(transformer.getCorrPackage().getNsURI()));
               corr.getContents().add(transformer.getCorrPackage());

               String packageLocation = temp.getLocation().toString() + "\\" + transformer.getCorrPackage().getName() + ".ecore";
               OutputStream outputStream = new FileOutputStream(new File(packageLocation));
               corr.save(outputStream, null);
               outputStream.close();

               // Persist tgg
               String tggLocation = temp.getLocation().toString() + "\\" + tgg.getName() + ".tgg.xmi";
               eMoflonEMFUtil.saveModel(tgg, tggLocation, resourceSet);

               String srcName = transformer.getSrcPackage().getName();
               String trgName = transformer.getTrgPackage().getName();
               String corrName = transformer.getCorrPackage().getName();
               addPropertyFile(project, monitor, temp, corrName, srcName, trgName);
            } catch (IOException e)
            {
               e.printStackTrace();
            }

            temp.refreshLocal(IResource.DEPTH_ONE, monitor);
         }
      }

      return false;
   }

   
   private void addPropertyFile(IProject project, IProgressMonitor monitor, IFolder folder, String corr, String src, String trg) throws IOException,
         CoreException
   {

      // Defining property File name
      String propertyFileName = project.getName() + ".properties";
      IFile propertiesFile = folder.getFile(propertyFileName);

      // Creating the property file if not existing
      if (!propertiesFile.exists())
      {
         InputStream source = new ByteArrayInputStream("".getBytes());
         propertiesFile.create(source, true, monitor);
      }

      // Defining properties
      Properties properties = new Properties();

      // Load properties from file
      InputStream contents = propertiesFile.getContents(true);
      properties.load(contents);
      contents.close();

      // Setting the properties
      properties.setProperty(corr + ".type", "integration");
      properties.setProperty(corr + ".workingSet", project.getName());
      properties.setProperty(corr + ".dependencies", src + "," + trg);

      // Persist properties
      StringBuilderWriter sbw = new StringBuilderWriter();
      properties.store(sbw, "Moflon Property File");
      propertiesFile.setContents(new ByteArrayInputStream(sbw.toString().getBytes()), true, true, new NullProgressMonitor());
   }
   
}
