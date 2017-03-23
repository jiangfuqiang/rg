package org.rg.core.javagen;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by jiang on 17/1/18.
 */
public class JavaEntityGen extends AbstractJavaGen{

    public JavaEntityGen(JavaGenConfig javaGenConfig, Map<String, Class> fields) {
        super(javaGenConfig,fields);
    }

    @Override
    public void gen() throws IOException{
        //生成文件
        String fileName = getFileName();
        String fileAbsolutePath = getFileAbsolutePath(fileName);


        //生成类的共有基础信息
        generateBaseInfo(fileName,fileAbsolutePath,false,true);
        
        //写入字段定义
        writeFieldsCode(fileAbsolutePath,entityFields, true);

        //写入构造函数
        writeConstructCode(fileAbsolutePath, fileName, true);

        //写入set,get方法
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldType = entry.getValue();
            String getFieldName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            String setFieldName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            StringBuilder getSb = new StringBuilder();
            StringBuilder setSb = new StringBuilder();
            getSb.append("\tpublic ");
            setSb.append("\tpublic void ").append(setFieldName).append("(");
            if(fieldType == String.class) {
                getSb.append("String ");
                setSb.append("String ");
            } else if(fieldType == Integer.class) {
                getSb.append("Integer ");
                setSb.append("Integer ");
            } else if(fieldType == Double.class) {
                getSb.append("Double ");
                setSb.append("Double ");
            } else if(fieldType == Float.class) {
                getSb.append("Float ");
                setSb.append("Float ");
            } else if(fieldType == Long.class) {
                getSb.append("Long ");
                setSb.append("Long ");
            } else if(fieldType == Short.class) {
                getSb.append("Short ");
                setSb.append("Short ");
            } else if(fieldType == Byte.class) {
                getSb.append("Byte ");
                setSb.append("Byte ");
            } else if(fieldType == BigInteger.class) {
                getSb.append("BigInteger ");
                setSb.append("BigInteger ");
            } else if(fieldType == BigDecimal.class) {
                getSb.append("BigDecimal ");
                setSb.append("BigDecimal ");
            } else if(fieldType == Boolean.class) {
                getSb.append("Boolean ");
                setSb.append("Boolean ");
            } else if(fieldType == Date.class) {
                getSb.append("Date ");
                setSb.append("Date ");
            } else if(fieldType == Timestamp.class) {
                getSb.append("Timestamp ");
                setSb.append("Timestamp ");
            }

            setSb.append(fieldName).append("){ this.").append(fieldName).append("=").append(fieldName).append("; }");
            getSb.append(getFieldName).append("(){");
            getSb.append(" return this.").append(fieldName).append(";}");
            writeCode(fileAbsolutePath,setSb.toString(),true);
            writeCode(fileAbsolutePath,getSb.toString(),true);
        }
        
        //闭合
        writeCode(fileAbsolutePath,"}", true);
    }
}
