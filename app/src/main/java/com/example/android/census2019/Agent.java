package com.example.android.census2019;

public class Agent {
String id;
String phone;
String county;
String job;
    public Agent(){}
    public Agent(String id,String phone, String county,String job){
        this.id=id;
        this.phone=phone;
        this.county=county;
        this.job=job;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getCounty() {
        return county;
    }

    public String getJob() {
        return job;
    }
}
