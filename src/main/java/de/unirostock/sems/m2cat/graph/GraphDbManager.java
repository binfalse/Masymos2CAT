package de.unirostock.sems.m2cat.graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.m2cat.Main;


// TODO: Auto-generated Javadoc
/**
 * The Class GraphDbManager.
 *
 * @author Martin Scharm
 */
public class GraphDbManager
{
	/**
	 * 
	 */
	public static final String ROOT_URI = "http://sems.uni-rostock.de:17474/db/data/";
	
	/**
	 * 
	 */
	public static final String CYPHER = ROOT_URI + "cypher/";
	
	/**
	 * The Class Response.
	 */
	public static class Response
	{
		
		/** The location. */
		public URI location;
		
		/** The content. */
		public String content;
		
		/** The status. */
		public int status;
		
		/**
		 * The Constructor.
		 *
		 * @param location the location
		 * @param content the content
		 * @param status the status
		 */
		public Response (URI location, String content, int status)
		{
			this.location = location;
			this.content = content;
			this.status = status;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString ()
		{
			return " < [" + status + "] from " + location + "\n < " + content;
		}
	}

	/**
	 * Cypher.
	 *
	 * @param query the query
	 * @param params the params
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response cypher (String query, JSONObject params) throws IOException
	{
		StringBuilder sb = new StringBuilder ("{\"query\":\"");
		// insert actual query
		sb.append (query);
		
		sb.append ("\", \"params\":");
		// append param
		sb.append (params.toJSONString ());
		
		sb.append ("}");
		
		LOGGER.debug ("sending cypher");
			
		return post (CYPHER, sb.toString ());
	}

	/**
	 * Gets the.
	 *
	 * @param target the target
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response get (String target) throws IOException
	{
		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("sending get:\n > " + target.toString ());
		
		ClientResponse response = Client.create()
      .resource( target )
      .accept( MediaType.APPLICATION_JSON )
      .get( ClientResponse.class );
		
		Response ret  = readResponse(response);
		response.close();

		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("received:\n" + ret);
		
		return ret;
	}

	/**
	 * Delete.
	 *
	 * @param target the target
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response delete (String target) throws IOException
	{
		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("sending delete:\n > " + target.toString ());
		
		ClientResponse response = Client.create()
      .resource( target )
      .accept( MediaType.APPLICATION_JSON )
      .delete( ClientResponse.class );
		
		Response ret  = readResponse(response);
		response.close();

		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("received:\n" + ret);
		
		return ret;
	}

	/**
	 * Post.
	 *
	 * @param target the target
	 * @param json the json
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response post (String target, String json) throws IOException
	{
		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("sending post:\n > " + target.toString () + "\n > " + json);
		
		ClientResponse response = Client.create()
      .resource( target )
      .accept( MediaType.APPLICATION_JSON )
      .type( MediaType.APPLICATION_JSON )
      .entity( json )
      .post( ClientResponse.class );
		
		Response ret  = readResponse(response);
		response.close();

		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("received:\n" + ret);
		
		return ret;
	}
	
	
	/**
	 * Put.
	 *
	 * @param target the target
	 * @param json the json
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response put (String target, String json) throws IOException
	{
		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("sending put:\n > " + target.toString () + "\n > " + json);
		
		ClientResponse response = Client.create()
      .resource( target )
      .accept( MediaType.APPLICATION_JSON )
      .type( MediaType.APPLICATION_JSON )
			.entity (json)
			.put ( ClientResponse.class );

		Response ret = readResponse(response);
		response.close();

		if (LOGGER.isDebugEnabled ())
			LOGGER.debug ("received:\n" + ret);
		
		return ret;
	}
	
	/**
	 * Read response.
	 *
	 * @param response the response
	 * @return the response
	 * @throws IOException the IO exception
	 */
	public static Response readResponse (ClientResponse response) throws IOException
	{
		StringBuilder sb = new StringBuilder ();
		BufferedReader br = new BufferedReader (new InputStreamReader (response.getEntityInputStream ()));
		while (br.ready ())
		{
			sb.append (br.readLine ());
			sb.append (Main.NL);
		}
		br.close ();
		
		return new Response (response.getLocation(), sb.toString (), response.getStatus ());
	}
	
}
