package com.SYSC4806_Group13.SYSC4806_Project.Security.oauth2.user;



import com.SYSC4806_Group13.SYSC4806_Project.Exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

import static com.SYSC4806_Group13.SYSC4806_Project.Model.AuthProvider.google;


public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}