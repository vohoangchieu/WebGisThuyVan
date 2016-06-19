/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.gis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
public class AjaxDeleteTuyenCapServlet extends BaseServlet {

    private static final Logger logger = Logger.getLogger(AjaxDeleteTuyenCapServlet.class);

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
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(AppConfig.dateFormat);
        String Id = Util.getParameter(request, "Id");
        if (StringUtils.isEmpty(Id)) {
            responseObject.returnMessage = "Mã số tuyến cáp không hợp lệ";
            return;
        }
       
        dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
        dataAccess.getConnection();
        
       
        int maso = 0;
        try {
            maso = Integer.parseInt(Id);
            if (maso < 1) {
                responseObject.returnMessage = "Mã số tuyến cáp  không hợp lệ";
                return;
            }
        } catch (Exception ex) {
            responseObject.returnMessage = "Mã số tuyến cáp không hợp lệ";
            return;
        }
      
        int row = dataAccess.deleteTuyenCap(maso);
        if (row >= 0) {
            responseObject.returnCode = 1;
            responseObject.returnMessage = "Xóa tuyến cáp thành công";
        } else {
            responseObject.returnMessage = "Lỗi hệ thống, vui lòng thử lại sau";
        }

    }

  

}
