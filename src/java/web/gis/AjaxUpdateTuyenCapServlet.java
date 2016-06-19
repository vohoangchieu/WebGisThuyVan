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
public class AjaxUpdateTuyenCapServlet extends BaseServlet {

    private static final Logger logger = Logger.getLogger(AjaxUpdateTuyenCapServlet.class);

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
        Gson gson = gsonBuilder.create();
        String Id = Util.getParameter(request, "Id");
        if (StringUtils.isEmpty(Id)) {
            responseObject.returnMessage = "Mã số tuyến cáp không hợp lệ";
            return;
        }
        String Ten = Util.getParameter(request, "Ten");
        if (StringUtils.isEmpty(Ten)) {
            responseObject.returnMessage = "Vui lòng nhập tên tuyến cáp";
            return;
        }
        String X1 = Util.getParameter(request, "X1");
        if (StringUtils.isEmpty(X1)) {
            responseObject.returnMessage = "Vui lòng nhập tọa độ X1";
            return;
        }
        String Y1 = Util.getParameter(request, "Y1");
        if (StringUtils.isEmpty(Y1)) {
            responseObject.returnMessage = "Vui lòng nhập tọa độ Y1";
            return;
        }
        String X2 = Util.getParameter(request, "X2");
        if (StringUtils.isEmpty(X2)) {
            responseObject.returnMessage = "Vui lòng nhập tọa độ X2";
            return;
        }
        String Y2 = Util.getParameter(request, "Y2");
        if (StringUtils.isEmpty(Y2)) {
            responseObject.returnMessage = "Vui lòng nhập tọa độ Y2";
            return;
        }

        dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
        dataAccess.getConnection();
        float x1, y1, x2, y2;
        try {
            x1 = Float.parseFloat(X1);
        } catch (Exception ex) {
            responseObject.returnMessage = "Tọa độ X1 không hợp lệ";
            return;
        }
        try {
            y1 = Float.parseFloat(Y1);
        } catch (Exception ex) {
            responseObject.returnMessage = "Tọa độ Y1 không hợp lệ";
            return;
        }
        try {
            x2 = Float.parseFloat(X2);
        } catch (Exception ex) {
            responseObject.returnMessage = "Tọa độ X2 không hợp lệ";
            return;
        }
        try {
            y2 = Float.parseFloat(Y2);
        } catch (Exception ex) {
            responseObject.returnMessage = "Tọa độ Y2 không hợp lệ";
            return;
        }
        int id = 0;
        try {
            id = Integer.parseInt(Id);
            if (id < 1) {
                responseObject.returnMessage = "Mã số tuyến cáp không hợp lệ";
                return;
            }
        } catch (Exception ex) {
            responseObject.returnMessage = "Mã số tuyến cáp không hợp lệ";
            return;
        }
        TuyenCapEntity entity = new TuyenCapEntity();
        entity.Id = id;
        entity.Ten = Ten;
        entity.X1 = x1;
        entity.Y1 = y1;
        entity.X2 = x2;
        entity.Y2 = y2;

       
        int row = dataAccess.updateTuyenCap(entity);
        if (row >= 0) {
            responseObject.data = entity.toJsonString();
            responseObject.returnCode = 1;
            responseObject.returnMessage = "Cập nhật tuyến cáp thành công";
        } else {
            responseObject.returnMessage = "Lỗi hệ thống, vui lòng thử lại sau";
        }

    }

}
