package aisha.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.Subscription;
import aisha.bean.PlatformUser;
import aisha.dao.BasicDAO;

 
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {


@Autowired
private BasicDAO basicDAO;

protected static Logger logger = Logger.getLogger(SubscriptionServiceImpl.class);


@Override
public long addSubscription(Subscription sub) {
	logger.debug("Inside method SubscriptionProfileServiceImpl.addSubscriptionProfile, add bean : " + sub);
	// profile.setCreationTime(new Date());
	sub.setStatus("active");
     //SystemUser currentUser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // profile.setCreatedBy(currentUser.getUserName());
	sub.setStatus("inserted");
	long result = basicDAO.addBean(sub);
	logger.debug("Inside method SubscriptionProfileServiceImpl.addSubscriptionProfile, after adding bean with id: " + result);
	return result;
	
}


@Override
public Subscription listSubscriptions(Subscription sub)
 {
	logger.debug("Inside method SubscriptionProfileServiceImpl.listSubscriptionProfiles");
		Subscription result =  (Subscription) this.basicDAO.listBeans(sub);
		logger.debug("Inside method SubscriptionProfileServiceImpl.listSubscriptionProfiles, after listing beans : " + result.getResults());
		return result;
	
}


@Override
public Subscription getSubscription(Subscription sub) {
	logger.debug("Inside method SubscriptionProfileServiceImpl.getSubscriptionProfile");
	Subscription result = (Subscription) this.basicDAO.getBean(sub);
	logger.debug("Inside method SubscriptionProfileServiceImpl.getSubscriptionProfile, after get SubscriptionProfile : " + result);
	return result;
}


@Override
public void updateSubscription(Subscription sub) {
	logger.debug("Inside method SubscriptionProfileServiceImpl.updateSubscriptionProfile");
	//profile.setLastModificationTime(new Date());
    PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //profile.setLastModifiedBy(currentUser.getUserName());
	this.basicDAO.updateBean(sub);
	logger.debug("Inside method SubscriptionProfileServiceImpl.updateSubscriptionProfile, after update SubscriptionProfile : " + sub);
}
 
}