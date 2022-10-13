* [Coroutine이란?](#coroutine이란)
* [EventBus란?](#eventbus란)
* [LifecycleScope란](#lifecyclescope란)

* * *

## Coroutine이란?
> [참고](https://wooooooak.github.io/kotlin/2019/08/25/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%BD%94%EB%A3%A8%ED%8B%B4-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0/)

> 최초작성 : 2022.03.15

* Co(협력)+Routine(하나의 태스크, 함수)
* Routine에는 두가지가 존재: Main Routine, Sub Routine
    * Sub Routine은 Main에서 호출하는 시점에서 진입, return을 만나면 탈출
    * Coroutine은 함수에 진입할 수 있는 진입점도 여러개, 빠져나가는 탈출점도 여러개
        * return 문이나 마지막 닫는 괄호를 만나지 않더라도 언제든 나가고 들어갈 수 있음

```kt
// 스레드의 main 함수가 drawPerson()을 호출하면 하나의 코루틴 블럭(함수)이 생성됨
// drawPerson()은 언제든 진입, 탈출할 수 있는 자격이 주어짐
fun drawPerson() {
    /* 실제로 startCoroutine은 없음. 직관적 이해를 위해 이 코드에서만 사용 */
    startCoroutine {
        // 코루틴 함수가 실행되는 과정에서 suspend 키워드를 가진 함수를 만나게 되면,    
        // 더이상 아래 코드를 실행하지 않고 멈춤(suspend) 후 코루틴 블럭 탈출
        drawHead()
        drawBody()
        drawLegs()
        // 다른 코드들이 실행되다가도, drawHead가 끝이나면
        // 다시 코루틴으로 진입해 아까 멈춘 부분(drawHead) 아래부터 다시 실행됨
    }
}

suspend fun drawHead() {
    delay(2000)
}

suspend fun drawBody() {
    delay(5000)
}

suspend fun drawLegs() {
    delay(3000)
}
```

* * *

## EventBus란?
>   최초작성 : 2022.03.15

버스 네트워크의 동작 방식과 유사하게 하나의 이벤트를 동일 채널에 연결된 모든 노드(Subscriber)들에게 보내는 방식 <br>
각 노드들은 채널에 연결되어 있기만 하다면 이벤트를 채널에 보내는 얕은 결합으로도 간단하게 데이터를 전달받을 수 있음

```kt
class MainFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) //코드
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = this
        return binding.root
    }
    
    override fun onResume() {
        super.onResume()

        // 이벤트버스 등록
		// 안드로이드 문서에서는 onStart에서 등록하고 onStop에서 해제 권고
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }
    
    override fun onPause() {
        super.onPause()
        
        // 이벤트버스 해제
		// 안드로이드 문서에서는 onStart에서 등록하고 onStop에서 해제 권고
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }
    
    // @Subscribe를 통해 데이터를 불러옴
    // ThreadMode.MAIN: 메인 스레드에서 이벤트 처리
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printId(event: CallEvent) {
        Toast.makeText(context, "${event.id}", Toast.LENGTH_SHORT).show()
    }
}
```
```kt
class CallEvent(val id: Int)
```
```kt
// 이벤트 발생 : post(발생시키고 싶은 이벤트) -> CallEvent
EventBus.getDefault().post(CallEvent(id))
```
* 안드로이드 문서에서는 onStart에서 등록하고 onStop에서 해제 권고

* * *

## LifecycleScope란?

Kotlin 코루틴은 비동기 코드를 작성할 수 있게 하는 API를 제공함.<br>
Kotlin 코루틴을 사용하면 코루틴이 실행되어야 하는 시기를 관리하는 데 도움이 되는 CoroutineScope를 정의할 수 있음 


LifecycleScope는 각 Lifecycle 객체에서 정의됨.<br>
이 범위에서 실행된 코루틴은 Lifecycle이 끝날 때 제거됨.<br>
lifecycle.coroutineScope 또는 lifecycleOwner.lifecycleScope 속성을 통해 Lifecycle의 CoroutineScope에 엑섹스 가능

```kt
class MyFragment: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            val params = TextViewCompat.getTextMetricsParams(textView)
            val precomputedText = withContext(Dispatchers.Default) {
                PrecomputedTextCompat.create(longTextContent, params)
            }
            TextViewCompat.setPrecomputedText(textView, precomputedText)
        }
    }
}
```

### 재시작 가능한 수명 주기 인식 코루틴
Lifecycle과 LifecycleOwner는 실행 정지 repeatOnLifecycle API 제공

```kt
class MyFragment : Fragment() {

    val viewModel: MyViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // lifecycle coroutine 생성
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.someDataFlow.collect {
                    // Process item
                }
            }
        }
    }
}
```

### 수명주기 인식 코루틴 정지
Lifecycle은 lifecycle.whenCreated, lifecycle.whenStarted, lifecycle.whenResumed와 같은 추가 메서드 제공<br>
-> 블록 내부에서 실행되는 코루틴은 Lifecycle이 원하는 최소한의 상태가 아니면 정지됨

```kt
class MyFragment: Fragment {
    init { // Notice that we can safely launch in the constructor of the Fragment.
        lifecycleScope.launch {
            whenStarted {
                // The block inside will run only when Lifecycle is at least STARTED.
                // It will start executing when fragment is started and
                // can call other suspend methods.
                loadingView.visibility = View.VISIBLE
                val canAccess = withContext(Dispatchers.IO) {
                    checkUserAccess()
                }

                // When checkUserAccess returns, the next line is automatically
                // suspended if the Lifecycle is not *at least* STARTED.
                // We could safely run fragment transactions because we know the
                // code won't run unless the lifecycle is at least STARTED.
                loadingView.visibility = View.GONE
                if (canAccess == false) {
                    findNavController().popBackStack()
                } else {
                    showContent()
                }
            }

            // This line runs only after the whenStarted block above has completed.

        }
    }
}
```
코루틴이 활성 상태인 동안 when 메서드 중 하나를 통해 Lifecycle이 끝나면 코루틴은 자동 취소됨
```kt
class MyFragment: Fragment {
    init {
        lifecycleScope.launchWhenStarted {
            try {
                // Call some suspend functions.
            } finally {
                // This line might execute after Lifecycle is DESTROYED.
                if (lifecycle.state >= STARTED) {
                    // Here, since we've checked, it is safe to run any
                    // Fragment transactions.
                }
            }
        }
    }
}
```