package com.api.exercise.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@UtilityClass
public class TokenUtils {
    public String generateJwt(String issuer, String subject, int expirationInHours, String secret) {
        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setSubject(subject)
                .setExpiration(
                        Date.from(
                                LocalDateTime
                                        .now()
                                        .plus(expirationInHours, ChronoUnit.HOURS)
                                        .atZone(ZoneId.systemDefault()).toInstant()
                        )
                )
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
