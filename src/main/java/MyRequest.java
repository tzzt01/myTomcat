import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyRequest表示一个HTTP请求。功能：
 *      1. 可以传递InputStream对象（accept()方法生成的Socket对象的getInputStream()方法生成）
 *      2. InputStream对象生成Request对象；
 *      3. 调用InputStream对象的read()方法，可以读取HTTP请求的原始数据
 */
public class MyRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRequest.class);

    private InputStream input;
    private String uri;

    public MyRequest(InputStream input) {
        this.input = input;
    }

    public String getUri() {
        LOGGER.info("uri === ", uri);
        return uri;
    }

    /**
     *  解析用户请求
     */
    public void parse() {
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[1024];
        int i;
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j=0; j<i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.println("request == " + request.toString());
        this.parseUri(request.toString());
    }

    /**
     * GET /index.html HTTP/1.1
     * 该方法在请求行中搜索第一个和第二个空格，从而找出URI
     * @param requestString
     * @return
     */
    private void parseUri(String requestString) {
        int index1;
        int index2 = 0;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
        }
        if (index1 == -1 || index2 == -1) {
            System.out.println("请求信息为空！");
        }
        uri = requestString.substring(index1 + 1, index2);
    }
}
