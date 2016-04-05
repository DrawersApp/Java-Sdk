package drawers.impl;


import drawers.DrawersCryptoEngine;
import drawers.helper.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by nishant.pathak on 28/03/16.
 */
public final class DrawersCryptoEngineImpl implements DrawersCryptoEngine {
    @Override
    public byte[] aesEncrypt(byte[] key, byte[] ctr, byte[] b) throws Exception {

        if (ctr == null)
            ctr = ZERO_CTR;

        try {
            IvParameterSpec iv = new IvParameterSpec(ctr);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(b);
            return Base64.encode(encrypted, Base64.DEFAULT  );
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }

    @Override
    public byte[] aesDecrypt(byte[] key, byte[] ctr, byte[] b) throws Exception {

        if (ctr == null)
            ctr = ZERO_CTR;
        try {
            IvParameterSpec iv = new IvParameterSpec(ctr);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            return cipher.doFinal(Base64.decode(b, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }
}
