package aisha.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.Application;
import aisha.bean.BasicBean;
import aisha.bean.Talent;
import aisha.controller.ApplicationController;
import aisha.dao.ApplicationTemplateDAO;
import aisha.dao.BasicDAO;
import aisha.dao.TalentDAO;
import aisha.security.beans.SystemUser;

 
@Service
@Transactional
public class ApplicationTemplateServiceImpl implements ApplicationTemplateService {
 
@Autowired
private BasicDAO appDAO;
protected static Logger logger = Logger.getLogger(ApplicationTemplateServiceImpl.class);


@Override
public long addApplicationTemplate(Application app) {
	logger.debug("Inside method ApplicationTemplateServiceImpl.addApplicationTemplate");
	return appDAO.addBean(app);
	
}


@Override
public Application listApplicationTemplate(Application app)
 {
	logger.debug("Inside method ApplicationTemplateServiceImpl.listApplicationTemplate");
	String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	HashMap<String, Object> criteria = new HashMap<>();
	/*if (x != "anonymousUser")
	{
		SystemUser currentUser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser.getUserRole().equals("Evaluator"))
		{
			
			criteria.put("appStatus", "Screened");
		}
		
	}*/
	//app.setCriteria(criteria);
	BasicBean result = this.appDAO.listBeans(app);
	List<BasicBean> resultList = new ArrayList<BasicBean>();
	resultList = result.getResults();

		result.setResults(resultList);
		return (Application) result;	
}

@Override
public Application getApplicationTemplate(Application app) {
	logger.debug("Inside method ApplicationTemplateServiceImpl.getApplicationTemplate");
	return (Application) this.appDAO.getBean(app);
}


@Override
public void updateApplicationTemplate(Application app) {
	logger.debug("Inside method ApplicationTemplateServiceImpl.updateApplicationTemplate");
int count = 0;


	if(app.getAppStatus() == "Screened")
	{
		logger.debug("Inside method ApplicationTemplateServiceImpl.updateApplicationTemplate, app status : " + app.getAppStatus());
for(int j = 0; j < app.getNoOfEvaluators() ; j++)
	{
		if(app.getEvaluation(j+1) != null)
		count = count +1;
	if(count == app.getNoOfEvaluators())	
		app.setAppStatus("Evaluated");

	}
	}
	logger.debug("Inside method ApplicationTemplateServiceImpl.updateApplicationTemplate, before calling appDAO.updateApplicationTemplat");
	this.appDAO.updateBean(app);
}
 
}