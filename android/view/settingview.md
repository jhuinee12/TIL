# 설정화면 구현하기
> 최초작성 : 2021.05.12

**1\. 설정화면(Preference)를 사용하기 위하여 의존성을 주입한다.**

```gradle
//설정화면에서 사용할 preference
implementation 'androidx.preference:preference:1.1.0-alpha01'
```

**2\. 설정 화면을 구현한다. (res>xml 폴더 생성, XML Resource File)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
	<PreferenceCategory android:title="{세팅 타이틀}">
      <CheckBoxPreference
          android:title="{타이틀로 쓸 내용}"
          android:summary="{작은 글씨로 상세 설명을 보여주는 곳}"
          android:key="chbox"
          />
      <ListPreference
          android:title="Ringtone"
          android:summary="벨소리를 선택할 수 있습니다."
          android:key="ls_alarm_bell_choice"
          android:entries="@array/Ringtone"
          android:entryValues="@array/Ringtone"
          />
    </PreferenceCategory>
    <SwitchPreference
        android:title="{타이틀로 쓸 내용}"
        android:summary="{작은 글씨로 상세 설명을 보여주는 곳}"
        android:key="switch"
    />
</PreferenceScreen>
```

\* CheckBoxPreference는 설정을 체크하거나 해제할 수 있도록 지원해준다.

\* SwitchPreference는 설정을 on/off 할 수 있도록 지원해준다.

\* ListPreference는 선택 시 팝업 창으로 선택할 수 있는 리스트들이 뿌려지는데, 이건 values/strings.xml에서 선언해준다.

**_2-1) values/strings.xml에 리스트 추가한다._**

```xml
<string-array name="Ringtone">
	<item>aaa</item>
	<item>bbb</item>
	<item>ccc</item>
	<item>ddd</item>
</string-array>
```

\* string-array의 이름을 name으로 주었으므로 @array/Ringtone을 가져오면 해당 item들이 자동으로 뿌려진다.

**3\. 알람을 세팅해준 xml을 환경 설정 프래그먼트(PreferenceFragment)에 접착시킬 클래스를 하나 생성한다.**

```kt
class SettingsPreference: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.alarm_settings_layout)
    }
}
```

**4\. 빈 액티비티를 이용하여 설정 화면을 연다.**

```xml
<?xml version="1.0" encoding="utf-8"?>
<fragment xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragment_setting"
    android:name="com.example.{프로젝트명}.fragment.{xml 연결시킨 프래그먼트 클래스명}"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</fragment>
```

---