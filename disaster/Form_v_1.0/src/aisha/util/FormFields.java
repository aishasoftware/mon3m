package aisha.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import aisha.bean.BasicBean;
import aisha.bean.Connection;
import aisha.bean.Startup;
import aisha.bean.Talent;
import aisha.bean.Investor;
import aisha.bean.Connection;
import aisha.bean.PlatformUser;
import aisha.dao.BasicDAO;
import aisha.service.ConnectionService;
import aisha.util.bean.FieldAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class FormFields {
private static BasicDAO basicDAO;
@Autowired
private  BasicDAO mybasicDAO;


private static ConnectionService connectionService;
@Autowired
private  ConnectionService myConnectionService;

@PostConstruct
private void init()
{
	basicDAO = this.mybasicDAO;
	connectionService = this.myConnectionService;
}
	
	
	public static List<BasicBean> getOptionsFromBean(BasicBean basic, BasicBean bean)
	{  
		List<BasicBean> beanList = new ArrayList<BasicBean>();
		Iterator itr;
        if(basic.getClass().getSimpleName().equals("Startup") && bean.getClass().getSimpleName().equals("Talent"))
        	//&& CurrentUser.getUserType().equals("Talent"))
        {
        	
		Set<Startup> beanSet = ((Talent) bean).getStartups();
		Startup current = new Startup();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Startup) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }
        
        if(basic.getClass().getSimpleName().equals("Startup") && bean.getClass().getSimpleName().equals("Investor"))
        	//&& CurrentUser.getUserType().equals("Investor"))
        {
		Set<Startup> beanSet = ((Investor) bean).getStartups();
		Startup current = new Startup();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Startup) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }
        
        if(basic.getClass().getSimpleName().equals("Investor") && bean.getClass().getSimpleName().equals("Startup"))
        	//&& CurrentUser.getUserType().equals("Investor"))
        {
		Set<Investor> beanSet = ((Startup) bean).getInvestors();
		Investor current = new Investor();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Investor) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }
        
        if(basic.getClass().getSimpleName().equals("Talent") && bean.getClass().getSimpleName().equals("Startup"))
        	//&& CurrentUser.getUserType().equals("Investor"))
        {
		Set<Talent> beanSet = ((Startup) bean).getTalents();
		Talent current = new Talent();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Talent) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }
        
      /*  if(basic.getClass().getSimpleName().equals("Talent"))
        {
		Set<Talent> beanSet = ((Startup) bean).getTalents();
		Talent current = new Talent();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Talent) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }*/
        
      /*  if(basic.getClass().getSimpleName().equals("Investor"))
        {
		Set<Investor> beanSet = ((Startup) basic).getInvestors();
		Investor current = new Investor();
		itr = beanSet.iterator();
		while(itr.hasNext())
		{
			current = (Investor) itr.next();
			//startupList.add((BasicBean)current);
			beanList.add(current);
		}
       		
        }*/
		
		return beanList;
	}
		public static List<BasicBean> getOptionsFromDB(BasicBean basic)
		{  
	
	BasicBean result = basicDAO.listBeans(basic);
	List<BasicBean> results = result.getResults();
	return results;
	
	}
	public static List<FieldAttributes> getFormFields(String beanName,String type, BasicBean bean , String accessMode) throws ParserConfigurationException, SAXException, IOException {
		
		ArrayList<FieldAttributes> result = new ArrayList<>();

		/*File fXmlFile = new File(
				Thread.currentThread().getContextClassLoader().getResource(beanName + ".xml").getFile());
		*/
		final File fXmlFile = new File(Thread.currentThread().getContextClassLoader().getResource("resources/" + beanName + ".xml").getFile());
		String userRole;
		String x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (x != "anonymousUser")
		{
			PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userRole = currentUser.getUserRole();
		}
		else
			userRole = x;
	
		
		//File fXmlFile = new File(beanName+".xml");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		NodeList ParentTag = doc.getElementsByTagName("fields");

		for (int temp = 0; temp < ParentTag.getLength(); temp++) {

			Node childFieldTag = ParentTag.item(temp);
			Element childFieldElement = (Element) childFieldTag;
			if (childFieldTag.getNodeType() == Node.ELEMENT_NODE) {
				NodeList FieldsNodeList = childFieldElement.getElementsByTagName("field");

				for (int temp1 = 0; temp1 < FieldsNodeList.getLength(); temp1++) {

					Node singleFieldNode = FieldsNodeList.item(temp1);
					//Element FieldElement = (Element) singleFieldNode;
					if (singleFieldNode.getNodeType() == Node.ELEMENT_NODE) {
						//NodeList FieldNodeList = FieldElement.getElementsByTagName("field");

						FieldAttributes myField = new FieldAttributes();
						Element fieldElement = (Element) FieldsNodeList.item(temp1);
						if (fieldElement.getElementsByTagName("List").item(
								0) != null) {
							String beanClassPath = fieldElement
									.getElementsByTagName("List")
									.item(0).getTextContent();
							
							myField.setList(beanClassPath);
							try {
								if (fieldElement.getElementsByTagName("type").item(0).getTextContent().equals("link")) 
								{
									BasicBean object = (BasicBean) Class.forName("aisha.bean."+beanClassPath)
								
										.newInstance();
					
							
									//Set<Startup> optionsList =  new HashSet<Startup>();
									//optionsList = ((Talent)bean).getStartups();
									myField.setOptions(getOptionsFromBean(object,bean));
									

								}
								

									if (fieldElement.getElementsByTagName("items").item(
											0) != null) 
									{
										BasicBean object = (BasicBean) Class.forName("aisha.bean."+beanClassPath)
									
											.newInstance();
						
									if(bean.getClass().getSimpleName().equals("Startup"))
										{
										//Set<Startup> optionsList =  new HashSet<Startup>();
										//optionsList = ((Talent)bean).getStartups();
										myField.setOptions(getOptionsFromBean(object,bean));
										}

									}
								if (fieldElement.getElementsByTagName("type").item(0).getTextContent().equals("select")) 
									{
									BasicBean talent = (BasicBean) Class.forName(beanClassPath)
											
											.newInstance();
									myField.setOptions(getOptionsFromDB(talent));
									}
								
								//myField.setOptions(getOptions(object));
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						/*	myField.setOptionsObject(basicDAO.get(object,
									beanClassPath, commonService));*/
						}
						
						
						if (fieldElement.getElementsByTagName("name").item(0) != null)
							myField.setName(fieldElement.getElementsByTagName("name").item(0).getTextContent());
						
						if (fieldElement.getElementsByTagName("nameEng").item(0) != null)
							myField.setNameEng(fieldElement.getElementsByTagName("nameEng").item(0).getTextContent());

						if (fieldElement.getElementsByTagName("nameArb").item(0) != null)
							myField.setNameArb(
									fieldElement.getElementsByTagName("nameArb").item(0).getTextContent());
						if (fieldElement.getElementsByTagName("regExpr").item(0) != null)
							myField.setRegExpr(fieldElement.getElementsByTagName("regExpr").item(0).getTextContent());
						if (fieldElement.getElementsByTagName("type").item(0) != null)
							myField.setType(
									fieldElement.getElementsByTagName("type").item(0).getTextContent());
						if (fieldElement.getElementsByTagName("required").item(0) != null)
							myField.setRequired(
									fieldElement.getElementsByTagName("required").item(0).getTextContent());

						if (fieldElement.getElementsByTagName("boxSize").item(0) != null)
							myField.setBoxSize(fieldElement.getElementsByTagName("boxSize").item(0).getTextContent());

						if (fieldElement.getElementsByTagName("multiple").item(0) != null)
							myField.setMultiple(fieldElement.getElementsByTagName("multiple").item(0).getTextContent());

						
						if (fieldElement.getElementsByTagName("maxLength").item(0) != null)
							myField.setMaxLength(
									fieldElement.getElementsByTagName("maxLength").item(0).getTextContent());

						//if (type!="View" && fieldElement.getElementsByTagName("enabledFor").item(0) != null)
						if (fieldElement.getElementsByTagName("enabledFor").item(0) != null)
						{

							String enableFor = fieldElement.getElementsByTagName("enabledFor").item(0).getTextContent();
							List<String> enableForlist = Arrays.asList(enableFor.split(","));
							if(!enableForlist.contains(accessMode) || accessMode.equals("Disabled"))
							   myField.setEnabledFor("disabled");
							//if(enableForlist.contains(accessMode) || enableForlist.contains("all"))		
						}
						/*else
							myField.setEnabledFor("disabled");*/
						
						/*if (fieldElement.getElementsByTagName("useAs").item(0) != null)
						{
							
						
							String useAs = fieldElement.getElementsByTagName("useAs").item(0).getTextContent();
							List<String> useAslist = Arrays.asList(useAs.split(","));
							if(useAslist.contains(type))
							   myField.setUseAs("yes");
									
						}*/
						
						if (fieldElement.getElementsByTagName("accessedBy").item(0) != null)
{
			
							
							String accessedBy = fieldElement.getElementsByTagName("accessedBy").item(0).getTextContent();
							List<String> accessedBylist = Arrays.asList(accessedBy.split(","));
							if(accessedBylist.contains(accessMode) || accessedBylist.contains("all"))
							   myField.setAccessedBy("yes");
									
						}
	
						if (fieldElement.getElementsByTagName("saveInField").item(0) != null)
							myField.setSaveInField(fieldElement.getElementsByTagName("saveInField").item(0).getTextContent());

						
						if (fieldElement.getElementsByTagName("options").item(0) != null)
						{
							Set<String> itemsList =  new HashSet<String>();	
							NodeList optionssNodeList = fieldElement.getElementsByTagName("item");

							for (int temp2 = 0; temp2 < optionssNodeList.getLength(); temp2++) {

								Node singleOtionNode = optionssNodeList.item(temp2);
								Element optionElement = (Element) singleFieldNode;
								if (optionElement.getNodeType() == Node.ELEMENT_NODE) {
							
									//Element optionElement = (Element) optionssNodeList.item(temp2);
									if (optionElement.getElementsByTagName("item").item(0) != null)
									//if (option.getElementsByTagName("option").item(0) != null)
									{
										
										itemsList.add(fieldElement.getElementsByTagName("item").item(temp2).getTextContent());
									
									}
									
									}
							}
							myField.setItems(itemsList);
							System.out.println("################# here are my items:"+itemsList);
						}
					
						//if(myField.getAccessedBy()=="yes" && myField.getUseAs()=="yes")
						if(myField.getAccessedBy()=="yes")  
						    result.add(myField);
					}

				}

			}

		}

		return result;
}

	
	public static Model fillModelGeneric(Model model,String beanName, String operation,BasicBean origBean, BasicBean newBean) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		String section = null;
		String mode = null;
	    String controllerName = null;
	    
	    
	    if(operation.equals("ListApps"))
	    {
	    	mode = "all";
	    }
	    
	    else if(operation.equals("List"))
	    {
	    	operation = "Summary";
	    	mode = "all";
	    }
   
	    if(beanName.equals("Resource") || beanName.equals("Package"))
	    	controllerName = "Asset";
	    else if(beanName.equals("Program"))
	    	controllerName = "Investor";
	    else
	    	controllerName = beanName;
	    
		if(operation.equals("Add"))
		{
			section = "platform-body-add";
			mode = "all";
		}
		
		
		if(operation.equals("SubmitAdd") || operation.equals("View"))
		{
			section = "platform-body-view-get";
			mode = "Disabled";
		}
		
		if(operation.equals("ViewProfile"))
		{
			operation = "Update";
			section = "platform-body-add";
			mode = "ProfileOwner";
		}
		
		
	    if(operation.equals("Get") && CurrentUser.getUserRole().equals("PlatformAdmin"))
	    {
	    	section = "platform-body-view-get";
			mode = "PlatformAdmin";
			operation = "Update";
	    }
	    
	    else if(operation.equals("Get") && !CurrentUser.getUserRole().equals("PlatformAdmin"))
	    {
	    	section = "platform-body-view-get";
			mode = "Disabled";
			operation = "View";
	    }	
	    
	    if(beanName.equals("Event") || operation.equals("Get"))
	    	mode = "Disabled";
	    
		return fillModel( model,  beanName,  controllerName + "Controller",  operation, section, origBean,  newBean, mode ) ;
	}
	
	public static Model fillModel(Model model, String beanName, String controllerName, String operation,String section,BasicBean origBean, BasicBean newBean,String mode ) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
        String accessMode = mode;
		Class cls = null;
			try {
				cls = Class.forName("aisha.bean." + beanName);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		ArrayList<String> publicFields = new ArrayList<String>();
		ArrayList<String> privateFields = new ArrayList<String>();
		ArrayList<String> connectionsFields = new ArrayList<String>();
		ArrayList<String> adminFields = new ArrayList<String>();
		ArrayList<String> addFields = new ArrayList<String>();
		ArrayList<String> viewFields = new ArrayList<String>();
		ArrayList<String> tableFields = new ArrayList<String>();
		
		List<FieldAttributes> myfields = new ArrayList<FieldAttributes>();
		ArrayList<FieldAttributes> allowedfields = new ArrayList<FieldAttributes>();
		FieldAttributes field = new FieldAttributes();
		
        Method getPublicFields = null ;
		Method getPrivateFields = null ;
		Method getConnectionsFields = null ;
		Method getAdminFields = null ;
		Method getAddFields = null ;
		Method getViewFields = null ;
		Method getTableFields = null ;
		
		try
		{
			getTableFields = cls.getDeclaredMethod("getTableFields");
			getAddFields = cls.getDeclaredMethod("getAddFields");
			getViewFields = cls.getDeclaredMethod("getViewFields");
	        getPublicFields = cls.getDeclaredMethod("getPublicFields");
			getPrivateFields = cls.getDeclaredMethod("getPrivateFields");
			getConnectionsFields = cls.getDeclaredMethod("getConnectionsFields");
			getAdminFields = cls.getDeclaredMethod("getAdminFields");
		
			
		}
		catch(Exception e)
		{System.out.println(" Exception !!!!!!!!!!!!!!!!!!!!!!!" + e.getMessage());}
		/*if(CurrentUser.getEntityId().equals(newBean.getId()))
			accessMode = "ProfileOwner";
		if(operation.equals("Add"))
			accessMode = "all";
		else if(CurrentUser.getUserRole().equals("PlatformAdmin"))
			accessMode = "PlatformAdmin";
		else if(mode.equals("Disabled"))
			accessMode = "Disabled";
		
		else
			accessMode = "None";*/
		
	/*	if(newBean.getClass().getSimpleName().equals("Startup") || newBean.getClass().getSimpleName().equals("Investor"))
			{
			   Connection connect = new Connection();
			   HashMap<String, Object> searchCriteria = new HashMap<>();
			   searchCriteria.put("outerId",(int) newBean.getId());
			   connect.setSearchCriteria(searchCriteria);
			   connect = connectionService.listConnections(connect);	  
			   List<BasicBean> resultList = connect.getResults();
			   resultList.forEach(item->{
					if(CurrentUser.getEntityId().equals(((Connection) item).getInnerId()))
					{
						mode.replace("","connection");
					}
			   });
		
			}
			   */
		Set<Investor> startupInvestors = new HashSet<Investor>();
		
		String beanString = null ;
		if(origBean!=null)
		 {
			ObjectMapper mapper = new ObjectMapper();
		 
		   try {
			 beanString = mapper.writeValueAsString(origBean);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 }
		

				if(operation.equals("View"))
				{
					try {
						myfields = getFormFields(beanName,operation,newBean,accessMode);
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
					viewFields = (ArrayList<String>) getViewFields.invoke(null);
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(viewFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
					 
				    if(accessMode.equals("PlatformAdmin"))
						{
				    	try {
							myfields = getFormFields(beanName,operation,newBean,mode);
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
				    	adminFields = (ArrayList<String>) getAdminFields.invoke(null);
				    	 
						 for(int i= 0 ; i < myfields.size() ; i++)
						 {
							 if(adminFields.contains(myfields.get(i).getName()))
						 
							 allowedfields.add(myfields.get(i));
					
						 }
						}
				    
				    if(accessMode.equals("connection"))
					{
				    	try {
							myfields = getFormFields(beanName,operation,newBean,accessMode);
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
				    	
					privateFields = (ArrayList<String>) getPrivateFields.invoke(null);
					//connectionsFields = (ArrayList<String>) getConnectionsFields.invoke(null);
 
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(privateFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
					}
					if(accessMode.equals("ProfileOwner") || accessMode.equals("PlatformAdmin"))
					{
						try {
							myfields = getFormFields(beanName,operation,newBean,accessMode);
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
					privateFields = (ArrayList<String>) getPrivateFields.invoke(null);
					connectionsFields = (ArrayList<String>) getConnectionsFields.invoke(null);
 
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(privateFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
					 
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(connectionsFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
					}
                }

				if(operation.equals("Add"))
				{
					try {
						myfields = getFormFields(beanName,operation,newBean,accessMode);
						
						
						
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
					addFields = (ArrayList<String>) getAddFields.invoke(null);
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(addFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
                }
			  
				if(operation.equals("Update"))
				{
					try {
						myfields = getFormFields(beanName,operation,newBean,accessMode);
						
						
						
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
					addFields = (ArrayList<String>) getViewFields.invoke(null);
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(addFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
                }
				if(operation.equals("Summary"))
				{
					try {
						myfields = getFormFields(beanName,operation,newBean,accessMode);
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
					tableFields = (ArrayList<String>) getTableFields.invoke(null);
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(tableFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
                }
				
				if(operation.equals("ListApps"))
				{
					try {
						operation = "Summary";
						myfields = getFormFields(beanName,operation,newBean,accessMode);
						 model.addAttribute("innerBean",  "Application");
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
					tableFields = (ArrayList<String>) getTableFields.invoke(null);
					 for(int i= 0 ; i < myfields.size() ; i++)
					 {
						 if(tableFields.contains(myfields.get(i).getName()))
					 
						 allowedfields.add(myfields.get(i));
				
					 }
                }
			  
								/*
				Method getPublicFields = cls.getDeclaredMethod("getPublicFields");
				Method getPrivateFields = cls.getDeclaredMethod("getPrivateFields");
				Method getConnectionsFields = cls.getDeclaredMethod("getConnectionsFields");
				Method getAdminFields = cls.getDeclaredMethod("getAdminFields");
				Method getAddFields = cls.getDeclaredMethod("getAddFields");
				Method getViewFields = cls.getDeclaredMethod("getViewFields");
				Method getTableFields = cls.getDeclaredMethod("getTableFields");*/
				
				//publicFields = (ArrayList<String>) getPublicFields.invoke(null);
				
				
				/* 
				if(mode.equals("profileOwner"))
					{
					try {
					
						myfields = getFormFields(beanName,operation,newBean);
						
					} catch (ParserConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					viewFields = (ArrayList<String>) getViewFields.invoke(null);
					 
					  for(int i= 0 ; i < myfields.size() ; i++)
					   {
						 if(viewFields.contains(myfields.get(i).getName()))
							 allowedfields.add(myfields.get(i));
					   }

	}*/
				
			/*	if(mode.equals("PlatformAdmin"))
				{
				try {
				
					myfields = getFormFields(beanName,operation,newBean);
					
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				viewFields = (ArrayList<String>) getViewFields.invoke(null);
				adminFields = (ArrayList<String>) getAdminFields.invoke(null);
				  for(int i= 0 ; i < myfields.size() ; i++)
				   {
					 if(viewFields.contains(myfields.get(i).getName()))
						 allowedfields.add(myfields.get(i));
					 if(adminFields.contains(myfields.get(i).getName()))
						 allowedfields.add(myfields.get(i));
				   }

}*/
				
				/*if(mode.equals("anonymousUser"))
				{
				try {
				
					myfields = getFormFields(beanName,operation,newBean);
					
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				viewFields = (ArrayList<String>) getViewFields.invoke(null);
				 
				  for(int i= 0 ; i < myfields.size() ; i++)
				   {
					 if(viewFields.contains(myfields.get(i).getName()))
						 allowedfields.add(myfields.get(i));
				   }

}
			*/	
				/*if(CurrentUser.getPriviliges().contains("8323072"))
					 privateFields = (ArrayList<String>) getPrivateFields.invoke(null);*/

			/*	if(CurrentUser.getPriviliges().contains(""
						+ ""))
					{*/
					//String beanClass = newBean.getClass().getSimpleName();
/*					if(beanClass.equals("Startup") && CurrentUser.getUserType().equals("Investor")||mode.equals("Admin"))
					{				
					Method getInvestors = cls.getDeclaredMethod("getInvestors");
					startupInvestors = (Set<Investor> ) getInvestors.invoke(newBean);
					for(int j=0;j<startupInvestors.size();j++)
					{
						if(startupInvestors!=null && startupInvestors.size()>0)
						{
						Iterator<Investor> itr = startupInvestors.iterator();
						while(itr.hasNext())
						{
					if(String.valueOf(itr.next().getId()).contains(CurrentUser.getUserKey()))
					{
					connectionsFields = (ArrayList<String>) getConnectionsFields.invoke(null);
					}
					}
					}
					}
					
					 for(int i= 0 ; i < myfields.size() ; i++)
					   {
						 if(publicFields.contains(myfields.get(i).getName()))
							 allowedfields.add(myfields.get(i));
						 if(privateFields.contains(myfields.get(i).getName()))
							 allowedfields.add(myfields.get(i));
						 if(connectionsFields.contains(myfields.get(i).getName()))
							 allowedfields.add(myfields.get(i));
						 if(adminFields.contains(myfields.get(i).getName()))
							 allowedfields.add(myfields.get(i));
						 
					   }
					}*/
				/*
				if(mode.equals("Admin"))
				{

				adminFields = (ArrayList<String>) getAdminFields.invoke(null);
                }*/
				
				/*try {
					myfields = getFormFields(beanName,operation,newBean);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	
System.out.println("$$$$$$$$$$$$$$$$ allowedfields : "+allowedfields);

			
		    model.addAttribute("beanName",  beanName);
			model.addAttribute("controllerName", controllerName);
			if(accessMode.equals("ProfileOwner") || accessMode.equals("PlatformAdmin"))
			model.addAttribute("operation", "Update");
			if(mode.equals("submitAdd"))
			model.addAttribute("operation", "submitAdd");
			else
		    model.addAttribute("operation", operation);	
			model.addAttribute("section", section);
			model.addAttribute("oldBean", beanString);
			model.addAttribute("bean", newBean);
			model.addAttribute("newBean", newBean);
			model.addAttribute("fields", allowedfields);
			if(CurrentUser.getUserType().equals("Investor") && newBean.getClass().getSimpleName().equals("Startup"))
				model.addAttribute("connect", "yes");
				if(CurrentUser.getUserType().equals("Startup") && newBean.getClass().getSimpleName().equals("Talent"))
					model.addAttribute("connect", "yes");
			if(operation.equals("Summary"))
				{
				model.addAttribute("beanList", newBean.getResults());
				model.addAttribute("tableFields", allowedfields);
				
				}

			/*if(beanName.equals("Startup"))
			
			{
				model.addAttribute("innerBean1", "Talent");
				model.addAttribute("innerBean2", "Investor");
			}
		
			if(beanName.equals("Talent"))
			model.addAttribute("innerBean", "Startup");
			
			if(beanName.equals("Investor"))
			model.addAttribute("innerBean", "Startup");*/
			/*if(mode.equals("Admin"))
			{
				for(int i= 0 ; i < myfields.size() ; i++)
					if(myfields.get(i).getName().equals("status"))
						{
				field = myfields.get(i);
				field.setEnabledFor(null);
				myfields.remove(i);
				myfields.add(field);
				model.addAttribute("userRole",  "Admin");
				model.addAttribute("fields",  myfields);
				}
			}
 
			else
				model.addAttribute("fields",  allowedfields);*/

		return model;
		}
}

