package net.thrymr.task.configuration;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import net.thrymr.task.entity.AppUser;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JwtTokenUtils {

    public static String secretKey = "841D8A6C80CBA4FCAD32D5367C18C53B";

    /**
     *
     */
    private static final long serialVersionUID = -1029281748694725202L;
    public String getToken(AppUser user) throws JOSEException {
        //Payload
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder();
        claims.expirationTime(new Date(new Date().getTime() + 8 * 24 * 60 * 60 * 1000));
        claims.claim("email", user.getEmail()).claim("name", user.getName())
                .build();
        Payload payload = new Payload(claims.build().toJSONObject());
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
        DirectEncrypter encrypter = new DirectEncrypter(secretKey.getBytes(StandardCharsets.UTF_8));
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(encrypter);
        String token = jweObject.serialize();

        return token;
    }
    //    final SignedJWT signedJWT = SignedJWT.parse(jwtToken);
    public Map<String, String> parseToken(String token) throws BadJOSEException, ParseException, JOSEException, BadJOSEException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<SimpleSecurityContext>();
        JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<SimpleSecurityContext>(secretKey.getBytes());
        JWEKeySelector<SimpleSecurityContext> jweKeySelector =
                new JWEDecryptionKeySelector<SimpleSecurityContext>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jweKeySource);
        jwtProcessor.setJWEKeySelector(jweKeySelector);

        JWTClaimsSet claims = jwtProcessor.process(token, null);
        Map<String, String> stringStringMap = new HashMap<String, String>();
        stringStringMap.put("email", (String) claims.getClaim("email"));
        stringStringMap.put("name", (String) claims.getClaim("name"));

        return stringStringMap;
    }
}
