package com.enation.app.javashop.framework.database.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * map mapper
 * @author kingapex
 * @version v1.0
 * @since v7.0.0
 * 2018年3月23日 上午10:26:41
 */
public class FilterColumnMapRowMapper extends ColumnMapRowMapper {
	private IRowMapperColumnFilter filter;
	public FilterColumnMapRowMapper(IRowMapperColumnFilter filter){
		this.filter = filter;
	}
	
	@Override
	public  Map<String, Object>  mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map mapOfColValues = createColumnMap(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			key  = key.toLowerCase();
			Object obj = getColumnValue(rs, i);
			mapOfColValues.put(key, obj);
			//对此行结果集进行过滤
			this.filter.filter(mapOfColValues, rs);
		}
		return mapOfColValues;
	}
}
