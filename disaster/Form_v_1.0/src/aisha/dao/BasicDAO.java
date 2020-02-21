package aisha.dao;

import java.util.List;

import aisha.bean.BasicBean;
import aisha.bean.Talent;


 
public interface BasicDAO {
 
    public long addBean(BasicBean basic);
    public BasicBean getBean(BasicBean basic);
    public BasicBean getMyBean(BasicBean basic);
    public void updateBean(BasicBean basic);		
	public BasicBean listBeans(BasicBean basic);
	public List<String> getPriviliges(String packId);
     
}