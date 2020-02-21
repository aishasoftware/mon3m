package aisha.security.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import aisha.bean.PlatformUser;
import aisha.dao.BasicDAO;
import aisha.security.dao.SystemUserDAO;
import aisha.util.PasswordGenerator;


public class SystemUserServiceImpl implements UserDetailsService,SystemUserService {

	@Autowired
	SystemUserDAO systemUserDBAdapter;
	@Autowired
	BasicDAO basicDBAdapter;	

	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender mailSender;
	
	@Transactional
	public PlatformUser getSystemUser(PlatformUser user)
	{
		user = (PlatformUser) basicDBAdapter.getBean(user);
	
System.out.println("############# shishi : "+user);
		return user;
		
	}
	/* this method add new system User after check it is not exist on dataBase
	*//* (non-Javadoc)
	 * @see com.ebs.commons.services.CommonService#addBean(com.ebs.commons.beans.BaseRequest)
	 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		System.out.println("######### loadUserByUsername");// TODO Auto-generated method stub
		
		PlatformUser user = new PlatformUser();
		//user.setUserName(arg0);
		//user.setUserName(arg0);
		//user.setPassword("123456");
		//user.setUserRole("Engineer");

		//user = cypherPassword(user);
		//systemUserDBAdapter.addBean(user);
	    HashMap<String, Object> searchCriteria = new HashMap<>();
	    
	    searchCriteria.put("userName", arg0);
	    user.setSearchCriteria(searchCriteria);
		user = (PlatformUser) basicDBAdapter.getBean(user);
		user.setEnabled(true);
		user.setLoginTryCount(0);
		//user.setUserFullName("aisha");
		//user.setPassword("aisha");
		//user.setPassword("aisha");
		//user = cypherPassword(user);
		System.out.println("############# after cypher : "+user);
		//user.setUserRole("ROLE_ADMIN");
		//user.setPassword("aisha");
		
		System.out.println("############# shishi : "+user);
		/*ApplicationController.setCurrentUser(user);*/
				return user;
	}
	protected PlatformUser cypherPassword(PlatformUser user) {
		final String thisMethod = "SystemUserController.addBean: ";
	
		
		String password = user.getPassword();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(password.getBytes(), 0, password.length());
			String hashedPass = new BigInteger(1, messageDigest.digest())
					.toString(16);
			if (hashedPass.length() < 32) {
				hashedPass = "0" + hashedPass;
			}

			user.setPassword(hashedPass);

		} catch (Exception e) {
			
			
		}

		return user;
	}
	
	
	@Transactional
	public long addSystemUser(PlatformUser user) {

		user.setStatus("active");
		user.setCreationTime(new Timestamp(System.currentTimeMillis()));
		user.setPassword("123456");
		PlatformUser cyper = cypherPassword(user);
		return basicDBAdapter.addBean(cyper);
	}
	
	
/*	public String generatePassayPassword() {
	    PasswordGenerator gen = new PasswordGenerator();
	    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
	    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
	    lowerCaseRule.setNumberOfCharacters(2);
	 
	    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
	    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
	    upperCaseRule.setNumberOfCharacters(2);
	 
	    CharacterData digitChars = EnglishCharacterData.Digit;
	    CharacterRule digitRule = new CharacterRule(digitChars);
	    digitRule.setNumberOfCharacters(2);
	 
	    CharacterData specialChars = new CharacterData() {
	        public String getErrorCode() {
	            return "505";
	        }
	 
	        public String getCharacters() {
	            return "!@#$%^&*()_+";
	        }
	    };
	    CharacterRule splCharRule = new CharacterRule(specialChars);
	    splCharRule.setNumberOfCharacters(2);
	 
	    String password = gen.generatePassword(10, splCharRule, lowerCaseRule, 
	      upperCaseRule, digitRule);
	    return password;
	}
*/

	@Transactional
	public void updateSystemUser(PlatformUser user) {
		// TODO Auto-generated method stub
		basicDBAdapter.updateBean(user);
	}

	@Transactional
	public PlatformUser listSystemUsers(PlatformUser user) {
		//PlatformUser result = systemUserDBAdapter.listPlatformUsers(user);
		PlatformUser result = (PlatformUser) basicDBAdapter.listBeans(user);
		return result;
	}
	
	
	@Override
	public PlatformUser resetPassword(String username) {
		PlatformUser user = new PlatformUser();
		user.setUserName(username);

		user = systemUserDBAdapter.getPlatformUser(user);
		if (user == null)
			return null;

		user = generatePassword(user);
		sendResetPasswordEmail(user);
		return user;
	}

	private PlatformUser generatePassword(PlatformUser user) {
		String pass = PasswordGenerator.generateRandomPassword(10);
		user.setPassword(pass);
		return user;
	}

	private void sendResetPasswordEmail(PlatformUser user) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("249Platform");
		message.setText("You have been nominated for Nobel prize " + user.getPassword());
		mailSender.send(message);
	}

	}