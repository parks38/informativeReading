
카프카 익숙해지기 프로젝트 
- 채팅 프로그램 개발 

[구현 목표]
1.  분산처리를 할 수 있는 카프카 채팅 어플리케이션 만들기
2.  소켓은 특정 유저에게만 붙어서, 다른 유저가 데이터를 가로챌 수 없도록 하기 (그러니까 하나의 소켓을 연결하고 프론트에서 데이터 분배하는 야매짓 X)
3.  Local, DEV 환경 분리
4.  실제로 부하 테스트 해볼 것  
    4-2. 가능하다면 ES, 버로우, 그라파나 활용해서 대시보드로 실제로 부하가 얼마나 걸리는지에 대한 대쉬보드까지 만들어보면 행복할듯👨‍💻
5.  테스트 코드 작성

[기능]
```null
1. 방생성을 하면 실시간으로 사용자에게 방이 생성됨을 알릴 것
2. 방참가를 하면 기존 방에 참여해있던 모든 상대방에게 알릴 것
3. 특정 채팅방에 채팅을 치면 그 방에 존재하는 사용자에게 채팅 내용이 보내질 것
4. 현재 방에 참가한 유저 목록을 띄워줄 것
5. 방을 떠나거나 소켓 연결이 끊기면 참가한 유저 목록에서 해당유저를 삭제할 것
```




[참고]
https://velog.io/@ehdrms2034/Kafka-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%B1%84%ED%8C%85-%EC%96%B4%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EB%A7%8C%EB%93%A4%EA%B8%B0-%EA%B3%84%ED%9A%8D

https://pearlluck.tistory.com/351

https://dev.to/subhransu/realtime-chat-app-using-kafka-springboot-reactjs-and-websockets-lc

https://gaemi606.tistory.com/entry/Spring-Boot-Kafka%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%B1%84%ED%8C%85-3-%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%A3%BC%EA%B3%A0%EB%B0%9B%EA%B8%B0-ReactJS?category=745027


https://velog.io/@kimview/Kafka-%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-%EC%B1%84%ED%8C%85%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8-%EB%A7%8C%EB%93%A4%EA%B8%B0-1

https://www.joinc.co.kr/w/man/12/Kafka/chatting