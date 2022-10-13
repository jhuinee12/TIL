# This version of the Android Support plugin for IntelliJ IDEA
> 최초작성 : 2022.03.28

![](./image/This%20version%20of%20the%20Android%20Support%20plugin%20for%20IntelliJ%20IDEA.png)

두 대의 노트북으로 개발을 하다보니 한 노트북에서는 다음과 같은 오류가 났다.

해당 문제는 안드로이드 스튜디오 버전이 맞지 않을 경우 생기는 문제이다.

해결 1. 낮은 버전의 안드로이드 스튜디오를 업데이트하여 높은 버전과 맞춰준다.

해결 2. 아래 buildscript를 build.gradle(project)에 추가해준다.
```gradle
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:7.0.2"

    }
}

plugins {
    id 'com.android.application' version '7.0.0' apply false
    id 'com.android.library' version '7.0.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.6.10' apply false
}
```