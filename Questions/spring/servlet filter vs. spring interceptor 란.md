
[개요]
- filter?
- interceptor (feat AOT)?
- filter vs. interceptor 용도 및 차이 ?
- 예외 처리 방법 


##### | ☑️ Filter? 

 -  J2EE 표준 스펙 기능
 - 디스패처 서블릿(Dispatcher Servlet)에 요청이 전달되기 전/후에 응답과 요청을 거른 뒤 정제하는 역할
![[Pasted image 20230302093436.png]]


* Dispatcher Servlet 이전에 앞단에서 요청 내용을 변경하거나 체크 수행
* 톰캣과 같은 웹 컨테이너에 의해 관리 (과거에는 관리되지 않았으나 사실은! 스프링 빈으로 등록은 된다)
	* 필터(Filter)가 스프링 빈 등록과 주입이 가능한 이유(DelegatingFilterProxy의 등장) 
		* 요약: 스프링 빈 등록 => DelegatingFilterProxy 요청을 받음 -> Filter 에게 요청 위임 
		* (https://mangkyu.tistory.com/221)
		
* web.xml 에 등록하여 사용  
	* 인코딩 변환 처리나 XSS 방어 등의 요청에 대한 처리 
	* ex. Encoding, CorsFilter, springSecurityFilterChain, springSessionRepositoryFilter
	
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

* 필터를 추가하기 위해서는 javax.servlet의 Filter 인터페이스를 구현(implements) : 
	*    init 메소드
	-   doFilter 메소드
	-   destroy 메소드

✅**init 메소드**

- 초기화하고 서비스에 추가
- 웹 컨테이너가 1회 init 메소드를 호출시 이후의 요청들은 doFilter를 통해 처리

```java
public interface Filter { 

	public default void init(FilterConfig filterConfig) throws ServletException {} 
}
```

✅ **doFilter 메소드**

- 모든 HTTP 요청이 디스패처 서블릿으로 전달되기 실행
-  FilterChain : 다음 대상으로 요청을 전달 
	- chain.doFilter() 전/후에 필요한 처리 과정을 넣어주어 처리 
	
```java
public interface Filter { 

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException; 
}
```

✅ **destroy 메소드**

-  필터 객체를 서비스에서 제거하고 자원을 반환
- 1번 호출되며 이후에는 처리되지 않음. 

```java
public interface Filter { 

	public default void destroy() {} 
}
```

##### | ☑️ Interceptor? 

* Spring이 제공하는 기술
* 디스패처 서블릿(Dispatcher Servlet)이 컨트롤러를 호출하기 전과 후 요청과 응답을 참조하거나 가공
* 내부적인 `핸들러 매핑`을 통해 적절한 컨트롤러를 찾도록 요청하는데 결과로 `HandlerExecutionChain`을 반환한다. 
* 로그인 체크, 권한체크, 프로그램 실행시간 계산작업 로그확인 등의 업무처리
	* 예시 : 코드 동작 시간 초과 Email 전송 

![[Pasted image 20230126160905.png]]


> HandlerInterceptor

인터셉터를 추가하기 위해서는 org.springframework.web.servlet의 HandlerInterceptor 인터페이스를 구현(implements) : 
-   preHandle 메소드
-   postHandle 메소드
-   afterCompletion 메소드

✅ preHandle 메소드

- 컨트롤러가 호출되기 전에 실행
	- 이전에 처리해야 하는 전처리 작업
	- 정보를 가공하거나 추가하는 경우에 사용
- handler 파라미터
	- @RequestMapping 정보를 추상화한 객체
	- 컨트롤러 빈에 매핑되는 HandlerMethod 정보를 추상화한 객체이다.
* 반환값이 true이면 다음 단계로 진행되고 false라면 작업을 중단하여 이후의 작업은 진행되지 않음. 

```java
public interface HandlerInterceptor 

	// controller 호출 전 실행 / 요청 데이터 전처리, 가공에 사용
	default boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception { 
			return true;
	} 
}
```

✅ postHandle 메소드

- 컨트롤러를 호출된 후에 실행
	- 후처리 작업이 있을 때 사용
-   ModelAndView 타입의 정보가 제공되는데 RestAPI 기반의 컨트롤러(@RestController)를 만들면서 자주 사용되지는 않음. 
- 하위 계층에서 작업을 진행하다 중간에 예외가 발생하면 postHandle은 호출되지 않음. 

```java
public interface HandlerInterceptor 

	// 컨트롤러 호출 후 실행 / 후작업 처리시 사용
	default void postHandle(HttpServletRequest request, HttpServletResponse response, 
		Object handler, @Nullable ModelAndView modelAndView) throws Exception {} 

}
```


✅ afterCompletion 메소드

- 모든 뷰에서 최종 결과를 생성하고 모든 작업이 완료된 후에 실행
- 요청 처리 중에 사용한 리소스를 반환할 때 사용
- 하위 계층에서 작업을 진행하다가 중간에 예외가 발생하더라도 afterCompletion은 반드시 호출

```java
public interface HandlerInterceptor 

	// 뷰 생성을 포함한 모든 작업이 완료 후에 실행  
	default void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, @Nullable Exception ex) throws Exception { } 
}
```


###### | AOP 란? 

![[Pasted image 20230302110558.png]]

* 중복을 줄이기 위해 관점을 바라보고 처리
* 애플리케이션에서의 관심사의 분리(기능의 분리), 핵심적인 기능에서 부가적인 기능을 분리
* *'로깅', '트랜잭션', '에러 처리'등 비즈니스단의 메서드에서 사용

- keyword 
	- ASPECT = ADVICE + PointCut 
		- Advice : 부가기능을 정의한 코드
		- Pointcut : 어디에 적용하지를 결정

> 인터셉터(Interceptor)와 AOP의 비교 

AOP의 Advice와 HandlerInterceptor의 가장 큰 차이는 파라미터의 차이다.
* 메소드 전후의 지점에 자유롭게 설정이 가능
		* AOP의 포인트컷
			* @Before: 대상 메서드의 수행 전
			* @After: 대상 메서드의 수행 후
			* @After-returning: 대상 메서드의 정상적인 수행 후
			* @After-throwing: 예외발생 후
			* @Around: 대상 메서드의 수행 전/후
		
* AOP는 주소, 파라미터, 애노테이션 등 다양한 방법으로 대상을 지정 가능 
		* Interceptor와 Filter는 주소로 대상을 구분
		* url 기반이 아닌 `pointcut` 단위로 동작 
		

> 꼬리물기 : Q. 인터셉터 대신 AOP 를 사용해도 되는가? 

인터셉터 대신에 컨트롤러들에 적용할 부가기능을 어드바이스로 만들어 AOP(Aspect Oriented Programming, 관점 지향 프로그래밍)를 적용할 수도 있다. 하지만 다음과 같은 이유들로 컨트롤러의 호출 과정에 적용되는 부가기능들은 인터셉터를 사용하는 편이 낫다.

1.  컨트롤러는 타입과 실행 메소드가 모두 제각각이라 포인트컷(적용할 메소드 선별)의 작성이 어렵다.
2.  컨트롤러는 파라미터나 리턴 값이 일정하지 않다.
3.  AOP에서는 HttpServletRequest/Response를 객체를 얻기 어렵지만 인터셉터에서는 파라미터로 넘어온다.

![[Pasted image 20230302112455.png]]

![[Pasted image 20230302112426.png]]

##### | Filter vs. Interceptor 용도 및 차이 

> ✅ 비교 

| 대상                           | Filter                               | Interceptor     |
| ------------------------------ | ------------------------------------ | --------------- |
| 관리 컨테이너                  | 웹 컨테이너                          | 스프링 컨테이너 |
| Request/Response <br>조작 여부 | O                                    | x               |
| 용도                           | - 공통된 보안 및 인증/인가 관련 작업 <br> - 모든 요청에 대한 로깅 또는 감사 <br> - 이미지/데이터 압축 및 문자열 인코딩 <br> - spring 과 분리되어야 하는 기능  |     - 세부적인 보안 및 인증/인가 공통 작업 <br> - API 호출에 대한 로깅 또는 감사 <br> - Controller 로 넘겨주는 정보(데이터) 가공                            |                                      |                 |


######  ➡️ **Request/Response 객체 조작 가능 여부**

- 조작 여부 =>  다른 객체로 바꿔친다는 의미

> Filter 

* 필터 체이닝(다음 필터 호출)
	* 필터가 다음 필터를 호출
	* 이때 Request/Response 객체를 넘겨주어 넣어줄 수 있음.

```java
public FilterConfig implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        // 개발자가 다른 request와 response를 넣어줄 수 있음
        chain.doFilter(request, response);       
    }
    
}
```

> Interceptor
 
*  for문으로 순차적 인터셉터 목록 실행 
* boolean 반환 
	*  true를 반환하면 다음 인터셉터가 실행/컨트롤러로 요청이 전달
	*  false가 반환되면 요청이 중단

```java
public class InterceptorConfig implements HandlerInterceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Request/Response를 교체할 수 없고 boolean 값만 반환할 수 있다.
        return true;
    }

}
```

###### ➡️ Filter 의 용도 

==스프링과 무관==하게 전역적으로 처리해야 하는 작업 담당 

-   `공통된 보안 및 인증/인가 관련 작업`
	- 스프링 컨테이너 앞단에서 처리하여 안정성 높임 
-   모든 요청에 대한 로깅 또는 감사
-   `이미지/데이터 압축 및 문자열 인코딩`
	- 웹 애플리케이션에 전반적으로 사용되는 기능을 구현
-   Spring과 분리되어야 하는 기능

⭐ Filter는 다음 체인으로 넘기는 `ServletRequest/ServletResponse 객체를 조작할 수 있다는 점`에서
	Interceptor보다 훨씬 강력한 기술이다.

###### ➡️ Interceptor 의 용도 

==클라이언트의 요청과 관련==되어 전역적으로 처리해야 하는 작업

-   `세부적인 보안 및 인증/인가 공통 작업`
	- ex. 특정 사용자 기능 차단 => 컨트롤러 넘어가기 전에 검사 필요 
-   `Controller로 넘겨주는 정보(데이터)의 가공`
	-  ex. ID를 기반으로 조회한 사용자 정보 HttpServletRequest에 가공 
-   API 호출에 대한 로깅 또는 감사
	- ex. 클라이언트의 IP나 요청 정보들을 포함해 기록

⭐ Interceptor 는  `HttpServletRequest`나 `HttpServletResponse` 에서 제공받는 정보를 가공하는 작업 적합 

##### | 예외 처리 경우 

| 구분      | filter                 | Interceptor            |
| --------- | ---------------------- | ---------------------- |
| 위치      | DispatcherServlet 외부 (web application) | DispatcherServlet 내부 |
| 예외 처리 | ErrorController        | @ControllerAdvice / @ExceptionHandler                       |


> ErrorController

에러가 발생하면 서블릿 컨테이너가 캐치해서 에러페이지 경로로 forward

![[Pasted image 20230302115946.png]]
1.  서블릿 컨테이너(ex: 톰캣)에서 등록된 서블릿에서 요청을 처리
2.  오류가 발생
3.  해당 서블릿에서 처리하지 못함. 
4.  서블릿 컨테이너까지 오류가 전파되었을 때, 서블릿 컨테이너가 오류를 처리하기 위해 특정 경로(`server.error.path`)로 해당 요청처리를 위임

> @ExceptionHandler 

: Spring에서는 발생한 Exception을 기반으로 오류를 처리할 수 있도록 `@ExceptionHandler`를 제공

* Controller에서 예외가 발생한 경우, 해당 애너테이션에 선언된 예외 및 하위 예외에 대해서 특정 메서드에게 처리 위임
* 반환 
	* `@ResponseStatus`를 통해 응답 코드를 정의
	* `ModelAndView`, `String`을 반환하여 view를 resolve
	* `ResponseEntity<T>`를 반환 

> @ControllerAdvice 

Spring에서는 Bean으로 등록되는 `@Controller`들을 선택적으로, 혹은 전역으로 몇가지 공통 설정을 적용할 수 있도록 `@ControllerAdvice`를 사용할 수 있다  
이 `@ControllerAdvice`에서 사용할 수 있는 것 중 하나가 `@ExceptionHandler`다

```java
@Slf4j  
@ControllerAdvice  
public class GlobalControllerAdvice {  
  
  @ResponseStatus(HttpStatus.NOT_FOUND)  
  @ExceptionHandler(BoardNotFoundException.class)  
  public Object handle(BoardNotFoundException e, HttpServletRequest request) {  
    if (JSON_응답해야하는지(request)) {  
      return makeJson(e);  
    } else {  
      return "/error/404";  
    }  
  }  
}
```


----
https://velog.io/@miot2j/Spring-Filter-Interceptor-AOP-%EC%B0%A8%EC%9D%B4-%EB%B0%8F-AOP%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-Logging%EC%9D%84-%EA%B5%AC%ED%98%84%ED%95%9C-%EC%9D%B4%EC%9C%A0
