package org.moflon.dependency.handlers;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.moflon.dependency.Dependency;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.dependency.SimpleDependency;

/**
 * A dependency handler which extracts "additional dependencies" from moflon.properties.xmi
 */
public class UserDefinedProjectDependencyHandler implements DependencyHandler
{
   public Collection<Dependency> getEcoreDependencies(IProject project)
   {
      return DependencyHandler.getDependencies(project, properties -> properties.getAdditionalDependencies(), 
            dep -> new PackageRemappingDependency(URI.createURI(dep), true, false));
   }

   public Collection<Dependency> getGenModelDependencies(IProject project)
   {
      return DependencyHandler.getDependencies(project, properties -> properties.getAdditionalUsedGenPackages(), 
            dep -> new SimpleDependency(URI.createURI(dep)));
   }
}
