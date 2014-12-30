/**
 * 
 */
package de.unirostock.sems.m2cat.web;

import de.unirostock.sems.cbarchive.meta.omex.VCard;


/**
 * @author Martin Scharm
 *
 */
public class User
{
	private String firstName;
	private String lastName;
	private String mail;
	private String organization;
	private VCard vcard;
	
	public User (String firstName, String lastName, String mail, String organization)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.organization = organization;
		vcard = new VCard (lastName, firstName, mail, organization);
	}
	
	
	public String getFirstName ()
	{
		return firstName;
	}


	
	public String getLastName ()
	{
		return lastName;
	}


	
	public String getMail ()
	{
		return mail;
	}


	
	public String getOrganization ()
	{
		return organization;
	}

	public boolean isValid ()
	{
		return !vcard.isEmpty ();
	}

	public VCard getVcard ()
	{
		return vcard;
	}
}
