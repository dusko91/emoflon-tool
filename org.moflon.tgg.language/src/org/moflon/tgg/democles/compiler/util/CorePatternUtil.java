package org.moflon.tgg.democles.compiler.util;

import org.gervarro.democles.specification.emf.Pattern;
import org.gervarro.democles.specification.emf.PatternBody;
import org.gervarro.democles.specification.emf.SpecificationFactory;
import org.moflon.tgg.democles.compiler.patterntype.CorePatternTypes;
import org.moflon.tgg.language.DomainType;
import org.moflon.tgg.language.TGGLinkVariable;
import org.moflon.tgg.language.TGGObjectVariable;
import org.moflon.tgg.language.TGGRule;

import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.ObjectVariable;

public class CorePatternUtil
{

   public static Pattern createCorePattern(TGGRule rule, CorePatternTypes patternType)
   {
      Pattern pattern = CompilerUtil.createEmptyPattern(rule.getName() + '_' + patternType);

      rule.getObjectVariable().stream().filter(ov -> CorePatternUtil.isOVrelevant(ov, patternType))
            .map(CompilerUtil::createEMFVariable).forEach(emfV -> {
               pattern.getSymbolicParameters().add(emfV);
            });

      rule.getLinkVariable().stream().filter(lv -> CorePatternUtil.isLVrelevant(lv, patternType)).forEach(lv -> {
         CompilerUtil.createReference((TGGLinkVariable) lv, pattern);
      });

      return pattern;
   }
   
   public static boolean isOVrelevant(ObjectVariable ov, CorePatternTypes patternType) {

      switch (patternType) {
      case SOURCE_BLACK:
         return CompilerUtil.isContextInDomain(ov, DomainType.SOURCE);

      case TARGET_BLACK:
         return CompilerUtil.isContextInDomain(ov, DomainType.TARGET);

      case TRIPLE_BLACK:
         return CompilerUtil.isContext(ov);

      case FWD:
         return CompilerUtil.isContext(ov) || CompilerUtil.isCreateInDomain(ov, DomainType.SOURCE);

      case BWD:
         return CompilerUtil.isContext(ov) || CompilerUtil.isCreateInDomain(ov, DomainType.TARGET);

      case TRIPLE_WHOLE:
         return true;

      default:
         return false;
      }
   }


   public static boolean isLVrelevant(LinkVariable lv, CorePatternTypes patternType) {

      switch (patternType) {
      case SOURCE_BLACK:
         return CompilerUtil.isContextInDomain(lv, DomainType.SOURCE);
      case TARGET_BLACK:
         return CompilerUtil.isContextInDomain(lv, DomainType.TARGET);

      case TRIPLE_BLACK:
         return CompilerUtil.isContextInDomain(lv, DomainType.CORRESPONDENCE);

      case FWD:
         return CompilerUtil.isCreateInDomain(lv, DomainType.SOURCE);

      case BWD:
         return CompilerUtil.isCreateInDomain(lv, DomainType.TARGET);

      case TRIPLE_WHOLE:
         return CompilerUtil.isCreateInDomain(lv, DomainType.CORRESPONDENCE);

      default:
         return false;
      }

   }
}
