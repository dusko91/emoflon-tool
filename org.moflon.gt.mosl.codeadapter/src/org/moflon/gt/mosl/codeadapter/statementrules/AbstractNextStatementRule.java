package org.moflon.gt.mosl.codeadapter.statementrules;

import org.eclipse.emf.ecore.EClassifier;
import org.moflon.gt.mosl.codeadapter.StatementBuilder;
import org.moflon.gt.mosl.exceptions.MissingReturnException;
import org.moflon.gt.mosl.moslgt.MoslgtFactory;
import org.moflon.gt.mosl.moslgt.NextStatement;
import org.moflon.gt.mosl.moslgt.Statement;
import org.moflon.sdm.runtime.democles.CFNode;
import org.moflon.sdm.runtime.democles.Scope;

public abstract class AbstractNextStatementRule<S extends NextStatement> extends AbstractStatementRule<S>
{
   @Override
   protected void postTransformStatement(S stmnt, Scope scope, CFNode previosCFNode)
   {
      Statement nextStmnt = stmnt.getNext();
      if (nextStmnt == null)
      {
         EClassifier methodType = StatementBuilder.getInstance().getCurrentMethod().getType();
         if (methodType == null)
            nextStmnt = MoslgtFactory.eINSTANCE.createReturnStatement();
         else
            throw new MissingReturnException(StatementBuilder.getInstance().getCurrentMethod());
      }

      if (previosCFNode != null)
      {
         previosCFNode.setNext(currentNode);
         if (currentNode != null)
            currentNode.setId(previosCFNode.getId() + 1);
      } else
      {
         if (currentNode != null)
            currentNode.setId(1);
      }

      if (currentNode == null)
         currentNode = previosCFNode;

      StatementBuilder.getInstance().transformStatement(nextStmnt, scope, currentNode);
   }

   private CFNode currentNode;

   protected <C extends CFNode> C updateCurrentNode(C current)
   {
      currentNode = current;
      return current;
   }
}
