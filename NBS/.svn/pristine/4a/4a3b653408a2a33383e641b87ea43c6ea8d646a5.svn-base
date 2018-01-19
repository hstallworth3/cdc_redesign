package gov.cdc.nedss.webapp.nbs.servlet;

import gov.cdc.nedss.pagemanagement.wa.dt.BatchEntry;
import gov.cdc.nedss.util.LogUtils;
import gov.cdc.nedss.util.NEDSSConstants;
import gov.cdc.nedss.util.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

public class XSSFilter implements Filter, AjaxFilter  {
	
	static final LogUtils logger = new LogUtils(XSSFilter.class.getName());
	private static PropertyUtil pu = PropertyUtil.getInstance();
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain)

	throws IOException, ServletException {
		
	chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request),
				response);
	}

	@Override
	public Object doFilter(Object obj, Method method, Object[] params,
			AjaxFilterChain chain) throws Exception {
		// XSSRequestWrapper wrapper = new XSSRequestWrapper
		if(pu.getXSSFilterEnabled()!=null && pu.getXSSFilterEnabled().equals(NEDSSConstants.YES)){
		try {
			for (Object params1 : params) {
				if (params1 instanceof BatchEntry) {
					for (Entry<String, String> entry : ((BatchEntry) params1)
							.getAnswerMap().entrySet()) {
						((BatchEntry) params1).getAnswerMap().put(
								entry.getKey(), stripXSS(entry.getValue()));
					}
				}
			}
		} catch (Exception ex) {
			logger.debug("Could not strip XSS String");
		}
		}
		return chain.doFilter(obj, method, params);
	}

	private String stripXSS(String value) {

		if (value != null) {
			ArrayList<Pattern> patterns = XSSRequestWrapper.patterns;
			value = value.replaceAll("\0", "");
			// Remove all sections that match a pattern
			for (Pattern scriptPattern : patterns) {
				//logger.info("Value Before XSS Filter Strip - XSSFilter: "
				//		+ value);
				value = scriptPattern.matcher(value).replaceAll("");
				//logger.info("Value After XSS Filter Strip - XSSFilter: "
				//		+ value);
			}
		}
		return value;
	}
}
