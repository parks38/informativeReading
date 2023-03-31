

https://ahn3330.tistory.com/164

- @NamedEnityGraph
- @JsonIgnore
* `@JsonIdentityInfo`
* `@JsonBackReference` and `@JsonManagedReference`
https://velog.io/@minchae75/Spring-Boot-JPA-%EC%88%9C%ED%99%98-%EC%B0%B8%EC%A1%B0-%ED%95%B4%EA%B2%B0

https://dev-coco.tistory.com/133

https://dev-coco.tistory.com/133 => 예시 



Yes, another solution to handle the infinite recursion issue in JPA is to use the `@JsonFilter` annotation from the Jackson library. This approach allows you to selectively serialize and deserialize properties of your JPA entities based on a predefined filter.

Here's how you can use the `@JsonFilter` annotation to resolve the infinite recursion issue:

1.  Define a filter using the `SimpleBeanPropertyFilter` and `FilterProvider` classes from the Jackson library.

javaCopy code

`import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter; import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;  SimpleFilterProvider filterProvider = new SimpleFilterProvider(); filterProvider.addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept("parent"));`

The `serializeAllExcept` method takes a list of properties that you want to exclude from serialization.

2.  Apply the filter to your JPA entity using the `@JsonFilter` annotation.

javaCopy code

`@Entity @JsonFilter("myFilter") public class Employee {     @Id     @GeneratedValue(strategy = GenerationType.IDENTITY)     private Long id;      private String name;      @ManyToOne(fetch = FetchType.LAZY)     private Employee parent;      // getters and setters }`

In this example, we apply the `myFilter` filter to the `Employee` entity using the `@JsonFilter` annotation. We exclude the `parent` property from serialization by adding it to the filter.

3.  Serialize the JPA entity using the `ObjectMapper` class from the Jackson library and the `filterProvider



https://go-coding.tistory.com/80