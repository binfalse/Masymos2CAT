/**
 * 
 */
package de.unirostock.sems.m2cat.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.binfalse.bflog.LOGGER;
import de.binfalse.bfutils.FileRetriever;
import de.unirostock.sems.m2cat.graph.GraphModelDocument;
import de.unirostock.sems.m2cat.meth.Searcher;


/**
 * @author Martin Scharm
 *
 */
public class WebSite extends HttpServlet
{

	private final String COOKIE_FIRSTNAME = "M2CATFN";
	private final String COOKIE_LASTNAME = "M2CATLN";
	private final String COOKIE_MAIL = "M2CATMAIL";
	private final String COOKIE_ORG = "M2CATORG";
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6349768181334656279L;

	/** The storage. */
	public static File STORAGE = new File ("/tmp/m2cat");
	
	/** The base url. */
	public static String BASE_URL = "http://m2cat.sems.uni-rostock.de/";
	
	/**
	 * Load settings from context.
	 *
	 * @param context the context
	 */
	public static void loadSettingsFromContext( ServletContext context )
	{
		// Log Level
		LOGGER.setMinLevel (LOGGER.WARN);
		String desiredLogLevel = context.getInitParameter("LOGLEVEL");
	
		if (desiredLogLevel != null) {
			LOGGER.warn("Setting log level to ", desiredLogLevel);
	
			if (desiredLogLevel.equals ("DEBUG")) {
				LOGGER.setMinLevel (LOGGER.DEBUG);
				LOGGER.setLogStackTrace (true);
			}
			else if (desiredLogLevel.equals ("INFO"))
				LOGGER.setMinLevel (LOGGER.INFO);
			else if (desiredLogLevel.equals ("WARN"))
				LOGGER.setMinLevel (LOGGER.WARN);
			else if (desiredLogLevel.equals ("ERROR"))
				LOGGER.setMinLevel (LOGGER.ERROR);
			else if (desiredLogLevel.equals ("NONE"))
				LOGGER.setLogToStdErr (false);
		}
		
		// Storage
		String storage = context.getInitParameter("STORAGE");
		if( storage != null ) {
			STORAGE = new File( storage );
			LOGGER.info("Set storage to ", STORAGE);
		}
		LOGGER.debug ("stor: ", STORAGE.isDirectory (), " -- ", !STORAGE.mkdirs ());
		if (!STORAGE.isDirectory () && !STORAGE.mkdirs ())
		{
			LOGGER.error ("cannot create storage directory: ", STORAGE.getAbsolutePath ());
			return;
		}
		File cache = new File (STORAGE + File.separator + "cache");
		if (cache.isDirectory () || cache.mkdirs ())
			try
			{
				FileRetriever.setUpCache (cache);
			}
			catch (IOException e)
			{
				LOGGER.warn (e, "cannot setup cache directory");
			}
		
		// url
		BASE_URL = context.getInitParameter("BASEURL");
		if (BASE_URL == null)
		{
			LOGGER.error ("cannot create storage directory: ", STORAGE.getAbsolutePath ());
			return;
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// load settings form servlet context
		ServletContext context = config.getServletContext();
		loadSettingsFromContext(context);
		
	}

	
	private User loadUser (HttpServletRequest request, CookieManager cookies)
	{
		if (request.getParameter ("savemyinfo") != null)
		{
			cookies.setCookie (new Cookie (COOKIE_FIRSTNAME, request.getParameter ("firstname")));
			cookies.setCookie (new Cookie (COOKIE_LASTNAME, request.getParameter ("lastname")));
			cookies.setCookie (new Cookie (COOKIE_MAIL, request.getParameter ("mail")));
			cookies.setCookie (new Cookie (COOKIE_ORG, request.getParameter ("org")));
		}
		
		User user = new User (
			cookies.getCookieValue (COOKIE_FIRSTNAME), 
			cookies.getCookieValue (COOKIE_LASTNAME),
			cookies.getCookieValue (COOKIE_MAIL),
			cookies.getCookieValue (COOKIE_ORG));
		
		return user;
	}

	private void run (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		response.setContentType ("text/html");
		response.setCharacterEncoding ("UTF-8");
		request.setCharacterEncoding ("UTF-8");
		
		CookieManager cookies = new CookieManager (request, response);
		User user = loadUser (request, cookies);

		String[] req =  request.getRequestURI().substring(request.getContextPath().length()).split ("/", -1);
		LOGGER.debug ("req: ", Arrays.toString (req));
		
		
		String search = request.getParameter ("search");
		if (search != null && search.length () > 0)
		{
			request.setAttribute ("searchTerm", search);
			Searcher s = new Searcher ();
			List<GraphModelDocument> docs;
			try
			{
				docs = s.search (search);
				if (docs != null)
					request.setAttribute ("docs", docs);
				LOGGER.info ("found ", docs.size (), " docs for ", search);
				
			}
			catch (Exception e)
			{
				LOGGER.error (e, "couldn't search for", search);
			}
		}
		
		if (req.length > 2 && req[1].equals ("file"))
		{
			String name = req[2];
			
			if (name.equals ("archive") && req.length > 3)
			{
				
				if (req.length > 4)
				{
					user = User.getUserFromUrl (req[4]);
				}
				else
				{
					LOGGER.error ("invalid request: ", req.length);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				
				// generate archive
				Searcher s = new Searcher ();
				List<GraphModelDocument> docs;
				try
				{
					LOGGER.debug ("searching model id ", req[3]);
					docs = s.searchForId (Integer.parseInt (req[3]));
					if (docs != null)
						request.setAttribute ("docs", docs);
					LOGGER.info ("found ", docs.size (), " docs for ", search);
					
					
					
					for (GraphModelDocument doc : docs)
					{
						String caname = UUID.randomUUID ().toString ();
						while (new File (STORAGE + File.separator + caname).exists ())
							caname = UUID.randomUUID ().toString ();
						doc.createCombineArchive (new File (STORAGE + File.separator + caname), caname, user);
						if (passFile (request, response, caname))
							return;
					}
				}
				catch (Exception e)
				{
					LOGGER.error (e, "couldn't search for", search);
				}
			}
			
			else if (name.matches ("^(0-9a-zA-Z.-)\\+$"))
			{
				if (passFile (request, response, name))
					return;
			}
			else if (name.equals ("cache") && req.length > 3 && req[3].matches ("^(0-9a-zA-Z.-)\\+$"))
				if (passFile (request, response, req[3]))
					return;
			LOGGER.debug ("passing file apparently failed");
		}
			
		
		request.setAttribute ("ContextPath", request.getContextPath ());
		request.setAttribute ("user", user);
		request.setAttribute ("base", BASE_URL);
		request.getRequestDispatcher ("/WEB-INF/Index.jsp").forward (request, response);
	}
	
  private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
	
	private boolean passFile (HttpServletRequest request,
		HttpServletResponse response, String name) throws ServletException, IOException
	{
		File file = new File (STORAGE + File.separator + name);
		
		if (!file.exists ())
			return false;
		
		response.reset();
    response.setBufferSize(DEFAULT_BUFFER_SIZE);
    response.setContentType(Files.probeContentType (file.toPath ()));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    
    BufferedInputStream input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
    BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

    // pass the stream to client
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int length;
    while ((length = input.read(buffer)) > 0)
        output.write(buffer, 0, length);
    
    input.close();
    output.close();

		LOGGER.debug ("passing file ", name, " was succ");
		return true;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		run (request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		run (request, response);
	}

}
