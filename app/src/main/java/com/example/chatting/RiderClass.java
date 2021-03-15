package com.example.chatting;

public class RiderClass {
    private String Msg,Colr;
    public RiderClass()
    {

    }
    public RiderClass(String m,String c)
    {
this.Msg=m;
this.Colr=c;
    }
    public String getMsg() {
        return Msg;
    }

    public void setMsg(String m) {
        this.Msg = m;
    }

    public String getColr() {
        return Colr;
    }

    public void setName(String c) {
        this.Colr = c;
    }
}
