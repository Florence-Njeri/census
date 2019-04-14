package com.example.android.census2019;

public class Household {
    private String head;
    private String county;
    private String subCounty;

    public Household() {
    }

    public Household(String head, String county,String subCounty) {
        this.head = head;
        this.county = county;
        this.subCounty=subCounty;


    }

    public String getCounty() {
        return county;
    }

    public String getHead() {
        return head;
    }

    public String getSubCounty() {
        return subCounty;
    }


}


