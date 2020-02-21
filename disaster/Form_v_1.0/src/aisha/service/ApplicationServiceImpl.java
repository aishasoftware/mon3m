package aisha.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.Program;
import aisha.dao.ApplicationDAO;
import aisha.dao.BasicDAO;
import aisha.util.CurrentUser;

 
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
 
@Autowired
 private BasicDAO basicDAO;
 
@Autowired
private ApplicationDAO appDAO;


@Override
public long addApplication(Program app) {
	app.setStatus("create");
	app.setCreationTime(new Timestamp((new Date().getTime())));
	app.setCreatedBy(String.valueOf(CurrentUser.getUserId()));
	app.setInvestorId(Long.valueOf(CurrentUser.getEntityId()));
	return basicDAO.addBean(app);
	
}


@Override
public Program listApplications(Program app)
 {
	
		return (Program) this.basicDAO.listBeans(app);
	
}


@Override
public Program getApplication(int id) {
	// TODO Auto-generated method stub
	return null;
}


@Override
public void updateApplication(Program app) {
	// TODO Auto-generated method stub
	
}


/*@Override
public Program getApplication(int id) {
	// TODO Auto-generated method stub
	return this.appDAO.getApplication(id);
}


@Override
public void updateApplication(Program app) {
	// TODO Auto-generated method stub
	this.appDAO.updateApplication(app);
}*/
 
}