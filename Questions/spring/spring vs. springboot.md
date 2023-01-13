----
SPRING 과 SPRING boot 환경에 차이점에 대해 설명해 보려고 한다.

[Spring 의 개요] 
먼저 스프링은 무엇인가 부터 알아봐야 한다. 
스프링은 개발자에게 봄이 찾아 왔다 해서 스프링이라고 할 정도로 개발자들에게 많은 편리함을 제공하고 있다. 
먼저, 스프링의 정의에 대해서 알아 보겠다. 

"자바 엔터프라이즈 개발을 편하게 해주는 오픈소스 경량급 애플리케이션 프레임워크"

여기서 중요한 것은 스프링은 애플리케이션의 전 영역을 포괄하는 범용적인 프레임워크이며 애플리케이션 개발의 과정을 편하고 빠르고 효율적일수 있도록 목표를 두는 프레임 워크이다. 
애플리케이션 전 계층과 영역에 핵심 기술의 프로그래밍 모델을 적용해 애플리케이션 개발을 편리하게 해주는 기능들을 제공하는 것이다.

참고로 여기서 경량급이라는 것은 프로그램이 가볍다거나 함축적이라는 의미가 아니다. 스프링은 불필요하게 무겁지 않다는 의미이다. 가볍고 단순한 환경속에서도 EJB 와 고가의 WAS 에서 가능했던 애플리케이션 개발의 고급 기술을 사용할 수 있게 해준다는 것에 경량이라는 단어가 붙은 것이다. 스프링은 불필요한 프레임워크와 서버 환경의 의존적인 부분을 제거하였다.

이 모든 환경들은 스프링의 엔터프라이즈 개발을 편하게 해주는 해결책이다. 그러므로 자바의 목적인 객체지향 프로그래밍을 통해 유연하고 확장성 좋은 애플리케이션 개발과 합쳐 졌을때 빛을 바래는 이유이자 개발을 할때도 이 것들을 누리며 개발하려고 노력해야 한다.

스프링은 ==POJO로 개발할 수 있게 하는 기능기술 (enabling technology)를 제공하고 그의 주요 기술은 IoC/DI, AOP 그리고 PSA(Portable Server Abstraction)==이 있다. 하지만 이번 글에서는 스프링의 이러한 특징을 설명하기 보다는 스프링과 스프링 부트의 환경적인 차이점에 대해서 설명해 보고 싶다.

[Spring 의 특징] 
스프링은 약 20개의 모듈로 나누어져 있어 개발자에게 엔터프라이즈 기능들을 제공하고 있다. 대중적인 모듈로는 아래 항목들을 제공 한다. 
	• Spring JDBC
	• Spring MVC 
	• Spring AOP 
	• Spring ORM 
	• Spring Test 
스프링 프레임워크는 ==웹 어플리케이션 개발시 결합도를 낮추는 방향의 개발 방법==을 제공 한다.

[Spring boot 는 왜 필요해 졌을까?] 
스프링으로 개발하며 `Transaction Manager, Hibernate Datasource, Entity Manager, Session Factory` 와 같은 설정에 대한 어려움이 많았습니다. 스프링의 궁국적인 목적인 비즈니스 개발에 집중할수 있도록 해주려는 것과 달리 Spring MVC 를 사용하며 기본 프로젝트 세팅하는 것에 시간이 많이 소요되기 시작하여 스프링 부트 부터는 미리 설정된 starter project 를 제공하며 `AutoConfiguration` 을 이용하여 모든 내부 디펜던시를 관리하도록 설정해 두었습니다.

그러면 둘의 본격적인 차이점에 대해서 알아 보겠습니다.

✔️ 스프링부트 부터는 ==Embeded Tomcat== 을 사용하므로 매번 버전 관리 또는 설치의 어려움이 없다. 스프링과 같은 경우에는 톰캣을 설치해

✔️ 스타터 프로젝트를 통한 Dependecy 자동화  
아마 Spring 유저들이 가장 열광한 기능이 아닐까 싶다. 과거 Spring framework에서는 각각의 dependency들의 호환되는 버전을 일일이 맞추어 주어야 했고, 때문에 하나의 버전을 올리고자 하면 다른 dependeny에 까지 영향을 미쳐 version 관리에 어려움이 많았다. 하지만, 이제 starter가 대부분의 dependency를 관리해주기 때문에 이러한 걱정을 많이 덜게 되었다.

✔️ Spring boot 의 보안 
Spring 에서는 보안을 활성화하기 위해 `spring-security-web/ spring security-config` 종속성을 모두 필요로 했다. Spring Boot 의 경우에는 모든 종속성이 클래스 경로에 저동으로 추가하기 때문에 spring-boot-starter-security 만 종속성으로 연결해 주면 가능하다.

[pom.xml]  - spring-boot 
```
<dependency> 
	<groupId> org.springframework.boot </groupId>
	<artifactId> spring-boot-starter-security </artifactId>
</dependency>
```



[pom.xml]   - spring
```
<spring.security.version>5.0.4.RELEASE</spring.security.version>
<!--SpringSecurityVersion,SpringMVC버전을SpringSecurity의MVCDependency기준으로맞출것.-->
<dependency>

<groupId>org.springframework.security</groupId>
<artifactId>spring-security-taglibs</artifactId>
<version>${spring.security.version}</version>
</dependency>
```

✔️ XML설정을 하지 않아도 된다. 
	• Web.xml, dispatcher-servlet.xml   
✔️  jar file을 이용해 자바 옵션만으로 손쉽게 배포가 가능하다. 
	Spring Actuaor를 이용한 애플리케이션의 모니터링과 관리를 제공한다.

위와 같은 장점으로 스프링 부트를 통해 개발자들에게 비즈니스 로직 개발에만 집중할수 있고 환경 설정에 대한 고민을 줄일수 있는 환경을 제공한다는 대에 있어 스프링 부트는 스프링보다 널리 사용되고 있다.

이 글을 작성하며 참고하게 된 자료가 있는데 스프링에 대해 아주 자세하게 작성된 글이라 이글을 읽고 스프링에 대한 더 자세한 내용들이 궁금해졌다면 아래 블로그를 참고하길 바란다. 

출처: [https://12bme.tistory.com/157](https://12bme.tistory.com/157) [길은 가면, 뒤에 있다.:티스토리]