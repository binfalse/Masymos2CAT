/**
 * 
 */
package de.unirostock.sems.m2cat.graph;

import org.json.simple.JSONObject;


/**
 * @author Martin Scharm
 *
 */
public class GraphSedml extends GraphNode
{
	/**
	 * @param data
	 */
	public GraphSedml (JSONObject data)
	{
		super (data);
	}
	
	public String toString ()
	{
		return "Sedml";
	}
}
