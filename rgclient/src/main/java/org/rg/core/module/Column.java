package org.rg.core.module;

/**
 * Created by jiang on 9/30/16.
 */
public class Column {
    private int columnDisplaySize;   //列的最大长度
    private String columnName;
    private int precision;   //For numeric data, this is the maximum precision.  For character data, this is the length in characters
    private int scale;   //保留多少位
    private int columnType;   //列类型
    private Class clazz;  //列类型
    private String columnTypeName;  //列类型名
    private boolean isPk;  //是否为主键


    public boolean isPk() {
        return isPk;
    }

    public void setPk(boolean pk) {
        isPk = pk;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String toString() {
        return this.columnName + " " + this.columnTypeName + " " + this.isPk;
    }
}
