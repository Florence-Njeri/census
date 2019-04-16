package com.example.android.census2019;

public class Agent {
    private String id_agent;
    private String county;
    private String job_title;
    public Agent(){}
    public Agent(String id_agent,String job_title,String county){
        this.id_agent=id_agent;
        this.county=county;
        this.job_title=job_title;
    }

    public String getId_agent() {
        return id_agent;
    }

    public String getCounty() {
        return county;
    }

    public String getJob_title() {
        return job_title;
    }
}
