/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifco.inventory.bean;

import com.prueba.model.primefaces.Util;
import com.sifco.businesspartner.bean.BusinessPartner;
import com.sifcoapp.assignment.bean.AccassignmentBean;
import com.sifcoapp.client.AccountingEJBClient;
import com.sifcoapp.client.AdminEJBClient;
import com.sifcoapp.client.InventoryEJBClient;
import com.sifcoapp.objects.accounting.to.AccountTO;
import com.sifcoapp.objects.admin.to.ArticlesInTO;
import com.sifcoapp.objects.admin.to.ArticlesTO;
import com.sifcoapp.objects.admin.to.BranchArticlesTO;
import com.sifcoapp.objects.admin.to.BranchTO;
import com.sifcoapp.objects.catalogos.Common;
import com.sifcoapp.objects.common.to.ResultOutTO;
import com.sifcoapp.objects.inventory.to.GoodsReceiptDetailTO;
import com.sifcoapp.objects.inventory.to.GoodsReceiptInTO;
import com.sifcoapp.objects.inventory.to.GoodsreceiptTO;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Digits;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "goodsReceiptBean")
@SessionScoped
public class GoodsReceiptBean implements Serializable {

    HttpSession session = Util.getSession();

//<editor-fold defaultstate="collapsed" desc="Declaración de variables para formulario" >
    private static AdminEJBClient AdminEJBService;
    private static InventoryEJBClient inventoryEJB;
    private static AccountingEJBClient AccountingEJBClient;
    private String completeCodigo; //autocompletado del campo codigo
    private String completeNomArt; //autocompletado del campo nombre articulo

    //--------------------------------------------------------------------------
    private GoodsreceiptTO newReceipt = new GoodsreceiptTO(); //cabecera de la pantalla
    private ArrayList<GoodsreceiptTO> listaBusquedaTable = new ArrayList<GoodsreceiptTO>();
    private List listaBusqueda = new Vector();
    private Object selectReceipt = new GoodsreceiptTO();
    private int docEntry;             //No.
    private int docNum;
    private String almDest;     //Almacen destino
    private Date fechaConta = new Date();    //Fecha Contabilizacion

    private Date fechaDoc = new Date();      //Fecha Documento
    private String refe;        //Referencia
    private String comentario;  //Comentario
    private String newCodCuenta;
    private String newNomCuenta;
    //--------------------------------------------------------------------------
    //nuevo detalle
    private GoodsReceiptDetailTO selectDetail = new GoodsReceiptDetailTO();

    private ArrayList<GoodsReceiptDetailTO> listaDetalles = new ArrayList<GoodsReceiptDetailTO>();  //lista para el datatable <GoodsReceiptDetailTO>
    private List listaPadre = new Vector();
    private String newCod;
    private String newNomArt;
    private Double newTotal;
    private String newUnidad;
    private Double newCostoPromedio;

    @Digits(integer = 14, fraction = 2, message = "Maximo 2 Decimales en Precio")
    private Double newPrecio;

    @Digits(integer = 14, fraction = 2, message = "Cantidad inadecuada")
    private Double newCantidad;

    private Double newExistencia;

    //--------------------------------------------------------------------------
    private List<BranchTO> listaAlmacenes; //para llenar el combo-box
    private String nomAlmacen;
    private String codAlmacen;

    //--------------------------------------------------------------------------
    private int varEstados;     //0=guardar; 1=update; 2=buscar
    private String botonEstado;
    private boolean disable, disableComun, btn1, btn2;
    private boolean docNumState;

    //--------------------------------------------------------------------------
    //Confirmacion
    private String msjDialog;
    private boolean confirm;
    private int toolbarBoton;
    private boolean rendered;
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="G & S" >
    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public String getNewCodCuenta() {
        return newCodCuenta;
    }

    public void setNewCodCuenta(String newCodCuenta) {
        this.newCodCuenta = newCodCuenta;
    }

    public String getNewNomCuenta() {
        return newNomCuenta;
    }

    public void setNewNomCuenta(String newNomCuenta) {
        this.newNomCuenta = newNomCuenta;
    }

    public Double getNewCostoPromedio() {
        return newCostoPromedio;
    }

    public void setNewCostoPromedio(Double newCostoPromedio) {
        this.newCostoPromedio = newCostoPromedio;
    }

    public String getMsjDialog() {
        return msjDialog;
    }

    public void setMsjDialog(String msjDialog) {
        this.msjDialog = msjDialog;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public int getDocNum() {
        return docNum;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public ArrayList<GoodsreceiptTO> getListaBusquedaTable() {
        return listaBusquedaTable;
    }

    public void setListaBusquedaTable(ArrayList<GoodsreceiptTO> listaBusquedaTable) {
        this.listaBusquedaTable = listaBusquedaTable;
    }

    public Object getSelectReceipt() {
        return selectReceipt;
    }

    public void setSelectReceipt(Object selectReceipt) {
        this.selectReceipt = selectReceipt;
    }

    public boolean isBtn1() {
        return btn1;
    }

    public void setBtn1(boolean btn1) {
        this.btn1 = btn1;
    }

    public boolean isBtn2() {
        return btn2;
    }

    public void setBtn2(boolean btn2) {
        this.btn2 = btn2;
    }

    public List getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }

    public boolean isDisableComun() {
        return disableComun;
    }

    public void setDisableComun(boolean disableComun) {
        this.disableComun = disableComun;
    }

    public boolean isDocEntryState() {
        return docNumState;
    }

    public void setDocEntryState(boolean docEntryState) {
        this.docNumState = docEntryState;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getBotonEstado() {
        return botonEstado;
    }

    public void setBotonEstado(String botonEstado) {
        this.botonEstado = botonEstado;
    }

    public int getVarEstados() {
        return varEstados;
    }

    public void setVarEstados(int varEstados) {
        this.varEstados = varEstados;
    }

    public GoodsReceiptDetailTO getSelectDetail() {
        return selectDetail;
    }

    public void setSelectDetail(GoodsReceiptDetailTO selectDetail) {
        this.selectDetail = selectDetail;
    }

    public Double getNewExistencia() {
        return newExistencia;
    }

    public void setNewExistencia(Double newExistencia) {
        this.newExistencia = newExistencia;
    }

    public Double getNewPrecio() {
        return newPrecio;
    }

    public void setNewPrecio(Double newPrecio) {
        this.newPrecio = newPrecio;
    }

    public Double getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(Double newTotal) {
        this.newTotal = newTotal;
    }

    public String getNewUnidad() {
        return newUnidad;
    }

    public void setNewUnidad(String newUnidad) {
        this.newUnidad = newUnidad;
    }

    public GoodsreceiptTO getReceipt() {
        return newReceipt;
    }

    public void setReceipt(GoodsreceiptTO receipt) {
        this.newReceipt = receipt;
    }

    public List<GoodsReceiptDetailTO> getListaDetalles() {
        return listaDetalles;
    }

    public String getNewCod() {
        return newCod;
    }

    public void setNewCod(String newCod) {
        this.newCod = newCod;
    }

    public String getNewNomArt() {
        return newNomArt;
    }

    public void setNewNomArt(String newNomArt) {
        this.newNomArt = newNomArt;
    }

    public Double getNewCantidad() {
        return newCantidad;
    }

    public void setNewCantidad(Double newCantidad) {
        this.newCantidad = newCantidad;
    }

    public int getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(int docEntry) {
        this.docEntry = docEntry;
    }

    public String getAlmDest() {
        return almDest;
    }

    public void setAlmDest(String almDest) {
        this.almDest = almDest;
    }

    public Date getFechaConta() {
        return fechaConta;
    }

    public void setFechaConta(Date fechaConta) {
        this.fechaConta = fechaConta;
    }

    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public String getRefe() {
        return refe;
    }

    public void setRefe(String refe) {
        this.refe = refe;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setListaDetalles(ArrayList<GoodsReceiptDetailTO> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getNomAlmacen() {
        return nomAlmacen;
    }

    public void setNomAlmacen(String nomAlmacen) {
        this.nomAlmacen = nomAlmacen;
    }

    public List<BranchTO> getListaAlmacenes() {
        return listaAlmacenes;
    }

    public void setListaAlmacenes(List<BranchTO> listaAlmacenes) {
        this.listaAlmacenes = listaAlmacenes;
    }

    public String getCompleteCodigo() {
        return completeCodigo;
    }

    public void setCompleteCodigo(String completeCodigo) {
        this.completeCodigo = completeCodigo;
    }

    public String getCompleteNomArt() {
        return completeNomArt;
    }

    public void setCompleteNomArt(String completeNomArt) {
        this.completeNomArt = completeNomArt;
    }

    //</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Load de Pantalla" >    
    @PostConstruct
    public void initForm() {

        if (AdminEJBService == null) {
            AdminEJBService = new AdminEJBClient();
        }

        if (inventoryEJB == null) {
            inventoryEJB = new InventoryEJBClient();
        }

        if (AccountingEJBClient == null) {
            AccountingEJBClient = new AccountingEJBClient();
        }

        //llenar los almacenes en combobox
        String var = null;
        try {
            this.setListaAlmacenes((List<BranchTO>) AdminEJBService.getBranch(var, var));

        } catch (Exception e) {
            faceMessage(e.getMessage() + " -" + e.getCause());
            System.out.println(e.getMessage() + " -" + e.getCause());
        }

        //estado por defecto
        estateGuardar();

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Autocompletado" > 
    public List<String> completeText(String query) {
        List _result = null;
        String var = null;

        ArticlesInTO in = new ArticlesInTO();
        in.setItemCode(var);
        in.setItemName(query);

        try {
            _result = AdminEJBService.getArticles(in);

        } catch (Exception e) {
        }

        List<String> results = new ArrayList<String>();

        Iterator<ArticlesTO> iterator = _result.iterator();

        while (iterator.hasNext()) {
            ArticlesTO articulo = (ArticlesTO) iterator.next();
            results.add(articulo.getItemName());
        }
        return results;
    }

    public List<String> completeCode(String query) {
        List _result = null;
        String var = null;

        ArticlesInTO in = new ArticlesInTO();
        in.setItemCode(query);
        in.setItemName(var);

        try {
            _result = AdminEJBService.getArticles(in);

        } catch (Exception e) {
        }

        List<String> results = new ArrayList<String>();

        Iterator<ArticlesTO> iterator = _result.iterator();

        while (iterator.hasNext()) {
            ArticlesTO articulo = (ArticlesTO) iterator.next();
            results.add(articulo.getItemCode());
        }
        return results;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Autocompletado de CUENTA ">
    public List<String> completeName(String query) {
        List _result = null;

        String filterByCode = null;
        try {

            _result = AccountingEJBClient.getAccountByFilter(filterByCode, query, "Y");
        } catch (Exception ex) {
            Logger.getLogger(AccassignmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> results = new ArrayList<>();

        Iterator<AccountTO> iterator = _result.iterator();
        while (iterator.hasNext()) {
            AccountTO cuentas = (AccountTO) iterator.next();
            results.add(cuentas.getAcctname());
        }
        return results;
    }

    public List<String> completeCode2(String query) {
        List _result = null;

        String filterByName = null;
        try {
            _result = AccountingEJBClient.getAccountByFilter(query, filterByName, "Y");
        } catch (Exception ex) {
            Logger.getLogger(AccassignmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> results = new ArrayList<>();

        Iterator<AccountTO> iterator = _result.iterator();
        while (iterator.hasNext()) {
            AccountTO cuentas = (AccountTO) iterator.next();
            results.add(cuentas.getAcctcode());
        }
        return results;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Evento al seleccionar del autocomplete CUENTA" > 
    public void findAccount(SelectEvent event) {
        List account = new Vector();
        String var = null;
        if (event.getObject().toString() != var) {
            List _result = null;

            try {
                if (newCodCuenta != null || newNomCuenta != null) {
                    _result = AccountingEJBClient.getAccountByFilter(newCodCuenta, newNomCuenta);
                }

            } catch (Exception e) {
                faceMessage(e.getMessage() + " -- " + e.getCause());
                newCodCuenta = null;
                newNomCuenta = null;
            }
            if (_result.isEmpty()) {
                this.newCodCuenta = null;
                this.newNomCuenta = null;

            } else {
                Iterator<AccountTO> iterator = _result.iterator();
                while (iterator.hasNext()) {
                    AccountTO articulo = (AccountTO) iterator.next();
                    account.add(articulo);
                }
                if (account.size() == 1) {
                    try {
                        System.out.println("articulo unico, llenar campos en pantalla");
                        AccountTO art = (AccountTO) account.get(0);
                        if (newCodCuenta != null || newNomCuenta != null) {
                            newCodCuenta = art.getAcctcode();
                            newNomCuenta = art.getAcctname();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(BusinessPartner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    for (Object ac : account) {
                        AccountTO art = (AccountTO) ac;
                        if (newNomCuenta.equals(art.getAcctname())) {
                            newCodCuenta = art.getAcctcode();
                            newNomCuenta = art.getAcctname();
                            break;
                        }
                    }

                    /* AccountTO art = (AccountTO) account.get(0);
                     if (newCodCuenta != null || newNomCuenta != null) {
                     newCodCuenta = art.getAcctcode();
                     newNomCuenta = art.getAcctname();
                     }*/
                }
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Evento al seleccionar del ComboBox de almacenes" > 
    public void stateChangeListener(ValueChangeEvent event) {
        System.out.print(event.getNewValue().toString());
        if (event.getNewValue() != almDest) {
            try {
                List _result = null;
                String var = null;
                _result = AdminEJBService.getBranch(event.getNewValue().toString(), var);
                Iterator<BranchTO> iterator = _result.iterator();
                BranchTO articulo = (BranchTO) iterator.next();
                nomAlmacen = articulo.getWhsname();
            } catch (Exception ex) {
                Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Evento al seleccionar del autocomplete" > 
    public void findArticle(SelectEvent event) {
        List articulos = new Vector();
        String var = null;

        if (event.getObject().toString() != var) {
            List _result = null;

            ArticlesInTO in = new ArticlesInTO();
            in.setItemCode(newCod);
            in.setItemName(newNomArt);

            try {
                _result = AdminEJBService.getArticles(in);

            } catch (Exception e) {
                faceMessage(e.getMessage() + " -- " + e.getCause());
                newCod = null;
                newNomArt = null;
            }
            if (_result.isEmpty()) {
                this.newCod = null;
                this.newNomArt = null;

            } else {
                Iterator<ArticlesTO> iterator = _result.iterator();
                while (iterator.hasNext()) {
                    ArticlesTO articulo = (ArticlesTO) iterator.next();
                    articulos.add(articulo);
                }
                if (articulos.size() == 1) {
                    try {
                        System.out.println("articulo unico, llenar campos en pantalla");
                        ArticlesTO art = (ArticlesTO) articulos.get(0);
                        newNomArt = art.getItemName();
                        newCod = art.getItemCode();
                        newUnidad = art.getInvntryUom();
                        newExistencia = art.getOnHand();
                        newCostoPromedio = art.getAvgPrice();
                        art = AdminEJBService.getArticlesByKey(newCod);
                        if (almDest != "-1") {
                            List alm = art.getBranchArticles();
                            Iterator<BranchArticlesTO> iter = alm.iterator();
                            while (iter.hasNext()) {
                                BranchArticlesTO branch = (BranchArticlesTO) iter.next();
                                if (branch.getWhscode().equals(almDest)) {
                                    newExistencia = branch.getOnhand();
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    for (Object artt : articulos) {
                        ArticlesTO art = (ArticlesTO) artt;
                        if (newNomArt.equals(art.getItemName())) {
                            newNomArt = art.getItemName();
                            newCod = art.getItemCode();
                            newUnidad = art.getBuyUnitMsr();
                        }
                    }//cierre for
                }
            }
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Agregar detalles al dataTable" > 
    public void accionAgregar(ActionEvent actionEvent) {
        try {
            if (validarNull()) {
                calcularTotal();
                GoodsReceiptDetailTO nuevoDetalle = new GoodsReceiptDetailTO();

                nuevoDetalle.setObjtype(30 + "");
                nuevoDetalle.setItemcode(newCod);
                nuevoDetalle.setDscription(newNomArt);
                nuevoDetalle.setQuantity(newCantidad);
                nuevoDetalle.setPrice(newPrecio);
                nuevoDetalle.setLinetotal(newTotal);
                nuevoDetalle.setUnitmsr(newUnidad);
                nuevoDetalle.setLinenum(UUID.randomUUID().hashCode());
                nuevoDetalle.setAcctcode(newCodCuenta);
                // nuevoDetalle.setLinenum(getListaDetalles().size() + 1);

                if (listaDetalles == null) {
                    listaDetalles = new ArrayList<>();
                }
                if (listaPadre == null) {
                    listaPadre = new Vector();
                }

                listaPadre.add(nuevoDetalle);
                getListaDetalles().add(nuevoDetalle);
                //cleanDetalle();
            }
        } catch (Exception e) {
            faceMessage("Valores Incorrectos: " + e.getMessage() + e.getCause());
        }
        cleanDetalle();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Funciones varias" > 
    public void reload() throws IOException {
        // ...

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    //valida que los campos esten llenos para agregar un detalle al datatable
    public boolean validarNull() {
        return !newCodCuenta.isEmpty() && !newCod.isEmpty() && !newNomArt.isEmpty() && newCantidad > 0 && newPrecio > 0; //&& !newUnidad.isEmpty();
    }

    //limpiar variables
    public void cleanDetalle() {
        newCod = null;
        newNomArt = null;
        newPrecio = null;
        newCantidad = null;
        newTotal = null;
        newUnidad = null;
        newExistencia = null;
        newCostoPromedio = null;
        newCodCuenta = null;
        newNomCuenta = null;
    }

    //calcula el total visual en pantalla
    public void calcularTotal() {
        try {
            if (newPrecio > 0 && newCantidad > 0 && newPrecio != null && newCantidad != null) {
                Double aux = (newPrecio) * (newCantidad);
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                String st = nf.format(aux);
                Double dou = Double.valueOf(st);
                newTotal = dou;
            }
        } catch (Exception e) {
            //faceMessage("Error al calcular total: " + e.getMessage() + "-" + e.getCause());
            //faceMessage("Precio: " + newPrecio + "  " + "Cantidad: " + newCantidad);
        }

    }

    //validaciones antes de guardar a la base
    public boolean validarDatosReceipt() {
        if (fechaConta == null && fechaDoc == null) {
            faceMessage("Fechas vacias");
            return false;
        }
        if (almDest.contains("-1")) {
            faceMessage("Seleccione un Almacen");
            return false;
        }
        if (getListaDetalles().size() < 1) {
            faceMessage("Ingrese al menos, 1 articulo");
            return false;
        }
        return true;
    }

    //ventana emergente
    public void addArt() {
        RequestContext.getCurrentInstance().openDialog("/faces/view/inventory/GoodsReceiptProducto.xhtml");
    }

    /**
     * Mandar un String para mostrar en pantalla
     *
     * @param var
     */
    public void faceMessage(String var) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(var));
    }

    //limpiar variables
    public void cleanBean() {
        this.docEntry = 0;
        this.docNum = 0;
        this.almDest = null;
        this.nomAlmacen = null;
        this.refe = null;
        this.comentario = null;
        Date d = new Date();
        this.setFechaConta(d);
        this.setFechaDoc(d);
        cleanDetalle();
        this.newReceipt = new GoodsreceiptTO();
        this.listaDetalles.clear();
        this.listaPadre.clear();

        this.listaBusquedaTable.clear();
        this.listaBusqueda.clear();
    }

    //limpiar variables 2
    public void cleanBean2() {
        cleanBean();
        Date d = null;
        this.setFechaConta(d);
        this.setFechaDoc(d);
    }

    //llenar pantalla dado un Receipt
    public void llenarPantalla(GoodsreceiptTO var) {
        setDocNum(var.getDocnum());
        setDocEntry(var.getDocentry());
        setAlmDest(var.getTowhscode());
        setFechaDoc(var.getDocduedate());
        setFechaConta(var.getDocdate());
        setRefe(var.getRef1());
        setComentario(var.getComments());

        for (Object detalle : var.getGoodReceiptDetail()) {
            GoodsReceiptDetailTO det = (GoodsReceiptDetailTO) detalle;
            this.listaDetalles.add(det);
        }
    }

    //evento al seleccionar un elemento del dialogo
    public void selectDialog() {
        showHideDialog("dlg2", 2);
        GoodsreceiptTO var = (GoodsreceiptTO) selectReceipt;
        //llenarPantalla(var);

        try {
            newReceipt = inventoryEJB.getGoodsReceiptByKey(var.getDocentry());
            llenarPantalla(newReceipt);
            estateActualizar();
        } catch (Exception ex) {
            Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
            faceMessage("Error en busqueda por PK");
        }

        listaBusqueda = new Vector();
        RequestContext.getCurrentInstance().update("formReceipt");

    }

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

    public void confirmDialog(ActionEvent actionEvent) {
        showHideDialog("dlg9", 2);
        if (varEstados == 1) {
            saveReceipt();
        } else {
            if (varEstados == 2) {
                updateReceipt();
            }
        }
    }

    public void confirmDialog2(ActionEvent actionEvent) {
        showHideDialog("dlg10", 2);
        this.confirm = true;
        if (toolbarBoton == 1) {
            cleanBean();
        } else {
            cleanBean2();
        }

        RequestContext.getCurrentInstance().update("formReceipt");
    }

    public void cancelDialog(ActionEvent actionEvent) {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('dlg9').hide();");
    }

    public void cancelDialog2(ActionEvent actionEvent) {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('dlg10').hide();");
    }

    public boolean validarClear() {
        return this.listaPadre.size() >= 1;
    }

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Guardar en base" > 
    public void saveReceipt() {
        int line = 0;
        String vacio = null;
        if (newReceipt == null) {
            newReceipt = new GoodsreceiptTO();
        }
        newReceipt.setObjtype(30 + "");
        newReceipt.setUsersign((int) session.getAttribute("usersign"));
        newReceipt.setTowhscode(almDest);
        newReceipt.setDocdate(fechaConta);
        newReceipt.setDocduedate(fechaDoc);
        //newReceipt.setRef1(refe);
        //newReceipt.setComments(comentario);

        if (refe.equals("")) {
            newReceipt.setRef1(vacio);
        } else {
            newReceipt.setRef1(refe);
        }

        if (comentario.equals("")) {
            newReceipt.setComments(vacio);
        } else {
            newReceipt.setComments(comentario);
        }

        Iterator<GoodsReceiptDetailTO> iterator2 = listaPadre.iterator();
        while (iterator2.hasNext()) {
            GoodsReceiptDetailTO articleDetalle = (GoodsReceiptDetailTO) iterator2.next();
            articleDetalle.setLinenum(line + 1);
            line = line + 1;
        }

        newReceipt.setGoodReceiptDetail(listaPadre);

        try {
            ResultOutTO _res;
            _res = inventoryEJB.inv_GoodsReceipt_mtto(newReceipt, 1); //1 insert

            if (_res.getCodigoError() == 0) {//se realizo correctamente
                docEntry = _res.getDocentry();
                docNum = docEntry; //
                faceMessage(_res.getMensaje());

                estateActualizar();

            } else {
                faceMessage(_res.getMensaje());
            }
        } catch (Exception ex) {
            Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            faceMessage(ex.getMessage() + "-" + ex.getCause());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Update en base" > 
    public void updateReceipt() {
        // if (validarDatosReceipt()) {
        newReceipt.setObjtype(30 + "");
        newReceipt.setUsersign((int) session.getAttribute("usersign"));
        newReceipt.setDocentry(docEntry);
        newReceipt.setDocnum(docNum);
        newReceipt.setTowhscode(almDest);
        newReceipt.setDocdate(fechaConta);
        newReceipt.setDocduedate(fechaDoc);
        newReceipt.setRef1(refe);
        newReceipt.setComments(comentario);
        newReceipt.setGoodReceiptDetail(listaPadre);

        try {
            ResultOutTO _res; //= new ResultOutTO();
            _res = inventoryEJB.inv_GoodsReceipt_mtto(newReceipt, 2); //2 Update

            if (_res.getCodigoError() == 0) {//se realizo correctamente
                docEntry = _res.getDocentry();
                faceMessage(_res.getMensaje());

            } else {
                faceMessage(_res.getMensaje());
            }
        } catch (Exception ex) {
            Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            faceMessage(ex.getMessage() + "-" + ex.getCause());
        }
        //}
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Buscar en base" >
    public void searchReceipt() {

        GoodsReceiptInTO searchReceipt = new GoodsReceiptInTO();

        searchReceipt.setDocnum(docNum);
        searchReceipt.setDocdate(fechaConta);
        searchReceipt.setDocduedate(fechaDoc);
        if (refe.equals("")) {
            searchReceipt.setRef1(null);
        } else {
            searchReceipt.setRef1(refe);
        }
        if (comentario.equals("")) {
            searchReceipt.setComments(null);
        } else {
            searchReceipt.setComments(comentario);
        }

        if (almDest.equals("-1")) {
            searchReceipt.setTowhscode(null);
        } else {
            searchReceipt.setTowhscode(almDest);
        }

        try {
            listaBusqueda = inventoryEJB.getGoodsreceipt(searchReceipt);
        } catch (Exception e) {
            faceMessage(e.getMessage() + "Error en la busqueda");
        }
        if (!listaBusqueda.isEmpty()) {
            if (listaBusqueda.size() == 1) {
                faceMessage("Elemento unico encontrado");
                newReceipt = (GoodsreceiptTO) listaBusqueda.get(0);
                try {
                    GoodsreceiptTO var2 = inventoryEJB.getGoodsReceiptByKey(newReceipt.getDocentry());
                    llenarPantalla(var2);
                    estateActualizar();
                } catch (Exception ex) {
                    Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
                    faceMessage("Error en busqueda por PK");
                }

            } else {
                faceMessage("Seleccione un elemento");

                for (Object receipt : listaBusqueda) {
                    GoodsreceiptTO var = (GoodsreceiptTO) receipt;
                    listaBusquedaTable.add(var);
                }
                showHideDialog("dlg2", 1);

            }

        } else {
            faceMessage("No se obtuvieron resultados de la Busqueda");
        }
        //listaBusqueda = new Vector();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Eliminar del dataTable" > 
    public void deleteDetalle() {
        try {
            boolean var1, var2;
            var1 = getListaDetalles().remove(this.selectDetail);
            var2 = listaPadre.remove(this.selectDetail);
            this.selectDetail = null;
            if (var1 && var2) {
                faceMessage("Articulo Eliminado.");

            } else {
                faceMessage("Seleccione un articulo para eliminar");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "---" + e.getCause());
            //faceMessage("No pudo eliminarse la entrada: " + e.getMessage());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manejo de estados de la pantalla GUARDAR; ACTUALIZAR; BUSCAR; NUEVO" > 
    public void estateGuardar() {//Estado por defecto
        this.varEstados = Common.MTTOINSERT; //1;
        this.botonEstado = "Guardar";
        this.disable = false;
        this.disableComun = false;
        this.docNumState = true;
        this.rendered = true;
        RequestContext.getCurrentInstance().update("formReceipt");
    }

    public void estateActualizar() {//se activa automaticamente despues de Guardar o buscar
        this.varEstados = Common.MTTOUPDATE; //2
        this.botonEstado = "Actualizar";
        this.disable = true;
        this.disableComun = true;
        this.docNumState = true;
        this.rendered = false;
        //RequestContext.getCurrentInstance().update("formReceipt");
        try {
            reload();
        } catch (IOException ex) {
            Logger.getLogger(GoodsReceiptBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void estateBuscar() {
        this.varEstados = 3; //buscar
        this.botonEstado = "Buscar";
        this.disable = false;
        this.disableComun = true;
        this.rendered = false;
        docNumState = false;
        RequestContext.getCurrentInstance().update("formReceipt");
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Botones toolbar " > 
    public void botonNuevo(ActionEvent actionEvent) {
        if (validarClear() && varEstados != 2) {//mas de un detalle
            toolbarBoton = 1;
            showHideDialog("dlg10", 1);
            if (confirm) {
                confirm = false;
                estateGuardar();
            }
        } else {
            cleanBean();
            estateGuardar();
        }
    }

    public void botonBuscar(ActionEvent actionEvent) {
        if (validarClear() && varEstados != 2) {//mas de un detalle
            toolbarBoton = 2;
            showHideDialog("dlg10", 1);
            if (confirm) {
                confirm = false;
                estateBuscar();
            }
        } else {
            cleanBean2();
            estateBuscar();
        }
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Boton Principal" >
    public void botonPrincipal(ActionEvent actionEvent) {
        switch (varEstados) {
            case 1:
                if (validarDatosReceipt()) {
                    showHideDialog("dlg9", 1);
                }
                break;

            case 2:
                if (validarDatosReceipt()) {
                    showHideDialog("dlg9", 1);
                }
                break;

            case 3:
                listaBusqueda.clear();
                listaBusquedaTable.clear();
                searchReceipt();
                break;

            default:
                break;

        }

    }
//</editor-fold>

}//final de la clase
