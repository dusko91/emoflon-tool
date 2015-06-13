(Description based on Eclipse 3.7 - Indigo)
right click on build.xml --> select "Ant build..." --> go to "JRE" ribbon --> choose "Run in the same JRE as the workspace" --> apply and run

It is important that the ant-file is run within the Eclipse OSGi environment.

(
It might be possible to do this from command line... Try something like:

***************************************************************************************
java
  -jar <eclipse-install-directory>\eclipse\plugins\org.eclipse.equinox.launcher_*.jar
  -nosplash
  -data ${java.io.tmpdir}/workspace
  -consolelog
  -application org.eclipse.ant.core.antRunner
  -f /path/to/scripts/build.xml 
***************************************************************************************
)



This will result in a new composite repository located in the build-directory (as specified in the "build.properties") that references all
required plugins for our development workspace. The generated repository (jar-file) can then be deployed to a web-server for deployment.
Due to legal constraints, it might be a good idea to restrict repository access to internal users only. 