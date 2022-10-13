# 안드로이드 리사이클러뷰

---

## RecyclerView를 업데이트하는 이벤트
> 최초작성 : 2022.04.13

>>>>> [참고문서](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#notifyDataSetChanged())

### <데이터 변경 이벤트의 종류>
1. 항목 변경 : 단일 항목의 데이터가 업데이트 O , 위치 변경 발생 X
2. 구조적 변경 : 데이터 세트 내 항목이 삽입/제거/이동

### 01. notifyDataSetChanged()
: 데이터 세트가 변경되었음을 알리는 이벤트

**※ 최후 수단으로 이용되어야 함!!**
<br>→ 이 이벤트는 기존의 모든 항목과 구조가 더 이상 유효하지 않을 수 있다고 가정하여 완전히 다시 바인딩하고 릴레이함.

=> 따라서 가능하면 구체적인 변경 이벤트를 사용하는 것이 더 효율적

### 02. notifyItemChanged(int position, Object payload) : 항목 변경 이벤트
: position 아이템이 변경되었음을 알리는 이벤트

* postioin : (int)변경된 아이템의 위치
* payload : (Object)선택적 매개변수, null을 사용하여 "전체" 업데이트 식별

### 3. notifyItemChanged(int position) : 항목 변경 이벤트
: 데이터 세트가 변경되었음을 알리는 이벤트

* postioin : (int)변경된 아이템의 위치

### 4. notifyItemInserted(int position) : 구조 변경 이벤트
: position에 새로운 항목이 삽입되었음을 알리는 이벤트

데이터 세트에 있는 다른 기존 항목의 표현은 여전히 ​​최신 상태로 간주되며 해당 위치가 변경될 수 있지만 리바운드되지 않음

* postioin : (int)데이터 세트에서 새로 삽입된 항목의 위치

### 5. notifyItemRangeChanged (int positionStart, int itemCount) : 항목 변경 이벤트
: positionStart에서부터 itemCount만큼의 아이템이 변경되었음을 알리는 이벤트

* positionStart : (int)가장 먼저 변경된 항목의 위치
* itemCount : (int)변경된 항목 수

<br>

---

<br>

### 리사이클러뷰 내 Swipe 기능으로 리스트 새로고침
> 최초작성 : 2021.05.16

**1\. SwipeRefreshLayout을 사용하기 위해 implementation 해준다.**

```gradle
implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
```

**2\. 리사이클러뷰가 존재하는 xml에서 리사이클러뷰는 SwipeRefreshLayout으로 감싸준다.**

```xml
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
	android:id="@+id/SwipeRefresh"
	android:layout_width="match_parent"
	android:layout_height="match_parent">

	<androidx.recyclerview.widget.RecyclerView
		android:id="@+id/RecyclerView"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:background="#F0F0F0" />

</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

**3\. 스와이프뷰에 setOnRefreshListener를 통해 리스트를 갱신하는 기능을 넣어준다.**

```kt
view.Swipe_HomeF_SwipeRefresh.setOnRefreshListener {
	// 리사이클러뷰와 연결한 배열을 초기화하고 수정하는 소스를 추가해준다.
    
    // 배열이 변경되었다는 것을 리사이클러뷰에 알려준다
	RecyclerView.adapter?.notifyDataSetChanged()
    // 새로고침 아이콘을 멈춰준다. -> 넣어주지 않으면 무한로딩
	view.Swipe_HomeF_SwipeRefresh.isRefreshing = false
}
```

---

### RecyclerView 내에서 View Binding 사용하기
> 최초작성 : 2021.09.26

**01\. view binding을 사용할 수 있도록 환경 설정**

**_<build.gradle(Module:프로젝트파일명)>_**

```xml
android {
    // 기존 내용들 생략..
    buildFeatures {
        // 뷰 바인딩 활성화
        viewBinding true 
    }
}
```

**02\. Activity / Fragment에서 RecyclerView 설정**

_**<JAVA를 이용한 샘플 코드>**_

```java
binding.recyclerView.setLayoutManager(
	new LinearLayoutManager({this 혹은 context}, LinearLayoutManager.VERTICAL, false));
binding.recyclerView.setAdapter(new {Adapter명: MyAdapter}({리사이클러뷰에 넣을 리스트}));
binding.recyclerView.setHasFixedSize(true);
```

**_<Kotlin을 이용한 샘플 코드>_**

```kt
binding.recyclerView.layoutManager 
	= LinearLayoutManager({this 혹은 context}, LinearLayoutManager.VERTICAL, false)
binding.recyclerView.adapter = {Adapter명: MyAdapter}({리사이클러뷰에 넣을 리스트})
binding.recyclerView.setHasFixedSize(true)
```

**03\. Adapter  설정**

**_<JAVA를 이용한 샘플 코드>_**

```java
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.HomeHolder> {
    ArrayList<String> itemList;
    public MyAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        {리사이클러뷰 아이템 레이아웃명 + Binding} binding 
        	= {리사이클러뷰 아이템 레이아웃명 + Binding}
			.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new HomeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        // holder.binding.{아이템} 세팅
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder{
        {리사이클러뷰 아이템 레이아웃명 + Binding} binding;
        public HomeHolder({리사이클러뷰 아이템 레이아웃명 + Binding} b){
            super(b.getRoot());
            binding = b;
        }
    }
}
```

**_<Kotlin을 이용한 샘플 코드>_**

```kt
class MyAdapter(private val itemList: ArrayList<String>) : RecyclerView.Adapter<MyHolder>(){
	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyHolder {
		val binding = {리사이클러뷰 아이템 레이아웃명 + Binding}
			.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
		return MyHolder(binding)
	}

	override fun getItemCount(): Int {
		return itemList.size
	}

	override fun onBindViewHolder(holder: MyHolder, position: Int) {
		holder.binding.{아이템명} 세팅
	}

	class MyHolder(val binding: {리사이클러뷰 아이템 레이아웃명 + Binding}) : RecyclerView.ViewHolder(binding.root)
}
```

**\* 리사이클러뷰 아이템 레이아웃명 + Binding : 레이아웃이 layout\_list 이면 ListLayoutBinding으로 사용**