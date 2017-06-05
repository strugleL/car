package com.wangyichao.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Nicholas_Wang on 2016/10/12.
 */
public class Config {

    private static String confDir;
    private static Properties clearConf;
    private final static String clearConfName = "app.properties";

    public static String getProperty(String key) {
        clearConf = new Properties();
        /**这句话的含义是什么**/
        confDir = Thread.currentThread().getContextClassLoader().getResource("conf").getPath();

        FileInputStream clearConfFis = null;
        try {
            //FileInputStream 读取本地文件中的字节数据
            clearConfFis = new FileInputStream(new File(confDir + File.separator + clearConfName));
            //load()从输入流中读取属性列表（键和元素对）
            clearConf.load(clearConfFis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clearConf.getProperty(key);
    }
}
