
[[servlet filter vs. spring interceptor 란]]

[정리]
- filter 
- interceptor (feat AOT)
- filter vs. interceptor 용도 및 차이 
- 예외 처리 경우 

### | Filter 
*  J2EE 표준 스펙 기능으로 디스패처 서블릿(Dispatcher Servlet)에 요청이 전달되기 전/후에 url 패턴에 맞는 모든 요청에 대해 부가작업을 처리할 수 있는 기능을 제공한다.
![[Pasted image 20230302093436.png]]

* 응답과 요청을 거른 뒤 정제하는 역할
* Dispatcher Servlet 이전에 실행 되는데 필터가 동작하도록된 자원의 앞단에서 요청 내용을 변경하거나 체크 수행 가능 
* web.xml 에 등록 가능 
	* ex. Encoding, CorsFilter, springSecurityFilterChain, springSessionRepositoryFilter
	* 인코딩 변환 처리나 XSS 방어 등의 요청에 대한 처리 
	
```java
<filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encoding</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

* 스프링 컨테이너가 아닌 톰캣과 같은 웹 컨테이너에 의해 관리가 되는 것이고(스프링 빈으로 등록은 된다), 디스패처 서블릿 전/후에 처리하는 것


> Filter Class 

* 필터를 추가하기 위해서는 javax.servlet의 Filter 인터페이스를 구현(implements) : 
	*    init 메소드
	-   doFilter 메소드
	-   destroy 메소드

```java
public interface Filter { 
	// 필터 객체를 초기화 하고 서비스 추가하기 위한 메소드
	//웹 컨테이너가 1회 init 메소드를 호출하여 필터 객체를 초기화하면 이후의 요청들은 doFilter를 통해 처리된다.
	public default void init(FilterConfig filterConfig) throws ServletException {
	} 
	// 처리를 구현해서 동작 받은 후 FilterChain 파라미터 이용해서 다음 대상으로 요청 넘겨주기 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException; 

    // 필터를 제거하고 자원을 반환하기 위한 메소드 
	public default void destroy() {} 
}
```


### | Interceptor 

Spring이 제공하는 기술로써, 디스패처 서블릿(Dispatcher Servlet)이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 기능을 제공한다. 즉, 웹 컨테이너에서 동작하는 필터와 달리 인터셉터는 스프링 컨텍스트에서 동작을 하는 것이다.

![[Pasted image 20230126160905.png]]

J2EE 표준 스펙인 필터(Filter)와 달리 Spring이 제공하는 기술로써, 디스패처 서블릿(Dispatcher Servlet)이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 기능을 제공한다.
내부적인 `핸들러 매핑`을 통해 적절한 컨트롤러를 찾도록 요청하는데 결과로 `HandlerExecutionChain`을 반환한다. 

인터셉터를 추가하기 위해서는 org.springframework.web.servlet의 HandlerInterceptor 인터페이스를 구현(implements) : 
-   preHandle 메소드
-   postHandle 메소드
-   afterCompletion 메소드

```java
public interface HandlerInterceptor 

	// controller 호출 전 실행 / 요청 데이터 전처리, 가공에 사용
	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception { 
			return true;
	} 
	// 컨트롤러 호출 후 실행 / 후작업 처리시 사용
	default void postHandle(HttpServletRequest request, HttpServletResponse response, 
		Object handler, @Nullable ModelAndView modelAndView) throws Exception {} 

	// 뷰 생성을 포함한 모든 작업이 완료 후에 실행  
	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, @Nullable Exception ex) throws Exception { } 
}
```

* 예시 : 코드 동작 시간 초과 Email 전송 
* 스프링의 모든 빈 객체에 접근할 수 있다. 
* 여러개 사용 가능하며, 로그인 체크, 권한체크, 프로그램 실행시간 계산 작업 로그 확인 등의 업무를 처리. 


##### ✅ AOP

* AOP의 Advice와 HandlerInterceptor의 가장 큰 차이는 파라미터의 차이다.
* AOP의 포인트컷
	* @Before: 대상 메서드의 수행 전
	* @After: 대상 메서드의 수행 후
	* @After-returning: 대상 메서드의 정상적인 수행 후
	* @After-throwing: 예외발생 후
	* @Around: 대상 메서드의 수행 전/후

> 인터셉터(Interceptor)와 AOP의 비교 

인터셉터 대신에 컨트롤러들에 적용할 부가기능을 어드바이스로 만들어 AOP(Aspect Oriented Programming, 관점 지향 프로그래밍)를 적용할 수도 있다. 하지만 다음과 같은 이유들로 컨트롤러의 호출 과정에 적용되는 부가기능들은 인터셉터를 사용하는 편이 낫다.

1.  컨트롤러는 타입과 실행 메소드가 모두 제각각이라 포인트컷(적용할 메소드 선별)의 작성이 어렵다.
2.  컨트롤러는 파라미터나 리턴 값이 일정하지 않다.
3.  AOP에서는 HttpServletRequest/Response를 객체를 얻기 어렵지만 인터셉터에서는 파라미터로 넘어온다.

### | Filter vs. Interceptor 용도 및 차이 

##### 📌 비교 
필터와 인터셉터는 각각이 관리되는 컨테이너와 Request/Response의 조작가능 여부가 다르고, 그에 따라 용도가 다르다.
| 대상                           | Filter                               | Interceptor     |
| ------------------------------ | ------------------------------------ | --------------- |
| 관리 컨테이너                  | 웹 컨테이너                          | 스프링 컨테이너 |
| Request/Response <br>조작 여부 | O                                    | x               |
| 용도                           | - 공통된 보안 및 인증/인가 관련 작업 <br> - 모든 요청에 대한 로깅 또는 감사 <br> - 이미지/데이터 압축 및 문자열 인코딩 <br> - spring 과 분리되어야 하는 기능  |     - 세부적인 보안 및 인증/인가 공통 작업 <br> - API 호출에 대한 로깅 또는 감사 <br> - Controller 로 넘겨주는 정보(데이터) 가공                            |                                      |                 |

위의 표에 적힌 내용들 중에서 각각이 사용되는 용도에 대해서는 자세히 살펴보도록 하자. 일부에서 필터(Filter)가 스프링 빈으로 등록되지 못하며, 빈을 주입 받을 수도 없다고 하는데, 이는 잘못된 설명이다. 이는 매우 옛날의 이야기이며, 필터는 현재 스프링 빈으로 등록이 가능하며, 다른 곳에 주입되거나 다른 빈을 주입받을 수도 있다. 이와 관련해서는 [다음 포스팅](https://mangkyu.tistory.com/221)에서 자세히 살펴보도록 하자.

### | 예외 처리 경우 

Filter는 DispatcherServlet 외부에 존재하기 때문에 예외가 발생했을 때 ErrorController에서 처리해야 합니다. 

하지만 Interceptor는 DispatcherServlet 내부에 존재하기 때문에 @ControllerAdvice를 적용해서 처리할 수 있습니다.
Interceptor의 경우 AOP를 흉내내거나, Spring 애플리케이션에서 전역적으로 전후처리 로직에서 예외를 사용하도록 하거나, Handler Method에서 사용자의 권한을 체크해서 다른 동작을 시켜준다거나 할 때 사용합니다.

동작 대상이 다르기 때문에 필터는 예외가 발생하면 Web Application에서 처리해야 합니다.  
선언이나 필터 내에서 request.getRequestDispatcher(String)과 같이  
예외 처리 할 수 있습니다.  
반면 인터셉터는 스프링의 ServletDispatcher내부에 있으므로 @ConstrollerAdvice에서 @ExceptionHandler를 사용해 예외처리 할 수 있습니다.

----
[참고]
https://mangkyu.tistory.com/173
https://meetup.nhncloud.com/posts/151
https://velog.io/@leemin-jae/%EB%B0%B1%EC%97%94%EB%93%9C-%EB%A9%B4%EC%A0%91-%EC%A4%80%EB%B9%84-%EC%9A%B4%EC%98%81%EC%B2%B4%EC%A0%9C-dnn2spz7

[질문 리스트]
https://github.com/ksundong/backend-interview-question/blob/master/README.md
https://land-turtler.tistory.com/154




----
![[Pasted image 20230126160905.png]]



##### 📌 비교 

| 대상                           | Filter                               | Interceptor     |
| ------------------------------ | ------------------------------------ | --------------- |
| 관리 컨테이너                  | 웹 컨테이너                          | 스프링 컨테이너 |
| Request/Response <br>조작 여부 | O                                    | x               |
| 용도                           | - 공통된 보안 및 인증/인가 관련 작업 <br> - 모든 요청에 대한 로깅 또는 감사 <br> - 이미지/데이터 압축 및 문자열 인코딩 <br> - spring 과 분리되어야 하는 기능  |     - 세부적인 보안 및 인증/인가 공통 작업 <br> - API 호출에 대한 로깅 또는 감사 <br> - Controller 로 넘겨주는 정보(데이터) 가공                            |                                      |                 |


##### ✅ Filter 
* 응답과 요청을 거른 뒤 정제하는 역할
* Dispatcher Servlet 이전에 실행 되는데 필터가 동작하도록된 자원의 앞단에서 요청 내용을 변경하거나 체크 수행 가능 
* web.xml 에 등록 가능 
	* ex. Encoding, CorsFilter, springSecurityFilterChain, springSessionRepositoryFilter
	* 인코딩 변환 처리나 XSS 방어 등의 요청에 대한 처리 
```java
<filter>

    <filter-name>encoding</filter-name>

    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>

    <init-param>

        <param-name>encoding</param-name>

        <param-value>UTF-8</param-value>

    </init-param>

</filter>

<filter-mapping>

    <filter-name>encoding</filter-name>

    <url-pattern>/*</url-pattern>

</filter-mapping>
```


> Filter Class 

```java
public interface Filter { 
	// 필터 객체를 초기화 하고 서비스 추가하기 위한 메소드
	public default void init(FilterConfig filterConfig) throws ServletException {
	} 
	// 처리를 구현해서 동작 받은 후 FilterChain 파라미터 이용해서 다음 대상으로 요청 넘겨주기 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException; 

    // 필터를 제거하고 자원을 반환하기 위한 메소드 
	public default void destroy() {} 
}
```

:✅**init 메소드**

init 메소드는 필터 객체를 초기화하고 서비스에 추가하기 위한 메소드이다. 웹 컨테이너가 1회 init 메소드를 호출하여 필터 객체를 초기화하면 이후의 요청들은 doFilter를 통해 처리된다.

:✅ **doFilter 메소드**

doFilter 메소드는 url-pattern에 맞는 모든 HTTP 요청이 디스패처 서블릿으로 전달되기 전에 웹 컨테이너에 의해 실행되는 메소드이다. doFilter의 파라미터로는 FilterChain이 있는데, FilterChain의 doFilter 통해 다음 대상으로 요청을 전달하게 된다. chain.doFilter() 전/후에 우리가 필요한 처리 과정을 넣어줌으로써 원하는 처리를 진행할 수 있다.

:✅ **destroy 메소드**

destroy 메소드는 필터 객체를 서비스에서 제거하고 사용하는 자원을 반환하기 위한 메소드이다. 이는 웹 컨테이너에 의해 1번 호출되며 이후에는 이제 doFilter에 의해 처리되지 않는다.

##### ✅ 인터셉터 

스프링이 제공하는 기술로 Dispatcher Servlet 이 컨트롤러를 호출하기 전과 후에 요청을 가로채서 응답을 참조하거나 가공한다. 
내부적인 `핸들러 매핑`을 통해 적절한 컨트롤러를 찾도록 요청하는데 결과로 `HandlerExecutionChain`을 반환한다. 

```java
public interface HandlerInterceptor 

	// controller 호출 전 실행 / 요청 데이터 전처리, 가공에 사용
	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception { 
			return true;
	} 
	// 컨트롤러 호출 후 실행 / 후작업 처리시 사용
	default void postHandle(HttpServletRequest request, HttpServletResponse response, 
		Object handler, @Nullable ModelAndView modelAndView) throws Exception {} 

	// 뷰 생성을 포함한 모든 작업이 완료 후에 실행  
	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, @Nullable Exception ex) throws Exception { } 
}
```

* 예시 : 코드 동작 시간 초과 Email 전송 
* 스프링의 모든 빈 객체에 접근할 수 있다. 
* 여러개 사용 가능하며, 로그인 체크, 권한체크, 프로그램 실행시간 계산 작업 로그 확인 등의 업무를 처리. 

#### **preHandle 메소드**

preHandle 메소드는 컨트롤러가 호출되기 전에 실행된다. 그렇기 때문에 컨트롤러 이전에 처리해야 하는 전처리 작업이나 요청 정보를 가공하거나 추가하는 경우에 사용할 수 있다.

preHandle의 3번째 파라미터인 handler 파라미터는 핸들러 매핑이 찾아준 컨트롤러 빈에 매핑되는 HandlerMethod라는 새로운 타입의 객체로써, @RequestMapping이 붙은 메소드의 정보를 추상화한 객체이다.

또한 preHandle의 반환 타입은 boolean인데 반환값이 true이면 다음 단계로 진행이 되지만, false라면 작업을 중단하여 이후의 작업(다음 인터셉터 또는 컨트롤러)은 진행되지 않는다.

#### **postHandle 메소드**

postHandle 메소드는 컨트롤러를 호출된 후에 실행된다. 그렇기 때문에 컨트롤러 이후에 처리해야 하는 후처리 작업이 있을 때 사용할 수 있다. 이 메소드에는 컨트롤러가 반환하는 ModelAndView 타입의 정보가 제공되는데, 최근에는 Json 형태로 데이터를 제공하는 RestAPI 기반의 컨트롤러(@RestController)를 만들면서 자주 사용되지는 않는다.

또한 컨트롤러 하위 계층에서 작업을 진행하다가 중간에 예외가 발생하면 postHandle은 호출되지 않는다.

#### **afterCompletion 메소드**

afterCompletion 메소드는 이름 그대로 모든 뷰에서 최종 결과를 생성하는 일을 포함해 모든 작업이 완료된 후에 실행된다. 요청 처리 중에 사용한 리소스를 반환할 때 사용하기에 적합하다. postHandler과 달리 컨트롤러 하위 계층에서 작업을 진행하다가 중간에 예외가 발생하더라도 afterCompletion은 반드시 호출된다.

##### ✅ AOP

* AOP의 Advice와 HandlerInterceptor의 가장 큰 차이는 파라미터의 차이다.
* AOP의 포인트컷
	* @Before: 대상 메서드의 수행 전
	* @After: 대상 메서드의 수행 후
	* @After-returning: 대상 메서드의 정상적인 수행 후
	* @After-throwing: 예외발생 후
	* @Around: 대상 메서드의 수행 전/후



---- 
https://velog.io/@miot2j/Spring-Filter-Interceptor-AOP-%EC%B0%A8%EC%9D%B4-%EB%B0%8F-AOP%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-Logging%EC%9D%84-%EA%B5%AC%ED%98%84%ED%95%9C-%EC%9D%B4%EC%9C%A0
