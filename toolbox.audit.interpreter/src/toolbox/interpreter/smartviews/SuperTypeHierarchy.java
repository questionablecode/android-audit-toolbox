package toolbox.interpreter.smartviews;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ensoftcorp.atlas.java.core.query.Q;
import com.ensoftcorp.atlas.java.core.script.CommonQueries;
import com.ensoftcorp.atlas.java.core.script.CommonQueries.TraversalDirection;
import com.ensoftcorp.atlas.java.core.script.StyledResult;
import com.ensoftcorp.atlas.java.ui.scripts.selections.AtlasSmartViewScript;
import com.ensoftcorp.atlas.java.ui.selection.event.IAtlasSelectionEvent;

public class SuperTypeHierarchy implements AtlasSmartViewScript{
	
	@Override
	public String getTitle() {
		return "Supertype Hierarchy";
	}

	@Override
	public void indexChanged(IProgressMonitor monitor) {}

	@Override
	public void indexCleared() {}

	@Override
	public StyledResult selectionChanged(IAtlasSelectionEvent input) {
		Q interpretedSelection = input.getSelection();
		Q res = CommonQueries.typeHierarchy(interpretedSelection, TraversalDirection.FORWARD);
		return new StyledResult(res);
	}
	
}
