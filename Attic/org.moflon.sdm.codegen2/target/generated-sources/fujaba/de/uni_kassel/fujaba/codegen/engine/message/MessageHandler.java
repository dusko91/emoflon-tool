/*
 * generated by Fujaba - CodeGen2
 */
package de.uni_kassel.fujaba.codegen.engine.message;
import de.uni_paderborn.fujaba.metamodel.common.FElement;


public interface MessageHandler
{


   public void error (String userFeedback , FElement context );

   public void internalError (String userFeedback , FElement context );

   public void warning (String userFeedback , FElement context );

   public void removeYou();
}

