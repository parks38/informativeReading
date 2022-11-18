#### | 연습 문제 
1. 테스트 이름을 표기하는 방법으로 공백, 특수 문자 등을 자유롭게 쓰는 애노테이션 
	=> `@DisplayName`

2. 테스트를 실행하는 런처와 테스트 엔진 API 를 제공하는 모듈? 
 => `platform` : 테스트 실행 해주는 런처 제공/ TestEngine API 
	    `jupiter` : TestEngine API 구현체로 Junit5를 제공 
	    `vintage` : Junit4 와 3를 지원하는 TestEngine 구현체 

3. 테스트 그룹을 만들고 필터링 하여 실행하는데 사용하는 애노테이션 
	=> `@Tag`

4.
```java
@Test
@DisplayName("스터디 만들기")
void create_new_study() {
	Study actual = new Study(1, "테스트 스터디");
	assertAll(
		() -> assertEquals(1, actual.getLimit()),
		() -> assertEquals("테스트 스터디", actual.getName()),
		() -> assertEquals(StudyStatus.DRAFT, actual.getStatus())
	);
}
```

5.  제공하는 애노테이션으로 컴포짓 애노테이션을 만드는 코드 
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) // 실행 시점에 참조 필요 
@Test
@Tag("fast")
public @interface FastTest {
}
```
* `Retention` 
	* 실제로 적용되고 유지되는 범위 
	* 종류
		* `RetentionPolicy.RUNTIME`
			* compile 이후에도 JVM 에 의해 참조 가능
			* reflection 이나 로깅에 사용
		* `RetentionPolicy.CLASS`
			* 컴파일러가 클래스를 참조할때 유효 
		* `RetentionPolicy.SOURCE`
			* 컴파일 전까지만 유효. 즉, 컴파일 이후 사라짐. 

6. 확장팩 등록 방법
	* `@ExtendWith`
	* `@Rule` (X) 
	* `@RegisterExtention`
	* `ServiceLoader`

*추가 설명*
Junit4 에서는 `@Rule` 및 `@ClassRule` 주석을 사용하여 테스트에 특수 기능을 추가 
Junit5 에는 `@ExtendWith` 주석을 사용하여 동일한 논리 재현 

7)
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestInstance(value = PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @see MethodOrderer
// @see MethodOrderer.Alphanumeric 
// @see MethodOrderer.OrderAnnotation 
// @see MethodOrderer.Random
public class StudyCreateUsecaseTest {
	private Study study;
	@Order(1)
	@Test
	@DisplayName("스터디 만들기")
	public void create_study() {
		study = new Study(10, "자바");
		assertEquals(StudyStatus.DRAFT, study.getStatus());
	}
	@Order(2)
	@Test
	@DisplayName("스터디 공개")
	public void publish_study() {
		study.publish();
		assertEquals(StudyStatus.OPENED, study.getStatus());
		assertNotNull(study.getOpenedDateTime());
	}
}
```
`@TestInstance(Lifecycle.PER_CLASS)`
- 테스트 클래스당 <u>인스턴스를 하나만 만들어 사용</u>한다.
- 경우에 따라, 테스트 간에 공유하는 모든 상태를 `@BeforeEach` 또는 `@AfterEach`에서 초기화 할 필요가 있다.

`@TestMethodOrder`
 (ex. @TestInstance(value = PER_CLASS)) 테스트들의 실행 순서가 반드시 필요하다면 **@TestMethodOrder**을 사용
 - 종류
	 - @DisplayName
	 - @Order(n) 
	 - Random 


8] 매개변수를 바꿔가면서 동일한 테스트 실행 코드 
```java
@Order(4)
@DisplayName("스터디 만들기")
@ParameterizedTest(name = "{index} {displayName} message={0}")
@CsvSource({"10, '자바 스터디'", "20, 스프링"})
void parameterizedTest(@AggregateWith(StudyAggregator.class) Study study) {
	System.out.println(study);
}

static class StudyAggregator implements ArgumentsAggregator {
	@Override
	public Object aggregateArguments(ArgumentsAccessor accessor,
	ParameterContext context) throws ArgumentsAggregationException {
	
		return new Study(accessor.getInteger(0), accessor.getString(1));
	}
}
```
-  명시적인 타입 변환
	-  SimpleArgumentConverter 상속 받은 구현체 제공
	-  @ConvertWith 
