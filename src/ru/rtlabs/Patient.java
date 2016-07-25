package ru.rtlabs;


import java.sql.Date;

public class Patient {
    private String name;
    private String Surname;
    private String pName;
    private Date bDate;
    private String polNumber;
    private Date attachmentDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Date getbDate() {
        return bDate;
    }

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }

    public String getPolNumber() {
        return polNumber;
    }

    public void setPolNumber(String polNumber) {
        this.polNumber = polNumber;
    }

    public Date getAttachmentDate() {
        return attachmentDate;
    }

    public void setAttachmentDate(Date attachmentDate) {
        this.attachmentDate = attachmentDate;
    }
}
