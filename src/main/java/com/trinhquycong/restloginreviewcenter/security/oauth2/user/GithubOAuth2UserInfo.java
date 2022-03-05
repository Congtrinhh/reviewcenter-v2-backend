package com.trinhquycong.restloginreviewcenter.security.oauth2.user;

import java.util.Map;
import org.springframework.util.StringUtils;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		// TODO Auto-generated constructor stub
	}

	@Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }
 
    @Override
    public String getName() {
    	String name = (String) attributes.get("name"); // if user has already set name in his profile
    	if (!StringUtils.hasText(name)) {
    		name = (String) attributes.get("login"); // get default login name of user
    	}
        return name;
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }

}
