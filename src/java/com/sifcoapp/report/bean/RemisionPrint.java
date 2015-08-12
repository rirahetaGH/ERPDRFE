/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifcoapp.report.bean;

import com.sifcoapp.client.AdminEJBClient;
import com.sifcoapp.client.SalesEJBClient;
import com.sifcoapp.objects.admin.to.CatalogTO;
import com.sifcoapp.objects.sales.to.SalesDetailTO;
import com.sifcoapp.objects.sales.to.SalesTO;
import com.sifcoapp.report.common.numerosAletras;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemisionPrint extends HttpServlet {

    private static AdminEJBClient AdminEJBService;
    private static SalesEJBClient SalesEJBService;
    private numerosAletras convertNumber;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request2 servlet request2
     * @param response2 servlet response2
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request2, HttpServletResponse response2) throws ServletException, IOException {
        SalesTO var = new SalesTO();
        CatalogTO _R = new CatalogTO();
        String numberToletter = null;
        Double total = 0.0;
        try {
            convertNumber = new numerosAletras() {
            };
            SalesEJBService = new SalesEJBClient();
            AdminEJBService = new AdminEJBClient();

            var = SalesEJBService.getSalesByKey(Integer.parseInt(request2.getParameter("foo")));//quemado
            _R = AdminEJBService.findCatalogByKey(var.getPeymethod(), 8);
            total = formatNumber(var.getDoctotal());
            numberToletter = convertNumber.convertNumberToLetter(total);

        } catch (Exception ex) {
            Logger.getLogger(RemisionPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
        response2.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response2.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "\n"
                    + "    <head>\n"
                    + "        <title>Sales Print</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "<script type=\"text/javascript\">\n"
                    + "window.print();\n"
                    + "window.open('', '_self', ''); window.close(); \n"
                    + "</script>"
                    + "\n"
                    + "    </head>\n"
                    + "\n"
                    + "    <body style=\"font-size: 10px\">\n"
                    + "        <!-- tamanio de factura 566x453px-->\n"
                    + "        <div style=\"width: 690px; height: 453px\">\n"
                    + "\n"
                    + "            <table style=\"width:100%\" border=\"0\">\n"
                    + "                <!-- borde top y unica fila-->\n"
                    + "                <tr>\n"
                    + "                    <td style=\"height: 115px\">\n"
                    + "\n"
                    + "                    </td>\n"
                    + "                </tr>\n"
                    + "                <tr>\n"
                    + "                    <!-- borde izquierdo -->\n"
                    + "                    <td style=\"width: 5px; height: 313px\"></td>\n"
                    + "\n"
                    + "                    <!-- contenido -->\n"
                    + "                    <td style=\"width: 556px; height: 313px\">\n"
                    + "                        <!-- ENCABEZADO -->\n"
                    + "                        <table style=\"width:100%\" border=\"0\">\n"
                    + "\n"
                    + "                            <!-- Encabezado 56px -->\n"
                    + "                            \n"
                    + "                            <tr style=\"height: 18px\">\n"
                    + "                                <td style=\"width: 60px\">\n"
                    + "                                    \n"
                    + "                                </td>\n"
                    + "                                <td colspan=\"5\">\n"
                    + "                                    " + var.getCardname() + "\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    " + "remision" + "\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 18px\">\n"
                    + "                                <td>\n"
                    + "                                    \n"
                    + "                                </td>\n"
                    + "                                <td style=\"width: 50px\">\n"
                    + "                                    \n"
                    + "                                </td>\n"
                    + "                                <td colspan=\"2\">\n"
                    + "                                    " + "" + "\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 18px\">\n"
                    + "                                <td></td>\n"
                    + "                                <td colspan=\"5\">\n"
                    + "                                    " + "" + "\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    " + _R.getCatvalue() + "\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 2px\"/>\n"
                    + "\n"
                    + "                            <!-- header detalles 30px -->\n"
                    + "                            <tr style=\"height: 30px\">\n"
                    + "                                <td colspan=\"7\"></td>\n"
                    + "                            </tr>\n"
                    + "                        </table>\n"
                    + "\n"
                    + "                        <!-- DETALLES -->\n"
                    + "                        <table border=\"0\">\n"
                    + "                            <!-- Detalles de factura 148px -->\n");
            int tam = var.getSalesDetails().size();
            List det = var.getSalesDetails();
            for (int i = 0; i < 12; i++) {

                if (i < tam) {
                    SalesDetailTO var2 = (SalesDetailTO) det.get(i);
                    out.println(
                            "                            <tr style=\"height: 14px; width: 100%\" >\n"
                            + "\n"
                            + "                                <td style=\"width: 60px\">" + var2.getQuantity() + "</td>\n"
                            + "                                <td style=\"width: 95px\">" + var2.getItemcode() + "</td>\n"
                            + "                                <td style=\"width: 270px\">" + var2.getDscription() + "</td>\n"
                            + "                                <td style=\"width: 60px\">$" + var2.getPrice() + "</td>\n"
                            + "                                <td style=\"width: 50px\">" + "$0.00" + "</td>\n"
                            + "                                <td style=\"width: 50px\">" + "$0.00" + "</td>\n"
                            + "                                <td style=\"width: 10px\">$" + var2.getLinetotal() + "</td>\n"
                            + "\n"
                            + "                            </tr>\n");
                } else {
                    String vacio = "";
                    out.println(
                            "                            <tr style=\"height: 14px; width: 100%\" >\n"
                            + "\n"
                            + "                                <td style=\"width: 60px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 95px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 270px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 60px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 50px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 50px\">" + vacio + "</td>\n"
                            + "                                <td style=\"width: 10px\">" + vacio + "</td>\n"
                            + "\n"
                            + "                            </tr>\n");
                }
            }

            out.println("                        </table>\n"
                    + "                        \n"
                    + "                        <!-- TOTALES -->\n"
                    + "                        <table border=\"0\">\n"
                    + "                            <!-- Parte de totales de factura -->\n"
                    + "                            <tr style=\"height: 18px\">\n"
                    + "                                <td style=\"width: 50px\"/>\n"
                    + "                                <td valign=\"bot\" style=\"width: 550px\">\n"
                    + "                                    " + numberToletter + "\n"
                    + "                                </td>\n"
                    + "                                <td valign=\"bot\">\n"
                    + "                                    $" + total + "\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 15.8px\">\n"
                    + "                                <td style=\"width: 50px\"/>\n"
                    + "                                <td   style=\"width: 100px\">\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    $0.00\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 15.8px\">\n"
                    + "                                <td style=\"width: 50px\"/>\n"
                    + "                                <td   style=\"width: 100px\">\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    $0.00\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 15.8px\">\n"
                    + "                                <td style=\"width: 50px\"/>\n"
                    + "                                <td   style=\"width: 100px\">\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    $0.00\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                            <tr style=\"height: 15.8px\">\n"
                    + "                                <td style=\"width: 50px\"/>\n"
                    + "                                <td  style=\"width: 100px\">\n"
                    + "                                </td>\n"
                    + "                                <td>\n"
                    + "                                    $" + total + "\n"
                    + "                                </td>\n"
                    + "                            </tr>\n"
                    + "                        </table>\n"
                    + "                    </td> \n"
                    + "\n"
                    + "                    <!-- borde derecho -->\n"
                    + "                    <td style=\"width: 5px; height: 313px\"></td>\n"
                    + "                </tr>\n"
                    + "                <tr>\n"
                    + "                    <td style=\"height: 36px\">\n"
                    + "\n"
                    + "                    </td>\n"
                    + "                </tr>\n"
                    + "            </table>\n"
                    + "            \n"
                    + "        </div>\n"
                    + "        <!--<object data=\"/sifcoappFE/servlets/report/PDF?faces-redirect=true\" type=\"application/pdf\">\n"
                    + "            <embed src=\"/sifcoappFE/servlets/report/PDF?faces-redirect=true\" type=\"application/pdf\" />\n"
                    + "        </object> \n"
                    + "        <iframe class=\"page-icon preview-pane\" frameborder=\"0\" height=\"352\" width=\"396\" src=\"C:\\Users\\Valentin\\Desktop\\PDF.pdf\"></iframe>\n"
                    + "        -->\n"
                    + "    </body>\n"
                    + "</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request2 servlet request2
     * @param response2 servlet response2
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request2, HttpServletResponse response2)
            throws ServletException, IOException {
        processRequest(request2, response2);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request2 servlet request2
     * @param response2 servlet response2
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request2, HttpServletResponse response2)
            throws ServletException, IOException {
        processRequest(request2, response2);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formato Numeros NO USADA">
    public Double formatNumber(Double num) {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        String st = nf.format(num);
        Double dou = Double.valueOf(st);
        return dou;
    }

//</editor-fold>
}//cierre de clase

