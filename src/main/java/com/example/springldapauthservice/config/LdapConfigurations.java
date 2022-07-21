package com.example.ldapauthservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfigurations {

    @Autowired
    Environment env;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();

        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.base"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        contextSource.setPooled(false);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
