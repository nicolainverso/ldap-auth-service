package com.example.ldapauthservice;

import com.example.ldapauthservice.models.AuthenticationResult;
import com.example.ldapauthservice.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Component;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
public class LdapProvider {

    @Autowired
    LdapContextSource contextSource;
    @Autowired
    LdapTemplate ldapTemplate;

    public AuthenticationResult authenticate(String username, String password) {

        AuthenticationResult authenticationResult = new AuthenticationResult();

        Name dn = LdapNameBuilder.newInstance().build();
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("uid", username));

       boolean result = ldapTemplate.authenticate(dn, filter.encode(),  password, new AuthenticationErrorCallback() {
            @Override
            public void execute(Exception e) {
                authenticationResult.setAuthenticationException(e);
            }
        });

       authenticationResult.setAuthenticated(result);
       return authenticationResult;
    }

    public void create(String username, String password, String[] userGroups) {
        Name dn = LdapNameBuilder
                .newInstance()
                .add("ou", "users")
                .add("cn", username)
                .build();
        DirContextAdapter context = new DirContextAdapter(dn);

        context.setAttributeValues(
                "objectclass",
                userGroups);
        context.setAttributeValue("cn", username);
        context.setAttributeValue("sn", username);
        context.setAttributeValue("userPassword", password);

        ldapTemplate.bind(context);
    }

    public void modify(Name dn, Map<String,String> attribute) {

        DirContextOperations context = ldapTemplate.lookupContext(dn);
        attribute.forEach((k, v) -> context.setAttributeValue(k,v));
        ldapTemplate.modifyAttributes(context);
    }

    public List<Person> search(String username, AttributesMapper<Person> mapper) {
        return ldapTemplate.search("", "uid=" + username, mapper);
    }
}
