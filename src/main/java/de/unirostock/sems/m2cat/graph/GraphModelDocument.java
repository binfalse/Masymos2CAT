/**
 * 
 */
package de.unirostock.sems.m2cat.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.apache.http.client.ClientProtocolException;
import org.jdom2.JDOMException;
import org.json.simple.JSONObject;

import de.binfalse.bflog.LOGGER;
import de.binfalse.bfutils.FileRetriever;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.OmexMetaDataObject;
import de.unirostock.sems.cbarchive.meta.omex.OmexDescription;
import de.unirostock.sems.cbarchive.meta.omex.VCard;
import de.unirostock.sems.cbext.Formatizer;
import de.unirostock.sems.m2cat.meth.Resource;
import de.unirostock.sems.m2cat.web.User;


/**
 * The Class GraphDocument.
 *
 * @author Martin Scharm
 */
public class GraphModelDocument extends GraphNode
{
	
	/** The model. */
	private GraphModel model;
	
	/** The simulation description. */
	private List<GraphSedmlDocument> simulationDescription;
	
	/** The resources. */
	private List<Resource> resources;
	
	private boolean isCellML;
	
	/**
	 * The Constructor.
	 *
	 * @param data the data
	 * @param model the model
	 */
	public GraphModelDocument (JSONObject data, GraphModel model)
	{
		super (data);
		this.model = model;
		simulationDescription = new ArrayList<GraphSedmlDocument> ();
		resources = new ArrayList<Resource> ();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		String s = "DOC[" + getFileUri () + "]=" + getFileName () + " ---> " + model;
		for (GraphSedmlDocument sedml : simulationDescription)
			s += "\n" + sedml;
		return s;
	}
	
	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	public List<Resource> getResources ()
	{
		return resources;
	}
	
	/**
	 * Gets the model.
	 *
	 * @return model
	 */
	public GraphModel getModel ()
	{
		return model;
	}
	
	
	/**
	 * Gets the URI to the document.
	 *
	 * @return the URI
	 */
	public String getDocUri ()
	{
		Object o = data.get ("FILENAME");
		if (o != null)
			return o.toString ();
		return null;
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
	 * Retrieve model file.
	 *
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the uRI syntax exception
	 */
	public void retrieveModelFiles () throws IOException, URISyntaxException
	{
		File tmp = File.createTempFile ("m2cat", "modelFile");
		tmp.deleteOnExit ();
		String name = FileRetriever.getFile (new URI (getFileUri ()), tmp);
		if (name == null || name.length () < 1)
			name = getFileName ();
		resources.add (new Resource (Resource.DIR_MODEL, name, "model found in masymos", tmp, "model file"));
		for (GraphSedmlDocument sedml : simulationDescription)
			resources.add (sedml.retrieveModelFiles ());
	}

	/**
	 * Adds the simulation description.
	 *
	 * @param simulation the simulation
	 */
	public void addSimulationDescription (GraphSedmlDocument simulation)
	{
		simulationDescription.add (simulation);
	}
	
	
	/**
	 * Checks if is cell ml.
	 *
	 * @return true, if checks if is cell ml
	 */
	public boolean isCellML ()
	{
		return isCellML;
	}
	
	
	/**
	 * Checks if is simulation description.
	 *
	 * @return true, if checks if is simulation description
	 */
	public boolean isSimulationDescription ()
	{
		return simulationDescription.size () > 0;
	}
	
	/**
	 * Gets the first simulation description.
	 *
	 * @return the first simulation description
	 */
	public GraphSedmlDocument getFirstSimulationDescription ()
	{
		return simulationDescription.get (0);
	}
	
	
	/**
	 * Gets the additional resources.
	 *
	 * @return the additional resources
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the uRI syntax exception
	 */
	public List<Resource> retrieveAdditionalResources () throws ClientProtocolException, IOException, URISyntaxException
	{
		// filename matches BIOM...xml?
		// -> download sbgn img and curation stuff from biomodels
		getReactionNetworkPic (resources);
		getCurationResultPic (resources);
		
		if (getFileName ().matches (".*.cellml"))
			isCellML = true;
		
		// TODO: URI matches http://models.cellml.org/
		// -> find repo name. tell the user. clone all from cellml
		
		
		return resources;
	}
	
	
	/**
	 * Gets the reaction network.
	 *
	 * @param resources the resources
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the uRI syntax exception
	 */
	public void getCurationResultPic (List<Resource> resources) throws ClientProtocolException, IOException, URISyntaxException
	{
		if (getFileName ().matches ("BIOMD\\d+\\.xml"))
		{
			String id = getFileName ();
			id = id.substring (0, id.length () - 4);
			LOGGER.debug (id);
			
			File tmp = File.createTempFile ("m2cat", "temp");
			//tmp.deleteOnExit ();
			String remoteUrl = "http://www.ebi.ac.uk/biomodels-main/simulation-result.do?uri=publ-model.do&mid=" + id;
			String name = id + "-curation.png";

			LOGGER.debug ("downloading curation result page from ", remoteUrl);
			FileRetriever.getFile (new URI (remoteUrl), tmp);
			
			
			// read the file
			BufferedReader br = new BufferedReader (new FileReader (tmp));
			File pic = File.createTempFile ("m2cat", "curationResult");
			List<String> comments = new ArrayList<String> ();
			boolean succ = false, comment = false;
			String line;
			while (br.ready ())
			{
				line = br.readLine ();
				if (line.contains ("<img") && !line.contains ("<a") && !line.contains ("icons"))
				{
					Matcher matcher = Pattern.compile (
						"^\\s*<img src=\"([^\"]+)\" title=\"([^\"]+)\" alt=.*$",
						Pattern.CASE_INSENSITIVE).matcher (line);
					if (matcher.find ())
					{
						String url = matcher.group (1);
						if (url.startsWith ("//"))
							url = "http:" + url;
						LOGGER.debug ("downloading curation result from ", url);
						FileRetriever.getFile (new URI (url), pic);
						comments.add (0, matcher.group (2));
						succ = true;
					}
				}
				if (line.contains ("window.close"))
					break;
				if (comment)
					comments.add (line.replaceAll ("<[^>]*>", ""));
				if (line.contains ("Curator's comment:"))
					comment = true;
				
			}
			br.close ();
			
			if (succ)
			{
				StringBuffer c = new StringBuffer ();
				for (String s : comments)
					c.append (s).append (" &mdash; ");
				c.append ("(curation result as taken from BiomodelsDatabase: ").append (remoteUrl).append (")");
				resources.add (new Resource (Resource.DIR_MODEL, name, c.toString (), pic, "curation result"));
			}
		}
	}
	
	
	/**
	 * Gets the reaction network.
	 *
	 * @param resources the resources
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the uRI syntax exception
	 */
	public void getReactionNetworkPic (List<Resource> resources) throws ClientProtocolException, IOException, URISyntaxException
	{
		if (getFileName ().matches ("BIOMD\\d+\\.xml"))
		{
			String id = getFileName ();
			id = id.substring (0, id.length () - 4);
			LOGGER.debug (id);
			
			File tmp = File.createTempFile ("m2cat", "modelpic");
			tmp.deleteOnExit ();
			String remoteUrl = "http://www.ebi.ac.uk/biomodels-main/download?mid=" + id + "&format=PNG";
			String name = id + ".png";
			
			LOGGER.debug ("downloading reaction network from ", remoteUrl);
			String sName = FileRetriever.getFile (new URI (remoteUrl), tmp);
			if (sName != null && sName.length () > 0)
				name = sName;
			resources.add (new Resource (Resource.DIR_MODEL, name, "taken from BiomodelsDatabase (" + remoteUrl + ")", tmp, "reaction network"));
		}
	}
	
	
	/**
	 * Creates an combine archive of this model.
	 *
	 * @param f the file to write to
	 * @param archiveName the archive name
	 * @param user the user
	 * @throws IOException the IO exception
	 * @throws JDOMException the JDOM exception
	 * @throws ParseException the parse exception
	 * @throws CombineArchiveException the combine archive exception
	 * @throws TransformerException the transformer exception
	 */
	public void createCombineArchive (File f, String archiveName, User user) throws IOException, JDOMException, ParseException, CombineArchiveException, TransformerException
	{
		List<VCard> creators = new ArrayList<VCard> ();
		creators.add (user.getVcard ()); 
		OmexDescription omex = new OmexDescription (creators, new Date ());
		
		CombineArchive ca = new CombineArchive (f);
		for (Resource r : resources)
		{
			try
			{
				URI format = Formatizer.guessFormat (r.f);
				String target = r.dir + File.separator + r.name;
				while (ca.getEntry (target) != null)
					target = r.dir + File.separator + UUID.randomUUID () + r.name;
				ArchiveEntry ae = ca.addEntry (r.f, target, format);
				OmexDescription descr = omex.clone ();
				descr.setDescription (r.note);
				ae.addDescription (new OmexMetaDataObject (descr));
			}
			catch (IOException e)
			{
				LOGGER.error (e, "couldn't add resource ", r);
			}
		}
		
		omex.setDescription ("archive created using masymos2CAT (" + new Date () + ") -- see https://sems.uni-rostock.de");
		ca.addDescription (new OmexMetaDataObject (omex));
		
		ca.pack ();
		ca.close ();
		
		LOGGER.debug ("wrote archive to ", f);
		
		this.archiveName = archiveName;
	}
	
	/** The archive name. */
	private String archiveName;
	
	/**
	 * Gets the archive name.
	 *
	 * @return the archive name
	 */
	public Integer getDocId ()
	{
		return getId ();
	}
	
	/**
	 * Gets the archive name.
	 *
	 * @return the archive name
	 */
	public String getArchiveName ()
	{
		return archiveName;
	}
}
