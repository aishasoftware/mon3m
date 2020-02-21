package aisha.security.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import aisha.bean.PlatformUser;
import aisha.bean.Strategy;
import aisha.security.beans.SystemUser;

public interface SystemUserService extends UserDetailsService{
	   
	    public long addSystemUser(PlatformUser sysUser);
	    public PlatformUser getSystemUser(PlatformUser user);
	    public void updateSystemUser(PlatformUser user);
		public PlatformUser listSystemUsers(PlatformUser user);
		public PlatformUser resetPassword(String username) ;
	 
}
