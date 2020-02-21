package aisha.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aisha.bean.Package;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.BasicBean;
import aisha.bean.Investor;
import aisha.bean.PlatformUser;
import aisha.bean.Startup;
import aisha.bean.Subscription;
import aisha.bean.Talent;
import aisha.security.services.SystemUserService;
import aisha.service.PackageService;
import aisha.service.SubscriptionService;
import aisha.service.TalentService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;
import aisha.util.bean.FieldAttributes;
@Controller
@RequestMapping(value = "/SubscriptionController")
public class SubscriptionController {
	protected static Logger logger = Logger.getLogger(SubscriptionController.class);
private static String thisBean = "Subscription";
	@Autowired
	private SubscriptionService service;
	@Autowired
	private SystemUserService sysService;
	@Autowired
	private PackageService packService;
	@Autowired
	private TalentService talentService;


	@RequestMapping(value = "/getSubscriptionList", method = RequestMethod.GET)
	public String getSubscriptionList(Model model, HttpServletRequest request) {
		logger.debug("Entering method ");
		String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (x != "anonymousUser")
		{
			model.addAttribute("role",  "Admin");
		}
		else
		model.addAttribute("role",  "guest");
		int pageNumber = 1;
		if (request.getParameter("currentPage") != null) {
			int currentPageNumber = Integer.parseInt(request.getParameter("currentPage"));
			if (request.getParameter("left") != null)
				pageNumber = currentPageNumber - 1;
			else if (request.getParameter("right") != null)
				pageNumber = currentPageNumber + 1;
		}
		pageNumber--;

		Subscription profile = new Subscription();
		profile.setFirstPage(pageNumber * 50);
		profile.setMaxResult(50);
		HashMap<String, Object> criteria = new HashMap<>();
		HashMap<String, Object> createDateFilter = new HashMap<>();
		HashMap<String, Object> modifyDateFilter = new HashMap<>();
		ArrayList<String> searchFields = Subscription.getSearchFields();
		model.addAttribute("beanName", "Subscription");
		model.addAttribute("controllerName", "SubscriptionController");
		for (int i = 0; i < searchFields.size(); i++) {
			if (searchFields.get(i).equals("fromCreate") || searchFields.get(i).equals("toCreate")) {
				if (request.getParameter(searchFields.get(i)) != null
						&& !request.getParameter(searchFields.get(i)).isEmpty())

				{
					createDateFilter.put(searchFields.get(i), request.getParameter(searchFields.get(i)));

				}
			}
			
			if (searchFields.get(i).equals("fromModify") || searchFields.get(i).equals("toModify")) {
				if (request.getParameter(searchFields.get(i)) != null
						&& !request.getParameter(searchFields.get(i)).isEmpty())

				{
					modifyDateFilter.put(searchFields.get(i), request.getParameter(searchFields.get(i)));

				}
			}
			/*if (!searchFields.get(i).equals("fromDate") && !searchFields.get(i).equals("toDate")
					&& request.getParameter(searchFields.get(i)) != null)
				criteria.put(searchFields.get(i), request.getParameter(searchFields.get(i)));*/
			
		}
		
		if (!createDateFilter.isEmpty())
			{
			criteria.put("creationTime", createDateFilter);
			}
		if (!modifyDateFilter.isEmpty())
		{
			criteria.put("LastUpdateTime", modifyDateFilter);
	    }
		
		if(!CurrentUser.getUserType().equals("1"))
		criteria.put("status", "active");
		profile.setSearchCriteria(criteria);
		Subscription profileList = subService.listSubscriptions(profile);
		List<BasicBean> resultList = profileList.getResults();
		int totalCount = profile.getTotalResult();
		model.addAttribute("beanList", resultList);
		model.addAttribute("tableFields", Subscription.getTableFields());
		Integer nOfRecords = totalCount;
		Integer nOfPages = (totalCount + 4) / 5;
		if (nOfPages == 0)
			nOfPages = 1;
		model.addAttribute("nOfRecords", nOfRecords);
		model.addAttribute("nOfPages", nOfPages);

		if (request.getParameter("currentPage") == null) {
			model.addAttribute("beanList", resultList);
			model.addAttribute("currentPage", "1");
		} else {

			Integer currentPage;
			if (request.getParameter("left") == null)
				currentPage = new Integer(request.getParameter("currentPage")) + 1;
			else
				currentPage = new Integer(request.getParameter("currentPage")) - 1;
			model.addAttribute("beanList", resultList);

			model.addAttribute("currentPage", currentPage.toString());
		}
		
		return "getAllBeans";

	}


	 @RequestMapping(value = "/addSubscription", method = RequestMethod.GET)
	 public String addSubscription(Model model) {
		 String thisOperation = "Add";

	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Subscription",  "SubscriptionController",  "Add", "platform-body-add", new Subscription(), new Subscription(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Subscription(), new Subscription()); 
				} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 	return "addBean";
	 }
	 
		@RequestMapping(value= "/submitAddSubscription", method = RequestMethod.POST)
		 public String submitAddSubscription(HttpServletRequest request, @ModelAttribute("bean") Subscription bean, BindingResult result, Model model) throws IOException{
			 String thisOperation = "SubmitAdd";
			 PlatformUser sysUser = new PlatformUser();
			 ArrayList<String> messages = new ArrayList<String>();
			 Long beanId = 0L;
			 
			try {

				
				  beanId = service.addBean(bean);	

					 PlatformUser user = new PlatformUser();
					 HashMap<String, Object> searchCriteria = new HashMap<>();
					 searchCriteria.put("id", bean.getPackUserId());
					 user.setSearchCriteria(searchCriteria);
					 user = sysService.getSystemUser(user);
					 user.setSubscriptionId(String.valueOf(bean.getPackageId()));
					 sysService.updateSystemUser(user);
					 
				  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
				 
							messages.add("Your prrofile creation request is recieved successfully, please wait 249 feedback");
							model.addAttribute("messages", messages);
							model = FormFields.fillModelGeneric( model, thisBean,   "View", bean, bean);
						
							
							}
		
			catch (NoSuchMethodException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} catch (SecurityException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
				
		     catch (IllegalAccessException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} 
			
			catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "addBean";
				}
			

	 @RequestMapping(value = "/viewMyProfile", method = RequestMethod.GET)
	 public String viewMyProfile(Model model) throws ParserConfigurationException, SAXException, IOException {
		 logger.debug("Entering method TalentController.viewProfile");
		 
		 logger.debug("Inside method TalentController.viewProfile, before set talent bean : ");
		 String userId = null ;

				PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				 userId = currentUser.getUserKey();
				 model.addAttribute("userName", currentUser.getUserName());
				 HashMap<String, Object> searchCriteria = new HashMap<>();
				 searchCriteria.put("id", userId);
				 //searchCriteria.put("status", "active");
	    Subscription search = new Subscription();
	    search.setSearchCriteria(searchCriteria);
	    Subscription result = subService.getSubscription(search);
		System.out.println("@@@@@@@@@@@@ talent serach : " + result);
		
	
	    model.addAttribute("fields",  FormFields.getFormFields("Subscription","view"));
	    model.addAttribute("beanName",  "Subscription");
		model.addAttribute("bean",  result);
	    model.addAttribute("controllerName","SubscriptionController");
	    logger.debug("Exiting method SubscriptionController.viewProfile");
	    
	    String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (x != "anonymousUser")
		{
			model.addAttribute("userType",  "platformuser");
		}
		else
		model.addAttribute("userType",  "guest");
		
		model.addAttribute("section",  "viewBean");
		model.addAttribute("linkKeys",  Subscription.getLinks().keySet());
		model.addAttribute("links",  Subscription.getLinks());
	 	return "addBean";
	 }
	 
	 @RequestMapping(value= "/submitUpdateSubscription", method = RequestMethod.POST)
	 public String submitUpdateSubscription(HttpServletRequest request, @RequestParam("oldBean") String oldBean,@ModelAttribute("bean") Subscription sub, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
	/*	 String myBean = request.getParameter("oldBean");
		 Subscription newBean = new Subscription();*/
		 ObjectMapper mapper = new ObjectMapper();
		 Subscription beanBack = mapper.readValue(oldBean, Subscription.class);
		 BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		 try {
			notNull.copyProperties(beanBack, sub);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	 newBean.setId(Long.valueOf(id));
		 newBean.setField1("test dynamic update");*/
		subService.updateSubscription(beanBack);
		logger.debug("Entering method TalentController.submitAddTalent");
		
		
		logger.debug("Inside method TalentController.submitAddTalent, before add bean : " + sub);
	
	logger.debug("Exiting method TalentController.submitAddTalent");
	
	ArrayList<String> messages = new ArrayList<String>();
	messages.add("Profile updated successfully!");
	try {
		model.addAttribute("fields",  FormFields.getFormFields("Subscription","view"));
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Subscription s= new Subscription();
    s.setUserId("0");
	model.addAttribute("bean",  beanBack);
	System.out.println("######################################### i am beanBack : "+beanBack);
	model.addAttribute("beanName", "Subscription");
	model.addAttribute("operation", "view");
	model.addAttribute("controllerName",  "SubscriptionController");
	model.addAttribute("messages",messages);
	
	return "addBean";
}
	 
	 
	 @RequestMapping(value = "/getTalentsList", method = RequestMethod.GET)
		public String getTalentsList(Model model, HttpServletRequest request) {
		//	logger.debug("Entering method ApplicationController.getSubmittedApplications");
			PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//model.addAttribute("role", currentUser.getUserRole());
			int pageNumber = 1;
			if (request.getParameter("currentPage") != null) {
				int currentPageNumber = Integer.parseInt(request.getParameter("currentPage"));
				if (request.getParameter("left") != null)
					pageNumber = currentPageNumber - 1;
				else if (request.getParameter("right") != null)
					pageNumber = currentPageNumber + 1;
			}
			pageNumber--;

			Talent talent = new Talent();
			
			talent.setFirstPage(pageNumber * 50);
			talent.setMaxResult(50);
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> dateFilter = new HashMap<>();
			ArrayList<String> searchFields;
			//= Talent.getSearchFields();
			model.addAttribute("controllerName", "TalentController");
			model.addAttribute("beanName", "Talent");
			Subscription sub = new Subscription();
			sub.setId(Long.valueOf(currentUser.getUserKey()));
			sub = subService.getSubscription(sub);
			//Talent usersList = talentService.listSubscriptionTalents(talent);
			//List<BasicBean> resultList = new ArrayList<BasicBean>();
		   // resultList = usersList.getResults();
			int totalCount = sub.getTalents().size();
	
			model.addAttribute("beanList", sub.getTalents());
			model.addAttribute("tableFields", Talent.getTableFields());
			Integer nOfRecords = totalCount;
			Integer nOfPages = (totalCount + 4) / 5;
			if (nOfPages == 0)
				nOfPages = 1;
			model.addAttribute("nOfRecords", nOfRecords);
			model.addAttribute("nOfPages", nOfPages);

/*			if (request.getParameter("currentPage") == null) {
				model.addAttribute("beanList", resultList);
				model.addAttribute("currentPage", "1");
			} else {

				Integer currentPage;
				if (request.getParameter("left") == null)
					currentPage = new Integer(request.getParameter("currentPage")) + 1;
				else
					currentPage = new Integer(request.getParameter("currentPage")) - 1;
				model.addAttribute("beanList", resultList);

				model.addAttribute("currentPage", currentPage.toString());
			}*/
			model.addAttribute("role","Admin");
			
			//logger.debug("Exiting method ApplicationController.getSubmittedApplications");
			return "getAllBeans";

		}
	 
	 
	 @RequestMapping(value="/getSubscriptionPackage", method=RequestMethod.GET)
	 public String getSubscriptionPackage(Model model,@RequestParam("id") Integer id) {
	   Package pack = new Package();
	   HashMap<String, Object> searchCriteria = new HashMap<>();
	   searchCriteria.put("id", id);
	   pack.setSearchCriteria(searchCriteria);
	   pack = packService.getPackage(pack);
       try {
		model = FormFields.fillModel( model,  "Subscription",  "SubscriptionController",  "view", "platform-body-view-get", pack,  pack ,"userMode");
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   return "addBean";
	 }
	 
	 @RequestMapping(value="/getSubscriptionAdminView", method=RequestMethod.GET)
	 public String getSubscriptionAdminView(Model model,@RequestParam("id") Integer id) {
	   Subscription sub = new Subscription();
	   HashMap<String, Object> searchCriteria = new HashMap<>();
	   searchCriteria.put("id", id);
	   sub.setSearchCriteria(searchCriteria);
	   sub = subService.getSubscription(sub);
	  

    	   model = FormFields.fillModel( model,  "Subscription",  "SubscriptionController",  "view", "platform-body-view-get", null,  sub ,"adminMode");
	

	   return "addBean";
	 }
	 
	 
	 @RequestMapping(value="/connectSubscription", method=RequestMethod.GET)
	 public String connectSubscription(Model model,@RequestParam("id") Integer id) {
		 logger.debug("Entering method TalentController.getTalent");
		 String subID = null ;
				String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
				if (x != "anonymousUser")
				{
					PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					//currentUser.getuserkey;
					subID = currentUser.getUserId();
				}
				
	Investor investor = new Investor();
	investor.setId(Long.valueOf("3637248"));
				Subscription sub = new Subscription();
				sub.getInvestors().add(investor);
				sub.setId(Long.valueOf(id));
				
				subService.updateSubscription(sub);
	   return "viewBean";
	 }
	 public class NullAwareBeanUtilsBean extends BeanUtilsBean{

		    @Override
		    public void copyProperty(Object dest, String name, Object value)
		            throws IllegalAccessException, InvocationTargetException {
		        if(value==null)return;
		        super.copyProperty(dest, name, value);
		    }

		}
}
