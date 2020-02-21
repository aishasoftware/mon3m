package aisha.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

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

import aisha.bean.BasicBean;
import aisha.bean.Investor;
import aisha.bean.Package;
import aisha.bean.PlatformUser;
import aisha.bean.Resource;
import aisha.bean.Subscription;
import aisha.security.services.SystemUserService;
import aisha.service.ApplicationService;
import aisha.service.ComplainService;
import aisha.service.PackageService;
import aisha.service.ResourceService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;
import aisha.util.bean.FieldAttributes;

@Controller
@RequestMapping(value = "/AssetController")
public class AssetController {

	@Autowired
	private SystemUserService sysUserService; 
	protected static Logger logger = Logger.getLogger(AssetController.class);


	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ApplicationService appService;
	

	@Autowired
	private ComplainService complainService;
	
	@Autowired
	private PackageService packageService;

	/*@RequestMapping(value = "/getAssetProfiles", method = RequestMethod.GET)
	public String getAssetProfiles(Model model, HttpServletRequest request) {
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

		AssetProfile profile = new AssetProfile();
		//profile.setId(1);
		profile.setFirstPage(pageNumber * 50);
		profile.setMaxResult(50);
		HashMap<String, Object> criteria = new HashMap<>();
		HashMap<String, Object> dateFilter = new HashMap<>();
		ArrayList<String> searchFields = ApplicationTemplate.getSearchFields();
		model.addAttribute("beanName", "AssetProfile");

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
		model.addAttribute("controllerName", "AssetProfileController");
		//profile.setSearchCriteria(criteria);
		logger.debug(
				"Inside method ApplicationController.getSubmittedApplications , before retrieving submitted applications from database");
		AssetProfile profileList = investorProfileService.listAssetProfiles(profile);
		List<BasicBean> resultList = profileList.getResults();
		int totalCount = profile.getTotalResult();
		logger.debug(
				"Inside method ApplicationController.getSubmittedApplications , after retrieving submitted applications from database, no of records : "
						+ totalCount);
		model.addAttribute("beanList", resultList);
		model.addAttribute("tableFields", AssetProfile.getTableFields());
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
		return "JobTemp_getAssets";

	}
*/

	@RequestMapping(value = "/addResource", method = RequestMethod.GET)
	 public String addResource(Model model) {
		 String thisOperation = "Add";
String thisBean = "Resource";
	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Resource",  "ResourceController",  "Add", "platform-body-add", new Resource(), new Resource(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Resource(), new Resource()); 
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
	 
	 @RequestMapping(value= "/submitAddResource", method = RequestMethod.POST)
	 public String submitAddResource(HttpServletRequest request, @ModelAttribute("bean") Resource resource, BindingResult result, Model model) throws IOException{


		Long resourceId = resourceService.addResource(resource);
		HashMap<String, Object> searchCriteria = new HashMap<>();
		searchCriteria.put("id", resourceId);
		resource.setSearchCriteria(searchCriteria);
		Resource newResource = resourceService.getResource(resource);
		logger.debug("Entering method TalentController.submitAddTalent");
	
		
		logger.debug("Inside method TalentController.submitAddTalent, before add bean : " + resourceId);
		
	//	model.addAttribute("message1", "Your profile has been created successfully");
		//model.addAttribute("message2","An email containing your crednetials is sent to you, check and login");
	logger.debug("Exiting method TalentController.submitAddTalent");
	//return "succCompleted";
	String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	if (x != "anonymousUser")
	{
		PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//currentUser.getuserkey;
		model.addAttribute("userType",  "platformuser");
	}
	else
	model.addAttribute("userType",  "guest");

 try {
	model = FormFields.fillModel( model,  "Resource",  "AssetController",  "add", "platform-body-add", null,  new Resource() ,"adminMode");
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


	

ArrayList<String> messages = new ArrayList<String>();
messages.add("Resource created successfully!");

		model.addAttribute("messages",  messages);

	return "addBean";
	 	}
	
	 
	 @RequestMapping(value = "/addPackage", method = RequestMethod.GET)
	 public String addPackage(Model model) {
		 String thisOperation = "Add";
String thisBean = "Package" ;
	     model.addAttribute("role", CurrentUser.getUserRole());
		
	     try {
			//model = FormFields.fillModel( model,  "Resource",  "ResourceController",  "Add", "platform-body-add", new Resource(), new Resource(),"Add");
			model = FormFields.fillModelGeneric( model, thisBean,   thisOperation, new Package(), new Package()); 
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
	 
	 
	 
	

	 @RequestMapping(value= "/submitAddPackage", method = RequestMethod.POST)
	 public String submitAddPackage(HttpServletRequest request, @ModelAttribute("bean") Package pack, BindingResult result, Model model) throws IOException{

			String resources[] =  request.getParameterValues("resources");
			//Set<String> resSet = new HashSet<>(Arrays.asList(resources));
			Set<Resource> beansSet = new HashSet<Resource>();
			for(int i=0;i<resources.length;i++)
				{
				Resource bean = new Resource();
				
			bean.setId(Long.valueOf(resources[i]));
				beansSet.add(bean);
				}
			pack.setResources(beansSet);
		Long packId = packageService.addPackage(pack);

		logger.debug("Entering method TalentController.submitAddTalent");
	
		
		logger.debug("Inside method TalentController.submitAddTalent, before add bean : " + packId);
		
	//	model.addAttribute("message1", "Your profile has been created successfully");
		//model.addAttribute("message2","An email containing your crednetials is sent to you, check and login");
	logger.debug("Exiting method TalentController.submitAddTalent");
	//return "succCompleted";
	 try {
		model = FormFields.fillModel( model,  "Package",  "AssetController",  "add", "platform-body-add", null,  new Package() ,"adminMode");
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
ArrayList<String> messages = new ArrayList<String>();
messages.add("Package created successfully!");

		model.addAttribute("messages",  messages);

	return "addBean";
	 	}
	 
	 
	 @RequestMapping(value="/getPackage", method=RequestMethod.GET)
	 public String getPackage(Model model,@RequestParam("id") Integer id) {
		 logger.debug("Entering method TalentController.getTalent");
	   model.addAttribute("beanName","Package");
	   
	   model.addAttribute("innerBeanController","TalentController");
	   model.addAttribute("innerBeanName","Talent");
	   model.addAttribute("controllerName","AssetController");
	   Package pack = new Package();
	   pack.setId(id);
		HashMap<String, Object> searchCriteria = new HashMap<>();
	   searchCriteria.put("id", id);
	   pack.setSearchCriteria(searchCriteria);
	   pack = packageService.getPackage(pack);
	   model.addAttribute("bean", pack);
	   //model.addAttribute("innerBeans", pack.getResources());
	   try {
		   List<FieldAttributes> fields = FormFields.getFormFields("Package","view");
		   for(int i = 0 ; i < fields.size(); i ++)
			  if(fields.get(i).getOptions()!=null)
			  {
				  Set<Resource> basics = new HashSet<Resource>();
				  basics = pack.getResources();
			  
		   Set<BasicBean> newSet = new HashSet<BasicBean>();
		   Iterator<Resource> resItr = basics.iterator();
		   while(resItr.hasNext())
			   newSet.add(resItr.next());

				  fields.get(i).setOptions(newSet);
			  }
		model.addAttribute("fields",   fields);
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
	   logger.debug("Exiting method TalentController.getTalent");
	   return "viewBean";
	 }

	 @RequestMapping(value= "/submitUpdatePackage", method = RequestMethod.POST)
	 public String submitUpdatePackage(HttpServletRequest request, @ModelAttribute("bean") Package pack,  BindingResult result, Model model) throws IOException{
		//@ModelAttribute("id") String id, PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String resources[] =  request.getParameterValues("resources");
			String id =  request.getParameter("id");
			//Set<String> resSet = new HashSet<>(Arrays.asList(resources));
			Set<Resource> beansSet = new HashSet<Resource>();
			if(resources != null)
			for(int i=0;i<resources.length;i++)
				{
				Resource bean = new Resource();
				
			bean.setId(Long.valueOf(resources[i]));
				beansSet.add(bean);
				}
			pack.setResources(beansSet);
			pack.setId(Long.valueOf(id));
		packageService.updatePackage(pack);
		logger.debug("Entering method TalentController.submitAddTalent");
		
		
		logger.debug("Inside method TalentController.submitAddTalent, before add bean : " + pack);
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("Profile updated successfully!");

		model.addAttribute("messages",messages);
		
		return "viewBean";

	

	 	}
	 
	 
	 @RequestMapping(value="/getResourceList", method=RequestMethod.GET)
	 public String getResourceList(Model model, HttpServletRequest request) {
		 String thisOperation = "List";
		 String thisBean = "Resource";
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

			Resource profile = new Resource();
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
			BasicBean profileList = resourceService.listResources(profile);
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
	 }
