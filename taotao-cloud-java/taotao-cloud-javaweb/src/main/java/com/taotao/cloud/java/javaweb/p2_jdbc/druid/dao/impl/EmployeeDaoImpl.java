package com.taotao.cloud.java.javaweb.p2_jdbc.druid.dao.impl;

import com.taotao.cloud.java.javaweb.p2_jdbc.druid.domain.Employee;
import com.taotao.cloud.java.javaweb.p2_jdbc.druid.utils.DBUtilDruid;
import com.taotao.cloud.java.javaweb.p2_jdbc.druid.dao.EmployeeDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author 千锋大数据教学团队
 * @Company 千锋好程序员大数据
 * @Description dao实现类
 */
public class EmployeeDaoImpl implements EmployeeDao {
	@Override
	public void addEmployee(Employee emp) throws SQLException {
		//Connection connection = DBUtilDruid.getConnection();
		String sql = "insert into emp values(?,?,?,?,?,?,?,?)";
		//PreparedStatement preparedStatement = connection.prepareStatement(sql);

		QueryRunner queryRunner = new QueryRunner();
		Object[] obj = new Object[]{emp.getEmpno(), emp.getEname(), emp.getJob(), emp.getMgr(), emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno()};
		queryRunner.update(DBUtilDruid.getConnection(), sql, obj);
                
        /*preparedStatement.setInt(1,emp.getEmpno());
        preparedStatement.setString(2,emp.getEname());
        preparedStatement.setString(3,emp.getJob());
        preparedStatement.setInt(4,emp.getMgr());
        preparedStatement.setDate(5,emp.getHiredate());
        preparedStatement.setDouble(6,emp.getSal());
        preparedStatement.setDouble(7,emp.getComm());
        preparedStatement.setInt(8,emp.getDeptno());
        preparedStatement.executeUpdate();*/
		//DBUtilDruid.close(null,preparedStatement,connection);
	}

	@Override
	public void delEmployee(int empno) throws SQLException {
		//Connection connection = DBUtilDruid.getConnection();
		String sql = "delete from emp where empno=?";
		//PreparedStatement preparedStatement = connection.prepareStatement(sql);
		//preparedStatement.setInt(1,empno);
		QueryRunner queryRunner = new QueryRunner();
		queryRunner.update(DBUtilDruid.getConnection(), sql, empno);
		//preparedStatement.executeUpdate();

		//DBUtilDruid.close(null,preparedStatement,connection);
	}

	@Override
	public void modifyEmployee(Employee emp) throws SQLException {
		//Connection connection = DBUtilDruid.getConnection();
		String sql = "update emp set ename=?,job=?,mgr=?,hiredate=?,sal=?,comm=?,deptno=? where empno=?";
		QueryRunner queryRunner = new QueryRunner();
		Object[] obj = new Object[]{emp.getEname(), emp.getJob(), emp.getMgr(), emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno(), emp.getEmpno()};
		queryRunner.update(DBUtilDruid.getConnection(), sql, obj);
       /* PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,emp.getEname());
        preparedStatement.setString(2,emp.getJob());
        preparedStatement.setInt(3,emp.getMgr());
        preparedStatement.setDate(4,emp.getHiredate());
        preparedStatement.setDouble(5,emp.getSal());
        preparedStatement.setDouble(6,emp.getComm());
        preparedStatement.setInt(7,emp.getDeptno());
        preparedStatement.setInt(8,emp.getEmpno());

        preparedStatement.executeUpdate();

        DBUtilDruid.close(null,preparedStatement,connection);*/
	}

	@Override
	public Employee findById(int empno) throws SQLException {
		//Connection connection = DBUtilDruid.getConnection();
		String sql = "select * from emp where empno=?";
		QueryRunner queryRunner = new QueryRunner();
		Employee emp = queryRunner.query(DBUtilDruid.getConnection(), sql, new BeanHandler<Employee>(Employee.class), empno);
        /*PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,empno);

        ResultSet resultSet = preparedStatement.executeQuery();
        Employee emp=null;
        if(resultSet.next()){
            emp=new Employee();
            emp.setEmpno(resultSet.getInt("empno"));
            emp.setEname(resultSet.getString("ename"));
            emp.setJob(resultSet.getString("job"));
            emp.setMgr(resultSet.getInt("mgr"));
            emp.setHiredate(resultSet.getDate("hiredate"));
            emp.setSal(resultSet.getDouble("sal"));
            emp.setComm(resultSet.getDouble("comm"));
            emp.setDeptno(resultSet.getInt("deptno"));
        }
        DBUtilDruid.close(resultSet,preparedStatement,connection);*/
		return emp;
	}

	@Override
	public List<Employee> findAll() throws SQLException {
		String sql = "select * from emp";
		QueryRunner queryRunner = new QueryRunner();
		List<Employee> emps = queryRunner.query(DBUtilDruid.getConnection(), sql, new BeanListHandler<Employee>(Employee.class));
        /*Connection connection = DBUtilDruid.getConnection();
        String sql="select * from emp";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        List<Employee> emps=null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet!=null){
            emps=new ArrayList<>();
            while(resultSet.next()){
                Employee emp=new Employee();
                emp.setEmpno(resultSet.getInt("empno"));
                emp.setEname(resultSet.getString("ename"));
                emp.setJob(resultSet.getString("job"));
                emp.setMgr(resultSet.getInt("mgr"));
                emp.setHiredate(resultSet.getDate("hiredate"));
                emp.setSal(resultSet.getDouble("sal"));
                emp.setComm(resultSet.getDouble("comm"));
                emp.setDeptno(resultSet.getInt("deptno"));

                emps.add(emp);
            }
        }
        DBUtilDruid.close(resultSet,preparedStatement,connection);*/
		return emps;
	}

	@Override
	public List<Employee> findByPage(int page, int pageSize) throws SQLException {
		String sql = "select * from emp limit ?,?";
		QueryRunner queryRunner = new QueryRunner();
		List<Employee> emps = queryRunner.query(DBUtilDruid.getConnection(), sql, new BeanListHandler<Employee>(Employee.class), (page - 1) * pageSize, pageSize);

        /*Connection connection = DBUtilDruid.getConnection();
        String sql="select * from emp limit ?,?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,(page-1)*pageSize);
        preparedStatement.setInt(2,pageSize);

        List<Employee> emps=null;

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet!=null){
            emps=new ArrayList<>();
            while(resultSet.next()){
                Employee emp=new Employee();
                emp.setEmpno(resultSet.getInt("empno"));
                emp.setEname(resultSet.getString("ename"));
                emp.setJob(resultSet.getString("job"));
                emp.setMgr(resultSet.getInt("mgr"));
                emp.setHiredate(resultSet.getDate("hiredate"));
                emp.setSal(resultSet.getDouble("sal"));
                emp.setComm(resultSet.getDouble("comm"));
                emp.setDeptno(resultSet.getInt("deptno"));

                emps.add(emp);
            }
        }
        DBUtilDruid.close(resultSet,preparedStatement,connection);*/
		return emps;
	}
}
