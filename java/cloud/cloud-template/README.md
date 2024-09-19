# springboot-template

springboot 模板项目，定义一个项目需要有哪些目录结构，以及它的作用，定义好规范

- com-haohaodayouxi-demo：项目名称
    - aspect：切面类
    - config：配置类
    - constants：常量类
    - controller：控制器
    - event：事件类
    - jobs：定时任务
    - listen：监听器
    - mapper：数据库方法
    - model：实体类
        - bo: 业务对象
        - db: 数据库实体类
        - req: 请求对象
        - res: 响应对象
    - repository：数据库操作类，作用service和mapper中间层，可以做缓存，</br>
      如：用户所有信息对象包含（用户信息、身份信息两种信息，service层获取repository层的缓存封装一个大的缓存，具体根据项目体量判断是否需要此层）
        - impl：数据库操作实现类
        - redis：redis缓存类
    - service：业务逻辑层
        - impl：业务逻辑实现类
        - redis：redis缓存类
        - sync：异步业务类
    - utils：工具类
- resources：项目资源文件
    - mapper：数据库映射文件
    - META-INF：项目配置文件
        - spring.factories：集中管理自动配置类和其他扩展点，用于自动化组件装配。通过 SPI 机制注册自动配置类、后处理器等
    - application.yml：项目配置文件
    - application-dev.yml：开发环境配置文件
    - application-prod.yml：生产环境配置文件
    - application-test.yml：测试环境配置文件
- pom：项目依赖管理
- Readme：项目说明

----
