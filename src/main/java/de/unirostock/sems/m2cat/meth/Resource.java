/**
 * 
 */
package de.unirostock.sems.m2cat.meth;

import java.io.File;


/**
 * The Class Resource.
 *
 * @author Martin Scharm
 */
public class Resource
{
	
	/** The Constant DIR_MODEL representing the directory containing the models. */
	public static final String DIR_MODEL = "model";
	
	/** The Constant DIR_DATA representing the directory containing data. */
	public static final String DIR_DATA = "data";
	
	/** The Constant DIR_DOC representing the directory containing documentation, e.g. articles and stuff. */
	public static final String DIR_DOC = "doc";
	
	
	/** The name. */
	public String name;
	
	/** The note. */
	public String note;
	
	/** The f. */
	public File f;
	
	/** The directory in archive. */
	public String dir;
	
	/** The type. */
	public String type;
	
	/**
	 * The Constructor.
	 *
	 * @param dir the directory in the archive
	 * @param name the name of the file
	 * @param note the note or description
	 * @param f the actual file resource
	 * @param type the type
	 */
	public Resource (String dir, String name, String note, File f, String type)
	{
		this.dir = dir;
		this.name = name;
		this.note = note;
		this.f = f;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return name + "|" + note + "|" + f.getAbsolutePath ();
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType ()
	{
		return type;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName ()
	{
		return name;
	}
}
