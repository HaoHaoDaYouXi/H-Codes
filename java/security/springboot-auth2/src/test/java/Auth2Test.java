import com.haohaodayouxi.auth2.Auth2Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * UtilTest
 *
 * @author TONE
 * @date 2024/8/11 11:33
 */
@Slf4j
@SpringBootTest(classes = Auth2Bootstrap.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Auth2Test {

    @Test
    public void test() {
        log.info("Auth2Test");
    }
}
