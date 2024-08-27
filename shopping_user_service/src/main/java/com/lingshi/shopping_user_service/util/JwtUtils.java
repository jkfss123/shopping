package com.lingshi.shopping_user_service.util;


import lombok.SneakyThrows;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // 私钥
    public final static String PRIVATE_JSON = "";
    // 公钥
    public final static String PUBLIC_JSON = "";


    /**
     * 生成token
     *
     * @param userId    用户id
     * @param username 用户名字
     * @return
     */
    @SneakyThrows
    public static String sign(Long userId, String username) {
        // 1、 创建jwtclaims  jwt内容载荷部分
        JwtClaims claims = new JwtClaims();
        // 是谁创建了令牌并且签署了它
        claims.setIssuer("lingshi");
        // 令牌将被发送给谁
        claims.setAudience("audience");
        // 失效时间长 （分钟）
        claims.setExpirationTimeMinutesInTheFuture(60 * 24);
        // 令牌唯一标识符
        claims.setGeneratedJwtId();
        // 当令牌被发布或者创建现在
        claims.setIssuedAtToNow();
        // 再次之前令牌无效
        claims.setNotBeforeMinutesInThePast(2);
        // 主题
        claims.setSubject("shopping");
        // 可以添加关于这个主题得声明属性
        claims.setClaim("userId", userId);
        claims.setClaim("username", username);


        // 2、签名
        JsonWebSignature jws = new JsonWebSignature();
        //赋值载荷
        jws.setPayload(claims.toJson());


        // 3、jwt使用私钥签署
        PrivateKey privateKey = new RsaJsonWebKey(JsonUtil.parseJson(PRIVATE_JSON)).getPrivateKey();
        jws.setKey(privateKey);


        // 4、设置关键 kid
        jws.setKeyIdHeaderValue("keyId");


        // 5、设置签名算法
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        // 6、生成jwt
        String jwt = jws.getCompactSerialization();
        return jwt;
    }




    /**
     * 解密token，获取token中的信息
     *
     * @param token
     */
    @SneakyThrows
    public static Map<String, Object> verify(String token){
        // 1、引入公钥
        PublicKey publicKey = new RsaJsonWebKey(JsonUtil.parseJson(PUBLIC_JSON)).getPublicKey();
        // 2、使用jwtcoonsumer  验证和处理jwt
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() //过期时间
                .setAllowedClockSkewInSeconds(30) //允许在验证得时候留有一些余地 计算时钟偏差  秒
                .setRequireSubject() // 主题生命
                .setExpectedIssuer("lingshi") // jwt需要知道谁发布得 用来验证发布人
                .setExpectedAudience("audience") //jwt目的是谁 用来验证观众
                .setVerificationKey(publicKey) // 用公钥验证签名  验证密钥
                .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256))
                .build();
        // 3、验证jwt 并将其处理为 claims
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getClaimsMap();
        }catch (Exception e){
            return new HashMap();
        }
    }




    public static void main(String[] args){
        // 生成
        String jinken = sign(1001L, "jinken");
        System.out.println(jinken);


        Map<String, Object> stringObjectMap = verify(jinken);
        System.out.println(stringObjectMap.get("userId"));
        System.out.println(stringObjectMap.get("username"));
    }
}

