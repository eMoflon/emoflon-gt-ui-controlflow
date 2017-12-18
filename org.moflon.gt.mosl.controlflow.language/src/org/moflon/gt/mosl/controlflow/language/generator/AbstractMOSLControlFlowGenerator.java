package org.moflon.gt.mosl.controlflow.language.generator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.EClassDef;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.GraphTransformationControlFlowFile;
import org.moflon.ide.mosl.core.scoping.utils.MOSLScopeUtil;

public abstract class AbstractMOSLControlFlowGenerator extends AbstractGenerator
{

   @Override
   public void doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context)
   {
      GraphTransformationControlFlowFile gtcf = GraphTransformationControlFlowFile.class.cast(input.getContents().get(0));
      gtcf.getEClassifiers().addAll(gtcf.getEClasses().parallelStream().map(this::moveEOperations).collect(Collectors.toList()));
      gtcf.getEClasses().clear();
      URI uri = createURI(input, "model/generated", input.getURI().toString().split("/")[2]+ ".ecore");
      MOSLScopeUtil.getInstance().saveToResource(uri, input.getResourceSet(), gtcf);
   }
   
   private EClass moveEOperations(EClassDef eClassDef){
      Collection<EOperation> toDelete =eClassDef.getName().getEOperations().parallelStream().filter(eop -> exist(eop, eClassDef.getOperations())).collect(Collectors.toList());
      eClassDef.getName().getEOperations().removeAll(toDelete);
      eClassDef.getName().getEOperations().addAll(eClassDef.getOperations());
      return eClassDef.getName();
   }
   
   private boolean exist(EOperation eOperation, List<? extends EOperation> others){
      return others.parallelStream().anyMatch(other -> other.getName().equals(eOperation.getName()) && sameTypes(other.getEParameters(), eOperation.getEParameters()));
   }
   
   private boolean sameTypes(List<EParameter> parameters1, List<EParameter> parameters2){
      if(parameters1.size() != parameters2.size())
         return false;
      for(int index = 0; index < parameters1.size(); ++index){
         if(!parameters1.get(index).getEType().equals(parameters2.get(index).getEType()))
            return false;
      }
         
      return true;   
   }
   
   protected URI createURI(Resource resource, String folder, String fileName){
      URI oldUri = resource.getURI();
      List<String> parts = Arrays.asList(oldUri.toString().split("/")).subList(0, 3);
      return URI.createURI(parts.stream().reduce("", (a,b) -> a + b + "/")+folder+"/"+fileName);
   }

}
