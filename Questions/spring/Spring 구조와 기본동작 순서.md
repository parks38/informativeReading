----
#### | 실행 순서 
요청  -> **DispatcherServlet**(web.xml) -> **HandlerMapping**(servlet-context.xml)  -> 요청 처리하기 (**Controller <-> Service <-> DAO <-> DB**)  -> **DispatcherServlet** -> **ViewResolver** -> **View**  -> **DispatcherServlet** -> 응답

> Spring MVC

Model, View, Controller 구성요소를 사용해 `HTTP Request`를 처리하고 
단순한 정적 응답부터 REST 형시의 응답 물론 view 표시 html retunr 응답까지 응답할수 있는 프레임워크이다. 
인터페이스를 사용해 규격화하여 유연하고 확장성있게 웹 애플리케이션 설계 가능하게 해줌.

> spring 구조

![[Pasted image 20221209151748.png]]

1. 클라이언트 request 하면 `Dispatcher Servlet` 이 요청을 가로챈다.  모든 요청을 가로채는 것은 아니고 web.xml 에 < url-pattern> 에 등록된 내용만 가로챈다. 
	ex)
```xml
<servlet-mapping>  
	<servlet-name>appServlet</servlet-name>  
	<url-pattern>*.do</url-pattern>  
</servlet-mapping>

<!-- The mappings for the JSP servlet -->  
<servlet-mapping>  
    <servlet-name>jsp</servlet-name>  
    <url-pattern>*.jsp</url-pattern>  
    <url-pattern>*.jspx</url-pattern>  
</servlet-mapping>
```

2. DispatcherServlet 이 가로챈 정보를 HandlerMapping 에게 보내 요청의 Controller 를 검색한다. 
```

```



----
[참고]

https://devpad.tistory.com/24
https://hpark3.tistory.com/28
https://catsbi.oopy.io/f52511f3-1455-4a01-b8b7-f10875895d5b
https://kotlinworld.com/326




