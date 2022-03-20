## Android Webview 공부
+ [1. 주소를 가져와 화면에 호출](#1-주소를-가져와-화면에-호출)

---

## 1. 주소를 가져와 화면에 호출
> 최초작성 : 2021.09.23

**01\. 환경설정**

Webview에 곧바로 loadUrl로 띄울 경우 ERR\_CLEARTEXT\_NOT\_PERMITTED 에러 발생

_**설정 1. AndroidMenifest 인터넷 접속 권한 추가**_

```xml
<!-- 인터넷 접속 권한 추가 -->
<uses-permission android:name="android.permission.INTERNET" />
```

**_설정 2. AndroidMenifest application에 usesCleartextTraffic="true" 설정_**

**_https 경로의 경우 접속이 가능하나 http 경로의 경우 접속이 불가능_**

```xml
<application
	android:usesCleartextTraffic="true"
    	...>
```

**02\. 레이아웃 세팅**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".Web1Activity">

    <WebView
        android:id="@+id/webview1"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

**03\. 소스코드**

_**<JAVA를 이용한 샘플 코드>**_

```java
public class Web1Activity extends AppCompatActivity {
    private ActivityWeb1Binding web1Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web1Binding = ActivityWeb1Binding.inflate(getLayoutInflater());
        setContentView(web1Binding.getRoot());

        // 웹뷰 안에서 자바스크립스 실행 허용
        web1Binding.webview1.getSettings().setJavaScriptEnabled(true);
        // 현재 화면(액티비티)에 출력되도록 설정 :: 이 코드가 없으면 웹브라우저 호출됨
        web1Binding.webview1.setWebViewClient(new WebViewClient());
        // 웹뷰에서 해당 url 로드
        web1Binding.webview1.loadUrl("http://m.nate.com");
    }
 }
```

**_<Kotlin을 이용한 샘플 코드>_**

```kt
class Web1Activity : AppCompatActivity() {
    private var web1Binding: ActivityWeb1Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web1Binding = ActivityWeb1Binding.inflate(layoutInflater)
        setContentView(web1Binding!!.root)

        // 웹뷰 안에서 자바스크립스 실행 허용
        web1Binding.webview1.settings.javaScriptEnabled = true
        // 현재 화면(액티비티)에 출력되도록 설정 :: 이 코드가 없으면 웹브라우저 호출됨
        web1Binding.webview1.webViewClient = WebViewClient()
        // 웹뷰에서 해당 url 로드
        web1Binding.webview1.loadUrl("http://m.nate.com")
    }
 }
```

### **04\. 실행화면**

![](./image/webview-basic-complete.gif)

---

