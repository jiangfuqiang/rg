package org.rg.core.xmlgen;

import org.apache.commons.lang3.StringUtils;
import org.rg.core.javagen.AbstractJavaGen;
import org.rg.core.javagen.JavaGenConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jiang on 17/1/19.
 */
public class ModuleMappingGen extends AbstractJavaGen{


    public ModuleMappingGen(JavaGenConfig javaGenConfig, Map<String, Class> fieldTypes) {
        super(javaGenConfig,fieldTypes);
    }

    public void gen() throws IOException{

        //生成文件
        String fileName = getFileName();
        String fileAbsolutePath = getFileAbsolutePath(fileName);
        String basePackage = javaGenConfig.getPackagePath();
        String entityName = javaGenConfig.getEntityName();
        String aliasEntity = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
        String enityPKName = javaGenConfig.getEntityPrimaryKeyName();
        String pkId = javaGenConfig.getTablePkName();
        String pfileName = javaGenConfig.getFileName();
        String tableName = javaGenConfig.getTableName();
        String tablePkName = javaGenConfig.getTablePkName();
        Map<String, String> fieldToColumn = javaGenConfig.getFieldToColumn();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD MAPPER 3.0//EN\"\n")
            .append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");

        sb.append("<mapper namespace=\"").append(basePackage).append(".mapper.").append(pfileName).append("Mapper\">\n");

        //count method
        sb.append(oneTab).append("<select id=\"count\" resultType=\"int\" parameterType=\"Map\">\n");
        sb.append(twoTab).append("select count(1) from `").append(tableName).append("` where 1 = 1 \n");
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            if(key.equals(enityPKName)) {   //去掉主键
                continue;
            }
            String column = fieldToColumn.get(key);
            sb.append(twoTab).append("<if test=\"").append(key).append(" != null\">\n");
            sb.append(oneTab).append(twoTab).append("and ").append(column).append(" = #{").append(key).append("}\n");
            sb.append(twoTab).append("</if>\n");
        }
        sb.append(oneTab).append("</select>\n");


        //list method
        sb.append(oneTab).append("<select id=\"list\" resultType=\""+entityName).append("\" parameterType=\"Map\">\n");
        sb.append(twoTab).append("select * from `").append(tableName).append("` where 1 = 1 \n");
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            if(key.equals(enityPKName)) {   //去掉主键
                continue;
            }
            String column = fieldToColumn.get(key);
            sb.append(twoTab).append("<if test=\"").append(key).append(" != null\">\n");
            sb.append(oneTab).append(twoTab).append("and ").append(column).append(" = #{").append(key).append("}\n");
            sb.append(twoTab).append("</if>\n");
        }
        sb.append(twoTab).append("order by id desc limit #{start},#{limit}");
        sb.append(oneTab).append("</select>\n");


        //findEntityById method
        sb.append(oneTab).append("<select id=\"find"+entityName+"ById\" resultType=\"").append(entityName)
                .append("\"");
        if(StringUtils.isNotBlank(enityPKName)) {
            String pkType = this.getPKType();
            sb.append(" parameterType=\"").append(convertType(pkType)).append("\">\n");
        } else {
            sb.append(" parameterType=\"").append(entityName).append("\">\n");
        }


        sb.append(twoTab).append("select * from `").append(tableName);
        if(StringUtils.isNotBlank(enityPKName)) {
            sb.append("` where ").append(tablePkName)
                    .append(" = #{").append(enityPKName).append("}\n");
        } else {
            sb.append("` where 1=1");
            for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
                String key = entry.getKey();
                if(key.equals(enityPKName)) {   //去掉主键
                    continue;
                }
                String column = fieldToColumn.get(key);
                sb.append(twoTab).append("<if test=\"").append(key).append(" != null\">\n");
                sb.append(oneTab).append(twoTab).append("and ").append(column).append(" = #{").append(key).append("}\n");
                sb.append(twoTab).append("</if>\n");
            }
        }
        sb.append(oneTab).append("</select>\n");


        //delete method
        sb.append(oneTab).append("<delete id=\"delete\" ");
        if(StringUtils.isNotBlank(enityPKName)) {
            String pkType = this.getPKType();
            sb.append(" parameterType=\"").append(convertType(pkType)).append("\">\n");
        } else {
            sb.append(" parameterType=\"").append(entityName).append("\">\n");
        }
        sb.append(twoTab).append("delete from `").append(tableName);
        if(StringUtils.isNotBlank(enityPKName)) {
            sb.append("` where ").append(tablePkName)
                    .append(" = #{").append(enityPKName).append("}\n");
        } else {
            sb.append("` where ");
            for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
                String key = entry.getKey();
                if(key.equals(enityPKName)) {   //去掉主键
                    continue;
                }
                String column = fieldToColumn.get(key);
                sb.append(twoTab).append("<if test=\"").append(key).append(" != null\">\n");
                sb.append(oneTab).append(twoTab).append("and ").append(column).append(" = #{").append(key).append("}\n");
                sb.append(twoTab).append("</if>\n");
            }
        }
        sb.append(oneTab).append("</delete>\n");

        //save method
        sb.append(oneTab).append("<insert id=\"save\" parameterType=\"").append(entityName).append("\"")
                .append(" useGeneratedKeys=\"true\"").append("  keyProperty=\"").append(enityPKName)
                .append("\" keyColumn=\"").append(tablePkName).append("\" ")
                .append(">\n");
        sb.append(twoTab).append("insert into ").append(tableName).append("(");
        StringBuilder tempSb = new StringBuilder();
        StringBuilder csb = new StringBuilder();
        tempSb.append(twoTab);
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            if(key.equals(enityPKName)) {   //去掉主键
                continue;
            }
            String column = fieldToColumn.get(key);
            tempSb.append("#{").append(key).append("},");
            csb.append("`").append(column).append("`,");
        }
        sb.append(csb.substring(0,csb.length()-1));
        sb.append(twoTab).append(")values(\n");
        sb.append(twoTab).append(tempSb.substring(0,tempSb.length()-1));
        sb.append(")\n");
        sb.append(oneTab).append("</insert>\n");


        //update method
        sb.append(oneTab).append("<update id=\"update\" parameterType=\"").append(entityName).append("\">\n");
        sb.append(twoTab).append("update ").append(tableName).append(" set 1=1 \n");
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            if(key.equals(enityPKName)) {  //去掉主键
                continue;
            }
            String column = fieldToColumn.get(key);
            sb.append(twoTab).append("<if test=\"").append(key).append(" != null\">\n");
            sb.append(oneTab).append(twoTab).append(", ").append(column).append(" = #{").append(key).append("}\n");
            sb.append(twoTab).append("</if>\n");
        }
        sb.append(twoTab).append(" where ").append(tablePkName).append(" = #{").append(enityPKName).append("}\n");
        sb.append(oneTab).append("</update>\n");


        //resultMap
        sb.append(oneTab).append("<resultMap type=\"").append(entityName).append("\" id=\"").append(entityName).append("\">\n");
        if(fieldToColumn.containsKey(enityPKName)) {
            sb.append(twoTab).append("<id column=\"").append(fieldToColumn.get(enityPKName)).append("\" property=\"")
                    .append(enityPKName).append("\"/>\n");
        }
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            String column = fieldToColumn.get(key);

            sb.append(twoTab).append("<result column=\"").append(column).append("\" property=\"").append(key).append("\"/>\n");
        }
        sb.append(oneTab).append("</resultMap>\n");

        sb.append("</mapper>\n");
        writeCode(fileAbsolutePath,sb.toString(),false);
    }

    private String convertType(String meta) {
        if("Integer".equals(meta)) {
            return "int";
        } else if("Long".equals(meta)) {
            return "long";
        } else if("Double".equals(meta)) {
            return "double";
        } else if("Float".equals(meta)) {
            return "float";
        } else if("Byte".equals(meta)) {
            return "byte";
        } else if("Short".equals(meta)) {
            return "short";
        } else {
            return meta;
        }
    }

}
