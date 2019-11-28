/**
 * 
 */
package nl.tno.ict.ds.cb.jena;

import static org.apache.jena.atlas.lib.tuple.TupleFactory.tuple;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.atlas.lib.tuple.Tuple;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.sparql.lang.arq.ARQParser;
import org.apache.jena.sparql.lang.arq.ParseException;
import org.apache.jena.sparql.resultset.ResultSetCompare;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.util.IsoMatcher;
import org.apache.jena.sparql.util.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nouwtb
 *
 */
public class JenaIsomorphTest {

	private static final Logger LOG = LoggerFactory.getLogger(JenaIsomorphTest.class);

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		String patternString1 = "?a <http://www.tno.nl/test1> ?b . ?b <http://www.tno.nl/test2> ?a .";
		String patternString2 = "?a <http://www.tno.nl/test1> ?b . ?a <http://www.tno.nl/test2> ?b .";

		ElementPathBlock pattern1 = parse(patternString1);
		ElementPathBlock pattern2 = parse(patternString2);

		LOG.info("Step 1: string to element path block");
		LOG.info("pattern1: {}", pattern1);
		LOG.info("pattern2: {}", pattern2);
		LOG.info("----------------------------------------");

		Graph graph1 = convertToJenaGraphWithBlankNodes(pattern1);
		Graph graph2 = convertToJenaGraphWithBlankNodes(pattern2);
		
		LOG.info("Step 2: element path block to Jena graph");
		LOG.info("graph1: {}", graph1);
		LOG.info("graph2: {}", graph2);
		LOG.info("----------------------------------------");

		IsoMatcher match = new IsoMatcher(tuplesTriples(graph1.find()), tuplesTriples(graph2.find()),
				new ResultSetCompare.BNodeIso(NodeUtils.sameValue));

		LOG.info("Step 3: compare the two graphs (this should be false): {}", match.match());

	}

	/**
	 * Copied from IsoMatcher.tuplesTriples(...)
	 */
	private static List<Tuple<Node>> tuplesTriples(Iterator<Triple> iter) {
		List<Tuple<Node>> tuples = new ArrayList<>();
		for (; iter.hasNext();) {
			Triple t = iter.next();
			Tuple<Node> tuple = tuple(t.getSubject(), t.getPredicate(), t.getObject());
			tuples.add(tuple);
		}
		return tuples;
	}

	/**
	 * Parse string into ElementPathBlocks using ARQParser.
	 * 
	 * @param aPattern
	 * @return
	 * @throws ParseException
	 */
	private static ElementPathBlock parse(String aPattern) throws ParseException {
		ARQParser parser = new ARQParser(new StringReader(aPattern));
		Element e = null;
		e = parser.GroupGraphPatternSub();
		LOG.debug("parsed knowledge: {}", e);
		ElementGroup eg = (ElementGroup) e;
		Element last = eg.getLast();

		if (!(last instanceof ElementPathBlock)) {
			LOG.error("This knowledge '{}' should be parseable to a ElementPathBlock", aPattern);
			throw new ParseException(
					"The knowledge should be parseable to a ARQ ElementPathBlock (i.e. a BasicGraphPattern in the SPARQL syntax specification)");
		}

		ElementPathBlock epb = (ElementPathBlock) eg.getLast();

		Iterator<TriplePath> pathIter = epb.patternElts();

		TriplePath tp;
		while (pathIter.hasNext()) {
			tp = pathIter.next();
		}

		return (ElementPathBlock) eg.getLast();
	}

	/**
	 * Convert the ElementPathBlocks into Jena graphs where the variables are
	 * replaced by blank nodes (with the same label).
	 * 
	 * @param epb
	 * @return
	 */
	private static Graph convertToJenaGraphWithBlankNodes(ElementPathBlock epb) {

		Graph g = GraphFactory.createGraphMem();

		List<TriplePath> triples = epb.getPattern().getList();
		for (TriplePath t : triples) {

			Node subject = t.getSubject();
			if (t.getSubject().isVariable()) {
				subject = NodeFactory.createBlankNode(t.getSubject().getName());
			}

			Node predicate = t.getPredicate();
			if (t.getPredicate().isVariable()) {
				predicate = NodeFactory.createBlankNode(t.getPredicate().getName());
			}

			Node object = t.getObject();
			if (t.getObject().isVariable()) {
				object = NodeFactory.createBlankNode(t.getObject().getName());
			}

			g.add(new Triple(subject, predicate, object));
		}

		return g;
	}

}
