package com.j256.ormlite.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseResults;

/**
 * Android implementation of the compiled statement.
 * 
 * @author kevingalligan
 */
public class AndroidCompiledStatement implements CompiledStatement {

	private final String sql;
	private final SQLiteDatabase db;

	private Cursor cursor;
	private final List<Object> args = new ArrayList<Object>();
	private Integer max;

	public AndroidCompiledStatement(String sql, SQLiteDatabase db) {
		this.sql = sql;
		this.db = db;
	}

	public int getColumnCount() throws SQLException {
		return getCursor().getColumnCount();
	}

	public String getColumnName(int column) throws SQLException {
		return getCursor().getColumnName(AndroidDatabaseResults.jdbcColumnIndexToAndroid(column));
	}

	public DatabaseResults executeQuery() throws SQLException {
		return new AndroidDatabaseResults(getCursor());
	}

	public int executeUpdate() throws SQLException {
		String finalSql = null;
		try {
			if (max == null) {
				finalSql = sql;
			} else {
				finalSql = sql + " " + max;
			}
			db.execSQL(finalSql, args.toArray(new Object[args.size()]));
		} catch (Exception e) {
			throw SqlExceptionUtil.create("Problems executing Android query: " + finalSql, e);
		}
		return 1;
	}

	public DatabaseResults getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException("Unsupported operation to getGeneratedKeys");
	}

	public void close() throws SQLException {
		if (cursor != null) {
			cursor.close();
		}
	}

	public void setNull(int parameterIndex, SqlType sqlType) throws SQLException {
		isInPrep();
		args.add(AndroidDatabaseResults.jdbcColumnIndexToAndroid(parameterIndex), null);
	}

	public void setObject(int parameterIndex, Object obj, SqlType sqlType) throws SQLException {
		isInPrep();
		args.add(AndroidDatabaseResults.jdbcColumnIndexToAndroid(parameterIndex), obj.toString());
	}

	public void setMaxRows(int max) throws SQLException {
		isInPrep();
		this.max = max;
	}

	/**
	 * Not thread safe. Not sure if we need it, but keep that in mind.
	 */
	private Cursor getCursor() throws SQLException {
		String finalSql = null;
		if (cursor == null) {
			try {
				if (max == null) {
					finalSql = sql;
				} else {
					finalSql = sql + " " + max;
				}
				cursor = db.rawQuery(finalSql, args.toArray(new String[args.size()]));
				cursor.moveToFirst();
			} catch (Exception e) {
				throw SqlExceptionUtil.create("Problems executing Android query: " + finalSql, e);
			}
		}

		return cursor;
	}

	private void isInPrep() throws SQLException {
		if (cursor != null) {
			throw new SQLException("Query already run. Cannot add argument values.");
		}
	}
}
