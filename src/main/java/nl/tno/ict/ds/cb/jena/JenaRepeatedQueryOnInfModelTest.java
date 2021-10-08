package nl.tno.ict.ds.cb.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;

public class JenaRepeatedQueryOnInfModelTest {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Model m = ModelFactory.createDefaultModel();
		m.read(JenaRepeatedQueryOnInfModelTest.class.getResourceAsStream("/dataonto.ttl"), null, "turtle");

		assert !m.isEmpty();
		Reasoner reasoner = ReasonerRegistry.getRDFSSimpleReasoner();
		InfModel infModel = ModelFactory.createInfModel(reasoner.bind(m.getGraph()));
		assert !infModel.isEmpty();

		infModel.write(new FileOutputStream("before.ttl"), "turtle");

		for (int i = 0; i < 500; i++) {
			QueryExecution qe = QueryExecutionFactory.create(
					"SELECT * WHERE {<https://www.tno.nl/interconnect/kb1/interaction/ask/act/requirement> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?someClass . FILTER NOT EXISTS {<https://www.tno.nl/interconnect/kb3/interaction/answer/act/satisfaction> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?someClass .}}",
					infModel);

			ResultSet rs = qe.execSelect();

			
			// the correct answer is to find nothing!
			if (!rs.hasNext())
				System.out.println("Nothing found!");
			
			while (rs.hasNext()) {
				System.out.println("Statement: " + rs.next());
			}
		}
		
		infModel.write(new FileOutputStream("after.ttl"), "turtle");
	}

}
