/**
 * 
 */
package de.unirostock.sems.m2cat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;

import de.binfalse.bfutils.FileRetriever;


/**
 * The Class GraphDocument.
 *
 * @author Martin Scharm
 */
public class GraphSedmlDocument extends GraphNode
{
	
	/** The sedml. */
	private GraphSedml sedml;
	
	/**
	 * The Constructor.
	 *
	 * @param data the data
	 * @param sedml the sedml model
	 */
	public GraphSedmlDocument (JSONObject data, GraphSedml sedml)
	{
		super (data);
		this.sedml = sedml;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return "DOC[" + getFileUri () + "]=" + getFileName () + " ---> " + sedml;
	}
	
	/**
	 * Gets the sedml.
	 *
	 * @return simulation description
	 */
	public GraphSedml getSedml ()
	{
		return sedml;
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName ()
	{
		Object o = data.get ("FILENAME");
		if (o != null)
			return o.toString ();
		return null;
	}
	
	/**
	 * Gets the file uri.
	 *
	 * @return the file uri
	 */
	public String getFileUri ()
	{
		Object o = data.get ("URI");
		if (o != null)
			return o.toString ();
		return null;
	}
	
	
	
	/**
	 * Retrieve sedml file.
	 * 
	 * @return the sedml file as resource
	 * @throws IOException the IO exception
	 * @throws URISyntaxException 
	 */
	public Resource retrieveModelFiles () throws IOException, URISyntaxException
	{
		File tmp = File.createTempFile ("m2cat", "modelFile");
		tmp.deleteOnExit ();
		String name = FileRetriever.getFile (new URI (getFileUri ()), tmp);
		if (name == null || name.length () < 1)
			name = getFileName ();
		return new Resource (Resource.DIR_MODEL, name, "simulation description found in masymos", tmp, "simulation description");
	}
}
