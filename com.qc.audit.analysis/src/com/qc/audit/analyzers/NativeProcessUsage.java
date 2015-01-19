package com.qc.audit.analyzers;

import com.ensoftcorp.atlas.java.core.query.Attr.Edge;
import com.ensoftcorp.atlas.java.core.query.Attr.Node;
import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.atlas.java.core.script.Common;
import com.ensoftcorp.atlas.java.core.script.CommonQueries;
import com.ensoftcorp.atlas.java.core.script.CommonQueries.TraversalDirection;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

/**
 * Analyzes uses of native processes in an application
 * @author Ben Holland
 */
public class NativeProcessUsage extends Analyzer {

	@Override
	public String getName() {
		return "Native Process Usage";
	}
	
	@Override
	public String getDescription() {
		return "Discovers uses of the native processes.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"Native processes can only be launched by the Java Process, ProcessBuilder, and Runtime.exec(...) APIs.", 
							"Uses of native processes APIs are only made through direct method calls."};
	}

	@Override
	protected Q evaluateEnvelope() {
		// select the process APIs
		Q processAPIs = Common.typeSelect("java.lang", "Process").union(
				Common.typeSelect("java.lang", "ProcessBuilder"), 
				Common.typeSelect("java.lang", "Runtime"));
		
		// select all methods declared under the process APIs
		Q processAPIMethods = CommonQueries.declarations(context, processAPIs).nodesTaggedWithAny(Node.METHOD);
		
		// add overridden process API methods
		processAPIMethods = CommonQueries.overrides(context, processAPIMethods, TraversalDirection.FORWARD);
		
		// remove boring overridden Object methods (like toString())
		processAPIMethods = processAPIMethods.difference(SetDefinitions.objectMethodOverrides());
		
		// get the calls to the process API methods by the application
		return CommonQueries.interactions(context, appContext, processAPIMethods, Edge.CALL);
	}

}
