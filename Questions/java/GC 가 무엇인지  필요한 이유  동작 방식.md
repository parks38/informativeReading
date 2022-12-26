----
[꼬리 질문]
* GC란 무엇이고, 왜 써야할까요?
* 개발자가 메모리에 대해 신경을 덜 쓸 수 있어서 편해지는데, 그에 따른 단점은 없을까요?
* 개발자가 GC 튜닝을 하는 궁극적인 목표는 무엇일까요?
* G1GC부터는 GC튜닝에 크게 손이 가진 않는데, G1GC는 어떻게 만들었길래 개발자가 튜닝을 이전보다 덜 해도 되는걸까요? 

> 키워드

-   `성능`
-   `Stop the world`


> 가바지 컬렉터? 

메모리를 명시적으로 해제하지 않기 때문에 더 이상 필요 없는 (쓰레기) 객체를 찾아 지우는 작업을 말한다. 
두가지 가설 하에 만들어졌다.
1) 대부분 객체는 금방 접근 불가능 상태 (unreachable)이 된다. 
2) 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다. 

해당 가설의 장점을 살리기 위해 Hotspot VM 은 `Yound 영역`과 `Old 영역`이라는 2개의 물리적 공간으로 나누었다. 

![[Pasted image 20221214185355.png]]

* Young 영역: 새롭게 생성한 객체의 대부분이 여기에 위치하면 ==금방 접근 불가능 상태가 되기 때문에 매우 많은 객체들이 해당 영역에서 생성==되었다가 사라진다. 객체가 사라질때는 ==Minor GC==가 발생한다고 한다. 
* Old 영역: ==접근 불가능 상태로 되지 않아 Yound 영역에서 살아남은 객체는 해당 영역으로 복사==된다. Younf 영역보다 크게 할당되며 CG는 Young 보다 더 적게 발생한다. 영역에서 객체가 사라질때는 ==Major GC==가 발생한다고 한다.

* `Permanent Generation 영역`은 Method Area 라고 하며 ==객체나 억류(itnern)된 문자열 정보를 저장하며== Old 영역에서 살아 남은 객체가 영원히 남아 있는 곳은 아니다. 이 영역에도 GC 가 발생할수 있으며 Major GC의 횟수에 포함된다. 



==stop-the-world==가 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다.
GC 튜닝이란 주로 stop-the-world 시간을 줄이는 것이다. 


[참고]
java Refernece와 GC
https://d2.naver.com/helloworld/329631

java garbage collection ; https://d2.naver.com/helloworld/1329
GC 튜닝 : https://d2.naver.com/helloworld/37111
GC 모니터링 방법 : https://d2.naver.com/helloworld/6043


[[Java Garbage Collection 상세 분석]]