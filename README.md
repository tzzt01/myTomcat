# myTomcat
 《深入剖析Tomcat》的源码无法运行，因此使用jdk8.0尝试开发。<br/>
## 期间遇到的问题：
#### 1. 响应页面一直无法显示内容<br>
 问题出在
 MyResponse.java的responseToByte(HttpStatusEnum)方法。
 原因是最后一个请求头之后是必须有一个空行，发送回车符和换行符，
 通知服务器以下不再有请求头，而我没有添加空行导致页面无法加载。
