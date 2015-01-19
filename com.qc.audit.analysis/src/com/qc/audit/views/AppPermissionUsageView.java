package com.qc.audit.views;

import com.ensoftcorp.atlas.java.core.query.Attr.Node;
import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.open.android.essentials.views.PermissionUsageView;
import com.qc.adlib.Libraries;

/**
 * An Eclipse view for searching and viewing apply permission mapping values in the Atlas index
 * @author Ben Holland
 */
public class AppPermissionUsageView extends PermissionUsageView {
	public AppPermissionUsageView() {}
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.qc.audit.views.AppPermissionUsageView";
	
	/**
	 * Filters out callsite results (by default, unless overridden, this does no filtering)
	 * @return
	 */
	@Override
	protected Q getCallsiteFilter() {
		return Libraries.getLibraryDeclarations().nodesTaggedWithAny(Node.CONTROL_FLOW);
	}
}