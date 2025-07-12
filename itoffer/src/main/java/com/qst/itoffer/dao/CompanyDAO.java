package com.qst.itoffer.dao;
import com.qst.itoffer.bean.Company;
import com.qst.itoffer.bean.Job;
import com.qst.itoffer.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    /**
     * 企业列表查询
     * @return
     */
    public List<Company> selectAll() {
        List<Company> list = new ArrayList<Company>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        String sql = "SELECT company_id,company_name,company_area,company_size, company_type,company_state,company_sort,company_viewnum "
            + "FROM tb_company ORDER BY company_id DESC";
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Company company = new Company();
                company.setCompanyId(rs.getInt(1));
                company.setCompanyName(rs.getString(2));
                company.setCompanyArea(rs.getString(3));
                company.setCompanySize(rs.getString(4));
                company.setCompanyType(rs.getString(5));
                company.setCompanyState(rs.getInt(6));
                company.setCompanySort(rs.getInt(7));
                company.setCompanyViewnum(rs.getInt(8));
                list.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 企业信息添加
     * @param company
     */
    public void save(Company company) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO tb_company("
                + "company_name,company_area,company_size,company_type,company_brief,company_state,company_sort,company_viewnum,company_pic"
                + ") VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getCompanyArea());
            pstmt.setString(3, company.getCompanySize());
            pstmt.setString(4, company.getCompanyType());
            pstmt.setString(5, company.getCompanyBrief());
            pstmt.setInt(6, company.getCompanyState());
            pstmt.setInt(7, company.getCompanySort());
            pstmt.setInt(8, company.getCompanyViewnum());
            pstmt.setString(9, company.getCompanyPic());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(null, pstmt, conn);
        }
    }

    /**
     * 根据企业标识查询企业详细信息
     * @param companyId
     * @return
     */
    public Company selectById(int companyId) {
        Company company = new Company();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM tb_company WHERE company_id = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                company.setCompanyId(rs.getInt(1));
                company.setCompanyName(rs.getString(2));
                company.setCompanyArea(rs.getString(3));
                company.setCompanySize(rs.getString(4));
                company.setCompanyType(rs.getString(5));
                company.setCompanyBrief(rs.getString(6));
                company.setCompanyState(rs.getInt(7));
                company.setCompanySort(rs.getInt(8));
                company.setCompanyViewnum(rs.getInt(9));
                company.setCompanyPic(rs.getString(10));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    /**
     * 查询所有企业的名称和标识
     * @return
     */
    public List<Company> selectAllCompanyName() {
        List<Company> list = new ArrayList<Company>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        String sql = "SELECT company_id,company_name FROM tb_company ORDER BY company_id DESC";
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Company company = new Company();
                company.setCompanyId(rs.getInt(1));
                company.setCompanyName(rs.getString(2));
                list.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Company company) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        String sql = "UPDATE tb_company "
                + "SET company_name=?,company_area=?,company_size=?,company_type=?,company_brief=?,company_state=?,company_sort=?,company_viewnum=?,company_pic=? "
                + "WHERE company_id=?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getCompanyArea());
            pstmt.setString(3, company.getCompanySize());
            pstmt.setString(4, company.getCompanyType());
            pstmt.setString(5, company.getCompanyBrief());
            pstmt.setInt(6, company.getCompanyState());
            pstmt.setInt(7, company.getCompanySort());
            pstmt.setInt(8, company.getCompanyViewnum());
            pstmt.setString(9, company.getCompanyPic());
            pstmt.setInt(10, company.getCompanyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(null, pstmt, conn);
        }
    }


    /**
     * 分页查询首页所需要的所有企业信息及职位信息
     * @return
     */
    public List<Company> getCompanyPageList(int pageNo,int pageSize) {
        // 定义本页记录索引值
        int firstIndex = pageSize * (pageNo-1);
        List<Company> list = new ArrayList<Company>();
        Connection connection = DBUtil.getConnection();
        if (connection == null)
            return null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection
                    .prepareStatement("SELECT tb_company.COMPANY_ID,tb_company.COMPANY_PIC,tb_job.JOB_ID,tb_job.JOB_NAME,job_salary,tb_job.JOB_AREA,tb_job.JOB_ENDTIME "
                            + "FROM tb_company LEFT JOIN tb_job ON tb_job.COMPANY_ID = tb_company.COMPANY_ID "
                            + "WHERE tb_company.COMPANY_STATE = 1  "
                            + "AND tb_job.JOB_ID IN (SELECT MAX(job_id) FROM tb_job WHERE job_state=1 GROUP BY company_id) limit ?,?");
            pstmt.setInt(1, firstIndex);
            pstmt.setInt(2, firstIndex+pageSize);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Company company = new Company();
                Job job = new Job();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyPic(rs.getString("company_pic"));
                job.setJobId(rs.getInt("job_id"));
                job.setJobName(rs.getString("job_name"));
                job.setJobSalary(rs.getString("job_salary"));
                job.setJobArea(rs.getString("job_area"));
                job.setJobEnddate(rs.getTimestamp("job_endtime"));
                company.getJobs().add(job);
                list.add(company);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, pstmt, connection);
        }
        return list;
    }

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
            String sql = "SELECT count(*) FROM tb_company "
                    + "LEFT OUTER JOIN tb_job ON tb_job.company_id=tb_company.company_id "
                    + "WHERE company_state=1 and job_id IN ("
                    + "SELECT MAX(job_id) FROM tb_job WHERE job_state=1 GROUP BY company_id"
                    + ")";
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
}