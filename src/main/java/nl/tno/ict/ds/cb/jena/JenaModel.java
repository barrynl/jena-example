package nl.tno.ict.ds.cb.jena;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class JenaModel {

	public static void main(String[] args) {

		Model m = ModelFactory.createDefaultModel();
		m.read(JenaModel.class.getResourceAsStream("/data.ttl"), null, "turtle");

		assert !m.isEmpty();

		QueryExecution qe = QueryExecutionFactory.create("SELECT  *\n" + "WHERE\n"
				+ "  { ?kb   a                     <https://www.tno.nl/energy/ontology/interconnect#KnowledgeBase> ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasName>  ?name ;\n "
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasDescription>  ?description ;\n "
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasKnowledgeInteraction>  ?ki .\n"
				+ "    ?ki   a                     ?kiType ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#isMeta>  ?isMeta ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasCommunicativeAct>  ?act .\n"
				+ "    ?act  a                     <https://www.tno.nl/energy/ontology/interconnect#CommunicativeAct> ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasRequirement>  ?req ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasSatisfaction>  ?sat .\n"
				+ "    ?req  a                     ?reqType .\n" + "    ?sat  a                     ?satType .\n"
				+ "    ?ki   <https://www.tno.nl/energy/ontology/interconnect#hasGraphPattern>  ?gp ;\n"
				+ "          ?patternType          ?gp .\n"
				+ "    ?gp   a                     <https://www.tno.nl/energy/ontology/interconnect#GraphPattern> ;\n"
				+ "          <https://www.tno.nl/energy/ontology/interconnect#hasPattern>  ?pattern\n" + "  }"
				, m);

//		QueryExecution qe = QueryExecutionFactory.create("SELECT * WHERE { ?s ?p ?o }", m);

		ResultSet rs = qe.execSelect();

		System.out.println("--------------");
		while (rs.hasNext()) {
			System.out.println(rs.next());
		}
		System.out.println("--------------");
	}

}
