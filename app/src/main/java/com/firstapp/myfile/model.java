package com.firstapp.myfile;

public class model {
    String eid,en,ea,ef,er,es;

    public model() {
    }

    public model(String eid, String en, String ea, String ef, String er, String es) {
        this.eid = eid;
        this.en = en;
        this.ea = ea;
        this.ef = ef;
        this.er = er;
        this.es = es;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEa() {
        return ea;
    }

    public void setEa(String ea) {
        this.ea = ea;
    }

    public String getEf() {
        return ef;
    }

    public void setEf(String ef) {
        this.ef = ef;
    }

    public String getEr() {
        return er;
    }

    public void setEr(String er) {
        this.er = er;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }
}
