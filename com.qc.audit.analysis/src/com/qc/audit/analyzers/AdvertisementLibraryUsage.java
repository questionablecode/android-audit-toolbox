package com.qc.audit.analyzers;

import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.core.db.set.AtlasSet;
import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.atlas.java.core.query.Attr.Edge;
import com.ensoftcorp.atlas.java.core.query.Attr.Node;
import com.ensoftcorp.atlas.java.core.script.Common;
import com.ensoftcorp.atlas.java.core.script.CommonQueries;
import com.ensoftcorp.atlas.java.core.script.CommonQueries.TraversalDirection;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;
import com.qc.adlib.android.AdLibrary;

/**
 * Analyzes uses of Android advertisement library uses
 * @author Ben Holland
 */
public class AdvertisementLibraryUsage  extends Analyzer {

	@Override
	public String getName() {
		return "Android Advertisement Library Usage";
	}
	
	@Override
	public String getDescription() {
		return "Discovers uses of Android advertisement libraries.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"Uses of Android advertisement libraries are only made through direct method calls.", 
							"The Advertisement Library Toolbox contains a complete and accurate list of libraries."};
	}

	@Override
	protected Q evaluateEnvelope() {
		
		// get all the used advertisement library packages
		AtlasSet<GraphElement> adLibTypes = new AtlasHashSet<GraphElement>();
		for(AdLibrary adlib : AdLibrary.getAllAdvertisementLibraries()){
			adLibTypes.addAll(adlib.getTypes().eval().nodes());
		}
		
		// figure out a new app context so we can determine if the library is used
		Q adLibDeclarations = CommonQueries.declarations(context, Common.toQ(Common.toGraph(adLibTypes)), TraversalDirection.FORWARD);
		Q appWithoutAdLibs = appContext.difference(adLibDeclarations);
		
		// get the advertisement library methods
		Q adLibMethods = CommonQueries.declarations(context, Common.toQ(Common.toGraph(adLibTypes)), 
				TraversalDirection.FORWARD).nodesTaggedWithAny(Node.METHOD);
		
		// remove the boring class loader methods inherited from Object
		adLibMethods = adLibMethods.difference(SetDefinitions.objectMethodOverrides());
		
		// get the calls to the class loader API methods by the application
		return CommonQueries.interactions(context, appWithoutAdLibs, adLibMethods, Edge.CALL);
	}
	
}