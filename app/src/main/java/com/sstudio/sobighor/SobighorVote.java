package com.sstudio.sobighor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Alan on 9/4/2017.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class SobighorVote {
    private String name;
    private int no;
    private  int age;
    public SobighorVote(){

    }
    public SobighorVote(int no,String name,int age){
        this.no=no;
        this.name=name;
        this.age=age;
    }
    public String getName(){
        return name;
    }
    public int getNo(){
        return no;
    }
    public int getAge(){
        return age;
    }
}
