
package com.github.benhaixiao.concurrent.configure;

import com.github.benhaixiao.concurrent.configure.impl.InitConfigProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author xiaobenhai
 */
public class DefaultConfigure<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigure.class);


    public T init(String initConfigPath, Class object) {
        InitConfigProvider<T> initProvider = new InitConfigProvider<T>();
        String configFilePath = StringUtils.isNotBlank(initConfigPath) ? DefaultConfigure.class.getResource(initConfigPath).getPath() : initProvider.getPath();
        LOGGER.info("***********DefaultConfigure init() begin*********** {}", configFilePath);
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            return initProvider.get(configFile.getAbsolutePath(), object);
        } else {
            LOGGER.warn("message: can not found config file ,file path: {},", configFilePath);
        }
        return null;
    }
}
