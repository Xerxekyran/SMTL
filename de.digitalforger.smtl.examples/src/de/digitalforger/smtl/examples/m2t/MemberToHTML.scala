package de.digitalforger.smtl.examples.m2t

import de.digitalforger.examples.model.families.Families.FamiliesPackage
import de.digitalforger.smtl.m2t.TransformationM2T
import de.digitalforger.examples.model.families.Families.Family
import de.digitalforger.smtl.m2t.IM2TGenerator
import de.digitalforger.examples.model.families.Families.Member
import org.eclipse.emf.ecore.resource.Resource

object MemberToHTML {
	def main(args : Array[String]) : Unit = {
		// init the models
		FamiliesPackage.eINSTANCE.eClass()
		val inputFile = "file:../de.digitalforger.smtl.examples.model/resources/FamiliesToPersons/sample-Families.xmi"
		val familiesMetaModel = "http://www.digital-forger.de/examples/model/families"

		new TransformationM2T from familiesMetaModel using new HTMLGenerator("families.html") transform inputFile
	}
}

/**
 * An example of a generator for a model to text transformation
 * This generator builds an HTML file listing all members of the given families
 * 
 */
class HTMLGenerator(fileName : String) extends IM2TGenerator {

	/**
	 * @inheritDoc
	 */
	override def generate(resource : Resource) : Unit = {

		this createFile fileName withContent START +
			"<html>" + << + 
				"<head>" + << +
					"<title>Families member page</title>" + << +
					getCssDefinitions() + << +
				"</head>" + << +
				"<body>" + << +
					"<div id='page'>" + << +
						"<h1>My favourite Families</h1>" + << +
						resource.foreach(classOf[Family], getFamiliesAsHTML) + << +
					"</div>"+ << +
				"</body>" + << +
			"</html>" + <<
	}

	/**
	 * converts a familiy to an html representation
	 */
	def getFamiliesAsHTML(family : Family) : String = {
		START +
		"<div class='family'>" + << +
			"<b>Family: " + family.getLastName() + "</b>" + << +
			"<ul>" + family.foreach(classOf[Member], getMembersAsHTML) + "</ul>" + << +
		"</div>" + <<
	}

	/**
	 * converts a member to an html representation
	 */
	def getMembersAsHTML(member : Member) : String = {
		var ret = START
		ret += "<li>" 					
			
		ret += member.getFirstName()
		
		if(member.getFamilyMother() != null) ret += " <span class='familyType'>[mother]</span>"
		else if(member.getFamilyFather() != null) ret += " <span class='familyType'>[father]</span>"
		else if(member.getFamilySon() != null) ret += " <span class='familyType'>[son]</span>"
		else if(member.getFamilyDaughter() != null) ret += " <span class='familyType'>[daughter]</span>"
		
		ret += "</li>"
		
		ret
	}
	
	/**
	 * Returns the css definitions
	 */
	def getCssDefinitions() : String = {
		START +
		"<style type='text/css'>" + << +
		"	html, body { background-color: lightgrey; color: black; }" + << +
		"	#page { width : 400px; left:50%; margin-left:-200px; background-color: white; position : absolute; text-align: center;}" + << +
		"	div.family { border-bottom: dotted black 1px; }" + << +
		"	span.familyType { font-style : italic; }" + << +
		"</style>" + <<
	}
}