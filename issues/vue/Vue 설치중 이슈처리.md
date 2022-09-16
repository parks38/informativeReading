### Vue 설치 중 Issue 
[permission]
```
Error: EACCES: permission denied, mkdir
npm ERR! code EACCES
npm ERR! syscall mkdir
npm ERR! path /usr/local/lib/node_modules/@vue
npm ERR! errno -13
npm ERR! Error: EACCES: permission denied, mkdir '/usr/local/lib/node_modules/@vue'
npm ERR!  [Error: EACCES: permission denied, mkdir '/usr/local/lib/node_modules/@vue'] {
npm ERR!   errno: -13,
npm ERR!   code: 'EACCES',
npm ERR!   syscall: 'mkdir',
npm ERR!   path: '/usr/local/lib/node_modules/@vue'
npm ERR! }
npm ERR!
```

* 해결방법 : sudo 이용해서 권한 부여 
```
sudo npm install -g @vue/cli
```

* 이 이슈는 NPM폴더(경로)가 접근하고 쓰기를 실행할때 권한 설정이 되어있지 않기 때문에 발생한다.
```java
sudo chown -R `whoami` ~/.npm
sudo chown -R `whoami` /usr/local/lib/node_modules

```

* 폴더생성
```terminal
> vue create [이름]
```

* server 
`npm run serve`
```
 DONE  Compiled successfully in 78ms                                 10:54:52 AM


  App running at:
  - Local:   http://localhost:8080/
  - Network: http://192.168.45.26:8080/

```
* 