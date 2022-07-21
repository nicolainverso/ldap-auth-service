package com.example.ldapauthservice;

import com.example.ldapauthservice.models.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;


import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    private static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    public static String generateTokenHMACSigned(Person person) throws Exception {

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();

        String jwtToken = Jwts.builder()
                .claim("name", person.getCn())
                .claim("surname", person.getSn())
                .setSubject("example")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwtToken;
    }

    public static Jws<Claims> parseJwtHMACSigned(String jwtString) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .build()
                .parseClaimsJws(jwtString);
    }
/*
    private static Key getPrivateKey() throws Exception
    {
        ClassLoader classLoader = JwtProvider.class.getClassLoader();
        File file = new File(classLoader.getResource("private.key").getFile());
        byte[] rsaPrivateKeyArr = FileUtils.readFileToByteArray(file);
        String rsaPrivateKey = new String(rsaPrivateKeyArr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(rsaPrivateKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }
*/
}
