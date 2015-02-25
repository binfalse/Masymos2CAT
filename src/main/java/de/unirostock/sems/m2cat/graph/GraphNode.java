package de.unirostock.sems.m2cat.graph;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.unirostock.sems.m2cat.graph.GraphDbManager.Response;



/**
 * The Class GraphNode.
 *
 * @author Martin Scharm
 */
public abstract class GraphNode
{
	
	/** The Constant DB_ENTRY_NODE_LABELS. */
	public static final String DB_ENTRY_NODE_LABELS	= "labels";
	
	/** The Constant DB_ENTRY_NODE_SELF. */
	public final static String DB_ENTRY_NODE_SELF = "self";
	
	/** The Constant DB_ENTRY_NODE_PROPERTIES. */
	public static final String DB_ENTRY_NODE_PROPERTIES	= "properties";
	
	/** The Constant DB_ENTRY_EDGE_START. */
	public final static String DB_ENTRY_EDGE_START = "start";
	
	/** The Constant DB_ENTRY_EDGE_END. */
	public final static String DB_ENTRY_EDGE_END = "end";
	
	/** The Constant DB_ENTRY_EDGE_TYPE. */
	public final static String DB_ENTRY_EDGE_TYPE = "type";
	
	/** The Constant TYPE_COMMENT. */
	public static final int TYPE_COMMENT = 1;
	
	/** The Constant TYPE_REGISTERED. */
	public static final int TYPE_REGISTERED = 2;
	
	/** The Constant TYPE_FILE. */
	public static final int TYPE_FILE = 3;
	
	/** The Constant TYPE_BIBENTRY. */
	public static final int TYPE_BIBENTRY = 4;
	
	/** The Constant TYPE_AUTHOR. */
	public static final int TYPE_AUTHOR = 5;
	
	/** The Constant TYPE_POST. */
	public static final int TYPE_POST = 6;
	
	/** The Constant TYPE_PUBLISHER. */
	public static final int TYPE_PUBLISHER = 7;
	
	/** The Constant TYPE_TAG. */
	public static final int TYPE_TAG = 8;
	
	/** The Constant TYPE_USER. */
	public static final int TYPE_USER = 9;
	
	/** The Constant TYPE_INSTALLATION. */
	public static final int TYPE_INSTALLATION = 10;
	
	/** The Constant TYPE_PLATFORM. */
	public static final int TYPE_PLATFORM = 11;
	
	/** r/o entry received from db, might be null if this entry wasn't obtained from db.  */
	protected JSONObject dbEntry;
	
	/** The data. */
	protected JSONObject data;
	
	/**
	 * The Constructor.
	 *
	 * @param dbEntry the db entry
	 */
	public GraphNode (JSONObject dbEntry)
	{
		rereadDbEntry (dbEntry);
	}
	
	
	/**
	 * Reread db entry.
	 *
	 * @param dbEntry the db entry
	 */
	private final void rereadDbEntry (JSONObject dbEntry)
	{
		this.dbEntry = dbEntry;
		if (dbEntry != null)
			data = (JSONObject) dbEntry.get ("data");
		if (data == null)
			data = new JSONObject ();
	}
	
	
	/**
	 * Gets the rest properties.
	 *
	 * @return the rest properties
	 */
	public final String getRestProperties ()
	{
		if (dbEntry == null)
			return null;
		String uri = (String) dbEntry.get (DB_ENTRY_NODE_PROPERTIES);
		if (uri == null)
			return null;
		return uri;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public final Integer getId ()
	{
		if (dbEntry == null)
			return null;
		String uri = (String) dbEntry.get (DB_ENTRY_NODE_SELF);
		if (uri == null)
			return null;
		int p = uri.lastIndexOf ("/");
		if (p < 0)
			return null;
		return Integer.parseInt (uri.substring (p + 1));
	}
	
	/**
	 * Gets the node entry.
	 *
	 * @param key the key
	 * @return the node entry
	 */
	public final String getNodeEntry (String key)
	{
		if (dbEntry == null)
			return null;
		return (String) dbEntry.get (key);
	}
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public final JSONObject getData ()
	{
		return data;
	}
	
	/**
	 * Gets the labels.
	 *
	 * @return the labels
	 * @throws IOException the IO exception
	 */
	public JSONArray getLabels () throws IOException
	{
		Response res = GraphDbManager.get ((String) dbEntry.get (DB_ENTRY_NODE_LABELS));
		if (res.status != 200)
			throw new IOException ("couldn't obtain label");
		
		JSONObject json = (JSONObject) JSONValue.parse(res.content);
		JSONArray labels = (JSONArray) json.get ("data");
		return labels;
	}
}
