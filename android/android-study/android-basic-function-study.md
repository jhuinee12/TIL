# 안드로이드 기본 기능 내용 정리

- - -

## string.xml
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

----


## SharedPreference

> 최초작성 : ???

### ***SharedPreference란?***
- 저장하려는 키-값 컬렉션이 비교적 작은 경우 SharedPreference API를 사용
- 키-값 쌍이 포함된 파일을 가리키며 키-값 쌍을 읽고 쓸 수 있는 간단한 메서드를 제공
- 해당 파일은 프레임워크에서 관리하며 비공개이거나 공유일 수 있음

### 1. 공유 환경설정의 핸들 가져오기
다음 메서드 중 하나를 호출하여 새로운 공유 환경설정 파일을 생성하거나 기존 파일을 엑세스 할 수 있음

- getSharedPreference() : 첫번째 매개변수로 지정하는 이름으로 식별되는 여러 공유 환경설정 파일이 필요한 경우 이 메서드를 사용. 앱의 모든 Context에서 이 메서드를 호출할 수 있음
- getPreference() : 활동에 하나의 공유 환경설정 파일만 사용해야 하는 경우 Activity에서 이 메서드를 사용. 이 메서드는 활동에 속한 기본 공유 환경설정 파일을 검색하기 때문에 이름을 제공할 필요가 없음.

```kotlin
val sharedPref = activity?.getSharedPreferences(
    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
```
```kotlin
val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
```

### 2. 공유 환경설정에 쓰기
공유 환경설정 파일에 쓰려면 SharedPreference에서 edit()을 호출하여 SharedPreference.Editor를 만듦

- putInt() 및 putstring()과 같은 메서드를 사용하여 쓰려고 하는 키와 값을 전달
- appy() 또는 commit()을 호출하여 변경사항을 저장

```kotlin
val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
with (sharedPref.edit()) {
	putInt(getString(R.string.saved_high_score_key), newHighScore)
	commit()
}
```

### 3. 공유 환경설정에서 읽기
공유 환경설정 파일에서 값을 검색하려면 getInt() 및 getString()과 같은 메서드를 호출하여 원하는 값에 키를 제공하고 키가 없으면 선택적으로 반환할 기본값을 제공

```kotlin
val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
val highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue)
```

### ***소스 예제***
#### **(1) Activity 생성 전, SharedPreference를 만들어주는 클래스 App을 생성하고 Manifest에 추가**
```kotlin
/**
  * App.kt
  */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        // SharedPreference
        SharedPrefUtil.init(applicationContext)
    }
}
```
```xml
<!--AndroidManifest.xml-->

<application
        android:name=".App"
        ...
        
    </activity>
</application>
```

#### **(2) SharePreference Key를 변수로 만들어주고 변수명을 이용해 사용할 예정이므로 해당 변수를 저장해주는 클래스 생성**
```kotlin
/**
  * SharedPrefKey.kt
  * class 대신 object를 사용하면 static처럼 사용할 수 있음
  */
object SharedPrefKey {
    var KEY1: String = "key1"
    var KEY2: String = "key2"
    var KEY3: String = "key3"
}
```

#### **(3) put과 get을 편하게 사용하기 위해 해당 메서드를 정리해놓은 Util 클래스 생성**

```kotlin
/**
  * SharedPrefUtil.kt
  */
object SharedPrefUtil {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun put(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun put(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
    fun getInt(key: String, defValue: Int): String {
        return prefs.getInt(key, defValue).toString()
    }

    fun put(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
}
```

#### **(4) SharedPreference 입출력**
```kotlin
SharedPrefUtil.getString(SharedKey.KEY1, "defaultValue")  // 내용읽기
SharedPrefUtil.put(SharedKey.KEY1, "value") // 내용쓰기
```

#### **(5) 내장 저장소에서 SharedPreference 파일 읽기**
- view > Tool Window > Device File Explorer
- data/data/패키지명/sharedPref

----
