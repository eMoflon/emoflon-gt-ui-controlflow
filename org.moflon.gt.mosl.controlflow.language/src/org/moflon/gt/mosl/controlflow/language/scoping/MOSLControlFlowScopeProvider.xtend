/*
 * generated by Xtext 2.11.0
 */
package org.moflon.gt.mosl.controlflow.language.scoping

import java.util.HashMap
import java.util.List
import org.apache.log4j.Logger
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EParameter
import org.eclipse.emf.ecore.EReference
import org.moflon.codegen.eclipse.CodeGeneratorPlugin
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.CalledPatternParameter
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.EClassDef
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.GraphTransformationControlFlowFile
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.MethodDec
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.ObjectVariableStatement
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.PatternReference
import org.moflon.gt.mosl.controlflow.language.utils.MOSLGTControlFlowUtil
import org.moflon.gt.mosl.pattern.language.moslPattern.GraphTransformationPatternFile
import org.moflon.ide.mosl.core.exceptions.CannotFindScopeException
import org.moflon.ide.mosl.core.scoping.ScopeProviderHelper
import org.moflon.ide.mosl.core.scoping.utils.MOSLScopeUtil

/**
 * This class contains custom scoping description.
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class MOSLControlFlowScopeProvider extends AbstractMOSLControlFlowScopeProvider {
	private ScopeProviderHelper<EPackage> scopeEPackageHelper = new ScopeProviderHelper()
	private var resolvingCache = new HashMap<GraphTransformationControlFlowFile, List<GraphTransformationPatternFile>>();

	private Logger log = Logger.getLogger(MOSLControlFlowScopeProvider.getClass());

	override getScope(EObject context, EReference reference) {
	MOSLGTControlFlowUtil.instance.resolvePatterns(context, resolvingCache, scopeEPackageHelper.resourceSet)
	try{
		if(searchForEClass(context,reference)){
			return getScopeByType(context, EClass)
		}
		else if(searchForEClassifier(context,reference)){
			return getScopeByType(context, EClassifier)
		}
		else if(searchForPattern(context))
			return MOSLGTControlFlowUtil.instance.getScopeByPattern(context,reference, resolvingCache)
	}catch (CannotFindScopeException e){
		log.debug("Cannot find Scope",e)
	}
		super.getScope(context, reference);
	}

	def boolean searchForCalledPatternParameter(EObject context, EReference reference) {
		return context instanceof CalledPatternParameter;
	}


	def boolean searchForPattern(EObject context) {
		return context instanceof PatternReference
	}

	def getScopeByType(EObject context, Class<? extends EObject> type)throws CannotFindScopeException{
		val set = scopeEPackageHelper.resourceSet
		CodeGeneratorPlugin.createPluginToResourceMapping(set);
		var gtf = MOSLScopeUtil.instance.getRootObject(context, GraphTransformationControlFlowFile)//getGraphTransformationControlFlowFile(context)
		var uris = gtf.imports.map[importValue | URI.createURI(importValue.name)];
		return scopeEPackageHelper.createScope(uris, EPackage, type);
	}

	def boolean searchForEClass(EObject context, EReference reference){
		return context instanceof EClassDef
	}

	def boolean searchForEClassifier(EObject context, EReference reference){
		return context instanceof MethodDec ||  context instanceof ObjectVariableStatement
		|| (context instanceof EParameter && reference.name.equals("eType"))
	}
}
