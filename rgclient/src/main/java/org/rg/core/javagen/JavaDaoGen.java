package org.rg.core.javagen;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 17/1/18.
 */
public class JavaDaoGen extends AbstractJavaGen {


    public JavaDaoGen(JavaGenConfig javaGenConfig, Map<String, Class> entityFields) {
        super(javaGenConfig, entityFields);
    }

    @Override
    public void gen() throws IOException{
        //生成文件
        String fileName = getFileName();
        String pfileName = javaGenConfig.getFileName();
        String fileAbsolutePath = getFileAbsolutePath(fileName);
        String pkType = getPKType();

        //生成类的共有基础信息
        generateBaseInfo(fileName,fileAbsolutePath,false,false);

        String entityName = javaGenConfig.getEntityName();
        String aliasEntity = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
        String aliasfileName = Character.toLowerCase(pfileName.charAt(0)) + pfileName.substring(1);

        StringBuilder countSb = new StringBuilder();
        countSb.append(oneTab).append("public int count(").append(entityName).append(" ").append(aliasEntity)
                .append("){\n");
        StringBuilder findSb = new StringBuilder();
        String createdMap = "Map<String,Object> map = new HashMap<String, Object>();\n";
        findSb.append(twoTab).append(createdMap);
        //写入set,get方法
        for(Map.Entry<String, Class> entry : entityFields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldType = entry.getValue();
            String getFieldName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

            findSb.append(twoTab).append("map.put(\"").append(fieldName).append("\"").append(",")
                    .append(aliasEntity).append(".").append(getFieldName).append("());\n");
        }
        countSb.append(findSb.toString());
        countSb.append(twoTab).append("return ").append(aliasfileName).append("Mapper.count(map);\n");
        countSb.append(oneTab).append("}\n\n");

        countSb.append(oneTab).append("public List<").append(entityName).append("> list(").append(entityName)
                .append(" ").append(aliasEntity).append(",int start, int limit){\n");
        findSb.append(twoTab).append("map.put(\"start\",start);\n\t\tmap.put(\"limit\", limit);\n");
        countSb.append(findSb.toString());
        countSb.append(twoTab).append("return ").append(aliasfileName).append("Mapper.list(map);\n");
        countSb.append(oneTab).append("}\n\n");

        countSb.append(oneTab).append("public void save(").append(entityName).append(" ").append(aliasEntity)
                .append("){\n");
        countSb.append(twoTab).append(aliasfileName).append("Mapper.save(").append(aliasEntity).append(");\n");
        countSb.append(oneTab).append("}\n");

        countSb.append(oneTab).append("public void update(").append(entityName).append(" ").append(aliasEntity)
                .append("){\n");
        countSb.append(twoTab).append(aliasfileName).append("Mapper.update(").append(aliasEntity).append(");\n");
        countSb.append(oneTab).append("}\n");

        if(StringUtils.isNotBlank(pkType)) {
            countSb.append(oneTab).append("public void delete(").append(pkType).append(" ").append(javaGenConfig.getEntityPrimaryKeyName())
                    .append("){\n");
            countSb.append(twoTab).append(aliasfileName).append("Mapper.delete(").append(javaGenConfig.getEntityPrimaryKeyName()).append(");\n");
        } else {
            countSb.append(oneTab).append("public void delete(").append(entityName).append(" ").append(aliasEntity)
                    .append("){\n");
            countSb.append(twoTab).append(aliasfileName).append("Mapper.delete(").append(aliasEntity).append(");\n");
        }
        countSb.append(oneTab).append("}\n");


        if(StringUtils.isNotEmpty(pkType)) {
            countSb.append(oneTab).append("public ").append(entityName).append(" find").append(entityName).append("ById(").append(pkType).append(" ").append(javaGenConfig.getEntityPrimaryKeyName())
                    .append("){\n");
            countSb.append(twoTab).append("return ").append(aliasfileName).append("Mapper.find").append(entityName)
                    .append("ById(").append(javaGenConfig.getEntityPrimaryKeyName()).append(");\n");
        } else {
            countSb.append(oneTab).append("public ").append(entityName).append(" find").append(entityName).append("ById(").append(entityName).append(" ").append(aliasEntity)
                    .append("){\n");
            countSb.append(twoTab).append("return ").append(aliasfileName).append("Mapper.find").append(entityName)
                    .append("ById(").append(aliasEntity).append(");\n");
        }

        countSb.append(oneTab).append("}\n");

        writeCode(fileAbsolutePath, countSb.toString(), true);

        writeCode(fileAbsolutePath,"}",true);
    }
}
