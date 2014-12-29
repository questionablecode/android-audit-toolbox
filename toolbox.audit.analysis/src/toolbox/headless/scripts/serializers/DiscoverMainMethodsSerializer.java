package toolbox.headless.scripts.serializers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ensoftcorp.atlas.core.db.graph.GraphElement;

import toolbox.analysis.Script;
import toolbox.library.util.FormattedSourceCorrespondence;

public class DiscoverMainMethodsSerializer extends Serializer {

	public static final String LOCATION = "location";
	
	@Override
	public void serialize(Document doc, Element scriptElement, Script script){
		super.serialize(doc, scriptElement, script);
		
		// for each main method list the source file and line numbers
		for(GraphElement mainMethod : envelope.eval().nodes()){
			Element locationElement = doc.createElement(LOCATION);
			FormattedSourceCorrespondence sc = FormattedSourceCorrespondence.getSourceCorrespondent(mainMethod);
			locationElement.setTextContent(sc.toString());
			scriptElement.appendChild(locationElement);
		}
	}
	
}
