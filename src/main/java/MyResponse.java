import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * MyResponse表示HTTP响应
 */
/*
    HTTP Response = Status-Line
        *( (general - header | response-header | entity-header) CRLF)
        CRLF
        [ message-body ]
        Status-Line - HTTP-Version SP Status-Code SP Reason-Phrase CRLF
 */
public class MyResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyResponse.class);

    private static final int BUFFER_SIZE = 1024;
    MyRequest request;
    OutputStream output;

    public MyResponse(OutputStream output, MyRequest request) {
        this.output = output;
        this.request = request;
    }

    /**
     * 发送静态资源
     * @throws IOException
     */
    public void sendStaticResource() {
        try {
            File file = new File(HttpServer.WEB_ROOT + request.getUri());//目录+文件名
            if (file.exists() && file.isFile() ) {
                output.write(responseToByte(HttpStatusEnum.OK));
                writer(file);
            } else {
                //如果资源不存在
                file = new File(HttpServer.WEB_ROOT + "/404.html");
                output.write(responseToByte(HttpStatusEnum.NOT_FOUND));
                writer(file);
            }
        } catch (Exception e) {
            System.out.println("访问失败的原因是：" + e.toString());
        }
    }

    private byte[] responseToByte(HttpStatusEnum status) {
        return new StringBuilder().append("HTTP/1.1").append(" ")
                .append(status.getStatus()).append(" ")
                .append(status.getDesc()).append("\r\n\r\n")
                .toString().getBytes();
    }

    private void writer(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(bytes, 0, BUFFER_SIZE)) != -1) {
                output.write(bytes, 0, read);
            }
        }
    }


}
