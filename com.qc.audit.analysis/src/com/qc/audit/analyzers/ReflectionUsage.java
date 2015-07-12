package com.qc.audit.analyzers;

import com.ensoftcorp.atlas.core.query.Attr.Edge;
import com.ensoftcorp.atlas.core.query.Attr.Node;
import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.CommonQueries.TraversalDirection;
import com.ensoftcorp.atlas.java.core.script.Common;
import com.ensoftcorp.atlas.java.core.script.CommonQueries;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

/**
 * Analyzes uses of Java reflection in an application
 * @author Ben Holland
 */
public class ReflectionUsage extends Analyzer {

	@Override
	public String getName() {
		return "Reflection Usage";
	}
	
	@Override
	public String getDescription() {
		return "Discovers uses of reflection.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"Uses of reflection occur through APIs in the java.lang.reflect package.",
							"Uses of reflection occur through through various methods in java.lang.Class", 
							"Uses of class loaders are not considered uses of reflection",
							"Uses of reflection APIs are only made through direct method calls."};
	}

	@Override
	protected Q evaluateEnvelope() {
		// get reflection APIs
		Q reflectionAPIs = CommonQueries.declarations(context, Common.pkg("java.lang.reflect"));
		reflectionAPIs = reflectionAPIs.union(Common.typeSelect("java.lang", "Class"));
		
		// get reflection API's methods
		Q reflectionAPIMethods = CommonQueries.declarations(context, reflectionAPIs).nodesTaggedWithAny(Node.METHOD);
		
		// add overridden reflection API methods
		reflectionAPIMethods = CommonQueries.overrides(context, reflectionAPIMethods, TraversalDirection.FORWARD);
		
		// remove boring overridden Object methods (like toString())
		reflectionAPIMethods = reflectionAPIMethods.difference(SetDefinitions.objectMethodOverrides());
		
		// get the calls to the reflection API methods by the application
		return CommonQueries.interactions(context, appContext, reflectionAPIMethods, Edge.CALL);
	}

}
