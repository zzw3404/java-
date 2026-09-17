-- 第 2 章：部门与员工表
-- 用于 MyBatis 的 resultMap、association、collection 和多表查询练习。
-- 只重建 emp、dept 两张表，不会修改已有的 user 表。

CREATE DATABASE IF NOT EXISTS mybatis_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mybatis_db;

DROP TABLE IF EXISTS emp;
DROP TABLE IF EXISTS dept;

CREATE TABLE dept (
    deptno INT PRIMARY KEY AUTO_INCREMENT COMMENT '部门编号',
    dname VARCHAR(50) NOT NULL COMMENT '部门名称',
    loc VARCHAR(50) COMMENT '部门位置',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE emp (
    empno INT PRIMARY KEY AUTO_INCREMENT COMMENT '员工编号',
    ename VARCHAR(50) NOT NULL COMMENT '员工姓名',
    job VARCHAR(50) COMMENT '职位',
    mgr INT COMMENT '上级编号',
    hiredate DATE COMMENT '入职日期',
    sal DECIMAL(10,2) COMMENT '薪水',
    comm DECIMAL(10,2) COMMENT '佣金',
    deptno INT COMMENT '部门编号',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    CONSTRAINT fk_emp_dept FOREIGN KEY (deptno) REFERENCES dept(deptno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

INSERT INTO dept (dname, loc) VALUES
('ACCOUNTING', 'NEW YORK'),
('RESEARCH', 'DALLAS'),
('SALES', 'CHICAGO'),
('OPERATIONS', 'BOSTON');

INSERT INTO emp (ename, job, mgr, hiredate, sal, comm, deptno) VALUES
('SMITH', 'CLERK', 7902, '1980-12-17', 800.00, NULL, 2),
('ALLEN', 'SALESMAN', 7698, '1981-02-20', 1600.00, 300.00, 3),
('WARD', 'SALESMAN', 7698, '1981-02-22', 1250.00, 500.00, 3),
('JONES', 'MANAGER', 7839, '1981-04-02', 2975.00, NULL, 2),
('MARTIN', 'SALESMAN', 7698, '1981-09-28', 1250.00, 1400.00, 3),
('BLAKE', 'MANAGER', 7839, '1981-05-01', 2850.00, NULL, 3),
('CLARK', 'MANAGER', 7839, '1981-06-09', 2450.00, NULL, 1),
('SCOTT', 'ANALYST', 7566, '1987-04-19', 3000.00, NULL, 2),
('KING', 'PRESIDENT', NULL, '1981-11-17', 5000.00, NULL, 1),
('TURNER', 'SALESMAN', 7698, '1981-09-08', 1500.00, 0.00, 3),
('ADAMS', 'CLERK', 7788, '1987-05-23', 1100.00, NULL, 2),
('JAMES', 'CLERK', 7698, '1981-12-03', 950.00, NULL, 3),
('FORD', 'ANALYST', 7566, '1981-12-03', 3000.00, NULL, 2),
('MILLER', 'CLERK', 7782, '1982-01-23', 1300.00, NULL, 1);

SELECT * FROM dept ORDER BY deptno;
SELECT * FROM emp ORDER BY empno;
