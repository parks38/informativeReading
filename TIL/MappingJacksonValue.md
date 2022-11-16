
Response 필터링 데이터 
- 클라이언트 에게 보여질 데이터만 보낸다. 
- @JsonIgnore, @JsonIgnoreProperties
```java
@JsonIgnoreProperties(value={"password", "ssn"})
```

* @JsonFilter
	: 추가하여 조건에 맞는 필드 설정 
	```java
@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password", "ssn"})
@JsonFilter("UserInfo")
public class User {
    private Integer id;

    @Size(min=4, message = "Name 4글자 이상 입력 해야함")
    private String name;
    @Past
    private Date joinDate;

    private String password;
    private String ssn;
}
```

```java
@GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUsers(@PathVariable int id){
        User user = service.findOne(id);

        if(user == null){
            throw new UserNotFoundException(
            String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", 
                "joinDate", "password", "ssn");

        FilterProvider filters = new SimpleFilterProvider()
        .addFilter("UserInfo", filter);
        
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }
```


```java
@GetMapping("/users")
public MappingJacksonValue retrieveAllUsers(){
    List<User> users = service.findAll();

    SimpleBeanPropertyFilter filter = 
    SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", 
        "joinDate", "password", "ssn");

    FilterProvider filters = new SimpleFilterProvider()
    .addFilter("UserInfo", filter);
    
    MappingJacksonValue mapping = new MappingJacksonValue(users);
    mapping.setFilters(filters);

    return mapping;
}
```

