# myTomcat
 《深入剖析Tomcat》的源码无法运行，因此使用jdk8.0尝试开发。
期间遇到的第一个问题是响应页面一直无法显示内容。经过调查发现问题出在MyResponse.java的responseToByte(HttpStatusEnum)方法。原因是<b>最后一个请求头之后是必须有一个空行，发送回车符和换行符，通知服务器以下不再有请求头</b>，而我没有添加空行导致页面无法加载。
