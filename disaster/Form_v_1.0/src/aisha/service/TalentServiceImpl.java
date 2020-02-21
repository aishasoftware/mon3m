package aisha.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.BasicBean;
import aisha.dao.BasicDAO;


 
@Service
@Transactional
public class TalentServiceImpl implements TalentService {
 

@Autowired
private BasicDAO basicDAO;


protected static Logger logger = Logger.getLogger(TalentServiceImpl.class);


@Override
public long addBean(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.addBasicBean, add bean : " + talent);
	talent.setStatus("pending");
    talent.setCreationTime(new Timestamp((new Date()).getTime()));
	long result = basicDAO.addBean(talent);
	logger.debug("Inside method BasicBeanServiceImpl.addBasicBean, after adding bean with id: " + result);
	return result;
	
}


@Override
public BasicBean listBeans(BasicBean talent)
 {
	logger.debug("Inside method BasicBeanServiceImpl.listBasicBeans");
		BasicBean result = (BasicBean) this.basicDAO.listBeans(talent);
	//	logger.debug("Inside method BasicBeanServiceImpl.listBasicBeans, after listing beans : " + result.getBasicBeanList());
		return result;	
}

@Override
@Transactional
public BasicBean getBean(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean");
	BasicBean result = (BasicBean) this.basicDAO.getBean(talent);
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean, after get talent : " + result);
	return result;
}

@Override
public BasicBean getMyBean(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean");
	BasicBean result = (BasicBean) this.basicDAO.getMyBean(talent);
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean, after get talent : " + result);
	return result;
}


@Override
public void updateBean(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.updateBasicBean");
	
	this.basicDAO.updateBean(talent);
	logger.debug("Inside method BasicBeanServiceImpl.updateBasicBean, after update talent : " + talent);
}


@Override
public BasicBean listBeansCustom(BasicBean talent) {
	// TODO Auto-generated method stub
	return null;
}


@Override
public BasicBean listStartupBasicBeans(BasicBean talent) {
	// TODO Auto-generated method stub
	return null;
}

/*@Override
public BasicBean listStartupBasicBeans(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.updateBasicBean");
	
	BasicBean result = this.talentDAO.listStartupBasicBeans(talent);
	logger.debug("Inside method BasicBeanServiceImpl.updateBasicBean, after update talent : " + talent);
return result;
}
*/

/*
@Override
public BasicBean listBeansCustom(BasicBean talent) {
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean");
	BasicBean result = this.talentDAO.listBeansCustom(talent);
	logger.debug("Inside method BasicBeanServiceImpl.getBasicBean, after get talent : " + result);
	return result;
}*/

}
 
