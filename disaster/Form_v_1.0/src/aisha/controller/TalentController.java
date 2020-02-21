package aisha.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.Application;
import aisha.bean.BasicBean;
import aisha.bean.PlatformUser;
import aisha.bean.Startup;
import aisha.bean.Talent;
import aisha.controller.StartupController.NullAwareBeanUtilsBean;
import aisha.security.beans.SystemUser;
import aisha.security.services.SystemUserService;
import aisha.service.TalentService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;
import aisha.test.getFromXML;

@Controller
@RequestMapping(value="/TalentController")
public class TalentController {
	
	protected static Logger logger = Logger.getLogger(TalentController.class);
	public static String thisBean = "Talent";
	@Autowired
	private TalentService service; 
	@Autowired
	private SystemUserService userService;
	
	
	 @RequestMapping(value = "/addTalent", method = RequestMethod.GET)
	 public String addTalent(Model model) {
		 String thisOperation = "Add";
		 model.addAttribute("userName", CurrentUser.getUserName());
	     model.addAttribute("role", CurrentUser.getUserRole());
	     model.addAttribute("title", "Create Talent Profile");

	
	     try {
			//model = FormFields.fillModel( model,  "Talent",  "TalentController",  "Add", "platform-body-add", new Talent(), new Talent(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Talent(), new Talent()); 
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
	 
		@RequestMapping(value= "/submitAddTalent", method = RequestMethod.POST)
		 public String submitAddTalent(HttpServletRequest request, @ModelAttribute("bean") Talent bean, BindingResult result, Model model) throws IOException{
			 String thisOperation = "SubmitAdd";
			 model.addAttribute("userName", CurrentUser.getUserName());
		     model.addAttribute("role", CurrentUser.getUserRole());
		     model.addAttribute("title", "Create Talent Profile");

			 Class cls = null;
			 Talent childBean = (Talent) bean;
			 Method getField2 = null;
			 Method getField13 = null;
			 Method getField14 = null;
			 Method getField15 = null;
			 Method getField16 = null;
			 Object object = null;
			 PlatformUser sysUser = new PlatformUser();
			 ArrayList<String> messages = new ArrayList<String>();
			 Long beanId = 0L;
			 
			try {
				  cls = Class.forName("aisha.bean." + thisBean);
				  getField2 = cls.getDeclaredMethod("getField2");
				  getField13 = cls.getDeclaredMethod("getField13");
				  getField14 = cls.getDeclaredMethod("getField14");
				  getField15 = cls.getDeclaredMethod("getField15");
				  
				  BasicBean emptyBean = (BasicBean) Class.forName("aisha.bean."+ thisBean).newInstance();
				  beanId = service.addBean(bean);	  
				  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
				  sysUser.setUserName((String) getField13.invoke(childBean, null));
					if( !((String)getField14.invoke(childBean, null)).equals( (String)getField15.invoke(childBean, null)))			
					        {
								  messages.add("There is a mismatch between password and confirmed password");
								  model.addAttribute("messages", messages);
								  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
							} 
							
							sysUser.setEmail((String) getField2.invoke(childBean, null));
							sysUser.setUserType(thisBean);
							sysUser.setUserRole("ProfileOWner");
							sysUser.setUserKey(beanId.toString());
							sysUser.setPassword((String) getField14.invoke(childBean, null));
							sysUser.setConfirmedPassword((String) getField15.invoke(childBean, null));
							Long userId = userService.addSystemUser(sysUser);
							if(userId == null)
						    {
								messages.add("This user name is already used, please try another user name");
								model.addAttribute("messages", messages);
								model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
								return "addBean";
						    }
						
							
							messages.add("Your prrofile creation request is recieved successfully, please wait 249 feedback");
							model.addAttribute("messages", messages);
							model = FormFields.fillModelGeneric( model, thisBean,   "View", bean, bean);
						
							
							}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchMethodException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} catch (SecurityException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
				
		      catch (InstantiationException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} catch (IllegalAccessException e4) {
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
			

		
		
		@RequestMapping(value= "/viewMyProfile", method = RequestMethod.GET)
		 public String viewMyProfile(Model model) throws IOException{
			 String thisOperation = "ViewProfile";
			 model.addAttribute("userName", CurrentUser.getUserName());
		     model.addAttribute("role", CurrentUser.getUserRole());
		     model.addAttribute("title", "View Talent Profile");

			 Class cls = null;
			 Talent searchBean = new Talent();
			 BasicBean resultBean = new Talent();
			 Method getField2 = null;
			 Method getField13 = null;
			 Method getField14 = null;
			 Method getField15 = null;
			 Method getField16 = null;
			 Object object = null;
			 PlatformUser sysUser = new PlatformUser();
			 ArrayList<String> messages = new ArrayList<String>();
			 HashMap<String, Object> searchCriteria = new HashMap<>();
			 Long beanId = 0L;
			 
			try {
				  cls = Class.forName("aisha.bean." + thisBean);
				  getField2 = cls.getDeclaredMethod("getField2");
				  getField13 = cls.getDeclaredMethod("getField13");
				  getField14 = cls.getDeclaredMethod("getField14");
				  getField15 = cls.getDeclaredMethod("getField15");
				  
				  searchCriteria.put("id", CurrentUser.getUserKey());
				  searchBean.setSearchCriteria(searchCriteria);
				  resultBean = service.getBean(searchBean);	  
				  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, resultBean, resultBean);
				 
}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
		
		
		/* @RequestMapping(value = "/viewTalentApplications", method = RequestMethod.GET)
	 public String viewTalentApplications(HttpServletRequest request,Model model) throws ParserConfigurationException, SAXException, IOException {
		 logger.debug("Entering method TalentController.viewProfile");
		 
		 logger.debug("Inside method TalentController.viewProfile, before set talent bean : ");
		 int pageNumber = 1;

			if (request.getParameter("currentPage") != null) {
				int currentPageNumber = Integer.parseInt(request.getParameter("currentPage"));
				if (request.getParameter("left") != null)
					pageNumber = currentPageNumber - 1;
				else if (request.getParameter("right") != null)
					pageNumber = currentPageNumber + 1;
			}
			pageNumber--;

			Application app = new Application();
			app.setId(1);
			app.setFirstPage(pageNumber * 5);
			app.setMaxResult(5);
			Talent search = new Talent();
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> dateFilter = new HashMap<>();

			ArrayList<String> searchFields = TalentApplications.getSearchFields();
			search.setSearchCriteria(criteria);
		 model.addAttribute("tableFields",  TalentApplications.getTableFields());
		 
		 model.addAttribute("xmlFields",  getFromXML.getFormFields("Talent"));
	    model.addAttribute("controllerName","TalentController");
	    Talent result = talentService.listBeansCustom(new Talent());
	    
	    List<Talent> appList = result.getResults();
		int totalCount = appList.size();
		logger.debug(
				"Inside method ApplicationController.getApplicationList , after retrieving applications from database. no of records:"
						+ totalCount);
		Integer nOfRecords = totalCount;
		Integer nOfPages = (totalCount) / 2;
		if (nOfPages == 0)
			nOfPages = 1;
		model.addAttribute("nOfRecords", nOfRecords);
		model.addAttribute("nOfPages", nOfPages);

		if (request.getParameter("currentPage") == null) {
			model.addAttribute("beanList", appList);
			model.addAttribute("currentPage", "1");
		} else {

			Integer currentPage;
			if (request.getParameter("left") == null)
				currentPage = new Integer(request.getParameter("currentPage")) + 1;
			else
				currentPage = new Integer(request.getParameter("currentPage")) - 1;
			model.addAttribute("beanList", appList);

			model.addAttribute("currentPage", currentPage.toString());
		}


		
	    model.addAttribute("beanList",result.getResults());
	     logger.debug("Exiting method TalentController.viewProfile");
	 	return "JobTemp_viewTalentApplications";
	 }
*/	
	 @RequestMapping(value="/getTalent", method=RequestMethod.GET)
	 public String getTalent(Model model, @RequestParam("id") Integer id) {
		 String thisOperation = "Get";
		 model.addAttribute("userName", CurrentUser.getUserName());
	     model.addAttribute("role", CurrentUser.getUserRole());
	     model.addAttribute("title", "View Talent Profile");

		 Talent talent = new Talent();
		 BasicBean result = new Talent();
		 List<BasicBean> beanList = new ArrayList<BasicBean>();
		 //talent.setFirstResults(10);
		// talent.setMaxResults(10);
	
		 HashMap<String, Object> searchCriteria = new HashMap<>();
		   searchCriteria.put("id", id);
		   talent.setSearchCriteria(searchCriteria);
		 result = service.getBean(talent);
try
{
	model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, result, result);
	
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
		// logger.debug("Inside method TalentController.getTalentList, before list talents with criteria : " + talent.getFilter());

		 logger.debug("Exiting method TalentController.getTalentList");
		 return "addBean";
	
	 }
	 
	 @RequestMapping(value= "/submitUpdateTalent", method = RequestMethod.POST)
	 public String submitUpdateTalent(HttpServletRequest request, @RequestParam("oldBean") String oldBean,@ModelAttribute("bean") Talent bean, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
		 String thisOperation = "View";
		 model.addAttribute("userName", CurrentUser.getUserName());
	     model.addAttribute("role", CurrentUser.getUserRole());
	     model.addAttribute("title", "View Talent Profile");

		 Class cls = null;
		 Talent childBean = (Talent) bean;
		 Method getField2 = null;
		 Method getField13 = null;
		 Method getField14 = null;
		 Method getField15 = null;
		 Method getField16 = null;
		 Object object = null;
		 PlatformUser sysUser = new PlatformUser();
		 ArrayList<String> messages = new ArrayList<String>();
		 Long beanId = 0L;
		 HashMap<String, Object> searchCriteria = new HashMap<>();
		 
		try {
			
			  cls = Class.forName("aisha.bean." + thisBean);
			  getField2 = cls.getDeclaredMethod("getField2");
			  getField13 = cls.getDeclaredMethod("getField13");
			  getField14 = cls.getDeclaredMethod("getField14");
			  getField15 = cls.getDeclaredMethod("getField15");

			  searchCriteria.put("userKey", id);
				 sysUser.setSearchCriteria(searchCriteria);
				 sysUser = userService.getSystemUser(sysUser);
				 
				 if(!sysUser.getEmail().equals((String) getField2.invoke(childBean, null)))
				 {
					 sysUser.setEmail((String) getField2.invoke(childBean, null));
				     userService.updateSystemUser((sysUser));
				 }
				 
				 BasicBean savedBean = updateOldBean(oldBean,bean);
				 
			  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, savedBean, savedBean);
			  

} 	catch (ClassNotFoundException e) {
// TODO Auto-generated catch block
e.printStackTrace();
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
		
		messages.add("Profile updated successfully!");
		model.addAttribute("messages",messages);
		
		return "addBean";
		/*if(multipart.getSize() != 0)
		{
			String fileName = multipart.getOriginalFilename();
		
		 byte[] bytes = multipart.getBytes();
         Path path = Paths.get("//home//CVs//" + talent.getFullName() + ".docx");
         Files.write(path, bytes);

		talent.setFilePath(fileName);
		}*/
		

	 	}

	 @RequestMapping(value="/getTalentList", method=RequestMethod.GET)
	 public String getTalentList(Model model, HttpServletRequest request) {
		 String thisOperation = "List";
				model.addAttribute("role", CurrentUser.getUserRole());
				 model.addAttribute("userName", CurrentUser.getUserName());
			     model.addAttribute("role", CurrentUser.getUserRole());
		
		
			int pageNumber = 1;
			if (request.getParameter("currentPage") != null) {
				int currentPageNumber = Integer.parseInt(request.getParameter("currentPage"));
				if (request.getParameter("left") != null)
					pageNumber = currentPageNumber - 1;
				else if (request.getParameter("right") != null)
					pageNumber = currentPageNumber + 1;
			}
			pageNumber--;

			BasicBean profile = new Talent();
			profile.setFirstPage(pageNumber * 50);
			profile.setMaxResult(50);
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> createDateFilter = new HashMap<>();
			HashMap<String, Object> modifyDateFilter = new HashMap<>();
			ArrayList<String> searchFields = Startup.getSearchFields();

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
			HashMap<String, Object> searchCriteria = new HashMap<>();
			if(CurrentUser.getUserType()!=null && !CurrentUser.getUserRole().equals("PlatformAdmin"))
				searchCriteria.put("status", "active");
			profile.setSearchCriteria(searchCriteria);
			BasicBean profileList = service.listBeans(profile);
			List<BasicBean> resultList = profileList.getResults();
			
			try {
				//model = FormFields.fillModel( model,  "Talent",  "TalentController",  "Summary", "platform-body-view-get", profileList,  profileList ,"anonymousUser");
				model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, profileList, profileList);
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
			
			
			int totalCount = profile.getTotalResult();
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
 
	 
	 @RequestMapping(value = "/viewMyStartups", method = RequestMethod.GET)
	 public String viewMyStartups(Model model) throws ParserConfigurationException, SAXException, IOException {
		 logger.debug("Entering method TalentController.viewProfile");
		 
		 logger.debug("Inside method TalentController.viewProfile, before set talent bean : ");
		// String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		 String userId = null ;

				PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				 userId = currentUser.getUserKey();
				 model.addAttribute("userName", currentUser.getUserName());

		/*List<Field> f = getFromXML.getFormFields("Talent");
		Iterator<Field> itr = f.iterator();
		while(itr.hasNext())
		{Field field = itr.next();
		if(field.getfEnabledFor().contains("Evaluator")
				{}}
		*/ 
	    Talent search = new Talent();
	    search.setUserId(String.valueOf(userId));
	    HashMap<String, Object> searchCriteria = new HashMap<>();
		 searchCriteria.put("id", userId);
		 search.setSearchCriteria(searchCriteria);
		Talent result = talentService.getTalent(search);
		try {
			if(!result.getStatus().equals("active"))
		
				{
				model = FormFields.fillModel( model,  "Talent",  "TalentController",  "View", "platform-body-view-get", result,  result ,"profileOwner");
				ArrayList<String> messages = new ArrayList<String>();
				messages.add("Your profile is not active, contact technical support team!");
				model.addAttribute("messages",messages);
				}
				else
					model = FormFields.fillModel( model,  "Talent",  "TalentController",  "Update", "platform-body-add", result,  result ,"profileOwner");
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

		model.addAttribute("linkKeys",  Talent.getLinks().keySet());
		model.addAttribute("links",  Talent.getLinks());
	 	return "addBean";
	 }
	 @RequestMapping(value = "/viewTalentDetailsForm", method = RequestMethod.GET)
	 public String viewTalentApplication(Model model) throws ParserConfigurationException, SAXException, IOException {
		 logger.debug("Entering method TalentController.viewTalentDetailsForm");
		 Talent t = new Talent();
		 logger.debug("Inside method TalentController.viewTalentDetailsForm, before set talent bean : " + t);
	 
	 	model.addAttribute("emptyBean", new Talent());
	// 	model.addAttribute("xmlFields",  getFromXML.getFormFields(1));
	 	model.addAttribute("bean", t);
	 	model.addAttribute("searchBean", new Talent());
	 	model.addAttribute("beanName","Talent");
	   // model.addAttribute("addFields", Talent.getAddFields());
	    model.addAttribute("controllerName","TalentController");
	     logger.debug("Exiting method TalentController.viewTalentDetailsForm");
	 	return "talentDetailsPage";
	 }

	
	@InitBinder
	public void initBinder(final WebDataBinder binder){
	  final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
	  binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	 public BasicBean updateOldBean(String oldBeanString, BasicBean newBean)
	 {
		 ObjectMapper mapper = new ObjectMapper();
		 Talent beanBack = null;
		try {
			beanBack = mapper.readValue(oldBeanString, Talent.class);
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		 try {
			notNull.copyProperties(beanBack, newBean);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//talent.setId(Long.valueOf(id));
		//talent.setUserId("131072");
		service.updateBean(beanBack);
		return beanBack;
		}
	 
	 public class NullAwareBeanUtilsBean extends BeanUtilsBean{

		    @Override
		    public void copyProperty(Object dest, String name, Object value)
		            throws IllegalAccessException, InvocationTargetException {
		        if(value==null)
		        	return;
		        super.copyProperty(dest, name, value);
		    }
	 }
}
