> POST ] /quantus/getResults

* Data : 
```js
{
	universe : (String) KOR / KOSDAQ / KOSPI,
	filter : (List<String> finanace, PTP, chinese),
	sector: List<String> health, cart, product, chemical ...,
	marketCapHigh : A,
	marketCapLow: B, 
	PriceDTO : priceDTO, 
	EVDTO : evDTO,
	UserConfigDto : userConfigDto
}
```

[DTO]
* PriceDTO 
```java
public class PriceDto  {

	private Integer per;
	private Integer pbr;
	private Integer psr;
	private Integer por;
	private Integer pcr;
	private Integer pfcr;
	private Integer prr;
	private Integer pgpr;
	private Integer peg;
	private Integer par;
	private Integer pacr;
	private Integer ncav;
	private Integer 배당수익률;
	private Integer 주주수익률;
}
```

* EVDTO 
```java
public class EvDto  {

	private Integer ev;
	private Integer evNet;
	private Integer evSales;
	private Integer evEbitda;
	private Integer evEbit;
	private Integer evGp;
	private Integer evRnD;
	private Integer evCf;
	private Integer evAc;
}
```

* UserConfigDto 
```java
public class UserConfigDto {

	private Integer investAmount;
	private Integer count;
	private Integer rebalanceTime;
	private Integer starDate;
	private Integer endDate;
}
```

[Result 항목]
	Code
	Name
	시가총액 (억원)
	업종명
	상장된 시장
	종가 (전일기준)
	현재가 
	매수수량
	총매수금액

[Relation DB]
| 이름     | type        | 주석                  |
| -------- | ----------- | --------------------- |
| userId   | bigint      | 사용자                |
| ResultId | bigint      | 콴터스 이용 내역      |
| name     | varchar(10) | 해당 종목에 대한 이름 |

* 내역은 기간이 지나면 폐기 

##### 회원
* login
* logout
* GET
* POST (수정)

* 회원의 Qunatus 내역 


* FCM 으로 푸쉬 알람으로 해당 날자에 reminder 보내기 
	* 매달 종목/ 개수/ 가격 전송 
	* Java Quartz Scheduler 
		: https://data-make.tistory.com/700
	* https://foot-develop.tistory.com/21
	 * https://github.com/asd9211/kkotalk_alarm

* MSA 
* Kafka
* Messaging Queue
* 