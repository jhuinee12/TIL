# Kotlin 공부
+ [Kotlin 문법 정리](#코틀린-문법-정리)
    + [01. 변수 선언하기](#01-변수-선언하기)
    + [02. 조건문](#02-조건문)
        + [2-1 조건문 if](#2-1-조건문-if)
        + [2-2 조건문 when](#2-2-조건문-when)
        + [2-3 if문과 when문은 언제 사용할까?](#2-3-if문과-when문은-언제-사용할까)
    + [03. 배열과 컬렉션](#03-배열과-컬렉션)
        + [3-1 배열](#3-1-배열)
        + [3-2 컬렉션](#3-2-컬렉션)
            + [리스트(List)](#리스트list)
            + [셋(Set)](#셋set)
            + [맵(Map)](#맵map)
        + [3-3 이뮤터블 컬렉션](#3-3-이뮤터블-컬렉션)
    + [04. 반복문](#04-반복문)
        + [4-1 for 반복문](#4-1-for-반복문)
        + [4-2 while반복문](#4-2-while반복문)
        + [4-3 반복문 제어하기](#4-3-반복문-제어하기)
    + [05. 함수](#05-함수)
        + [5-1 함수의 정의](#5-1-함수의-정의)
        + [5-2 함수의 사용](#5-2-함수의-사용)
        + [5-3 함수 파라미터의 정의](#5-3-함수-파라미터의-정의)
    + [06. 클래스](#06-클래스)
        + [6-1 클래스와 설계](#6-1-클래스와-설계)
        + [6-2 클래스의 생성](#6-2-클래스의-생성)
        + [6-3 클래스의 사용](#6-3-클래스의-사용)
        + [6-4 데이터 클래스](#6-4-데이터-클래스)
        + [6-5 클래스의 상속과 확장](#6-5-클래스의-상속과-확장)
    + [07. 설계도구](#07-설계도구)
        + [7-1 설계도구](#7-1-설계도구)

---

## 코틀린 문법 정리

### 01. 변수 선언하기
> 최초작성 : 2020.12.17

**fun main(변수명: 타입)**

_<JAVA 샘플 코드>_
```java
public int solution(String s)
```

_<Kotlin 샘플 코드>_ 
```kt
fun solution(s: String)
```

**코틀린 변수 (val, var)**
```kt
val name = “hong”  ☞ 불변변수

var subject: String = “music” ☞ 가변변수

// 초기화

val grade: Int

grade = 1
```

### 02. 조건문
> 최초작성 : 2021.02.03

#### **2-1 조건문 if**

```kt
var myNumbers = "1,2,3,4,5,6"
var thisWeekNumbers = "5,6,7,8,9,10"
if (myNumbers == thisWeekNumbers)
    println("Lotto" + " 당첨되었습니다.")
else
    println("당첨 X")
```

**if문을 사용하기 위한 조건**

1. 비교 연산자 : ==,<,>
2. 논리 연산자 : &&, ||
<br> -> 조건식 결과는 Boolean 타입인 true와 false로 나타남

```kt
var a = 30
var b = 19
var bigger = a > b
println("비교 연산자 a는 b보다 큽니다.: $bigger") // $뒤에 변수명 넣으면 출력됨
// println("비교 연산자 a는 b보다 큽니다.: " + bigger)
```

```
비교 연산자 a는 b보다 큽니다.: true
```

```kt
var a = 30
var b = 19
var c = 37
bigger = a>b && a>c
println("비교 연산자 a는 b보다 크고 c보다도 큽니다: $bigger")
```

```
비교 연산자 a는 b보다 크고 c보다도 큽니다: false
```

**기본적인 if문 사용하기**

```
if (조건식) {

   조건식이 참일 경우 실행되는 코드 영역

}
```

**if~else문 사용하기**
```
if (조건식) {

   조건식이 참일 경우 실행되는 코드 영역

} else {

   조건식이 거짓일 경우 실행되는 코드 영역

}
```

```kt
var ball = 4
if (ball > 3)
    println("4볼로 출루합니다")
else println("타석에서 다음 타구를 기다립니다")
```

```
4볼로 출루합니다
```

#### **2-2 조건문 when**

**다른 언어에서는 if와 switch를 비교**
```
switch (변수) {

   case 비교값 :

      // 변숫값이 비굣값과 같다면 이 영역이 실행됩니다.

}
```

**코틀린에서는 if와 when을 비교**
```
when (파라미터) {

   비교값 -> {

      // 변숫값이 비교값과 같다면 이 영역이 실행됩니다.

   }

}
```

```kt
var now = 10
when (now) {
    8 -> {
        println("현재 시각은 8시입니다.")
    }
    9 -> {
        println("현재 시간은 9시 입니다.")
    }
    else -> {
        println("현재 시간은 8시도 9시도 아닙니다.")
    }
}
```

```
현재 시간은 8시도 9시도 아닙니다.
```

**콤마로 구분해서 사용하기**

```kt
now = 9
when(now) {
    8,9 -> {
        println("현재 시간은 8시 또는 9시입니다.")
    }
    else -> {
        println("현재 시간은 8시 또는 9시가 아닙니다.")
    }
}
```

```
현재 시간은 8시 또는 9시입니다.
```

**파라미터가 없는 when 사용하기**

```kt
var currentTime = 6
when {
    currentTime == 5 -> {
        println("현재 시간은 5시입니다.")
    }
    currentTime > 5 -> {
        println("현재 시간은 5시가 넘었습니다.")
    }
    else -> {
        println("현재 시간은 5시 이전입니다.")
    }
}
```

```
현재 시간은 5시가 넘었습니다.
```

#### **2-3 if문과 when문은 언제 사용할까?**

-   범위가 넓고 값을 특정할 수 없는 경우 -> _if문 사용_
-   범위가 제한되고 값도 특정할 수 있는 경우 (ex.요일) -> _when문 사용_

---

### 03. 배열과 컬렉션
> 최초작성 : 2021.02.06

#### **3-1 배열**

-   여러 개의 값을 담을 수 있는 대표적인 자료형
-   값을 담기 전에 먼저 배열 공간의 개수를 할당하거나 초기화 시에 데이터를 저장해두면 데이터의 개수만큼 배열의 크기가 결정됨
-   먼저 개수를 정해놓고 사용해야 하며 중간에 개수를 추가하거나 제거할 수 없음

**선언형태**

**var 변수 = Array(개수)**

```kt
var students = IntArray(10)
var longArray = LongArray(10)
var CharArray = CharArray(10)
var FloatArray = FloatArray(10)
var DoubleArray = DoubleArray(10)
```

**문자 배열에 빈 공간 할당하기**

String은 기본 타입이 아니기 때문에 StringArray는 없지만 아래와 같이 사용 가능

```kt
var stringArray = Array(10, {item->""})
```

**값으로 배열 공간 할당하기**

```kt
var dayArray = arrayOf("MON","TUE","WED","THU","FRI","SAT","SUN")
println(dayArray[0]+"\n")
```

```
*** 출력결과 ***
MON
````

**배열에 값 입력하기**

배열명\[인덱스\] = 값  
배열명.set(인덱스, 값)

```kt
students[0] = 90	// 배열명[인덱스] = 값
students.set(1,91)	// 배열명.set(인덱스,값)
students.set(2,92)
students.set(3,93)
students.set(4,94)
students.set(5,95)
students.set(6,96)
students.set(7,97)
students.set(8,98)
students[9] = 99

println("students[0] : " + students[0])
println("students[1] : " + students[1])
println("students[2] : " + students[2])
println("students[3] : " + students[3])
println("students[4] : " + students[4])
println("students[5] : " + students[5])
println("students[6] : " + students[6])
println("students[7] : " + students[7])
println("students[8] : " + students[8])
println("students[9] : " + students[9]+"\n")
```

```kt
var dayArray = arrayOf("MON","TUE","WED","THU","FRI","SAT","SUN")
println(dayArray[0]+"\n")
```

```
*** 출력결과 ***

students[0] : 90
students[1] : 91
students[2] : 92
students[3] : 93
students[4] : 94
students[5] : 95
students[6] : 96
students[7] : 97
students[8] : 98
students[9] : 99
```

**배열에 있는 값 꺼내기**

배열명\[인덱스\]

배열명.get(인덱스)

```kt
var seventhValue = students[6]
var tenthValue = students.get(9)

println("seventhValue : " + seventhValue)
println("tenthValue : " + tenthValue)
```

```
*** 출력결과 ***

seventhValue : 96
tenthValue : 99
```

#### **3-2 컬렉션**

-   다른 이름으로 동적 배열이라 부름
-   배열과는 다르게 공간의 크기를 처음 크기로 고정하지 않고 임의의 개수를 담음
-   리스트(List), 맵(Map), 셋(Set)

##### **리스트(List)**

-   저장되는 데이터에 인덱스를 부여한 컬렉션
-   중복된 값 입력 가능
-   코틀린에서는 동적으로 리스트를 사용하기 위해서는 자료형 앞에 뮤터블(Mutable)이라는 접두어 필요
-   mutableList, mutableMap, mutableSet

**리스트 생성하기 : mutableListOf**

```kt
var list = mutableListOf("MON","TUE","WED")
```

**리스트 값 추가하기 : add**

```kt
list.add("THU")
```

**리스트에 입력된 값 사용하기 : get**

```kt
var variable = list.get(1)
```

**리스트값 수정하기 : set**

```kt
list.set(1, "0")  // mutableList.set(인덱스, 수정할값)
```

**리스트에 입력된 값 제거하기 : removeAt**

```kt
list.removeAt(0)
```

**빈 리스트 사용하기**

**var 변수명 = mutableListOf<컬렉션에 입력될 값의 타입>()**

```kt
var emptyList = mutableListOf<Int>()
var stringList = mutableListOf<String>()
```

```kt
// 생성
var stringList = mutableListOf<String() // 문자열로 된 빈 컬렉션을 생성
// 입력
stringList.add("월")
stringList.add("화")
// 사용
println("stringList에 입력된 두 번째  " + stringList.get(1) + "입니다.")
// 수정
stringLlist.set(1,"수정된 값")
// 삭제
stringList.removeAt(1) // 두번째 값이 삭제
```

```
*** 출력결과 ***

stringList에 입력된 두 번째  화입니다.
```

**컬렉션 개수 가져오기 : size**

mutableList.size

##### **셋(Set)**

-   중복을 허용하지 않는 리스트
-   인덱스로 조회할 수 없고, get 함수도 지원하지 않음

```kt
var set = mutableSetOf<String>();
```

**빈 셋으로 초기화하고 값 입력하기**

```kt
set.add("JAN")
set.add("FEB")
set.add("MAR")
set.add("JAN")  // 동일한 값은 입력되지 않습니다.
```

**셋 삭제하기**

```kt
set.remove("FEB")
```

##### **맵(Map)**

-   키(Key)와 값(Value)이 쌍으로 입력되는 컬렉션
-   맵의 키는 리스트의 인덱스와 비슷한데 리스트와는 다르게 키를 직접 입력해야 함

```kt
var map = mutableMapOf<String, String>()
```

**빈 맵으로 생성하고 값 추가하기**

```kt
map.put("키1", "값1")
map.put("키2", "값2")
map.put("키2", "값3")
```

**맵 수정하기**

```kt
map.put("키2","수정")
```

**맵 삭제하기**

```kt
map.remove("키2")
```

#### **3-3 이뮤터블 컬렉션**

-   크기를 변경할 수 없으며 값 또한 변경할 수 없음
-   기준이 되는 어떤 값의 모음을 하나의 변수에 저장할 필요가 있거나 또는 여러 개의 값을 중간에 수정하지 않고 사용할 필요가 있을 때 사용

```kt
val IMMUTABLE_LIST = listOf("JAN","FEB","MAR")  // 생성
println("리스트의 두 번째 값은 " + IMMUTABLE_LIST.get(1) + "입니다.")   // 사용
```

```
*** 출력결과 ***

리스트의 두 번째 값은 FEB입니다.
```

---

### 04. 반복문
> 최초작성 : 2021.02.08

#### **4-1 for 반복문**

-   **for : 특정 횟수만큼 코드를 반복하기 위해 사용**

**⊙ for 다음의 괄호 () 안에 반복할 범위를 지정하면 for 블로의 코드들이 지정한 횟수만큼 반복해서 동작**

```kt
for (반복할 범위) {
  // 실행 코드
}
```

**⊙ for in ..(온점 2개) : 일반적인 형태의 for 반복문**

**시작값과 종료값으로 지정한 숫자 범위만큼 코드를 반복하는 일반적인 방법**

```kt
for (변수 in 시작값..종료값) {
  // 실행 코드
}
```

```kt
for (index in 1..10) {
    println("현재 숫자는 " + index)
}
```

```
*** 출력 결과 ***

현재 숫자는 1
현재 숫자는 2
현재 숫자는 3
현재 숫자는 4
현재 숫자는 5
현재 숫자는 6
현재 숫자는 7
현재 숫자는 8
현재 숫자는 9
현재 숫자는 10
```

**⊙ until : 마지막 숫자 제외하기**

**시작값과 종료값 사이의 ..(온점 2개) 대신에 until을 사용하면 종료값 이전까지만 반복**

```kt
for (변수 in 시작값 until 종료값) {
	// 실행 코드
}
```

```kt
var array = arrayOf("JAN","FEB","MAR","APR","MAY","JUN")
for (index in 0 until array.size) {
    println("현재 월은 ${array.get(index)}입니다.")
}
```

```
*** 출력 결과 ***

현재 월은 JAN입니다.
현재 월은 FEB입니다.
현재 월은 MAR입니다.
현재 월은 APR입니다.
현재 월은 MAY입니다.
현재 월은 JUN입니다.
```

**⊙ step : 건너뛰기**

**for문의 블록을 step 수만큼 건너뛰어서 실행**

```kt
for (변수 in 시작값..종료값 step 3) {
	// 실행 코드
}
```

```kt
for (index in 0..10 step 3) {
    println("현재 숫자는 ${index}")
}
```

```
*** 출력 결과 ***

현재 숫자는 0
현재 숫자는 3
현재 숫자는 6
현재 숫자는 9
```

**⊙ downTo : 감소시키기**

**큰 수에서 작은수로 감소하면서 실행, step 사용 가능**

```kt
for (downToIndex in 0 downTo 10) {
    println("현재 숫자는 ${downToIndex}")
}
```

**⊙ 배열, 컬렉션에 들어 있는 엘리먼트 반복하기**

**배열이나 컬렉션을 엘리먼트의 개수만큼 반복하면서 사용**

```kt
var arrayMonth = arrayOf("JAN","FEB","MAR","APR","MAY","JUN")
for (month in arrayMonth) {
    println("현재 월은 ${month}입니다.")
}
```

```
*** 출력 결과 ***

현재 월은 JAN입니다.
현재 월은 FEB입니다.
현재 월은 MAR입니다.
현재 월은 APR입니다.
현재 월은 MAY입니다.
현재 월은 JUN입니다.
```

#### **4-2 while반복문**

-   **while : 특정 조건이 만족할 때까지 코드를 반복할 때 사용**
-   **괄호 안의 조건식에는 주로 두 항과 비교 연산자가 사용됨**
-   **괄호 안의 조건식 결과가 항상 true이면 무한루프에 빠짐**

***무한루프 while문 예제***

```kt
var a = 1
while (a == 1) {
	println("조건을 만족하면 여기를 출력하세요!")
}
```

**⊙ 일반적인 while 반복문**

**for문과는 다르게 증감되는 인덱스가 있으면 코드에서 직접 처리해야 함**

```kt
var current = 1
val until = 12
while (current < until) {
    println("현재 값은 ${current}입니다")
    current = current+1
}
```

```
*** 출력 결과 ***

현재 값은 1입니다
현재 값은 2입니다
현재 값은 3입니다
현재 값은 4입니다
현재 값은 5입니다
현재 값은 6입니다
현재 값은 7입니다
현재 값은 8입니다
현재 값은 9입니다
현재 값은 10입니다
현재 값은 11입니다
```

**⊙ do와 함께 사용하기**

**do와 함께 사용하면 while문의 조건식과 관계없이 do 블록 안의 코드를 한 번 실행함**

```kt
var game = 1
val match = 6
do {
    println("${game}게임 이겼습니다. 우승까지 ${match-game}게임 남앗습니다.")
    game += 1
} while (game < match)
```

```
*** 출력 결과 ***

1게임 이겼습니다. 우승까지 5게임 남앗습니다.
2게임 이겼습니다. 우승까지 4게임 남앗습니다.
3게임 이겼습니다. 우승까지 3게임 남앗습니다.
4게임 이겼습니다. 우승까지 2게임 남앗습니다.
5게임 이겼습니다. 우승까지 1게임 남앗습니다.
```

**⊙ while과 do~while의 차이점**

**가장 큰 차이점은 최초값이 조건식을 만족하지 않았을 경우 실행 코드가 달라지는 것**

```kt
// 앞의 코드에 이어서
game = 6
while (game < match) {
    println("while 테스트입니다.")
    game + 1
}
// do ~ while 테스트
game = 6
do {
    println("do ~ while 테스트입니다.")
    game +=1
} while (game < match)
```

```
*** 출력 결과 ***

do ~ while 테스트입니다.
```

#### **4-3 반복문 제어하기**

**⊙ break : 반복문 탈출하기**

**반복문 안에서 break를 만나면 반복문을 탈출, 특정 조건에서 무조건 for 반복문을 벗어날 때 사용**

```kt
for (index in 1..10) {
    println("현재 index는 ${index}입니다.")
    if (index > 5)
        break
}
```

```
*** 출력 결과 ***

현재 index는 1입니다.
현재 index는 2입니다.
현재 index는 3입니다.
현재 index는 4입니다.
현재 index는 5입니다.
현재 index는 6입니다.
```

**⊙ continue : 다음 반복문으로**

**반복문 안에서 continue를 만나면 다음 코드는 실행하지 않고 반복문의 처음으로 돌아감**

```kt
for (except in 1..10) {
    if (except > 3 && except < 8)
        continue
    println("현재 index는 ${except}입니다.")
}
```

```
*** 출력 결과 ***

현재 index는 1입니다.
현재 index는 2입니다.
현재 index는 3입니다.
현재 index는 8입니다.
현재 index는 9입니다.
현재 index는 10입니다.
```

--- 

### 05. 함수
> 최초작성 : 2021.03.01

#### **5-1 함수의 정의**

-   **함수는 fun 키워드로 정의, 값을 입력받아 사용**
-   **입력될 값을 기술한 것을 파라미터(Parameter)라 부름**

```kt
fun 함수명(파라미터 이름 : 타입): 반환타입 {
	return 값
}
```

**⊙ 반환값과 입력값이 있는 함수의 정의**

```kt
fun square(x: Int): Int {
    return x * x
}
```

**⊙ 반환값과 입력값이 있는 함수의 정의**

```kt
fun printSum(x:Int, y:Int) {
    println("x+y=${x+y}")
}
```

**⊙ 입력값 없이 반환값만 있는 함수의 정의**

```kt
fun getPi(): Double {
    return 3.14
}
```

#### **5-2 함수의 사용**

-   **함수의 사용은 이름 뒤에 괄호를 붙여 명령어를 실행하는 형태 => 함수명(값)**

```kt
fun 함수명(파라미터 이름 : 타입): 반환타입 {
	return 값
}
```

**⊙ 반환값과 입력값이 있는 함수의 호출**

```kt
var squareResult = square(30)
println("30의 제곱은 ${squareResult}입니다.")
```

```
*** 출력 결과 ***

30의 제곱은 900입니다.
```

**⊙ 반환값이 없는 함수의 호출**

```
printSum(3,5)
```

```
*** 출력 결과 ***

x+y=8
```

**⊙ 입력값이 없는 함수의 호출**

```
var PI = getPi()
println("지름이 10인 원의 둘레는 ${10*PI}입니다.")
```

```
*** 출력 결과 ***

x+y=8
```

#### **5-3 함수 파라미터의 정의**

-   **함수에 입력되는 파라미터는 '이름:타입'의 형태로 정의**
-   **여러 개의 파라미터가 정의될 경우 콤마로 구분**
-   **함수 파라미터를 통해 입력되는 모든 값은 변하지 않는 이뮤터블 : val**

```
fun 함수명((val 생략) name: String, name2: Int, name3: Double) {실행코드}
```

**⊙ 파라미터의 기본값 정의와 호출**

```
fun newFunction(name: String, age: Int = 29, weight: Double = 65.5) {
    println("name의 값은 ${name}입니다.")
    println("age 값은 ${age}입니다.")
    println("weight 값은 ${weight}입니다.")
}

newFunction("Hello")
```

```
*** 출력 결과 ***

name의 값은 Hello입니다.
age 값은 29입니다.
weight 값은 65.5입니다.
```

**⊙ 파라미터 이름으로 값을 입력하기**

```
newFunction("Michae", weight=67.5)
```

```
*** 출력 결과 ***

name의 값은 Michae입니다.
age 값은 29입니다.
weight 값은 67.5입니다.
```

---

### 06. 클래스
> 최초작성 : 2021.03.03

#### **6-1 클래스와 설계**

-   **그룹화할 수 있는 함수와 변수를 한군데 모아놓고 사용하기 쉽게 이름을 붙여놓은 것**

```kt
class 클래스명 {
	var 변수
    fun 함수() {
    	// 코드
    }
}
```

```kt
class Log {
    var status: Intcompanion objext {
        static fun d(tag: String, msg: String) {
            // 문자열을 출력하는 코드
        }
        static fun e(tag: String, msg: String) {
            // 문자열을 출력하는 코드
        }
    }
}
```

#### **6-2 클래스의 생성**

-   **함수 형태로 제공되는 생성자를 호출해야지만 클래스 실행**

```kt
class Kotlin {
	init {
    	// 생성자가 없으면 아무것도 없는 init 블록이 실행되는 것과 같음
    }
}
```

**⊙ 프라이머리 생성자**

```kt
class KotlinOne (생략가능)constructor(value: String) {
    // 코드
}
```

**⊙ 세컨더리 생성자**

```kt
class KotlinTwo {
    constructor (value: String) {
        println("생성자로부터 전달받은 값은 ${value}입니다.")
    }
}
```

#### **6-3 클래스의 사용**

-   **클래스 이름에 괄호를 붙여서 클래스의 생성자 호출 : 클래스명() -> constructor 키워드를 직접 호출하지 않음**
-   **Kotlin클래스의 생성자를 호출한 후 생성되는 것을 인스턴스라고 하는데, 생성된 인스턴스는 변수에 담아둘 수 있음**
-   **: var kotlin = Kotlin()**
-   **생성자에 파라미터가 있으면 값을 입력해야함 :  var one = KotlinOne("value")**

```kt
class KotlinTwo {
    constructor (value: String) {
        println("생성자로부터 전달받은 값은 ${value}입니다.")
    }
}

var one = KotlinTwo("value")
```

```
*** 출력 결과 ***

생성자로부터 전달받은 값은 value입니다.
```

**⊙ 클래스 안에 정의된 함수와 변수 사용하기**

```kt
class KotlinThree {
    var one: String = "None"
    fun printOne() {
        println("one에 입력된 값은 ${one}입니다")
    }
}

var kotlinThree = KotlinThree();

kotlinThree.one = "new value"
kotlinThree.printOne()
```

```
*** 출력 결과 ***

one에 입력된 값은 new value입니다
```

**⊙ 클래스를 인스턴스화 하지 않고 사용하기 : companion object**

```kt
class KotlinFour {
    companion object {
        var one: String = "None"
        fun printOne() {
            println("one에 입력된 값은 ${one}입니다")
        }
    }
}

KotlinFour.one = "새로운 값"
KotlinFour.printOne()
```

```
*** 출력 결과 ***

one에 입력된 값은 새로운 값입니다
```

#### **6-4 데이터 클래스**

```kt
data class 클래스명 (val 파라미터1: 타입, var 파라미터2: 타입)
```

**⊙ 데이터 클래스의 정의와 생성**

```kt
data class DataUser (var name: String, var age: Int)
var dataUser = DataUser("Michael", 21)
println("DataUser는 ${dataUser.toString()}")
```

```
*** 출력 결과 ***

DataUser는 DataUser(name=Michael, age=21)
```

**⊙ copy() 메서드 지원**

```
var newData = dataUser.copy()
```

#### **6-5 클래스의 상속과 확장**

-   **코틀린은 클래스의 재사용을 위해 상속을 지원**

**⊙ 클래스의 상속**

-   **상속 대상이 되는 부모 클래스는 open 키워드로 만들어야만 자식 클래스에서 사용 가능**
-   **만약 open 키워드로 열려있지 않은 경우 상속 불가능**
-   **상속을 받을 자식 클래스에서는 콜론을 이용해서 상속할 부모 클래스 지정**

```kt
open class 상속될 부모 클래스 { // 코드 }
class 자식 클래스 (value: String) : 부모 클래스(value) { // 코드 }
```

**⊙ 생성자 파라미터가 있는 클래스의 상속**

```kt
open class 상속될 부모 클래스 (value: String) { // 코드 }
class 자식 클래스 (value: String) : 부모 클래스(value) { // 코드 }
```

**⊙ 부모 클래스의 프로퍼티와 메서드 사용하기**

```kt
open class Parent {
    var hello: String = "안녕하세요"
    fun sayHello() {
        println(hello)
    }
}
class Child: Parent() {
    fun myHello() {
        hello = "Hello"
        sayHello()
    }
}
```

**⊙ 프로퍼티와 메서드의 재정의 : 오버라이드**

-   **상받은 부모 클래스의 프로퍼티와 메서드 중에 자식 클래스에서는 다른 용도로 사용해야 하는 경우 Override 필요**
-   **동일한 이름의 메서드나 프로퍼티를 사용할 필요가 있을 경우 override 키워드를 사용해 재정의**

****⊙ 메서드 오버라이드****

```kt
open class BaseClass {
    open fun opened() {
    }
    fun notOpend() {
    }
}
class ChildClass: BaseClass() {
    override fun opened() {
    }
    override fun notOpend() { // notOpend 메서드는 open 키워드가 없으므로 잘못된 사용
    }
}
```

********⊙ 프로퍼티 오버라이드********

```kt
open class BaseClass2 {
    open var opened: String = "I am"
}
class ChildClass2 : BaseClass2() {
    override var opened: String = "You are"
}
```

**********⊙ 익스텐션**********

```kt
fun 클래스.확장할 함수() {
	// 코드
}
```

---

### 07. 설계도구
> 최초작성 : 2021.03.04

#### **7-1 설계도구**

-   **객체지향 프로그래밍은 구현과 설계로 구분함**

**⊙ 패키지**

-   **클래스와 소스 파일을 관리하기 위한 디렉터리 구조의 저장 공간**
-   **디렉터리가 계층 구조로 만들어져 있으면 온점(.)으로 구분해서 각 디렉터리를 모두 나열해줌**

```kt
package 메인 디렉터리.서브 디렉터리
class 클래스 {
}
```

**⊙ 추상화**

-   **메서드 이름으로 나열했을 때, 명확한 코드는 설계 단계에서 메서드 블록 안에 직접 코드를 작성**
-   **명확하지 않은 경우는 구현 단계에서 코드를 작성하도록 메서드의 이름만 작성 => 추상화(abstract)**
-   **Activity가 상속받는 클래스 중 최상위 클래스 : Context => abstract로 설계되어 있음**

```kt
abstract class Animal {
	fun walk() {
    	println("걷습니다.")
    }
	abstract fun move()
}
```

```kt
class Bird : Animal() {
	override fun move() {
    	println("날아서 이동합니다.")
    }
}
```

**⊙ 인터페이스**

-   **실행 코드 없이 메서드 이름만 가진 추상 클래스**
-   **실행 코드가 한 줄이라도 있으면 추상화, 코드 없이 메서드 이름만 나열되어 있으면 인터페이스**
-   **인터페이스는 interface 예약어를 사용해 정의 가능, 인터페이스에 정의된 메서드를 오버라이드해서 구현 가능**
-   **추상 클래스와 다르게 class 키워드는 사용되지 않음**

```kt
interface 인터페이스명 {
	var 변수: String
    fun 함수1()
    fun 함수2()
}
```

**⊙ 인터페이스 만들기**

```kt
interface InterfaceKotlin {
	(abstract 생략) var variable: String
		fun get()
		fun set()
}
```

**⊙ 클래스에서 구현하기**

```kt
class KotlinImpl : InterfaceKotlin {
	override var variable: String = "init value"
    override fun get() { //코드 구현 }
    override fun set() { //코드 구현 }
}
```

```kt
var kotlinImpl = object : InterfaceKotlin {
	override var variable: String = "init"
    override fun get() { //코드 }
    override fun set() { //코드 }
}
```

**⊙ 접근 제한자**

-   **private : 다른 파일에서 접근할 수 없음**
-   **internal : 같은 모듈에 있는 파일만 접근 가능**
-   **protected : private과 같으나 상속 관계에서 자식 클래스가 접근할 수 있음**
-   **public : 제한 없이 모든 파일에서 접근 가능**

**⊙ 제네릭**

-   **입력되는 값의 타입을 자유롭게 사용하기 위한 설계 도구**

```kt
var list: MutableList<제네릭> = mutableListOf("월","화","수")
```