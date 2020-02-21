package aisha.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.BasicBean;
import aisha.bean.PlatformUser;
import aisha.bean.Talent;

 
//@Repository
public class BasicDAOImpl implements BasicDAO {
 
 @Autowired
 private SessionFactory sessionFactory;
 protected static Logger logger = Logger.getLogger(BasicDAOImpl.class);
 
 private Session getCurrentSession() {
  return sessionFactory.getCurrentSession();
 }

 @Override
 public long addBean(BasicBean basic) {
	 logger.debug("Inside method BasicDAOImpl.addBean");
	 logger.debug("inserting bean into db : " + basic);
  long result = (long) getCurrentSession().save(basic);
  logger.debug("Inside method BasicDAOImpl.addBean, after add bean with id :" + result);
  return result;
 }
 
 @Override
 //@Transactional
 //@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public BasicBean getBean(BasicBean bean) {
	 logger.debug("Inside method BasicDAOImpl.getBean");
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(bean.getClass());
	String name = bean.getClass().getSimpleName();
	
	if (bean.getSearchCriteria() != null)
	{
		List<String> keysList = new ArrayList<String>(bean.getSearchCriteria().keySet());
		Iterator<String> keyListIterator = keysList.iterator();
		while (keyListIterator.hasNext()) {
			String searchCriteriaKey = keyListIterator.next();
			
			
			Object searchCreteriaValue = bean.getSearchCriteria()
					.get(searchCriteriaKey);
			
			
			if(searchCreteriaValue != null && searchCreteriaValue.toString().isEmpty()!= true)

			{	

				if(searchCriteriaKey == "id")
				criteria.add(Restrictions.eq(searchCriteriaKey, Long.valueOf(String.valueOf(searchCreteriaValue))));
				else
					criteria.add(Restrictions.eq(searchCriteriaKey, searchCreteriaValue));
					
			}
			

		}
		
	}
	

		List<BasicBean> sList = null;
		logger.debug("Inside method BasicDAOImpl.getBean, get basic with id : "+bean.getId());
		sList=criteria.list();
		logger.debug("Inside method BasicDAOImpl.getBean, get basic result : "+bean.getId());

		
		System.out.println("################# :" + sList.size());
		try
		{logger.debug("Inside method BasicDAOImpl.getBean, get talent result : " + sList.get(0));
		return sList.get(0);}
		catch(Exception e)
		{System.out.println("#######  exception : "+e.getMessage());return null;}
	}

 
 @Override
 //@Transactional
 //@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public BasicBean getMyBean(BasicBean basic) {
	 logger.debug("Inside method BasicDAOImpl.getBean");
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(basic.getClass());
	String name = basic.getClass().getSimpleName();
		if(basic.getClass().getSimpleName().equals("PlatformUser"))
		{
		if(((PlatformUser)basic).getUserName() != null )
			criteria.add(Restrictions.eq("userName", ((PlatformUser)basic).getUserName()));
		else
			criteria.add(Restrictions.eq("id", ((PlatformUser)basic).getId()));
		}
		else if(basic.getClass().getSimpleName().equals("Talent"))
		criteria.add(Restrictions.eq("userId", ((Talent)basic).getUserId()));
		
		else if(basic.getClass().getSimpleName().equals("Talent") 
				|| basic.getClass().getSimpleName().equals("Startup")
				|| basic.getClass().getSimpleName().equals("Investor"))
		criteria.add(Restrictions.eq("userId", basic.getUserId()));
		
		else
		criteria.add(Restrictions.eq("id", Long.valueOf(String.valueOf(basic.getId()))));
		List<Talent> sList = null;
		logger.debug("Inside method BasicDAOImpl.getBean, get basic with id : "+basic.getId());
		sList=criteria.list();
		logger.debug("Inside method BasicDAOImpl.getBean, get basic result : "+basic.getId());

		
		System.out.println("################# :" + sList.size());
		System.out.println("################# :" + sList);
		try
		{logger.debug("Inside method BasicDAOImpl.getBean, get talent result : " + sList.get(0));
		return sList.get(0);}
		catch(Exception e)
		{System.out.println("#######  exception : "+e.getMessage());return null;}
	}

 
 @Override
 @Transactional
	public BasicBean listBeans(BasicBean bean) {
	 logger.debug("Inside method TalentDAOImpl.listBeans");
	 Criteria criteria= sessionFactory.getCurrentSession().createCriteria(bean.getClass());
	 String sDate1;
	 String sDate2;
		List<BasicBean> sList = null;
		if (bean.getSearchCriteria() != null)
		{
			List<String> keysList = new ArrayList<String>(bean.getSearchCriteria().keySet());
			Iterator<String> keyListIterator = keysList.iterator();
			while (keyListIterator.hasNext()) {
				String searchCriteriaKey = keyListIterator.next();
				
				
				Object searchCreteriaValue = bean.getSearchCriteria()
						.get(searchCriteriaKey);
				
				
				if(searchCreteriaValue != null && searchCreteriaValue.toString().isEmpty()!= true)

				{	
					 if (searchCriteriaKey.contains("Time")) {
						 HashMap<String, Object> dateFrame = new HashMap<>();
						 dateFrame = (HashMap<String, Object>) searchCreteriaValue;
						 if(searchCriteriaKey=="LastUpdateTime")
						 {
						  sDate1 = (String) dateFrame.get("fromModify");
						  sDate2 = (String) dateFrame.get("toModify");
						 }
						 else
						 {
							  sDate1 = (String) dateFrame.get("fromCreate");
							  sDate2 = (String) dateFrame.get("toCreate");
							 
						 }
						    Date date1 = new Date();
						    Date date2 = new Date();
							try {
								if(sDate1!=null)
								date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
								if(sDate2!=null)
								date2 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate2);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  
						    System.out.println(sDate1+"\t"+date1);  

						criteria.add(Restrictions.between(searchCriteriaKey,
								date1, date2));
					 }
					 else
					criteria.add(Restrictions.eq(searchCriteriaKey,
							searchCreteriaValue));
				}
				
				
			}
			
		}
/*	
	if (talent.isOrderAsc() == true) {
		criteria.addOrder(Order.asc(talent.getOrderBy()));
	} else {
		criteria.addOrder(Order.desc(talent.getOrderBy()));
	}*/
	
	logger.debug("Inside method TalentDAOImpl.listBeans, criteria :" + criteria);
	criteria.setProjection(null);
	//disabled for testing since no data
	//criteria.setFirstResult(talent.getFirstResults());
	//criteria.setMaxResults(talent.getMaxResults());

		
	
	sList=criteria.list();
	logger.debug("Inside method TalentDAOImpl.listBeans, result size :" + sList.size());
	logger.debug("Inside method TalentDAOImpl.listBeans, result list :" + sList);
	int numerOfRows = sList.size();
//	talent.setTotalResults(numerOfRows);
	
		bean.setResults(sList);
		logger.debug("Inside method TalentDAOImpl.listBeans, criteria :" );
		return bean;
		
	}


 @Override
	public void updateBean(BasicBean basic) {
	 logger.debug("Inside method TalentDAOImpl.updateTalent : " + basic);
		Session session = this.sessionFactory.getCurrentSession();
		session.update(basic);
		logger.debug("Inside method TalentDAOImpl.updateTalent, after update basic");
}

 @Override
 public List<String> getPriviliges(String packId)
 {
 SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT RESOURCE_ID FROM DEM_PACKAGE_RESOURCE WHERE PACKAGE_ID =" + packId);
	//List<Talent> sList = null;

	
	List<Object[]> sList = query.list();
	List<String> omg = new ArrayList<String>();
		Object[] row;
		for(int i = 0 ; i < sList.size() ; i++)
	{
			omg.add(String.valueOf(sList.get(i)));
		
	}

	return omg;
 }
}