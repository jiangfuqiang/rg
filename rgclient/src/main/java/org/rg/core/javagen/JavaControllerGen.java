package org.rg.core.javagen;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 17/1/18.
 */
public class JavaControllerGen extends AbstractJavaGen{

    private List<String> methodAnno;
    public JavaControllerGen(JavaGenConfig javaGenConfig, Map<String, Class> entityFields,List<String> methodAnno) {
        super(javaGenConfig, entityFields);
        this.methodAnno = methodAnno;
    }

    private void appendMethodAnno(StringBuilder sb) {
        for(String ma : methodAnno) {
            sb.append(oneTab).append(ma).append("\n");
        }
    }

    @Override
    public void gen() throws IOException {
        //生成文件
        String fileName = getFileName();
        String pfileName = javaGenConfig.getFileName();
        String fileAbsolutePath = getFileAbsolutePath(fileName);

        String pkType = this.getPKType();
        String pkName = javaGenConfig.getEntityPrimaryKeyName();

        //生成类的共有基础信息
        generateBaseInfo(fileName,fileAbsolutePath,false,false);

        String entityName = javaGenConfig.getEntityName();
        String aliasEntity = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
        String aliasfileName = Character.toLowerCase(pfileName.charAt(0)) + pfileName.substring(1);

        StringBuilder sb = new StringBuilder();

        //list method
        sb.append(oneTab).append("@RequestMapping(value=\"list\", method={RequestMethod.GET,RequestMethod.POST})\n");
//        sb.append(oneTab).append("@ResponseBody\n");
        appendMethodAnno(sb);
        sb.append(oneTab).append("public Map<String, Object> list(HttpServletRequest request) {\n");
        sb.append(twoTab).append("Map<String,Object> map = new HashMap<String,Object>();\n");
        sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                .append("parseRequestToEntity(request);\n");
        sb.append(twoTab).append("String pageStr = request.getParameter(\"page\");\n");
        sb.append(twoTab).append("Integer page = 1;\n");
        
        sb.append(twoTab).append("if (StringUtils.isNotEmpty(pageStr)){\n");
        sb.append(oneTab).append(twoTab).append("page = Integer.parseInt(pageStr);\n");
        sb.append(twoTab).append("}\n");
        
        sb.append(twoTab).append("String limitStr = request.getParameter(\"limit\");\n");
        sb.append(twoTab).append("Integer limit = 20;\n");
        sb.append(twoTab).append("if (StringUtils.isNotEmpty(limitStr)){\n");
        sb.append(oneTab).append(twoTab).append("limit = Integer.parseInt(limitStr);\n");
        sb.append(twoTab).append("}\n");
        
        sb.append(twoTab).append("List<").append(entityName).append("> ").append(aliasEntity).append("s")
                .append(" = ").append(aliasfileName).append("Service.list(").append(aliasEntity).append(",")
                .append("(page-1)*limit,limit);\n");
        sb.append(twoTab).append("int totalCount = ").append(aliasfileName).append("Service.count(")
                .append(aliasEntity).append(");\n");
        sb.append(twoTab).append("map.put(\"data\",").append(aliasEntity).append("s);\n");
        sb.append(twoTab).append("map.put(\"recordsTotal\",totalCount);\n");

        sb.append(twoTab).append("return map;\n");
        sb.append(oneTab).append("}\n\n");

        //findEntityById method
        sb.append(oneTab).append("@RequestMapping(value=\"find").append(entityName).append("ById\", method={RequestMethod.GET,RequestMethod.POST})\n");
//        sb.append(oneTab).append("@ResponseBody\n");
        appendMethodAnno(sb);
        sb.append(oneTab).append("public Map<String, Object> find").append(entityName).append("ById(HttpServletRequest request){\n");
        sb.append(twoTab).append("Map<String,Object> map = new HashMap<String,Object>();\n");
        if(StringUtils.isNotEmpty(pkName)) {
            sb.append(twoTab).append("String ").append(pkName).append("Str = request.getParameter(\"").append(javaGenConfig.getEntityPrimaryKeyName()).append("\");\n");
            sb.append(twoTab).append(pkType).append(" ").append(pkName).append(" = null;\n");
            sb.append(twoTab).append("if (StringUtils.isNotEmpty(").append(pkName).append("Str)){\n");
            String idParsedVaue = parseField(pkName, entityFields.get(pkName));
            sb.append(oneTab).append(twoTab).append(pkName).append(" = ").append(idParsedVaue).append(";\n");
            sb.append(twoTab).append("} else {\n");
            sb.append(oneTab).append(twoTab).append("Assert.isTrue(false,\"参数错误\");\n");
            sb.append(twoTab).append("}\n");
            sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ").append(aliasfileName)
                    .append("Service.find").append(entityName).append("ById(").append(pkName).append(");\n");
        } else {
            sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                    .append("parseRequestToEntity(request);\n");
            sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ").append(aliasfileName)
                    .append("Service.find").append(entityName).append("ById(").append(aliasEntity).append(");\n");
        }

        sb.append(twoTab).append("map.put(\"data\",").append(aliasEntity).append(");\n");
        sb.append(twoTab).append("return map;\n");
        sb.append(oneTab).append("}\n\n");



        //save method
        sb.append(oneTab).append("@RequestMapping(value=\"save\", method={RequestMethod.POST})\n");
//        sb.append(oneTab).append("@ResponseBody\n");
        appendMethodAnno(sb);
        sb.append(oneTab).append("public Map<String, Object> save(HttpServletRequest request){\n");
        sb.append(twoTab).append("Map<String,Object> map = new HashMap<String,Object>();\n");

        sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                .append("parseRequestToEntity(request);\n");
        sb.append(twoTab).append(aliasfileName)
                .append("Service.save(").append(aliasEntity).append(");\n");
        sb.append(twoTab).append("map.put(\"success\",true);\n");
        sb.append(twoTab).append("return map;\n");
        sb.append(oneTab).append("}\n\n");

        //update method
        sb.append(oneTab).append("@RequestMapping(value=\"update\", method={RequestMethod.POST})\n");
//        sb.append(oneTab).append("@ResponseBody\n");
        appendMethodAnno(sb);
        sb.append(oneTab).append("public Map<String, Object> update(HttpServletRequest request){\n");
        sb.append(twoTab).append("Map<String,Object> map = new HashMap<String,Object>();\n");
        sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                .append("parseRequestToEntity(request);\n");
        sb.append(twoTab).append(aliasfileName)
                .append("Service.update(").append(aliasEntity).append(");\n");
        sb.append(twoTab).append("map.put(\"success\",true);\n");
        sb.append(twoTab).append("return map;\n");
        sb.append(oneTab).append("}\n\n");


        //delete method
        sb.append(oneTab).append("@RequestMapping(value=\"delete\", method={RequestMethod.GET,RequestMethod.POST})\n");
//        sb.append(oneTab).append("@ResponseBody\n");
        appendMethodAnno(sb);
        sb.append(oneTab).append("public Map<String, Object> delete(HttpServletRequest request){\n");
        sb.append(twoTab).append("Map<String,Object> map = new HashMap<String,Object>();\n");
        if(StringUtils.isNotEmpty(pkName)) {
            sb.append(twoTab).append("String ").append(pkName).append("Str = request.getParameter(\"").append(javaGenConfig.getEntityPrimaryKeyName()).append("\");\n");
            sb.append(twoTab).append(pkType).append(" ").append(pkName).append(" = null;\n");
            sb.append(twoTab).append("if (StringUtils.isNotEmpty(").append(pkName).append("Str)){\n");
            String idParsedVaue = parseField(pkName, entityFields.get(pkName));
            sb.append(oneTab).append(twoTab).append(pkName).append(" = ").append(idParsedVaue).append(";\n");
            sb.append(twoTab).append("} else {\n");
            sb.append(oneTab).append(twoTab).append("Assert.isTrue(false,\"参数错误\");\n");
            sb.append(twoTab).append("}\n");
            sb.append(twoTab).append(aliasfileName).append("Service.delete(").append(pkName).append(");\n");
        } else {
            sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                    .append("parseRequestToEntity(request);\n");
            sb.append(twoTab).append(aliasfileName).append("Service.delete(").append(aliasEntity).append(");\n");
        }

        sb.append(twoTab).append("map.put(\"success\",true);\n");
        sb.append(twoTab).append("return map;\n");
        sb.append(oneTab).append("}\n\n");

        sb.append(oneTab).append("public ").append(entityName).append(" parseRequestToEntity(HttpServletRequest request) {\n");
        sb.append(twoTab).append(entityName).append(" ").append(aliasEntity).append(" = ")
                .append("new ").append(entityName).append("();\n");

        if(StringUtils.isNotBlank(pkName)) {
            sb.append(twoTab).append("String ").append(pkName).append("Str")
                    .append(" = request.getParameter(\"").append(pkName).append("\");\n");
            Class clazz = entityFields.get(pkName);
            String setFieldName = "set" + Character.toUpperCase(pkName.charAt(0)) + pkName.substring(1);
            sb.append(twoTab).append("if (StringUtils.isNotEmpty(").append(pkName).append("Str)){\n");
            sb.append(oneTab).append(twoTab).append(aliasEntity).append(".").append(setFieldName).append("(");
            sb.append(parseField(pkName, clazz));
            sb.append(");\n");
            sb.append(twoTab).append("}\n");
        }
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.isNotBlank(pkName) && key.equals(pkName)) {
                continue;
            }
            Class clazz = entry.getValue();
            String setFieldName = "set" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
            if(key.toLowerCase().equals("createtime") ||
                    key.toLowerCase().equals("registtime") ||
                    key.toLowerCase().equals("modifytime") ||
                    key.toLowerCase().equals("updatetime") ||
                    key.toLowerCase().equals("lastupdatetime")) {
                if(StringUtils.isNotBlank(pkName)) {
                    sb.append(twoTab).append("if (StringUtils.isNotEmpty(").append(pkName).append("Str)){\n");
                    sb.append(oneTab).append(twoTab).append(aliasEntity).append(".").append(setFieldName).append("(");
                    parseTimeField(sb,key, clazz);
                    sb.append(");\n");
                    sb.append(twoTab).append("}\n");
                }
            } else {

                sb.append(twoTab).append("String ").append(key).append("Str")
                        .append(" = request.getParameter(\"").append(key).append("\");\n");
                if(clazz == String.class) {
                    sb.append(twoTab).append(aliasEntity).append(".").append(setFieldName).append("(");
                    sb.append(parseField(key, clazz));
                    sb.append(");\n");
                } else {
                    sb.append(twoTab).append("if (StringUtils.isNotEmpty(").append(key).append("Str)){\n");
                    sb.append(oneTab).append(twoTab).append(aliasEntity).append(".").append(setFieldName).append("(");
                    sb.append(parseField(key, clazz));
                    sb.append(");\n");
                    sb.append(twoTab).append("}\n");
                }
            }

        }

        sb.append(twoTab).append("return ").append(aliasEntity).append(";\n");
        sb.append(oneTab).append("}\n");

        writeCode(fileAbsolutePath,sb.toString(),true);
        
        writeCode(fileAbsolutePath,"}",true);

    }

    private String parseField(String key, Class clazz) {
        StringBuilder sb = new StringBuilder();
        if(clazz == Integer.class) {
            sb.append("Integer.parseInt(").append(key).append("Str)");
        } else if(clazz == Long.class) {
            sb.append("Long.parseLong(").append(key).append("Str)");
        } else if(clazz == String.class) {
            sb.append(key).append("Str");
        } else if(clazz == Double.class) {
            sb.append("Double.parseDouble(").append(key).append("Str)");
        } else if(clazz == Float.class) {
            sb.append("Float.parseFloat(").append(key).append("Str)");
        } else if(clazz == Short.class) {
            sb.append("Short.parseShort(").append(key).append("Str)");
        } else if(clazz == Byte.class) {
            sb.append("Byte.parseByte(").append(key).append("Str)");
        } else if(clazz == BigDecimal.class) {
            sb.append("BigDecimal.valueOf(Double.parseDouble(").append(key).append("Str))");
        } else if(clazz == BigInteger.class) {
            sb.append("BigInteger.valueOf(Long.parseLong(").append(key).append("Str))");
        } else if(clazz == Date.class) {
            sb.append("DateUtils.parseStrToLongDate(").append(key).append("Str)");
        } else if(clazz == Boolean.class) {
            sb.append("Boolean.parseBoolean(").append(key).append("Str)");
        } else if(clazz == Timestamp.class) {
            sb.append("Timestamp.valueOf(").append(key).append("Str)");
        }
        return sb.toString();
    }
    private void parseTimeField(StringBuilder sb, String key, Class clazz) {

        if(clazz == Integer.class) {
            sb.append("(int)System.currentTimeMillis()/1000");
        } else if(clazz == Long.class) {
            sb.append("System.currentTimeMillis()");
        } else if(clazz == String.class) {
            sb.append(key).append("Str");
        }  else if(clazz == Date.class) {
            sb.append("new java.util.Date()");
        }  else if(clazz == Timestamp.class) {
            sb.append("new java.sql.Timestamp(System.currentTimeMillis())");
        }
    }
}
