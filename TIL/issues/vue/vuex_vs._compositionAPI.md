## vuex vs. composition api

### Vuex

모든 컴포넌트에서 접근 가능한 store 이 있고 앱내에서 state 을 보유한 컨테이너이다. 
중앙 집중식 저장소 역할을 통한 상태 관리를 하고 있다. 
devtools 확장 프로그램과 통합되어 디버깅 및 상태 스냅 샷 기능을 제공한다. 

[특징]
1. store 은 반응형이다. 
	- 어느 곳에서도 store 의 state 를 찾을 수 있고 state 가 변경되면 신속하게 view 도 변경된다. 

2. store 내의 state 는 view 에서 임의 변경은 불가능하다. 
	- vuex 에서 허락한 방식은 commit 을 이용한 `mutation` 또는 `dispatch` 만 가능하다. 

```ad-warning
강제로 store 에 주입하면 vuex-warning 이 뜬다. 
```

> vuex 구조 

vuex 는 state, mutations, actions, getter 4가지 형태로 관리되며 해당 포인트들을 store 라고 불린다. 서로간의 간접적 영향이 있으며 단방향 데이터 흐름이다. 

* state

vue 컴포넌트에서 data 역할을 한다. 원본 소스의 역할이며 view 와 직접적으로 연결되어 있는 Model 이다. view 에서의 변경은 불가능하고 `mutation` 을 통해 변경 가능하다. 

* mutations

state 를 변경하는 유일한 방법이자 이벤트와 유사하다. 함수로 구현되며 첫번째 인자로는 state를 받을 수 있으며 두번째 인자는 payload 를 받을 수 있다. (payload 는 객체 형태로 사용 가능하다). `mutations` 는 직접 호출은 불가능하며 `commit` 을 통해 호출 가능하다. 
*데이터 가공하요 state 설정하는 역할 담당 *

```js
store.commit('setData' payload);
```

* actions

`mutations` 과 비슷하지만 *비동기 작업*이 가능하다. 
`action` 에서도 `mutation`  을 통해 `state` 변경이 가능하다. 
첫번째 인자로는 context 인자를 받을 수 있으며 context 에는 `state` `commit` `disptach` `rootstate` 와 같은 속성들을 포함한다. 
두번째 인자로는 payload 를 받을 수 있다. 

actions 는 `dispatch` 를 통해 호출한다. 

*Axios 를 통한 API 호출과 결과에 대한 반환(return) 하거나 mutation 으로 commit 하는 상태 용도로 사용*

```js
store.dispatch('setData', payload)
```

* Getters

vue component 에서 `computed` 로 볼 수 있다. 계산된 속성을 말하며 `getter` 의 결과는 종속성에 따라 캐시되고 일부 종속성이 변경된 경우에만 다시 재계산된다. 

%%
특정 state 에 대해 연산을 하고 그 결과를 view 에 바인딩 하며
state의 변경 여부 따라 getter 는 재계산을 하고 
view 에 업데이트 해준다. 
state 는 원본 데이터로서 변경이 일어나지 않는다. 
%%


> vuex 흐름

![[Pasted image 20220919093449.png]]


단방향으로 `view` 에서 `Dispatch` 라는 함수를 통해 `action` 을 발동하고 안에 정의된 `commit` 함수에 의해 `mutation` 실행된다. 
`mutations` 에 정의된 로직에 `state` 가 변화하고 `state` 를 사용하고 있는 `view` 에 변화한다. 

```js
const store = new Vuex.Store({
  state: {
    count: 0
  },
  mutations: {
    increment(state) {
      state.count++;
    }
  }
});
```

`count` 를 사용하는 vue 파일에서 `store.state` 로 접근하여 `store.commit` 메소드로 상태 변경한다. 

```js
store.commit("increment");
```

[패턴]
`view` => `action(Dispatch)` => `commit(Mutation)` => `state` => `view` (단방향 데이터 흐름)

==vuex는 여러 컴포넌트에서 공유하는 상태 관리에는 유용하지만 한개의 컴포넌트에서만 사용한다면 vuex보단 간단한 [이벤트버스만]으로 데이터 처리하는 것을 추천합니다.

#### | Vuex로 해결할 수 있는 문제
-   MVC 패턴에서 발생하는 구조적 오류
-   컴포넌트 간 데이터 전달 명시
-   여러 개의 컴포넌트에서 같은 데이터를 업데이트 할 때 동기화 문제(mutation, actions)
    
출처:  [개발자의 기록습관:티스토리]

#### | 예시 

**store/index.js**
```js
import Vue from 'vue'  
import Vuex from 'vuex'  
  
import BookStore from './book'  
  
Vue.use(Vuex)  
  
export default new Vuex.Store({  
modules: {  
book: BookStore  
}  
})
```

**store/book/book.js**
```js
// state  
const state = {  
	message: 'Hello'  
}  
  
// mutations  
const mutations = {  
	changeMessage (state, newMsg) {  
		state.message = newMsg  
	}  
}  
  
// actions  
const actions = {  
	callMutation ({ state, commit }, { newMsg }) {  
		commit('changeMessage', newMsg)  
	}  
}  
  
// getters  
const getters = {  
	getMsg (state) {  
		return `${state.message} => Length : ${state.message.length}`  
	}  
}  
  
export default {  
	state,  
	mutations,  
	actions,  
	getters  
}
```

**Sample.vue**
```js
import { createNamespacedHelpers } from 'vuex'  
  
const  
	bookHelper = createNamespacedHelpers('book'),  
	bookListHelper = createNamespacedHelpers('bookList')  
  
export default {  
	name: 'Sample',  
	computed: {  
		...bookHelper.mapState({  
			message: state => state.message // -> this.message  
		}),  
		...bookHelper.mapGetters([  
			'getMsg' // -> this.getMsg  
		]),  
		...bookListHelper.mapState({  
			messageList: state => state.messageList // -> this.messageList  
		}),  
		...bookListHelper.mapGetters([  
			'getMsgList' // -> this.getMsgList  
		])  
	},  
	methods: {  
		...bookHelper.mapMutations([  
			'changeMessage' // -> this.changeMessage()  
		]),  
		...bookHelper.mapActions([  
			'callMutation' // -> this.callMutation()  
		]),  
		...bookListHelper.mapMutations([  
			'changeMessageList' // -> this.changeMessageList()  
		]),  
		...bookListHelper.mapActions([  
			'callMutationList' // -> this.callMutationList()  
		]),  
	}  
}
```


### Composition API 

컴포넌트의 로직을 유연하게 구성할 수 있는 함수 기반의 API 이다. 
코드의 재사용성[^1]과 타입 추론이 크게 개선되어 있다. 

기능 :
* reactivity API : `ref()`  / `reactive` => state, computed state, watcher 생성 
* lifecycle hook : `onMounted()` / `onUnounted` 등...
* Dependebcy Injection : `porovide()`, `inject()` 

#### | 반응형 

컴포지션 API 는 [reactive, ref] 의 변경 가능한 반응형 데이터를 만든다
computed 의 경우는 반응형이지만 읽기만 가능하다. 

```js
import { reactive, ref, computed } from '@vue/composition-api'

// 1. reactive
const reactiveValue = reactive({ name : 'tom' })
reactiveValue.name // tom

// 2. ref
const refValue = ref(10)
refValue.value // 10
```

* reactive

`reactive` 은 생성을 담당하며 오직 객체만을 받는다. 인자로 받은 객체는
==완전히 동일한 프록시 객체를 반환하고== 값에 접근하는 방법은 
원본 객체에 접근하는 것과 동일하다. 

생성된 객체는 모두 `깊은 감지` 를 수행하기엔
객체가 중첩된 상황에서도 반응형 데이터를 쉽게 조작하고 처리가 가능하다. 

* ref

`reactive`  와 동일하게 객체를 생성하며 모든 원시타입 (primitive) 값을 포함한 여러가지 타입의 값을 받을 수 있다. 원본 값은 ref객체의 value 속성을 통해 접근하며 
변경할 때도 value 속성에 접근하여 조작한다. 

ref의 값으로 객체가 전달 될 경우,
reactive 메소드를 통해 깊은(deep) 감지를 수행하기에 reactive 는 필요하다. 

#### | reactive vs. ref

![[Pasted image 20220919100913.png]]

`ref` 객체는 원본 값을 value 라는 속성에 담아두고 변경을 감지하며 
`reactive` 은 원본 객체 자체에 변경을 감지하는 옵저버를 추가하여 그대로 반환하다. 

1.  ref는 function에서 값을 변경할 때 ref.value를 넣어주고 값을 바꾸나 reactive는 바로 값을 바꿀 수 있습니다.
2.  **reactive는 원시값에 대해서는 반응형을 가지지 않습니다.** (string, number 값은 값을 바꾸어도 reactive하게 리렌더링 되지 않는다) 그래서 객체나 배열을 사용하는 경우에만 reactive를 사용할 수 있습니다, 그러나 ref는 원시값도 반응형 값으로 취급되어 리렌더링 됩니다.
3.  reactive나 ref나 둘 중 하나만 사용하는 것이 옳다고 생각합니다. 그런데 reactive는 원시값을 반응형으로 사용되지 않기 때문에 ref를 처음부터 끝까지 사용하는 것이 좋다고 생각합니다.

#### | computed

 computed 는 getter 함수가 반환하는 값에 대해 ==변경불가능한 데이터== 를 반환한다. 
```js
imported { ref, computed } from '@vue/composition-api'

export defualt {

	setup () {
		const myValue = ref(10)
		const value2 = computed(() => myValue * 2)

		return {
			value2	
		}
	}
}
```

#### | watch (변경 감지)

composition api 또한 변경 감지가 가능하며 deep, immediate 옵션을 가지고 있다. 

```js
import { ref, watch } from '@vue/composition-api'

const useUserList = () => {
	const userList = ref([
		{ id: 1, name: 'tom'},
		{ id: 2, name: 'jessica'},
		{ id: 3, name: 'daniel'}
	])

	watch(userList, (newVal, oldVal) => {
		//do stuff when userLsit was chenged ...
	}, {deep: true})
		return { userList }
}

export default {
	setup() {
		const { userList } = useUserList()

		setInterval(() => {
			// add '!' to first user name (tom)
			userList.value[0].name += '!'
		}, 3000)
	}
}
```

watch 를 통해 반응형 값에 대한 변경 감지를 하며
첫번째 인자는 getter가 반호나하는 반응형 값 혹은 ref값이 변경될 경우에는
두번째 인자로 전달된 콜백을 실행한다. 

객체가 중첩된 데이터 구조를 갖는 경우에는 세번째 인자로
`deep` 과 같은 옵션을 활성화 하므로 내부 값의 변경도 갑지 할 수 있다. 

> 예시 

```js
<script>
import { computed, ref, watch, watchEffect } from "vue";

export default {
  name: "HOME",
  setup() {
    const search = ref("");
    const names = ref(["qq", "aa", "zz", "dd"]);

    const matchingNames = computed(() => {
      return names.valie.filter(name => name.includes(search.value));
    });

    watch(search, () => {
      "search 값이 바뀔 때 마다 실행되는 함수";
    });

    watchEffect(() => {
      console.log(
        "search value가 정의됬기에 search가 바뀔때마다 실행된다",
        search.value
      );
    });

    return { names, search, matchingNames };
  }
};
</script>
```


#### | 생명 주기 훅 (Life Cycle Hook)

```js
import {
	onMounted,
	onUpdated,
	onUnmounted
} from '@vue/composition-api'

export default {
	setup () {
		onMounted () => {
		// mounted	
		},
		onUpdated () => {
			// updated
		},
		onUnmounted () => {
			// destroyed
		}
	}
}
```

생명주기 훅에 해당하는 메소드 인자로 해당 주기 때 ==호출될 콜백 함수를 전달== 한다. 
| vue2          | composition API |
| ------------- | --------------- |
| beforeCreate  | setup           |
| created       | setup           |
| beforeMount   | onBeforeMount   |
| beforeUpdate  | onBeforeUpdate  |
| beforeDestroy | onBeforeDestroy |
| mounted       | onMounted       |
| updated       | onUpdated       |
| destroyed     | onDestoryed     |
| errorCaptured | onErrorCaptured |
#### | Props 

부모 컴포넌트에서 pros 를 내릴 경우 ==상위에서 어떤 props 를 받을지 알려준후 setup 에서 pros.xxx 로 접근==한다. 

> 예시 

home -> propsList로 내리고  propsList 에서 props 를받아 사용하고 home 에서 propsList 사용 방법이다. 

*home*
```vue
<template>
  <dlv class="home">
    <!-- child 컴포넌트에게 props 내림 -->
    <PostList :posts="posts" />
  </div>
</template>
<script>
  // 사용할 컴포넌트 import
  import PostList from '../components/PostList.vue'
  import { ref } from 'vue';

  export default {
    name: 'Home',
    // 사용할 컴포넌트를 넣어줍니다.
    components: { PostList },

    setup() {
      const posts = ref([
        { title: '1번 타이틀', body: '1번 제목', id: 1 },
        { title: '2번 타이틀', body: '2번 제목', id: 2 },
      ]);

      return { posts }
    }
  }
</script>
```

*posts*
```vue
<template>
  <div>
    {{ post.title }}
    {{ post.body }}
  </div>
</template>

<script>
export default {
  // 사용할 props를 배열내에 정의합니다.
  props: ["posts"],
  setup(props) {
    console.log(props.posts); // 받은 prop 사용가능
  }
};
</script>
```

#### | utils 함수 재사용 (composable)

composition api 사용하며 재사용하는 util 함수를 `import/export` 가능하게 되어
데이터 추적 및 사용하기 쉬워졌다. 

**src/composable/getPost.js**

```js
import { ref } from "vue";
const getPosts = () => {
  const posts = ref([]);
  const error = ref(null);

  const load = async () => {
    try {
      // 예시 api
      let res = await fetch("http://localhost:3000/posts");
      if (!res.isSuccess) {
        throw Error("fail");
      }
      posts.value = await res.json();
    } catch (err) {
      error.value = err.message;
    }
  };

  return { posts, err, load };
};

export default getPosts;
```

**Home** (composable 함수 사용하는 컴포넌트)
```vue
<template>
  <dlv class="home">
    <div v-if="error">{{ error }}</div>
    <div v-if="posts.length">
      <PostList :posts="posts" />
    </div>
    <div v-else>loading...</div>
  </div>
</template>
<script>

// 사용할 컴포넌트 import
import PostList from '../components/PostList.vue';
import getPosts from '../composables/getPosts';

export default {
  name: 'Home',
  components: { PostList },

  setup() {
    const { posts, error, load } = getPosts();

    load();

    return { posts, error };
  };
}
</script>
```

---
[^1] : mixin 을 통해 컴포넌트 로직을 재사용할수 있지만 오버라이딩이나 다중 믹스인을 사용하면 컴포넌트 관리가 어려워 진다는 점이 아쉽다. 

[^참고] : https://velog.io/@bluestragglr/Vue3-%EB%AC%B4%EC%97%87%EC%9D%B4-%EB%B0%94%EB%80%8C%EB%82%98%EC%9A%94#computed-%EC%86%8D%EC%84%B1-%EC%82%AC%EC%9A%A9%EC%9D%98-%EB%B3%80%ED%99%94

