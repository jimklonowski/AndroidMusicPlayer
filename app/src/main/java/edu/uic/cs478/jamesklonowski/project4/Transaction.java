package edu.uic.cs478.jamesklonowski.project4;

/**
 * Created by Jimmy on 4/25/2015.
 */
public class Transaction {
    private long id;
    private String date;
    private String time;
    private String dateTime;
    private String status;
    private String type;
    private int clipnumber;

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getDateTime(){
        return this.dateTime;
    }
    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getTime(){
        return this.time;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public int getClipnumber(){
        return this.clipnumber;
    }
    public void setClipnumber(int clipnumber){
        this.clipnumber = clipnumber;
    }
    @Override
    public String toString(){
        return id+" "+dateTime+" "+type+" "+clipnumber+" "+status;
    }
}
