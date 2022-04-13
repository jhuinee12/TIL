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