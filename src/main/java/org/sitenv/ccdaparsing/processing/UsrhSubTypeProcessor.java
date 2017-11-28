package org.sitenv.ccdaparsing.processing;

import java.util.List;
import java.util.concurrent.Future;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.sitenv.ccdaparsing.model.CCDAII;
import org.sitenv.ccdaparsing.model.UsrhII;
import org.sitenv.ccdaparsing.model.UsrhSubType;
import org.sitenv.ccdaparsing.util.ApplicationConstants;
import org.sitenv.ccdaparsing.util.ApplicationConstants.UsrhIISubTypes;
import org.sitenv.ccdaparsing.util.ApplicationUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Service
public class UsrhSubTypeProcessor {
	
	@Async()
	public Future<UsrhSubType> retrieveUsrhSubTypeDetails(XPath xPath, Document doc)
			throws XPathExpressionException {		
		List<CCDAII> iiElements = null;
		try {
			iiElements = ApplicationUtil.readTemplateIdList((NodeList) xPath
					.compile(ApplicationConstants.USRH_SUB_TYPE_EXPRESSION)
					.evaluate(doc, XPathConstants.NODESET), false);
		} catch (TransformerException e) {
			e.printStackTrace();
		}		
		return   new AsyncResult<UsrhSubType>(extractUsrhSubType(iiElements));
	}
	
	private UsrhSubType extractUsrhSubType(List<CCDAII> iiElements) {
		UsrhSubType resultingDocumentType = null;
		if (iiElements != null && iiElements.size() > 0) {
			//compare extracted templateID root values with the constant document type root values
			for(CCDAII xmlII : iiElements) {
				for(UsrhII constantUsrhII : UsrhIISubTypes.ccdaDocumentTypes) {
					if(xmlII.getRootValue().equals(constantUsrhII.getRootValue())) {
						//halt and return doc type from first match
						return constantUsrhII.getUsrhSubType();
					}
				}
			}			
		}
		return resultingDocumentType;
	}
	
}
