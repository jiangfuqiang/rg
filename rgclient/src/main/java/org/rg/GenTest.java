package org.rg;

import org.rg.core.db.DBFactory;
import org.rg.core.javagen.*;
import org.rg.core.module.Column;
import org.rg.core.xmlgen.ModuleMappingGen;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.*;

import org.rg.core.db.DBFactory;
import org.rg.core.javagen.*;
import org.rg.core.module.Column;
import org.rg.core.xmlgen.ModuleMappingGen;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by jiang on 17/1/18.
 */
public class GenTest {

    private static String path = "/Users/jiang/Documents/jiangblog";
    private static String packgePath = "com.dcs";
    private static String projectName = packgePath.substring(packgePath.lastIndexOf('.')+1);
    private static String prefixPackage = packgePath.substring(0, packgePath.lastIndexOf('.'));
    public static String confile = "druid.properties";
    public static Properties p = null;

    static {
        p = new Properties();
        InputStream inputStream = null;
        try {
            //java应用
            confile = GenTest.class.getClassLoader().getResource("").getPath()
                    + confile;
            System.out.println(confile);
            File file = new File(confile);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            p.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{

        DBFactory dbFactory1 = new DBFactory();
        DBFactory dbFactory = dbFactory1.builder(p);
        Connection connection = dbFactory.createConnection();
        List<String> tables = dbFactory.listTables(connection);
        System.out.println(tables.toString());
        for(String table : tables) {
            List<Column> columns = dbFactory.getTableInfo(connection, table);

            String pkId = "";
            for(Column column : columns) {
                if(column.isPk()) {
                    pkId = column.getColumnName();
                    break;
                }
            }

            Map<String, String> fieldToColumn = dbFactory.getFieldsColumn(columns);
            Map<String, Class> feidls = dbFactory.getFieldsClass(columns);
            String entityPkName = "";
            if(pkId.length() > 0) {
                entityPkName = dbFactory.parseColumnName(pkId);
            }
            String entityName = dbFactory.generateName(table);
//            System.out.println(fieldToColumn.toString());
            System.out.println(feidls.toString());
            testJavaEntity(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testModuleMapper(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testJavaMapper(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testJavaDao(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testJavaServiceInterface(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testJavaService(fieldToColumn, feidls,entityName,table,pkId, entityPkName);
            testJavaController(fieldToColumn, feidls,entityName,table,pkId, entityPkName);

//            packCode("/Users/jiang/Documents/jingblog.zip");
        }

    }

    public static void packCode(String storePath)throws IOException {
        GZIPOutputStream zipOutputStream = new GZIPOutputStream(new FileOutputStream(new File(storePath)));
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(new File(path)));
        byte[] buffer = new byte[1024];
        while(gzipInputStream.read(buffer) != -1) {
            zipOutputStream.write(buffer);
        }
        zipOutputStream.flush();
        zipOutputStream.close();
        gzipInputStream.close();
    }

    public static void testModuleMapper(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                        String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName+"";
        String fileSuffix = "xml";



        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setClassNameTemplate("{template}");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/mapping");
        javaGenConfig.setPackagePath(packgePath);
        javaGenConfig.setEntityName(entityName);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);
        javaGenConfig.setTableName(tableName);
        javaGenConfig.setTablePkName(pkId);
        javaGenConfig.setFieldToColumn(fieldToColumn);


        AbstractJavaGen javaEntityGen = new ModuleMappingGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }


    public static void testJavaController(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                          String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName+"";
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import "+packgePath+".model"+"."+entityName+";");
        imports.add("import "+packgePath+".service."+entityName+"Service;");
        imports.add("import "+prefixPackage+".utils.DateUtils;");
        imports.add("import java.util.List;");
        imports.add("import java.util.Map;");
        imports.add("import java.util.HashMap;");
//        imports.add("import com.weidian.proxy.annotation.JsonRes;");
        imports.add("import org.springframework.beans.factory.annotation.Autowired;");
        imports.add("import org.springframework.util.Assert;");
        imports.add("import org.springframework.web.bind.annotation.RequestMapping;");
        imports.add("import org.springframework.web.bind.annotation.ResponseBody;");
        imports.add("import javax.servlet.http.HttpServletRequest;");
        imports.add("import org.springframework.web.bind.annotation.RequestMethod;");
        imports.add("import org.apache.commons.lang3.StringUtils;");
        imports.add("import org.springframework.stereotype.Controller;");

        Map<String, String> autowiredClass = new HashMap<String, String>();
        autowiredClass.put(entityName+"Service", toFirstLetterLow(entityName) +"Service");

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setAuthowiredMap(autowiredClass);
        javaGenConfig.setClassNameTemplate("{template}Controller");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/controller");
        javaGenConfig.setPackagePath(packgePath+".controller");
        javaGenConfig.setEntityName(entityName);
        List<String> classAutowireds = new ArrayList<String>();
        classAutowireds.add("Controller");
        classAutowireds.add("RequestMapping(\"/"+fileName.toLowerCase()+"\")");
        javaGenConfig.setClassAutowireds(classAutowireds);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);


        javaGenConfig.setImports(imports);

        javaGenConfig.setFieldToColumn(fieldToColumn);

        AbstractJavaGen javaEntityGen = new JavaControllerGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }

    public static void testJavaService(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                       String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName;
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import "+packgePath+".model"+"."+entityName+";");
        imports.add("import "+packgePath+".dao."+entityName+"Dao;");
        imports.add("import "+packgePath+".service."+entityName+"Service;");
        imports.add("import java.util.List;");
        imports.add("import java.util.Map;");
        imports.add("import java.util.HashMap;");
        imports.add("import org.springframework.beans.factory.annotation.Autowired;");
        imports.add("import org.springframework.stereotype.Component;");

        Map<String, String> autowiredClass = new HashMap<String, String>();
        autowiredClass.put(entityName+"Dao", toFirstLetterLow(entityName)+"Dao");

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setAuthowiredMap(autowiredClass);
        javaGenConfig.setClassNameTemplate("{template}ServiceImpl");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/service/impl");
        javaGenConfig.setPackagePath(packgePath+".service.impl");
        javaGenConfig.setEntityName(entityName);
        List<String> classAutowireds = new ArrayList<String>();
        classAutowireds.add("Component");
        javaGenConfig.setClassAutowireds(classAutowireds);
        List<String> implInterfaceNames = new ArrayList<String>();
        implInterfaceNames.add(entityName+"Service");
        javaGenConfig.setImplInterfaceNames(implInterfaceNames);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);

        javaGenConfig.setImports(imports);

        javaGenConfig.setFieldToColumn(fieldToColumn);


        AbstractJavaGen javaEntityGen = new JavaServiceGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }

    public static void testJavaServiceInterface(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                                String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName;
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import "+packgePath+".model"+"."+fileName+";");
        imports.add("import java.util.List;");

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setClassNameTemplate("{template}Service");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/service");
        javaGenConfig.setPackagePath(packgePath+".service");
        javaGenConfig.setEntityName(entityName);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);

        javaGenConfig.setImports(imports);

        javaGenConfig.setFieldToColumn(fieldToColumn);

        AbstractJavaGen javaEntityGen = new JavaServiceIntefaceGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }

    private static String toFirstLetterLow(String data) {
        return Character.toLowerCase(data.charAt(0)) + data.substring(1);
    }

    public static void testJavaDao(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                   String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName;
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import "+packgePath+".model"+"."+entityName+";");
        imports.add("import "+packgePath+".mapper."+entityName+"Mapper;");
        imports.add("import java.util.List;");
        imports.add("import java.util.Map;");
        imports.add("import java.util.HashMap;");
        imports.add("import org.springframework.beans.factory.annotation.Autowired;");
        imports.add("import org.springframework.stereotype.Component;");


        Map<String, String> autowiredClass = new HashMap<String, String>();
        autowiredClass.put(entityName+"Mapper", toFirstLetterLow(entityName) +"Mapper");

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setAuthowiredMap(autowiredClass);
        javaGenConfig.setClassNameTemplate("{template}Dao");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/dao");
        javaGenConfig.setPackagePath(packgePath+".dao");
        javaGenConfig.setEntityName(entityName);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);
        List<String> classAutowireds = new ArrayList<String>();
        classAutowireds.add("Component");
        javaGenConfig.setClassAutowireds(classAutowireds);

        javaGenConfig.setImports(imports);

        javaGenConfig.setFieldToColumn(fieldToColumn);

        AbstractJavaGen javaEntityGen = new JavaDaoGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }

    public static void testJavaMapper(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                      String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName;
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import "+packgePath+".model"+"."+entityName+";");
        imports.add("import java.util.List;");
        imports.add("import java.util.Map;");

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setClassNameTemplate("{template}Mapper");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/mapper");
        javaGenConfig.setPackagePath(packgePath+".mapper");
        javaGenConfig.setEntityName(entityName);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);

        javaGenConfig.setImports(imports);
        javaGenConfig.setFieldToColumn(fieldToColumn);

        AbstractJavaGen javaEntityGen = new JavaMapperGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }

    public static void testJavaEntity(Map<String, String> fieldToColumn, Map<String, Class> fields,
                                      String entityName, String tableName, String pkId, String entityPkeyName) throws IOException{
        JavaGenConfig javaGenConfig = new JavaGenConfig();
        String fileName = entityName;
        String fileSuffix = "java";
        List<String> imports = new ArrayList<String>();
        imports.add("import java.io.Serializable;");

        for(Map.Entry<String, Class> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldType = entry.getValue();
            if(fieldType == BigInteger.class) {
                if(imports.contains("import java.math.BigInteger;")) {
                    continue;
                }
                imports.add("import java.math.BigInteger;");
            } else if(fieldType == BigDecimal.class) {
                if(imports.contains("import java.math.BigDecimal;")) {
                    continue;
                }
                imports.add("import java.math.BigDecimal;");
            } else if(fieldType == Date.class) {
                if(imports.contains("import java.util.Date;")) {
                    continue;
                }
                imports.add("import java.util.Date;");
            }
        }

        List<String> descs = new ArrayList<String>();
        descs.add("作者：jiangfuqiang");
        javaGenConfig.setDescs(descs);
        javaGenConfig.setClassNameTemplate("{template}");
        javaGenConfig.setFileName(fileName);
        javaGenConfig.setFileSuffix(fileSuffix);
        javaGenConfig.setPath(path+"/model");
        javaGenConfig.setPackagePath(packgePath+".model");
        javaGenConfig.setImports(imports);
        javaGenConfig.setEntityPrimaryKeyName(entityPkeyName);
        javaGenConfig.setFieldToColumn(fieldToColumn);

        AbstractJavaGen javaEntityGen = new JavaEntityGen(javaGenConfig,fields);
        javaEntityGen.gen();
    }
}
