package de.unirostock.sems.m2cat.meth;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.m2cat.graph.GraphDbManager;
import de.unirostock.sems.m2cat.graph.GraphDbManager.Response;
import de.unirostock.sems.m2cat.graph.GraphModel;
import de.unirostock.sems.m2cat.graph.GraphModelDocument;
import de.unirostock.sems.m2cat.graph.GraphSedml;
import de.unirostock.sems.m2cat.graph.GraphSedmlDocument;

/**
 * Hello world!
 *
 */
public class Searcher 
{
	
	private boolean dbErr;
	
	/**
	 * Instantiates a new searcher.
	 */
	public Searcher ()
	{
		dbErr = false;
	}
	
	/**
	 * Checks for a DB err.
	 *
	 * @return true, if successful
	 */
	public boolean hasDbErr ()
	{
		return dbErr;
	}
	
	/**
	 * Search for annotation.
	 *
	 * @param search the search
	 * @return list of found docs
	 * @throws IOException the IO exception
	 */
	public List<GraphModelDocument> searchForAnnotation (String search) throws IOException
	{
		
		/*
curl -X POST \
		http://sems.uni-rostock.de:7474/db/data/cypher/ \
			-H "Content-Type: application/json" \
			-d '{"query":"start res=node:annotationIndex({SEARCH}) MATCH res<-[rel:is]-(:ANNOTATION)-->(s)-[:BELONGS_TO]->(m:MODEL)-[:BELONGS_TO]->(d:DOCUMENT) return distinct(m),d limit 20",
			"params":{"SEARCH":"RESOURCETEXT:Adenosine phosphate"}}'


curl -X POST \
		http://sems.uni-rostock.de:7474/db/data/cypher/ \
			-H "Content-Type: application/json" \
			-d '{"query":"start res=node:annotationIndex({SEARCH}) return res limit 20",
			"params":{"SEARCH":"RESOURCETEXT:\"Adenosine phosphate\""}}'*/

		
		String qry = 
			"START res=node:annotationIndex({SEARCH}) "
			+ "MATCH res<-[rel:is]-(:ANNOTATION)-->(s)-[:BELONGS_TO]->(m:MODEL)-[:BELONGS_TO]->(d:DOCUMENT) "
			+ "return distinct(m),d limit 20";
		
		JSONObject json = new JSONObject ();
		json.put ("SEARCH", "RESOURCETEXT:(\"" + search + "\")");
		
		Response res = GraphDbManager.cypher (qry, json);
		if (res.status == 200)
			return parseSearchResponse (res);
		else
		{
			LOGGER.error ("couldn't find documents");
			dbErr = true;
			return new ArrayList<GraphModelDocument> ();
		}
	}

	/**
	 * Search for author.
	 *
	 * @param search the search
	 * @return the list of graph model documents
	 * @throws IOException the IO exception
	 */
	public List<GraphModelDocument> searchForAuthor (String search) throws IOException
	{

		String qry = 
			"MATCH (d:DOCUMENT)-[HAS_MODEL]->(l:MODEL)-[HAS_ANNOTATION]->(k:ANNOTATION)-[HAS_PUBLICATION]->(m:PUBLICATION)-[HAS_AUTHOR]->(n:PERSON)"
			+ "WHERE n.FAMILYNAME =~ {SEARCH} OR n.GIVENNAME =~ {SEARCH} OR n.ORGANIZATION =~ {SEARCH}"
			+ "return distinct(m),d limit 20";
		
		JSONObject json = new JSONObject ();
		json.put ("SEARCH", search);
		
		Response res = GraphDbManager.cypher (qry, json);
		if (res.status == 200)
			return parseSearchResponse (res);
		else
		{
			LOGGER.error ("couldn't find documents");
			dbErr = true;
			return new ArrayList<GraphModelDocument> ();
		}
	}

	/**
	 * Search for model id.
	 *
	 * @param id the id
	 * @return list of found docs
	 * @throws IOException the IO exception
	 */
	public List<GraphModelDocument> searchForModelId (int id) throws IOException
	{
		String qry = 
			"START d=node({ID}) "
			+ "MATCH (m:MODEL)-[:BELONGS_TO]->(d:DOCUMENT) "
			+ "return distinct(m),d limit 20";
		
		JSONObject json = new JSONObject ();
		json.put ("ID", id);
		
		Response res = GraphDbManager.cypher (qry, json);
		if (res.status == 200)
			return parseSearchResponse (res);
		else
		{
			LOGGER.error ("couldn't find documents");
			dbErr = true;
			return new ArrayList<GraphModelDocument> ();
		}
	}
	
	
	
	/**
	 * Find associated simulations.
	 *
	 * @param doc the doc
	 * @throws IOException the IO exception
	 */
	public void findAssociatedSimulations (GraphModelDocument doc) throws IOException
	{
		String qry = 
			"start known=node({NODEID}) "
			+ "match (known)-->(:MODEL)<--(:SEDML_MODELREFERENCE)<--(sedml:SEDML)<--(doc:DOCUMENT) "
			+ "return sedml,doc";
		
		
		
		JSONObject json = new JSONObject ();
		json.put ("NODEID", doc.getId ());
		
		Response res = GraphDbManager.cypher (qry, json);
		if (res.status == 200)
			parseSedmlResponse (res, doc);
		else
		{
			LOGGER.error ("couldn't find sedml descriptions");
			dbErr = true;
		}
	}
	
	
	/**
	 * Parses the search response.
	 *
	 * @param res the res
	 * @param doc the doc
	 */
	public void parseSedmlResponse (Response res, GraphModelDocument doc)
	{
		JSONObject json = (JSONObject) JSONValue.parse(res.content);
		//LOGGER.debug(json);
		JSONArray rows = (JSONArray) json.get ("data");
		
		
		for (Object o : rows)
		{
			JSONArray row = (JSONArray) o;
	
			GraphSedml sedml = new GraphSedml ((JSONObject) row.get (0));
			GraphSedmlDocument sedmlDoc = new GraphSedmlDocument ((JSONObject) row.get (1), sedml);
			
			doc.addSimulationDescription (sedmlDoc);
			
		}
	}
		
	
	/**
	 * Parses the search response.
	 *
	 * @param res the res
	 * @return the list of graph documents
	 */
	public List<GraphModelDocument> parseSearchResponse (Response res)
	{
		JSONObject json = (JSONObject) JSONValue.parse(res.content);
		//LOGGER.debug (json);
		JSONArray rows = (JSONArray) json.get ("data");
		
		List<GraphModelDocument> docs = new ArrayList<GraphModelDocument> ();
		
		for (Object o : rows)
		{
			JSONArray row = (JSONArray) o;
			
			GraphModel model = new GraphModel ((JSONObject) row.get (0));
			GraphModelDocument doc = new GraphModelDocument ((JSONObject) row.get (1), model);
			docs.add (doc);
		}
		return docs;
	}
	
	/**
	 * Gets the further resources.
	 *
	 * @param doc the doc
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public void retrieveResources (GraphModelDocument doc) throws ClientProtocolException, IOException, URISyntaxException
	{
		doc.retrieveModelFiles ();
		doc.retrieveAdditionalResources ();
	}
	
	
	/**
	 * Search.
	 *
	 * @param term the term
	 * @return the list of graph model documents
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public List<GraphModelDocument> searchForId (int term) throws ClientProtocolException, IOException, URISyntaxException
	{
		List<GraphModelDocument> docs = searchForModelId (term);
		
		for (GraphModelDocument doc : docs)
  	{
  		findAssociatedSimulations (doc);
  		retrieveResources (doc);
  	}
		return docs;
	}
	
	
	/**
	 * Search.
	 *
	 * @param term the term
	 * @return the list of graph model documents
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public List<GraphModelDocument> search (String term) throws ClientProtocolException, IOException, URISyntaxException
	{
		List<GraphModelDocument> docs = searchForAnnotation (term);
		docs.addAll (searchForAuthor (term)); 
		for (GraphModelDocument doc : docs)
  	{
  		findAssociatedSimulations (doc);
  		retrieveResources (doc);
  	}
		return docs;
	}
	
    /**
     * The main method.
     *
     * @param args the args
     * @throws IOException the IO exception
     * @throws URISyntaxException the URI syntax exception
     */
    public static void main( String[] args ) throws IOException, URISyntaxException
    {
    	LOGGER.setMinLevel (LOGGER.DEBUG);
    	//LOGGER.setLogStackTrace (true);
    	
    	File masymostest = new File ("/tmp/masymostest");
    	
    	Searcher s = new Searcher ();
    	List<GraphModelDocument> docs = s.searchForAnnotation ("ubiquitin");
    	
    	for (GraphModelDocument doc : docs)
    	{
    		s.findAssociatedSimulations (doc);
    		s.retrieveResources (doc);
    		LOGGER.debug (doc);
    		/*File f = File.createTempFile ("m2cat", "combineArchive", masymostest);
    		f.delete ();
    		try
				{
					doc.createCombineArchive (f);
					LOGGER.debug ("--- ca in " + f.getAbsolutePath ());
				}
				catch (Exception e)
				{
					LOGGER.error (e, "couldn't create ca from doc. ", f);
				}*/
    	}
    	
    	
    	
    	
    	
    }
}
