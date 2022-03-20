* [MVVM패턴이란?](#mvvm패턴이란) - 2022.03.13
* [DataBinding 구현 방법](#databinding-구현-방법) - 2022.03.13
* [ViewModel + LiveData + DataBinding이란?](#viewmodel--livedata--databinding이란) - 2022.03.13
* [BaseActivity란?](#baseactivity란) - 2022.03.13
* [AppCompatActivity란?](#appcompatactivity란) - 2022.03.13

* * *

## MVVM패턴이란?
> 최초작성: 2022.03.13
View - ViewModel - Model

![MVVM도식화](./image/MVVM%20%EB%8F%84%EC%8B%9D%ED%99%94.png)

* View: 사용자에게 보이는 화면
* ViewModel: View를 표현하기 위해 만든 View를 위한 Model && View를 나타내주기 위한 데이터 처리 담당
* Model: 어플에서 사용되는 데이터 및 데이터 조작 부분 (ex. 서버에서 들어오는 데이터)

### 동작순서
1. 사용자의 Action이 View를 통해 들어옴
2. Command 패턴을 이용해 ViewModel에 Action을 전달
3. ViewModel이 Model에서 데이터를 요청하고, Model은 ViewModel에서 요청받은 데이터를 전달
4. ViewModel은 응답받은 데이터를 가공 및 저장
5. View는 ViewModel과의 Data Binding을 이용해 화면 갱신

### 특징
* View와 Model 사이 의존성이 없음
* View와 ViewModel 사이 의존성이 없음
* 각 부분들은 독립적이기 때문에 개별적 개발 가능
* 개발이 어려움

* * *

## DataBinding 구현 방법
> 최초작성: 2022.03.13
#### **(1) 기본 세팅**
build.gradle (java는 아래 코드만 추가)
```gradle
android {
...
dataBinding {
        enabled = true
    }
...
}
```
build.gradle (kotlin의 경우, 아래 코드도 추가)
```gradle
apply plugin: 'kotlin-kapt'
...
dependencies {
	...
    // 버전은 본인 gradle 버전으로 작성
    kapt 'com.android.databinding:compiler:3.5.3'
	...
}
```
#### **(2) xml 수정**
activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    <data>
    ...
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <Button
            android:id="@+id/btnSample"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="button"
         />

    </LinearLayout>
</layout>
```
* 기존 xml 태그 최상위에 <layout>이라는 태그로 전체를 감싸줌
* <data> 태그 안에는 xml에서 사용할 변수들을 <variable> 태그를 이용해 작성해줌
#### **(3) kotlin 파일 수정**
MainActivity.kt
```kt
private lateinit var binding: ActivityMainBinding
...

	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
     	...
        
    }
```
* 기존에 존재했던 setContentView가 다른 걸로 대체
    * xml의 이름 기준으로 □□□Binding이라는 클래스가 자동으로 만들어짐
* xml의 변수를 참조하고 싶을 경우에는 'binding.뷰이름'으로 불러옴
#### **(4) 버튼 클릭 이벤트**
MainActivity.kt
```kt
 fun btnClick(view : View){
        // 이 안에 원하는 실행 메세지 작성
}
```
activity_main.xml
```xml
...
<data>
    <variable
        name="activity"
        type="org.techtown.databinding.MainActivity" />
</data>
...
```
* <data> 태그 안에 <variable>로 activity라는 이름의 변수를 설정함 (아름은 자유)
* type은 해당 경로
```xml
...
<Button
    android:id="@+id/btnSample"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="button"
    android:onClick="@{activity::btnClick}"/>
...
```
* 기존에 존재했던 btnSample이라는 이름의 버튼에 다음과 같이 onClick 속성을 추가
    * @{변수 이름 :: 함수 이름}
        *  함수 이름 같은 경우는 실제 액티비티에 존재하는 함수 이름과 일치해야 하며, 반드시 :: 을 써야함
#### **(5) 액티비티에서 해당 변수(variable)에 값을 넣어줌**
MainAcitivity.kt
```kt
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
	...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.activity = this@MainActivity
    }
    ...
```
### 2) RecyclerView에서 각각의 아이템을 데이터 바인딩을 이용해 사용
#### **(1) data class 생성 (이름과 나이 저장)**
ProfileData.kt
```kt
data class ProfileData(
    var name : String,
    var age : Int
)
```
#### **(2) xml 수정**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="activity"
            type="org.techtown.databinding.MainActivity" />

    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <Button
            android:id="@+id/btnSample"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="button"
            android:onClick="@{activity::btnClick}"/>

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/mainRcv"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </LinearLayout>
</layout>
```
#### **(3) RecyclerView Layout 생성**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="user"
            type="org.techtown.databinding.ProfileData" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:gravity="center_vertical">

        <TextView
            android:id="@+id/item_name"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            tools:text="Title"
            android:textSize="30sp"
            android:layout_weight="1"
            android:gravity="center_vertical"
            android:paddingLeft="10dp"
            android:text="@{user.name}"/>

        <TextView
            android:id="@+id/item_age"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            tools:text="Age"
            android:textSize="20sp"
            android:gravity="center_vertical"
            android:padding="10dp"
            android:text="@{Integer.toString(user.age)}"/>

    </LinearLayout>
</layout>
```
*  user라는 이름의 변수로 <data> 태그 안에 만들어 주고, 값들이 들어갈 텍스트뷰에 맞춰 ProfileData에 존재하는 변수를 넣어줌
#### **(4) ViewHolder 수정**

기존 ViewHolder
```kt
class BaseVH(view : View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.item_name)
        val age : TextView = view.findViewById(R.id.item_age)

        fun onbind(data : ProfileData){
            name.text = data.name
            age.text = data.age.toString()
        }

}
```
데이터바인딩 ViewHolder
```kt
 class ProfileVH(val binding : RcvListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data : ProfileData){
            binding.user = data
        }
    }
```
#### **(5) Adapter 수정**
기본 Adapter
```kt
class BaseAdapter (val context : Context) : RecyclerView.Adapter<BaseAdapter.BaseVH>(){

    var data = listOf<ProfileData>()
    ...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.BaseVH {
        val view  = LayoutInflater.from(context)
            .inflate(R.layout.rcv_list_item, parent, false)
        return BaseVH(view)
    }
    ...
}
```
데이터바인딩 Adapter
```kt
class ProfileAdapter (private val context : Context) : RecyclerView.Adapter<ProfileAdapter.ProfileVH>(){

    var data = listOf<ProfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileVH {
      val binding = RcvListItemBinding.inflate(
          LayoutInflater.from(context), parent, false)

        return ProfileVH(binding)
    }
    
    ...
}
```
#### **(6) MainActivity 수정**
```kt
class MainActivity : AppCompatActivity() {	
   private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
    	...
        setRcv()
        ...
    }
    
    ...
    
	fun setRcv(){
        val profileAdapter = ProfileAdapter(this)
        binding.mainRcv.layoutManager = LinearLayoutManager(this)
        binding.mainRcv.adapter = profileAdapter
        profileAdapter.data = listOf(
            ProfileData(name = "Kang", age = 26),
            ProfileData(name = "Kim", age = 25)
        )
        profileAdapter.notifyDataSetChanged()
    }
}
```

* * *

## ViewModel + LiveData + DataBinding이란?
> 최초작성: 2022.03.13

### 01. ViewModel
ViewModel을 사용하는 이유는? UI와 로직을 분리하기 위해서
<br>-> UI관련 데이터를 액티비티와 프래그먼트로부터 분리시킬 수 있음
<br>-> 즉, 액티비티와 프래그먼트는 UI를 업데이트 하는 데만 주력할 수 있고, ViewModel에 있는 데이터는 액티비티 또는 프래그먼트의 수명주기로부터 자유로워짐

#### **onCleared()**
액티비티의 onDestroy()가 호출된 후 실행되는 함수
<br>이 함수에서 ViewModel에 있는 리소스를 해제하기 적합
```kotlin
override fun onCleared() {
	if (lifecycleEventDelegate.isInitialized()) {
		lifecycleEvent.onNext(ViewModelEvent.CLEARED)
	}
	super.onCleared()
}
```

### 02. LiveData
LiveData란? 
1. 식별 가능한 데이터 홀더 클래스. 스스로 수명주기를 인식함
2. Data의 변경을 관찰할 수 있는 Data Holder 클래스

* UI와 데이터 상태의 일치 보장
* 메모리 누출이 없음
* 비정상 종료가 없음
* 수명주기를 자동으로 관리
* 최신의 데이터 유지
* 기기회전 등 프래그먼트나 액티비티가 재생성되어도 데이터의 변화가 없음

ViewModel과 함께해야 효과가 커짐<br>
ViewModel 안에 있는 LiveData 객체를 DataBinding을 통해 UI에서 관찰만 할 수 있도록 만들면 액티비티나 프래그먼트에서 일일히 데이터를 갱신할 필요 없이 알아서 UI에 최신 데이터가 보이게 될 것

* Observable과 다르게 LifeCycle을 알고있음 → 활성(active:STARTED/RESUMED)상태일 때만 데이터를 업데이트
* Observer 객체와 함께 사용 → LiveData 변화 감지!! → LiveData는 Observer에게 변화 알림 → Observer OnChanged() 메소드 호출

#### **Gradle 세팅**
```gradle
apply plugin: 'kotlin-kapt'
android {
	...
    
    buildFeatures{
        dataBinding true
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    // For Kotlin projects
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies{
	...
    
    def lifecycle_version = "2.2.0"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.activity:activity-ktx:1.1.0"
}
```
#### **MainViewModel.kt**
```kt
class MainViewModel():ViewModel(){
    // LiveData
    // 값이 변경되는 경우 MutableLiveData로 선언한다.
    var count = MutableLiveData<Int>()

    init {
        count.value = 0
    }

    fun increase(){
        count.value = count.value?.plus(1)
    }

    fun decrease(){
        count.value = count.value?.minus(1)
    }
}
```
* ViewModel안에 LiveData가 있는 모습
* LiveData 객체의 값이 변경될 경우에는 MutableLiveData<T>()으로 선언
* increase(), decrease() 함수를 선언 해 각각 값이 증가/감소하는 역할의 함수
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
            android:id="@+id/fab_plus"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{()->viewModel.increase()}"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:srcCompat="@drawable/ic_baseline_exposure_plus_1_24" />
```

* * *

## BaseActivity란?
> 최초작성: 2022.03.13
여러 Activity를 사용할 때 중복되는 코드를 미리 정의하여 필요한 코드만 구현하도록 사용하는 기본 액티비티
* T: ViewDataBinding , R: BaseViewModel

=> 코드의 중복을 줄이고 가독성 강화

* * *

## AppCompatActivity란?
> 최초작성: 2022.03.13
- setSupportActionBar(Toolbar) API를 사용하여 action item, navigation mode 등을 포함하는 action bar를 지원
- Theme.AppCompat.DayNight 테마를 사용하며 AppCompatDelegate.setDefaultNightMode(int) API를 사용하여 다크 모드를 지원
- getDrawerToggleDelegate() API를 사용하여 DrawerLayout과 통합

* * *