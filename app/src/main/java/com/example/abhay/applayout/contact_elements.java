package com.example.abhay.applayout;

/**
 * Created by abhay on 09/04/17.
 */

public class contact_elements {

    private String contact;
    private String contactname;
    private String msgflag;
    private String conflag;

    public contact_elements() {}

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getMsgflag() {
        return msgflag;
    }

    public void setMsgflag(String msgflag) {
        this.msgflag = msgflag;
    }

    public String getConflag() {
        return conflag;
    }

    public void setConflag(String conflag) {
        this.conflag = conflag;
    }

    public contact_elements(String contact,String contactname,String msgflag, String conflag)
    {
        this.contact=contact;
        this.contactname=contactname;
        this.msgflag=msgflag;
        this.conflag = conflag;

    }
}
