/**
 * 
 */
package de.unirostock.sems.m2cat.graph;

import org.json.simple.JSONObject;


/**
 * @author Martin Scharm
 *
 */
public class GraphModel extends GraphNode
{
	/**
	 * @param data
	 */
	public GraphModel (JSONObject data)
	{
		super (data);
	}
	
	public String toString ()
	{
		return "MODEL[" + getModelId ()+ "]=" + getModelName ();
	}
	
	/**
	 * Gets the model id.
	 *
	 * @return the model id
	 */
	public String getModelId ()
	{
		Object o = data.get ("ID");
		if (o != null)
			return o.toString ();
		return null;
	}
	
	/**
	 * Gets the model name.
	 *
	 * @return the model name
	 */
	public String getModelName ()
	{
		Object o = data.get ("NAME");
		if (o != null)
			return o.toString ();
		return null;
	}
}
