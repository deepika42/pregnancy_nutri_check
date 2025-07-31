package com.example.pregnancynutricheck;

public class Model {
    private int id;
    private String name;
    private String detail;
    private String date;
    private String time;
    private String note;

    public Model(int id, String name, String detail, String date, String time, String note){
        this.id= id;
        this.name= name;
        this.detail= detail;
        this.date= date;
        this.time= time;
        this.note= note;

    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDetail(){
        return detail;
    }
    public void setDetail(String detail){
        this.detail=detail;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note=note;
    }
}
