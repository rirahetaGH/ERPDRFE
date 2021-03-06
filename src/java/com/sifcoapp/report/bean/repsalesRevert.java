/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifcoapp.report.bean;

import com.sifcoapp.client.AdminEJBClient;
import com.sifcoapp.objects.admin.to.EnterpriseTO;
import com.sifcoapp.report.common.AbstractReportBean;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ri00642
 */
@ManagedBean(name = "rsalesRev")
@RequestScoped
public class repsalesRevert implements Serializable {

    private String fcode;
    private String fname;
    private Date fdatefrom;
    private Date fdateto;
    @ManagedProperty(value = "#{reportsBean}")
    private ReportsBean bean;
 private static AdminEJBClient AdminEJBService = null;
    private int ftype;
    
    @PostConstruct
    public void initForm() {
        this.setFtype(1);
        Calendar c1 = GregorianCalendar.getInstance();

        Date sDate = c1.getTime();
        this.setFdateto(sDate);
        c1.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), 1);  //January 30th 2000
        sDate = c1.getTime();
        this.setFdatefrom(sDate);
        if (AdminEJBService == null) {
            AdminEJBService = new AdminEJBClient();
        }
    }
    /*
     * despliega pdf a pantalla
     * Rutilio
     * Abril 2015
     */
    public void doPrint() {
        this.print(0);
    }

    public void print(int _type) {
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        String _whereclausule = null;
        String _whereclausuleSR = null;
        String _reportname = null;
        String _reportTitle = null;
        EnterpriseTO resp=null;
        try {
            resp=AdminEJBService.getEnterpriseInfo();
        } catch (Exception ex) {
            Logger.getLogger(repPurchases.class.getName()).log(Level.SEVERE, null, ex);
        }
        _reportname = "/sales/dailySalesRevert";
        _reportTitle = "Notas de Crédito";

        _whereclausule = " cn.docentry=cnd.docentry and cn.docdate>=$P{pdocdate} and cn.docdate<=$P{PDOCDATE2}";
        if (this.getFcode() != null && this.getFcode().length() > 0) {
            _whereclausule += " and cn.docnum=" + this.getFcode();
        }
        if (this.getFname() != null && this.getFname().length() > 0) {
            _whereclausule += " and cn.cardcode='" + this.getFname() + "'";
        }
        System.out.println(_whereclausule);
        reportParameters.put("corpName", resp.getCrintHeadr());
        reportParameters.put("pdocdate", this.getFdatefrom());
        reportParameters.put("PDOCDATE2", this.getFdateto());
        reportParameters.put("PWHERE", _whereclausule);
        reportParameters.put("PWHERESR", _whereclausuleSR);
        reportParameters.put("reportName", _reportTitle);
        if (_type == 0) {
            getBean().setExportOption(AbstractReportBean.ExportOption.valueOf(AbstractReportBean.ExportOption.class, "PDF"));
        }
        if (_type == 1) {
            getBean().setExportOption(AbstractReportBean.ExportOption.valueOf(AbstractReportBean.ExportOption.class, "FILE"));
            getBean().setFileName(_reportTitle);
        }
        getBean().setParameters(reportParameters);
        getBean().setReportName(_reportname);
        getBean().execute();

    }

    public void printFormat() {
        this.print(1);
    }

    /**
     * Creates a new instance of repsales
     */
    public repsalesRevert() {
    }

    /**
     * @return the fcode
     */
    public String getFcode() {
        return fcode;
    }

    /**
     * @param fcode the fcode to set
     */
    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the bean
     */
    public ReportsBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(ReportsBean bean) {
        this.bean = bean;
    }

    /**
     * @return the fdatefrom
     */
    public Date getFdatefrom() {
        return fdatefrom;
    }

    /**
     * @param fdatefrom the fdatefrom to set
     */
    public void setFdatefrom(Date fdatefrom) {
        this.fdatefrom = fdatefrom;
    }

    /**
     * @return the fdateto
     */
    public Date getFdateto() {
        return fdateto;
    }

    /**
     * @param fdateto the fdateto to set
     */
    public void setFdateto(Date fdateto) {
        this.fdateto = fdateto;
    }

    /**
     * @return the ftype
     */
    public int getFtype() {
        return ftype;
    }

    /**
     * @param ftype the ftype to set
     */
    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

}
