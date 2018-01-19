package gov.cdc.nedss.webapp.nbs.action.pagemanagement.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gov.cdc.nedss.act.ctcontact.dt.CTContactAttachmentDT;
import gov.cdc.nedss.page.ejb.pageproxyejb.dt.NBSAttachmentDT;
import gov.cdc.nedss.proxy.ejb.investigationproxyejb.dao.RetrieveSummaryVO;
import gov.cdc.nedss.systemservice.ejb.mainsessionejb.bean.MainSessionCommand;
import gov.cdc.nedss.systemservice.nbscontext.NBSConstantUtil;
import gov.cdc.nedss.systemservice.nbscontext.NBSContext;
import gov.cdc.nedss.systemservice.nbssecurity.NBSSecurityObj;
import gov.cdc.nedss.systemservice.util.MainSessionHolder;
import gov.cdc.nedss.util.JNDINames;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;
import gov.cdc.nedss.util.StringUtils;
import gov.cdc.nedss.webapp.nbs.action.util.CallProxyEJB;
import gov.cdc.nedss.webapp.nbs.action.util.FileUploadUtil;
import gov.cdc.nedss.webapp.nbs.form.util.FileUploadForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.apache.struts.upload.FormFile;

public class NBSFileUploadAction extends MappingDispatchAction {

	static final LogUtils logger = new LogUtils(NBSFileUploadAction.class
			.getName());

	public ActionForward showForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = mapping.findForward("success");
		return actionForward;
	}

	public ActionForward doUpload(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward actionForward = null;
		// Need to change this uid?
		String contactUid = "999999";
		try {
			FileUploadForm form = (FileUploadForm) aForm;

			// proceed if the file is of valid size
			FormFile file = form.getCtFile();
			int maxSizeInMB = PropertyUtil.getInstance()
					.getMaxFileAttachmentSizeInMB();
			long maxSizeInBytes = maxSizeInMB * 1024 * 1024;
			if (file.getFileSize() < maxSizeInBytes) {
				// Construct NBSCaseAttachmentDT from form and get it
				// out to back end...attachment, desc_txt, file_nm_txt
				NBSAttachmentDT dt = new NBSAttachmentDT();
				dt.setItNew(true);
				dt.setAttachmentParentUid(new Long(-1));

				// TO DO Public Health case uid should be retrieved from
				// NBSContext or session when user clicks on
				// Public Health case Id on Events Tab

				String publicHealthCaseUid = (String) request.getSession()
						.getAttribute("DSInvUid");
				if (publicHealthCaseUid != null)
					dt.setAttachmentParentUid(Long
							.parseLong(publicHealthCaseUid));
				// Retrieve addUserId from SecurityObject
				NBSSecurityObj secObj = (NBSSecurityObj) request.getSession()
						.getAttribute("NBSSecurityObject");
				String userId = secObj.getTheUserProfile().getTheUser()
						.getEntryID();
				dt.setAddUserId(Long.valueOf(userId));
				dt.setLastChgUserId(Long.valueOf(userId));
				dt.setLastChgTime(new Timestamp(new Date().getTime()));

				// FileName
				String fileNm = String.valueOf(form.getFileName());
				dt.setFileNmTxt(fileNm);

				// FileDescription
				String descTxt = String.valueOf(form.getFileDescription());
				dt.setDescTxt(descTxt);

				// Attachment
				byte[] fileData = file.getFileData();
				dt.setAttachment(fileData);

				// type code
				// For Investigation the type code is set as CASE
				dt.setTypeCd(NEDSSConstants.INVESTIGATION_CD);

				// Make EJB Call - Persistance
				Long resultUid = processRequest(dt, request.getSession());
				if (resultUid != null && resultUid.longValue() > 0) {
					request.setAttribute("confirmation", "true");
					dt.setNbsAttachmentUid(resultUid);
					request.setAttribute("newAttachment",
							_makeNewAttachmentRow(dt));
				}
			} else {
				form.reset();
				request.setAttribute("fileUploadForm", form);
				request.setAttribute("maxFileSizeExceeded", "true");
				request.setAttribute("maxFileSizeInMB", String
						.valueOf(maxSizeInMB));
			}
		} catch (Exception e) {
			logger.error("Exception in NBSFileUploadAction.doUpload = " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
		}

		actionForward = mapping.findForward("success");
		logger.debug("Leaving home:doUpload");
		return actionForward;
	}

	public ActionForward doDownload(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			// retrieve details from request parameter
			String nbsAttachmentUid = request
					.getParameter("nbsAttachmentUid");
			String fileName = request.getParameter("fileNmTxt");

			logger.debug("Download the file with id:" + nbsAttachmentUid
					+ "; and name:" + fileName);

			if (nbsAttachmentUid == null || fileName == null) {
				logger.error("1 or both attributes nbsAttachmentUid"
						+ " and fileName NOT found in the request parameter");
			}

			// get file contents
			MainSessionCommand msCommand = null;
			/*
			 * TO DO Call the correct method
			 */
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
			String sMethod = "getNBSAttachment";
			MainSessionHolder holder = new MainSessionHolder();
			msCommand = holder.getMainSessionCommand(request.getSession());

			Object[] oParams = { Long.valueOf(nbsAttachmentUid) };
			ArrayList<?> arr = msCommand.processRequest(sBeanJndiName, sMethod,
					oParams);
			byte[] theByteArray = (byte[]) arr.get(0);
			int size = theByteArray.length;

			// prepare response
			ServletContext context = request.getSession().getServletContext();
			String mimetype = context.getMimeType(fileName);
			response.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream");
			response.setHeader("cache-control", "must-revalidate");
			response.setContentLength(size);
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName);

			// write response
			ServletOutputStream op = response.getOutputStream();
			op.write(theByteArray, 0, size);
			op.flush();
			op.close();
		} catch (Exception e) {
			logger.error("Exception in NBSFileUploadAction.doDownload = " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
		}

		return null;
	}

	private static Long processRequest(NBSAttachmentDT dt, HttpSession session)
			throws Exception {

		MainSessionCommand msCommand = null;
		Long resultUid = null;
		try {
			// Change the EJB and use the correct method
			String sBeanJndiName = JNDINames.PAGE_PROXY_EJB;
			String sMethod = "setNBSAttachment";
			Object[] oParams = new Object[] { dt };
			MainSessionHolder holder = new MainSessionHolder();
			Object returnObj = CallProxyEJB.callProxyEJB(oParams,
					sBeanJndiName, sMethod, session);
			resultUid = (Long) returnObj;
		} catch (Exception ex) {
			logger
					.error("Error in FileUploadAction while  calling setNBSAttachment method : "
							+ ex.toString());
			throw new Exception(ex);
		}

		return resultUid;
	}

	private static String _makeNewAttachmentRow(NBSAttachmentDT dt) {

		StringBuffer sb = new StringBuffer("<td style=\"text-align: center;\">");

		String delLnk = "<a id=\"td_" + dt.getNbsAttachmentUid()
				+ "\" href=javascript:deleteAttachment(\""
				+ dt.getNbsAttachmentUid() + "\")>Delete</a>";
		dt.setDeleteLink(delLnk);
		sb.append(dt.getDeleteLink()).append("</td>");
		// Date Added
		String dateAdded = StringUtils.formatDate(dt.getLastChgTime());
		sb.append("<td class=\"dateField\">").append(dateAdded).append("</td>");
		// Added By
		RetrieveSummaryVO rsVO = new RetrieveSummaryVO();
		String userNm = rsVO.getUserName(dt.getLastChgUserId());
		sb.append("<td class=\"nameField\">").append(userNm).append("</td>");
		// File Name (with Link)
		HashMap<Object, Object> parameterMap = new HashMap<Object, Object>();
		parameterMap.put("nbsAttachmentUid", String.valueOf(dt
				.getNbsAttachmentUid()));
		parameterMap.put("fileNmTxt", dt.getFileNmTxt());
		FileUploadUtil util = new FileUploadUtil();
		dt.setViewLink(util.buildHyperLink("InvDownloadFile.do", parameterMap,
				null, dt.getFileNmTxt()));
        
		sb.append("<td>").append(dt.getViewLink()).append("</td>");
		// Description
		// Remove new line and Carriage return from the string 
		dt.setDescTxt(util.replaceNewLineCarRetn(dt.getDescTxt()));
		
		sb.append("<td>").append(dt.getDescTxt()).append("</td>");

		return sb.toString();
	}

}
