import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;

/**
 * 相当于一个Tomcat
 */
public class HttpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    public static final String WEB_ROOT;
    static {
        URL webrootURL = HttpServer.class.getClassLoader().getResource("webRoot");
        WEB_ROOT = Optional.ofNullable(webrootURL).orElseThrow(
                () -> new IllegalStateException("无法找到静态目录") ).getFile().substring(1);
    }
    // 关闭
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    // 是否接收到关闭指令
    // transient关键字用来序列化，shutdown需要在网络传输，所以要序列化
    private transient boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    private void await() {
        ServerSocket serverSocket = null;
        try {
            /*
             * InetAddress:表示互联网协议（IP）地址
             * InetAddress.getByName("www.163.com"):
             *      --> 在给定主机名的情况下确定主机的IP地址
             *      --> 如果参数为null,获得的是本机的IP地址
             */
            LOGGER.info("IP地址: " + InetAddress.getByName(""));// IP地址: localhost/127.0.0.1
            /*
             * 第二个参数是backlog参数，用来显式设置连接请求队列的长度，它将覆盖操作系统限定的队列的最大长度
             */
            serverSocket = new ServerSocket(8081, 1, InetAddress.getByName("127.0.0.1"));
            LOGGER.info("服务器正在启动，监听的端口号是：{}", 8081);
        } catch (IOException e) {
            LOGGER.error("服务器因异常关闭！！！", e);
            System.exit(1);
        }

        while (!shutdown) {
            try (Socket socket = serverSocket.accept();
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream() ) {
                /*
                 * accept()接收到客户端的套接字内容后，生成并返回Socket对象。
                 * 然后调用getInputStream()方法和getOutputStream()方法生成InputStream和FileOutputStream对象。
                 */

                // 创建请求的对象和参数
                MyRequest request = new MyRequest(input);
                request.parse();
                // 创建响应对象
                MyResponse response = new MyResponse(output, request);
                response.sendStaticResource();
                // 如果本次请求是关闭服务器则修改标识为关闭
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                LOGGER.warn("用户请求捕获异常：",e);
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.error("关闭服务器套接字失败！！！", e);
        }
    }
}
