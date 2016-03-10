package org.moflon.tgg.democles.compiler.util;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gervarro.democles.specification.emf.Pattern;
import org.moflon.tgg.democles.compiler.patterntype.DECPatternTypes;
import org.moflon.tgg.language.DomainType;
import org.moflon.tgg.language.TGGLinkVariable;
import org.moflon.tgg.language.TGGRule;

import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.ObjectVariable;

public class DECPatternUtil
{

   public static Collection<Pattern> createDECPatterns(TGGRule rule, DECPatternTypes patternType)
   {

      Stream<Pattern> patternsForDECOVs = rule.getObjectVariable().stream().filter(ov -> CompilerUtil.isNegativeInDomain(ov, getDomainTypeFor(patternType)))
            .map(DECPatternUtil::createNACPatternFor);

      Stream<Pattern> patternsForDECLVs = rule.getLinkVariable().stream().filter(lv -> CompilerUtil.isNegativeInDomain(lv, getDomainTypeFor(patternType)))
            .map(DECPatternUtil::createNACPatternFor);

      return Stream.concat(patternsForDECOVs, patternsForDECLVs).collect(Collectors.toSet());
   }

   private static Pattern createNACPatternFor(ObjectVariable negativeOV)
   {

      // We assume that a NAC-OV has exactly one incident link

      ObjectVariable neighbour = null;
      LinkVariable link = null;

      if (negativeOV.getIncomingLink().size() > 0)
      {
         link = negativeOV.getIncomingLink().get(0);
         neighbour = link.getSource();
      } else
      {
         link = negativeOV.getOutgoingLink().get(0);
         neighbour = link.getTarget();
      }

      String ruleName = ((TGGRule) negativeOV.getPattern()).getName();
      Pattern result = CompilerUtil.createEmptyPattern(ruleName + "_" + negativeOV.getName());

      result.getSymbolicParameters().add(CompilerUtil.createEMFVariable(neighbour));
      result.getBodies().get(0).getLocalVariables().add(CompilerUtil.createEMFVariable(negativeOV));
      CompilerUtil.createReference((TGGLinkVariable) link, result);

      return result;
   }

   private static Pattern createNACPatternFor(LinkVariable negativeLV)
   {
      String ruleName = ((TGGRule) negativeLV.getPattern()).getName();
      
      Pattern result = CompilerUtil.createEmptyPattern(ruleName + "_DEC_" + negativeLV.getSource().getName() + "_" + negativeLV.getName() + "_" + negativeLV.getTarget().getName());
      
      result.getSymbolicParameters().add(CompilerUtil.createEMFVariable(negativeLV.getSource()));
      if(negativeLV.getSource() != negativeLV.getTarget())
         result.getSymbolicParameters().add(CompilerUtil.createEMFVariable(negativeLV.getTarget()));
      
      CompilerUtil.createReference((TGGLinkVariable) negativeLV, result);
      
      return result;
   }

   private static DomainType getDomainTypeFor(DECPatternTypes patternType)
   {
      if (patternType.equals(DECPatternTypes.SOURCE_DEC))
         return DomainType.SOURCE;
      else if (patternType.equals(DECPatternTypes.TARGET_DEC))
         return DomainType.TARGET;
      return null;
   }

}
