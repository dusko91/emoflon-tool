/*
 * The FUJABA ToolSuite project:
 *
 *   FUJABA is the acronym for 'From Uml to Java And Back Again'
 *   and originally aims to provide an environment for round-trip
 *   engineering using UML as visual programming language. During
 *   the last years, the environment has become a base for several
 *   research activities, e.g. distributed software, database
 *   systems, modelling mechanical and electrical systems and
 *   their simulation. Thus, the environment has become a project,
 *   where this source code is part of. Further details are avail-
 *   able via http://www.fujaba.de
 *
 *      Copyright (C) Fujaba Development Group
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free
 *   Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *   MA 02111-1307, USA or download the license under
 *   http://www.gnu.org/copyleft/lesser.html
 *
 * WARRANTY:
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 * Contact address:
 *
 *   Fujaba Management Board
 *   Software Engineering Group
 *   University of Paderborn
 *   Warburgerstr. 100
 *   D-33098 Paderborn
 *   Germany
 *
 *   URL  : http://www.fujaba.de
 *   email: info@fujaba.de
 *
 */
package de.fujaba.text.expression;

import de.fujaba.text.FParsedElement;
import de.fujaba.text.TextNode;
import de.fujaba.text.visitor.ArgVisitor;
import de.fujaba.text.visitor.ArgVoidVisitor;
import de.fujaba.text.visitor.NoArgVisitor;
import de.fujaba.text.visitor.NoArgVoidVisitor;

/**
 * 
 * 
 * @author patrick.oppermann@cs.uni-kassel.de
 */
public class Instanceof extends BooleanOperation
{
   /**
    * 
    * @param diagramItem
    * @param expression
    * @param typeExpression
    */
   public Instanceof(FParsedElement parsedElement, TextNode expression, TextNode typeExpression)
   {
      super(parsedElement);
      setExpression(expression);
      setTypeExpression(typeExpression);
   }

   /**
    * <pre>
    *           0..1     expression     0..1 
    * UMLTextNode ------------------------- InstanceOf
    *           expression
    * </pre>
    */
   private TextNode expression;

   public boolean setExpression(TextNode value)
   {
      boolean changed = false;

      if(this.expression != value)
      {
         TextNode oldValue = this.expression;
         this.expression = value;

         // sync children link
         this.removeFromChildren(oldValue);
         this.addToChildren(0, value);

         changed = true;
      }
      return changed;
   }

   public TextNode getExpression()
   {
      return this.expression;
   }

   /**
    * 
    */
   private Operator operator;

   public Operator getOperator()
   {
      return operator;
   }

   public boolean setOperator(Operator value)
   {
      boolean changed = false;

      if(this.operator != value)
      {
         TextNode oldValue = this.operator;
         this.operator = value;

         // sync children link
         this.removeFromChildren(oldValue);
         this.addAfterOfChildren(this.getExpression(), value);

         changed = true;
      }
      return changed;
   }

   /**
    * <pre>
    *           0..1     typeExpression     0..1 
    * UMLTextNode ------------------------- InstanceOf
    *           typeExpression      
    * </pre>
    */
   private TextNode typeExpression;

   public boolean setTypeExpression(TextNode value)
   {
      boolean changed = false;

      if(this.typeExpression != value)
      {
         TextNode oldValue = this.typeExpression;
         this.typeExpression = value;

         // sync children link
         this.removeFromChildren(oldValue);
         this.addToChildren(value);

         changed = true;
      }
      return changed;
   }

   public TextNode getTypeExpression()
   {
      return this.typeExpression;
   }

   /*
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * 
    * removeYou() implementation that fires an event to indicate that the
    * removal of this instance is about to begin.
    * 
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

   @Override
   public void removeYou()
   {
      this.getPropertyChangeSupport().firePropertyChange(TextNode.REMOVE_YOU_STARTED, this, this);

      if(this.getExpression() != null)
      {
         this.getExpression().removeYou();
         this.setExpression(null);
      }

      if(this.getOperator() != null)
      {
         this.getOperator().removeYou();
         this.setOperator(null);
      }

      if(this.getTypeExpression() != null)
      {
         this.getTypeExpression().removeYou();
         this.setTypeExpression(null);
      }

      super.removeYou();
   }

   /*
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    * 
    * Visitable implementation.
    * 
    * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

   @Override
   public void accept(NoArgVoidVisitor v)
   {
      v.visit(this);
   }

   @Override
   public <R, A> R accept(ArgVisitor<R, A> v, A argu)
   {
      return v.visit(this, argu);
   }

   @Override
   public <R> R accept(NoArgVisitor<R> v)
   {
      return v.visit(this);
   }

   @Override
   public <A> void accept(ArgVoidVisitor<A> v, A argu)
   {
      v.visit(this, argu);
   }

}