# Android 구현 팁 정리
- [SNS Login](./snslogin/snslogin.md)
- [Android 고유 기능](#Android-고유-기능)
- [Android 구현](#Android-구현)
    - [위치 퍼미션 (권한 허용)](#위치-퍼미션-권한-허용)

----

# Android 고유 기능

## [SNS Login](./snslogin/snslogin.md)

# Android 구현
## 위치 퍼미션 (권한 허용)
> ### ***위치 정보 엑세스 권한 유형***
- [1. 포그라운드 위치](#1-포그라운드-위치)
- [2. 백그라운드 위치](#2-백그라운드-위치)

#### **1. 포그라운드 위치**
- 내비게이션 앱에서 사용자가 세부 경로 안내를 받을 수 있는 기능
- 메시지 앱에서 사용자가 현재 위치를 다른 사용자와 공유할 수 있는 기능

위 예시와 같이 앱에 위치 정보를 한 번만 또는 정의된 시간 동안 공유하거나 수신하는 기능이 포함되어 있으면 포그라운드 위치 정보 엑세스 원한 필요

- ***ACCESS_COARSE_LOCATION*** : 도시 블록 내에 위치 정확성을 제공
- ***ACCESS_FINE_LOCATION*** : ACCESS_COARSE_LOCATION을 요청할 때 제공되는 위치보다 더 정확한 위치를 제공

<br>

#### **2. 백그라운드 위치**
- 가족 위치 공유 앱에서 사용자가 가족 구성원과 지속적으로 위치를 공유할 수 있는 기능
- IoT 앱에서 사용자가 집을 나갈 때 꺼지고 집에 돌아올 때 다시 켜지도록 홈 기기를 구성할 수 있는 기능

위 예시와 같이 앱의 기능이 지속적으로 다른 사용자와 위치를 공유하거나 [Geofencing API](https://developer.android.com/training/location/geofencing?hl=ko)를 사용하는 경우 백그라운드 위치 정보 엑세스 권한 필요

- Android 10(API 수준 29) 이상에서 개발자는 런타임 시 백그라운드 위치 정보 액세스 권한을 요청하기 위해 앱 매니페스트에서 ACCESS_BACKGROUND_LOCATION 권한을 선언
- 이전 버전의 Android에서는 앱이 포그라운드 위치 정보 액세스 권한을 수신하면 자동으로 백그라운드 위치 정보 액세스 권한도 수신

<br>

- 앱의 기능이 Android 10(API 수준 29)을 실행하는 기기에서 백그라운드 위치를 요청하면 시스템 권한 대화상자에는 항상 허용이라는 옵션이 포함
- 하지만 Android 11(API 수준 30) 이상에서는 시스템 대화상자에 항상 허용 옵션이 포함되지 않음 :: 설정 페이지에서 백그라운드 위치를 사용 설정

<br>

> ### ***소스 예제***
#### **1. AndroidManifest.xml 설정**
```xml
    <!--도시 블록 내에 위치 정확성을 제공-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <!--ACCESS_COARSE_LOCATION을 요청할 때 제공되는 위치보다 더 정확한 위치를 제공-->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <!-- Android 10(API 수준 29) 이상 백그라운드 위치 정보 액세스 권한을 요청 :: Geofencing API(위치범위 지정) 사용하기 위해 선언 -->
    <!-- Android 10(API 수준 29) 이전 버전에서는 자동으로 백그라운드 위치 정보 엑세스 권한 수신 -->
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```
- Manifest에 위치 권한을 추가한 뒤에, 필요한 시점에 사용자에게 위치 권한을 요청

<br>

#### **2. Android 10.0 이상 버전과 이전 버전을 나눠서 권한 설정**
```kotlin
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
{
    // Android 10(API 29) 이상 버전 :: 항상 허용 / 앱 사용 중에만 허용 / 거부
    // Android 11(API 30) 이상 버전 :: 항상 허용 / 앱 사용 중에만 허용 / 이번만 허용 / 거부
    // Android 11(API 30) 이상 버전에서 항상 허용은 애플리케이션 설정 화면으로 들어가야 사용 가능
    Log.e(GlobalVar.TAG, "onCreate: >= android.os.Build.VERSION_CODES.Q")
    checkPermission10()
}
else
{
    // Android 10(API 29) 이전 버전 :: 허용 / 거부
    Log.e(GlobalVar.TAG, "onCreate: < android.os.Build.VERSION_CODES.Q")
    checkPermission6()
}
```
- Android 6.0 버전, Android 10.0 버전, Android 11.0 버전 별로 권한 설정이 다름

<br>

#### **3. Android 6.0 이상 버전 위치 퍼미션**
```kotlin
// TODO: 2021-07-14 한재희 위치퍼미션 6.0 버전 이상
private fun checkPermission6() {
    val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // 위치 권한 확인 :: 현재 위치 권한 없음

        // shouldShowRequestPermissionRationale :: 사용자가 거부했을 경우 true 반환
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showRequireAlert()  // 위치퍼미션 재요청
        }
        else
        {
            //위치 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

    }
}
```

<br>

#### **4. Android 10.0 이상 버전 위치 퍼미션**
```kotlin
private fun checkPermission10() {
    val permissionCheckFore = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) // 포그라운드 위치 권한 확인
    val permissionCheckBack = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) // 백그라운드 위치 권한 확인

    if (permissionCheckFore != PackageManager.PERMISSION_GRANTED || permissionCheckBack != PackageManager.PERMISSION_GRANTED) { // 위치 권한 확인 :: 현재 위치 권한 없음

        // shouldShowRequestPermissionRationale :: 사용자가 거부했을 경우 true 반환
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION))
        {
            showRequireAlert()  // 위치퍼미션 재요청
        }
        else
        {
            //위치 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

    }
}
```

<br>

#### **5. 권한 거부 시 설정 화면으로 이동시켜주는 다이얼로그 호출**
```kotlin
private fun showRequireAlert() {
    if (!this.isFinishing) {
        AlertDialog.Builder(this)
            .setTitle(this.getString(R.string.location_permission_title))
            .setMessage(this.getString(R.string.location_permission_message))
            .setPositiveButton(
                this.getString(R.string.btn_setting)
            ) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", this.packageName, null)
                this.startActivity(intent)
                dialog.dismiss()
            }.show()
    }
}
```

<br>

***문제점 :: 사용자의 설정 여부와 관계없이 현재 권한이 설정되어 있지 않으면 바로 다이얼로그가 호출됨***