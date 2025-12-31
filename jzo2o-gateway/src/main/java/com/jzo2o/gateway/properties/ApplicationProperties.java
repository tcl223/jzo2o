package com.jzo2o.gateway.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "jzo2o")
@Data
@Slf4j
/**
 *ApplicationProperties是Spring Boot配置属性类，用于绑定外部配置文件（如application.yml）中的属性，实现配置的集中管理和注入。
 *@ConfigurationProperties(prefix = "jzo2o") ：绑定配置文件中以 jzo2o 为前缀的属性
 * @NestedConfigurationProperty ：标记嵌套配置属性，支持复杂类型绑定
 */
public class ApplicationProperties {

    /**
     * 每一个端都要配置一个token解析key
     * “1”：xxx c端用户token生成key
     * "2": xxx 服务端用户token生成key
     * "3": xxx 机构端用户token生成key
     * "4": xxx 运营端用户token生成key
     * tokenkey
     */
    @NestedConfigurationProperty
    private final Map<String,String> tokenKey = new HashMap<>();

    /**
     * 访问路径地址白名单
     */
    @NestedConfigurationProperty
    private List<String> accessPathWhiteList = new ArrayList<>();

    /**
     * 访问路径黑名单
     */
    @NestedConfigurationProperty
    private List<String> accessPathBlackList = new ArrayList<>();

}
