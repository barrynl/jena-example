package nl.tno.ict.ds.cb.jena;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class JenaExample {

	public static void main(String[] args) {

		Dataset d = DatasetFactory.assemble("conf.ttl");

		// System.out.println("Dataset: " + d);

		QueryExecution qe = QueryExecutionFactory.create(QueryFactory.create(
				"SELECT ?subject WHERE { ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Class> .}"),
				d);

		ResultSet results = qe.execSelect();

		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();
			System.out.println("Solution: " + soln);
		}

		// System.out.println("Model: " + d.getDefaultModel());

	}

}
