package org.smart4j.framework.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.util.CollectionUtil;

/**
 * 数据库操作助手类
 * @author 鑫哲
 *
 */
public class DatabaseHelper {
	
	private DatabaseHelper() {
		// TODO
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
	
	private static final ThreadLocal<Connection> CONNECTION_HOLDER;
	private static final QueryRunner QUERY_RUNNER;
	private static final BasicDataSource DATA_SOURCE;
	
	static{
		CONNECTION_HOLDER = new ThreadLocal<Connection>();
		QUERY_RUNNER = new QueryRunner();
		
		String driver = ConfigHelper.getJdbcDriver();
		String url = ConfigHelper.getJdbcUrl();
		String username = ConfigHelper.getJdbcUsername();
		String password = ConfigHelper.getJdbcPassword();
		
		DATA_SOURCE = new BasicDataSource();
		DATA_SOURCE.setDriverClassName(driver);
		DATA_SOURCE.setUrl(url);
		DATA_SOURCE.setUsername(username);
		DATA_SOURCE.setPassword(password);
	}
	
	/**
	 * 查询实体列表
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return List<T>
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> entityList;
		try {
			Connection conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("query entity list failure", e);
			throw new RuntimeException(e);
		}
		return entityList;
	}
	
	/**
	 * 查询实体
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return T
	 */
	public static <T> T getEntity(Class<T> entityClass, String sql, Object... params) {
		T entity;
		try {
			Connection conn = getConnection();
			entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("query entity failure", e);
			throw new RuntimeException(e);
		}
		return entity;
	}
	
	/**
	 * 执行查询语句
	 * @param sql
	 * @param params
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
		List<Map<String, Object>> result;
		try {
			Connection conn = getConnection();
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			LOGGER.error("execute query failure", e);
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 执行修改语句(delete, update, insert)
	 * @param sql
	 * @param params
	 * @return int
	 */
	public static int executeEdit(String sql, Object... params) {
		int rows;
		try {
			Connection conn = getConnection();
			rows = QUERY_RUNNER.update(conn, sql, params);
		} catch (SQLException e) {
			LOGGER.error("execute edit failure", e);
			throw new RuntimeException(e);
		}
		return rows;
	}
	
	/**
	 * 插入实体
	 * @param entityClass
	 * @param fieldMap
	 * @return boolean
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not insert entity: fieldMap is empty");
			return false;
		}
		
		String sql = "INSERT INTO " + entityClass.getSimpleName();
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
		values.replace(values.lastIndexOf(", "), values.length(), ")");
		sql += columns + "VALUES" + values;
		
		Object[] params = fieldMap.values().toArray();
		
		return executeEdit(sql, params) == 1;
	}
	
	/**
	 * 更新实体
	 * @param entityClass
	 * @param id
	 * @param fieldMap
	 * @return boolean
	 */
	public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String ,Object> fieldMap) {
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not update entity: fieldMap is empty");
			return false;
		}
		
		String sql = "UPDATE " + entityClass.getSimpleName() + " SET ";
		StringBuilder columns = new StringBuilder();
		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append("=?, ");
		}
		sql += columns.substring(0, columns.lastIndexOf(", ")) + "WHERE id =?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params = paramList.toArray();
		return executeEdit(sql, params) == 1;
	}
	
	/**
	 * 删除实体
	 * @param entityClass
	 * @param id
	 * @return boolean
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
		String sql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE id = ?";
		return executeEdit(sql, id) == 1;
	}
	
	/**
	 * 获取数据库连接(线程安全)
	 */
	public static Connection getConnection() {
		Connection conn = CONNECTION_HOLDER.get();
		if (conn == null) {
			try {
				conn = DATA_SOURCE.getConnection();
			} catch (SQLException e) {
				LOGGER.error("get connection failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
		return conn;
	}
	
	/**
	 * 开启事物
	 */
	public static void beginTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				LOGGER.error("begin transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
	}
	
	/**
	 * 提交事务
	 */
	public static void commitTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException e) {
				LOGGER.error("commit transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}
	
	/**
	 * 回滚事物
	 */
	public static void rollbackTransaction() {
		Connection conn = getConnection();
		try {
			conn.rollback();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("rollback transaction failure", e);
			throw new RuntimeException(e);
		}
	}
	
}
