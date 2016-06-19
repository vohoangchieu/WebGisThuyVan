/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.gis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import web.gis.config.AppConfig;

/**
 *
 * @author chieuvh
 */
public class DataAccess {

    private static final Logger logger = Logger.getLogger(DataAccess.class);
    private Connection conn = null;
    private String host = null;
    private String port = null;
    private String dbname = null;
    private String user = null;
    private String pass = null;
    private String url = null;

    public DataAccess(String host, String port, String dbname, String user, String pass) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.user = user;
        this.host = host;
        this.pass = pass;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&";

    }

    public DataAccess(String url, String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;

    }

    public void getConnection() throws ClassNotFoundException, SQLException {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        Class.forName(JDBC_DRIVER);
//        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useUnicode=true&characterEncoding=UTF-8&";
        conn = DriverManager.getConnection(url, user, pass);
    }

    public void closeConnection() {
        try {
            if (conn != null && conn.isClosed() == false) {
                conn.close();
                conn = null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public int insertTramBTS(TramBTSEntity tramBTSEntity) throws SQLException {
        String sql = "INSERT INTO `thongtin_trambts` ("
                + " `TenTram`, `NgayLapDat`, `DiaChiLapDat`, `TinhThanhLD`, "
                + "`QuanHuyenLD`, `PhuongXaLD`, `TrangThai`, `TinhTrang`, `DonViThue`,"
                + " `ToaDoKD`, `ToaDoVD`, `LoaiTram`, `LoaiCotAT`, `ChieuCao`,"
                + " `DTSanLap`, `TenDVQL`, `DiaChiDVQL`, `DienThoaiDVQL`, `SoVBTT`,"
                + " `NgayVBTT`, `CoQuanVBTT`, `TapTinVBTT`, `ThueDDTuNgay`, `ThueDDDenNgay`, "
                + "`NgayDuaVaoSuDung`, `DaKiemDinh`, `DaCapGiayCN`, `SoGiayCN`, `TapTinKD`,"
                + " `DaKiemTra`, `NgayKT`, `TapTinKT`, `GhiChu`, `IsActive`, "
                + "`CreatedDate`, `CreatedByUserID`, `ModifiedByUserID`, `ModifiedDate`) "
                + "VALUES ("
                + ":TenTram,  :NgayLapDat,  :DiaChiLapDat,  :TinhThanhLD,  "
                + ":QuanHuyenLD,  :PhuongXaLD,  :TrangThai,  :TinhTrang,  :DonViThue, "
                + " :ToaDoKD,  :ToaDoVD,  :LoaiTram,  :LoaiCotAT,  :ChieuCao, "
                + " :DTSanLap,  :TenDVQL,  :DiaChiDVQL,  :DienThoaiDVQL,  :SoVBTT, "
                + " :NgayVBTT,  :CoQuanVBTT,  :TapTinVBTT,  :ThueDDTuNgay,  :ThueDDDenNgay,  "
                + ":NgayDuaVaoSuDung,  :DaKiemDinh,  :DaCapGiayCN,  :SoGiayCN,  :TapTinKD, "
                + " :DaKiemTra,  :NgayKT,  :TapTinKT,  :GhiChu,  :IsActive,  "
                + ":CreatedDate,  :CreatedByUserID,  :ModifiedByUserID,  :ModifiedDate"
                + ") ";
        int row = 0;
        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString("TenTram", tramBTSEntity.TenTram);
            statement.setTimestamp("NgayLapDat", new Timestamp(tramBTSEntity.NgayLapDat.getTime()));
            statement.setString("DiaChiLapDat", tramBTSEntity.DiaChiLapDat);
            statement.setInt("TinhThanhLD", tramBTSEntity.TinhThanhLD);
            statement.setInt("QuanHuyenLD", tramBTSEntity.QuanHuyenLD);
            statement.setInt("PhuongXaLD", tramBTSEntity.PhuongXaLD);
            statement.setInt("TrangThai", tramBTSEntity.TrangThai);
            statement.setInt("TinhTrang", tramBTSEntity.TinhTrang);
            statement.setString("DonViThue", tramBTSEntity.DonViThue);
            statement.setFloat("ToaDoKD", tramBTSEntity.ToaDoKD);
            statement.setFloat("ToaDoVD", tramBTSEntity.ToaDoVD);
            statement.setInt("LoaiTram", tramBTSEntity.LoaiTram);
            statement.setString("LoaiCotAT", tramBTSEntity.LoaiCotAT);
            statement.setFloat("ChieuCao", tramBTSEntity.ChieuCao);
            statement.setFloat("DTSanLap", tramBTSEntity.DTSanLap);
            statement.setString("TenDVQL", tramBTSEntity.TenDVQL);
            statement.setString("DiaChiDVQL", tramBTSEntity.DiaChiDVQL);
            statement.setString("DienThoaiDVQL", tramBTSEntity.DienThoaiDVQL);
            statement.setString("SoVBTT", tramBTSEntity.SoVBTT);
            statement.setTimestamp("NgayVBTT", new Timestamp(tramBTSEntity.NgayVBTT.getTime()));
            statement.setString("CoQuanVBTT", tramBTSEntity.CoQuanVBTT);
            statement.setString("TapTinVBTT", tramBTSEntity.TapTinVBTT);
            statement.setTimestamp("ThueDDTuNgay", new Timestamp(tramBTSEntity.ThueDDTuNgay.getTime()));
            statement.setTimestamp("ThueDDDenNgay", new Timestamp(tramBTSEntity.ThueDDDenNgay.getTime()));
            statement.setTimestamp("NgayDuaVaoSuDung", new Timestamp(tramBTSEntity.NgayDuaVaoSuDung.getTime()));
            statement.setInt("DaKiemDinh", tramBTSEntity.DaKiemDinh ? 1 : 0);
            statement.setInt("DaCapGiayCN", tramBTSEntity.DaCapGiayCN ? 1 : 0);
            statement.setString("SoGiayCN", tramBTSEntity.SoGiayCN);
            statement.setString("TapTinKD", tramBTSEntity.TapTinKD);
            statement.setInt("DaKiemTra", tramBTSEntity.DaKiemTra ? 1 : 0);
            statement.setTimestamp("NgayKT", new Timestamp(tramBTSEntity.NgayKT.getTime()));
            statement.setString("TapTinKT", tramBTSEntity.TapTinKT);
            statement.setString("GhiChu", tramBTSEntity.GhiChu);
            statement.setInt("IsActive", tramBTSEntity.IsActive ? 1 : 0);
            statement.setTimestamp("CreatedDate", new Timestamp(tramBTSEntity.CreatedDate.getTime()));
            statement.setInt("CreatedByUserID", tramBTSEntity.CreatedByUserID);
            statement.setInt("ModifiedByUserID", tramBTSEntity.ModifiedByUserID);
            statement.setTimestamp("ModifiedDate", new Timestamp(tramBTSEntity.ModifiedDate.getTime()));
            logger.info(statement.toString());
            row = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public List<TramBTSEntity> getAllTramBTS() throws SQLException {
        List<TramBTSEntity> result = new ArrayList();
        String sql = "select  "
                + "`MaSo`, `TenTram`, `NgayLapDat`, `DiaChiLapDat`, `TinhThanhLD`, "
                + "`QuanHuyenLD`, `PhuongXaLD`, `TrangThai`, `DonViThue`,"
                + " `ToaDoKD`, `ToaDoVD`, `LoaiTram`, `LoaiCotAT`, `ChieuCao`,"
                + " `DTSanLap`, `DienThoaiDVQL`, "
                + " `ThueDDTuNgay`, `ThueDDDenNgay`, "
                + "`NgayDuaVaoSuDung`, "
                + " `DaKiemTra`,`GhiChu`,`TenDVQL` "
                + " from `thongtin_trambts` where IsActive=1";

        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            logger.info(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TramBTSEntity entity = new TramBTSEntity();
                entity.MaSo = resultSet.getInt("MaSo");
                entity.TenTram = resultSet.getString("TenTram");
                entity.NgayLapDat = resultSet.getTimestamp("NgayLapDat");
                entity.DiaChiLapDat = resultSet.getString("DiaChiLapDat");
                entity.TinhThanhLD = resultSet.getInt("TinhThanhLD");
                entity.QuanHuyenLD = resultSet.getInt("QuanHuyenLD");
                entity.PhuongXaLD = resultSet.getInt("PhuongXaLD");
                entity.TrangThai = resultSet.getInt("TrangThai");
                entity.DonViThue = resultSet.getString("DonViThue");
                entity.ToaDoKD = resultSet.getFloat("ToaDoKD");
                entity.ToaDoVD = resultSet.getFloat("ToaDoVD");
                entity.LoaiTram = resultSet.getInt("LoaiTram");
                entity.LoaiCotAT = resultSet.getString("LoaiCotAT");
                entity.ChieuCao = resultSet.getFloat("ChieuCao");
                entity.DTSanLap = resultSet.getFloat("DTSanLap");
                entity.ThueDDTuNgay = resultSet.getTimestamp("ThueDDTuNgay");
                entity.ThueDDDenNgay = resultSet.getTimestamp("ThueDDDenNgay");
                entity.NgayDuaVaoSuDung = resultSet.getTimestamp("NgayDuaVaoSuDung");
                entity.DaKiemTra = resultSet.getInt("DaKiemTra") == 1;
                entity.GhiChu = resultSet.getString("GhiChu");
                entity.TenDVQL = resultSet.getString("TenDVQL");
                result.add(entity);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            closeConnection();
        }
        return result;
    }

    public List<TuyenCapEntity> getAllTuyenCap() throws SQLException {
        List<TuyenCapEntity> result = new ArrayList();
        String sql = "select  "
                + " * "
                + " from `tuyencap`";

        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            logger.info(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TuyenCapEntity entity = new TuyenCapEntity();
                entity.Id = resultSet.getInt("Id");
                entity.Ten = resultSet.getString("Ten");
                entity.X1 = resultSet.getFloat("X1");
                entity.Y1 = resultSet.getFloat("Y1");
                entity.X2 = resultSet.getFloat("X2");
                entity.Y2 = resultSet.getFloat("Y2");
                result.add(entity);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            closeConnection();
        }
        return result;
    }

    public int updateTramBTS(TramBTSEntity tramBTSEntity) throws SQLException {
        int row = -1;
        try {
            getConnection();
            String sql = "update thongtin_trambts "
                    + "set TenTram=:TenTram,"
                    + "NgayLapDat=:NgayLapDat,DiaChiLapDat=:DiaChiLapDat, "
                    + "TinhThanhLD=:TinhThanhLD,QuanHuyenLD=:QuanHuyenLD, "
                    + "PhuongXaLD=:PhuongXaLD,TrangThai=:TrangThai, "
                    + "DonViThue=:DonViThue,TenDVQL=:TenDVQL, "
                    + "ToaDoKD=:ToaDoKD,ToaDoVD=:ToaDoVD,ChieuCao=:ChieuCao "
                    + "where MaSo=:MaSo";
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            statement.setInt("MaSo", tramBTSEntity.MaSo);
            statement.setString("TenTram", tramBTSEntity.TenTram);
            statement.setTimestamp("NgayLapDat", new Timestamp(tramBTSEntity.NgayLapDat.getTime()));
            statement.setString("DiaChiLapDat", tramBTSEntity.DiaChiLapDat);
            statement.setInt("TinhThanhLD", tramBTSEntity.TinhThanhLD);
            statement.setInt("QuanHuyenLD", tramBTSEntity.QuanHuyenLD);
            statement.setInt("PhuongXaLD", tramBTSEntity.PhuongXaLD);
            statement.setInt("TrangThai", tramBTSEntity.TrangThai);
            statement.setString("DonViThue", tramBTSEntity.DonViThue);
            statement.setString("TenDVQL", tramBTSEntity.TenDVQL);
            statement.setFloat("ToaDoVD", tramBTSEntity.ToaDoVD);
            statement.setFloat("ToaDoKD", tramBTSEntity.ToaDoKD);
            statement.setFloat("ChieuCao", tramBTSEntity.ChieuCao);
            logger.info(statement.toString());
            row = statement.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            logger.error(cnfex.getMessage(), cnfex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int deleteTramBTS(int MaSo) throws SQLException {
        int row = -1;
        try {
            getConnection();
            String sql = "delete from thongtin_trambts "
                    + "where MaSo=:MaSo";
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            statement.setInt("MaSo", MaSo);
            logger.info(statement.toString());
            row = statement.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            logger.error(cnfex.getMessage(), cnfex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int insertTuyenCap(TuyenCapEntity tuyencapEntity) throws SQLException {
        String sql = "INSERT INTO `tuyencap` ("
                + " `Ten`, `X1`, `Y1`, `X2`,`Y2` "
                + ") "
                + "VALUES ("
                + ":Ten,  :X1,  :Y1,  :X2,  :Y2"
                + ") ";
        int row = 0;
        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString("Ten", tuyencapEntity.Ten);
            statement.setFloat("X1", tuyencapEntity.X1);
            statement.setFloat("Y1", tuyencapEntity.Y1);
            statement.setFloat("X2", tuyencapEntity.X2);
            statement.setFloat("Y2", tuyencapEntity.Y2);
            logger.info(statement.toString());
            row = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int updateTuyenCap(TuyenCapEntity tuyencapEntity) throws SQLException {
        int row = -1;
        try {
            getConnection();
            String sql = "update tuyencap "
                    + "set Ten=:Ten,"
                    + "X1=:X1,Y1=:Y1, "
                    + "X2=:X2,Y2=:Y2 "
                    + "where Id=:Id";
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            statement.setInt("Id", tuyencapEntity.Id);
            statement.setString("Ten", tuyencapEntity.Ten);
            statement.setFloat("X1", tuyencapEntity.X1);
            statement.setFloat("Y1", tuyencapEntity.Y1);
            statement.setFloat("X2", tuyencapEntity.X2);
            statement.setFloat("Y2", tuyencapEntity.Y2);
            logger.info(statement.toString());
            row = statement.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            logger.error(cnfex.getMessage(), cnfex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int deleteTuyenCap(int Id) throws SQLException {
        int row = -1;
        try {
            getConnection();
            String sql = "delete from tuyencap "
                    + "where Id=:Id";
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            statement.setInt("Id", Id);
            logger.info(statement.toString());
            row = statement.executeUpdate();
        } catch (ClassNotFoundException cnfex) {
            logger.error(cnfex.getMessage(), cnfex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int insertTramThuyVan(TramThuyVanEntity tramThuyVanEntity) throws SQLException {
        String sql = "INSERT INTO `tramthuyvan` ("
                + " `tentram`, `x`, `y`, `vitri`,`hinh` "
                + ") "
                + "VALUES ("
                + ":tentram,  :x,  :y,  :vitri,  :hinh"
                + ") ";
        int row = 0;
        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString("tentram", tramThuyVanEntity.tentram);
            statement.setFloat("x", tramThuyVanEntity.x);
            statement.setFloat("y", tramThuyVanEntity.y);
            statement.setString("vitri", tramThuyVanEntity.vitri);
            statement.setString("hinh", tramThuyVanEntity.hinh);
            logger.info(statement.toString());
            row = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            closeConnection();
        }
        return row;
    }

    public int insertSoLieuDo(SoLieuDoEntity soLieuDoEntity) throws SQLException {
        String sql = "INSERT INTO `solieudo` ("
                + " `matram`, `ngayquantrac`, `giatricaonhat`, `giatrithapnhat` "
                + ") "
                + "VALUES ("
                + ":matram,  :ngayquantrac,  :giatricaonhat,  :giatrithapnhat"
                + ") ";
        int row = 0;
        try {
            getConnection();
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt("matram", soLieuDoEntity.matram);
            statement.setTimestamp("ngayquantrac", new Timestamp(soLieuDoEntity.ngayquantrac.getTime()));
            statement.setFloat("giatricaonhat", soLieuDoEntity.giatricaonhat);
            statement.setFloat("giatrithapnhat", soLieuDoEntity.giatrithapnhat);
            logger.info(statement.toString());
            row = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            closeConnection();
        }
        return row;
    }

    public List<TramThuyVanEntity> getTramThuyVanList() throws SQLException {
        List<TramThuyVanEntity> result = new ArrayList();
        try {
            getConnection();
            String sql = "select *from tramthuyvan";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                TramThuyVanEntity tramThuyVanEntity = new TramThuyVanEntity();
                tramThuyVanEntity.id = resultSet.getInt("id");
                tramThuyVanEntity.tentram = resultSet.getString("tentram");
                tramThuyVanEntity.x = resultSet.getFloat("x");
                tramThuyVanEntity.y = resultSet.getFloat("y");
                tramThuyVanEntity.vitri = resultSet.getString("vitri");
                tramThuyVanEntity.hinh = resultSet.getString("hinh");
                result.add(tramThuyVanEntity);
            }
        } catch (ClassNotFoundException cnfex) {
            logger.error(cnfex.getMessage(), cnfex);
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<SoLieuDoEntity> getSoLieuDoThuyVan(int nam, int thang, int matram) {
        List<SoLieuDoEntity> result = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConfig.dateFormat);
        try {
            getConnection();
            String sql = "select *from solieudo where matram=:matram and YEAR(ngayquantrac)=:nam and MONTH(ngayquantrac)=:thang";
            NamedParameterStatement statement = new NamedParameterStatement(conn, sql);
            
            statement.setInt("matram", matram);
            statement.setInt("nam", nam);
            statement.setInt("thang", thang);
            logger.info(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SoLieuDoEntity soLieuDoEntity = new SoLieuDoEntity();
                soLieuDoEntity.matram = matram;
                soLieuDoEntity.ngayquantrac = resultSet.getDate("ngayquantrac");
                soLieuDoEntity.giatricaonhat = resultSet.getFloat("giatricaonhat");
                soLieuDoEntity.giatrithapnhat = resultSet.getFloat("giatrithapnhat");
                soLieuDoEntity.ddmmyyyy = simpleDateFormat.format(soLieuDoEntity.ngayquantrac);
                result.add(soLieuDoEntity);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            closeConnection();
        }
        return result;
    }
}
