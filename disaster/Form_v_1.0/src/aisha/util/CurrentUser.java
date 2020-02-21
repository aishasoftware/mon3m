package aisha.util;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import aisha.bean.PlatformUser;
import aisha.security.beans.SystemUser;

public class CurrentUser {
public static int getUserId()
{
	PlatformUser currentUser = new PlatformUser();
	String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
	if (user != "anonymousUser")
	    {
		currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	int userId = (int) currentUser.getId();
	return userId;
	    }
	else
	return 0;
}

public static String getPriviliges()
{
	PlatformUser currentUser = new PlatformUser();
	String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
	if (user != "anonymousUser")
	    {
		Collection<SimpleGrantedAuthority> Authorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	return Authorities.toString();
	    }
	else
	return "0";
}


public static String getUserRole()
{
	PlatformUser currentUser = new PlatformUser();
String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
if (user != "anonymousUser")
    {
	currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
	String userRole =  currentUser.getUserRole();
	return userRole;
}
else
	currentUser.setUserRole("anonymousUser");	
return user;
}

public static String getUserName()
{
	PlatformUser currentUser = new PlatformUser();
String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
if (user != "anonymousUser")
    {
	currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
	String userName =  currentUser.getUserName();
	return userName;
}
return user;
}

public static String getUserType()
{
	PlatformUser currentUser = new PlatformUser();
	String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
	if (user != "anonymousUser")
        {
		currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		String userType =  currentUser.getUserType();
		return userType;
}
	return user;
}

public static String getUserKey()
{
	PlatformUser currentUser = new PlatformUser();
	String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
	if (user != "anonymousUser")
        {
		currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		String userKey =  currentUser.getUserKey();
		return userKey;
}
	return user;
}
public static String getEntityId()
{
	PlatformUser currentUser = new PlatformUser();
	String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();	
	if (user != "anonymousUser")
        {
		currentUser =  (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		String userKey =  currentUser.getUserKey();
		return userKey;
}
	return user;
}

public static String getEntityType()
{
	SystemUser currentUser =  (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
	String entityType = currentUser.getUserType();
	return entityType;
}


}
