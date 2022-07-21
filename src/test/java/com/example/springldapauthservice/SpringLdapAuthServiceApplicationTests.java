package com.example.ldapauthservice;

import com.example.ldapauthservice.models.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
class SpringLdapAuthServiceApplicationTests {

	@Autowired
	LdapService ldapService;

	@Test
	void contextLoads() {
	}

	@Test
	void authenticate() throws Exception {
		ldapService.authenticate("john","password");
	}

	@Test
	void search(){
		Person p = ldapService.find("john");
		System.out.println(p.toString());
	}

	@Test
	void encodeDecodeJwt() throws Exception {
		Person p = ldapService.find("john");
		String jwt = JwtProvider.generateTokenHMACSigned(p);
		System.out.println(jwt);

		Jwt claims = JwtProvider.parseJwtHMACSigned(jwt);
		System.out.println(claims.getBody().toString());
	}
}
