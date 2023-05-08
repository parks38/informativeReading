----

> Spring Data JPA 

```java
ublic interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

	/**
	 * Returns all entities sorted by the given options.
	 * 
	 * @param sort
	 * @return all entities sorted by the given options
	 */
	Iterable<T> findAll(Sort sort);

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable);
}
```

implements pgination with `PagingAndSortingRepository`

> Spring Data Page

```java
public interface Page<T> extends Slice<T> {
  static <T> Page<T> empty();
  static <T> Page<T> empty(Pageable pageable);
  long getTotalElements();
  int getTotalPages();
  <U> Page<U> map(Function<? super T,? extends U> converter);
}
```

```java
public interface Slice<T> extends Streamable<T> {
  int getNumber();
  int getSize();
  int getNumberOfElements();
  List<T> getContent();
  boolean hasContent();
  Sort getSort();
  boolean isFirst();
  boolean isLast();
  boolean hasNext();
  boolean hasPrevious();
  ...
}
```

A `Slice` object knows less information than a `Page`, for example, whether the next one or previous one is available or not, or this slice is the first/last one. You can use it when you don’t need the total number of items and total pages.

> Spring Data Pageable 

```java
public interface Pageable {
  int getPageNumber();
  int getPageSize();
  long getOffset();
  Sort getSort();
  Pageable next();
  Pageable previousOrFirst();
  Pageable first();
  boolean hasPrevious();
  ...
}
```

```java
Page<Tutorial> findAll(Pageable pageable);
Page<Tutorial> findByPublished(boolean published, Pageable pageable);
Page<Tutorial> findByTitleContaining(String title, Pageable pageable);
```

This is how we create `Pageable` objects using [PageRequest](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageRequest.html) class which implements `Pageable` interface:

```java
Pageable paging = PageRequest.of(page, size);
```

-   `page`: zero-based page index, must NOT be negative.
-   `size`: number of items in a page to be returned, must be greater than 0.

메소드

설명

int getNumber()

현재 페이지의 정보

int getSize()

한 페이지의 크기

int getTotalPages

전체 페이지의 수

int getNumberOfElements()

결과 데이터 수

boolean hasPreviousPage()

이전 페이지의 존재 여부

boolean hasNextPage()

다음 페이지의 존재 여부

boolean isLastPage()

마지막 페이지 여부

Pageable nextPageable()

다음 페이지 객체

Pageable previousPageable()

이전 페이지 객체

List<T> getContent()

조회된 데이터

boolean hasContent()

결과 존재 여부

Sort getSort()



-----
[참고]

https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/
https://jddng.tistory.com/345
