import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 16:12
 * @projectName health_parent
 * @description: 七牛测试
 */
public class UploadTest {


/*

    //构造一个带指定 Region 对象的配置类
    Configuration cfg = new Configuration(Zone.zone2());
    //...其他参数参考类注释

    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "MZDnEe7rMyd-ukMRe81XzwclnDr1VrPPNcoPzmH3";
    String secretKey = "LbG2E35i9taJdyLqG-uyuXRmvZQlJkXUmiiawe9f";
    String bucket = "zuoyueer-health2";

    //默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;

    byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);


    public UploadTest() throws UnsupportedEncodingException {
    }
*/


    /**
     * 测试删除文件
     */
    @Test
    public void method() {
        String accessKey = "MZDnEe7rMyd-ukMRe81XzwclnDr1VrPPNcoPzmH3";
        String secretKey = "LbG2E35i9taJdyLqG-uyuXRmvZQlJkXUmiiawe9f";
        String bucket = "zuoyueer-health2";

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = "珂朵莉.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
