--- 
#### | 답변 
 @Autowired 란 DI(의존관계 주입)할때 사용하는 `어노테이션으로 의존 객체 타입에 해당하는 빈(Bean)을 주입해 주는 역할`을 한다. 
 빈 주입 방법은 애노테이션이 생성자, 필드, setter 메서드에 붙을 수 있다. 
Spring Framework 에서 `Bean 생성과 관리는 IoC(Inversion of Control) Container 가 담당하며 ApplicationsContext라는 인터페이스로 구현`되어 있다. 

xml 혹은 spring context file(java config)를 바탕으로 Bean Definition 을 생성하면 그 정보를 바탕으로 실제 Bean 을 생성한다. Spring이 가동될때 `ApplicationContext가 @Bean, @Service, @Controller 이용하여 스프링 빈을 생성`하고 ``@Autowired 가 붙은 위치에 의존성을 주입`한다.

실제 Autowired 가 붙은 빈을 `BeanPostProcessor` 이 주입하며 이의 구현이 `AutowiredAnnotationBeanPostProcessor` 이다. 
오토 와이어링 객체들은 일반적으로 private 선언되어 있는데 그럼에도 주입될 수 있는 것은 `Reflection` 때문이며 `makeAccessible` 메서드가 이를 가능하게 해주며 `field.set` 혹은 `method.invoke` 를 통해 객체가 주입된다. 

> 참고 

💡 **어노테이션**

필드, 메서드, 클래스에 컴파일 타임과 런타임에 적용될 메타데이터를 말한다.

어노테이션에 대해서는 별도의 포스팅 후 링크를 달아둘테니 참고 바란다.

💡 **빈**

스프링에서는 스프링이 제어권을 가져서 직접 생성하고 의존관계를 부여하는 오브젝트를 빈이라고 부른다.

💡 **코드로 주입 가능한 Bean**

```java
ApplicationContext applicationContext = 
		ApplicationContextProvider.getApplicationContext(); 
Object obj = applicationContext.getBean(beanName);
```

💡 **@Autowired Interface**
```java
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD}) 
// 생성자와 필드, 메서드에 적용 가능 
@Retention(RetentionPolicy.RUNTIME)
// 컴파일 이후에도 (runtime) JVM 에 의해 참조 가능 
// 런타임시 어노테이션의 정보를 리플렉션으로 얻을 수 있음. 
@Documented public @interface Autowired { 
	boolean required() default true; 
}
```

#### | spring bean autowiring 이란? 
Spring Framework 자동 DI 를 지원한다. 
Spring Configuration 파일에 빈 등록을 하면 Spring container 는 의존성을 받는 빈들 사이의 연관성을 autowire 할 수 있다. 

autowiring 은 properties, setter, constructor 에 사용 가능하다. 

```java
@Component 
public class FooService { 
	@Autowired private FooFormatter fooFormatter; 
}

// @Autowired on setter
public class FooService { 
	private FooFormatter fooFormatter; 
	@Autowired 
	public void setFormatter(FooFormatter fooFormatter) { 
		this.fooFormatter = fooFormatter; 
	} 
}

// @Autowired on Constructors
public class FooService { 
	private FooFormatter fooFormatter; 
	/** 
	 _FooFormatter_ is injected by Spring as an argument to 
	 the _FooService_ constructor
	*/
	@Autowired 
	public FooService(FooFormatter fooFormatter) { 
		this.fooFormatter = fooFormatter; 
	} 
}
```

#### | @Qualifier 

```java
@Component("fooFormatter")
public class FooFormatter implements Formatter {
    public String format() {
        return "foo";
    }
}

@Component("barFormatter")
public class BarFormatter implements Formatter {
    public String format() {
        return "bar";
    }
}

public class FooService {
    @Autowired
    private Formatter formatter;
}
```

=> Exception 
```
Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type [com.autowire.sample.Formatter] is defined: expected single matching bean but found 2: barFormatter,fooFormatter
```

** 해결방법
: implemented 된 것 중에 어떤 것을 사용하고 싶은지를 
`Qualifier` 를 통해 피할 수 있다. 
```java
public class FooService {
    @Autowired
    @Qualifier("fooFormatter")
    private Formatter formatter;
}
```

 - custom @Qualifer 를 통해 Autowiring 도 가능하다. 
 ```java
@Qualifier
@Target({
  ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, 
		  ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormatterType {  
    String value();
}
```

#### | 빈 생명 주기 및 @Autowired 동작 방법 
스프링의 IoC 컨테이너는 `빈을 책임지고 의존성을 관리`한다. 
객체들을 관리한다는 것은 `생성부터 소멸까지 LifeCycle` 관계를 컨테이너가 대신 해준다는 의미이다.
이를 통해 `프레임워크(Conatiner)`가 객체 관리의 주체이므로 개발자 로직에 집중할 수 있는 장점을 가지고 있다. 

> [0] **의존 관계 주입 전 단계** 

[빈 생성 라이프 사이클]
라이프사이클은 크게 '객체생성 - 의존관계 설정 - 초기화 - 소멸
![[Pasted image 20221128145243.png]]

```
스프링 IoC 컨테이너 생성 →  스프링 빈 생성 → 의존관계 주입 → **초기화 콜백 메소드 호출** → 사용 → **소멸 전 콜백 메소드 호출** → 스프링 종료
```

![[Pasted image 20221128145328.png]]

[객체의 생성 단계 ]
* ==생성자 주입== : 객체의 생성과 의존관계 주입이 동시에 일어남
* ==Setter, Field 주입== : 객체의 생성 > 의존관계 주입으로 라이프 사이클이 나누어져 있음.

[생성자 주입]
```java
@Controller
public class CocoController {
    private final CocoService cocoService;
 
    public CocoController(CocoService cocoService) {
        this.cocoService = cocoService;
    }
}
```

new 연산을 호출하면서 생성자가 호출되는데 controller 클래스에 존재하는 서비스 클래스간의 의존관계가 존재하지않으면 controller 클래스의 객체 생성이 불가능하기 때문에 `객체 생성과 동시에 생성자 주입은 의존관계 주입도 동시에 일어난다`

* 장점 
	* null 주입 없이는 `NullPointerException` 발생하지 않음
	* 의존관계를 주입하지 않은 경우 객체를 생성할 수 없으므로 `의존관계에 대한 내용을 컴파일 타임 오류에 잡을 수 있다.`

[setter, field 주입]
```java
@Controller
public class CocoController {
	
    private CocoService cocoService;
    
    @Autowired
    public void setCocoService(CocoService cocoService) {
    	this.cocoService = cocoService;
    }
}
```

생성자 주입을 별개로 Controller 객체를 생성시 Service 객체와 의존관계가 없어도 Controller 객체 생성이 가능하며 의존 관계 주읩 단계를 나누어 Bean LifeCycle 이 진행 된다. 
=> 작성된 코드의 의존관계를 보고 IoC 컨테이너에 의존성 주입을 해준다. 


> [1] 의존성 주입 

@Autowired 핵심 클래스인 `AutowiredAnnotationBeanPostProcessor` 은 객체의 생성과 소멸 라이프 사이클을 담당하는 `BeanPostProcessor` 구현체가 있어야 메서드를 사용 할 수 있다.  해당 클래스는 BeanPostProcessor 인터페이스를 상속받고 있다. 

![[Pasted image 20221128145555.png]]

아래는 Spring Container 이 의존성 주입 받는 방식이다. 

![[Pasted image 20221128130121.png]]

Component Scan 으로 Bean 을 등록한다. 
빈을 등록하는 방법으로는 @Component 혹은 @Bean 애노테이션이 있다. 

예시] 
![[Pasted image 20221128130259.png]]


@Autowired -> proctInjection -> ReflectionUtils 

> processInjection 

: 빈의 클래스 정보를 읽어와서 (getClass()) 자동으로 의존관계를 설정할 메타데이터를 얻는다. 

> ReflectionUtils 

: @Autowired 는 리플렉션을 통해 수행 된다. 
`AutowiredAnnotationBeanPostProcessor` 은 InjectMetadata 를 상속받는 `AutowiredFieldElement` 와 `AutowiredMethodElement` 를 구현하고 있으며 필드든 메서드든 각각 맞게 오버라이딩 된 Inject 메서드를 호출한다. 

![[Pasted image 20221128150259.png]]


---
[footnote]
https://beststar-1.tistory.com/40
https://dev-coco.tistory.com/170
https://kellis.tistory.com/58
