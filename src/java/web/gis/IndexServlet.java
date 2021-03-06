/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.gis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import web.gis.config.AppConfig;

/**
 *
 * @author chieuvh
 */
public class IndexServlet extends BaseServlet {

    private static final Logger logger = Logger.getLogger(IndexServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataAccess dataAccess = null;
        try {
            dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(AppConfig.dateFormat);
            Gson gson = gsonBuilder.create();
            String x = Util.getParameter(request, "x");
            String y = Util.getParameter(request, "y");
            String zoom = Util.getParameter(request, "zoom");
            String requestUrl = Util.getRequestUrl(request);
            TemplateDataDictionary dic = TemplateDictionary.create();
            dic.showSection("popup");
            dic.showSection("tuyencap");
            dic.showSection("tramthuyvan");
            dic.showSection("top");
            dic.showSection("left");
            dic.showSection("right");
            dic.setVariable("x", x);
            dic.setVariable("y", y);
            dic.setVariable("zoom", zoom);
//            dic.setVariable("staticVersion",String.valueOf(System.currentTimeMillis()));// AppConfig.staticVersion);
            dic.setVariable("staticVersion","");// AppConfig.staticVersion);
//            dic.setVariable("staticVersion","0");
            dic.setVariable("webTitle", AppConfig.webTitle);
            dic.setVariable("contextPath", AppConfig.contextPath);
            dic.setVariable("homeLat", AppConfig.homeLat);
            dic.setVariable("homeLng", AppConfig.homeLng);
            dic.setVariable("homeZoomLevel", AppConfig.homeZoomLevel);
            dic.setVariable("detailZoomLevel", AppConfig.detailZoomLevel);
            dic.setVariable("requestUrl", requestUrl);
            dataAccess.getConnection();

            List<TramThuyVanEntity> tramThuyVanList = dataAccess.getTramThuyVanList();
//            List<TuyenCapEntity> tuyencapList = dataAccess.getAllTuyenCap();
            dic.setVariable("tramThuyVanList", gson.toJson(tramThuyVanList));
//            dic.setVariable("tuyencapList", gson.toJson(tuyencapList));
            Template template = getCTemplate("index");
            String data = template.renderToString(dic);
            outContent(data, response);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (dataAccess != null) {
                dataAccess.closeConnection();
            }
        }

    }

   

}
