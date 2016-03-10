package csp.constraints;

import java.util.regex.Pattern;

import org.moflon.tgg.language.csp.Variable;
import org.moflon.tgg.language.csp.impl.TGGConstraintImpl;

import csp.constraints.generator.Generator;

public class Concat extends TGGConstraintImpl
{

   /**
    * concat(":", a, b, c)
    * 
    * a . ":" . b = c
    * 
    * @see TGGLanguage.csp.impl.ConstraintImpl#solve()
    */
   public void solve(Variable separator, Variable a, Variable b, Variable c)
   {
      String bindingStates = getBindingStates(separator, a, b, c);

      switch (bindingStates)
      {
      case "BBBB":
      {
         setSatisfied(((String) a.getValue() + separator.getValue() + b.getValue()).equals(c.getValue()));
         return;
      }

      case "BBBF":
      {
         c.setValue((String) a.getValue() + separator.getValue() + b.getValue());
         c.setBound(true);
         setSatisfied(true);
         return;
      }

      case "BBFB":
      {
         String[] split = ((String) c.getValue()).split(Pattern.quote((String) separator.getValue()));
         if (split.length != 2)
         {
            setSatisfied(false);
         } else
         {
            b.setValue(split[1]);
            b.setBound(true);
            setSatisfied(true);
         }
         return;
      }

      case "BFBB":
      {
         String[] split = c.getValue().toString().split(Pattern.quote((String) separator.getValue()));
         a.setValue(split[0]);
         a.setBound(true);
         setSatisfied(true);
         return;
      }

      case "BFFB":
      {
         String[] split = c.getValue().toString().split(Pattern.quote((String) separator.getValue()));
         if (split.length == 2)
         {
            a.setValue(split[0]);
            a.setBound(true);
            b.setValue(split[1]);
            b.setBound(true);
            setSatisfied(true);
         }
         return;
      }
      // modelgen implementations
      case "BFFF":
      {
         setSatisfied(true);
         String value1 = Generator.getNewRandomString(a.getType());
         String value2 = Generator.getNewRandomString(b.getType());
         a.bindToValue(value1);
         b.bindToValue(value2);
         c.bindToValue(value1 + separator.getValue() + value2);
         return;
      }

      case "BBFF":
      {
         setSatisfied(true);
         String value2 = Generator.getNewRandomString(c.getType());
         c.bindToValue((String) a.getValue() + separator.getValue() + value2);
         return;
      }

      case "BFBF":
      {
         setSatisfied(true);
         String value1 = Generator.getNewRandomString(a.getType());
         a.bindToValue(value1);
         c.bindToValue(value1 + separator.getValue() + b.getValue());
         return;
      }

      default:
         throw new UnsupportedOperationException("This case in the constraint has not been implemented yet: " + bindingStates);
      }
   }
}
