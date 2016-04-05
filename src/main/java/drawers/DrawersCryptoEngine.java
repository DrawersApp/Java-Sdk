package drawers;

/**
 * Created by nishant.pathak on 05/04/16.
 */
public interface DrawersCryptoEngine {
    public static final byte[] ZERO_CTR = new byte[] { 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 };

    public abstract byte[] aesDecrypt(byte[] key, byte[] ctr, byte[] b)
            throws Exception;

    public abstract byte[] aesEncrypt(byte[] key, byte[] ctr, byte[] b)
            throws Exception;
}
