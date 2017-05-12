package org.rg.core.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.rg.core.module.Column;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by jiang on 9/30/16.
 */
public class DBFactory {

    private DruidDataSource druidDataSource = null;
    public static DBFactory builder(Properties params){
        return new DBFactory(params);
    }

    public DBFactory(){}

    private DBFactory(Properties params) {
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection createConnection() throws Exception {
        DruidPooledConnection pooledConnection = druidDataSource.getConnection();
        return pooledConnection.getConnection();
    }

    public List<String> listTables(Connection connection) {
        List<String> tables = new ArrayList<String>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(connection.getCatalog(), databaseMetaData.getUserName(), null, new String[]{"TABLE"});
            while(resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Column> getTableInfo(Connection connection, String tableName) {
        PreparedStatement preparedStatement = null;
        List<Column> columns = new ArrayList<Column>();
        try {
            String sql = "desc " + tableName;
            preparedStatement = connection.prepareStatement(sql);
            DatabaseMetaData dbMeta = connection.getMetaData();
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
            ResultSet pkRs = dbMeta.getPrimaryKeys(null,null,tableName);
            String pkName = "";
            while(pkRs.next()) {
                String objPkName = pkRs.getString("COLUMN_NAME");
                if(objPkName != null) {
                    pkName = objPkName;
                }
            }
            pkRs.close();

            ResultSet colRet = dbMeta.getColumns(null,"%", tableName,"%");
            while(colRet.next()) {
                Column column = new Column();
                String columnName = colRet.getString("COLUMN_NAME");
                String columnTypeName = colRet.getString("TYPE_NAME");
                int datasize = colRet.getInt("COLUMN_SIZE");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int nullable = colRet.getInt("NULLABLE");
                if(columnName.equals(pkName)) {
                    column.setPk(true);
                }

                column.setColumnDisplaySize(datasize);
                column.setColumnName(columnName);
//                column.setColumnType(columnType);
                column.setClazz(getFieldType(columnTypeName));
//                column.setPrecision(precision);
                column.setScale(digits);
                columns.add(column);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return columns;
        }

    }

    public String parseColumnName(String name) {
        String[] names =name.split("_");
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(String cn : names) {
            if(index == 0) {
                sb.append(cn);
            } else {
                sb.append(Character.toUpperCase(cn.charAt(0)));
                if(cn.length() > 1) {
                    sb.append(cn.substring(1));
                }
            }
            index++;
        }
        return sb.toString();
    }

    public String generateName(String data) {
        String[] names =data.split("_");
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(String cn : names) {

            sb.append(Character.toUpperCase(cn.charAt(0)));
            if(cn.length() > 1) {
                sb.append(cn.substring(1));
            }

        }
        return sb.toString();
    }

    private Class getFieldType(String columnType) {
        /*
         * tinyblob tinyblob byte[]
            tinytext varchar java.lang.string
            blob blob byte[]
            text varchar java.lang.string
            mediumblob mediumblob byte[]
            mediumtext varchar java.lang.string
            longblob longblob byte[]
            longtext varchar java.lang.string
            enum('value1','value2',...) char java.lang.string
            set('value1','value2',...) char java.lang.string
         */

        columnType = columnType.toLowerCase();
        columnType = columnType.replace("unsigned","").trim();
        if (columnType.equals("varchar") || columnType.equals("nvarchar")
                || columnType.equals("char")
                || columnType.equals("tinytext")
                || columnType.equals("text")
                || columnType.equals("mediumtext")
                || columnType.equals("longtext")
                ) {
            return String.class;
        } else if (columnType.equals("tinyblob")
                ||columnType.equals("blob")
                ||columnType.equals("mediumblob")
                ||columnType.equals("longblob")) {
            return Byte[].class;
        } else if (columnType.equals("datetime")
                ||columnType.equals("date")
                ||columnType.equals("timestamp")
                ||columnType.equals("time")
                ||columnType.equals("year")) {
            return Date.class;
        } else if (columnType.equals("bit")
                ||columnType.equals("int")
                ||columnType.equals("tinyint")
                ||columnType.equals("smallint")
                ) {
            return Integer.class;
        } else if (columnType.equals("float")) {
            return Float.class;
        } else if (columnType.equals("double")) {
            return Double.class;
        } else if (columnType.equals("decimal")) {
            return BigDecimal.class;
        } else if(columnType.equals("bigint")) {
            return Long.class;
        }
        return null;
    }

    public Map<String, String> getFieldsColumn(List<Column> columns) {
        Map<String, String> fieldToColumn = new HashMap<String, String>();
        for(Column column : columns) {
            String name = column.getColumnName();
            String filedName = parseColumnName(name);
            fieldToColumn.put(filedName, name);
        }
        return fieldToColumn;
    }

    public  Map<String, Class> getFieldsClass(List<Column> columns) {
        Map<String, Class> fields = new HashMap<String, Class>();

        for(Column column : columns) {
            String name = column.getColumnName();
            String filedName = parseColumnName(name);
            fields.put(filedName, column.getClazz());
        }

        return fields;
    }
}
