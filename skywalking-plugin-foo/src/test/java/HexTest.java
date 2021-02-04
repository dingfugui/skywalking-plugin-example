import org.apache.commons.codec.binary.Base64;

public class HexTest {
    public static void main(String[] args) {
        Base64 c = new Base64();
        System.out.println(c.encodeToString("abc".getBytes()));
        ;
    }

}
