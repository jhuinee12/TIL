#  xml에서 tools 란?
> 최초작성 : 2022.04.05

```xml
<ListView
    android:id="@+id/listView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintHorizontal_bias="0.0"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    tools: visibility="visible" />
```

안드로이드에서 xml을 작성하다 보면 android, app, tools를 만나게 됩니다.

처음엔 그냥 안드로이드에서 추천해주는 코드대로 쓰다보니 저들의 관계에 대해 생각해보지 않다가 우연히 tools 용도에 대해 알게되어 작성해보려고 합니다.ㅇㅁ

1\. 실제 소스상에서는 나타나지 않지만, Preview에서는 보고 싶을 때 사용

2\. text, visibility, listitem 등이 있음

3\. 참고링크 : [https://developer.android.com/studio/write/tool-attributes](https://developer.android.com/studio/write/tool-attributes)