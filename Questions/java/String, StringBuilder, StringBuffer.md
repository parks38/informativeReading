----

* 문자열 다루는 클래스 : String, StrinBuilder, StringBuffer 
	* 연산 횟수가 많아지거나 멀티 쓰레드, Race condition 등의 상황이 발생하면 적절한 클래스를 사용해주어야 함. 

**String은 짧은 문자열을 더할 경우 사용합니다.**
**StringBuffer는 스레드에 안전한 프로그램이 필요할 때나, 개발 중인 시스템의 부분이 스레드에 안전한지 모를 경우 사용하면 좋습니다.**
**StringBuilder는 스레드에 안전한지 여부가 전혀 관계 없는 프로그램을 개발할 때 사용하면 좋습니다.**

연산이 많은 경우, ==StringBuilder > StringBuffer >>> String==


| String                 | StringBuilder | StringBuffer |
| ---------------------- | ------------- | ------------ |
| 불변                   | 가변          | 가변         |
| 스토리지 - string pool | Heap          | Heap         |
| thread safe (내부 데이터 공유 가능)        | no            | thread safe  |
| 동기화                 | 비동기화      | 동기화       |
| 빠르다                 | 빠르다        | 느리다       |

> String

 - immutable (불변)의 속성을 갖는다. 
 - String 클래스는 불변이기에 문자열을 수정하는 시점에서 새로운 String 인스턴스가 생성된다. 
 - ==새로운 메모리영역을 가리키게 변경되고 처음 선언된 값이 할당되어 있는 메모르 영역은 Garbage로 남아있다가 GC 에 의해 사라진다.==
 - `힙 메모리` 에 많은 임시 가비지가 생성되어 힙메모리 부족으로 애플리케이션 성능에 문제를 줄수 있다. 
![[Pasted image 20221212154759.png]]

 > StringBulder/StringBuffer
 
 * 가변 (mutable) 성격을 가지고 있다. 
 * `동일 객체내에서 문자열을 변경하는 것이 가능하다.`
![[Pasted image 20221212155016.png]]

⭐ StringBuffer vs. StringBuilder

동기화의 유무로써 **StringBuffer**는 `동기화 키워드`를 지원하여 ==멀티쓰레드 환경에서 안전하다는 점(thread-safe)== 입니다.  참고로 ==String도 불변성==을 가지기때문에 마찬가지로  ==멀티쓰레드 환경에서의 안정성(thread-safe)==을 가지고 있습니다.

 **StringBuilder**는 동기화를 지원하지 않기때문에 멀티쓰레드 환경에서 사용하는 것은 적합하지 않지만 동기화를 고려하지 않는 만큼 `단일쓰레드에서의 성능은 StringBuffer 보다 뛰어`납니다.