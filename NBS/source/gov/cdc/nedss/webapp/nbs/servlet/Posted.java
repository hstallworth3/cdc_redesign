package gov.cdc.nedss.webapp.nbs.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *  Duplicates part of the functionality of DebugServlet.
 *  @author Jay Kim
 */
public class Posted extends HttpServlet
{
	private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html";

    public void destroy()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Posted</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a POST. This is the reply.</p>");
        Enumeration enumeration = request.getParameterNames();
        ArrayList<String> list = new ArrayList<String> ();
        while(enumeration.hasMoreElements())
        {
            list.add((String)enumeration.nextElement());
        }
        Collections.sort(list);
       Iterator<String>  itr = list.iterator();
        out.println("<table>");
        while(itr.hasNext())
        {
            String names = (String)itr.next();
            out.println("<tr>");
            out.println("<td>" + names + "</td>");
            out.println("</tr>");
            String[] values = request.getParameterValues(names);
            for(int i = 0; i < values.length; i++)
            {
                out.println("<tr><td colspan=\"100%\"><table border=\"1\"><tr>");
                out.println("<td></td><td>" + values[i] + "</td>");
                out.println("</tr></table></td></tr>");
            }
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    public void init() throws ServletException
    {
    }

}
