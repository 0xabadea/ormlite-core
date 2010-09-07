package com.j256.ormlite.android.apptools;

import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Wraps the TableUtils object and hard codes the {@link SqliteAndroidDatabaseType} as a helper class.
 * 
 * @author kevingalligan
 */
public class AndroidTableUtils {

	static SqliteAndroidDatabaseType dbType = new SqliteAndroidDatabaseType();

	/**
	 * Create the table for the dataClass through the connectionSource.
	 */
	public static <T> int createTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
		return TableUtils.createTable(dbType, connectionSource, dataClass);
	}

	/**
	 * Create the table for the tableConfig through the connectionSource.
	 */
	public static <T> int createTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig)
			throws SQLException {
		return TableUtils.createTable(dbType, connectionSource, tableConfig);
	}

	/**
	 * Drop the table for the dataClass through the connectionSource. If ignoreErrors is false then any exceptions will
	 * be ignored.
	 */
	public static <T> int dropTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ignoreErrors)
			throws SQLException {
		return TableUtils.dropTable(dbType, connectionSource, dataClass, ignoreErrors);
	}

	/**
	 * Drop the table for the tableConfig through the connectionSource. If ignoreErrors is false then any exceptions
	 * will be ignored.
	 */
	public static <T> int dropTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig,
			boolean ignoreErrors) throws SQLException {
		return TableUtils.dropTable(dbType, connectionSource, tableConfig, ignoreErrors);
	}
}
