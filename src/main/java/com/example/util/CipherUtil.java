package com.example.util;

import java.io.Serializable;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import lombok.NonNull;

/**
 * @see {@link http://a4dosanddos.hatenablog.com/entry/2014/02/08/144856}
 */
@Component
public class CipherUtil {
    private Cipher encCipher;
    private Cipher decCipher;
    private Encoder encoder = Base64.getUrlEncoder();
    private Decoder decoder = Base64.getUrlDecoder();

    @PostConstruct
    public void init() throws Exception {
//	initRsaKeyPair();
	initDesKey();
    }

    // 暗号
    public String encrypt(@NonNull Serializable serializableObject) throws Exception {
	byte[] serializedData = SerializationUtils.serialize(serializableObject);
	byte[] encryptedData = encrypt(serializedData);
	return encoder.encodeToString(encryptedData);
    }

    public byte[] encrypt(@NonNull byte[] data) throws Exception {
	return encCipher.doFinal(data);
    }

    // 復号
    public Object decrypt(@NonNull String base64Encrypted) throws Exception {
	byte[] encryptedData = decoder.decode(base64Encrypted);
	byte[] decryptedData = decrypt(encryptedData);
	return SerializationUtils.deserialize(decryptedData);
    }

    public byte[] decrypt(@NonNull byte[] data) throws Exception {
	return decCipher.doFinal(data);
    }

    private void initRsaKeyPair() throws Exception {
	KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
	kg.initialize(2048);
	KeyPair keyPair = kg.generateKeyPair();
	KeyFactory factoty = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
	RSAPublicKeySpec publicKeySpec = factoty.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
	RSAPrivateKeySpec privateKeySpec = factoty.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
	
	PublicKey publicKey = factoty.generatePublic(publicKeySpec);
	PrivateKey privateKey = factoty.generatePrivate(privateKeySpec);

	encCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	encCipher.init(Cipher.ENCRYPT_MODE, privateKey);
	decCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	decCipher.init(Cipher.ENCRYPT_MODE, publicKey);
    }

    private void initDesKey() throws Exception {
	KeyGenerator kg = KeyGenerator.getInstance("DES");
	Key key = kg.generateKey();

	encCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	encCipher.init(Cipher.ENCRYPT_MODE, key);
	
	decCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	byte iv[] = encCipher.getIV();
        IvParameterSpec dps = new IvParameterSpec(iv);
        decCipher.init(Cipher.DECRYPT_MODE, key, dps);
    }
}
