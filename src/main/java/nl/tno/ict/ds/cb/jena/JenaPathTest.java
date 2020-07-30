package nl.tno.ict.ds.cb.jena;

import org.apache.jena.fuseki.main.FusekiServer;

public class JenaPathTest {
	public static void main(String[] args) {

		FusekiServer.Builder builder = FusekiServer.create();
//		builder.parseConfigFile("C:\\Users\\nouwtb\\git\\jena-example\\src\\test\\resources\\config.ttl");
//		builder.parseConfigFile("src\\test\\resources\\config.ttl");
//		builder.parseConfigFile("C:/Users/nouwtb/git/jena-example/src/test/resources/config.ttl");
		builder.parseConfigFile("src/test/resources/config.ttl");
	}
}
