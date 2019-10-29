package com.shop.bean;

import com.shop.common.util.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {

    private String secret; // 密钥

    private String pubKeyPath;// 公钥

    private String priKeyPath;// 私钥

    private int expire;// token过期时间

    private PublicKey publicKey; // 公钥

    private PrivateKey privateKey; // 私钥

    private String cookieName;//cookie名称

    private int cookieMaxAge;//cookie的过期时间

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

    public JwtProperties(String secret, String pubKeyPath, String priKeyPath, int expire, PublicKey publicKey, PrivateKey privateKey, String cookieName, int cookieMaxAge) {
        this.secret = secret;
        this.pubKeyPath = pubKeyPath;
        this.priKeyPath = priKeyPath;
        this.expire = expire;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
    }

    @Override
    public String toString() {
        return "JwtProperties{" +
                "secret='" + secret + '\'' +
                ", pubKeyPath='" + pubKeyPath + '\'' +
                ", priKeyPath='" + priKeyPath + '\'' +
                ", expire=" + expire +
                ", publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                ", cookieName='" + cookieName + '\'' +
                ", cookieMaxAge=" + cookieMaxAge +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtProperties that = (JwtProperties) o;
        return expire == that.expire &&
                cookieMaxAge == that.cookieMaxAge &&
                Objects.equals(secret, that.secret) &&
                Objects.equals(pubKeyPath, that.pubKeyPath) &&
                Objects.equals(priKeyPath, that.priKeyPath) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(cookieName, that.cookieName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secret, pubKeyPath, priKeyPath, expire, publicKey, privateKey, cookieName, cookieMaxAge);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public static Logger getLogger() {
        return logger;
    }

    public JwtProperties() {
    }
}
