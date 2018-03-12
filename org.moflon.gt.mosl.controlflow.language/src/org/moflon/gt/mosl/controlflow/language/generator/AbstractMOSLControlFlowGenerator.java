package org.moflon.gt.mosl.controlflow.language.generator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.moflon.core.xtext.scoping.utils.MOSLScopeUtil;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.EClassDef;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.GraphTransformationControlFlowFile;

public abstract class AbstractMOSLControlFlowGenerator extends AbstractGenerator
{

   @Override
   public void doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context)
   {
      GraphTransformationControlFlowFile gtcf = GraphTransformationControlFlowFile.class.cast(input.getContents().get(0));
      Set<EPackage> oldEPackages = gtcf.getEClasses().parallelStream().map(eclassDef -> eclassDef.getName().getEPackage()).collect(Collectors.toSet());
      gtcf.getEClassifiers().addAll(gtcf.getEClasses().parallelStream().map(this::moveEOperations).collect(Collectors.toList()));
      gtcf.getEClasses().clear();
      List<EClassifier> classes = oldEPackages.parallelStream().flatMap(ePackage -> ePackage.getEClassifiers().parallelStream().filter(eClassifier -> !gtcf.getEClassifiers().contains(eClassifier))).collect(Collectors.toList());
      gtcf.getEClassifiers().addAll(classes);
      update(gtcf);
      URI uri = createURI(input, "model/generated", input.getURI().toString().split("/")[2]+ ".ecore");
      MOSLScopeUtil.getInstance().saveToResource(uri, input.getResourceSet(), gtcf);
   }
   
   private void update(EPackage ePackage) {
	   Set<EClassifier> classifiers = ePackage.getEClassifiers().parallelStream().collect(Collectors.toSet());
	   List<ETypedElement> typedElements = EcoreUtil2.eAllOfType(ePackage, ETypedElement.class).parallelStream().filter(eType -> eType.getEType() != null && !classifiers.contains(eType.getEType())).collect(Collectors.toList());
	   typedElements.parallelStream().forEach(typedElement -> updateETypes(typedElement, classifiers));
	   
	   List<EReference> references = ePackage.getEClassifiers().parallelStream().filter(EClass.class::isInstance).flatMap(eClass -> EClass.class.cast(eClass).getEReferences().stream()).collect(Collectors.toList());
	   references.parallelStream().filter(ref -> ref.getEOpposite() != null).forEach(ref -> update(ref, references));
   }
   
   private void update(EReference reference, Collection<EReference> others) {
	   Optional<EReference> referenceMonad = others.parallelStream().filter(ref -> ref.getName().equals(reference.getEOpposite().getName())).findFirst();
	   if(referenceMonad.isPresent())
		   reference.setEOpposite(referenceMonad.get());
   }
   
   private void updateETypes(ETypedElement typedElement, Collection<EClassifier> classifiers) {
	   Optional<EClassifier> classifierMonad = classifiers.parallelStream().filter(eClassifier -> eClassifier.getName().equals(typedElement.getEType().getName())).findFirst();
	   if(classifierMonad.isPresent()) {
		   typedElement.setEType(classifierMonad.get());
	   }
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
