# SharedPreference

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