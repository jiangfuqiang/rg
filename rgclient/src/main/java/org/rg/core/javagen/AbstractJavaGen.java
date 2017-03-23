package org.rg.core.javagen;

import org.apache.commons.lang3.StringUtils;
import org.rg.core.utils.DateUtils;
import org.slf4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 17/1/18.
 */
public abstract class AbstractJavaGen {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbstractJavaGen.class);

    protected String oneTab = "\t";
    protected String twoTab = "\t\t";

    protected JavaGenConfig javaGenConfig;
    protected Map<String, Class> entityFields;
    public AbstractJavaGen(JavaGenConfig javaGenConfig,Map<String, Class> entityFields) {

        this.javaGenConfig = javaGenConfig;
        this.entityFields = entityFields;
    }

    /**
     * 生成文件
     * @param path
     * @param fileName
     * @param fileSuffix
     * @throws IOException
     */
    protected String generateFile(String path, String fileName, String fileSuffix) throws IOException{
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
        file = new File(path, fileName + "." + fileSuffix);
        if(!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    /**
     * 给文件写入内容
     * @param path
     * @param message
     * @param isAppend
     * @throws IOException
     */
    protected void writeCode(String path,String message, boolean isAppend) throws IOException{
        File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,isAppend)));

        try {
            bufferedWriter.write(message);
            bufferedWriter.write("\n\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            LOGGER.error("写入文件失败：" + path, e);
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    /**
     * 批量给文件写入内容
     * @param path
     * @param messages
     * @param isAppend
     * @throws IOException
     */
    protected void writeCodeForList(String path,List<String> messages, boolean isAppend) throws IOException{
        File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,isAppend)));

        try {
            for(String message : messages) {
                bufferedWriter.write(message);
                bufferedWriter.write("\n\n");
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            LOGGER.error("写入文件失败：" + path, e);
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    /**
     * 给文件写入字段定义
     * @param path
     * @param messages
     * @param isAppend
     * @throws IOException
     */
    protected void writeFieldsCode(String path,Map<String, Class> messages, boolean isAppend) throws IOException{
        File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,isAppend)));

        try {
            for(Map.Entry<String, Class> entry : messages.entrySet()) {
                String fieldName = entry.getKey();
                Class fieldType = entry.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append("\tprivate ");

                sb.append(parseFieldType(fieldType));
                sb.append(fieldName).append(";\n");
                bufferedWriter.write(sb.toString());
            }
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            LOGGER.error("写入文件失败：" + path, e);
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    protected String parseFieldType(Class fieldType) {
        StringBuilder sb = new StringBuilder();
        if(fieldType == String.class) {
            sb.append("String ");
        } else if(fieldType == Integer.class) {
            sb.append("Integer ");
        } else if(fieldType == Double.class) {
            sb.append("Double ");
        } else if(fieldType == Float.class) {
            sb.append("Float ");
        } else if(fieldType == Long.class) {
            sb.append("Long ");
        } else if(fieldType == Short.class) {
            sb.append("Short ");
        } else if(fieldType == Byte.class) {
            sb.append("Byte ");
        } else if(fieldType == BigInteger.class) {
            sb.append("BigInteger ");
        } else if(fieldType == BigDecimal.class) {
            sb.append("BigDecimal ");
        } else if(fieldType == Boolean.class) {
            sb.append("Boolean ");
        } else if(fieldType == Date.class) {
            sb.append("Date ");
        } else if(fieldType == Timestamp.class) {
            sb.append("Timestamp ");
        }
        return sb.toString();
    }

    /**
     * 给文件写入构造函数
     * @param path
     * @param constructName
     * @param isAppend
     * @throws IOException
     */
    protected void writeConstructCode(String path,String constructName, boolean isAppend) throws IOException{
        File file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,isAppend)));

        try {
            bufferedWriter.write("\t");
            bufferedWriter.write("public ");
            bufferedWriter.write(constructName);
            bufferedWriter.write("(){}");
            bufferedWriter.write("\n\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            LOGGER.error("写入文件失败：" + path, e);
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    /**
     * 生成类描述
     * @param descs
     * @return
     */
    protected String generateClassDesc(List<String> descs) {
        StringBuffer sb = new StringBuffer();
        sb.append("/**\n");
        sb.append("* ").append("created date ").append(DateUtils.parseLongDate(null)).append("\n");
        if(descs != null && descs.size() > 0) {
            for (String desc : descs) {
                sb.append("* ").append(desc).append("\n");
            }
        }
        sb.append("* \n");
        sb.append("*/");
        return sb.toString();
    }


    /**
     * 生成需要注解的类列表
     * @param datas
     * @return
     */
    protected Map<String, String> generateAutowiredMap(String ...datas) {
        Map<String, String> map = new HashMap<String, String>();
        for(String data : datas) {
            String className = data.substring(data.lastIndexOf("\\.")+1);
            String autowiredName = Character.toUpperCase(className.charAt(0)) + className.substring(1);
            map.put("className", autowiredName);
        }
        return map;
    }

    protected void generateBaseInfo(String fileName, String fileAbsolutePath, boolean isInteface, boolean isEntity) throws IOException{
        String packagePath = "package " + javaGenConfig.getPackagePath() + ";";
        //写入包地址
        writeCode(fileAbsolutePath, packagePath, false);

        List<String> imports = javaGenConfig.getImports();
        //导入需要的类
        writeCodeForList(fileAbsolutePath, imports, true);

        List<String> descs = javaGenConfig.getDescs();
        String comments = generateClassDesc(descs);

        //写入类描述
        writeCode(fileAbsolutePath, comments, true);

        if(javaGenConfig.getClassAutowireds() != null && javaGenConfig.getClassAutowireds().size() > 0) {
            //写入类注解
            List<String> classAutowireds = javaGenConfig.getClassAutowireds();
            StringBuilder sb = new StringBuilder();
            for(String auto : classAutowireds) {
                sb.append("@").append(auto).append("\n");
            }
            writeCode(fileAbsolutePath, sb.toString(), true);
        }
        String className = "public " + (isInteface?"interface ":"class ") + fileName +
                (isInteface?"":(isEntity?" implements Serializable":""));

        List<String> implInterfaceNames = javaGenConfig.getImplInterfaceNames();
        if(implInterfaceNames != null && implInterfaceNames.size() > 0) {
            for(String interfaceName : implInterfaceNames) {
                if(className.indexOf(" implements ") > 0) {
                    className += "," + interfaceName;
                } else {
                    className += " implements " + interfaceName;
                }
            }
        }
        className += "{";

        //写入类定义
        writeCode(fileAbsolutePath, className, true);

        //写入注入
        Map<String, String> autowiredClass = javaGenConfig.getAuthowiredMap();
        if(autowiredClass != null && autowiredClass.size() > 0) {
            for (Map.Entry<String, String> entry : autowiredClass.entrySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\t@Autowired\n");
                String key = entry.getKey();
                String value = entry.getValue();
                stringBuilder.append(oneTab).append("private").append("\t").append(key).append(" ").append(value).append(";\n");
                writeCode(fileAbsolutePath, stringBuilder.toString(), true);
            }
        }
    }

    protected String getPKType() {
        if(StringUtils.isNoneBlank(javaGenConfig.getEntityPrimaryKeyName())) {
            Class clazz = entityFields.get(javaGenConfig.getEntityPrimaryKeyName());
            String fieldType = parseFieldType(clazz).trim();
            return fieldType;
        }
        return null;
    }

    protected String getFileName() {
        return javaGenConfig.getClassNameTemplate().replace("{template}", javaGenConfig.getFileName());
    }

    protected String getFileAbsolutePath(String fileName) throws IOException{
        return generateFile(javaGenConfig.getPath(),fileName,javaGenConfig.getFileSuffix());
    }

    public abstract void gen() throws IOException;

}
