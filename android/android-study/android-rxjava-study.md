* [RxJava란?](#rxjava란) - 2022.05.17
* [Observable이란?](#observable이란) - 2022.03.13
* [Schedulers란?](#schedulers란) - 2022.05.19
* [RxJava: Subject, PublishSubject, BehaviorSubject](#rxjava-subject-publishsubject-behaviorsubject) - 2022.03.13

* * *

# RxJava란?
> 최초작성: 2022.05.17
* RxJava는 ReactiveX(Reactive Entensions)를 Java로 구현한 라이브러리이다.
* RxAndroid는 RxJava에 안드로이드용 스케쥴러 등 몇 가지 클래스를 추가해 안드로이드 개발을 쉽게 해주는 라이브러리이다.
* RxJava는 반응형 프로그래밍이다.
    * 반응형이란? 데이터가 변하면 알아서 캐치하여 결과로 반영됨. (데이터 관찰, 스트림 처리)
* RX = Observable + Observers + Schedulers

* * *

# Observable이란?
> 최초작성: 2022.03.13<br>
> 수　　정: 2022.05.17
* RxJava의 가장 핵심적인 요소 
* 데이터 흐름에 맞게 알림을 보내 Observer가 데이터를 사용할 수 있도록 함
    * 즉, Observable을 이용해 데이터를 회수하고 변환하는 메커니즘을 정의하고, Observer는 이를 구독해 데이터가 준비되면 반응함

1. Observable이 데이터 스트림을 처리하고, 완료되면 데이터를 발행(emit)
2. 데이터를 발행할 때마다 구독하고 있는 모든 Observer가 알림을 받음
3. Observer는 수신한 데이터를 가지고 어떠한 일을 함

## **관찰자(Observer)패턴이란?**
* 옵저버들의 목록을 객체에 등록해서 상태 변화가 있을 때마다 메서드 등을 통해 객체가 직접 목록의 각 옵저버에게 알리도록 하는 디자인 패턴
* 각각의 옵저버들은 관찰 대상인 객체가 발생시키는 이벤트를 받아 처리
<br>:: 이벤트가 발생하면 각 옵저버들은 콜백을 받음

## **어떻게 Subscribe 가는가?**
Observable이 데이터를 발행하고 알림(Event)을 보내면 Observer는 Observable을 구독(Subscribe)해 데이터를 소비(Consume)한다. 실제로는 Observable이 데이터 흐름을 정의하고 알림을 보낸 뒤 Observer가 Subscribe를 해야 데이터가 발행되고 소비된다.

### **Observable의 데이터 발행**
```kt
// Emitter를 통해 알림을 보낸다고 생각하면 된다
public interface Emitter<@NonNull T> {
    void onNext(@NonNull T value);  // 데이터의 발행을 알림
    void onError(@NonNull Throwable error);  // 오류가 발생했음을 알림, 이후에 onNext와 onComplete가 발생하지 않음
    void onComplete(); // 모든 데이터의 발행이 완료되었음을 알림, 딱 한 번만 발생하며 이후에 onNext가 발생하면 안됨
}
```
### **Observer의 Subscribe**
구독(Subscribe)이란 단순하게 수신한 데이터를 가지고 할 행동을 정의하는 것이다. Observer는 subsribe() 메소드에서 수신한 각각의 알림에 대해 실행할 내용을 지정한다.
```kt
public final Disposable subscribe()
public final Disposable subscribe(@NonNull Consumer<? super T> onNext)
public final Disposable subscribe(@NonNull Consumer<? super T> onNext, @NonNull Consumer<? super Throwable> onError)
public final Disposable subscribe(@NonNull Consumer<? super T> onNext, @NonNull Consumer<? super Throwable> onError, @NonNull Action onComplete)
public final void subscribe(@NonNull Observer<? super T> observer)
```
### **총 예제**
```kt
//Observable 생성
Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
        // 데이터 흐름 정의
        emitter.onNext(1);
        emitter.onNext(2);
        emitter.onComplete();
        // onComplete() 이후의 데이터는 발행되지 않음
        emitter.onNext(3);
    }
});

// subscribe 함수를 통해 실제로 데이터를 발행하여 소비함
observable.subscribe(
    // onNext
   new Consumer<Integer>() {
       @Override
       public void accept(Integer integer) throws Throwable {
           System.out.println("onNext : " + integer);
       }
   },
   // onError
   new Consumer<Throwable>() {
       @Override
       public void accept(Throwable throwable) throws Throwable {
           System.out.println("onError : " + throwable);

       }
   },
   // onComplete
   new Action() {
       @Override
       public void run() throws Throwable {
           System.out.println("onComplete");
       }
   }
);
```
```
[실행결과]
onNext : 1
onNext : 2
onComplete
```
### **just()**
가장 간단한 생성 방식이다. 함수에 인자로 넣은 데이터를 차례로 발행한다. 인자로 10개까지 전달할 수 있다. 자동으로 onNext, onComplete 혹은 onError가 호출된다. 데이터가 그대로 발행되므로 다르게 변경하고 싶으면 map과 같은 연산자를 통해 변환해야한다.
```kt
//그대로 발행
Observable.just(1, 2, 3)
        .subscribe(System.out::println);

// 변환하고 싶은 경우
Observable.just(1, 2, 3)
        .map(x -> x * 10)
        .subscribe(System.out::println);
```
```
[실행결과]
1
2
3
[실행결과]
10
20
30
```
### **create()**
Observable을 생성하지만 just()와는 다르게 프로그래머가 직접 onNext, onComplete, onError를 호출해야한다. 위에서 본 예제에서 쓰인 방식이다.
```kt
//그대로 발행
Observable.create(emitter -> {
    emitter.onNext(1);
    emitter.onNext(2);
    emitter.onNext(3);
    emitter.onComplete();
}).subscribe(System.out::println);
```
```
[실행결과]
1
2
3
```
* onNext(): 하나의 소스 Observable에서 Observer까지 한 번에 순차적으로 데이터 발행
* onComplete(): 데이터 발행이 끝났음을 알리는 완료 이벤트를 Observer에 전달하여 onNext()를 더 호출하지 않음
* onError(): 오류가 발생했음을 Observer에 전달

* * *

# Schedulers란?
> 최초작성 : 2022.05.19

* RxJava에서의 Schedulers는 RxJava 비동기 프로그래밍을 위한 쓰레드(Thread) 관리자
    * 즉, 스케쥴러를 이용해서 어떤 쓰레드에서 무엇을 처리할 지에 대해서 제어할 수 있다.
* 스케쥴러를 이용해서 데이터를 통지하는 쪽과 데이터를 처리하는 쪽 쓰레드를 별도로 지정해서 분리 가능
* RxJava의 스케쥴러를 통해 쓰레드를 위한 코드의 간결성 및 쓰레드 관리의 복잡함을 줄일 수 있음
* RxJava에서 스케쥴러를 지정하기 위해서 subscribeOn(), observeOn() 유틸리티 연산자를 사용
    * 생산자(Observable) 쪽의 데이터 흐름을 제어하기 위해서는 subscribeOn() 연산자를 사용한다.
    * 소비자(Observer) 쪽에서 전달받은 데이터 처리를 제어하기 위해서는 observeOn() 연산자를 사용한다.
    * subscribeOn(), observeOn() 연산자는 각각 파라미터로 Scheduler를 지정해야 한다.

## Schedulers의 종류

| Scheduler | 설명 |
|---|---|
|Schdulers.io()|- I/O 처리 작업을 할 때 사용하는 스케쥴러<br>- 네트워크 요청 처리, 각종 입/출력 작업, 데이터베이스 쿼리 등에 사용<br>- 쓰레드 풀에서 쓰레드를 가져오거나 가져올 쓰레드가 없으면 새로운 쓰레드를 생성한다.|
|Schdulers.computation()|- 논리적인 연산 처리 시, 사용하는 스케쥴러<br>- CPU 코어의 물리적 쓰레드 수를 넘지 않는 범위에서 쓰레들르 생성한다.<br>- 대기 시간 없이 빠르게 계산 작업을 수행하기위해 사용한다.|
|Schdulers.newThread()|- 요청시마다 매번 새로운 쓰레드를 생성한다.<br>- 매번 생성되면 쓰레드 비용도 많이 들고, 재사용도 되지 않는다.|

* * *

## 정리
* **Observable**
    * 데이터 스트림
    * 하나의 스레드에서 다른 스레드로 전달 할 데이터를 압축
    * 설정에 따라 lifecycle에서 한번만 데이터를 방출
    * 데이터를 처리하고 다른 구성요소에 전달하는 역할
* **Observers**
    * Observable에 의해 방출된 데이터 스트림 소비
    * subscribeOn() 메서드를 사용해 Observable을 구독하고 방출하는 데이터 수신 가능
* **Schedulers**
    * Observable과 Observers에게 그들이 실행되어야 할 스레드를 알려줌
    * observeOn() 메서드로 observers에게 관찰해야 할 스레드를 알려줄 수 있음
    * scheduleOn() 메서드로 observable이 실행해야 할 스레드를 알려줄 수 있음

* * *

# RxJava: Subject, PublishSubject, BehaviorSubject
> 최초작성: 2022.03.13
## **1. Subject**
* 구독하고 있는 관찰자(Observer)에게 새로운 값을 전달 할 때 사용하는 클래스
* 따로 Observable로 새로운 값을 만들 필요 없이 Subject 객체에 내장된 onNext 함수로 새로운 값을 옵저버에 전달할 수 있기 때문에 짧은 코드로고 reactive하게 구현 가능
* LiveData와 비슷한 역할

### **PublishSubject를 이용해서 새로운 값 갱신 예제**
```kt
class Person {
    var publishName: PublishSubject<String> = PublishSubject.create()
}

val person = Person()
person.publishName.subscribe {
    Log.d(TAG, "publishName: " + it)
}

person.publishName.onNext("selfish")
person.publishName.onNext("developer")
```
```
[실행결과]
publishName: selfish
publishName: developer
```

## 02. PublishSubject vs BehaviorSubject
```kt
class Person {
    var behaviorName: BehaviorSubject<String> = BehaviorSubject.create() // BehaviorSubject 객체 선언
    var publishName: PublishSubject<String> = PublishSubject.create() // PublishSubject 객체 선언
    
    // subject 객체의 값을 한번에 바꾸는 함수
    fun nextName(name: String) {
        behaviorName.onNext(name)
        publishName.onNext(name)
    }
}

person.nextName("selfish")
person.publishName.subscribe  { Log.d(TAG, "publishName: " + it)  }
person.behaviorName.subscribe { Log.d(TAG, "behaviorName: " + it) }
person.nextName("developer")
```
```
[실행결과]
behaviorName: selfish
behaviorName: developer
publishName: developer
```
* BehaviorSubject로 선언된 객체는 구독 전에 갱신한 "selfish" 문자열을 출력
* PublishSubject로 선언된 객체는 구독 이후에 갱신한 "developer" 문자열만 출력

## 03. PublishSubject
* 구독 이후에 갱신된 값에 대해서만 값을 받음

![PublishSubject](../image/PublishSubject.png)
* 구독하기 이전에 갱신된 빨간공, 초록공은 무시하고 파란공만 받음
* 과거 데이터를 무시하고 새로 갱신된 값만 보고 싶은 경우 사용하기 유용
* 대표적으로 버튼 클릭 이벤트에 많이 사용

## 04. BehaviorSubject
* 구독하는 시점의 가장 최근에 갱신된 값을 받음

![BehaviorSubject](../image/BehaviorSubject.png)
* 구독하면서 가장 최근에 갱신된 초록생 공과 그 이후에 갱신된 파란색 공을 받음
* 구독하는 시점에서 과거 갱신된 데이터 중 가장 최근 값이 필요할 때 사용하기 유용