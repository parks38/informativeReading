- collection 1:N
- association 1:1

```mybatis
<select id="selectUser" parameterType="hashmap" resultType="hashmap">

select user_id as userId
     , user_nm as userNm  
 from t_user_m
where user_id = #{0.a}
 and user_id = #{1.b} 

</select>
```


https://codevang.tistory.com/276




