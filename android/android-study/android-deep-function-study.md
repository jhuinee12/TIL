* [DeepLink란?](#deeplink란) - 2022.05.20
* [gradle 활용하기](#gradle-활용하기) - 2022.08.26

<br>

* * *

<br>

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

<br>

* * *

<br>

# gradle 활용하기
> 최초작성 : 2022.08.26

회사에서 apk를 추출하는데 자동으로 이름이 생성이 되고, 직접 폴더를 만들어 그 폴더에도 저장이 된다.

gradle에 해당 코드가 있었고, 찾아보니 gradle을 통해 설정할 수 있는 기능이었다.

## 1. apk 생성 시 빌드타입, 날짜, 버전명 자동 기입
*build.gradle (:app)*
```gradle
android {
    ...
    buildTypes {
        ...
        applicationVariants.all { variant -> 
            variant.outputs.all {
                def name = parent.project.getName()
                def buildType = variant.buildType.name
                def versionName = variant.versionName
		        outputFileName = new File("${name}_${buildType}_${versionName}.apk")
            }
        }
    }
}
```
* applicationVarients 객체는 각 빌드 변형의 내부 속성을 참조할 수 있음
* outputFileName은 assemble 태스크 결과로 생성되는 APK 파일의 파일명을 의미함

## 2. 생성된 apk를 특정 폴더로 복사
*build.gradle (:app)*
```gradle
android.applicationVariants.all { variant ->
	variant.outputs.all {output ->
        // 1번과 이어짐
		outputFileName = new File("${name}_${buildType}_${versionName}.apk")

		def taskSuffix = variant.name.capitalize()
		def assembleTaskName = "assemble$taskSuffix"

		if (tasks.findByName(assembleTaskName)) {
            // (=) def copyAPKFolderTask = tasks.create("archive$taskSuffix", Copy) {
			def copyAPKFolderTask = tasks.create(name: "archive$taskSuffix", type: org.gradle.api.tasks.Copy) {
				from buildDir
				into "{복사를 원하는 위치}"
				include parent.name + ".apk"
			}

			tasks[assembleTaskName].finalizedBy = [copyAPKFolderTask]
		}
	}
}
```