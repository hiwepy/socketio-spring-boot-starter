# socketio-spring-boot-starter


#### 组件简介

> 基于 [netty-socketio 2.x](https://github.com/mrniko/netty-socketio) 开源项目实现的Socket项目整合

#### 使用说明

##### 1、Spring Boot 项目添加 Maven 依赖

``` xml
<dependency>
	<groupId>com.github.hiwepy</groupId>
	<artifactId>socketio-spring-boot-starter</artifactId>
	<version>${project.version}</version>
</dependency>
```

##### 2、在`application.yml`文件中增加如下配置

```yaml
#################################################################################################
### SocketIO 配置：
#################################################################################################
socketio:
  redis:
    redisson:
      enabled: true
      server: single
      single:
        address: redis://192.168.2.237:6379
        password: redis
        client-name: redis
        connection-minimum-idle-size: 5
        connection-pool-size: 50
    template:
      enabled: false
  # 服务端配置
  server:
    enabled: true
    ## host在本地测试可以设置为localhost或者本机IP，在Linux服务器跑可换成服务器IP
    hostname: 192.168.2.222
    ## netty启动端口
    port: 10065
    ## 添加头部版本信息
    add-version-header: true
    ## Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
    ping-interval: 25000
    ## Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
    ping-timeout: 60000
    ## 设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
    max-frame-payload-length: 1048576
    ## 设置http交互最大内容长度
    max-http-content-length: 1048576
    ## socket连接数大小（如只监听一个端口boss线程组为1即可）
    boss-threads: 1
    transports:
      - polling
      - websocket
    worker-threads: 100
    ## 协议升级超时时间（毫秒），默认10秒。HTTP握手升级为ws协议超时时间
    upgrade-timeout: 15000
    ## socket配置
    socket-config:
      reuse-address: true
      tcp-no-delay: true
      so-linger: 0
    ack-mode: auto
    allow-custom-requests: true
    ## sessionID 通过请求头io来获取
    random-session: false
```

##### 3、前端示例

```html
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Socketio chat</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.0/jquery.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="https://cdn.bootcdn.net/ajax/libs/socket.io/2.4.0/socket.io.min.js"></script>
</head>
<body>
    <h1>Netty-socketio chat demo</h1>
    <br />
    <div id="console" class="well"></div>
    <form class="well form-inline" onsubmit="return false;">
        <input id="from" class="input-xlarge" type="text" placeholder="from. . . " />
        <input id="to" class="input-xlarge" type="text" placeholder="to. . . " />
        <input id="content" class="input-xlarge" type="text" placeholder="content. . . " />
        <button type="button" onClick="sendMessage()" class="btn">Send</button>
        <button type="button" onClick="sendDisconnect()" class="btn">Disconnect</button>
    </form>
</body>
<script type="text/javascript">

    var socket = io.connect('ws://localhost:10065?userId=12121212',{	  
	  path: "/socket.io"
	});	 

    socket.on('connect',function() {
	console.log(111111);
        output('<span class="connect-msg">Client has connected to the server!</span>');
    });
    socket.on('message', function(data) {
        output('<span class="username-msg">' + data.from + "对你说：" + data.content + '</span>');
    });
    socket.on('disconnect',function() {
        output('<span class="disconnect-msg">The client has disconnected! </span>');
    });
    function sendDisconnect() {
        socket.disconnect();
    }
    function sendMessage() {
        var from = $("#from").val();
        var to = $("#to").val();
        var content = $('#content').val();
        socket.emit('message', {
            from : from,
            to : to,
            content : content
        });
    }
    function output(message) {
        var currentTime = "<span class='time' >" + new Date() + "</span>";
        var element = $("<div>" + currentTime + " " + message + "</div>");
        $('#console').prepend(element);
    }
</script>
<style>
body {
    padding: 20px;
}
#console {
    height: 400px;
    overflow: auto;
    border: 1px solid #ccc;
}
.username-msg {
    color: orange;
}
.connect-msg {
    color: green;
}
.disconnect-msg {
    color: red;
}
.send-msg {
    color: #888
}
</style>
</html>
```

##### 3、如果通过Nginx进行服务负载，需要添加如下配置：

```json
	location ~ /socket.io {   

       	proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_buffering off;

        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; 
        proxy_set_header X-Forwarded-Port $server_port;
        proxy_set_header X-Nginx-Proxy true;

        proxy_connect_timeout 60s;
        proxy_send_timeout 600s;
        proxy_read_timeout 600s;
        send_timeout 600s;

        proxy_pass http://122.224.247.131:10065;
    }
```

##### 4、如果你的项目使用了uni-app进行移动端开发，需要进行接口对接，可参考如下代码进行

```javascript
var socketTask = uni.connectSocket({
	url: 'wss://{your-domain}/socket.io/?transport=websocket',
	header: {
		'X-Authorization'	: token,
		'io'				: 'test'
	},
	success: (res)=> {
		console.log(res);	
	},
	fail: (res)=> {
		console.log(res);	
	},
	complete: ()=> {
	}
});
uni.onSocketOpen(function (res) {
	console.log('WebSocket连接已打开！');		  
});
uni.onSocketError(function (res) {
	console.log(res,'WebSocket连接打开失败，请检查！');
});
uni.onSocketMessage(function (res) {
	console.log('收到服务器内容：' + res.data);
});
uni.onSocketClose(function (res) {
	console.log(res, 'WebSocket 已关闭！');
});
```

#### 代码示例

[https://github.com/hiwepy/spring-boot-starter-samples/tree/master/spring-boot-sample-socketio](https://github.com/hiwepy/spring-boot-starter-samples/tree/master/spring-boot-sample-socketio "spring-boot-sample-socketio")

## Jeebiz 技术社区

Jeebiz 技术社区 **微信公共号**、**小程序**，欢迎关注反馈意见和一起交流，关注公众号回复「Jeebiz」拉你入群。

|公共号|小程序|
|---|---|
| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/qrcode_for_gh_1d965ea2dfd1_344.jpg)| ![](https://raw.githubusercontent.com/hiwepy/static/main/images/gh_09d7d00da63e_344.jpg)|
