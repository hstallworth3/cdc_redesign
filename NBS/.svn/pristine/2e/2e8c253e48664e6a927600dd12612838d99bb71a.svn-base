package gov.cdc.nedss.webapp.nbs.servlet;

import  java.io.*;
import  java.net.*;
import  java.security.*;
import  java.text.*;
import  java.util.*;

import  javax.servlet.*;
import  javax.servlet.http.*;

/**
 *  DebugServlet.
 *  The servlet debugger enumerates the current values of system variables.
 *  @author Ed Jenkins
 */
public class DebugServlet extends HttpServlet
{

    /**
     *  HTTP POST method.
     *  Content-Type is unknown.
     */
    public static final int OTHER = 0;

    /**
     *  HTTP POST method.
     *  Content-Type = "application/x-www-form-urlencoded".
     */
    public static final int FORM = 1;

    /**
     *  HTTP POST method.
     *  Content-Type = "multipart/form-data".
     */
    public static final int FILE = 2;

    /**
     *  HTTP POST method.
     *  Content-Type = "application/x-www-form-urlencoded".
     */
    public static final String FORM_STRING = "application/x-www-form-urlencoded";

    /**
     *  HTTP POST method.
     *  Content-Type = "multipart/form-data".
     */
    public static final String FILE_STRING = "multipart/form-data";

    /**
     *  A non-breaking space.
     */
    public static final String NBSP = "&nbsp;";

    public void init(ServletConfig config) throws ServletException
    {
        //  Verify parameters.
        if(config == null)
        {
            return;
        }
        super.init(config);
    }

    public String getServletInfo()
    {
        return "The servlet debugger displays the current values of major system variables.";
    }

    /**
     *  Gets the list of all class and interface names associtated with the given object.
     *  @param pObject the object to check.
     *  @return a sorted list of class and interface names.
     */
    private TreeSet<Object>getClassList(Object pObject)
    {
        //  Create return variable.
        TreeSet<Object>ts = new TreeSet<Object>();
        //  Verify parameters.
        if(pObject == null)
        {
            return ts;
        }
        //  Get the class.
        Class c = pObject.getClass();
        //  Create temp variables.
        int x = 0;
        int y = 0;
        //  Loop through all parents.
        while(c != null)
        {
            //  Add my class name.
            ts.add(c.getName());
            //  Add my interfaces.
            Class[] ii = c.getInterfaces();
            for(x=0; x<y; x++)
            {
                ts.add(ii[x].getName());
            }
            //  Move up to my parents.
            c = c.getSuperclass();
        }
        //  Return result.
        return ts;
    }

    /**
     *  Gets the request type.
     *  If the request method is POST, then this method will
     *  examine the Content-Type header to determine whether
     *  a file or a form is being posted.
     *  @param req an HTTP servlet request.
     *  @return
     *      FILE if a file is being uploaded or
     *      FORM if a form is being posted or
     *      OTHER if something else.
     */
    private int getMethod(HttpServletRequest req)
    {
        //  Verify parameters.
        if(req == null)
        {
            return OTHER;
        }
        String strMethod = req.getMethod();
        if(strMethod.equalsIgnoreCase("POST"))
        {
            String strContentType = req.getContentType();
            if(strContentType.startsWith(FILE_STRING))
            {
                return FILE;
            }
            if(strContentType.startsWith(FORM_STRING))
            {
                return FORM;
            }
        }
        return OTHER;
    }

    /**
     *  Encodes an XML string, translating certain characters into XML entities.
     *  @param pXML the string to encode.
     *  @return an encoded string.
     */
    private String encodeXML(String pXML)
    {
        if(pXML == null)
        {
            return NBSP;
        }
        if(pXML.trim().equals(""))
        {
            return NBSP;
        }
        StringBuffer sb = new StringBuffer();
        int x = 0;
        int y = pXML.length();
        char c = 'x';
        for(x=0; x<y; x++)
        {
            c = pXML.charAt(x);
            if(c == '&')
            {
                sb.append("&amp;");
                continue;
            }
            if(c == '\'')
            {
                sb.append("&apos;");
                continue;
            }
            if(c == '>')
            {
                sb.append("&gt;");
                continue;
            }
            if(c == '<')
            {
                sb.append("&lt;");
                continue;
            }
            if(c == '\"')
            {
                sb.append("&quot;");
                continue;
            }
            sb.append(c);
        }
        String s = sb.toString();
        return s;
    }

    /**
     *  Serves up a log file for debugging.
     *  @param req an HTTP servlet request.
     *  @param res an HTTP servlet response.
     *  @param pApp true to send Tomcat's log file or false for SilverStream's log file.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void ServeLog(HttpServletRequest req, HttpServletResponse res, boolean pApp) throws ServletException, IOException
    {
        //  Verify parameters.
        if(req == null)
        {
            return;
        }
        if(res == null)
        {
            return;
        }
/*
        if(isAuthenticationRequired(req, res))
        {
            return;
        }
*/
        //  Read and write 1MB at a time.
        final int BUFFER_SIZE = 1024 * 1024;
        //  Find log files using generic methods, rather than hard-coding paths.
        //  This may keep it working after upgrades to the app server or web server.
        String strAppLog = System.getProperty("java.home");
        String strWebLog = System.getProperty("user.dir");
        StringBuffer sb = new StringBuffer();
        //  Select which log file to use.
        if( (pApp == true) && (strAppLog != null) )
        {
            sb.append(strAppLog);   sb.append(File.separator);
            sb.append("..");        sb.append(File.separator);
            sb.append("bin");
        }
        else
        {
            sb.append(strWebLog);
        }
        sb.append(File.separator);
        sb.append("nedss.log");
        String strFilename = sb.toString();
        //  Allocate a buffer.
        byte[] b = new byte[BUFFER_SIZE];
        int x = 0;
        int y = 0;
        int z = 0;
        File f = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ServletOutputStream sos = null;
        BufferedOutputStream bos = null;
        try
        {
            //  Open the log file.
            fis = new FileInputStream(strFilename);
            bis = new BufferedInputStream(fis, BUFFER_SIZE);
            //  Set content length so download will show percent done.
            f  = new File(strFilename);
            y  = (int)f.length();
            res.setContentType("application/text");
            res.setContentLength(y);
            //  Open the output stream.
            sos = res.getOutputStream();
            bos = new BufferedOutputStream(sos, BUFFER_SIZE);
            //  Loop through the file.
            while(true)
            {
                //  In case the file has grown since we opened it,
                //  don't serve more bytes than we said we would
                //  in the Content-Length header.
                if(z > y)
                {
                    break;
                }
                //  Read up to 1MB.
                x = bis.read(b, 0, BUFFER_SIZE);
                //  Stop at end of file.
                if(x < 1)
                {
                    break;
                }
                //  Increment the byte counter.
                z += x;
                //  But don't let it go too far.
                if(z > y)
                {
                    x -= z - y;
                }
                //  Write up to 1MB.
                bos.write(b, 0, x);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            //  Close the streams.
            if(bis != null)
            {
                bis.close();
            }
            if(bos != null)
            {
                bos.close();
            }
        }
    }

    /**
     *  Authenticates the user.
     *  @param req an HTTP servlet request.
     *  @param res an HTTP servlet response.
     *  @returns true if authentication is required or false if not.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private boolean isAuthenticationRequired(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        //  Verify parameters.
        if(req == null)
        {
            return false;
        }
        if(res == null)
        {
            return false;
        }
        String strRemoteUser = req.getRemoteUser();
        if( (strRemoteUser != null) && (strRemoteUser.equals("tomcat")) )
        {
            return false;
        }
        StringBuffer sb = new StringBuffer(1024);
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n");
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n");
        sb.append("    <head>\r\n");
        sb.append("        <title>Unauthorized</title>\r\n");
        sb.append("    </head>\r\n");
        sb.append("    <body>\r\n");
        sb.append("        <h1>Unauthorized</h1>\r\n");
        sb.append("        <h4>No soup for you!  :) </h4>\r\n");
        sb.append("    </body>\r\n");
        sb.append("</html>\r\n");
        String s = sb.toString();
        int y = s.length();
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.addHeader("WWW-Authenticate", "Basic realm=\"debug\"");
        res.setContentType("text/html");
        res.setContentLength(y);
        PrintWriter out = res.getWriter();
        out.print(s);
        out.flush();
        out.close();
        return true;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        //  Defer to doPost().
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        //  Verify parameters.
        if(req == null)
        {
            return;
        }
        if(res == null)
        {
            return;
        }
        try
        {
            String strPathInfo = req.getPathInfo();
            if(strPathInfo != null)
            {
                if(strPathInfo.equals("/get/app.nedss.log"))
                {
                    ServeLog(req, res, true);
                    return;
                }
                if(strPathInfo.equals("/get/web.nedss.log"))
                {
                    ServeLog(req, res, false);
                    return;
                }
            }
            //  Get session.
            HttpSession ses = req.getSession();
            //  Initialize output.
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            //  Write document.
            WriteHead(out, req);
            WriteClientInformation(out, req);
            WriteServerInformation(out);
            WriteSystemProperties(out);
            WriteContextAttributes(out);
            WriteContextParameters(out);
            WriteInitializationParameters(out);
            WriteSessionInformation(out, req, ses);
            WriteSessionAttributes(out, req, ses);
            WriteRequestInformation(out, req);
            WriteRequestAttributes(out, req);
            WriteRequestHeaders(out, req);
            WriteRequestCookies(out, req);
            WriteRequestParameters(out, req);
            if(req.getParameter("log") != null)
            {
                WriteServerLogs(out, req);
            }
            WriteTail(out);
            //  Send it to the client.
            out.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *  Writes opening tags, style sheets, etc.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteHead(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        //  Get servlet info.
        String strServletName = getServletName();
        String strServletInfo = getServletInfo();
        //  Get title.
        String strTitle = strServletName;
        //  Write the preamble and document type.
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        out.println("    <head>");
        //  Write META tags.
        out.println("        <meta name=\"author\" content=\"Ed Jenkins\" />");
        out.println("        <meta name=\"generator\" content=\"Ed Jenkins\" />");
        //  Write document title.
        out.println("        <title>" + strTitle + "</title>");
        //  Write style sheet.
        out.println("        <style type=\"text/css\">");
        out.println("            a:link");
        out.println("            {");
        out.println("                color: teal;");
        out.println("            }");
        out.println("            a:hover");
        out.println("            {");
        out.println("                color: aqua;");
        out.println("            }");
        out.println("            a:active");
        out.println("            {");
        out.println("                color: aqua;");
        out.println("            }");
        out.println("            a:visited");
        out.println("            {");
        out.println("                color: lime;");
        out.println("            }");
        out.println("            body");
        out.println("            {");
        out.println("                background-color: black;");
        out.println("                color: aqua;");
        out.println("                font-family: \"Lucida Sans Unicode\";");
        out.println("                font-size: medium;");
        out.println("                font-weight: bold;");
        out.println("            }");
        out.println("            h6");
        out.println("            {");
        out.println("                page-break-after: always;");
        out.println("            }");
        out.println("            td");
        out.println("            {");
        out.println("                border-color: silver;");
        out.println("            }");
        out.println("            th");
        out.println("            {");
        out.println("                border-color: silver;");
        out.println("            }");
        out.println("            .Button");
        out.println("            {");
        out.println("                color: black;");
        out.println("                font-family: \"Lucida Sans Unicode\";");
        out.println("                font-size: x-small;");
        out.println("                font-weight: bold;");
        out.println("            }");
        out.println("            .TableHeader");
        out.println("            {");
        out.println("                color: red;");
        out.println("                font-family: \"Haettenschweiler\";");
        out.println("                font-size: x-large;");
        out.println("                font-weight: normal;");
        out.println("                text-align: center;");
        out.println("            }");
        out.println("            .ColumnHeaderName");
        out.println("            {");
        out.println("                color: teal;");
        out.println("                font-family: \"Haettenschweiler\";");
        out.println("                font-size: x-large;");
        out.println("                font-weight: normal;");
        out.println("                text-align: center;");
        out.println("                width: 30%;");
        out.println("            }");
        out.println("            .ColumnHeaderValue");
        out.println("            {");
        out.println("                color: teal;");
        out.println("                font-family: \"Haettenschweiler\";");
        out.println("                font-size: x-large;");
        out.println("                font-weight: normal;");
        out.println("                text-align: center;");
        out.println("            }");
        out.println("            .DontWrap");
        out.println("            {");
        out.println("                white-space: nowrap;");
        out.println("            }");
        out.println("            .RowHeader");
        out.println("            {");
        out.println("                color: aqua;");
        out.println("                font-family: \"Haettenschweiler\";");
        out.println("                font-size: x-large;");
        out.println("                font-weight: normal;");
        out.println("                vertical-align: top;");
        out.println("            }");
        out.println("            .Name");
        out.println("            {");
        out.println("                color: aqua;");
        out.println("                font-family: \"Lucida Sans Unicode\";");
        out.println("                font-size: small;");
        out.println("                font-weight: bold;");
        out.println("                width: 30%;");
        out.println("            }");
        out.println("            .ValueCenter");
        out.println("            {");
        out.println("                color: lime;");
        out.println("                font-family: \"Lucida Console\";");
        out.println("                font-size: small;");
        out.println("                font-weight: bold;");
        out.println("                text-align: center;");
        out.println("            }");
        out.println("            .ValueLeft");
        out.println("            {");
        out.println("                color: lime;");
        out.println("                font-family: \"Lucida Console\";");
        out.println("                font-size: small;");
        out.println("                font-weight: bold;");
        out.println("                text-align: left;");
        out.println("            }");
        out.println("            .ValueRight");
        out.println("            {");
        out.println("                color: lime;");
        out.println("                font-family: \"Lucida Console\";");
        out.println("                font-size: small;");
        out.println("                font-weight: bold;");
        out.println("                text-align: right;");
        out.println("            }");
        out.println("        </style>");
        out.println("        <!--");
        out.println("            <script type=\"text/JavaScript\">");
        out.println("                function dofrmFileUploadSubmit()");
        out.println("                {");
        out.println("                    document.frmFileUpload.hidFileUpload.value = document.frmFileUpload.filFileUpload.value;");
        out.println("                }");
        out.println("            </script>");
        out.println("        -->");
        out.println("    </head>");
        out.println("    <body>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th class=\"TableHeader\">" + strServletName + "</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">");
        out.println("                        " + strServletInfo + "<br />");
        out.println("                        <ul>");
        out.println("                            <li>");
        out.println("                                <a href=\"#ClientInformation\">Client Information</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#ServerInformation\">Server Information</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#SystemProperties\">System Properties</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#ContextAttributes\">Context Attributes</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#ContextParameters\">Context Parameters</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#InitializationParameters\">Initialization Parameters</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#SessionInformation\">Session Information</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#SessionAttributes\">Session Attributes</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#RequestInformation\">Request Information</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#RequestAttributes\">Request Attributes</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#RequestHeaders\">Request Headers</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#RequestCookies\">Request Cookies</a>");
        out.println("                            </li>");
        out.println("                            <li>");
        out.println("                                <a href=\"#RequestParameters\">Request Parameters</a>");
        out.println("                            </li>");
        if(req.getParameter("log") != null)
        {
        out.println("                            <li>");
        out.println("                                <a href=\"#ServerLogs\">Server Logs</a>");
        out.println("                            </li>");
        }
        out.println("                        </ul>");
        out.println("                    </td>");
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of client information.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteClientInformation(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"ClientInformation\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Client Information</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        String strAddress = req.getRemoteAddr();
        String strHost = null;
        String strServer = null;
        InetAddress ia = null;
        InetAddress[] addresses = null;
        boolean booLocal = false;
        int x = 0;
        int y = 0;
        if(strAddress != null)
        {
            try
            {
                ia = InetAddress.getByName(strAddress);
                strHost = ia.getHostName();
                if(strAddress.startsWith("127."))
                {
                        ia = InetAddress.getLocalHost();
                        strAddress = ia.getHostAddress();
                        ia = InetAddress.getByName(strAddress);
                        strHost = ia.getHostName();
                }
                ia = InetAddress.getLocalHost();
                strServer = ia.getHostName();
                addresses = InetAddress.getAllByName(strServer);
                y = addresses.length;
                for(x=0; x<y; x++)
                {
                    ia = addresses[x];
                    strServer = ia.getHostAddress();
                    if(strServer.equals(strAddress))
                    {
                        booLocal = true;
                        break;
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Host Address</td>");
        out.println("                    <td class=\"ValueLeft\">" + strAddress + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Host Name</td>");
        out.println("                    <td class=\"ValueLeft\">" + strHost + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Browsing From Server</td>");
        out.println("                    <td class=\"ValueLeft\">" + booLocal + "</td>");
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of server information.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteServerInformation(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        InetAddress ia = null;
        String strAddress = null;
        String strHost = null;
        InetAddress[] iAll = null;
        int x = 0;
        int y = 0;
        try
        {
            ia = InetAddress.getLocalHost();
            strAddress = ia.getHostAddress();
            strHost = ia.getHostName();
            iAll = InetAddress.getAllByName(strHost);
            y = iAll.length;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //  Get server info.
        ServletContext context = getServletContext();
        String strServerInfo = context.getServerInfo();
        out.println("        <h6>");
        out.println("            <a name=\"ServerInformation\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Server Information</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        if(y > 1)
        {
            StringBuffer sb = new StringBuffer();
            for(x=0; x<y; x++)
            {
                sb.append("[");
                if
                (
                    (y > 10)
                &&  (x < 10)
                )
                {
                    sb.append("0");
                }
                sb.append(x);
                sb.append("]");
                sb.append("=");
                sb.append(iAll[x].getHostAddress());
                sb.append("<br/>");
            }
            sb.append("Default = ");
            sb.append(strAddress);
            strAddress = sb.toString();
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Host Address</td>");
        out.println("                    <td class=\"ValueLeft\">" + strAddress + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Host Name</td>");
        out.println("                    <td class=\"ValueLeft\">" + strHost + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Server Info</td>");
        out.println("                    <td class=\"ValueLeft\">" + strServerInfo + "</td>");
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of system properties.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteSystemProperties(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        String s = null;
        StringBuffer sb = new StringBuffer();
        int x = 0;
        int y = 0;
        int z = 0;
        int n = 0;
        out.println("        <h6>");
        out.println("            <a name=\"SystemProperties\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">System Properties</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Properties p = System.getProperties();
        Enumeration e = p.propertyNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        String strValue = p.getProperty(strName);
        //  Break up classpath (or anything like it)
        //  so that each part of the path is on a separate line.
        //  Otherwise, long paths can cause the right margin of the page to expand too far
        //  and make it unreadable and make you have to scroll.
        //  This will make it easier to read.
        if(strName.endsWith("path"))
        {
            sb.setLength(0);
            String strTemp = null;
            x = 0;
            while(true)
            {
                y = strValue.indexOf(";", x);
                if(y == -1)
                {
                    strTemp = strValue.substring(x);
                    sb.append(strTemp);
                    sb.append("<br/>");
                    break;
                }
                strTemp = strValue.substring(x, y);
                sb.append(strTemp);
                sb.append("<br/>");
                x = y + 1;
            }
            strValue = sb.toString();
        }
        if(strValue == null)
        {
            strValue = NBSP;
        }
        if(strName.equals("line.separator"))
        {
            sb.setLength(0);
            char[] c = strValue.toCharArray();
            y = c.length;
            z = y - 1;
            for(x=0; x<y; x++)
            {
                n = (int)c[x];
                s = Integer.toHexString(n);
                s = s.toUpperCase();
                sb.append("0");
                sb.append(s);
                if(x < z)
                {
                    sb.append(" ");
                }
            }
            strValue = sb.toString();
        }
        strValue = strValue.trim();
        if(strValue.equals(""))
        {
            strValue = NBSP;
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of context attributes.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteContextAttributes(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"ContextAttributes\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Context Attributes</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get the context.
        ServletContext context = getServletContext();
        //  Get a list of names.
        Enumeration e = context.getAttributeNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        Object obj = context.getAttribute(strName);
        String strValue = (obj == null) ? "<null>" : obj.toString();
        //  Break up classpath (or anything like it)
        //  so that each part of the path is on a separate line.
        //  Otherwise, long paths can cause the right margin of the page to expand too far
        //  and make it unreadable and make you have to scroll.
        //  This will make it easier to read.
        if
        (
            strName.endsWith("path")
        &&  (obj instanceof String)
        )
        {
            String strTemp = null;
            StringBuffer sb = new StringBuffer();
            int x = 0;
            int y = 0;
            while(true)
            {
                y = strValue.indexOf(";", x);
                if(y == -1)
                {
                    strTemp = strValue.substring(x);
                    sb.append(strTemp);
                    sb.append("<br/>");
                    break;
                }
                sb.append("<span class=\"DontWrap\">");
                strTemp = strValue.substring(x, y);
                sb.append(strTemp);
                sb.append("</span>");
                sb.append("<br/>");
                x = y + 1;
            }
            strValue = sb.toString();
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of context parameters.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteContextParameters(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"ContextParameters\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Context Parameters</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get the context.
        ServletContext context = getServletContext();
        //  Get a list of names.
        Enumeration e = context.getInitParameterNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        String strValue = context.getInitParameter(strName);
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of initialization parameters.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteInitializationParameters(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"InitializationParameters\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Initialization Parameters</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Enumeration e = getInitParameterNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        String strValue = getInitParameter(strName);
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of session information.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @param ses an HTTP session.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteSessionInformation(PrintWriter out, HttpServletRequest req, HttpSession ses) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        if(ses == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"SessionInformation\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Session Information</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        if(req.isRequestedSessionIdValid())
        {
            String f = "MM/dd/yyyy HH:mm:ss z";
            SimpleDateFormat df = new SimpleDateFormat(f);
            long lngCreated = ses.getCreationTime();
            long lngAccessed = ses.getLastAccessedTime();
            Date datCreated = new Date(lngCreated);
            Date datAccessed = new Date(lngAccessed);
            String strCreated = df.format(datCreated);
            String strAccessed = df.format(datAccessed);
            int intSeconds = ses.getMaxInactiveInterval();
            int intMinutes = 0;
            long lngNow = System.currentTimeMillis();
            long lngSecondsRemaining = 0;
            long lngMinutesRemaining = 0;
            if(intSeconds > 0)
            {
                intMinutes = intSeconds / 60;
                long lngTimeout = lngAccessed + (intSeconds * 1000);
                if(lngNow < lngTimeout)
                {
                    lngSecondsRemaining = (lngTimeout - lngNow) / 1000;
                    lngMinutesRemaining = lngSecondsRemaining / 60;
                }
            }
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">Date Created</td>");
            out.println("                    <td class=\"ValueLeft\">" + strCreated + "</td>");
            out.println("                </tr>");
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">Date Last Accessed</td>");
            out.println("                    <td class=\"ValueLeft\">" + strAccessed + "</td>");
            out.println("                </tr>");
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">ID</td>");
            out.println("                    <td class=\"ValueLeft\">" + ses.getId() + "</td>");
            out.println("                </tr>");
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">New</td>");
            out.println("                    <td class=\"ValueLeft\">" + ses.isNew() + "</td>");
            out.println("                </tr>");
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">Timeout</td>");
            if(intSeconds < 0)
            {
                out.println("                    <td class=\"ValueLeft\">" + "never" + "</td>");
            }
            else
            {
                out.print("                    <td class=\"ValueLeft\">");
                out.print(intSeconds);
                out.print(" second");
                if(intSeconds > 1)
                {
                    out.print("s");
                }
                out.print(" (");
                out.print(intMinutes);
                out.print(" minute");
                if(intMinutes > 1)
                {
                    out.print("s");
                }
                out.print(")");
                out.println("</td>");
            }
            out.println("                </tr>");
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">Time Remaining</td>");
            if(intSeconds < 0)
            {
                out.println("                    <td class=\"ValueLeft\">" + "forever" + "</td>");
            }
            else
            {
                out.print("                    <td class=\"ValueLeft\">");
                out.print(lngSecondsRemaining);
                out.print(" second");
                if(lngSecondsRemaining > 1)
                {
                    out.print("s");
                }
                out.print(" (");
                out.print(lngMinutesRemaining);
                out.print(" minute");
                if(lngMinutesRemaining > 1)
                {
                    out.print("s");
                }
                out.print(")");
                out.println("</td>");
            }
            out.println("                </tr>");
        }
        else
        {
            out.println("                <tr>");
            out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
            out.println("                </tr>");
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of session attributes.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @param ses an HTTP session.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteSessionAttributes(PrintWriter out, HttpServletRequest req, HttpSession ses) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        if(ses == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"SessionAttributes\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Session Attributes</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Enumeration e = ses.getAttributeNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values and class names.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
            String strName = (String)i.next();
            Object obj = ses.getAttribute(strName);
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">" + strName + "</td>");
            out.println("<td class=\"ValueLeft\">");
            if(obj == null)
            {
                out.println(encodeXML("<null>"));
            }
            else
            {
                if(obj instanceof String)
                {
                    out.println(encodeXML((String)obj));
                }
                else
                {
                    out.println("<table border=\"1\" width=\"100%\">");
                    out.println("    <tr>");
                    out.println("        <td>");
                    out.println("            instanceof");
                    out.println("        </td>");
                    out.println("        <td width=\"100%\">");
                    TreeSet<Object>ts = getClassList(obj);
                    if(ts.contains("javax.rmi.CORBA.Stub"))
                    {
                        out.println(encodeXML("<ejb>"));
                    }
                    else
                    {
                   Iterator<Object>  its = ts.iterator();
                    while(its.hasNext())
                    {
                        String s = (String)its.next();
                        if(s.equals("java.lang.Object"))
                        {
                            continue;
                        }
                        out.println(s);
                        out.println("<br/>");
                    }
                    }
                    out.println("        </td>");
                    out.println("    </tr>");
                    out.println("    <tr>");
                    out.println("        <td>");
                    out.println("            value");
                    out.println("        </td>");
                    out.println("        <td width=\"100%\">");
                    if(ts.contains("javax.rmi.CORBA.Stub"))
                    {
                        out.println(encodeXML("<ejb>"));
                    }
                    else
                    {
                        out.println("            " + obj.toString());
                    }
                    out.println("        </td>");
                    out.println("    </tr>");
                    out.println("</table>");
                }
            }
            out.println("</td>");
            out.println("                </tr>");
            out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of request information.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteRequestInformation(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        //  Setup ability to switch HTTP request method.
        //  If you came to this servlet by way of a GET, you can refresh the page by doing a POST.
        //  Or if you POSTed to this servlet, you can refresh it with a GET if you want to.
        boolean booGET = false;
        boolean booPOST = false;
        String strMethod = req.getMethod();
        if(strMethod.equalsIgnoreCase("GET"))
        {
            booGET = true;
        }
        if(strMethod.equalsIgnoreCase("POST"))
        {
            booPOST = true;
        }
        String strContextPath = req.getContextPath();
        if(strContextPath.equals(""))
        {
            strContextPath = NBSP;
        }
        out.println("        <h6>");
        out.println("            <a name=\"RequestInformation\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Request Information</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Authentication Type</td>");
        out.println("                    <td class=\"ValueLeft\">" + encodeXML(req.getAuthType()) + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Character Encoding</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getCharacterEncoding() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Content Length</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getContentLength() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Content Type</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getContentType() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Context Path</td>");
        out.println("                    <td class=\"ValueLeft\">" + strContextPath + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Locale</td>");
        out.println("                    <td class=\"ValueLeft\">");
        Locale l = req.getLocale();
        out.print(l.getLanguage());
        out.print("_");
        out.print(l.getCountry());
        if(!l.getVariant().equals(""))
        {
            out.print("_");
            out.print(l.getVariant());
        }
        out.print(" = ");
        out.print(l.getDisplayLanguage());
        out.print(", ");
        out.print(l.getDisplayCountry());
        if(!l.getDisplayVariant().equals(""))
        {
            out.print(", ");
            out.print(l.getDisplayVariant());
        }
        out.println();
        out.println("</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Method</td>");
        out.println("                    <td class=\"ValueLeft\">");
        if(booGET == true)
        {
        out.println("                        <form id=\"frmSwitch\" name=\"frmSwitch\" method=\"post\" action=\"debug\" enctype=\"" + FORM_STRING + "\">");
        }
        out.println("                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" summary=\"\" width=\"100%\">");
        out.println("                                <tr>");
        out.println("                                    <td class=\"ValueLeft\">" + strMethod + "</td>");
        out.println("                                    <td class=\"ValueRight\">");
        if(booGET == true)
        {
        out.println("                                        <br />");
        out.println("                                        <input type=\"submit\" id=\"subSwitch\" name=\"subSwitch\" value=\"Switch To POST\" class=\"Button\" />");
        }
        if(booPOST == true)
        {
        out.println("                                        <a href=\"debug\">Switch To GET</a>");
        }
        if  (
            (booGET == false)
        &&  (booPOST == false)
            )
        {
        out.println("                                        &nbsp;");
        }
        out.println("                                    </td>");
        out.println("                                </tr>");
        out.println("                            </table>");
        if(booGET == true)
        {
        out.println("                        </form>");
        }
        out.println("                    </td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Path Info</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getPathInfo() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Path Translated</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getPathTranslated() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Protocol</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getProtocol().trim() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Query String</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getQueryString() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Remote Address</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getRemoteAddr() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Remote Host</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getRemoteHost() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Remote User</td>");
        out.println("                    <td class=\"ValueLeft\">" + encodeXML(req.getRemoteUser()) + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Request URI</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getRequestURI() + "</td>");
        out.println("                </tr>");
      /*  StringBuffer sb = req.getRequestURL();
        String strURL = sb.toString();
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Request URL</td>");
        out.println("                    <td class=\"ValueLeft\">" + strURL + "</td>");
        out.println("                </tr>");
        */
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Requested Session ID</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getRequestedSessionId() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Requested Session ID<br/>Came From A Cookie</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.isRequestedSessionIdFromCookie() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Requested Session ID<br/>Came From A URL</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.isRequestedSessionIdFromURL() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Requested Session ID Is Valid</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.isRequestedSessionIdValid() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Scheme</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getScheme() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Secure</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.isSecure() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Server Name</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getServerName() + "</td>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Server Port</td>");
        out.println("                    <td class=\"ValueLeft\">" + req.getServerPort() + "</td>");
        out.println("                </tr>");
        String strServletPath = req.getServletPath();
        if(strServletPath != null)
        {
            if(strServletPath.equalsIgnoreCase(""))
            {
                strServletPath = NBSP;
            }
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">Servlet Path</td>");
        out.println("                    <td class=\"ValueLeft\">" + strServletPath + "</td>");
        out.println("                </tr>");
        Principal principal = req.getUserPrincipal();
        String strPrincipal = null;
        if(principal != null)
        {
            strPrincipal = encodeXML(principal.getName());
        }
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">User Principal</td>");
        out.println("                    <td class=\"ValueLeft\">" + strPrincipal + "</td>");
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of request attributes.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteRequestAttributes(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"RequestAttributes\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Request Attributes</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Enumeration e = req.getAttributeNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        Object obj = req.getAttribute(strName);
        String strValue = (obj == null) ? "<null>" : obj.toString();
        strValue = encodeXML(strValue);
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of request headers.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteRequestHeaders(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"RequestHeaders\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Request Headers</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Enumeration e = req.getHeaderNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
        String strName = (String)i.next();
        String strValue = req.getHeader(strName);
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of request cookies.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteRequestCookies(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"RequestCookies\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Request Cookies</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of cookies.
        Cookie[] cookies = req.getCookies();
        //  If there are none, tell the user that.
        if(cookies == null)
        {
        out.println("                <tr>");
        out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
        out.println("                </tr>");
        }
        else
        {
            if(cookies.length == 0)
            {
                out.println("                <tr>");
                out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
                out.println("                </tr>");
            }
        }
        //  Sort the names.
        SortedMap sm = new TreeMap();
        String strName = null;
        String strValue = null;
        if(cookies != null)
        {
            for(int x=0; x<cookies.length; x++)
            {
                strName = cookies[x].getName();
                strValue = cookies[x].getValue();
                sm.put(strName, strValue);
            }
        }
        //  Get names and values.
        Set s = sm.keySet();
       Iterator<Object>  i = s.iterator();
        while(i.hasNext())
        {
        strName = (String)i.next();
        strValue = (String)sm.get(strName);
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + strName + "</td>");
        out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
        out.println("                </tr>");
        out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes a table of request parameters.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteRequestParameters(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        if(req == null)
        {
            return;
        }
        out.println("        <h6>");
        out.println("            <a name=\"RequestParameters\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Request Parameters</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        //  Get a list of names.
        Enumeration e = req.getParameterNames();
        //  If there are none, tell the user that.
        if(!e.hasMoreElements())
        {
            out.println("                <tr>");
            out.println("                    <td colspan=\"2\" class=\"Name\">None.</td>");
            out.println("                </tr>");
        }
        //  Sort the names.
        SortedSet ss = new TreeSet<Object>();
        while(e.hasMoreElements())
        {
            ss.add((String)e.nextElement());
        }
        //  Get values.
        String strName = null;
        String strValue = null;
        String[] strValues = null;
        StringBuffer sb = null;
        int x = 0;
        int y = 0;
       Iterator<Object>  i = ss.iterator();
        while(i.hasNext())
        {
            strName = (String)i.next();
            //  Get all values.
            strValues = req.getParameterValues(strName);
            sb = new StringBuffer();
            x = 0;
            y = strValues.length;
            //  Print a single value if that's all there is.
            if(y == 1)
            {
                strValue = encodeXML(strValues[0]);
            }
            //  Or print all values if there are more than one.
            else
            {
                for(x=0; x<y; x++)
                {
                    sb.append("[");
                    if
                    (
                        (y > 10)
                    &&  (x < 10)
                    )
                    {
                        sb.append("0");
                    }
                    sb.append(x);
                    sb.append("]");
                    sb.append("=");
                    sb.append("&quot;");
                    sb.append(encodeXML(strValues[x]));
                    sb.append("&quot;");
                    sb.append("<br/>");
                }
                strValue = sb.toString();
            }
            out.println("                <tr>");
            out.println("                    <td class=\"Name\">" + strName + "</td>");
            out.println("                    <td class=\"ValueLeft\">" + strValue + "</td>");
            out.println("                </tr>");
            out.flush();
        }
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes links to the server logs.
     *  @param out an output stream.
     *  @param req an HTTP servlet request.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteServerLogs(PrintWriter out, HttpServletRequest req) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        String strAppLog = System.getProperty("java.home");
        String strWebLog = System.getProperty("user.dir");
        StringBuffer sbApp = new StringBuffer();
        sbApp.append(strAppLog);    sbApp.append(File.separator);
        sbApp.append("..");         sbApp.append(File.separator);
        sbApp.append("bin");        sbApp.append(File.separator);
        sbApp.append("nedss.log");
        StringBuffer sbWeb = new StringBuffer();
        sbWeb.append(strWebLog);    sbWeb.append(File.separator);
        sbWeb.append("nedss.log");
        String strApp = sbApp.toString();
        String strWeb = sbWeb.toString();
        File fApp = new File(strApp);
        File fWeb = new File(strWeb);
        String strAppFilename = fApp.getCanonicalPath();
        String strWebFilename = fWeb.getCanonicalPath();
        String strAppURL = req.getRequestURI() + "/get/app.nedss.log";
        String strWebURL = req.getRequestURI() + "/get/web.nedss.log";
        out.println("        <h6>");
        out.println("            <a name=\"ServerLogs\">&nbsp;</a>");
        out.println("        </h6>");
        out.println("        <table border=\"1\" cellpadding=\"4\" cellspacing=\"2\" summary=\"\" width=\"100%\">");
        out.println("            <thead>");
        out.println("                <tr>");
        out.println("                    <th colspan=\"2\" class=\"TableHeader\">Server Logs</th>");
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <th class=\"ColumnHeaderName\">Name</th>");
        out.println("                    <th class=\"ColumnHeaderValue\">Value</th>");
        out.println("                </tr>");
        out.println("            </thead>");
        out.println("            <tbody>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + "App Server" + "</td>");
        if(fApp.exists())
        {
        out.println("                    <td class=\"ValueLeft\"><a href=\"" + strAppURL + "\">" + strAppFilename + "</a></td>");
        }
        else
        {
        out.println("                    <td class=\"ValueLeft\">" + strAppFilename + "</td>");
        }
        out.println("                </tr>");
        out.println("                <tr>");
        out.println("                    <td class=\"Name\">" + "Web Server" + "</td>");
        if(fWeb.exists())
        {
        out.println("                    <td class=\"ValueLeft\"><a href=\"" + strWebURL + "\">" + strWebFilename + "</a></td>");
        }
        else
        {
        out.println("                    <td class=\"ValueLeft\">" + strWebFilename + "</td>");
        }
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.flush();
    }

    /**
     *  Writes closing tags.
     *  @param out an output stream.
     *  @throws ServletException if the request can not be handled.
     *  @throws IOException if an I/O error occurs.
     */
    private void WriteTail(PrintWriter out) throws ServletException, IOException
    {
        //  Verify parameters.
        if(out == null)
        {
            return;
        }
        out.println("    </body>");
        out.println("</html>");
        out.flush();
    }

}
