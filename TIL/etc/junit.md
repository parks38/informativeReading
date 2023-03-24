## Junit
### Junit 소개

![[Pasted image 20220822092013.png]]

- Platform: 테스트를 실행해주는 런처 제공. TestEngine API 제공.
- Jupiter: TestEngine API 구현체로 JUnit 5를 제공.
- Vintage: JUnit 4와 3을 지원하는 TestEngine 구현체.

### | 시작 
* 2.2+ 버전의 스프링 부트 프로젝트 생성 시 자동 생성 JUnit 5 의존성 추가 됨.
* 스프링 부트 사용 (X)
```
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-engine</artifactId>
	<version>5.5.2</version>
	<scope>test</scope>
</dependency>
```

### 기본 annotation 
```ad-note
	@Test
	@BeforeAll / @AfterAll
	@BeforeEach / @AfterEach
	@Disabled
```

✅ 예시 ) 	
```java
	@BeforeAll // 테스트 실행 전 딱 한번만
    static void beforeAll() {  //private X default O return type 있으면 X
        System.out.println("before all");
    }

    /**
     * .afterAll()' must be static unless the test class
     * is annotated with @TestInstance(Lifecycle.PER_CLASS).
     */
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    //beforeEach()' must not be static.
    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
```

`@DisplayNameGeneration`
- Method와 Class 레퍼런스를 사용해서 테스트 이름을 표기하는 방법 설정.
- 기본 구현체로 ReplaceUnderscores 제공

`@DisplayName`
- 어떤 테스트인지 테스트 이름을 보다 쉽게 표현할 수 있는 방법을 제공하는 애노테이션.
- @DisplayNameGeneration 보다 우선 순위가 높다.
```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
											// nameGenerator 전략
class StudyTest  {

    @Test
    @DisplayName("스터디 만들기") 
	    // 메소드의 이름 생성(DisplayNameGeneration 보다 권장)
    @Disabled // 해당 테스트는 실행하고 싶지 않음 (좋은 방법은 아니지만 필요하다면)
    void create_new_study () {
	
	}
```

### | Assertion 
`org.junit.jupiter.api.Assertions.*`  
| method                                 | definition                            |
| -------------------------------------- | ------------------------------------- |
| assertEqulas(expected, actual)         | 실제 값이 기대한 값과 같은지 확인     |
| assertNotNull(actual)                  | 값이 null이 아닌지 확인               |
| assertTrue(boolean)                    | 다음 조건이 참(true) 인지 확인        |
| assertAll(executables...)              | 모든 확인 구문 확인                   |
| assertThrows(expectedType, executable) | 예외 발생 확인                        |
| assertTimeout(duration, executable)    | 특정 시간 안에 실행이 완료되는지 확인 | 
 
 > assertEquals   

```java
@Test
@DisplayName("스터디 만들기") 
	// 메소드의 이름 생성 (DisplayNameGeneration 보다 권장)
void create_new_study () {
    Study study = new Study(-10);

    assertNotNull(study);
    System.out.println("create");
    assertEquals(StudyStatus.DRAFT, 
		study.getStatus(), () -> "스터디를 처음 만들면 상태값이 draft 이어야 한다. ");
```

 - 순서 :  assertEquals(expected, actual, message)
```java
message
(new Supplier<String>() {
	@Override 
	public String get() { 
		return "스터디를"; 
	}
}))
```

 * 마지막 매개변수로 Supplier < `String` >타입 인스턴스를 람다 형태로 제공 가능  
 * 문자열 연산을 하게되면 <u>비용이 크다. 람다식을 쓰는 것이 성능 입장에서는 유리하다</u>  
    

### | 조건에 따라 테스트 실행  
`org.junit.jupiter.api.Assumptions.*`
- assumeTrue(조건)
- assumingThat(조건, 테스트)

✅ @Enabled__ 와 @Disabled__
- OnOS
- OnJre
- IfSystemProperty
- IfEnvironmentVariable
- If
```java
@Test
// @EnabledOnOs({OS.MAC, OS.WINDOWS}) // 특정 운영체제
// @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11})
// @EnabledIfEnvironmentVariable(named = "TEST_ENV" , matches = "LOCAL")
// @EnabledIf() || @DisabledIf()
void create1() {\
    System.out.println("create1");
    
    // 특정 조건 만족할 때만 사용
    String test_env = System.getenv("TEST_ENV");
    assumeTrue("Local".equalsIgnoreCase(test_env));  // 환경 변수 반영

    // ~일 경우에는 이렇게 하고 중복 if 문 처럼 사용 가능
    assumingThat("Local".equalsIgnoreCase(test_env), () -> {
        System.out.println("local");
        Study actual = new Study(100);
    });
}
``` 

### | Custom Tags 
- custom tag 으로 build/test 를 특정 원하는 태그만 실행 가능 

- FastTest 생성 
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@Test
public @interface FastTest {
}
```
✔️@Target 
```
ElementType.PACKAGE : 패키지 선언
ElementType.TYPE : 타입 선언
ElementType.ANNOTATION_TYPE : 어노테이션 타입 선언
ElementType.CONSTRUCTOR : 생성자 선언
ElementType.FIELD : 멤버 변수 선언
ElementType.LOCAL_VARIABLE : 지역 변수 선언
ElementType.METHOD : 메서드 선언
ElementType.PARAMETER : 전달인자 선언
ElementType.TYPE_PARAMETER : 전달인자 타입 선언
ElementType.TYPE_USE : 타입 선언
```


* local test 시에 설정 세팅 maven 설정 (profile build 설정)
```
	<profiles> <!-- 빌드 설정을 다르게 설정 -->
		<profile>
			<id>default</id>
			<activation>
				<activeByDefault> true </activeByDefault>
			</activation>
			<build>
				<plugins>
					<plugin>
						<artifactId>maven-surefire-plugin</artifactId>
						<configuration>
							<groups>fast</groups>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
```

* 예시]
```
@FastTest
@DisplayName("스터디 만들기 fast")
void create_new_study() { 
```
* slow test 예시]
```
@SlowTest
@DisplayName("스터디 만들기 slow")
void create_new_study_again() {
```

### | 테스트 반복하기 
`@RepeatedTest`
- 반복 횟수와 반복 테스트 이름을 설정할 수 있다.
	- {displayName}
	- {currentRepetition}
	- {totalRepetitions}
- RepetitionInfo 타입의 인자를 받을 수 있다.
``` java
    @DisplayName("스터디 만들기")
    @RepeatedTest(value= 10, name = "{displayName}, 
								    {currentRepetition}/{totalRepetitions}")
    void create_study(RepetitionInfo repetitionInfo) {
        System.out.println("test " + 
		        repetitionInfo.getCurrentRepetition());
        // repetitionInfo.getTotalRepetitions()
    }
```

``` ad-tip
ctrl + p : options 
```

`@ParameterizedTest`
- 테스트에 여러 다른 매개변수를 대입해가며 반복 실행한다.
	- {displayName}
	- {index}
	- {arguments}
	- {0}, {1}, ... 

``` java
    /**
     * parameter 의 개수만큼 (4)번 테스트 실행하여 호출
     * 각 테스트에 해당하는 결과값 반환
     *  {0,1,2} 로 parameter 참조
     * @param message
     */
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요"})
    void parameterizedTest(String message) {
    
        System.out.println(message);
    }
```

[인자 값들의 소스]
- @ValueSource
```java
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest(Integer study) {
        System.out.println(study);
    }
```

- @NullSource, @EmptySource, @NullAndEmptySource (혼합 형태)
	-  @EmptySource : 비어있는 문자열을 인자로 추가
	- @NullSource : null 의 문자열을 인자로 추가 
- @EnumSource
- @MethodSource
- ``@CsvSource`` : 여러 인자를 넘겨줄 수 있음. 
-  @CvsFileSource
-  @ArgumentSource

[인자 값 타입 변환]
-  암묵적인 타입 변환
	-  레퍼런스 참고
-  명시적인 타입 변환
	-  SimpleArgumentConverter 상속 받은 구현체 제공
	-  @ConvertWith 
	
- 인자를 내가 원하는 타입으로 변환시켜 주는 interface 존재 
	- <u>하나의 argument 받을 경우 </u> => `SimpleArgumentConverter`
```java
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }
	
	// 제약조건 : static or public class
    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> targetType) 
							        throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "can only convert");
            return new Study(Integer.parseInt(o.toString()));
        }
    }
```

[인자 값 조합]
-  ArgumentsAccessor
-  커스텀 Accessor
	-  ArgumentsAggregator 인터페이스 구현
	-  @AggregateWith 
- 여러개의 argument 받을 경우 => `aggregateArguments` / `ArgumentAccessor`
```
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디", "20, 스프링"})
    void parameterizedTest(ArgumentsAccessor argumentsAccessor) {
        Study study = new Study(argumentsAccessor.getInteger(0),
				         argumentsAccessor.getString(1));
        System.out.println(study);
    }
```

### |테스트 인스턴스 
JUnit은 테스트 메소드 마다 테스트 <u>인스턴스를 새로 만든다.</u>
	- Hash 값이 다름으로 확인 가능하다. 
- 이것이 기본 전략.
- 테스트 메소드를 <u>독립적으로 실행하여 예상치 못한 부작용을 방지</u>하기 위함이다.
	- test 간의 의존성을 없애기 위해 해당 방법으로 실행 
- 이 전략을 JUnit 5에서 변경할 수 있다.

`@TestInstance(Lifecycle.PER_CLASS)`
- 테스트 클래스당 <u>인스턴스를 하나만 만들어 사용</u>한다.
- 경우에 따라, 테스트 간에 공유하는 모든 상태를 `@BeforeEach` 또는 `@AfterEach`에서 초기화 할 필요가 있다.
- `@BeforeAll`과 `@AfterAll`을 인스턴스 메소드 또는 인터페이스에 정의한 default 메소드로 정의할 수도 있다.
	- TestInstance 통해 class 당 만들면 `static` 일 필요가 없음. 

### | 테스트 순서 
실행할 테스트 메소드 특정한 순서에 의해 실행되지만 어떻게 그 순서를 정하는지는 의도적으로 분명히 하지 않는다. (테스트 인스턴스를 테스트 마다 새로 만드는 것과 같은 이유)
경우에 따라, 특정 순서대로 테스트를 실행하고 싶을 때도 있다. 
그 경우에는 테스트 메소드를 원하는 순서에 따라 실행하도록

⭐테스트 마다 의존성이 없어야 하기 때문에 사실상 순서에 의존해서는 안되지만 순서가 필요한 경우도 있다. 
	=> user case 순차적으로 stateful 하게 상태 정도 유지하며 데이터 공유

`@TestInstance(Lifecycle.PER_CLASS)`와 함께 `@TestMethodOrder`를 사용
	- MethodOrderer 구현체를 설정한다.
	- 기본 구현체
		- Alphanumeric
		- OrderAnnoation
		- Random

- `Order()` 사용
	- 같은 숫자를 넣었을 경우, 내부 로직으로 알맞는 순서를 자동으로 정해줌.
```
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
class StudyTest  {

    @Order(1) // 낮은 값일 수록 높은 우선 순위
    @Test 
    @DisplayName("test 1")
    void create_1 () {
        
    }

    @Order(2) // 낮은 값일 수록 높은 우선 순위
    @Test
    @DisplayName("test 2")
    void create_2 () {

    }

    @Order(3) // 낮은 값일 수록 높은 우선 순위
    @Test
    @DisplayName("test 3")
    void create_3 () {

    }
}
```

### | junit-platform.properties
JUnit 설정 파일로, 클래스패스 루트 (src/test/resources/)에 넣어두면 적용된다.

* 테스트 인스턴스 라이프사이클 설정
`junit.jupiter.testinstance.lifecycle.default = per_class`
* 확장팩 자동 감지 기능
`junit.jupiter.extensions.autodetection.enabled = true`

 - @Disabled 무시하고 실행하기
 `junit.jupiter.conditions.deactivate = org.junit.*DisabledCondition`
 example )
 `DisabledonOsCondition`
 - 테스트 이름 표기 전략 설정
 `junit.jupiter.displayname.generator.default = \`
`org.junit.jupiter.api.DisplayNameGenerator$ReplaceUnderscores`


### | 확장 모델 
JUnit 4의 확장 모델은 `@RunWith(Runner), TestRule, MethodRule`.
JUnit 5의 확장 모델은 단 하나, `Extension`.

> 확장팩 등록 방법 (Extension Model)
-  선언적인 등록 `@ExtendWith`
ex]   `@ExtendWith(FindSlowTestExtention.class)`  
	
-  클래스 구현체 
```java
public class FlowSlowTestExtention implements BeforeTestExecutionCallback, AfterTestExecutionCallback {  
  
    private static final long THRESHOLD = 1000L;  
  
    @Override  
    public void afterTestExecution
    (ExtensionContext extensionContext) throws Exception {  
  
        String testMethodName = 
	        extensionContext.getRequiredTestMethod().getName();  
        ExtensionContext.Store store = getStore(extensionContext);  
  
        long start_time = store.remove("START_TIME", long.class);  
        long duration = System.currentTimeMillis() - start_time;  
  
        if (duration > THRESHOLD) {  
            System.out.printf(
            "Please consider mark method [%s] with @SlowTest. \n", 
            testMethodName);  
        }  
    }  
  
    @Override  
    public void beforeTestExecution
    (ExtensionContext extensionContext) throws Exception {  
        ExtensionContext.Store store = getStore(extensionContext);  
        store.put("START_TIME", System.currentTimeMillis());  
    }  
  
    private ExtensionContext.Store getStore
							    (ExtensionContext extensionContext) {  
        String testClassName = 
			        extensionContext.getRequiredTestClass().getName();  
        String testMethodName = 
			        extensionContext.getRequiredTestMethod().getName();  
        return extensionContext.getStore(
        ExtensionContext.Namespace
        .create(testClassName, testMethodName));  
    }  
}
```
-  프로그래밍 등록 `@RegisterExtension`
```java
@RegisterExtension
static FindSlowTestExtention findSlowTestExtention 
	= new FindSlowTestExtention(THRESHOLD: 1000L);
```
-- AutomaticRegisterExtention 확인해 보기

- FindSlowTestExtention
```java
public class FindSlowTestExtention {
	private long THRESHOLD;
	
	public FindSlowTestExtention (long THRESHOLD) {
		this.THRESHOLD = THRESHOLD;
	}
}
```

-  자동 등록 자바 `ServiceLoader` 이용

> 확장팩 만드는 방법
-  테스트 실행 조건
-  테스트 인스턴스 팩토리
-  테스트 인스턴스 후-처리기
-  테스트 매개변수 리졸버
-  테스트 라이프사이클 콜백
-  예외 처리
-  ...

참고
-  https://junit.org/junit5/docs/current/user-guide/#extensions

#### | Junit4 마이그레이션 
junit-vintage-engine을 의존성으로 추가하면, JUnit 5의 junit-platform으로 JUnit 3과 4로 작성된 테스트를 실행할 수 있다.
* `@Rule`은 기본적으로 지원하지 않지만, 
`junit-jupiter-migrationsupport` 모듈이 제공하는 `@EnableRuleMigrationSupport`를 사용하면 다음 타입의 Rule을 지원한다.
	* ExternalResource
	* Verifier
	- ExpectedException

| Junit 4                                    | Junit5                                         |
| ------------------------------------------ | ---------------------------------------------- |
| @Category(Class)                           | @Tag(String)                                   |
| @RunWith, @Rule, @ClassRule                | @ExtendWith, @RegisterExtension                |
| @Ignore                                    | @Disabled                                      |
| @Before, @After, @BeforeClass, @AfterClass | @BeforeEach, @AfterEach, @BeforeAll, @AfterAll |
- Junit5 에서는 `@RunWith` 더 이상 사용하지 않음. 
