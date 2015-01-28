package com.qc.audit.analyzers;

import java.util.HashSet;
import java.util.Set;

import com.ensoftcorp.atlas.core.db.graph.GraphElement;
import com.ensoftcorp.atlas.core.db.set.AtlasHashSet;
import com.ensoftcorp.atlas.java.core.query.Attr.Node;
import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.atlas.java.core.script.Common;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class TopLevelPackages extends Analyzer {

	public static final int N = 4;
	
	@Override
	public String getName() {
		return "Finds Top Level Packages";
	}
	
	@Override
	public String getDescription() {
		return "Discovers top level packages.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"A top level package is a package defined by at most N subpackages."};
	}

	@Override
	protected Q evaluateEnvelope() {
		Set<String> packageNames = new HashSet<String>();
		for(GraphElement packageNode : appContext.nodesTaggedWithAny(Node.PACKAGE).eval().nodes()){
			String[] packageName = packageNode.getAttr(Node.NAME).toString().split("\\.");
			String topLevelPackageName = "";
			String prefix = "";
			for(int i=0; i<packageName.length && i < N; i++){
				topLevelPackageName += prefix + packageName[i];
				prefix = ".";
			}
			packageNames.add(topLevelPackageName);
		}
		
		AtlasHashSet<GraphElement> packages = new AtlasHashSet<GraphElement>();
		for(String packageName : packageNames){
			packages.add(appContext.pkg(packageName).eval().nodes().getFirst());
		}
		
		return Common.toQ(Common.toGraph(packages));
	}
	
	public static HashSet<String> getTLPs(){
		HashSet<String> packageNames = new HashSet<String>();
		for(GraphElement packageNode : SetDefinitions.app().nodesTaggedWithAny(Node.PACKAGE).eval().nodes()){
			String[] packageName = packageNode.getAttr(Node.NAME).toString().split("\\.");
			String topLevelPackageName = "";
			String prefix = "";
			for(int i=0; i<packageName.length && i < N; i++){
				topLevelPackageName += prefix + packageName[i];
				prefix = ".";
			}
			packageNames.add(topLevelPackageName);
		}
		
		return packageNames;
	}
	
}
