----

```java
import java.math.BigInteger;  
import java.util.List;  
  
public class InfiniteRecursion {  
    class Board {  
        private BigInteger boardId;  
        @OneToMany(mappedBy = "board")  
        private List<Post> postList;  
        private String category;  
    }  
    class Post {  
        private BigInteger postId;  
        @ManyToOne(fetch = FetchType.LAZY)  
        private Board board;  
        private String title;  
        private String content;  
    }  
}
```

###### ✅ 순환 참조란? 

참조하는 대상이 서로 물려 있어 참조할 수 없게 되는 현상
* 순환 참조는 1:N , N:1, 양방향 관계 일어날 수 있음. 
	* 양방향 연결된 Entity 그대로 조회하다 정보가 순환하며 `stackoverflow` 발생 
* @ResponseBody(rest api) 구현시 JSON 형태로 반환 위해 Jackson 라이버리 이용하는데, (직렬화 이용하여 JSON 형태로 객체 변환) getter 호출 과정에선 `stackoverflow` 발생 
➡️ 예시]]
```
// Board > Post > Board > Post 계속해서 참조

board : {
   boardId : 1,
   postList : [
	   {
		   postId : 1,
		   board : {
			   boardId: 1,
			   postList : [{
				   postId : 1,
				   board : {
					   boardId : 1,
					   postLList: .......
				   }
			   }]
		   }
	   }
   ]
}
```

----
[script]
JPA에서 양방향으로 연결된 Entity를 그대로 조회하는 경우 서로의 정보를 순환하면서 조회하다가 stackoverflow가 발생하게 된다.
※ Spring Boot는 @ResponseBody(rest api)를 구현할 시 Object를 JSON 형태로 변환하기 위해 Jackson 라이브러리를 이용하는데,
Jackson은 entity의 getter를 호출하고, 직렬화를 이용해 JSON 형태로 객체를 변화시키고 view로 전달하는데
getter를 호출하는 과정에서부터 순환 참조가 계속 발생해 view로 전달하면서 stackoverflow가 발생하게 된다.
※ 직렬화란, 객체의 내용을 바이트 단위로 변환하여 파일 또는 네트워크를 통해 스트림(송수신)하도록 하는 것을 의미한다.

----

###### ✅ 해결 방법 

> ➡️ @JsonManagedReference / @JsonBackReference

부모 클래스(Posts entity)의 Comment 필드에 @JsonManagedReference를, 자식 클래스(Comment entity)의 Posts 필드에 @JsonBackReference를 추가해주면 순환 참조를 막을 수 있다.

```java
	class Board {  
        private BigInteger boardId;  
        @OneToMany(mappedBy = "board")  
        @JsonManagedReference
        private List<Post> postList;  
        private String category;  
    }
      
    class Post {  
        private BigInteger postId;  
        @ManyToOne(fetch = FetchType.LAZY)  
        @JsonBackReference 
        private Board board;  
        private String title;  
        private String content;  
    }  
```

> ➡️ @JsonIgnore/ @JsonFilter 

이 어노테이션을 붙이면 JSON 데이터에 해당 프로퍼티는 null로 들어가게 된다.
즉, 데이터에 아예 포함시키지 않는다

> ➡️ @JsonIgnoreProperties

부모 클래스(Posts entity)의 Comment 필드에 @JsonIgnoreProperties({"posts"}) 를 붙여주면 순환 참조를 막을 수 있다.

> ➡️ @NamedEntityGraphs

> ➡️  ⭐DTO 객체를 만들어서 반환

 위와 같은 상황이 발생하게된 주원인은 '양방향 매핑'이기도 하지만, 더 정확하게는 Entity 자체를 response로 리턴한데에 있다. entity 자체를 return 하지 말고, DTO 객체를 만들어 필요한 데이터만 옮겨담아 Client로 리턴하면 순환 참조 관련 문제는 애초에 방지 할 수 있다.

```java
	class BoardDTO {  
        private BigInteger boardId;  
        @OneToMany(mappedBy = "board")  
        @JsonManagedReference
        private List<Post> postList;  
        private String category;  

		
    }
      
    class PostDTO {  
        private BigInteger postId;  
        @ManyToOne(fetch = FetchType.LAZY)  
        @JsonBackReference 
        private Board board;  
        private String title;  
        private String content;  
    }  
```

> ➡ 매핑 재설정 

양방향 매핑이 꼭 필요한지 다시 한번 생각해볼 필요가 있다. 만약 양쪽에서 접근할 필요가 없다면 단방향 매핑을 해줘서 자연스레 순환 참조 문제를 해결하자.





-----
[참고]
https://dev-coco.tistory.com/133
https://go-coding.tistory.com/80

https://data-make.tistory.com/727

https://ksh-coding.tistory.com/38
https://vividswan.tistory.com/entry/JPA-%EC%96%91%EB%B0%A9%ED%96%A5-%EB%AC%B4%ED%95%9C-%EC%B0%B8%EC%A1%B0
https://thxwelchs.github.io/JPA%20%EC%96%91%EB%B0%A9%ED%96%A5%20Entity%20%EB%AC%B4%ED%95%9C%20%EC%9E%AC%EA%B7%80%20%EB%AC%B8%EC%A0%9C%20%ED%95%B4%EA%B2%B0/
