# springboot-netty

springboot + netty的一个demo实现网络协议自定义解析，可以实现多种网络请求

## 启动步骤

先启动Server，在启动Client，项目端口application.yml的记得替换

### Server

```
AppBootstrap中的init方法中的 new MyServer().run();
注释new MyClient().run();
```

### Client

```
AppBootstrap中的init方法中的 new MyClient().run();
注释new MyServer().run();
```
