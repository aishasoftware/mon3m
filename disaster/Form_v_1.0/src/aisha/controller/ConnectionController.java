package aisha.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aisha.bean.BasicBean;
import aisha.bean.Connection;
import aisha.bean.Investor;
import aisha.bean.PlatformUser;
import aisha.bean.Resource;
import aisha.bean.Startup;
import aisha.bean.Subscription;
import aisha.bean.Talent;
import aisha.controller.StartupController.NullAwareBeanUtilsBean;
import aisha.service.ConnectionService;
import aisha.service.InvestorService;
import aisha.service.StartupService;
import aisha.service.TalentService;
import aisha.util.CurrentUser;
import aisha.util.FormFields;
import aisha.util.bean.FieldAttributes;
@Controller
@RequestMapping(value = "/ConnectionController")
public class ConnectionController {
	protected static Logger logger = Logger.getLogger(ConnectionController.class);

	@Autowired
	private StartupService startupService;
	
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private ConnectionService connectionService;
	
	@Autowired
	private TalentService talentService;


	 @RequestMapping(value="/connect", method=RequestMethod.GET)
	 public String connect(Model model,@RequestParam("id") Integer id) {
	   Connection connect = new Connection();
	   connect.setInnerId(Integer.valueOf(CurrentUser.getEntityId()));
	   connect.setOuterId(id);
	   HashMap<String, Object> searchCriteria = new HashMap<>();
	   searchCriteria.put("id", CurrentUser.getEntityId());
	   String beanName = null;
	   if(CurrentUser.getUserType().equals("Investor"))
	   {
		   beanName = "Startup";
		   Investor investor = new Investor();
	   
	   investor.setSearchCriteria(searchCriteria);
	   investor = investorService.getInvestor(investor);
	   connect.setConnectionName(investor.getField1());
	   
	   try {
			model = FormFields.fillModel( model,  "Investor",  "InvestorController",  "Summary", "platform-body-view-get", investor,  investor ,CurrentUser.getUserRole());
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
	   }
	   
	   if(CurrentUser.getUserType().equals("Startup"))
	   {
		   beanName = "Talent";
		   Startup startup = new Startup();
	   
		   startup.setSearchCriteria(searchCriteria);
		   startup = startupService.getStartup(startup);
		   connect.setConnectionName(startup.getField1());
		   
		   try {
				model = FormFields.fillModel( model,  "Startup",  "StartupController",  "Summary", "platform-body-view-get", startup,  startup ,CurrentUser.getUserRole());
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
	   }
	   connect.setStatus("pending");
	   connect.setRelationType(CurrentUser.getUserType());
	   connectionService.addConnection(connect);
	   //return "getAllBeans";
	   return "redirect:"+"/"+beanName+"Controller/get"+beanName+"List";
	 }
	
	 @RequestMapping(value="/getConnections", method=RequestMethod.GET)
	 public String getConnections(Model model,@RequestParam("id") Integer id,HttpServletRequest request) {
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

			Startup profile = new Startup();
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
	   Connection startup = new Connection();
	   Connection connect = new Connection();
	   HashMap<String, Object> searchCriteria = new HashMap<>();
	   if(CurrentUser.getEntityId().equals(String.valueOf(id)))
	   {
		   connect.setId(id);
	   
	   searchCriteria.put("outerId", id);
	   startup.setSearchCriteria(searchCriteria);
	   startup = connectionService.listConnections(startup);	  
		List<BasicBean> resultList = startup.getResults();
		startup.setId(id);
		int totalCount = startup.getTotalResult();
       try {
		model = FormFields.fillModel( model,  "Connection",  "ConnectionController",  "Summary", "platform-body-view-get", connect,  startup ,CurrentUser.getUserRole());
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
		//model.addAttribute("beanList", resultList);
	   }
	   else
	   { 
		   ArrayList<String> messages = new ArrayList<String>();
           messages.add("Please login first");
           model.addAttribute("messages",  messages);
	   }
	   return "getAllBeans";
	 }
	 
	 @RequestMapping(value= "/submitUpdateConnection", method = RequestMethod.POST)
	 public String submitUpdateConnection(HttpServletRequest request,@RequestParam("oldBean") String oldBean, @ModelAttribute("newBean") Connection connect, @ModelAttribute("id") String id, BindingResult result, Model model) throws IOException{
	/*	 String myBean = request.getParameter("oldBean");
		 Startup newBean = new Startup();*/
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
			ObjectMapper mapper = new ObjectMapper();
		 Connection beanBack = mapper.readValue(oldBean, Connection.class);
		 BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		 try {
			notNull.copyProperties(beanBack, connect);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	 newBean.setId(Long.valueOf(id));
		 newBean.setField1("test dynamic update");*/
		 HashMap<String, Object> criteria1 = new HashMap<>();
		 criteria1.put("id", beanBack.getId());
		 beanBack.setSearchCriteria(criteria1);
		 beanBack = connectionService.getConnection(beanBack);
		 beanBack.setStatus("active");
		 connectionService.updateConnection(beanBack);
	
		Startup profile = new Startup();
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
   Connection startup = new Connection();
   HashMap<String, Object> searchCriteria = new HashMap<>();
   searchCriteria.put("outerId", id);
   startup.setSearchCriteria(searchCriteria);
   startup = connectionService.listConnections(startup);	  
	List<BasicBean> resultList = startup.getResults();
	int totalCount = startup.getTotalResult();
   try {
	model = FormFields.fillModel( model,  "Connection",  "ConnectionController",  "Summary", "platform-body-view-get", startup,  startup ,CurrentUser.getUserRole());
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
	model.addAttribute("beanList", resultList);
   return "getAllBeans";
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
