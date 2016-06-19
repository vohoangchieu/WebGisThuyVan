/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.List;
import org.apache.log4j.Logger;
import web.gis.DataAccess;
import web.gis.TramBTSEntity;
import web.gis.TramThuyVanEntity;
import web.gis.config.AppConfig;

/**
 *
 * @author chieuvh
 */
public class GenTramThuyVan {

    private static final Logger logger = Logger.getLogger(GenTramThuyVan.class);

    public static void main(String[] args) {
        DataAccess dataAccess = null;
        try {
            AppConfig.databaseUrl = "jdbc:mysql://127.0.0.1:3306/thuyvan?useUnicode=true&characterEncoding=UTF-8&";
            AppConfig.databaseUser = "root";
            AppConfig.databasePassword = "hoaiphuong";
            dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
//            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(AppConfig.dateFormat);
            List<TramBTSEntity> tramBTSEntitys = dataAccess.getAllTramBTS();
            for (TramBTSEntity tramBTSEntity : tramBTSEntitys) {
                if (tramBTSEntity.MaSo % 5 == 0) {
                    TramThuyVanEntity tramThuyVanEntity = new TramThuyVanEntity();
                    tramThuyVanEntity.tentram = tramBTSEntity.TenTram.replace("BTS ", "");
                    tramThuyVanEntity.vitri = tramBTSEntity.DiaChiLapDat;
                    tramThuyVanEntity.x = tramBTSEntity.ToaDoVD;
                    tramThuyVanEntity.y = tramBTSEntity.ToaDoKD;
                    dataAccess.insertTramThuyVan(tramThuyVanEntity);
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (dataAccess != null) {
                dataAccess.closeConnection();
            }
        }

    }
}
