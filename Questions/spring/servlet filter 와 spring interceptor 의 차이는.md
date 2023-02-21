
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


##### ✅ AOP

* AOP의 Advice와 HandlerInterceptor의 가장 큰 차이는 파라미터의 차이다.
* AOP의 포인트컷
	* @Before: 대상 메서드의 수행 전
	* @After: 대상 메서드의 수행 후
	* @After-returning: 대상 메서드의 정상적인 수행 후
	* @After-throwing: 예외발생 후
	* @Around: 대상 메서드의 수행 전/후