# string.xml
> 최초작성 : ???

### **1. 문자열 리소스란?**
문자열 리소스는 옵션 사항인 텍스트 스타일 지정 및 서식 지정 기능과 함께애플리케이션에 사용할 수 있는 텍스트 문자열을 제공
1. **문자열 : 단일 문자열을 제공하는 XML 리소스**
1. **문자열 배열 : 문자열로 구성된 배열을 제공하는 XML 리소스**
1. **수량 문자열(복수형) : 복수형 표시를 위해 여러 문자열을 포함하는 XML 리소스**

#### (1) 문자열
- 파일 위치 : res/values/filename.xml (<string> 요소의 name이 리소스 ID로 사용됨)
- 리소스 참조 : {Java}R.string.string_name, {XML}@string/string_name
- 구문
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string
        name="string_name"
        >text_string</string>
</resources>
```
* 예
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="hello">Hello!</string>
</resources>
```
```xml
<TextView
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:text="@string/hello" />
```
- Kotlin
```kotlin
val string: String = getString(R.string.hello)
```
- Java
```java
String string = getString(R.string.hello);
```

<br>

#### (2) 문자열 배열
- 파일 위치 : res/values/filename.xml
- 리소스 참조 : {Java}R.array.string_array_name
- 구문
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array
        name="string_array_name">
        <item
            >text_string</item>
    </string-array>
</resources>
```
* 예
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="planets_array">
        <item>Mercury</item>
        <item>Venus</item>
        <item>Earth</item>
        <item>Mars</item>
    </string-array>
</resources>
```
- Kotlin
```kotlin
val array: Array = resources.getStringArray(R.array.planets_array)
```
- Java
```Java
Resources res = getResources();
String[] planets = res.getStringArray(R.array.planets_array);
```

#### (3) 수량 문자열(복수형)
- 무조건 복수형에서만 사용해야함.
- zero, one, two, few, many, other 집합 지원

    1. zero : 언어가 숫자 0에 대한 특수한 처리를 요구하는 경우
    1. one : 언어가 1과 같은 숫자에 대한 특수한 처리를 요구하는 경우
    1. two : 언어가 2와 같은 숫자에 대한 특수한 처리를 요구하는 경우
    1. few : 언어가 '작은' 숫자에 대한 특수한 처리를 요구하는 경우
    1. many : 언어가 '큰' 숫자에 대한 특수한 처리를 요구하는 경우
    1. other : 언어가 지정된 수량에 대한 특수한 처리를 요구하는 경우

- 파일 위치 : res/values/filename.xml
- 리소스 참조 : {Java}R.plurals.plural_name
- 구문
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <plurals
        name="plural_name">
        <item
            quantity=["zero" | "one" | "two" | "few" | "many" | "other"]
            >text_string</item>
    </plurals>
</resources>
```
- 예
```xml
`<?xml version="1.0" encoding="utf-8"?>
<resources>
    <plurals name="numberOfSongsAvailable">
        <!--
             As a developer, you should always supply "one" and "other"
             strings. Your translators will know which strings are actually
             needed for their language. Always include %d in "one" because
             translators will need to use %d for languages where "one"
             doesn't mean 1 (as explained above).
          -->
        <item quantity="one">%d song found.</item>
        <item quantity="other">%d songs found.</item>
    </plurals>
</resources>
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <plurals name="numberOfSongsAvailable">
        <item quantity="one">Znaleziono %d piosenkę.</item>
        <item quantity="few">Znaleziono %d piosenki.</item>
        <item quantity="other">Znaleziono %d piosenek.</item>
    </plurals>
</resources>
```
- kotlin
```kotlin
val count = getNumberOfSongsAvailable()
val songsFound = resources.getQuantityString(R.plurals.numberOfSongsAvailable, count, count)
```
- Java
```java
int count = getNumberOfSongsAvailable();
Resources res = getResources();
String songsFound = res.getQuantityString(R.plurals.numberOfSongsAvailable, count, count);
```