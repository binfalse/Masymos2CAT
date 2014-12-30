/**
 * 
 */
package de.unirostock.sems.m2cat.web;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The Class CookieManager.
 *
 * @author martin
 */
public class CookieManager
{
	/** how long do we want to store the users information? */
	public static final int COOKIE_LIFETIME = 60*60*24*365;
	
	/** The cookies. */
	private HashMap<String, Cookie> cookies;
	
	/** The response. */
	private HttpServletResponse response;
	
	/**
	 * Instantiates a new cookie manager.
	 *
	 * @param request the request
	 * @param response the response
	 */
	public CookieManager (HttpServletRequest request, HttpServletResponse response)
	{
		this.cookies = new HashMap<String, Cookie> ();
		Cookie[] cookies = request.getCookies ();
		this.response = response;
		if (cookies != null)
		for (Cookie c : cookies)
			this.cookies.put (c.getName (), c);
	}
	
	/**
	 * Gets the value of a cookie.
	 *
	 * @param name the name
	 * @return the cookie value or null if there is no such cookie
	 */
	public String getCookieValue (String name)
	{
		Cookie c = cookies.get (name);
		if (c != null)
			return c.getValue ();
		return null;
	}
	
	/**
	 * Gets the cookie.
	 *
	 * @param name the name
	 * @return the cookie
	 */
	public Cookie getCookie (String name)
	{
		return cookies.get (name);
	}
	
	/**
	 * Sets the cookie.
	 *
	 * @param cookie the new cookie
	 */
	public void setCookie (Cookie cookie) {
		cookie.setPath( "/" );
		cookie.setMaxAge( COOKIE_LIFETIME );
		
		cookies.put(cookie.getName (), cookie);
		response.addCookie (cookie);
	}
	
}
