package autobuildplugin.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants
{

   public static final String AB_PROJECTSET = "projectSetPreference";

   public static final String AB_DEFAULT_PROJECTSET = "<Path to PSF file>/<PSF file>.psf";

   public static final String AB_NEXTOP = "nextOPPreference";

   public static final String AB_DEFAULT_NEXTOP = NEXTOPERATION.DELETEWORKSPACE.toString();

   public static enum NEXTOPERATION {
      DELETEWORKSPACE {
         public String toString()
         {
            return "Delete all projects";
         }
      },
      IMPORTPROJECTSET {
         public String toString()
         {
            return "Import projects from PSF file";
         }
      },
      REFRESH {
         public String toString()
         {
            return "Refresh workspace (code generation)";
         }
      },
      JUNIT {
         public String toString()
         {
            return "Run JUnit Testsuite";
         }
      },
      MOFLONBUILD {
         public String toString()
         {
            return "Export Ecore models from EA project files";
         }
      }
   };

   public static NEXTOPERATION getNextOp(String value)
   {
      if (value.compareTo(NEXTOPERATION.DELETEWORKSPACE.toString()) == 0)
         return NEXTOPERATION.DELETEWORKSPACE;
      if (value.compareTo(NEXTOPERATION.IMPORTPROJECTSET.toString()) == 0)
         return NEXTOPERATION.IMPORTPROJECTSET;
      if (value.compareTo(NEXTOPERATION.REFRESH.toString()) == 0)
         return NEXTOPERATION.REFRESH;
      if (value.compareTo(NEXTOPERATION.JUNIT.toString()) == 0)
         return NEXTOPERATION.JUNIT;
      return NEXTOPERATION.MOFLONBUILD;
   }
}
