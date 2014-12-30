package de.unirostock.sems.m2cat;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ModelTests
{

	/**
	 * 
	 */
	@Test
	public void testGetResources ()
	{
		 JSONObject json = (JSONObject) JSONValue.parse ("{"
		 	+ "\"labels\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/labels\","
		  + "\"outgoing_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/out\","
			+ "  \"data\" : {"
			+ "    \"DATABASEID\" : 931316394598,"
			+ "    \"VERSION\" : 4,"
			+ "    \"LEVEL\" : 2,"
			+ "    \"FILENAME\" : \"BIOMD0000000293.xml\","
			+ "    \"URI\" : \"E:\\temp\\latest\\curated\\BIOMD0000000293.xml\""
			+ "  },"
			+ " \"traverse\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/traverse/{returnType}\","
			+ "  \"all_typed_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/all/{-list|&|types}\","
			+ "  \"property\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/properties/{key}\","
			+ "	    \"self\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564\","
		    	+ "    \"properties\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/properties\","
		    	+ "    \"outgoing_typed_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/out/{-list|&|types}\","
		    	+ "    \"incoming_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/in\","
		    	+ "	    \"extensions\" : {"
		  	+ "    },"
		  	+ "    \"create_relationship\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships\","
		  	+ "    \"paged_traverse\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/paged/traverse/{returnType}{?pageSize,leaseTime}\","
		  	+ "	    \"all_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/all\","
		  	+ "	    \"incoming_typed_relationships\" : \"http://sems.uni-rostock.de:7474/db/data/node/224564/relationships/in/{-list|&|types}\""
		  	+ "	  }");
		
		
		GraphModelDocument doc = new GraphModelDocument (json, null);
		
		List<Resource> resources = new ArrayList<Resource> ();
		try
		{
			doc.getReactionNetworkPic (resources);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("error getting reaction network");
		}
		assertEquals ("didn't received a reaction network", 1, resources.size ());
		System.out.println (resources.get (0));
		
		

		resources = new ArrayList<Resource> ();
		try
		{
			doc.getCurationResultPic (resources);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("error getting reaction network");
		}
		assertEquals ("didn't received a reaction network", 1, resources.size ());
		System.out.println (resources.get (0));
		
		
		
	}
	
	
	
}
