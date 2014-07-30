/**
 * 
 */
package com.domeke.app.model;

import java.security.NoSuchAlgorithmException;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author Administrator
 *
 */
public class User extends Model<User> {
	
	private static final String USER_CACHE = "usercache";
	public static User dao = new User();
	
    public User login(String email, String password){
        return dao.findFirst("select id, username, email, password from user where email=? and password=?", email, getMD5(password.getBytes()));
    }
    
    private String getMD5(byte[] src){
        StringBuffer sb=new StringBuffer();
        try {
            java.security.MessageDigest md=java.security.MessageDigest.getInstance("MD5");
            md.update(src);
            for(byte b : md.digest())
                sb.append(Integer.toString(b>>>4&0xF,16)).append(Integer.toString(b&0xF,16));
        } catch (NoSuchAlgorithmException e) {}
        return sb.toString();
    }
    private void removeCache(int id){
        CacheKit.remove(USER_CACHE, id);
    }
	
}
