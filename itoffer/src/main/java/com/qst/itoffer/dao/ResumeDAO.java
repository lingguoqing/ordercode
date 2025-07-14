package com.qst.itoffer.dao;

import com.qst.itoffer.bean.ResumeBasicinfo;
import com.qst.itoffer.bean.User;
import com.qst.itoffer.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 简历信息数据库操作类
 * @公司 青软实训
 * @作者 fengjj 
 */
public class ResumeDAO {
    /**
     * 查询所需分页的总记录数
     * @param pageSize
     * @return
     */
    public int getRecordCount() {
        int recordCount = 0;
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT count(*) FROM tb_resume_basicinfo";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next())
                recordCount = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, conn);
        }
        return recordCount;
    }
    /**
     * 查询请求页的简历数据
     * @return
     */
    public List<ResumeBasicinfo> getPageList(int pageNo, int pageSize) {
        // 定义本页记录索引值
        int firstIndex = pageSize * (pageNo-1);
        List<ResumeBasicinfo> list = new ArrayList<ResumeBasicinfo>();
        Connection connection = DBUtil.getConnection();
        if (connection == null)
            return null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection
            .prepareStatement("SELECT basicinfo_id,realname,telephone,email, job_experience,job_intension "
                    + "FROM tb_resume_basicinfo LIMIT ?,? ");
            pstmt.setInt(1, firstIndex);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ResumeBasicinfo resume = new ResumeBasicinfo();
                resume.setBasicinfoId(rs.getInt("basicinfo_id"));
                resume.setRealName(rs.getString("realname"));
                resume.setTelephone(rs.getString("telephone"));
                resume.setEmail(rs.getString("email"));
                resume.setJobExperience(rs.getString("job_experience"));
                resume.setJobIntension(rs.getString("job_intension"));
                list.add(resume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, connection);
        }
        return list;
    }

    /**
     * 根据简历标识查询简历基本信息
     * @param applicantID
     * @return
     */
    public ResumeBasicinfo selectBasicinfoByID(int resumeId){
        ResumeBasicinfo resume = new ResumeBasicinfo();
        String sql = "SELECT * FROM tb_resume_basicinfo WHERE basicinfo_id=? ";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, resumeId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                resume.setBasicinfoId(rs.getInt("basicinfo_id"));
                resume.setBirthday(rs.getDate("birthday"));
                resume.setCurrentLoc(rs.getString("current_loc"));
                resume.setEmail(rs.getString("email"));
                resume.setGender(rs.getString("gender"));
                resume.setHeadShot(rs.getString("head_shot"));
                resume.setJobExperience(rs.getString("job_experience"));
                resume.setJobIntension(rs.getString("job_intension"));
                resume.setRealName(rs.getString("realname"));
                resume.setResidentLoc(rs.getString("resident_loc"));
                resume.setTelephone(rs.getString("telephone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(null, pstmt, conn);
        }
        return resume;
    }
}