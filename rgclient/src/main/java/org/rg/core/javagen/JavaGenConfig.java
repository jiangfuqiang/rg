package org.rg.core.javagen;

import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 17/1/18.
 */
public class JavaGenConfig {

    private String packagePath;
    private List<String> imports;  //需要被导入的类
    private String path;
    private String fileName;
    private String fileSuffix;
    private int type;   //0:controller, 1:service 2:dao 3:inteface 4:mapper
    private Map<String, String> authowiredMap;   //需要被注解的类
    private String entityName;   //实体类的名字
    private String tableName;   //表名称
    private String tablePkName;  //表的主键
    private String entityPrimaryKeyName;  //表主键
    private String classNameTemplate;   //类名字   "Java{template}Dao"
    private List<String> descs;   //类描述
    private List<String> classAutowireds;  //类注解
    private List<String> implInterfaceNames;  //被实现的接口名
    private Map<String, String> fieldToColumn;   //域和列的对应关系

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, String> getAuthowiredMap() {
        return authowiredMap;
    }

    public void setAuthowiredMap(Map<String, String> authowiredMap) {
        this.authowiredMap = authowiredMap;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getClassNameTemplate() {
        return classNameTemplate;
    }

    public void setClassNameTemplate(String classNameTemplate) {
        this.classNameTemplate = classNameTemplate;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<String> getDescs() {
        return descs;
    }

    public void setDescs(List<String> descs) {
        this.descs = descs;
    }

    public List<String> getClassAutowireds() {
        return classAutowireds;
    }

    public void setClassAutowireds(List<String> classAutowireds) {
        this.classAutowireds = classAutowireds;
    }

    public List<String> getImplInterfaceNames() {
        return implInterfaceNames;
    }

    public void setImplInterfaceNames(List<String> implInterfaceNames) {
        this.implInterfaceNames = implInterfaceNames;
    }

    public String getEntityPrimaryKeyName() {
        return entityPrimaryKeyName;
    }

    public void setEntityPrimaryKeyName(String entityPrimaryKeyName) {
        this.entityPrimaryKeyName = entityPrimaryKeyName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTablePkName() {
        return tablePkName;
    }

    public void setTablePkName(String tablePkName) {
        this.tablePkName = tablePkName;
    }

    public Map<String, String> getFieldToColumn() {
        return fieldToColumn;
    }

    public void setFieldToColumn(Map<String, String> fieldToColumn) {
        this.fieldToColumn = fieldToColumn;
    }
}
