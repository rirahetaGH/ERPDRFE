/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifcoapp.bank.bean;

import com.sifcoapp.report.bean.RepAccount;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "periodEndBean")
@SessionScoped
public class PeriodEndBean implements Serializable{

    public PeriodEndBean() {
    }
    
//<editor-fold defaultstate="collapsed" desc="Variables">
    private final RepAccount rep = new RepAccount();
    private Date fecha = new Date();
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="G & S">

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
    
    
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="btn guardar">
    public void doSave(){
        faceMessage("Cerrar periodo");
        rep.initForm();
        rep.setFtype(3);
        rep.setFdateReport(fecha);
        rep.setReportLevel(3);
        try {
            rep.print(1);
        } catch (Exception ex) {
            faceMessage(ex.getMessage() + " " + ex.getCause());
            Logger.getLogger(PeriodEndBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        showHideDialog("dlgC2", 2);
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="btn Principal">
    public void btnPrincipal(){
        //faceMessage("guardar");
        showHideDialog("dlgC2", 1);
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Funcions varias">
    public void confirmDialog() {
        showHideDialog("dlgC2", 2);
        doSave();
    }
    
    //Mostrar u ocultar un dialogo; 1 muestra, 2 oculta
    public void showHideDialog(String name, int openClose) {
        try {
            RequestContext rc = RequestContext.getCurrentInstance();
            if (openClose == 1) {
                rc.execute("PF('" + name + "').show();");
            }
            if (openClose == 2) {
                rc.execute("PF('" + name + "').hide();");
            }

        } catch (Exception e) {
            faceMessage(e.getMessage() + "---" + e.getCause());
        }
    }
    
    public void faceMessage(String var) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(var));
    }
//</editor-fold>
    
}//cierre de clase
