[흐름]
- Spring Web MVC 
- Java Servlet



[Java Servlet]
- HTTP 요청을 처리하는 사용하며 `추상 클래스를 상속하여 구현`

[Servlet Container = Web Container]
- Servlet으로 클라이언트의 HTTP 요청을 처리하는 비즈니스 로직을 구현
- 호출/삭제 부분 구현 및 클라이언트 통신 
 => Servlet Container (JavaEE) 가 Servlet Specification 구현하여 Servlet 생명주기를 관리한다. 
 * Servlet 객체의 _init(),_ _service(),_ _destroy_() 메소드를 개발자가 직접 호출하지 않고 Servlet Container에 의해 자동으로 관리가 이루어 진다는 것을 의미
 *  소켓 생성, 포트 바인딩 등 클라이언트 연결과 관련된 다양한 기능을 포함
 * ex. Tomcat 

![[Pasted image 20230222093735.png]]


[Web MVC]
Servlet Container 내에 HTTP 요청을 받고 공통 기능을 처리할 단일 Servlet을 둡니다.
중앙에서 요청을 받는 Servlet을 [Dispatcher](https://docs.spring.io/spring-framework/docs/3.0.0.M4/spring-framework-reference/html/ch15s02.html)[Servlet](https://docs.spring.io/spring-framework/docs/3.0.0.M4/spring-framework-reference/html/ch15s02.html)이라고 하며, 이렇게 중앙화된 하나의 컴포넌트에서 요청을 다루는 디자인 패턴을 Front Controller 패턴
 - URI마다 이에 대응하는 Servlet을 구현해야 하고, 동시에 URI와 Servlet의 매핑 정보 또한 함께 관리
 - 비즈니스 로직의 처리는 Servlet이 아닌 Controller에 보내 처리하도록 구현

![[Pasted image 20230222090149.png]]


-  DispatcherServlet이 호출할 각각의 Controller는 클라이언트의 HTTP 호출을 직접 받지 않으므로, 더 이상 HTTP 요청을 처리하는 Servlet이 아니어도 됩니다.
DispatcherServlet은 더 이상 URI를 Servlet과 연결짓지 않고 메소드와 연결하므로 URI마다 Servlet 혹은 Controller를 구현하지 않아도 됩니다.

![[Pasted image 20230222094057.png]]

Controller가 분리되어 있으며 DispatcherServlet은 이들을 다루기 위해 다양한 컴포넌트를 활용합니다.
1.  DispatcherServlet이 가장 먼저 요청을 받습니다.
2.  DispatcherServlet은 요청을 [HandlerMapping](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerMapping.html)에 전달합니다. HandlerMapping은 요청 URL을 보고 요청을 처리할 적절한 Controller를 선택하여 이를 Handler와 함께 반환 합니다.
3.  DispatcherServlet은 비즈니스 로직을 실행하는 작업을 [HandlerAdapter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerAdapter.html)에 전달합니다.
4.  HandlerAdapter가 [Controller](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/Controller.html)의 비즈니스 로직을 호출합니다.
5.  Controller는 비즈니스 로직을 실행하고 처리 결과를 Model에 설정합니다. 그리고 HandlerAdapter에 결과를 보여줄 적절한 View(i.e. html 형식의 템플릿)의 이름을 DispatcherServlet에 반환합니다.
6.  DispatcherServlet은 View 이름으로부터 실제로 사용할 View 를 선택하는 작업을 [ViewResolver](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/ViewResolver.html)에 전달합니다. ViewResolver는 View 이름과 매핑되는 View를 반환합니다.
7.  DispatcherServlet 반환된 [View](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/View.html)에 렌더링 프로세스를 전달합니다.
8.  View는 Model의 데이터를 렌더링하고 응답을 반환합니다.


> 서블렛 컨테이너 - 다중요청 청리 

Keyword:`` Tomcat Thread Pool, NIO Connector, Embeded Tomcat`

✅ 요약 

스프링과 스프링 부트의 가장 큰 차이점은 스프링 부트에는 내장 서블렛 컨테이너가 잇다는 것이다. 

- 스프링부트는 내장 서블릿 컨테이너인 Tomcat을 이용합니다.
- Tomcat은 다중 요청을 처리하기 위해서, 부팅할 때 스레드의 컬렉션인 Thread Pool을 생성합니다.
- 유저 요청(HttpServletRequest)가 들어오면 Thread Pool에서 하나씩 Thread를 할당합니다. 해당 Thread에서 스프링부트에서 작성한 Dispatcher Servlet을 거쳐 유저 요청을 처리합니다.
- 작업을 모두 수행하고 나면 스레드는 스레드풀로 반환됩니다.

> 스레드 풀이란? 

hread Pool은 프로그램 실행에 필요한 Thread들을 미리 생성해놓는다는 개념입니다.(Thread는 cpu의 자원을 이용하여 코드를 실행하는 하나의 단위 입니다.

Tomcat 3.2 이전 버전에서는, 유저의 요청이 들어올 때 마다 Servlet을 실행할 Thread를 하나씩 생성했습니다. 요청이 끝나면 destory했고요.

  > 모든 요청에 대해 스레드를 생성하고 소멸하는 것은 OS와 JVM에 대해 많은 부담을 안겨준다. 동시에 일정 이상의 다수 요청이 들어올 경우 리소스(CPU와 메모리 자원) 소모에 대한 억제가 어렵다. 즉 순간적으로 서버가 다운되거나 동시다발적인 요청을 처리하지 못해서 생기는 문제가 야기될 수 있다.

해당 문제를 해결하기 위해, 톰캣은 스레드풀을 활용하기 시작합니다.

![[Pasted image 20230222091242.png]]

한줄요약 : 스레드를 미리 만들어놓고 필요한 작업에 할당했다가 돌려 받는다.

default로 HTTP Request를 받아주는 스레드(Thread)가 200개라고 한다. 

  - keyword : 
	  - `자바 소켓 프로그래밍` : Connection, Server socket에서 accept한 소캣 객체
	  * `스레드풀 전략`
	  * `적정 스레드 개수`

> Controller 의 개수는? 

Controller 객체는 힙에 생성된다. 
그러므로 `Singleton`인 Controller 이다. 

-   각각의 Thread는 singleton으로 생성된 Bean( ⇒ Controller 포함 ) 들을 참고하여 일을 한다. => Bean들은 기본적으로 Singleton으로 생성되고 관리된다.
-   사실상 이 Thread들은 그 1개의 Singleton Controller 객체를 **공유**하기에 최종적으로 1개의 Controller만 사용하는 것이다.
-   즉, 하나의 Singleton Controller가 수많은 request를 처리한다기 보다는, 각각의 Thread가 singleton으로 생성된 Controller를 참고하여 실행한다고 보면 된다.

* keyword: 
	* `싱글턴` : 오직 하나의 객체만 생성 할 수 있는 클래스. 객체의 유일성 보장한다.  
	*  **최초 한번만** 메모리를 할당하고(Static) 그 메모리에 인스턴스를 만들어 사용하는 디자인패턴.
		* 생성자가 여러 차례 호출되더라도 실제로 생성되는 객체는 하나고 최초 생성 이후에 호출된 생성자는 최초에 생성한 객체를 반환
	* 고정된 메모리 영역을 얻으면서 한번의 new로 인스턴스를 사용하기 때문에 메모리 낭비를 방지할 수 있음
	* 전역 인스턴스이기 때문에 다른 클래스의 인스턴스들이 데이터를 공유하기 쉽다.
		* 문제점 :  너무 많은 일을 하거나 많은 데이터를 공유시킬 경우 다른 클래스의 인스턴스들 간에 결합도가 높아져 "개방-폐쇄 원칙" 을 위배하게 된다. (=객체 지향 설계 원칙에 어긋남)

> Controller가 저장되는 곳

-   우선 Controller 객체 하나를 생성하면 객체 자체는 Heap에 생성되지만, 해당 Class의 정보는 Method Area(메서드 영역)에 저장된다.
- Heap:  모든 인스턴스 변수(객체)들이 저장되는 영역이며 자바에서는 new를 사용하여 객체를 생성하면 힙 영역에 저장됩니다. 힙 영역은 메모리 공간이 동적으로 할당되고 해제되며 메모리의 낮은 주소에서부터 높은 주소로 할당이 이루어집니다.
- `메모리 영역`
	- 멀티 스레드에서 메모리 영역을 살펴보자면 메소드 영역과 힙 영역은 모든 스레드가 같이 공유하는데 반해 스택의 경우에는 각 스레드별로 하나씩 생성되어집니다.
	-  스레드마다 스택 메모리는 다른 스레드에서 접근이 불가능한 반면에 Method Area와 Heap Area는 모든 스레드에서 접근이 가능하고 프로그램의 시작부터 종료 될 때까지 메모리에 남아 프로그램이 실행되고 있다면 어디서든지 사용이 가능

![[Pasted image 20230222092722.png]]


> 공유 되면 동기화 되어야 하는가?  (NO~)

-   공유되는 정보를 사용하기 위하여 굳이 Controller 객체를 사용하고 있는 Thread나 Controller 객체 자체가 동기화 될 필요가 없다.
-   원래 동기화를 해주는 이유는 프로세스(Thread)들간 알고있는 정보(상태)를 일치하기 위해서인데, **Controller가 내부적으로 상태를 갖는 것이 없으니**, 그냥 메소드 호출만 하면 된다. → 공유하는 데이터 즉, 클래스변수, 전역변수를 컨트롤러에서 사용하지 않기 때문에 상태를 갖는 것이 없음  
    
-   그로 인해 굳이 동기화할 이유도 없고 → 그저 처리 로직만 **‘공유되어’** 사용되는 것이다.
-   따라서 스레드는 Controller의 메소드를 공유하고 제각각 호출할 수 있기 때문에 → 들어오는 요청이 1만 개의 요청이든 10만 개의 요청이든 상관없게 된다.

   [상태 정보를 가지게 된다면?]
   -   결론적으로 싱글톤으로 관리되는 Bean의 경우 상태 정보를 갖지 않기 때문에 **요청의 수와 상관없이 싱글턴 객체의 장점**을 이용할 수 있었던 것이다.
	-   만약 Bean이 상태 정보를 갖게된다면 스레드들간의 동기화가 필요하고 그렇게 된다면 오버헤드가 발생하게 된다. → Controller 객체를 하나만 만들고 컨테이너에서 단순히 꺼내쓸 수 있었던 장점을 잃게 된다.

내부적으로 상태를 갖는게 없으니 그냥 메소드 호출만 하기 때문에 동기화할 필요가 없고 처리 로직만 쓰이기 때문에 1만 개의 요청이든 10만 개의 요청이든 상관없다는 얘기인 것이지요.


> Controller 개가 어떻게 수 많은 Request 를 처리 하는가? 

![[Pasted image 20230222091608.png]]

생성한 Controller 클래스에 대한 정보가 JVM 메모리 영역 중 Method Area(메서드 영역)에 올라가기 때문입니다.

Controller 객체는 Heap(힙)에 생성 되지만, 해당 클래스의 정보(메소드 처리 로직, 명령들)는 Method Area(메서드 영역)에 생성 됩니다.

따라서 결국 모든 Thread가 객체의 메서드를 공유할 수 있기 때문에 Controller는 1개만 생성됩니다.



[참고]
https://wrynn.tistory.com/68
https://sihyung92.oopy.io/spring/1  ⭐
https://jeong-pro.tistory.com/204
https://velog.io/@ejung803/Spring-Web-MVC%EC%97%90%EC%84%9C-%EC%9A%94%EC%B2%AD-%EB%A7%88%EB%8B%A4-Thread%EA%B0%80-%EC%83%9D%EC%84%B1%EB%90%98%EC%96%B4-Controller%EB%A5%BC-%ED%86%B5%ED%95%B4-%EC%9A%94%EC%B2%AD%EC%9D%84-%EC%88%98%ED%96%89%ED%95%A0%ED%85%90%EB%8D%B0-%EC%96%B4%EB%96%BB%EA%B2%8C-1%EA%B0%9C%EC%9D%98-Controller%EB%A7%8C-%EC%83%9D%EC%84%B1%EB%90%A0-%EC%88%98-%EC%9E%88%EC%9D%84%EA%B9%8C%EC%9A%94

https://velog.io/@whddlrs/50%EB%AC%B8-50%EB%8B%B5-9%EC%95%84%ED%8C%8C%EC%B9%98-%ED%86%B0%EC%BA%A3%EC%9D%80-%EA%B0%81%EA%B0%81-%EB%A9%80%ED%8B%B0-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%EC%9D%B8%EA%B0%80-%EB%A9%80%ED%8B%B0-%EC%93%B0%EB%A0%88%EB%93%9C%EC%9D%B8%EA%B0%80  50문 50답


