# Fragment에서 버튼을 생성하여 버튼 클릭 시 Activity로 intent하기
> 최초작성 : 2021.05.08

**1\. Fragment 내 intent를 실행시키기 위한 버튼 생성**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".fragment.Main_ChatFragment">

    <Button
        android:text="테스트1"
        android:textColor="@color/black"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/btn1"/>

</LinearLayout>
```

**2\. 버튼의 clicklistener 선언**

```kt
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.___, container, false)
        val go = Intent(context, 이동하고자 하는 액티비티명::class.java) // 인텐트를 생성
        view.btn1.setOnClickListener {
            startActivity(go)
        }
        return view
    }
```

\* view에서 버튼 클릭을 선언하기 위해서는 onCreateView 내에서 생성된 view를 가져와 view 안에 버튼 객체를 불러와야한다.