package com.example.android.census2019;

public class Household {
    private String head;
    private String county;
    private String subCounty;
    private String town;
    private String adultChildren;
    private String underageChildren;
    private String spouses;
    private String id;

    public Household() {
    }

    public Household(String head, String county,String subCounty) {
        this.head = head;
        this.county = county;
        this.subCounty=subCounty;
//        this.spouses=spouses;
//        this.subCounty=subCounty;
//        this.town=town;
//        this.adultChildren=adultChildren;
//        this.underageChildren=underageChildren;
//        this.id=id;

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

    public String getTown() {
        return town;
    }

    public String getAdultChildren() {
        return adultChildren;
    }

    public String getUnderageChildren() {
        return underageChildren;
    }

    public String getSpouses() {
        return spouses;
    }

    public String getId() {
        return id;
    }
}


