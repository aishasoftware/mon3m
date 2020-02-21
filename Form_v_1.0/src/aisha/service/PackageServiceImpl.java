package aisha.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aisha.bean.Package;
import aisha.bean.PlatformUser;
import aisha.dao.BasicDAO;

 
@Service
@Transactional
public class PackageServiceImpl implements PackageService {


@Autowired
private BasicDAO basicDAO;




@Override
public long addPackage(Package complain) {
	
	// profile.setCreationTime(new Date());
	complain.setStatus("active");
     //SystemUser currentUser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // profile.setCreatedBy(currentUser.getUserName());
	complain.setStatus("inserted");
	long result = basicDAO.addBean(complain);

	return result;
	
}


@Override
public Package listPackages(Package complain)
 {

		Package result =  (Package) this.basicDAO.listBeans(complain);
	
		return result;
	
}


@Override
public Package getPackage(Package complain) {
	
	Package result = (Package) this.basicDAO.getBean(complain);

	return result;
}


@Override
public void updatePackage(Package pack) {
	
	//profile.setLastModificationTime(new Date());
   // PlatformUser currentUser = (PlatformUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //profile.setLastModifiedBy(currentUser.getUsearName());
	this.basicDAO.updateBean(pack);
	
}

@Override
	public List<String> getPriviliges(String packId) {
	return this.basicDAO.getPriviliges(packId);

	}
 
}