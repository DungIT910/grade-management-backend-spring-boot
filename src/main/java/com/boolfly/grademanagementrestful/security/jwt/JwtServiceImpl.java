package com.boolfly.grademanagementrestful.security.jwt;

import com.boolfly.grademanagementrestful.domain.User;
import com.boolfly.grademanagementrestful.exception.base.GradeManagementRuntimeException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.signer-key}")
    private String signerKey;

    @Value("${jwt.valid-duration}")
    private Long validDuration;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("DungIT910")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("role", user.getRole().getName())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new GradeManagementRuntimeException(e);
        }
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (JOSEException | ParseException e) {
            throw new GradeManagementRuntimeException(e);
        }
        return claims;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            JWTClaimsSet claims = Optional.ofNullable(getClaimsFromToken(token))
                    .orElseThrow(() -> new GradeManagementRuntimeException("Claims not found"));
            username = claims.getStringClaim("username");
        } catch (ParseException e) {
            throw new GradeManagementRuntimeException(e);
        }
        return username;
    }
}
