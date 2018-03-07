package org.moflon.gt.mosl.controlflow.language.ui.wizards

import org.eclipse.core.resources.IFile
import org.eclipse.core.runtime.IPath
import org.moflon.gt.mosl.ide.ui.wizards.WizardFileTemplate

class ControlFileTemplate extends WizardFileTemplate {
	
	override setContent(IFile file) {
		val project = file.project
		val path = file.location.makeRelativeTo(project.location).removeFileExtension
		val packageName = convertPathToPackageNotation(path)
		val content = createTemplate(packageName)
		save(file, content)		
	}
	
	def convertPathToPackageNotation(IPath path){
		val pathString = path.toString
		pathString.replaceAll("/", "\\.")
	}
	
	def String createImportText(){
			
		'''
		«FOR epackage : epackageImports»
			import "«epackage.nsURI»"
		«ENDFOR»
		'''
	}
	
	def String createTemplate(String packageName){
		'''
		/*
		* import section
		*
		* for imports from plugins use:
		* import "platform:/plugin/something/ecoreFile.ecore"
		*
		*
		* for imports in your current eclipse runtime use:
		* import "platform:/resource/something/ecoreFile.ecore"
		*/
		«createImportText»
		
		/*
		* using session
		*
		* for every defined Patternfile you want to use:
		* using "platform:/.../patternFile.mpt"
		*/
		
		package «packageName»
		
		// EClass declaration
		'''
	}
}