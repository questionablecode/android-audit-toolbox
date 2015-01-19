package com.qc.audit.analyzers;

import com.ensoftcorp.atlas.java.core.query.Attr.Edge;
import com.ensoftcorp.atlas.java.core.query.Attr.Node;
import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.atlas.java.core.script.CommonQueries;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

/**
 * Analyzes uses of Java native code in an application
 * TODO: Consider native fields and classes
 * @author Ben Holland
 */
public class NativeCodeUsage extends Analyzer {

	@Override
	public String getName() {
		return "Native Code Usage";
	}
	
	@Override
	public String getDescription() {
		return "Discovers uses of the Java Native Interface (JNI).";
	}
	
	@Override
	public String[] getAssumptions() {
		return new String[]{"Uses of native code are limited to method calls to app methods with the \"native\" keyword.",
							"Uses of native code are only made through direct method calls."};
	}
	
	@Override
	protected Q evaluateEnvelope() {
		Q nativeMethods = appContext.nodesTaggedWithAll(Node.METHOD, Node.IS_NATIVE);
		return CommonQueries.interactions(context, appContext, nativeMethods, Edge.CALL);
	}

}
