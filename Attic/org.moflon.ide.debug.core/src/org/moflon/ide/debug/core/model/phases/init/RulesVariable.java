package org.moflon.ide.debug.core.model.phases.init;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.moflon.ide.debug.core.model.MoflonDebugElement;

import org.moflon.tgg.debug.language.DebugRules;

public class RulesVariable extends MoflonDebugElement implements IVariable
{
   IValue value;
   
   DebugRules rules;

   public RulesVariable(IDebugTarget target, DebugRules debugRules)
   {
      super(target);
      this.rules = debugRules;
      value = new RulesValue(getDebugTarget(), rules);
   }

   @Override
   public void setValue(String expression) throws DebugException
   {
      // TODO Auto-generated method stub
   }

   @Override
   public void setValue(IValue value) throws DebugException
   {
      // TODO Auto-generated method stub

   }

   @Override
   public boolean supportsValueModification()
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean verifyValue(String expression) throws DebugException
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean verifyValue(IValue value) throws DebugException
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public IValue getValue() throws DebugException
   {
      return value;
   }

   @Override
   public String getName() throws DebugException
   {
      return "Rules";
   }

   @Override
   public String getReferenceTypeName() throws DebugException
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean hasValueChanged() throws DebugException
   {
      // TODO Auto-generated method stub
      return false;
   }

}