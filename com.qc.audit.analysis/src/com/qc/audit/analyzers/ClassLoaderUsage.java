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
 * Analyzes uses of Java class loaders in an application
 * @author Ben Holland
 */
public class ClassLoaderUsage extends Analyzer {

	@Override
	public String getName() {
		return "Class Loader Usage";
	}
	
	@Override
	public String getDescription() {
		return "Discovers uses of the class loaders.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"Uses of class loader APIs are only made through direct method calls."};
	}

	@Override
	protected Q evaluateEnvelope() {
		// get all ClassLoader implementations (getting type hierarchy here instead of just base types 
		// and overrides because the custom ClassLoader methods may contain valuable contextual information)
		Q classLoaderAPIs = CommonQueries.typeHierarchy(context, Common.typeSelect("java.lang", "ClassLoader"), TraversalDirection.REVERSE);
		
		// get the class loader API methods
		Q classLoaderMethods = CommonQueries.declarations(context, classLoaderAPIs, TraversalDirection.FORWARD).nodesTaggedWithAny(Node.METHOD);
		
		// remove the boring class loader methods inherited from Object
		classLoaderMethods = classLoaderMethods.difference(SetDefinitions.objectMethodOverrides());
		
		// get the calls to the class loader API methods by the application
		return CommonQueries.interactions(context, appContext, classLoaderMethods, Edge.CALL);
	}

}
