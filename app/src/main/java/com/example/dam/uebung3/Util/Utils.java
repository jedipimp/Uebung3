package com.example.dam.uebung3.Util;

import android.telephony.TelephonyManager;
import android.util.Base64;

import com.example.dam.uebung3.Model.RadioType;
import com.example.dam.uebung3.Model.Record;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mat on 20.01.2016.
 */
public class Utils {


    public static int ITERATION_COUNT = 1000;
    public static int KEY_LENGTH = 256;
    public static int SALT_LENGTH = KEY_LENGTH / 8;

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static double calculateGPSDistance(double lat1, double lng1, double lat2, double lng2) {
        double pk = (float) (180.f/Math.PI);

        double a1 = lat1 / pk;
        double a2 = lng1 / pk;
        double b1 = lat2 / pk;
        double b2 = lng2 / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }

    public static RadioType getRadioType(int cellType) {
        switch (cellType) {
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return RadioType.WCDMA;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return RadioType.LTE;
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return RadioType.GMS;
        }

        return null;
    }

    public static String getFilename(Record record) {
        return record.getDate() + "_" + record.getId();
    }

    public static byte[] encrypt(Record record, String password, byte[] salt, byte[] iv) throws Exception {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                ITERATION_COUNT, KEY_LENGTH);

        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        return cipher.doFinal(record.serialize().getBytes("UTF-8"));
    }

    public static Record decrypt(byte[] bytes, String password, byte[] salt, byte[] iv) throws Exception {

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        byte[] plaintext = cipher.doFinal(bytes);
        String plainrStr = new String(plaintext , "UTF-8");

        return Record.newInstanceFromString(plainrStr);
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        return salt;
    }

    public static byte[] generateIV() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    public static byte[] fromBase64(String str) {
        return Base64.decode(str,Base64.DEFAULT);
    }
}
