Spring Cloud微服务

## 1.Spring Coud

 Spring Cloud是一系列框架的有序集合。它利用[Spring Boot](https://baike.baidu.com/item/Spring Boot/20249767)的开发便利性巧妙地简化了分布式系统基础设施的开发，如服务发现注册、配置中心、消息总线、负载均衡、断路器、数据监控等 

## 2.Eureka注册中心

Eureka作为Spring Cloud 默认注册中心组件，整体分为两个主体：Eureka Sever和Eureka Client.

 1、Eureka Server是服务端，负责管理各各微服务结点的信息 

 2、Eureka Client是客户端，将服务注册到注册中心

 3、微服务需要调用另一个微服务时从Eureka Server中获取服务调用地址，进行远程调用

- eureka的server端依赖

  ```pom
  <properties>
          <java.version>1.8</java.version>
          <spring-cloud.version>Greenwich.SR6</spring-cloud.version>
      </properties>
  <!--springcloud依赖-->
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-dependencies</artifactId>
                  <version>${spring-cloud.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
      <!--eureka-server依赖-->
      <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
          </dependency>
  ```

  

- eureka的client端依赖

  ```pom
   <!--eureka客户端依赖-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
  ```

- eureka使用的注解，在主启动类上添加，server端使用 @EnableEurekaServer ，client端使用@EnableEurekaClient 开启

-  server端的application.yml的配置内容 ,如下，eureka启动地址：http://localhost:7001

  ```java
  server:
    port: 7001
  
  eureka:
    instance:
      hostname: localhost #eureka服务端的实例名称
    client:
      register-with-eureka: false     #false表示不向注册中心注册自己。
      fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
      service-url:
        defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       #设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址（单机）。
        #defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
  ```

- client端的application.yml配置内容

  ```java
  eureka:
    client: #客户端注册进eureka服务列表内
      service-url:
        defaultZone: http://localhost:7001/eureka
        #defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
    instance:
      instance-id: microservice-provider-hystrix
      prefer-ip-address: true     #访问路径可以显示IP地址
  
  info:
    app.name: microservice-provider
    company.name: www.atguigu.com
    build.artifactId: $project.artifactId$
    build.version: $project.version$
  
  ```



## 3.Ribbon负载均衡

Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用

Ribbon本身自带有7中负载均衡策略

>  RandomRule		随机策略
>
>  RoundRobinRule 	轮询策略
>
>  RetryRule 		重试 策略
>
>  BestAvailableRule 	最低并发策略
>
>  AvailabilityFilteringRule 	可用过滤策略
>
>  ResponseTimeWeightedRule 	相应时间加权重策略
>
>  ZoneAvoidanceRule 		区域权重策略

- 引入Ribbon依赖

  ```pom
   <!-- Ribbon相关 -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-ribbon</artifactId>
          </dependency>
  ```

  

- 配置内容,需要通过使用RestTemplate，使用注解@LoadBalanced

  ```java
  @Configuration
  public class ConfigBean {
  
      @Bean
      @LoadBalanced//Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端  负载均衡的工具。
      public RestTemplate getRestTemplate() {
          return new RestTemplate();
      }
    
  }
  ```

- 更换负载均衡策略

  ```java
  	@Bean
      public IRule getIRule(){
          //return new RoundRobinRule();
          return new RandomRule();//达到的目的，用我们重新选择的随机算法替代默认的轮询。
          //return new RetryRule();
      }
  ```

- 自定义负载均衡策略

  1. 继承抽象类AbstractLoadBalancerRule 
  2. 自定义的策略类 不能放在@ComponentScan所扫描的当前包和子包下
  3. 主启动类上添加@@RibbonClient(value = "微服务名",configuration = {自定义类.class}

- RestTemplate使用

  ```java
  @RestController
  public class ConsumerController {
      //private static final String REST_PREFIX = "http://localhost:8001";
      private static final String REST_PREFIX = "http://MICROSERVICE-PROVIDER";//使用ribbon后 通过eureka注册中心查找发布的微服务
  
      @Autowired
      private RestTemplate restTemplate;
  
      @RequestMapping("/consumer/list")
      public List<Dept> list() {
          return restTemplate.getForObject(REST_PREFIX + "/list", List.class);
      }
  
      @RequestMapping("/consumer/add")
      public boolean add(Dept dept) {
          System.out.println("------" + dept);
          return restTemplate.postForObject(REST_PREFIX + "/add", dept, boolean.class);
      }
  
      @RequestMapping("/consumer/find/{id}")
      public Dept find(@PathVariable("id") Long id) {
          return restTemplate.getForObject(REST_PREFIX + "/find/" + id, Dept.class);
      }
  }
  ```

  

## 4.Feign负载均衡

Feign是一个http请求调用的轻量级框架，可以以Java接口注解的方式调用Http请求，而不用像Java中通过封装HTTP请求报文的方式直接调用 ， 在使用feign 时，会定义对应的接口类，在接口类上使用Http相关的注解，标识HTTP请求参数信息 

如下所示，接口上需要使用@FeignClient注解,在启动类上加注解@EnableFeignClients(basePackages= {"接口所在的包"})

```java
@FeignClient(value = "MICROSERVICE-PROVIDER",fallbackFactory = ClientFallBackFactory.class)
//@FeignClient(value = "MICROSERVICE-PROVIDER")
public interface DeptService {
    @RequestMapping("/list")
    public List<Dept> list() ;

    @RequestMapping("/find/{id}")
    public Dept find(@PathVariable("id") Long id);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean add(Dept dept);
}
```

- 导入Feign依赖

  ```java
  <!-- feign客户端依赖 -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-openfeign</artifactId>
          </dependency>
  ```

- controller层直接调用接口

  ```java
  @RestController
  public class ConsumerFeignController {
      @Autowired
      private DeptService deptService;
  
      @RequestMapping("/feign/list")
      public List<Dept> list() {
          return deptService.list();
      }
  
      @RequestMapping("/feign/find/{id}")
      public Dept find(@PathVariable("id") Long id) {
          return deptService.find(id);
      }
  }
  ```

## 5.Hystrix断路器

hystrix可以解决雪崩效应问题，它提供了资源隔离、降级机制、融断、缓存等功能 

熔断器原理

\- 开始时断路器处于关闭状态(Closed)。

\- 如果调用持续出错、超时或失败率超过一定限制，断路器打开进入熔断状态，后续一段时间内的所有请求都会被直接拒绝。

\- 一段时间以后，保护器会尝试进入半熔断状态(Half-Open)，允许少量请求进来尝试；如果调用仍然失败，则回到熔断状态，如果调用成功，则回到电路闭合状态

![](./springcloud/2019060635.png)

- 导入Hystrix依赖

  ```java
   <!--hystrix断路器依赖-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
          <dependency>
              <groupId>com.netflix.hystrix</groupId>
              <artifactId>hystrix-javanica</artifactId>
          </dependency>
          <!--断路器仪表盘依赖-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-netflix-hystrix-dashboard</artifactId>
          </dependency>
       <!--监控-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
  ```

- application.yml配置内容

  ```java
  #客户端开启hystrix断路器
  feign:
    hystrix:
      enabled: true
  ```

- hystrix使用需在启动类上添加注解@EnableHystrix后可在固定方法上使用@HystrixCommand注解

  ```java
  	@RequestMapping("/find/{id}")
      @HystrixCommand(fallbackMethod = "fallBack_find")//熔断  发生异常时执行fallBack_find方法
      public Dept find(@PathVariable("id") Long id) {
          Dept dept = deptService.find(id);
          if(dept == null){
              throw new RuntimeException("查询异常！");
          }
          return dept;
      }
   	public Dept fallBack_find(@PathVariable("id") Long id){
          Dept dept = new Dept();
          dept.setDeptNo(id);
          dept.setDeptName("程序异常或中断！！");
          dept.setDbSource("未查到任务数据！");
          return dept;
      }
  ```

- hystrix的注解@EnableHystrixDashboard开启熔断监控仪表盘；@EnableCircuitBreaker--开启断路器，开启监控后，通过访问http://hostname:port/hystrix查看 ，通过 http://localhost:port/hystrix.stream查看，需先注册HystrixMetricsStreamServlet类

  ```java
  @Configuration
  public class configBean {
  
      /**
       * 注册 hystrix-dashboard仪表盘地址
       * @return
       */
      @Bean
      public ServletRegistrationBean getServlet() {
          HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
          ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
          registrationBean.setLoadOnStartup(1);
          registrationBean.addUrlMappings("/hystrix.stream");
          registrationBean.setName("HystrixMetricsStreamServlet");
          return registrationBean;
      }
  }
  ```

- fallback流程--降级，配置application.yml开启hystrix,在feign接口上的@FeignClient注解中添加fallbackFactory指定降级的类，该类实现了FallbackFactory

  ```java
  @FeignClient(value = "MICROSERVICE-PROVIDER",fallbackFactory = ClientFallBackFactory.class)
  ```

  ```
  #客户端开启hystrix断路器
  feign:
    hystrix:
      enabled: true
  ```

  ```java
  @Component  //**必须要将该类加入到容器中管理
  public class ClientFallBackFactory implements FallbackFactory {
      public DeptService create(Throwable throwable) {
          /**
           * 当服务端停掉服务后 则会执行这里
           */
          return new DeptService() {
              public List<Dept> list() {
                  ArrayList<Dept> list = new ArrayList<Dept>();
                  Dept dept = new Dept();
                  dept.setDeptNo(11l);
                  dept.setDeptName("服务器宕机！暂停服务！");
                  list.add(dept);
                  return list;
              }
  
              public Dept find(Long id) {//需要处理的方法
                  Dept dept = new Dept();
                  dept.setDeptNo(id);
                  dept.setDeptName("服务器宕机！暂停服务！");
                  return dept;
              }
          };
      }
  }
  ```

## 6.Zuul路由网关

 Zuul是Spring Cloud中的微服务API网关，作为一个边界性质的应用程序，Zuul提供了动态路由、监控、弹性负载和安全功能 

- 引入依赖

  ```java
   <!-- zuul路由网关 -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
          </dependency>
  ```

- application.yml配置文件

  ```java
  server:
    port: 9002
  
  spring:
    application:
      name: microservice-zuul
  
  eureka:
    client:
      service-url:
        #defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka,http://eureka7003.com:7003/eureka
        defaultZone: http://localhost:7001/eureka
    instance:
      instance-id: microservice-zuul #zuul注册到eureka的ID
      prefer-ip-address: true #显示ip地址
  
  
  zuul:
    prefix: /api #请求地址前缀  如http://localhost:9002/api/mydept/find/1
    ignored-services: "*" #旧名称忽略 不可用 如原可以用地址：http://localhost:9002/api/microservice-provider/find/1
    routes:
      mydept.serviceId: microservice-provider #映射注册的微服务应用名称
      mydept.path: /mydept/** #代理的地址
  
  #点击eureka的id后显示的信息
  info:
    app.name: atguigu-microcloud
    company.name: www.atguigu.com
    build.artifactId: $project.artifactId$
    build.version: $project.version$
  ```

- 在入口程序开启注解@EnableZuulProxy

## 7.Spring Cloud Config配置中心

 采用 「Server 服务端」和 「Client 客户端」的方式来提供可扩展的配置服务。服务端提供配置文件的存储，以接口的形式将配置文件的内容提供出去；客户端通过接口获取数据、并依据此数据初始化自己的应用 

 配置中心负责管理所有服务的各种环境配置文件

- 引入依赖

  ```java
  <!--Spring Cloud Config 服务端依赖-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-config-server</artifactId>
          </dependency>
      <!--Spring Cloud Config 客户端依赖-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-config-client</artifactId>
              <version>2.1.8.RELEASE</version>
          </dependency>
  ```

- application.yml配置文件

  ```java
  #服务端配置文件
  server:
    port: 3344 #测试 http://localhost:3344/application-dev.yml
  
  spring:
    application:
      name: microservice-config-server
    cloud:
      config:
        server:
          git:
            uri: https://github.com/lzgblog/springcloud-application-config.git #GitHub上面的git仓库名字
  ```

  ```java
  #客户端配置文件  创建bootstrap.yml文件
  #访问地址  http://localhost:8201/config
  spring:
    cloud:
      config:
        name: microservicecloud-config-client #需要从github上读取的资源名称，注意没有yml后缀名
        profile: dev   #本次访问的配置项  dev--8201端口
        label: master   
        uri: http://localhost:3344  #本微服务启动后先去找3344号服务，通过SpringCloudConfig获取GitHub的服务地址
  
  ```

- 在入口配置注解，服务端使用@EnableConfigServer



## 8.相关资料

******注意SpringBoot对应SpringCloud版本选型**

具体参考资料： 

https://www.cnblogs.com/y3blogs/p/13263717.html 

https://spring.io/projects/spring-cloud 

https://www.springcloud.cc/spring-cloud-netflix.html 

https://www.springcloud.cc/spring-cloud-dalston.html 

https://www.springcloud.cc/ 