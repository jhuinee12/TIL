# Today I Learned(TIL : 1일 1커밋)

> 시작 : 2022.03.20

## MarkDown 작성법
+ [MarkDown 사용법 총정리](https://heropy.blog/2017/09/30/markdown/)
+ [README.md 작성하는 방법, 예시](https://m.blog.naver.com/jooeun0502/221956294941)
+ [Markdown 사용법: 링크 걸기](https://velog.io/@dblee/%EA%B9%83%ED%97%88%EB%B8%8CMarkdown-%EC%82%AC%EC%9A%A9%EB%B2%95-%EB%A7%81%ED%81%AC-%EA%B1%B8%EA%B8%B0)
+ [마크다운(MARKDOWN) 문법 사용법](https://eungbean.github.io/2018/06/11/How-to-use-markdown/)

- - -

## **TIL**

## [Common Basic](./til/common-basic/common-basic.md)

## [Kotlin](./til/kotlin/kotlin-idx.md)
+ [Kotlin 문법 정리](./til/kotlin/kotlin-basic.md)

## JAVA
+ [JAVA](./til/java/java.md)

## Algorithm
+ [기초 알고리즘 정리](./til/algorithm/algorithm.md)

## TIL-Android
+ [안드로이드 기본 기능](./til/til-android/til-android-basic.md)

- - -

## **번외**

## Git
+ [git 사용 기초](./extra/git/git.md)

## [Coding Test Practice](./extra/coding-test-practice/coding-test-practice.md)

### Programmers
+ [Java](./extra/coding-test-practice/programmers/java/programmers-java-idx.md)
+ [Kotlin](./extra/coding-test-practice/programmers/kotlin/programmers-kotlin-idx.md)

## My Dev FootPrint

### MDF-Android
+ [구현](./extra/my-dev-footprint/mdf-android/sample-source/idx.md)
+ [에러사항](./extra/my-dev-footprint/mdf-android/error/android-error.md)

### C#
+ [구현](./extra/my-dev-footprint/c%23/book-management-program.md)

- - -

<진행사항>

<진행예정>
* webview - CookieManager란? : Webview 애플리케이션 인스턴승서 사용하는 쿠키를 관리
```kt
clearHistory()	// 웹뷰에 내부 앞/뒤 목록을 지우도록 지시
clearCache(true)	// 리소스 캐시를 지움 -> 캐시는 응용 프로그램별이므로 사용된 모든 엡뷰에 대한 캐시가 지워짐

// CookieManager : Webview 애플리케이션 인스턴승서 사용하는 쿠키를 관리
CookieManager.getInstance()?.let { cookieManager ->
    cookieManager.setAcceptCookie(true)	// setAcceptCookie : 응용 프로그램의 Webview 인스턴스가 쿠키를 보내고 수락해야 하는지 여부를 설정 (기본값:true)
    cookieManager.removeSessionCookie() // removeSessionCookie() -> removeSessionCookies(android.webkit.ValueCallback) :: 세션 쿠키 제거
    cookieManager.setAcceptThirdPartyCookies(binding.webView, true)	// WebView타사 쿠키 설정을 허용 할지 여부 를 설정 :: lollipop 이후 버전부터 선언해줘야함
}
```

* publishsubject란? : 구독 이후에 갱신된 값에 대해서만 값을 받음
    * 구독하기 이전에 갱신된 빨간공, 초록공은 무시하고 파란공만 받음
    * 과거 데이터를 무시하고 새로 갱신된 값만 보고 싶은 경우 사용하기 유용
    * 대표적으로 버튼 클릭 이벤트에 많이 사용
    * create() : Observable을 생성하지만 just()와는 다르게 프로그래머가 직접 onNext, onComplete, onError를 호출해야함

* <내부 클래스의 장점>
1. 내부 클래스에서 외부 클래스의 멤버에 손쉽게 접근 가능
2. 서로 관련 있는 클래스를 논리적으로 묶어서 표현함으로써, 코드의 캡슐화 증가
3. 외부에서는 내부 클래스에 접근할 수 없으므로, 코드의 복잡성 감소

* LiveData란? Data의 변경을 관찰할 수 있는 Data Holder 클래스
Observable과 다르게 LifeCycle을 알고있음 → 활성(active:STARTED/RESUMED)상태일 때만 데이터를 업데이트
Observer 객체와 함께 사용 → LiveData 변화 감지!! → LiveData는 Observer에게 변화 알림 → Observer OnChanged() 메소드 호출
관찰자(Observer)패턴이란? 	옵저버들의 목록을 객체에 등록해서 상태 변화가 있을 때마다 메서드 등을 통해
							객체가 직접 목록의 각 옵저버에게 알리도록 하는 디자인 패턴
							각각의 옵저버들은 관찰 대상인 객체가 발생시키는 이벤트를 받아 처리
							:: 이벤트가 발생하면 각 옵저버들은 콜백을 받음
ViewModel과 함께해야 효과가 커짐
	::	ViewModel 안에 있는 LiveData 객체를 DataBinding을 통해 UI에서 관찰만 할 수 있도록 만들면
		액티비티나 프래그먼트에서 일일히 데이터를 갱신할 필요 없이 알아서 UI에 최신 데이터가 보이게 될 것

* BaseActivity란?
여러 Activity를 사용할 때 중복되는 코드를 미리 정의하여 필요한 코드만 구현하도록 사용하는 기본 액티비티
=> 코드의 중복을 줄이고 가독성 강화
* AppCompatActivity란?
	- setSupportActionBar(Toolbar) API를 사용하여 action item, navigation mode 등을 포함하는 action bar를 지원
	- Theme.AppCompat.DayNight 테마를 사용하며 AppCompatDelegate.setDefaultNightMode(int) API를 사용하여 다크 모드를 지원
	- getDrawerToggleDelegate() API를 사용하여 DrawerLayout과 통합

* abstract method - 추상 클래스 : 자식 클래스에서 반드시 오버라이딩해야만 사용할 수 있는 메서드
모듈처럼 중복되는 부분이나 공통적인 부분은 미리 다 만들어진 것을 사용 > 이를 받아 사용하는 쪽에서는 자신에게 필요한 부분만을 재정의하여 사용
생산성 향상, 배포가 쉬워짐
선언부만 존재. 구현부는 작성되어 있지 않음 -> 구현부는 자식클래스에서 오버라이딩하여 사용

* Interface란?
클래스가 반드시 구현해야 할 행동을 지정하는 데 사용되는 추상 타입
행동(Behavior) : 함수 or 메서드 -> 인터페이스에서는 행동을 정의
인터페이스를 구현할 때에는 implements 키워드를 사용
인터페이스를 구현하고자 한다면, 인터페이스내의 정의된 메소드들은 반드시 구현해야함

* ViewModel: View를 표현하기 위해 만든 View를 위한 Model && View를 나타내주기 위한 데이터 처리 담당
ViewModel을 사용하는 이유는? UI와 로직을 분리하기 위해서
-> UI관련 데이터를 액티비티와 프래그먼트로부터 분리시킬 수 있음
-> 즉, 액티비티와 프래그먼트는 UI를 업데이트 하는 데만 주력할 수 있고,
		  ViewModel에 있는 데이터는 액티비티 또는 프래그먼트의 수명주기로부터 자유로워짐

* EventBus
(event: Refresh) 는 이벤트 필터 기능
Refresh 이벤트가 아닌 다른 이벤트가 들어올 경우 실행되지 않음

* // Up : 상단 뒤로가기 버튼 || Back : 하단 뒤로가기 버튼
// navigateUp() 	: Up 버튼 실행 시 실행 	-> 스택에 아무 화면도 남아있지 않으면 새롭게 Activity를 생성 시키는 문제 야기
// popupBackStack() : Back 버튼 실행 시 실행 	-> 위 문제 때문에 되도록 이 함수를 사용하는게 더욱 용이

* typealias : 변수들에 대한 새로운 별명을 지어주고, 짧게 사용 가능
typealias를 클래스 혹은 함수 내에서 정의하는 것은 불가능하다
typealias는 누구나 접근할 수 있는 Top Level 변수이다
동일한 type을 여러 개의 이름으로 사용할 수 있다.
ex1-1. typealias NodeSet = Set<Network.Node>
ex1-2. typealias FileTable<K> = MutableMap<K, MutableList<File>>
ex2-1. typealias MyHandler = (Int, String, Any) > Uit
ex2-3. typealias Predicate<T> = (T) -> Boolean
PermissionDialog 클래스를 PermissionDialogCallback으로 정의
(PermissionDialog) -> Unit :: (인자타임) -> 반환타입