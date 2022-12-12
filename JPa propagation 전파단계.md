----
#### | Propagation
To configure the transaction propagation strategy for EJB components, Java EE defines the
@TransactionAttributea annotation. Since Java EE 7, even non-EJB components can now be
enrolled in a transactional context if they are augmented with the @Transactionalb annotation.
In Spring, transaction propagation (like any other transaction properties) is configurable via
the @Transactionalc annotation.
ahttp://docs.oracle.com/javaee/7/api/javax/ejb/TransactionAttribute.html
bhttp://docs.oracle.com/javaee/7/api/javax/transaction/Transactional.html
chttps://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/transaction/annotation/
Transactional.html#propagation--
Table 6.6: Transaction propagation strategies
| propagation   | Java EE | Spring | Description                                                                                                                                                        |
| ------------- | ------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| REQUIRED      | yes     | yes    | This is the default propagation strategy, and it only starts  a transaction if and only if the current thread is not already associated with a transaction context |
| REQUIRES_NEW  | yes     | yes    | Any currently running transaction context is suspended=and replaced by a new transaction                                                                           |
| NOT_SUPPORTED | yes     | yes    |  Any currently running transaction context is suspended, and the current method is run outside of a transaction                                                                                                                                                                  |
| MANDATORY     | yes     | yes    | The current method runs only if the current thread is already associated with a transaction context                                                                                                                                                                   |
| NESTED        | no      | yes    | The current method is executed within a nested transaction if the current thread is already associated with a transaction. Otherwise, a new transaction is started.                                                                                                                                                                   |
| NEVER         | no      | yes    | The current method must always run outside of a transaction context, and, if the current thread is

----
[참고]
https://stackoverflow.com/questions/8490852/spring-transactional-isolation-propagation
https://oingdaddy.tistory.com/28
https://n1tjrgns.tistory.com/266
