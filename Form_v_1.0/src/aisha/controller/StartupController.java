package aisha.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.BasicBean;
import aisha.bean.Investor;
import aisha.bean.PlatformUser;
import aisha.bean.Resource;
import aisha.bean.Startup;
import aisha.bean.Subscription;
import aisha.bean.Talent;
import aisha.controller.TalentController.NullAwareBeanUtilsBean;
import aisha.security.services.SystemUserService;
import aisha.service.StartupService;
import aisha.service.TalentService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;
import aisha.util.bean.FieldAttributes;
@Controller
@RequestMapping(value = "/StartupController")
public class StartupController {
	protected static Logger logger = Logger.getLogger(StartupController.class);
	private static String thisBean = "Startup";
	@Autowired
	private StartupService service;
	
	@Autowired
	private TalentService talentService;

	@Autowired
	private SystemUserService userService;
	
	 @RequestMapping(value="/getStartupList", method=RequestMethod.GET)
	 public String getStartupList(Model model, HttpServletRequest request) {
		 String thisOperation = "List";
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

			BasicBean profile = new Startup();
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
				//model = FormFields.fillModel( model,  "Startup",  "StartupController",  "Summary", "platform-body-view-get", profileList,  profileList ,"anonymousUser");
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
 


	 
	@RequestMapping(value = "/addStartup", method = RequestMethod.GET)
	 public String addStartup(Model model) {
		 String thisOperation = "Add";

	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Startup",  "StartupController",  "Add", "platform-body-add", new Startup(), new Startup(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Startup(), new Startup()); 
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
	 
		@RequestMapping(value= "/submitAddStartup", method = RequestMethod.POST)
		 public String submitAddStartup(HttpServletRequest request, @ModelAttribute("bean") Startup bean, BindingResult result, Model model) throws IOException{
			 String thisOperation = "SubmitAdd";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Class cls = null;
			 Startup childBean = (Startup) bean;
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
							model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
						
							
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
			

		

		
		 @RequestMapping(value="/getStartup", method=RequestMethod.GET)
		 public String getStartup(Model model, @RequestParam("id") Integer id) {
			 String thisOperation = "Get";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Startup startup = new Startup();
			 BasicBean result = new Startup();
			 List<BasicBean> beanList = new ArrayList<BasicBean>();
			 //startup.setFirstResults(10);
			// startup.setMaxResults(10);
		
			 HashMap<String, Object> searchCriteria = new HashMap<>();
			   searchCriteria.put("id", id);
			   startup.setSearchCriteria(searchCriteria);
			 result = service.getBean(startup);
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
			// logger.debug("Inside method StartupController.getStartupList, before list startups with criteria : " + startup.getFilter());

			 logger.debug("Exiting method StartupController.getStartupList");
			 return "addBean";
		
		 }
		 
		 @RequestMapping(value= "/submitUpdateStartup", method = RequestMethod.POST)
		 public String submitUpdateStartup(HttpServletRequest request, @RequestParam("oldBean") String oldBean,@ModelAttribute("bean") Startup bean, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
			 String thisOperation = "View";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Class cls = null;
			 Startup childBean = (Startup) bean;
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
	         Path path = Paths.get("//home//CVs//" + startup.getFullName() + ".docx");
	         Files.write(path, bytes);

			startup.setFilePath(fileName);
			}*/
			

		 	}

		 
		 @RequestMapping(value= "/viewMyProfile", method = RequestMethod.GET)
		 public String viewMyProfile(Model model) throws IOException{
			 String thisOperation = "ViewProfile";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 model.addAttribute("userName", CurrentUser.getUserName());
			 Class cls = null;
			 Startup searchBean = new Startup();
			 BasicBean resultBean = new Startup();
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
			model.addAttribute("linkKeys",  Startup.getLinks().keySet());
			model.addAttribute("links",  Startup.getLinks());
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
			Startup startup = new Startup();
			startup.setId(Long.valueOf(currentUser.getUserKey()));
			startup = startupService.getStartup(startup);
			//Talent usersList = talentService.listStartupTalents(talent);
			//List<BasicBean> resultList = new ArrayList<BasicBean>();
		   // resultList = usersList.getResults();
			int totalCount = startup.getTalents().size();
	
			model.addAttribute("beanList", startup.getTalents());
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
	 
	 
	
	 
	 @RequestMapping(value="/getStartupAdminView", method=RequestMethod.GET)
	 public String getStartupAdminView(Model model,@RequestParam("id") Integer id) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	   Startup startup = new Startup();
	   HashMap<String, Object> searchCriteria = new HashMap<>();
	   searchCriteria.put("id", id);
	   startup.setSearchCriteria(searchCriteria);
	   startup = startupService.getStartup(startup);
	  

    	   model = FormFields.fillModel( model,  "Startup",  "StartupController",  "view", "platform-body-view-get", null,  startup ,"Admin");
	

	   return "addBean";
	 }
	
	 public BasicBean updateOldBean(String oldBeanString, BasicBean newBean)
	 {
		 ObjectMapper mapper = new ObjectMapper();
		 Startup beanBack = null;
		try {
			beanBack = mapper.readValue(oldBeanString, Startup.class);
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
	 
	/* @RequestMapping(value="/connectStartup", method=RequestMethod.GET)
	 public String connectStartup(Model model,@RequestParam("id") Integer id) {
		 logger.debug("Entering method TalentController.getTalent");
		 String startupID = null ;
				String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
				if (x != "anonymousUser")
				{
					PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					//currentUser.getuserkey;
					startupID = currentUser.getUserId();
				}
				
	Investor investor = new Investor();
	investor.setId(Long.valueOf("3637248"));
				Startup startup = new Startup();
				startup.getInvestors().add(investor);
				startup.setId(Long.valueOf(id));
				
				startupService.updateStartup(startup);
	   return "viewBean";
	 }*/
	 public class NullAwareBeanUtilsBean extends BeanUtilsBean{

		    @Override
		    public void copyProperty(Object dest, String name, Object value)
		            throws IllegalAccessException, InvocationTargetException {
		        if(value==null)return;
		        super.copyProperty(dest, name, value);
		    }

		}
}
