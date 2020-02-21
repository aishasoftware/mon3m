package aisha.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.Application;
import aisha.bean.Complain;
import aisha.bean.PlatformUser;
import aisha.bean.Program;
import aisha.bean.ProgramOld;
import aisha.bean.BasicBean;
import aisha.bean.Investor;
import aisha.bean.Startup;
import aisha.bean.Talent;
import aisha.controller.StartupController.NullAwareBeanUtilsBean;
import aisha.security.beans.SystemUser;
import aisha.security.services.SystemUserService;
import aisha.service.ApplicationService;
import aisha.service.ApplicationTemplateService;
import aisha.service.ComplainService;
import aisha.service.InvestorService;
import aisha.test.getFromXML;
import aisha.util.CurrentUser;
import aisha.util.FormFields;

@Controller
@RequestMapping(value = "/InvestorController")
public class InvestorController {
	private static String thisBean = "Investor" ;
	protected Investor currentProfile = new Investor();
	@Autowired
	private ApplicationService programService;
	@Autowired
	private SystemUserService userService; 
	@Autowired
	private ApplicationTemplateService appService;
	@Autowired
	private InvestorService service;

	

	@Autowired
	private ComplainService complainService;
	protected static Logger logger = Logger.getLogger(InvestorController.class);

	public void setCurrentInvestor(Investor currentProfile) {
		this.currentProfile = currentProfile;
	}

	public Investor getCurrentInvestor() {
		return this.currentProfile;
	}



	/*@RequestMapping(value = "/getInvestorProfiles", method = RequestMethod.GET)
	public String getInvestorProfiles(Model model, HttpServletRequest request) {
		logger.debug("Entering method ApplicationController.getSubmittedApplications");
		String currentUser =  SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
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

		InvestorProfile profile = new InvestorProfile();
		//profile.setId(1);
		profile.setFirstPage(pageNumber * 50);
		profile.setMaxResult(50);
		HashMap<String, Object> criteria = new HashMap<>();
		HashMap<String, Object> dateFilter = new HashMap<>();
		ArrayList<String> searchFields = ApplicationTemplate.getSearchFields();
		model.addAttribute("beanName", "InvestorProfile");

		for (int i = 0; i < searchFields.size(); i++) {
			if (searchFields.get(i).equals("fromDate") || searchFields.get(i).equals("toDate")) {
				if (request.getParameter(searchFields.get(i)) != null
						&& !request.getParameter(searchFields.get(i)).isEmpty())

				{
					dateFilter.put(searchFields.get(i), request.getParameter(searchFields.get(i)));

				}
			}
			if (!searchFields.get(i).equals("fromDate") && !searchFields.get(i).equals("toDate")
					&& request.getParameter(searchFields.get(i)) != null)
				criteria.put(searchFields.get(i), request.getParameter(searchFields.get(i)));
			if (!dateFilter.isEmpty())
				criteria.put("submTime", dateFilter);
		}
		model.addAttribute("controllerName", "InvestorProfileController");
		//profile.setSearchCriteria(criteria);
		logger.debug(
				"Inside method ApplicationController.getSubmittedApplications , before retrieving submitted applications from database");
		InvestorProfile profileList = investorProfileService.listInvestorProfiles(profile);
		List<BasicBean> resultList = profileList.getResults();
		int totalCount = profile.getTotalResult();
		logger.debug(
				"Inside method ApplicationController.getSubmittedApplications , after retrieving submitted applications from database, no of records : "
						+ totalCount);
		model.addAttribute("beanList", resultList);
		model.addAttribute("tableFields", InvestorProfile.getTableFields());
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
		logger.debug("Exiting method ApplicationController.getSubmittedApplications");
		return "JobTemp_getInvestors";

	}
*/

	@RequestMapping(value = "/addInvestor", method = RequestMethod.GET)
	 public String addInvestor(Model model) {
		 String thisOperation = "Add";

	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Investor",  "InvestorController",  "Add", "platform-body-add", new Investor(), new Investor(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Investor(), new Investor()); 
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
	 
		@RequestMapping(value= "/submitAddInvestor", method = RequestMethod.POST)
		 public String submitAddInvestor(HttpServletRequest request, @ModelAttribute("bean") Investor bean, BindingResult result, Model model) throws IOException{
			 String thisOperation = "SubmitAdd";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Class cls = null;
			 Investor childBean = (Investor) bean;
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
			


		 @RequestMapping(value="/getInvestor", method=RequestMethod.GET)
		 public String getInvestor(Model model, @RequestParam("id") Integer id) {
			 String thisOperation = "Get";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Investor investor = new Investor();
			 BasicBean result = new Investor();
			 List<BasicBean> beanList = new ArrayList<BasicBean>();
			 //investor.setFirstResults(10);
			// investor.setMaxResults(10);
		
			 HashMap<String, Object> searchCriteria = new HashMap<>();
			   searchCriteria.put("id", id);
			   investor.setSearchCriteria(searchCriteria);
			 result = service.getBean(investor);
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
			// logger.debug("Inside method InvestorController.getInvestorList, before list investors with criteria : " + investor.getFilter());

			 logger.debug("Exiting method InvestorController.getInvestorList");
			 return "addBean";
		
		 }
		 
		 @RequestMapping(value= "/submitUpdateInvestor", method = RequestMethod.POST)
		 public String submitUpdateInvestor(HttpServletRequest request, @RequestParam("oldBean") String oldBean,@ModelAttribute("bean") Investor bean, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
			 String thisOperation = "View";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 Class cls = null;
			 Investor childBean = (Investor) bean;
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
	         Path path = Paths.get("//home//CVs//" + investor.getFullName() + ".docx");
	         Files.write(path, bytes);

			investor.setFilePath(fileName);
			}*/
			

		 	}

		 
		@RequestMapping(value= "/viewMyProfile", method = RequestMethod.GET)
		 public String viewMyProfile(Model model) throws IOException{
			 String thisOperation = "ViewProfile";
			 model.addAttribute("role", CurrentUser.getUserRole());
			 model.addAttribute("userName", CurrentUser.getUserName());
			 Class cls = null;
			 Investor searchBean = new Investor();
			 BasicBean resultBean = new Investor();
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
			model.addAttribute("linkKeys",  Investor.getLinks().keySet());
			model.addAttribute("links",  Investor.getLinks());
			
			return "addBean";
				}
		 @RequestMapping(value="/getInvestorList", method=RequestMethod.GET)
		 public String getInvestorList(Model model, HttpServletRequest request) {
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

				BasicBean profile = new Investor();
				profile.setFirstPage(pageNumber * 2);
				profile.setMaxResult(2);
				HashMap<String, Object> criteria = new HashMap<>();
				HashMap<String, Object> createDateFilter = new HashMap<>();
				HashMap<String, Object> modifyDateFilter = new HashMap<>();
				ArrayList<String> searchFields = Investor.getSearchFields();

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
					//model = FormFields.fillModel( model,  "Investor",  "InvestorController",  "Summary", "platform-body-view-get", profileList,  profileList ,"anonymousUser");
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
				Integer nOfPages = (totalCount + 1) / 2;
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
	 
	
		 @RequestMapping(value="/getProgramList", method=RequestMethod.GET)
		 public String getProgramList(Model model, HttpServletRequest request) {
			 String thisOperation = "List";
			 String  thisBean = "Program";
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

				BasicBean profile = new Program();
				profile.setFirstPage(pageNumber * 50);
				profile.setMaxResult(50);
				HashMap<String, Object> criteria = new HashMap<>();
				HashMap<String, Object> createDateFilter = new HashMap<>();
				HashMap<String, Object> modifyDateFilter = new HashMap<>();
				ArrayList<String> searchFields = Program.getSearchFields();

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
					//model = FormFields.fillModel( model,  "Program",  "ProgramController",  "Summary", "platform-body-view-get", profileList,  profileList ,"anonymousUser");
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
	 
	 @RequestMapping(value = "/listApplications", method = RequestMethod.GET)
		public String listApplications(Model model, HttpServletRequest request) {
		//	logger.debug("Entering method ApplicationController.getSubmittedApplications");
			PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//model.addAttribute("role", currentUser.getUserRole());
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

			Program app = new Program();
			
			app.setFirstPage(pageNumber * 50);
			app.setMaxResult(50);
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> dateFilter = new HashMap<>();
			ArrayList<String> searchFields;
			//= Talent.getSearchFields();
			model.addAttribute("controllerName", "TalentController");
			model.addAttribute("beanName", "Talent");
			
			Program usersList = appService.listApplications(app);
			List<BasicBean> resultList = new ArrayList<BasicBean>();
		    resultList = usersList.getResults();
			int totalCount = app.getTotalResult();
	
			model.addAttribute("beanList", resultList);
			model.addAttribute("tableFields", Talent.getTableFields());
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
			model.addAttribute("role","Admin");
			
			//logger.debug("Exiting method ApplicationController.getSubmittedApplications");
			return "getAllBeans";

		}
	 
	
	 @RequestMapping(value = "/getInvestorUsers", method = RequestMethod.GET)
		public String getInvestorUsers(Model model, HttpServletRequest request) {
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

			PlatformUser user = new PlatformUser();
			
			user.setFirstPage(pageNumber * 50);
			user.setMaxResult(50);
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> dateFilter = new HashMap<>();
/*			ArrayList<String> searchFields = PlatformUser.getSearchFields();
			

			for (int i = 0; i < searchFields.size(); i++) {
				if (searchFields.get(i).equals("fromDate") || searchFields.get(i).equals("toDate")) {
					if (request.getParameter(searchFields.get(i)) != null
							&& !request.getParameter(searchFields.get(i)).isEmpty())

					{
						dateFilter.put(searchFields.get(i), request.getParameter(searchFields.get(i)));

					}
				}
				if (!searchFields.get(i).equals("fromDate") && !searchFields.get(i).equals("toDate")
						&& request.getParameter(searchFields.get(i)) != null)
					criteria.put(searchFields.get(i), request.getParameter(searchFields.get(i)));
				if (!dateFilter.isEmpty())
					criteria.put("submTime", dateFilter);
			}*/
			model.addAttribute("beanName", "PlatformUser");
			criteria.put("userId", "1");
			user.setSearchCriteria(criteria);
			PlatformUser usersList = sysUserService.listSystemUsers(user);
			List<BasicBean> resultList = new ArrayList<BasicBean>();
		    resultList = usersList.getResults();
			int totalCount = user.getTotalResult();
	
			model.addAttribute("beanList", resultList);
			model.addAttribute("controllerName", "SystemUserController");
			model.addAttribute("tableFields", PlatformUser.getTableFields());
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
			model.addAttribute("role","Admin");
			
			//logger.debug("Exiting method ApplicationController.getSubmittedApplications");
			return "getAllBeans";

		}
	 
	 
	 @RequestMapping(value= "/submitAddComplain", method = RequestMethod.POST)
	 public String submitAddComplain(HttpServletRequest request, @ModelAttribute("bean") Complain complain, BindingResult result, Model model) throws IOException{


		Long complainId = complainService.addComplain(complain);
		//PlatformUser sysUser = new PlatformUser();
		//sysUser.setUserName(investor.getField1());
		//sysUser.setUserRole("Investor");
		//sysUser.setUserKey(talentId.toString());

		logger.debug("Entering method TalentController.submitAddTalent");
	
		
		logger.debug("Inside method TalentController.submitAddTalent, before add bean : " + complainId);
		
	//	model.addAttribute("message1", "Your profile has been created successfully");
		//model.addAttribute("message2","An email containing your crednetials is sent to you, check and login");
	logger.debug("Exiting method TalentController.submitAddTalent");
	//return "succCompleted";
	
	model.addAttribute("beanName",  "InvestorUser");
	model.addAttribute("controllerName",  "InvestorController");
ArrayList<String> messages = new ArrayList<String>();
messages.add("Your message recieved successfully!");


		model.addAttribute("messages",  messages);

	return "addProfile";
	 	}
	 
	 
	 
	 @RequestMapping(value = "/addProgram", method = RequestMethod.GET)
	 public String addProgram(Model model) {
		 String thisOperation = "Add";
String thisBean = "Program" ;
	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Program",  "ProgramController",  "Add", "platform-body-add", new Program(), new Program(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Program(), new Program()); 
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
	 

	 
	 @RequestMapping(value= "/submitAddProgram", method = RequestMethod.POST)
	 public String submitAddProgram(HttpServletRequest request, @ModelAttribute("bean") Program bean, BindingResult result, Model model) throws IOException{
		 String thisOperation = "SubmitAdd";
		 String thisBean = "Program";
		 model.addAttribute("role", CurrentUser.getUserRole());
		 ArrayList<String> messages = new ArrayList<String>();
		 Long beanId = 0L;
		 
		try {
			  
			beanId = programService.addApplication(bean);

			  model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);

						messages.add("Program created successfully");
						
						model.addAttribute("messages", messages);
						model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
					
						
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
		
	 @RequestMapping(value="/getProgram", method=RequestMethod.GET)
	 public String getProgram(Model model, @RequestParam("id") Integer id) {
		 String thisOperation = "Get";
		 String  thisBean = "Program";
		 model.addAttribute("role", CurrentUser.getUserRole());
		 Program program = new Program();
		 BasicBean result = new Program();
		 List<BasicBean> beanList = new ArrayList<BasicBean>();
		 //program.setFirstResults(10);
		// program.setMaxResults(10);
	
		 HashMap<String, Object> searchCriteria = new HashMap<>();
		   searchCriteria.put("id", id);
		   program.setSearchCriteria(searchCriteria);
		 result = service.getBean(program);
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
		// logger.debug("Inside method ProgramController.getProgramList, before list programs with criteria : " + program.getFilter());

		 logger.debug("Exiting method ProgramController.getProgramList");
		 return "addBean";
	
	 }
	 
	 @RequestMapping(value="/getApplicationForm", method=RequestMethod.GET)
	 public String getApplicationForm(Model model, @RequestParam("id") String app_Id) {
		// String[] ids = idsArrayy.split(" ");
		// String id = ids[0];
		// Integer app_Id = Integer.valueOf(ids[1]);
		 String thisOperation = "Get";
		 String  thisBean = "Application";
		 model.addAttribute("role", CurrentUser.getUserRole());
		 Application application = new Application();
		 BasicBean result = new Application();
		 List<BasicBean> beanList = new ArrayList<BasicBean>();
		 //application.setFirstResults(10);
		// application.setMaxResults(10);
	
	try {
	model.addAttribute("xmlFields", getFromXML.getFormFields(Integer.valueOf(app_Id)));
	model.addAttribute("bean", result);
	model.addAttribute("appID", app_Id);
	model.addAttribute("searchBean", new Talent());
	model.addAttribute("beanName", "Application");
	model.addAttribute("addFields", Application.getAddFields());
	model.addAttribute("controllerName", "InvestorController");
} catch (ParserConfigurationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (SAXException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

logger.debug("Exiting method ApplicationController.viewOppApplicationForm");
return "JobTemp_getOpportunityForm_v_1.1";

	
	 }
	 
	 
	 @RequestMapping(value="/getApplication", method=RequestMethod.GET)
	 public String getApplication(Model model, @RequestParam("id") String idsArrayy) {
		 String[] ids = idsArrayy.split(" ");
		 String id = ids[0];
		 Integer app_Id = Integer.valueOf(ids[1]);
		 String thisOperation = "Get";
		 String  thisBean = "Application";
		 model.addAttribute("role", CurrentUser.getUserRole());
		 Application application = new Application();
		 BasicBean result = new Application();
		 List<BasicBean> beanList = new ArrayList<BasicBean>();
		 //application.setFirstResults(10);
		// application.setMaxResults(10);
	
		 HashMap<String, Object> searchCriteria = new HashMap<>();
		   searchCriteria.put("id", id);
		   //searchCriteria.put("app_Id", app_Id);
		   application.setSearchCriteria(searchCriteria);
		 result = appService.getApplicationTemplate(application);
/*try
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
				}*/
		// logger.debug("Inside method ApplicationController.getApplicationList, before list applications with criteria : " + application.getFilter());
try {
	model.addAttribute("xmlFields", getFromXML.getFormFields(app_Id));
	model.addAttribute("bean", result);
	model.addAttribute("operation", "View");
	model.addAttribute("searchBean", new Talent());
	model.addAttribute("beanName", "Application");
	model.addAttribute("addFields", Application.getAddFields());
	model.addAttribute("controllerName", "InvestorController");
} catch (ParserConfigurationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (SAXException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

logger.debug("Exiting method ApplicationController.viewOppApplicationForm");
return "JobTemp_getOpportunityForm_v_1.1";

	
	 }
	 
	 @RequestMapping(value = "/getApplicationList", method = RequestMethod.GET)
		public String getApplicationList(Model model, HttpServletRequest request, @RequestParam("id") Integer appId) {
			logger.debug("Entering method ApplicationController.getSubmittedApplications");
			String thisOperation = "ListApps";
			//String thisBean = "Application";
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

			Application app = new Application();
			app.setId(1);
			app.setFirstPage(pageNumber * 2);
			app.setMaxResult(2);
			HashMap<String, Object> criteria = new HashMap<>();
			HashMap<String, Object> dateFilter = new HashMap<>();
			ArrayList<String> searchFields = Application.getSearchFields();
			model.addAttribute("beanName", "ApplicationController");

			for (int i = 0; i < searchFields.size(); i++) {
				if (searchFields.get(i).equals("fromDate") || searchFields.get(i).equals("toDate")) {
					if (request.getParameter(searchFields.get(i)) != null
							&& !request.getParameter(searchFields.get(i)).isEmpty())

					{
						dateFilter.put(searchFields.get(i), request.getParameter(searchFields.get(i)));

					}
				}
				if (!searchFields.get(i).equals("fromDate") && !searchFields.get(i).equals("toDate")
						&& request.getParameter(searchFields.get(i)) != null)
					criteria.put(searchFields.get(i), request.getParameter(searchFields.get(i)));
				if (!dateFilter.isEmpty())
					criteria.put("submTime", dateFilter);
			}
			model.addAttribute("controllerName", "ApplicationController");
			criteria.put("app_Id", appId);
			app.setCriteria(criteria);
			logger.debug(
					"Inside method ApplicationController.getSubmittedApplications , before retrieving submitted applications from database");
			Application appList = appService.listApplicationTemplate(app);
			List<BasicBean> resultList = appList.getResults();
			try {
				//model = FormFields.fillModel( model,  "Investor",  "InvestorController",  "Summary", "platform-body-view-get", profileList,  profileList ,"anonymousUser");
				model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, appList, appList);
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
			
			int totalCount = app.getTotalResult();
			logger.debug(
					"Inside method ApplicationController.getSubmittedApplications , after retrieving submitted applications from database, no of records : "
							+ totalCount);
			
			Integer nOfRecords = totalCount;
			Integer nOfPages = (totalCount + 1) / 2;
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
			logger.debug("Exiting method ApplicationController.getSubmittedApplications");
			return "getAllBeans";

		}

	 @RequestMapping(value= "/submitAddProgram2", method = RequestMethod.POST , consumes = {"multipart/form-data"})
	 public String submitAddProgram2(HttpServletResponse response, HttpServletRequest request, @ModelAttribute("bean") Program program, BindingResult result, Model model) throws IOException{
		 Method method = null;
		 //@RequestParam("files") MultipartFile[] multipart,
/*			 for(int i = 0; i < multipart.length ; i++)
			 {
				 try {
					 int j = i+1;
				 method	= program.getClass().getMethod("setAppFile" + j , String.class);
			 
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
			try {
				method.invoke(program,multipart[i].getOriginalFilename().toString());
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
			if(multipart[i].getSize() != 0)
			{
				String fileName = multipart[i].getOriginalFilename();
				// String ext = multipart[i].getOriginalFilename(); ext
			
			 byte[] bytes = multipart[i].getBytes();
	         Path path = Paths.get("C://Users//hp//Desktop//husam//"+fileName + "pdf");
	         Files.write(path, bytes);
			}
			}*/

		programService.addApplication(program);
		

ArrayList<String> messages = new ArrayList<String>();
messages.add("Your program created successfully!");
messages.add("You need admin approval to publish it");


		model.addAttribute("messages",  messages);
		
	       
		//Long complainId = ;
//program.setId(4194304);
		 //program.setField21(field21);

		 	return "addBean";
	 	}
	 

	
	 
	 @RequestMapping(value="/downloadFile", method=RequestMethod.GET)
	 public void downloadFile(HttpServletResponse response,Model model, @RequestParam("fileName") String fileName) {
		  File file = new File("C://Users//hp//Desktop//husam//" + fileName);
		   
			try {
				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
	

      response.setContentType("application/pdf");
      response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
      BufferedInputStream inStrem = null;

			inStrem = new BufferedInputStream(new FileInputStream(file));
			
			BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
	        
	        byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = inStrem.read(buffer)) != -1) {
	          outStream.write(buffer, 0, bytesRead);
	        
	        outStream.flush();
	        inStrem.close();
	    	}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

	 }
	 
	 public BasicBean updateOldBean(String oldBeanString, BasicBean newBean)
	 {
		 ObjectMapper mapper = new ObjectMapper();
		 Investor beanBack = null;
		try {
			beanBack = mapper.readValue(oldBeanString, Investor.class);
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
		        if(value==null)return;
		        super.copyProperty(dest, name, value);
		    }

		}
}
