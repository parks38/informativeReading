### [스프링 없이 의존성 주입]
* 새성자를 통한 의존성 주입
* 속성을 통한 의존성 주입

### [스프링을 통한 의존성 주입]
* XML 파일 사용
* 스프링 설정 파일(xml)에서 속성 주입
* @Autowired 통한 속성 주입
  - type 기준 매칭 : 같은 타입을 구현한 클래스가 여러개 존재하면 bean 태그 id 로 구분
* @Resource 통한 속성 주입

### 📌 @Autowired vs. @Resource vs.<property> 태그
 | |@Autowired | @Resource | 
  |---|---|---|
  |출처| 스프링 애노테이션 | 자바 애노테이션 | 
  |빈 검색 우선순위 | type > id | id > type | 
  |특이사항 | @Qualifier("") 혐업 | name 애트리뷰트 |
  |byName 강제 | @Qualifier("id") | @Resource(name="id")|

  ### 📌xml 설정 
  | 사례 | @Autowired |@Resource |
  |---|---|---|
  | 한개의 빈이 id 없이 인터페이스 구현 | O |O|
  |두개의 빈이 id 없이 인터페이스 구현 | no unique bean of type |  no unique bean of type |
  | 두개의 빈이 인터페이스를 구현하고 일치하는 id 를 가지는 경우 | O |O |
  |두개의 빈이 인터페이스를 구현하고 일치하는 id가 없는 경우 | no unique bean of type | no unique bean of type|
  |일치하는 id가 하나 있지만 인터페이스를 구현하지 않은 경우 | No matching bean of type | bean name must be of type|
  
  * <property> 유지보수성이 좋음
  * Resource : 다른 프레임워크 교체되는 경우 대비해서 좋음
  * DI 는 외부의 의존 대상을 주입하는 것을 말함. 
  * 🌟의존 대상을 구현하고 배치할때 SOLID 와 응집도는 높이고 결합도는 낮추는 기본 원칙에 충실해야 유지보수가 수월

  ❓ 빈 설정 이유
  * 객체의 생성과 의존성 주입을 스프링 프레임워크에게 위임
  * 스프링 프레임워크는 갹체 생성 뿐 아니라 객체의 생명주기 전반에 걸쳐 빈의 생성에서 소멸까지 관리
