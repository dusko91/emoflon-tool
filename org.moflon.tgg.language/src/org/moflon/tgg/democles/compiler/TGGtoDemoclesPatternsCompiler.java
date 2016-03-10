package org.moflon.tgg.democles.compiler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.gervarro.democles.specification.emf.Pattern;
import org.gervarro.democles.specification.emf.PatternInvocationConstraint;
import org.gervarro.democles.specification.emf.SpecificationFactory;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.tgg.democles.compiler.patterntype.CorePatternTypes;
import org.moflon.tgg.democles.compiler.patterntype.DECPatternTypes;
import org.moflon.tgg.democles.compiler.util.CompilerUtil;
import org.moflon.tgg.democles.compiler.util.CorePatternUtil;
import org.moflon.tgg.democles.compiler.util.DECPatternUtil;
import org.moflon.tgg.language.TGGRule;
import org.moflon.tgg.language.TripleGraphGrammar;
import org.moflon.tgg.language.analysis.decNACs.DECNACAnalysis;
import org.moflon.tgg.language.analysis.decNACs.DecNACsFactory;

import SDMLanguage.patterns.BindingSemantics;
import SDMLanguage.patterns.LinkVariable;
import SDMLanguage.patterns.ObjectVariable;

public class TGGtoDemoclesPatternsCompiler
{

   private DECNACAnalysis decNACAnalysis;

   public Stream<Pattern> createPatterns(TripleGraphGrammar tgg)
   {
      // Prepare DEC-NAC Analysis
      decNACAnalysis = DecNACsFactory.eINSTANCE.createDECNACAnalysis();
      eMoflonEMFUtil.createParentResourceAndInsertIntoResourceSet(decNACAnalysis, tgg.eResource().getResourceSet());
      decNACAnalysis.setTgg(EcoreUtil.copy(tgg));

      return tgg.getTggRule().stream().flatMap(this::createPatterns);
   }

   private Stream<Pattern> createPatterns(TGGRule rule)
   {

      // temporary solution as we don't support user NACs in the first version
      removeUserNACs(rule);
      // end of temporary solution remove NACs

      HashMap<CorePatternTypes, Pattern> corePatterns = new HashMap<>();
      Arrays.asList(CorePatternTypes.values()).stream().forEach(p -> corePatterns.put(p, CorePatternUtil.createCorePattern(rule, p)));

      HashMap<DECPatternTypes, Collection<Pattern>> decPatterns = new HashMap<>();
      Arrays.asList(DECPatternTypes.values()).stream().forEach(d -> decPatterns.put(d, DECPatternUtil.createDECPatterns(createRuleWithDECNACs(rule), d)));

      createPatternInvocations(corePatterns, decPatterns);

      return Stream.concat(corePatterns.values().stream(), decPatterns.values().stream().flatMap(v -> v.stream()));
   }

   private void createPatternInvocations(HashMap<CorePatternTypes, Pattern> corePatterns, HashMap<DECPatternTypes, Collection<Pattern>> decPatterns)
   {
      createPatternInvocation(corePatterns.get(CorePatternTypes.TRIPLE_BLACK), corePatterns.get(CorePatternTypes.SOURCE_BLACK), true);
      createPatternInvocation(corePatterns.get(CorePatternTypes.TRIPLE_BLACK), corePatterns.get(CorePatternTypes.TARGET_BLACK), true);
      createPatternInvocation(corePatterns.get(CorePatternTypes.FWD), corePatterns.get(CorePatternTypes.TRIPLE_BLACK), true);
      createPatternInvocation(corePatterns.get(CorePatternTypes.BWD), corePatterns.get(CorePatternTypes.TRIPLE_BLACK), true);
      createPatternInvocation(corePatterns.get(CorePatternTypes.TRIPLE_WHOLE), corePatterns.get(CorePatternTypes.FWD), true);
      createPatternInvocation(corePatterns.get(CorePatternTypes.TRIPLE_WHOLE), corePatterns.get(CorePatternTypes.BWD), true);

      decPatterns.get(DECPatternTypes.SOURCE_DEC).stream().forEach(d -> createPatternInvocation(corePatterns.get(CorePatternTypes.FWD), d, false));
      decPatterns.get(DECPatternTypes.TARGET_DEC).stream().forEach(d -> createPatternInvocation(corePatterns.get(CorePatternTypes.BWD), d, false));
   }

   private void createPatternInvocation(Pattern invoking, Pattern invoked, boolean positive)
   {
      PatternInvocationConstraint pic = SpecificationFactory.eINSTANCE.createPatternInvocationConstraint();
      pic.setInvokedPattern(invoked);
      pic.setPositive(positive);
      invoking.getBodies().get(0).getConstraints().add(pic);

      invoked.getSymbolicParameters().stream().map(p -> CompilerUtil.createConstraintParameter(CompilerUtil.getEMFVariable(invoking, p.getName())))
            .forEach(p -> pic.getParameters().add(p));

   }

   private TGGRule createRuleWithDECNACs(TGGRule rule)
   {

      TGGRule result = EcoreUtil.copy(rule);

      decNACAnalysis.createDecNacs(result);

      return result;
   }

   private void removeUserNACs(TGGRule result)
   {
      Set<ObjectVariable> negativeOVs = result.getObjectVariable().stream().filter(ov -> ov.getBindingSemantics().equals(BindingSemantics.NEGATIVE))
            .collect(Collectors.toSet());
      HashSet<LinkVariable> negativeLVs = new HashSet<>();
      negativeOVs.forEach(ov -> {
         negativeLVs.addAll(ov.getIncomingLink());
         negativeLVs.addAll(ov.getOutgoingLink());
         EcoreUtil.delete(ov);
      });
      negativeLVs
            .addAll(result.getLinkVariable().stream().filter(lv -> lv.getBindingSemantics().equals(BindingSemantics.NEGATIVE)).collect(Collectors.toSet()));
      negativeLVs.forEach(lv -> EcoreUtil.delete(lv));
   }

}
