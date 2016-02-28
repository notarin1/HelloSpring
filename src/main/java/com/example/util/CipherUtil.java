package com.example.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import lombok.NonNull;

/**
 * @see {@link http://a4dosanddos.hatenablog.com/entry/2014/02/08/144856}
 */
@Component
public class CipherUtil {
    private static final String CHAR_CODE = "UTF-8";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
	getKeyPair2();
    }

    // 暗号
    public String encrypt(@NonNull Object serializableObject) throws Exception {
	byte[] encryptedData = encrypt(SerializationUtils.serialize(serializableObject));
	return new String(encryptedData, CHAR_CODE);
    }

    public byte[] encrypt(@NonNull byte[] data) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	return cipher.doFinal(data);
    }

    // 復号
    public Object decrypt(@NonNull String encrypted) throws Exception {
	byte[] decryptedData = decrypt(encrypted.getBytes(CHAR_CODE));
	return SerializationUtils.deserialize(decryptedData);
    }

    public byte[] decrypt(@NonNull byte[] data) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	cipher.init(Cipher.DECRYPT_MODE, publicKey);
	return cipher.doFinal(data);
    }

    private void getKeyPair2() throws Exception {
	KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
	kg.initialize(2048);
	KeyPair keyPair = kg.generateKeyPair();
	KeyFactory factoty = KeyFactory.getInstance("RSA");
	RSAPublicKeySpec publicKeySpec = factoty.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
	RSAPrivateKeySpec privateKeySpec = factoty.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
	publicKey = factoty.generatePublic(publicKeySpec);
	privateKey = factoty.generatePrivate(privateKeySpec);
    }
}
