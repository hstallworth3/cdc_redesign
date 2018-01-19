package gov.cdc.nedss.pam.dao;

import gov.cdc.nedss.act.publichealthcase.vo.PublicHealthCaseVO;
import gov.cdc.nedss.exception.NEDSSSystemException;
import gov.cdc.nedss.nbsactentity.dao.NbsActEntityHistoryDAO;
import gov.cdc.nedss.util.DAOBase;
import gov.cdc.nedss.util.LogUtils;

import java.util.Collection;

/**
 * Name: NbsHistoryDAO.java Description: DAO Object for Pam History answers.
 * Copyright: Copyright (c) 2008 Company: Computer Sciences Corporation
 *
 * @author Pradeep Sharma
 */

public class NbsHistoryDAO extends DAOBase {

	private static final LogUtils logger = new LogUtils(NbsHistoryDAO.class
			.getName());

	public PublicHealthCaseVO getPamHistory(PublicHealthCaseVO publicHealthCaseVO)
	throws NEDSSSystemException {
		try{
			NbsActEntityHistoryDAO nbsActEntityHistoryDAO = new NbsActEntityHistoryDAO();
			Collection<Object>  pamEntityColl = nbsActEntityHistoryDAO.getPamCaseEntityDTCollection(publicHealthCaseVO.getThePublicHealthCaseDT());
			publicHealthCaseVO.setNbsCaseEntityCollection(pamEntityColl);
			Collection<Object>  pamAnswerColl = nbsActEntityHistoryDAO.getPamAnswerDTCollection(publicHealthCaseVO.getThePublicHealthCaseDT());
			publicHealthCaseVO.setNbsAnswerCollection(pamAnswerColl);
			return publicHealthCaseVO;
		}catch(Exception ex){
			logger.fatal("Exception  = "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
	}
	public void insertPamHistory(PublicHealthCaseVO publicHealthCaseVO,  PublicHealthCaseVO oldPublicHealthCaseVO)
	throws NEDSSSystemException {
		try{
			if (oldPublicHealthCaseVO.getThePublicHealthCaseDT() == null)
				return;
			NbsActEntityHistoryDAO nbsActEntityHistoryDAO = new NbsActEntityHistoryDAO();
			if ((publicHealthCaseVO.getNbsCaseEntityCollection() != null) && !(publicHealthCaseVO.getNbsCaseEntityCollection().isEmpty()))
				nbsActEntityHistoryDAO.insertPamEntityHistoryDTCollection( publicHealthCaseVO.getNbsCaseEntityCollection(), oldPublicHealthCaseVO.getThePublicHealthCaseDT());
			if (publicHealthCaseVO.getNbsAnswerCollection() != null && !(publicHealthCaseVO.getNbsAnswerCollection().isEmpty()))
				nbsActEntityHistoryDAO.insertPamAnswerHistoryDTCollection( publicHealthCaseVO.getNbsAnswerCollection(), oldPublicHealthCaseVO.getThePublicHealthCaseDT());
		}catch(Exception ex){
			logger.fatal("Exception  = "+ex.getMessage(), ex);
			throw new NEDSSSystemException(ex.toString());
		}
	}


	
}
