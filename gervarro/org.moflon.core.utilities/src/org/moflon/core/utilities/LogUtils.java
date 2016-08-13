package org.moflon.core.utilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This utility lass simplifies the printing of log messages.
 */
public final class LogUtils
{
   private LogUtils()
   {
      throw new UtilityClassNotInstantiableException();
   }

   public static void log(final Logger logger, Level level, final String formatString, final Object... arguments)
   {
      if (logger.getLevel().isGreaterOrEqual(level))
      {
         logger.log(level, String.format(formatString, arguments));
      }
   }

   public static void error(final Logger logger, final String formatString, final Object... arguments)
   {
      log(logger, Level.ERROR, formatString, arguments);
   }

   /**
    * This method solely logs the stacktrace of the given {@link Throwable} as an {@link Level#ERROR}.
    * 
    * The purpose of this method is to avoid the infamous 'e.printStackTrace()', which hides critical problems from
    * end-users.
    */
   public static void logStacktrace(final Logger logger, final Throwable t)
   {
      error(logger, WorkspaceHelper.printStacktraceToString(t));
   }

   public static void warn(final Logger logger, final String formatString, final Object... arguments)
   {
      log(logger, Level.WARN, formatString, arguments);
   }

   public static void info(final Logger logger, final String formatString, final Object... arguments)
   {
      log(logger, Level.INFO, formatString, arguments);
   }

   public static void debug(final Logger logger, final String formatString, final Object... arguments)
   {
      log(logger, Level.DEBUG, formatString, arguments);
   }
}
