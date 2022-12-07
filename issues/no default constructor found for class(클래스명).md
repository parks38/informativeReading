
=> java.lang.IllegalStateException: No primary or default constructor found for class com.coco.domain.BoardVO`

기본 생성자를 찾을 수 없다는 에러다.  
그래서 VO클래스에 Lombok의 `@NoArgsConstructor`를 이용하여 기본 생성자를 만들어줬더니,  
`@Builder`에 빨간 줄이 생기면서 `The constructor BoardVO(Long, String, String, String, Date, Date) is undefined`라는 메세지가 나왔다.  
`@Builder`를 쓰면 모든 요소가 포함된 생성자가 함께 생성되는데, 거기다가 `@NoArgs.....`를 붙이면 그 생성자가 기본 생성자로 바뀐다. 따라서, 빌더에 꼭 필요한 생성자가 사라졌다는 에러 메세지를 출력하는 것이다.  
`@AllArgsConstructor`도 함께 붙여주면 해결된다.

```java
@Data
@Builder
// @NoArgsConstructor
@AllArgsConstructor
public class BoardVO {
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date moddate;
}
```