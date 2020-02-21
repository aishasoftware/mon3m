package aisha.security.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtilsBean;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.BasicBean;
import aisha.bean.PlatformUser;
import aisha.bean.Startup;
import aisha.controller.StartupController.NullAwareBeanUtilsBean;
import aisha.security.beans.SystemUser;
import aisha.security.services.SystemUserService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;

@Controller
@RequestMapping(value="/PlatformUserController")
public class PlatformUserController {
	
	private static String thisBean = "PlatformUser";
	@Autowired
	private SystemUserService service; 
	
	protected SystemUser currentSystemUser;
	public void setCurrentBean(SystemUser user)
	{
		this.currentSystemUser = user;
		}
	
	public SystemUser getCurrentBean()
	{return this.currentSystemUser;}
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }
    
	 @RequestMapping(value = "/addPlatformUser", method = RequestMethod.GET)
	 public String addPlatformUser(Model model) {
		 String thisOperation = "Add";
		 model.addAttribute("userName", CurrentUser.getUserName());
	     model.addAttribute("role", CurrentUser.getUserRole());
	     model.addAttribute("title", "Create PlatformUser Profile");

	
	     try {
			//model = FormFields.fillModel( model,  "PlatformUser",  "PlatformUserController",  "Add", "platform-body-add", new PlatformUser(), new PlatformUser(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new PlatformUser(), new PlatformUser()); 
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
	 
		@RequestMapping(value= "/submitAddPlatformUser", method = RequestMethod.POST)
		 public String submitAddPlatformUser(HttpServletRequest request, @ModelAttribute("bean") PlatformUser bean, BindingResult result, Model model) throws IOException{
			 String thisOperation = "SubmitAdd";
			

			
			 PlatformUser sysUser = new PlatformUser();
			 ArrayList<String> messages = new ArrayList<String>();
			 Long beanId = 0L;
			 
			try {
				sysUser.setUserType(thisBean);
				sysUser.setUserRole("PlatformAdmin");
				  beanId = service.addSystemUser(bean);	  
				  try {
					model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
							if(beanId == null)
						    {
								messages.add("This user name is already used, please try another user name");
								model.addAttribute("messages", messages);
								try {
									model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, bean, bean);
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								return "addBean";
						    }
						
							
							messages.add("Your user created successfully");
							model.addAttribute("messages", messages);
							try {
								model = FormFields.fillModelGeneric( model, thisBean,   "View", bean, bean);
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							
							}
		
		 catch (SecurityException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
				
		   catch (IllegalAccessException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			} 
			
			catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 model.addAttribute("userName", CurrentUser.getUserName());
		     model.addAttribute("role", CurrentUser.getUserRole());
		     model.addAttribute("title", "Create PlatformUser Profile");
			return "addBean";
				}
			

	 
		@RequestMapping(value = "/getPlatformUserList", method = RequestMethod.GET)
		public String getPlatformUserList(Model model, HttpServletRequest request) {
		//	logger.debug("Entering method ApplicationController.getSubmittedApplications");
			//PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
			//ArrayList<String> searchFields = PlatformUser.getSearchFields();
			model.addAttribute("beanName", "PlatformUser");

/*			for (int i = 0; i < searchFields.size(); i++) {
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
			user.setSearchCriteria(criteria);
			
			PlatformUser usersList = service.listSystemUsers(user);
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

		 @RequestMapping(value="/getPlatformUser", method=RequestMethod.GET)
		 public String getPlatformUser(Model model,@ModelAttribute("id") Integer id) {
			 //logger.debug(getSystemUser"Entering method TalentController.getTalent");
		   model.addAttribute("beanName","PlatformUser");
		   model.addAttribute("controllerName","SystemUserController");
		   PlatformUser user = new PlatformUser();
		   
		   HashMap<String, Object> searchCriteria = new HashMap<>();
		   searchCriteria.put("id", id);
		   user.setSearchCriteria(searchCriteria);
		   //user.setId(id);
		   user = sysUserService.getSystemUser(user);
		   model.addAttribute("bean", user);
		   try {
			model.addAttribute("fields",  FormFields.getFormFields("PlatformUser","view"));
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
		   //logger.debug("Exiting method TalentController.getTalent");
		   return "viewBean";
		 }
	 
		 @RequestMapping(value= "/submitUpdatePlatformUser", method = RequestMethod.POST)
		 public String submitUpdatePlatformUser(HttpServletRequest request, @ModelAttribute("bean") PlatformUser user, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
			// PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// user.setId(Long.valueOf(id));
			sysUserService.updateSystemUser(user);
			ArrayList<String> messages = new ArrayList<String>();

			messages.add("Profile updated successfully");

					model.addAttribute("messages",  messages);

					  model.addAttribute("beanName","PlatformUser");
					   model.addAttribute("controllerName","SystemUserController");
				
					   
					   HashMap<String, Object> searchCriteria = new HashMap<>();
					   searchCriteria.put("id", id);
					   user.setSearchCriteria(searchCriteria);
					   //user.setId(id);
					   user = sysUserService.getSystemUser(user);
					   model.addAttribute("bean", user);
					   try {
						model.addAttribute("fields",  FormFields.getFormFields("PlatformUser","view"));
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
					   //logger.debug("Exiting method TalentController.getTalent");
					   
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
