package com.jzo2o.gateway.config;

import cn.hutool.extra.spring.SpringUtil;
import com.jzo2o.common.utils.JwtTool;
import com.jzo2o.gateway.properties.ApplicationProperties;
import com.jzo2o.gateway.constants.UserConstants;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 生成多个token解析器
 * 此文件的主要作用是根据配置文件中的 tokenKey 动态生成多个 JwtTool 实例，并注册到 Spring 容器中，
 * 便于在项目中按需使用这些工具类进行 Token 的生成和解析。
 *
 */
@Configuration
public class JwtToolConfiguration {

    @Resource
    private ApplicationProperties applicationProperties;

    /**
     * 初始化JwtTool
     * JwtTool 被动态初始化为多个实例，每个实例对应不同的用户类型（如 C 端用户、服务端用户等）。
     * 这些实例使用不同的密钥来生成和解析 Token，从而实现对不同用户类型的认证和授权逻辑。
     */
    @PostConstruct
    public void initJwtTools() {
        for (Map.Entry<String, String> entry : applicationProperties.getTokenKey().entrySet()) {
            String beanName = UserConstants.JWT_TOKEN_BEAN_NAME + entry.getKey();
            JwtTool jwtTool = new JwtTool(entry.getValue());
            SpringUtil.registerBean(beanName, jwtTool);
        }
    }

}
