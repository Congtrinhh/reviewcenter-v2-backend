package com.trinhquycong.restloginreviewcenter.security.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		// TODO Auto-generated constructor stub
	}

	@Override
    public String getId() {
        return (String) attributes.get("sub");
    }
 
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

}
