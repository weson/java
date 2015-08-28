package com.weson.util.db;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class MyUtil {

	private static Connection connection = null;

	private MyUtil() {
	}

	/**
	 * return DataSource
	 * 
	 * @throws Exception
	 */
	public static DataSource getDataSourceFromPro() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Properties prop = new Properties();
		InputStream is = MyUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
		prop.load(is);
		return BasicDataSourceFactory.createDataSource(prop);
	}

	/**
	 * return Connection from properties
	 * 
	 * @throws Exception
	 */
	public static Connection getConnectionFromPro() throws Exception {
		return getDataSourceFromPro().getConnection();
	}

	/**
	 * return Statement
	 * 
	 * @throws Exception
	 */
	public static Statement getStatementFromPro() throws Exception {
		return getConnectionFromPro().createStatement();
	}

	/**
	 * return PreparedStatement
	 * 
	 * @throws Exception
	 */
	public static PreparedStatement getPreStatementFromPro(String sql) throws

	Exception {
		return getConnectionFromPro().prepareStatement(sql);
	}

	/**
	 * return Query ResultSet
	 * 
	 * @throws Exception
	 */
	public static ResultSet getResultSetFromPro(String QuerySql) throws

	Exception {
		return getStatementFromPro().executeQuery(QuerySql);
	}

	/**
	 * return ResultSet List
	 */
	public List<Object> getResultList(ResultSet res) {
		List<Object> list = new ArrayList<Object>();
		try {
			ResultSetMetaData md = res.getMetaData();
			int columnCount = md.getColumnCount();
			while (res.next()) {
				Map<Object, Object> rowData = new HashMap<Object,

				Object>();
				for (int i = 1; i <= columnCount; i++)
					rowData.put(md.getColumnName(i),

					res.getObject(i));
				list.add(rowData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * return connection
	 * 
	 * 
	 * Oracle:"oracle.jdbc.dirver.OracleDriver",
	 * "jdbc:oracle:thin:@localhost:1521:DBName"
	 * 
	 * 
	 * Sql:"com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:sqlserver://
	 * localhost:1433
	 * 
	 * ;databaseName=数据库名" MySQL:"com.mysql.jdbc.Driver",
	 * "jdbc:sybase:Tds:localhost:5007/erp " Sybase:"base.jdbc.SybDriver",
	 * "jdbc:sybase:Tds:localhost:5007/erp "
	 * DB2:"com.ibm.db2.app.DB2Driver","jdbc:db2//localhost:5000/testDB"
	 * Hsql:"org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost:9902"
	 * 
	 * 
	 * Informix:"com.informix.jdbc.IfxDriver","jdbc:informixsqli://123.45.67.89:
	 * 1533/myDB:
	 * 
	 * INFORMIXSERVER=myserver"
	 * PostgreSQL:"org.postgresql.Driver","jdbc:postgresql://localhost/testDB"
	 */
	public static Connection getConnection(String driverName, String

	dbUrl, String username, String password) {
		try {
			Class.forName(driverName);
			Properties properties = new Properties();
			properties.put("username", username);
			properties.put("password", password);
			connection = DriverManager.getConnection(dbUrl, properties);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * return ResultSet
	 * 
	 * TABLE_CAT String => 表类别（可为 null） TABLE_SCHEM String => 表模式（可为 null）
	 * TABLE_NAME String => 表名称 TABLE_TYPE String => 表类型。 REMARKS String =>
	 * 表的解释性注释 TYPE_CAT String => 类型的类别（可为 null） TYPE_SCHEM String => 类型模式（可为
	 * null） TYPE_NAME String => 类型名称（可为 null） SELF_REFERENCING_COL_NAME String
	 * => 有类型表的指定 "identifier" 列的名称（
	 * 
	 * 可为 null） REF_GENERATION String SELF_REFERENCING_COL_NAME String => name
	 * of the designated "identifier"
	 * 
	 * column of a typed table (may be null) REF_GENERATION String => specifies
	 * how values in
	 * 
	 * SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
	 * "DERIVED". (may
	 * 
	 * be null)
	 */
	public static List<Object> getAllTablesInfo(Connection conn) {
		List<Object> list = new ArrayList<Object>();
		ResultSet resTable = null;
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();
			resTable = dbMetaData.getTables(conn.getCatalog(), null, "%",

			null);
			ResultSetMetaData rMetaData = resTable.getMetaData();
			int table_count = rMetaData.getColumnCount();
			while (resTable.next()) {
				Map<Object, Object> map = new HashMap<Object,

				Object>();
				for (int i = 1; i < table_count; i++)
					map.put(rMetaData.getColumnName(i),

					resTable.getString(i));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * return all tables name
	 */
	public static List<Object> getAllTablesNmae(Connection conn) {
		List<Object> list = new ArrayList<Object>();
		ResultSet resTable = null;
		try {
			DatabaseMetaData dbMetaData = conn.getMetaData();
			resTable = dbMetaData.getTables(conn.getCatalog(), null, "%",

			null);
			while (resTable.next()) {
				list.add(resTable.getString(3));
				/**
				 * 元数据的列分别为：TABLE_CAT、TABLE_SCHEM、
				 * 
				 * TABLE_NAME、TABLE_TYPE、REMARKS
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static <T> List<T> getResultList(String sql, Object[] params,

	Class<T> clazz) throws SQLException, Exception, IllegalAccessException, InvocationTargetException {
		Connection ct = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ct = getConnectionFromPro();
			ps = ct.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			rs = ps.executeQuery();
			String[] colNames = getColNames(rs);

			List<T> objects = new ArrayList<T>();
			Method[] methods = clazz.getMethods();
			while (rs.next()) {
				T object = clazz.newInstance();
				for (int i = 0; i < colNames.length; i++) {
					String colName = colNames[i];
					String methodName = "set" +

					colName;
					for (Method m : methods) {
						if (methodName.equals

						(m.getName())) {
							m.invoke(object,

							rs.getObject(colName));
							break;
						}
					}
					objects.add(object);
				}
			}
			return objects;
		} finally {
			close(rs, ps, ct);
		}
	}

	/**
	 * return all column name
	 */
	public static String[] getColNames(ResultSet res) throws

	SQLException {
		ResultSetMetaData rsmd = res.getMetaData();
		int count = rsmd.getColumnCount();
		String[] colNames = new String[count];
		for (int i = 1; i <= count; i++) {
			colNames[i - 1] = getUpper(rsmd.getColumnLabel(i));
		}
		return colNames;
	}

	private static String getUpper(String column) {
		return column.substring(0, 1).toUpperCase

		() + column.substring(1);
	}

	/**
	 * return rows number
	 */
	public static int getRowssNumber(ResultSet res) {
		int rows = 0;
		try {
			res.last();
			rows = res.getRow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 */
	public static <T> T getSingleResult(String sql, Object[] params,

	Class<T> clazz) throws SQLException, Exception, IllegalAccessException,

	InvocationTargetException {
		Connection ct = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ct = getConnectionFromPro();
			ps = ct.prepareStatement(sql);

			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}

			rs = ps.executeQuery();
			String[] colNames = getColNames(rs);

			T object = null;
			Method[] methods = clazz.getMethods();
			if (rs.next()) {
				object = clazz.newInstance();
				for (int i = 0; i < colNames.length; i++) {
					String colName = colNames[i];
					String methodName = "set" +

					colName;
					for (Method m : methods) {
						if (methodName.equals

						(m.getName())) {
							m.invoke(object,

							rs.getObject(colName));
							break;
						}
					}
				}
			}
			return object;
		} finally {
			close(rs, ps, ct);
		}
	}

	/**
	 * @param rs
	 * @param st
	 * @param ct
	 */
	public static void close(ResultSet res, Statement state, Connection

	connection) {
		try {
			if (res != null)
				res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (state != null)
					state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (connection != null)
					try {
						connection.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}

}
