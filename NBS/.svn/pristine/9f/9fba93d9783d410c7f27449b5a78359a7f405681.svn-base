package gov.cdc.nedss.webapp.nbs.action.client;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

/**
 * ClientVO: parent ClientVO for all ClientVO
 * @author Pradeep Sharma
 *
 */
public class ClientVO {
	private int unKnownRace  =0;
	private int americanIndianAlskanRace  =0;
	private int africanAmericanRace  =0;
	private int whiteRace  =0;
	private Map<Object,Object> answerMap = new HashMap<Object,Object>();
	protected Map<Object,Object> answerArrayMap = new HashMap<Object,Object>();
	private Map<Object,Object> hivAnswerMap = new HashMap<Object,Object>();
	private Map<Object,Object> hivAnswerBatchMap = new HashMap<Object,Object>();
	private int asianRace;
	private int hawaiianRace;
	private int otherRace;
	private int notAsked;
	private int refusedToAnswer;
	public int getUnKnownRace() {
		return unKnownRace;
	}
	public void setUnKnownRace(int unKnownRace) {
		this.unKnownRace = unKnownRace;
	}
	public int getAmericanIndianAlskanRace() {
		return americanIndianAlskanRace;
	}
	public void setAmericanIndianAlskanRace(int americanIndianAlskanRace) {
		this.americanIndianAlskanRace = americanIndianAlskanRace;
	}
	public int getAfricanAmericanRace() {
		return africanAmericanRace;
	}
	public void setAfricanAmericanRace(int africanAmericanRace) {
		this.africanAmericanRace = africanAmericanRace;
	}
	public int getWhiteRace() {
		return whiteRace;
	}
	public void setWhiteRace(int whiteRace) {
		this.whiteRace = whiteRace;
	}
	
	public Map<Object, Object> getAnswerArrayMap() {
		return answerArrayMap;
	}
	public void setAnswerArrayMap(Map<Object, Object> answerArrayMap) {
		this.answerArrayMap = answerArrayMap;
	}
	public int getAsianRace() {
		return asianRace;
	}
	public void setAsianRace(int asianRace) {
		this.asianRace = asianRace;
	}
	public int getHawaiianRace() {
		return hawaiianRace;
	}
	public void setHawaiianRace(int hawaiianRace) {
		this.hawaiianRace = hawaiianRace;
	}
	public void setAnswerArray(String key, String[] answer) {
		if(answer.length > 0) {
			String [] answerList = new String[answer.length];
			boolean selected = false;
			for(int i=1; i<=answer.length; i++) {
				String answerTxt = answer[i-1];
				if(!answerTxt.equals("")) {
					selected = true;
					answerList[i-1] = answerTxt;
				}
			}
			if(selected)
				answerArrayMap.put(key,answerList);
		}
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		 unKnownRace  =0;
		 americanIndianAlskanRace  =0;
		 africanAmericanRace  =0;
		 whiteRace  =0;
		 answerMap = new HashMap<Object,Object>();
		 answerArrayMap = new HashMap<Object,Object>();
		 asianRace = 0;
		 hawaiianRace = 0;
		 otherRace = 0;
		 notAsked = 0;
		 refusedToAnswer = 0;
	}
	public String[] getAnswerArray(String key) {
		return (String[])answerArrayMap.get(key);
	}
	public Map<Object, Object> getAnswerMap() {
		return answerMap;
	}
	public void setAnswerMap(Map<Object, Object> answerMap) {
		this.answerMap = answerMap;
	}
	public String getAnswer(String key) {
		return (String)answerMap.get(key);
	}

	public void setAnswer(String key, String answer) {
		answerMap.put(key,answer );
	}
	public Map<Object,Object> getArrayAnswerMap() {
		return answerArrayMap;
	}
	public void setArrayAnswerMap(Map<Object, Object>  answerArrayMap) {
		this.answerArrayMap = answerArrayMap;
	}
	/**
	 * @return the hivAnswerMap
	 */
	public Map<Object, Object> getHivAnswerMap() {
		return hivAnswerMap;
	}
	/**
	 * @param hivAnswerMap the hivAnswerMap to set
	 */
	public void setHivAnswerMap(Map<Object, Object> hivAnswerMap) {
		this.hivAnswerMap = hivAnswerMap;
	}
	/**
	 * @return the hivAnswerBatchMap
	 */
	public Map<Object, Object> getHivAnswerBatchMap() {
		return hivAnswerBatchMap;
	}
	/**
	 * @param hivAnswerBatchMap the hivAnswerBatchMap to set
	 */
	public void setHivAnswerBatchMap(Map<Object, Object> hivAnswerBatchMap) {
		this.hivAnswerBatchMap = hivAnswerBatchMap;
	}
	/**
	 * @return the otherRace
	 */
	public int getOtherRace() {
		return otherRace;
	}
	/**
	 * @param otherRace the otherRace to set
	 */
	public void setOtherRace(int otherRace) {
		this.otherRace = otherRace;
	}
	/**
	 * @return the refusedToAnswer
	 */
	public int getRefusedToAnswer() {
		return refusedToAnswer;
	}
	/**
	 * @param refusedToAnswer the refusedToAnswer to set
	 */
	public void setRefusedToAnswer(int refusedToAnswer) {
		this.refusedToAnswer = refusedToAnswer;
	}
	public int getNotAsked() {
		return notAsked;
	}
	public void setNotAsked(int notAsked) {
		this.notAsked = notAsked;
	}

}