package org.moflon.ide.core.runtime.builders.hooks;

import org.eclipse.core.runtime.IStatus;
import org.moflon.core.mocatomoflon.Exporter;
import org.moflon.ide.core.properties.MocaTreeEAPropertiesReader;
import org.moflon.ide.core.runtime.builders.MetamodelBuilder;

public interface PostMetamodelBuilderHook
{
   String POST_BUILD_EXTENSION_ID = "org.moflon.extensionpoints.postmetamodelbuilderhook";

   /**
    * Performs an action after the finalization of the {@link MetamodelBuilder}.
    * 
    * @param mocaToMoflonStatus
    *           the status returned from the Moca-to-Moflon transformation
    * @param mocaTreeReader
    *           the Moca tree reader with the stored Moca tree
    * @param exporter
    *           the Moca-to-Moflon transformer instance that has been used by the {@link MetamodelBuilder}
    * @return the execution status of this hook
    */
   IStatus run(final IStatus mocaToMoflonStatus, final MocaTreeEAPropertiesReader mocaTreeReader, final Exporter exporter);
}