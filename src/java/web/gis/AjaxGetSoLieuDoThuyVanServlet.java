/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.gis;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import web.gis.config.AppConfig;

/**
 *
 * @author chieuvh
 */
public class AjaxGetSoLieuDoThuyVanServlet extends BaseServlet {

    private static final Logger logger = Logger.getLogger(AjaxGetSoLieuDoThuyVanServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataAccess dataAccess = null;
        AjaxResponse responseObject = new AjaxResponse();
        try {
            handle(request, responseObject, dataAccess);
            outContent(responseObject.toJsonString(), response);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (dataAccess != null) {
                dataAccess.closeConnection();
            }
        }

    }

    private void handle(HttpServletRequest request, AjaxResponse responseObject, DataAccess dataAccess) throws Exception {
        String namstr = Util.getParameter(request, "nam");
        if (StringUtils.isEmpty(namstr)) {
            responseObject.returnMessage = "Vui lòng chọn năm";
            return;
        }
        String thangstr = Util.getParameter(request, "thang");
        if (StringUtils.isEmpty(thangstr)) {
            responseObject.returnMessage = "Vui lòng chọn tháng";
            return;
        }

        String masostr = Util.getParameter(request, "id");
        if (StringUtils.isEmpty(masostr)) {
            responseObject.returnMessage = "Mã số không hợp lệ";
            return;
        }

        int nam, thang;
        try {
            nam = Integer.parseInt(namstr);
        } catch (Exception ex) {
            responseObject.returnMessage = "Năm không hợp lệ";
            return;
        }
        try {
            thang = Integer.parseInt(thangstr);
        } catch (Exception ex) {
            responseObject.returnMessage = "Tháng không hợp lệ";
            return;
        }
        int maso;
        try {
            maso = Integer.parseInt(masostr);
        } catch (Exception ex) {
            responseObject.returnMessage = "Mã số không hợp lệ";
            return;
        }
        dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
        List<SoLieuDoEntity> data = dataAccess.getSoLieuDoThuyVan(nam, thang, maso);
        responseObject.data = new Gson().toJson(data);
        responseObject.returnCode = 1;
    }

}
