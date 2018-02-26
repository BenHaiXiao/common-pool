package com.github.benhaixiao.concurrent.configure.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;

/**
 * @author xiaobenhai
 */
public class InitConfigProvider<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitConfigProvider.class);

    private String path = InitConfigProvider.class.getResource("/common-config.xml").getPath();

    private T config;

    private T getConfig() {
        return config;
    }

    private void setConfig(T config) {
        this.config = config;
    }

    /**
     * webmail-InitConfig.xml这个文件的名称及其路径不能改变。
     */
    private synchronized void read(String path, Class object) {
        try {
            JAXBContext context;
            context = JAXBContext.newInstance(object);
            Unmarshaller marshaller = context.createUnmarshaller();
            setConfig((T) marshaller.unmarshal(new FileInputStream(path)));
        } catch (Exception e) {
            LOGGER.error("read config error", e);
        }
    }

    public void clear() {
        setConfig(null);
    }

    public T reload(String path, Class object) {
        clear();
        LOGGER.info("method<InitConfigProvider> getPath<" + getPath() + "> path<" + path + ">");
        if (path == null) {
            read(getPath(), object);
        } else {
            read(path, object);
        }
        return getConfig();
    }

    public void load(String path, Class object) {
        if (path == null) {
            read(getPath(), object);
        } else {
            read(path, object);
        }
    }

    public T get(String path, Class object) {
        LOGGER.info("method<InitConfigProvider> getPath<" + getPath() + "> path<" + path + ">");
        if (null == config) {
            if (path == null) {
                read(getPath(), object);
            } else {
                read(path, object);
            }
        }
        return getConfig();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
