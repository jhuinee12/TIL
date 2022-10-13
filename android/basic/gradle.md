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