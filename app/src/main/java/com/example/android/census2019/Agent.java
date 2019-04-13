package com.example.android.census2019;

public class Agent {
private String id;
private String county;
private String job_title;
    public Agent(){}
    public Agent(String id,String job_title,String county){
        this.id=id;
        this.county=county;
        this.job_title=job_title;
    }

    public String getId() {
        return id;
    }

    public String getCounty() {
        return county;
    }

    public String getJob_title() {
        return job_title;
    }
}
