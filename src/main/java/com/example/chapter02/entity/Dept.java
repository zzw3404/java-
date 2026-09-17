package com.example.chapter02.entity;

import java.io.Serializable;
import java.util.List;

/** 部门实体，对应 dept 表。 */
public class Dept implements Serializable {

    private Integer deptno;
    private String dname;
    private String loc;
    private Integer deleted;
    private List<Emp> emps;

    public Dept() {
    }

    public Dept(Integer deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }

    public Integer getDeptno() { return deptno; }
    public void setDeptno(Integer deptno) { this.deptno = deptno; }
    public String getDname() { return dname; }
    public void setDname(String dname) { this.dname = dname; }
    public String getLoc() { return loc; }
    public void setLoc(String loc) { this.loc = loc; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public List<Emp> getEmps() { return emps; }
    public void setEmps(List<Emp> emps) { this.emps = emps; }

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                ", emps=" + (emps == null ? 0 : emps.size()) +
                '}';
    }
}
