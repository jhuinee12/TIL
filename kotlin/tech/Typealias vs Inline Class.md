# Typealias VS Inline Class
> 최초작성 : 2022.04.05

## 1\. typealias
* 변수들에 대한 새로운 **별명**을 지어주고, 짧게 사용 가능
* typealias를 *클래스 혹은 함수 내에서 정의*하는 것은 **불가능**하다
* typealias는 누구나 접근할 수 있는 **Top Level 변수**이다
**동일한 type을 여러 개의 이름**으로 사용할 수 있다.

ex1-1. typealias NodeSet = Set<Network.Node>

ex1-2. typealias FileTable<K> = MutableMap<K, MutableList<File>>

ex2-1. typealias MyHandler = (Int, String, Any) > Uit

ex2-3. typealias Predicate<T> = (T) -> Boolean

* PermissionDialog 클래스를 PermissionDialogCallback으로 정의
* (PermissionDialog) -> Unit :: (인자타임) -> 반환타입

## 2\. Inline Class
* init block 적용 불가
* 추가적인 변수 선언 불가

inline class를 사용하기 위해선 gralde에 아래와 같은 소스코드 추가 필요

```gradle
compileKotlin {
    kotlinOptions {
        freeCompilerArgs = ["-Xinline-classes"]
    }
}
```

inline class 정의
```kt
inline class Name(val value: String)

val name = Name("Name") // 어떠한 값이든 들어올 수 있었던 String 대신, Name으로 명확화

name.value // inline class에서 값을 불러오는 방법
```

* typealias처럼 **Top Level로 정의**해야 함

## 3\. typealias -> inline class

* data class는 inline을 허용치 않음
* inline class 정의 시, primitive만 허용 가능 (제네릭이나 Map 형태 불가)

(1) typealias 사용
```kt
typealias Hour = Int
typealias Minute = Int
typealias Seconds = Int

data class TimeData(
    val hour: Hour,
    val minute: Minute,
    val seconds: Seconds
)

fun test(){
    val hour = 3
    val minute = 35
    val seconds = 55

    TimeData(minute, seconds, hour)
}
```
* typealias는 별칭만 달아줄 뿐, 값에 대한 보증 X
* hour, minute, seconds 모두 int이므로 혼동해서 사용해도 소스 상에서는 문제되지 않음

```kt
// 변수를 지정해주는 형태로 사용하여 실수를 줄일 것
TimeData(minute = minute, seconds = seconds, hour = hour)
```


```kt
data class TimeData(
    val hour: Hour,
    val minute: Minute,
    val seconds: Seconds
)
```

```kt
inline class Hour(val value: Int)
inline class Minute(val value: Int)
inline class Seconds(val value: Int)
``` 

```kt
fun test(){
    val hour = 3 // Hour(3)
    val minute = 35
    val seconds = 55

    TimeData(hour, minute, seconds) // type 오류 발생
    TimeData(Hour(hour), Minute(minute), Seconds(seconds))
}
```
* typealias보다 명확하게 값을 사용할 수 있음