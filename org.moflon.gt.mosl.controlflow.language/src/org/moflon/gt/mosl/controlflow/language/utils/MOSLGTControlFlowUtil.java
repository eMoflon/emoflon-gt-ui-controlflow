package org.moflon.gt.mosl.controlflow.language.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.emoflon.ibex.gt.editor.gT.EditorGTFile;
import org.moflon.core.xtext.exceptions.CannotFindScopeException;
import org.moflon.core.xtext.scoping.utils.MOSLScopeUtil;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.GraphTransformationControlFlowFile;

public class MOSLGTControlFlowUtil {
	public static IScope getScopeByPattern(EObject context, EReference reference,
			Map<GraphTransformationControlFlowFile, List<EditorGTFile>> resolvingCache)
			throws CannotFindScopeException {
		GraphTransformationControlFlowFile gtf = MOSLScopeUtil.getInstance().getRootObject(context,
				GraphTransformationControlFlowFile.class);
		List<EditorGTFile> patternFiles = resolvingCache.getOrDefault(gtf, new ArrayList<>()).stream()
				.collect(Collectors.toList());
		EClassifier type = reference.getEType();
		List<? extends EObject> candidates = patternFiles.parallelStream()
				.map(ptFile -> getElements(ptFile.eAllContents(), type)).flatMap(lst -> lst.stream())
				.collect(Collectors.toList());
		return Scopes.scopeFor(candidates);
	}

	private static <O extends EObject> List<O> getElements(Iterator<O> iterator, EClassifier type) {
		List<O> lst = new ArrayList<>();
		iterator.forEachRemaining(lst::add);
		return lst.stream().filter(o -> o.eClass().equals(type)).collect(Collectors.toList());
	}

	public static void resolvePatterns(EObject context,
			Map<GraphTransformationControlFlowFile, List<EditorGTFile>> resolvingCache, ResourceSet resSet) {
		GraphTransformationControlFlowFile cfFile = MOSLScopeUtil.getInstance().getRootObject(context,
				GraphTransformationControlFlowFile.class);
		if (!resolvingCache.containsKey(cfFile)) {
			Resource cfFileRes = cfFile.eResource();
			URI cfFileUri = cfFileRes.getURI();
			List<String> cfFileUriParts = Arrays.asList(cfFileUri.toString().split("/"));
			String cfFileURIStringPrefix = cfFileUriParts.stream().filter(part -> !part.endsWith(".mcf")).reduce("",
					(a, b) -> a + b + "/");

			List<URI> patternUris = cfFile.getIncludedPatterns().stream()
					.map(pattern -> URI.createURI(cfFileURIStringPrefix + pattern.getImportURI()))
					.collect(Collectors.toList());
			List<EditorGTFile> patternFiles = patternUris.stream()
					.map(uri -> MOSLScopeUtil.getInstance().getObjectFromResourceSet(uri, resSet, EditorGTFile.class))
					.collect(Collectors.toList());
			patternFiles.parallelStream().filter(EObject::eIsProxy).forEach(EcoreUtil::resolveAll);
			resolvingCache.put(cfFile, patternFiles);
		}
	}

}
