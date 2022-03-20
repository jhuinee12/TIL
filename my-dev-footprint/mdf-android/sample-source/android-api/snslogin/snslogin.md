## SNS로그인 기능 정리

<br>

### 목차
* [네이버로 로그인하기](#네이버로-로그인하기)
    * [네이버 로그인 기초](#네이버-로그인-기초)
    * [네이버 JSON 파싱으로 계정 정보 얻어오기](#네이버-json-파싱으로-계정-정보-얻어오기)
* [카카오로 로그인하기](#카카오로-로그인하기)
    * [카카오로 로그인하기 (V1)](#카카오로-로그인하기-v1)
    * [카카오로 로그인하기 (V2)](#카카오로-로그인하기-v2)
    * [친구(나)에게 카카오링크 메시지 보내기](#친구나에게-카카오링크-메시지-보내기)
* 라인으로 로그인하기
* [구글로 로그인하기](구글로-로그인하기)
* [페이스북으로 로그인하기](#페이스북으로-로그인하기)

----

## 네이버로 로그인하기

### 네이버 로그인 기초
> 최초작성 : 2021.05.10

**1\. Naver Developer에서 네이버 로그인 연동 API 개발 신청을 한다.**

[네이버 아이디로 로그인 - INTRO](https://developers.naver.com/products/login/api/api.md)

\* 현재 url이 없는 경우 아무 url이나 추가 후 나중에 배포 시 url 변경

**2\. 개발 환경 설정 - 네이버 로그인 sdk.aar 파일 다운로드 & build.gradle 의존성 추가**

[Releases · naver/naveridlogin-sdk-android](https://github.com/naver/naveridlogin-sdk-android/releases)

\* 여기서 Source code (zip) 다운로드 후 압축 풀기 -> \\naveridlogin-sdk-android-4.2.6\\naveridlogin\_android\_sdk 내 aar 파일을 libs 파일에 옮김

```gradle
implementation files('libs/naveridlogin_android_sdk_4.2.6.aar')    //네이버 로그인
```

\* 위 파일 의존성 추가

_**2-1) 네이버 Deverloper에는 의존성이 아래와 같이 나와있다.**_

```gradle
compile 'com.android.support:appcompat-v7:28.0.0'
compile 'com.android.support:support-core-utils:28.0.0'
compile 'com.android.support:customtabs:28.0.0'
compile 'com.android.support:support-v4:28.0.0'
```

\* 안드로이드는 더이상 compile을 지원하지 않으므로 compile을 지원하지 않으므로 이를 implementaion으로 변경해도 여전히 오류가 난다.

_오류코드: Version 28 (intended for Android Pie and below) is the last version of the legacy support library, so we recommend that you migrate to AndroidX libraries when using Android Q and moving forward. The IDE can help with this: Refactor > Migrate to AndroidX_

Version 28에서는 다음과 같은 의존성을 지원하지 않는다는 뜻으로, Refactor>Migrate to AndroidX...를 해준 뒤 하단에서 Migration을 한번 더 눌러주면 다음과 같이 마이그레이션이 되어 소스가 적용된다.

```gradle
implementation 'androidx.appcompat:appcompat:1.0.0'
implementation 'androidx.legacy:legacy-support-core-utils:1.0.0'
implementation 'androidx.browser:browser:1.0.0'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
```

*이거 알아내는데 이틀이 걸렸다...*

**3\. AndroidManifest.xml에 인터넷 사용 권한을 준다.**

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

**4\. 일일히 client\_id/client\_secret 등을 설정하는 대신 strings.xml에 미리 등록해둔다.**

```xml
<resources>
    <string name="app_name">SnsLoginTest</string>
    <string name="naver_client_id">client_id명</string>
    <string name="naver_client_secret">client_secret번호</string>
    <string name="naver_client_name">아무거나</string>
</resources>
```

**5\. 버튼을 미리 저장해둔 후 해당 버튼을 사용하여 레이아웃을 만든다.**

[로그인 버튼 사용 가이드 - LOGIN](https://developers.naver.com/docs/login/bi/bi.md)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.nhn.android.naverlogin.ui.view.OAuthLoginButton
        android:id="@+id/btn_naver_login"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
```

**6\. 네이버 아이디로 로그인 라이브러리 애플리케이션에 적용하기 위해 로그인 인스턴스 초기화**

```java
    public static OAuthLogin mOAuthLoginModule;
    public static Context mContext;
    public static OAuthLoginButton mOAuthLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // 초기화
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                mContext
                ,getString(R.string.naver_client_id)
                ,getString(R.string.naver_client_secret)
                ,getString(R.string.naver_client_name)
        );

        mOAuthLoginButton = (OAuthLoginButton)findViewById(R.id.btn_naver_login);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }
```

\* OAuthLogin.init() 메서드가 여러 번 실행돼도 기존에 저장된 접근 토큰(access token)과 갱신 토큰(refresh token)은 삭제되지 않습니다.  
\* 기존에 저장된 접근 토큰과 갱신 토큰을 삭제하려면 OAuthLogin.logout() 메서드나 OAuthLogin.logoutAndDeleteToken() 메서드를 호출합니다.

**7\. 로그인 로직을 구현합니다.**

```java
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {

            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                Log.i("LoginData","accessToken : "+ accessToken);
                Log.i("LoginData","refreshToken : "+ refreshToken);
                Log.i("LoginData","expiresAt : "+ expiresAt);
                Log.i("LoginData","tokenType : "+ tokenType);

            } else {
                String errorCode = mOAuthLoginModule
                        .getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };
```

### 네이버 JSON 파싱으로 계정 정보 얻어오기
> 최초작성 : 2021.05.14

**1\.** **로그인이 완료되어 id값과 pw값이 일치한다는 토큰을 얻어오고 토큰와 유저정보를 받아올 주소를 이용해 값을 추출한다.**

```java
btnNaverLogin.setOAuthLoginHandler(mOAuthLoginHandler); // 네이버 핸들러 연결
```

```java
private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
	@Override
	public void run(boolean success) {
		if (success) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String accessToken = mOAuthLoginModule.getAccessToken(mContext);
					String header = "Bearer " +  accessToken;
					Map<String, String> requestHeaders = new HashMap<>();
					requestHeaders.put("Authorization", header);
					String apiURL = "https://openapi.naver.com/v1/nid/me"; //엑세스 토큰으로 유저정보를 받아올 주소
					String responseBody = get(apiURL,requestHeaders);
					Log.d("responseBody 확인 ",responseBody); //주소로 얻은 유저정보 (제이슨)
					NaverUserInfo(responseBody);

					login_state = "n";
					new Thread()
					{
						public void run()
						{
							Message msg = btnInVisibleHandler.obtainMessage();
							btnInVisibleHandler.sendMessage(msg);
							txtLoginState.setText("네이버 : " + login_name);
						}
					}.start();
				}
			}).start();

		} else {
			String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
			String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
			Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
		}
	}
};
```

**2\. 위에서 얻어온 유저 정보를 NaverUserInfo 메서드를 만들어 매개변수로 보낸다. 이 메서드에서는 get() 메서드로 키 값을 이용해 밸류를 가져온다.**

```java
private void NaverUserInfo(String msg){
	JSONParser jsonParser = new JSONParser();

	try {
		JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
		String resultcode = jsonObject.get("resultcode").toString();
		Log.d("resultcode 확인 ",resultcode);

		String message = jsonObject.get("message").toString();
		Log.d("message 확인 ",message);

		if(resultcode.equals("00")){
			if(message.equals("success")){
				JSONObject naverJson = (JSONObject) jsonObject.get("response");

				login_name = naverJson.get("name").toString();
				Log.d("이름 확인 ",login_name);
			}
		}
		else{
			//Toast.makeText(getApplicationContext(),"로그인 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
		}
	}
	catch (ParseException e) {
		e.printStackTrace();
	}
}
```

```java
private static String get(String apiUrl, Map<String, String> requestHeaders){
	HttpURLConnection con = connect(apiUrl);
	try {
		con.setRequestMethod("GET");
		for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
			con.setRequestProperty(header.getKey(), header.getValue());
		}

		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
			return readBody(con.getInputStream());
		} else { // 에러 발생
			return readBody(con.getErrorStream());
		}
	} catch (IOException e) {
		throw new RuntimeException("API 요청과 응답 실패", e);
	} finally {
		con.disconnect();
	}
}
```

**3\. get() 메서드에서는 HttpURLConnection() 메서드를 통해 url과 연결된다.**

```java
private static HttpURLConnection connect(String apiUrl){
	try {
		java.net.URL url = new URL(apiUrl);
		return (HttpURLConnection)url.openConnection();
	} catch (MalformedURLException e) {
		throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	} catch (IOException e) {
		throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	}
}
```

**4\. get() 메서드에서는 readBody() 메서드를 통해 데이터들을 읽어온다.**

```java
private static String readBody(InputStream body){
	InputStreamReader streamReader = new InputStreamReader(body);

	try (BufferedReader lineReader = new BufferedReader(streamReader)) {
		StringBuilder responseBody = new StringBuilder();

		String line;
		while ((line = lineReader.readLine()) != null) {
			responseBody.append(line);
		}

		return responseBody.toString();
	} catch (IOException e) {
		throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	}
}
```

## 카카오로 로그인하기

### 카카오로 로그인하기 (V1)
> 최초작성 : 2021.05.11

**0\. Kakao Developer 기본 세팅**

[Kakao Developers](https://developers.kakao.com/docs/latest/ko/kakaologin/android)

```java
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(),0));
                Log.e("Hash Key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }
```

\* 세팅 시 키해시가 필요한데, 이는 위 코드를 이용하여 구할 수 있다.

**1\. build.gradle 의존성 추가**

* build.gradle(Module)
```gradle
implementation 'com.kakao.sdk:usermgmt:1.27.0'  // 카카오 로그인
```

* build.gradle(Project)
```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
    }
}
```

**2\. Kakao Login Adapter 생성**

```java
public class KakaoLogin extends Application {
    private static volatile KakaoLogin instance = null;

    private static class KakaoSDKAdapter extends KakaoAdapter {
        // 카카오 로그인 세션을 불러올 때의 설정값을 설정하는 부분.
        public ISessionConfig getSessionConfig() {

            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                    //로그인을 어떤 방식으로 할지 지정
                    //KAKAO_LOGIN_ALL: 모든 로그인방식을 사용하고 싶을때 지정.
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                    // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                    // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume 시에 Timer를 설정해 주어야 한다.
                    // 지정하지 않을 시 false로 설정된다.
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                    // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                    // 일반 사용자가 아닌 Kakao와 제휴된 앱에서만 사용되는 값으로, 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                    // Kakao SDK 에서 사용되는 WebView에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                    // true일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return KakaoLogin.getGlobalApplicationContext();
                }
            };
        }
    }

    public static KakaoLogin getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
```

**3\. AndroidManifest.xml에 위 내용 추가**

```xml
    <application
        android:name=".KakaoLogin"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:usesCleartextTraffic="true"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.SnsLoginTest_210511">
```

* application>androi:name 부분에 (2번)에서 만든 클래스 이름을 넣어준다.

**4\. 로그인 버튼 클릭 로직 구현**

* ISessionCallback : 세션의 상태 변경에 따른 콜백, 세션이 오픈되었을 때, 세션이 만료되어 닫혔을 때 세션 콜백을 넘기게 된다.

```java
   static private ISessionCallback mSessionCallBack = new ISessionCallback() {
        @Override
        public void onSessionOpened() {	// access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태
            Log.d("Kakao Login","MainActivity.java - onSessionOpened()");
            // 로그인 요청
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    // 로그인 실패
                    Toast.makeText(mContext,"로그인 도중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    // 세션이 닫힘..
                    Toast.makeText(mContext,"세션이 닫혔습니다. 다시 시도하세요", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Toast.makeText(mContext,result.getKakaoAccount().getProfile().getNickname() + "님 환영합니다!", Toast.LENGTH_LONG).show();
                    Log.d("Kakao Login","카카오 로그인 성공");
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 네트워크 등으로 로그인 실패
            Toast.makeText(mContext,exception.toString(), Toast.LENGTH_LONG).show();
            Log.d("Kakao Login",exception.toString());
        }
    };
```

```java
Session.getCurrentSession().addCallback(mSessionCallBack);
Session.getCurrentSession().checkAndImplicitOpen();
```

* 위에 정의한 메서드를 실행시키기 위해 onCreate() 메서드에서 해당 소스를 통해 메서드 실행

### 카카오로 로그인하기 (V2)
> 최초작성 : 2021.05.14

**1\. AndroidManifest.xml에 아래 코드를 입력하여 Redirect URI 설정합니다.**

```xml
<activity android:name="com.kakao.sdk.auth.AuthCodeHandlerActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <!-- Redirect URI: "kakao{NATIVE_APP_KEY}://oauth" -->
        <data android:host="oauth"
                android:scheme="kakao{NATIVE_APP_KEY}" />
    </intent-filter>
</activity>
```

\* 카카오 로그인 기능을 구현하기 위해서는 리다이렉션(Redirection)을 통해 인가 코드를 받아야 합니다.

**2\. 카카오 sdk2 로그인을 사용하기 위하여 build.gradle(:app) dependcies에 추가해줍니다.**

```gradle
implementation "com.kakao.sdk:v2-user:2.5.0" // 카카오 로그인 sdk v2
```

```gradle
repositories {
  maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' }
}
```

**3\. 로그인을 실행할 부분에 아래 코드를 작성해주세요.**

```java
if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(mContext)) {
	// 카카오톡이 설치되어 있을 경우
	UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, kakaoCallback);
} else {
	// 카카오톡이 설치되어있지 않은 경우
	UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, kakaoCallback);
}
```

* SDK V2는 카카오톡이 있으면 카카오톡으로 로그인이 되고, 없으면 웹페이지로 로그인이 됩니다.
* 여기서 kakaoCallback을 실행시킨다.

**4\. 토큰값을 얻어와 로그인 여부를 따지고, 카카오 프로필 정보를 얻어오는 getKakaoProfile() 메서드를 실행한다.**

```java
Function2<OAuthToken, Throwable, Unit> kakaoCallback = (oAuthToken, throwable) -> {
	if (oAuthToken != null) {
		Log.e("TAG", "로그인 성공");
	}
	if (throwable != null) {
		Log.e("TAG", "로그인 실패", throwable);
		Log.d("TAG", "Message : " + throwable.getLocalizedMessage());
	}
	getKaKaoProfile();
	return null;
};
```

**5\. 로그인 계정 정보를 받아온다.**

```java
private void getKaKaoProfile() {
	UserApiClient.getInstance().me((user, throwable) -> {
		if (user != null) {
			login_name = user.getKakaoAccount().getProfile().getNickname();
			Log.d("TAG", "Kakao id =" + login_name);
			txtLoginState.setText("카카오톡 : " + login_name);
			Toast.makeText(mContext,"환영합니다!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext,"계정 정보 없음!", Toast.LENGTH_LONG).show();
		}
		if (throwable != null) {
			Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());
		}
		return null;
	});
}
```

### 친구(나)에게 카카오링크 메시지 보내기
> 최초작성 : 2021.05.14

 [Kakao Developers](https://developers.kakao.com/docs/latest/ko/message/android-link)

**1\. AnroidManifest.xml에 쿼리 요소를 추가해주고 커스텀 스킴(Scheme)을 설정해줍니다.**

```xml
<manifest package="com.example.sample">
    <!--queries에 카카오톡 패키지 추가-->
    <queries>
        <package android:name="com.kakao.talk" />
    </queries>
    ...
</manifest>
```

```xml
<activity android:name=".{YOUR_ACTIVITY_NAME}">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <!-- "kakao{YOUR_NATIVE_APP_KEY}://kakaolink" 형식의 앱 실행 스킴을 설정하는데 사용 -->
        <data android:host="kakaolink"
                android:scheme="kakao{YOUR_NATIVE_APP_KEY}" />
    </intent-filter>
</activity>
```

**2\. Kakao Sdk를 초기화시켜주는 클래스를 생성하고 해당 클래스 이름을 매니페스트 name에 추가합니다.**

```java
public class KakaoApplication extends Application {
    private static volatile KakaoApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "0a7897983e0a0efaacad8f23437ba7a9");
    }
}
```

```xml
   <application
        android:name=".KakaoApplication"
        ... >
```

**3\. 카카오링크를 통해 보낼 템플릿을 설정해준다.**

```java
public static Content content;
public static Social social;
public static List<com.kakao.sdk.template.model.Button> buttons;
public static FeedTemplate defaultFeed;

Link link = new Link("https://jhuinee-diary.tistory.com/","https://jhuinee-diary.tistory.com/");
content = new Content("Title", "이미지 url", link , "desc");
social = new Social(286,45,845);
buttons = List.of(new com.kakao.sdk.template.model.Button("웹으로 보기",link));

defaultFeed = new FeedTemplate(GlobalVar.content, GlobalVar.social, buttons);
```

* [com.kakao.sdk.template.model.Button](http://m.kakao.sdk.template.model.Button) : android.widget.Button과 중복되지 않게 안드로이드 자체에서 저렇게 추가됨
* 이 때 url은 카카오 디벨로퍼에 웹 도메인 URL로 등록해야한다.

**4\. LinkClient를 통해 해당 템플릿을 보낸다.**

```java
LinkClient.getInstance().defaultTemplate(mContext, defaultFeed,null, (linkResult, throwable) -> {
	if (throwable != null) {
		Log.e("TAG", "카카오링크 보내기 실패", throwable);
	}
	else if (linkResult != null) {
		mContext.startActivity(linkResult.getIntent());

		// 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
		Log.w("TAG", "Warning Msg: "+ linkResult.getWarningMsg());
		Log.w("TAG", "Argument Msg: "+ linkResult.getArgumentMsg());
	}
	return null;
});
```

## 라인으로 로그인하기

## 구글로 로그인하기
> 최초작성 : 2021.05.12

**1\. 파이어베이스에 앱 추가한다.**

[Android 프로젝트에 Firebase 추가](https://firebase.google.com/docs/android/setup?hl=ko)

\* 이대로 따라하다보면 의존성 추가하는 법까지 나와있다. 아예 단계별로 같이 진행시킴

**2\. xml 파일에 버튼 추가**

```xml
    <com.google.android.gms.common.SignInButton
        android:id="@+id/btn_google_login"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

**3\. 로그인 객체를 생성한다.**

```java
private FirebaseAuth auth;  // 파이어 베이스 인증 객체
private GoogleSignInClient googleSignInClient;
private final int REQ_SIGN_GOOGLE = 9001; // 구글 로그인 결과 코드
```

**4\. GoogleSignInOptions 객체를 생성하고 requestIdToken을 호출한다.**

```java
// Configure Google Sign In
GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
```

**5\. 현재 인증상태를 확인하기 위한 FirebaseAuth 객체를 생성하고 초기화해준다.**

```java
auth = FirebaseAuth.getInstance(); 
```

**6\. 구글 로그인 버튼을 선택했을 때, 로그인 화면이 뜨도록 Intent 해준다.**

```java
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 구글 로그인 화면 intent
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_SIGN_GOOGLE);
            }
        });
```

**7\. (6번)에서 intent 되었던 액티비티에서 로그인 계정 값을 받아 매개변수로 resultGoogleLogin 메서드에 넘겨준다(8번)**

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SIGN_GOOGLE) {
        	Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        	try {
        		// Google Sign In was successful, authenticate with Firebase
        		GoogleSignInAccount account = task.getResult(ApiException.class);
        		resultGoogleLogin(account);
        	} catch (ApiException e) {
        		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        		Log.d("google login", e.toString());
        	}
        }
    }
```

\* onActivityResult : sub액티비티 호출로 넘어갔다가 다시 main 액티비티로 돌아올 때 사용되는 메서드

**8.  구글 로그인 인증을 요청하고 결과값을 받아온다.**

```java
private void resultGoogleLogin(GoogleSignInAccount account) {
	AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
	auth.signInWithCredential(credential)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if(task.isSuccessful()) {
						Toast.makeText(getApplicationContext(), account.getDisplayName() + "님 환영합니다!", Toast.LENGTH_LONG).show()
					} else {
						Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_LONG).show();
					}
				}
			});
}
```

## 페이스북으로 로그인하기
> 최초작성 : 2021.05.12

 [Android - Facebook 로그인](https://developers.facebook.com/docs/facebook-login/android)

**1.  dependcies{}에 의존성을 추가해준다.**

```gradle
implementation 'com.facebook.android:facebook-login:[8.1)'
```

**2\. 리소스 및 매니페스트 수정**

_**2-1 )/app/res/values/strings.xml 파일에 아래 소스 추가한다.**_

```xml
<string name="facebook_app_id">{facebook_app_id}</string>
<string name="fb_login_protocol_scheme">{fb_login_protocol_scheme}</string>
```

_**2-2) AndroidMenifest.xml에 meta-data 요소, Facebook에 대한 활동, Chrome 맞춤 탭에 대한 활동 및 인텐트 필터를 application 요소를 추가해준다.**_

```xml
<meta-data
	android:name="com.facebook.sdk.ApplicationId"
	android:value="@string/facebook_app_id"/>
<activity
	android:name="com.facebook.FacebookActivity"
	android:configChanges= "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
	android:label="@string/app_name" />
<activity
	android:name="com.facebook.CustomTabActivity"
	android:exported="true">
	<intent-filter>
	<action android:name="android.intent.action.VIEW" />
		<category android:name="android.intent.category.DEFAULT" />
		<category android:name="android.intent.category.BROWSABLE" />
		<data android:scheme="@string/fb_login_protocol_scheme" />
	</intent-filter>
</activity>
```

**3\. xml에 페이스북 로그인 버튼을 추가한다.**

```xml
<com.facebook.login.widget.LoginButton
    android:id="@+id/login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="30dp"
    android:layout_marginBottom="30dp" /> 
```

**4\. 버튼 클릭리스너 안에 콜백을 등록해준다.**

_**4-1) 로그인 응답을 처리할 콜백 관리자를 만든다.**_

```java
callbackManager = CallbackManager.Factory.create();
```

_**4-2) 로그인 계정의 정보를 담을 객체 LoginManager를 생성한다.**_

```java
LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
```

\* 계정 정보 중 "public\_profile"에 담긴 내용들을 배열로 담아옴

**_4-3) 로그인을 실행하는 LoginManager를 콜백한다._**

```java
LoginManager.getInstance().registerCallback(callbackManager,
		new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// 로그인 성공 로직
			}

			@Override
			public void onCancel() {
				// 로그인 취소 로직
			}

			@Override
			public void onError(FacebookException exception) {
				// 로그인 실패 로직
			}
		});
```

**5\. SubActivity(로그인화면)에서 로그인을 진행한 후 MainActivity로 돌아왔을 때 callbackManager.onActivityResult를 호출하여 로그인 결과를 callbackManager를 통해 LoginManager에 전달합니다.**

```java
  @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	callbackManager.onActivityResult(requestCode, resultCode, data);
	super.onActivityResult(requestCode, resultCode, data);
}
```

**6\. onSuccess() 메서드 안에서 로그인 계정 정보를 가져온다.**

**_6-1) 앱에서는 한 번에 사용자 한 명만 로그인할 수 있으며 LoginManager는 로그인한 사용자의 현재 AccessToken 및 Profile을 설정한다._**

```java
AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
```

_**6-2) 위에서 얻어온 토큰을 이용해 계정 정보를 받는 GraphRequest 객체를 생성한다.**_

```java
GraphRequest request = GraphRequest.newMeRequest(fbAccessToken, (object, response) -> {
	try {
    		// 로그인 정보
		Log.d("FB", "login email : " + object.getString("email"));
	} catch (JSONException je) {
		Log.d("FB", "No key provided.");
	}
});
```

_**6-3) 위에서 만든 request를 호출한다.**_

```java
Bundle parameters = new Bundle();
parameters.putString("fields", "id,name,email,gender,birthday");
request.setParameters(parameters);
request.executeAsync();
```

* Bundle 형식의 parameters에 객체를 생성한다. (Bundle 클래스에 Mapping(대응, 변환)하는 것이다.)
* parameters에 "fields"라는 object와 위 내용들을 reponse에 넣고, 이를 request에 매개변수로 뿌려준다.
* request 객체를 실행시켜 parameters에 들어간 값을 반환한다.