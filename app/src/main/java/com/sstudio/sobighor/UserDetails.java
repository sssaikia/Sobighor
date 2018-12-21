package com.sstudio.sobighor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Alan on 9/5/2017.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserDetails {
    String userId;
    String userEmail;
    int no;
    public UserDetails(){

    }
    public UserDetails(int no,String userId,String userEmail){
        this.no=no;
        this.userId=userId;
        this.userEmail=userEmail;
    }
    public String getUserId(){
        return userId;
    }
    public String getUserEmail(){
        return userEmail;
    }
    public int getNo(){
        return no;
    }
}
