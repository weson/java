package com.weson.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

	private static final String DBDRIVER = "com.mysql.jdbc.Driver";// 驱动类类名

	private static final String DBNAME = "wjj_backup";// 数据库名

	private static final String DBURL = "jdbc:mysql://42.121.122.150:3306/" + DBNAME;// 连接URL

	private static final String DBUSER = "dbuser";// 数据库用户名

	private static final String DBPASSWORD = "1qazXSW@";// 数据库密码

	private static Connection conn = null;

	private static PreparedStatement ps = null;

	private static ResultSet rs = null;

	// 获取数据库连接

	public static Connection getConnection() {
		try {
			Class.forName(DBDRIVER);// 注册驱动
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);// 获得连接对象
		} catch (ClassNotFoundException e) {// 捕获驱动类无法找到异常
			e.printStackTrace();
		} catch (SQLException e) {// 捕获SQL异常
			e.printStackTrace();
		}
		return conn;
	}

	// 查询数据

	public ResultSet select(String sql) throws Exception {
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery(sql);
			return rs;
		} catch (SQLException sqle) {
			throw new SQLException("select data Exception: " + sqle.getMessage());
		} catch (Exception e) {
			throw new Exception("System error: " + e.getMessage());
		}
	}
	// 插入数据

	public int insert(String sql) throws Exception {
		int num = 0;// 计数
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new SQLException("insert data Exception: " + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close exception: " + e.getMessage());
			}
		}
		return num;
	}
	// 删除数据
	public int delete(String sql) throws Exception {
		int num = 0;// 计数
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new SQLException("delete data Exception: " + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close Exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close Exception: " + e.getMessage());
			}
		}
		return num;
	}

	// 修改数据

	public int update(String sql) throws Exception {
		int num = 0;// 计数
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			num = ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new SQLException("update data Exception: " + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close Exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close Excepiton: " + e.getMessage());
			}
		}
		return num;
	}
	
	public static void closeConnection(Connection conn) throws Exception{
		if(conn != null){
			conn.close();//放回连接池
		}
	}	
}