package gov.cdc.nedss.webapp.nbs.action.reportadmin;

import gov.cdc.nedss.exception.NEDSSDAOSysException;
import gov.cdc.nedss.reportadmin.dao.DataSourceColumnDAO;
import gov.cdc.nedss.reportadmin.dao.DataSourceDAO;
import gov.cdc.nedss.reportadmin.dt.DataSourceDT;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Creates records in the Data_Source table.
 * @author Ed Jenkins
 */
public class CreateDataSource extends Action
{

    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(CreateDataSource.class);

    /**
     * DAO for Data_Source.
     */
    private static final DataSourceDAO daoDataSource = new DataSourceDAO();

    /**
     * DAO for Data_source_column.
     */
    private static final DataSourceColumnDAO daoDataSourceColumn = new DataSourceColumnDAO();

    /**
     * Constructor.
     */
    public CreateDataSource()
    {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        NBSSecurityObj sec = (NBSSecurityObj)session.getAttribute("NBSSecurityObject");
        String strRedirect = "";
        try
        {
            // Return Link
            String strLinkName = "Return to Data Sources";
            String strLinkAddr = "/nbs/ListDataSource.do";
            request.setAttribute("LinkName", strLinkName);
            request.setAttribute("LinkAddr", strLinkAddr);
            long next_data_source_uid = daoDataSource.getNextUID();
            strRedirect = "/ViewDataSource.do?data_source_uid=" + next_data_source_uid;
            DataSourceDT dtDataSource = (DataSourceDT)session.getAttribute("ReportAdmin.dtDataSource");
            dtDataSource.setDataSourceUID(next_data_source_uid);
            dtDataSource.setDataSourceName(request.getParameter("data_source_name").trim());
            dtDataSource.setDataSourceTitle(request.getParameter("data_source_title"));
            dtDataSource.setDescTxt(request.getParameter("desc_txt"));
            if(request.getParameter("jurisdiction_security") == null)
            {
                dtDataSource.setJurisdictionSecurity("N");
            }
            else
            {
                dtDataSource.setJurisdictionSecurity("Y");
            }
            
            if(request.getParameter("reporting_facility_security") == null)
            {
                dtDataSource.setReporting_facility_security("N");
            }
            else
            {
                dtDataSource.setReporting_facility_security("Y");
            }
            //dtDataSource.validate();
            daoDataSource.validate(dtDataSource);
            daoDataSource.add(dtDataSource);
            session.setAttribute("ReportAdmin.dtDataSource", dtDataSource);
            ArrayList<Object> alDataSourceColumn = daoDataSourceColumn.init(dtDataSource);
            daoDataSourceColumn.init(alDataSourceColumn);
            session.setAttribute("ReportAdmin.alDataSourceColumn", alDataSourceColumn);
            request.setAttribute("error", "The columns listed below have been created for you with default values.  Please review each column and make corrections as needed.");
            ArrayList<Object> alReport = new ArrayList<Object> ();
            session.setAttribute("ReportAdmin.alReport", alReport);
        }
        catch(Exception ex)
        {
            logger.error("Error in Report  CreateDataSource: " +ex.getMessage());
            request.setAttribute("error", ex.getMessage());
            return mapping.findForward("error");
        }
        ActionForward af = new ActionForward();
        af.setName("ViewDataSource");
        af.setPath(strRedirect);
        af.setRedirect(true);
        return af;
     
    }

}