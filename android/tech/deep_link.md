# DeepLink란?
> 최초작성 : 2022.05.20

DeepLink란 특정 페이지에 도달할 수 있는 링크를 의미한다.<br>
DeepLink는 특정 주소 혹은 값을 입력하면 앱이 실행되거나 앱 내 특정 화면으로 이동시키는 기능을 수행한다,<br>
지연된 DeepLink는 앱 설치 후 실행하면 특정 화면으로 바로 이동하게 된다.

### DeepLink의 구분 방식

1. URI 스킴 방식: 앱에 URI 스킴(scheme) 값을 등록하여 딥링크 사용
2. 앱링크(App Link): Android 제공 - 도메인 주소를 이용한 딥링크 사용
3. 유니버셜 링크 (Universal Link): iOS 제공 - 도메인 주소를 이용한 딥링크 사용

### DeepLink의 형태
```
Scheme://Path
```
* Scheme → 앱을 특정함 (ex. 인스타그램)
* Path → 앱 내 페이지를 특정함 (ex. 인스타그램 내 피드)