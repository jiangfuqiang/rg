package org.rg.core.javagen;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 17/1/18.
 */
public class JavaMapperGen extends AbstractJavaGen{

    public JavaMapperGen(JavaGenConfig javaGenConfig, Map<String, Class> entityFields) {
        super(javaGenConfig,entityFields);
    }

    @Override
    public void gen() throws IOException {
        //生成文件
        String fileName = getFileName();
        String fileAbsolutePath = getFileAbsolutePath(fileName);

        //生成类的共有基础信息
        generateBaseInfo(fileName,fileAbsolutePath,true,false);

        String entityName = javaGenConfig.getEntityName();
        String aliasEntity = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
        List<String> methods = new ArrayList<String>();
        methods.add(oneTab+"int count(Map<String,Object> map);");
        methods.add(oneTab+"List<"+entityName+"> list(Map<String,Object> map);");
        methods.add(oneTab+"void save("+entityName+" " + aliasEntity+");");
        methods.add(oneTab+"void update("+entityName+" " + aliasEntity+");");
        String pkType = getPKType();
        if(StringUtils.isNoneBlank(pkType)) {
            methods.add(oneTab+"void delete("+pkType+" " + javaGenConfig.getEntityPrimaryKeyName()+");");
        } else {
            methods.add(oneTab + "void delete(" + entityName + " " + aliasEntity + ");");
        }

        if(StringUtils.isNoneBlank(pkType)) {

            methods.add(oneTab+entityName+" find"+entityName+"ById(" + pkType+" "+javaGenConfig.getEntityPrimaryKeyName()+");");
        } else {
            methods.add(oneTab + entityName + " find" + entityName + "ById(" + entityName + " " + aliasEntity + ");");
        }
        this.writeCodeForList(fileAbsolutePath, methods, true);

        this.writeCode(fileAbsolutePath,"}",true);
    }
}
