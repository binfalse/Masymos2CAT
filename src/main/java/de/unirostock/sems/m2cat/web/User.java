/**
 * 
 */
package de.unirostock.sems.m2cat.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.binfalse.bfutils.GeneralTools;
import de.unirostock.sems.cbarchive.meta.omex.VCard;


/**
 * The Class User.
 *
 * @author Martin Scharm
 */
public class User
{
	
	/** The first name. */
	private String firstName;
	
	/** The last name. */
	private String lastName;
	
	/** The mail. */
	private String mail;
	
	/** The organization. */
	private String organization;
	
	/** The vcard. */
	private VCard vcard;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param mail the mail
	 * @param organization the organization
	 */
	public User (String firstName, String lastName, String mail, String organization)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.organization = organization;
		vcard = new VCard (lastName, firstName, mail, organization);
	}
	
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName ()
	{
		return firstName;
	}

	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName ()
	{
		return lastName;
	}


	
	/**
	 * Gets the mail.
	 *
	 * @return the mail
	 */
	public String getMail ()
	{
		return mail;
	}


	
	/**
	 * Gets the organization.
	 *
	 * @return the organization
	 */
	public String getOrganization ()
	{
		return organization;
	}

	/**
	 * Checks if is valid.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid ()
	{
		return !vcard.isEmpty ();
	}

	/**
	 * Gets the vcard.
	 *
	 * @return the vcard
	 */
	public VCard getVcard ()
	{
		return vcard;
	}
	
	/**
	 * Gets the user info encoded in base64.
	 *
	 * @return the url user info
	 */
	public String getUrlUserInfo ()
	{
		return GeneralTools.encodeBase64 ((firstName + ":" + lastName + ":" + mail + ":" + organization).getBytes ());
	}
	
	/**
	 * Gets the user from url.
	 *
	 * @param url the url
	 * @return the user from url
	 */
	public static User getUserFromUrl (String url)
	{
		String s = new String (GeneralTools.decodeBase64 (url));
		
		User u = new User ("", "", "", "");
		
		String [] s2 = s.split (":", -1);

		if (s2.length > 0)
			u.firstName = s2[0];
		if (s2.length > 1)
			u.lastName = s2[1];
		if (s2.length > 2)
			u.mail = s2[2];
		if (s2.length > 3)
			u.organization = s2[3];
		u.vcard = new VCard (u.lastName, u.firstName, u.mail, u.organization);
		
		
		return u;
	}
	
	
}
