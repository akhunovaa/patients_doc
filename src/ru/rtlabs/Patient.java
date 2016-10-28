package ru.rtlabs;


import java.sql.Date;

public class Patient {

    private String name;
    private String surname;
    private String pName;
    private Date bdate;
    private String docType;
    private String docNumber;
    private String docSer;
    private Date docDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getPolNumber() {
        return docNumber;
    }

    public void setPolNumber(String polNumber) {
        this.docNumber = polNumber;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getDocSer() {
        return docSer;
    }

    public void setDocSer(String docSer) {
        this.docSer = docSer;
    }
}
