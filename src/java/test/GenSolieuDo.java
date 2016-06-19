/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import web.gis.DataAccess;
import web.gis.SoLieuDoEntity;
import web.gis.TramBTSEntity;
import web.gis.TramThuyVanEntity;
import web.gis.config.AppConfig;

/**
 *
 * @author chieuvh
 */
public class GenSolieuDo {
    
    private static final Logger logger = Logger.getLogger(GenSolieuDo.class);
    
    public static void main(String[] args) throws ParseException {
        
        int milisinday = 24 * 60 * 60 * 1000;
        Random random = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date beginDate = simpleDateFormat.parse("2016-01-01 00:00:00");
        long beginTime = beginDate.getTime();
        long currentTime = System.currentTimeMillis();
        DataAccess dataAccess = null;
        try {
            AppConfig.databaseUrl = "jdbc:mysql://127.0.0.1:3306/thuyvan?useUnicode=true&characterEncoding=UTF-8&";
            AppConfig.databaseUser = "root";
            AppConfig.databasePassword = "hoaiphuong";
            dataAccess = new DataAccess(AppConfig.databaseUrl, AppConfig.databaseUser, AppConfig.databasePassword);
//            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(AppConfig.dateFormat);
            List<TramThuyVanEntity> tramThuyVanEntitys = dataAccess.getTramThuyVanList();
            for (TramThuyVanEntity tramThuyVanEntity : tramThuyVanEntitys) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2014, 0, 1, random.nextInt(23), random.nextInt(59));
                while (calendar.getTimeInMillis() < currentTime) {
                    SoLieuDoEntity soLieuDoEntity = new SoLieuDoEntity();
                    soLieuDoEntity.matram = tramThuyVanEntity.id;
                    soLieuDoEntity.ngayquantrac = calendar.getTime();
                    soLieuDoEntity.giatrithapnhat = 10 + random.nextInt(10) + random.nextInt(10) / 10f;
                    soLieuDoEntity.giatricaonhat = soLieuDoEntity.giatrithapnhat + random.nextInt(10) + random.nextInt(10) / 10f;
                    dataAccess.insertSoLieuDo(soLieuDoEntity);
                    calendar.add(Calendar.HOUR, 24);
                    calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(23));
                    calendar.set(Calendar.MINUTE, random.nextInt(59));
                }
                logger.info("finish " + tramThuyVanEntity.id);
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
