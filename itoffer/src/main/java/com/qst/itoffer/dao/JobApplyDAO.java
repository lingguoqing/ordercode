package com.qst.itoffer.dao;

import com.qst.itoffer.bean.Applicant;
import com.qst.itoffer.bean.Job;
import com.qst.itoffer.bean.JobApply;
import com.qst.itoffer.bean.ResumeBasicinfo;
import com.qst.itoffer.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JobApplyDAO {
    public List<JobApply> query(String companyId, String jobId,
                                String startDate, String endDate) {
        List<JobApply> list = new ArrayList<JobApply>();
        Connection conn = DBUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT a.apply_id,a.apply_state,a.apply_date,j.job_id,j.job_name,c.applicant_id,d.basicinfo_id,d.realname FROM tb_jobapply a INNER JOIN tb_job j on a.job_id=j.job_id INNER JOIN tb_applicant c on a.applicant_id=c.applicant_id INNER JOIN tb_resume_basicinfo d on c.applicant_id=d.applicant_id WHERE 1=1 ");
        try {
            stmt = conn.createStatement();
            int cid = Integer.parseInt(companyId == null ? "0" : companyId);
            int jid = Integer.parseInt(jobId == null ? "0" : jobId);
            if (cid != 0)
                sql.append(" and j.company_id = " + cid);
            if (jid != 0)
                sql.append(" and a.job_id = " + jid);
            if (!"".equals(startDate))
                sql.append(" and a.apply_date >= '" + startDate + "'");
            if (!"".equals(endDate))
                sql.equals(" and a.apply_date <= '" + endDate + "'");
            rs = stmt.executeQuery(sql.toString());
            System.out.println(sql.toString());
            while (rs.next()) {
                //姓名、申请职位、申请状态、申请日期
                JobApply ja = new JobApply();
                ja.setApplyId(rs.getInt(1));
                ja.setApplyState(rs.getInt(2));
                ja.setApplyDate(rs.getTimestamp(3));
                Job job = new Job();
                job.setJobId(rs.getInt(4));
                job.setJobName(rs.getString(5));
                Applicant applicant = new Applicant();
                applicant.setApplicantId(rs.getInt(6));
                ResumeBasicinfo resume = new ResumeBasicinfo();
                resume.setBasicinfoId(rs.getInt(7));
                resume.setRealName(rs.getString(8));
                applicant.setResume(resume);
                ja.setJob(job);
                ja.setApplicant(applicant);
                list.add(ja);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeJDBC(rs, stmt, conn);
        }
        return list;
    }
}