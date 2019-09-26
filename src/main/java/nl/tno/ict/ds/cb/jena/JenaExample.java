package nl.tno.ict.ds.cb.jena;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

public class JenaExample {

	public static void main(String[] args) {

		Dataset d = DatasetFactory.assemble("conf.ttl");

		System.out.println("Dataset: " + d);

		UpdateRequest ur = UpdateFactory.create("	PREFIX : <http://192.168.99.100/pizza/>\n"
				+ "	PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "	PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "	PREFIX pizza: <http://192.168.99.100/pizza/>\n" + "\n" + "	INSERT DATA {\n"
				+ "			:test rdf:type pizza:Pizza .\n"
				+ "			:test pizza:hasCountryOfOrigin <http://192.168.99.100/pizza/Italy> .\n"
				+ "			:test pizza:hasBase <http://192.168.99.100/pizza/23_base> .\n"
				+ "			:test pizza:hasSpiciness <http://192.168.99.100/pizza/23_flauw> .\n"
				+ "			:test pizza:hasTopping :18-Tomaat .\n"
				+ "			:test pizza:hasTopping :18-mozzerella .\n"
				+ "			:test pizza:hasTopping :18-rundergehakt .\n" + "	}");

		UpdateProcessor up = UpdateExecutionFactory.create(ur, d);
		up.execute();

		QueryExecution qe = QueryExecutionFactory.create(
				QueryFactory.create("SELECT ?subject ?predicate ?object WHERE { ?subject ?predicate ?object .}"), d);

		ResultSet results = qe.execSelect();

		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();
			System.out.println("Solution: " + soln);
		}
		
		System.out.println("--------------------------------------------------------------------------");

		qe.close();

		System.out.println("Default Model: " + d.getDefaultModel());
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("Onto Model: " + d.getNamedModel("https://www.tno.nl/agrifood/graph/pizza/onto"));
		System.out.println("--------------------------------------------------------------------------");
		Model m = d.getNamedModel("https://www.tno.nl/agrifood/graph/pizza/data");
		System.out.println("Data Model: " + m);
		StmtIterator i = m.listStatements();
		while (i.hasNext()) {
			System.out.println("Statement: " + i.next());
		}

	}
}
