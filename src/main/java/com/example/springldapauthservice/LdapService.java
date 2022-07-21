package com.example.ldapauthservice;

import com.example.ldapauthservice.models.AuthenticationResult;
import com.example.ldapauthservice.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.Map;

@Service
public class LdapService {

    //new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
    @Autowired
    LdapProvider ldapProvider;

    public String authenticate(String username,String password) throws Exception {

        AuthenticationResult authenticationResult = ldapProvider.authenticate(username,password);
        Person p = new Person();
        if(authenticationResult.isAuthenticated())
           p = find(username);

        return JwtProvider.generateTokenHMACSigned(p);
    }

    public Person find(String username){

        AttributesMapper<Person> personAttributesMapper = new AttributesMapper<Person>() {
            @Override
            public Person mapFromAttributes(Attributes attributes) throws NamingException {
                Person person = new Person();
                person.setCn(attributes.get("cn").get().toString());
                person.setUid(attributes.get("uid").get().toString());
                person.setSn(attributes.get("sn").get().toString());
                return person;
            }
        };
        List<Person> results = ldapProvider.search(username,personAttributesMapper);
        if(results.size()>0)
            return results.get(0);
        return null;
    }

    public void update(String username, Map<String,String> attributes){
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "users")
                .add("cn", username)
                .build();
        ldapProvider.modify(dn,attributes);
    }



}
