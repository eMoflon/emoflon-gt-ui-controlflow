package org.moflon.gt.mosl.controlflow.language.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

public abstract class BaseMOSLControlFlowValidator extends AbstractMOSLControlFlowValidator {
	private Map<EClassifier, Collection<EClassifier>> typeCache = new HashMap<>();

	protected boolean isInstanceOf(EClassifier src, EClassifier trg) {
		Collection<? extends EClassifier> types = typeCache.getOrDefault(src, findAllTypes(src));
		return types.contains(trg);
	}

	private Collection<EClassifier> findAllTypes(EClassifier eClassifier) {
		Collection<EClassifier> types;
		if (eClassifier instanceof EClass) {
			types = new HashSet<>();
			Stack<EClass> stack = new Stack<>();
			stack.push(EClass.class.cast(eClassifier));
			types.add(eClassifier);
			while (!stack.empty()) {
				EClass current = stack.pop();
				List<EClass> superTypes = current.getEAllSuperTypes().parallelStream()
						.filter(eClass -> !types.contains(eClass)).collect(Collectors.toList());
				types.addAll(superTypes);
				stack.addAll(superTypes);
			}

		} else {
			types = Arrays.asList(eClassifier);
		}
		typeCache.put(eClassifier, types);
		return types;
	}
}
