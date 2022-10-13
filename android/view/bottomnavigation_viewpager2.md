# BottomNavigationView와 ViewPager2를 이용한 화면 구축
> 최초작성 : 2021.05.08

![](./image/bottomnav-viewpager-complete.gif)

**1\. BottomNavigationView와 ViewPager2를 activity\_main.xml에 배치**

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!--ViewPager : bottom_nav 위에 위치-->
    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/vp_Main_ViewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@+id/nav_Main_BottomNav"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/nav_Main_BottomNav"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:background="#FFFFFF"
        app:itemIconSize="45dp"
        app:labelVisibilityMode="unlabeled"/>

</LinearLayout>
```

\* android:layout\_above="@+id/nav\_Main\_BottomNav"를 통해 viewPager2가 bottomNavigationView 위에 위치

**2\. 위 ViewPager2와 BottomNavigation을 사용하기 위해서는 build.gradle 의존 추가**

```
// 메테리얼 디자인 : BottomNavigation 사용
implementation 'com.google.android.material:material:1.1.0'
// 뷰페이저2
implementation 'androidx.viewpager2:viewpager2:1.0.0'
```

**3\. res 폴더에 menu 폴더를 만들어 BottomNavigation 메뉴 정의**

**_\* Android Resource Directory에서 menu 선택_**

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/menu_home"
        android:title="홈"
        android:icon="@drawable/ic_home"
        />
    <item
        android:id="@+id/menu_chat"
        android:title="채팅"
        android:icon="@drawable/ic_chat"
        />
    <item
        android:id="@+id/menu_myPage"
        android:title="마이페이지"
        android:icon="@drawable/ic_mypage"
        />
</menu>
```

\* icon은 직접 drawble 폴더에 넣어도 되고, 안드로이드 스튜디오 아이콘을 사용해도 됨

3-1) drawble 폴더 오른쪽 버튼 > New > Vector Asset > 아이콘 선택 후 Name 변경 저장

**4\. activity\_main.xml 내 bottomNavigation과 menu 연결 위해 app:menu 코드 추가**

```xml
    <com.google.android.material.bottomnavigation.BottomNavigationView
        app:menu="@menu/menu_main_bottomnav"
        android:id="@+id/nav_Main_BottomNav"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:background="#FFFFFF"
        app:itemIconSize="45dp"
        app:labelVisibilityMode="unlabeled"/>
```

**5\. 홈, 채팅, 마이페이지 화면을 구현할 Fragment 생성**

```kt
class Main_HomeFragment : Fragment() {
    // 외부에서 접근하여 Main_HomeFragment() 메모리를 가져오기 위한 함수
    companion object {
        fun newInstance() : Main_HomeFragment {
            return Main_HomeFragment()
        }
    }

    // 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragemnt - onCreat() called")
    }

    // 뷰가 생성되었을 때 : 프래그먼트와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "HomeFragemnt - onCreatView() called")
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        return view
    }
}
```

**6\. Fragment 배열 생성 후 FragmentStateAdapter에 연결**

```kotlin
private val homeFragment by lazy { Main_HomeFragment() }
private val chatFragment by lazy { Main_ChatFragment() }
private val myPageFragment by lazy { Main_MypageFragment() }

// 위에서 선언한 fragment를 담는 리스트
private val fragments: List<Fragment> = listOf(
	homeFragment, chatFragment, myPageFragment
)

private val pagerAdapter: MainViewPagerAdapter by lazy {
	// fragment 리스트인 fragments를 MainViewPagerAdapter에 연결
	MainViewPagerAdapter(this, fragments)
}
```

```
// fragment 어댑터
class MainViewPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
```

**7.  BottomNavigation에서 선택된 아이템과 ViewPager의 페이지를 맞춰주기**

```kotlin
// BottomNavigation이 선택되었을 때 실행
nav_Main_BottomNav.run {
    setOnNavigationItemSelectedListener {
        // 선택된 menu_item과 페이지 번호 맞추기
        val page = when (it.itemId) {   // it: 현재 선택된 메뉴 item
            R.id.menu_home -> 0     // it이 menu_home일 때 페이지 번호는 0번
            R.id.menu_chat -> 1     // it이 menu_chat일 때 페이지 번호는 1번
            R.id.menu_myPage -> 2   // it이 menu_myPage일 때 페이지 번호는 2번
            else -> 0               // 기본 0번 (menu_home)
        }

        // 위 when문에 따라 page가 맞지 않을 경우 맞춰주기
        if (page != vp_Main_ViewPager.currentItem) {
            vp_Main_ViewPager.currentItem = page
        }
        true
    }
    selectedItemId = R.id.menu_home
}

// ViewPager2가 변경되었을 때 실행
vp_Main_ViewPager.run {
    adapter = pagerAdapter
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            // 선택된 menu_item과 페이지 번호 맞추기
            val navigation = when (position) {
                0 -> R.id.menu_home     // 페이지 번호가 0번일 때, bottom item은 menu_home
                1 -> R.id.menu_chat     // 페이지 번호가 1번일 때, bottom item은 menu_chat
                2 -> R.id.menu_myPage   // 페이지 번호가 2번일 때, bottom item은 menu_myPage
                else -> R.id.menu_home  // 기본 bottom item은 menu_home
            }

            // 위 when문에 따라 bottom item이 맞지 않을 경우 맞춰주기
            if (nav_Main_BottomNav.selectedItemId != navigation) {
                nav_Main_BottomNav.selectedItemId = navigation
            }
        }
    })
}
```

\* MainActivity.java 내 onCreate()에서 실행

\* 아무것도 선택되지 않았을 때(즉 default 값)는 else를 이용해 Main\_HomeFragment() 선택