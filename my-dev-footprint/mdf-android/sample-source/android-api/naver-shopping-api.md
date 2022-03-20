# 네이버 쇼핑 API 파싱

* [XML 파싱](#xml-파싱)
* [JSON 파싱](#json-파싱)

- - -

## XML 파싱
> 최초작성 : 2021.02.03

**1\. 우선 API 등록 후 키 값을 이용해 주소 설정하기**

     └ 나의 경우 '식품안전나라' API를 사용함

**2\. 뽑아낼 데이터**

| **데이터명** | **변수명** | **데이터명** | **변수명** |
| --- | --- | --- | --- |
| 번호 | num | 식품군 | group\_name |
| 식품코드 | food\_cd | 식품이름 | desk\_kor |
| 지역명 | region\_name | 조사년도 | research\_year |
| 채취월 | month\_name | 조사명 | maker\_name |
| 지역코드 | region\_cd | 자료출처 | sub\_ref\_name |
| 채취월코드 | month\_cd | 총내용량 | serving\_size |

**3\. 파싱 데이터 get/set 클래스 생성 (XmlPullParser\_fsk.java)**

``` java
public class XmlPullParser_fsk {

    String num = "";		// 번호
    String food_cd = "";	// 식품코드
    String region_name = "";	 // 지역명
    String month_name = "";	 // 채취월
    String region_cd = "";	 // 지역코드
    String month_cd = "";	 // 채취월코드
    String group_name = "";	// 식품군
    String desk_kor = "";	// 식품이름
    String research_year = "";	// 조사년도
    String maker_name = "";	// 제조사명
    String sub_ref_name = "";	// 자료출처
    String serving_size = "";	// 총내용량

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFood_cd() {
        return food_cd;
    }

    public void setFood_cd(String food_cd) {
        this.food_cd = food_cd;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getRegion_cd() {
        return region_cd;
    }

    public void setRegion_cd(String region_cd) {
        this.region_cd = region_cd;
    }

    public String getMonth_cd() {
        return month_cd;
    }

    public void setMonth_cd(String month_cd) {
        this.month_cd = month_cd;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDesk_kor() {
        return desk_kor;
    }

    public void setDesk_kor(String desk_kor) {
        this.desk_kor = desk_kor;
    }

    public String getResearch_year() {
        return research_year;
    }

    public void setResearch_year(String research_year) {
        this.research_year = research_year;
    }

    public String getMaker_name() {
        return maker_name;
    }

    public void setMaker_name(String maker_name) {
        this.maker_name = maker_name;
    }

    public String getSub_ref_name() {
        return sub_ref_name;
    }

    public void setSub_ref_name(String sub_ref_name) {
        this.sub_ref_name = sub_ref_name;
    }

    public String getServing_size() {
        return serving_size;
    }

    public void setServing_size(String serving_size) {
        this.serving_size = serving_size;
    }

}
```

**4\. 각각 대치되는 텍스트뷰에 결과 확인위해 메인 java에서 변수명 선언**

```java
TextView tvFoodCd = (TextView)findViewById(R.id.tv_food_cd); //파싱된 결과확인!
TextView tvRegionName = (TextView)findViewById(R.id.tv_region_name); //파싱된 결과확인!
TextView tvMonthName = (TextView)findViewById(R.id.tv_month_name); //파싱된 결과확인!
TextView tvDeskKor = (TextView)findViewById(R.id.tv_desk_kor); //파싱된 결과확인!
TextView tvFoodCd = (TextView)findViewById(R.id.tv_food_cd); //파싱된 결과확인!
TextView tvRegionName = (TextView)findViewById(R.id.tv_region_name); //파싱된 결과확인!
TextView tvMonthName = (TextView)findViewById(R.id.tv_month_name); //파싱된 결과확인!
TextView tvDeskKor = (TextView)findViewById(R.id.tv_desk_kor); //파싱된 결과확인!

boolean inNum = false, inFoodCd = false, inRegionName = false, inMonthName = false, inRegionCd = false;
boolean inMonthCd = false, inGroupName = false, inDeskKor = false, inResearchYear = false, inMakerName = false;
boolean inSubRefName = false, inServingSize = false;
```

**5\. 메인자바에서 파싱 후 텍스트뷰에 setText**

```java
       try{
            // http://openapi.foodsafetykorea.go.kr/api/인증키/서비스명/요청파일타입/요청시작위치/요청종료위치
            URL url = new URL("http://openapi.foodsafetykorea.go.kr/api/40ca169d1ddf4764b029/I2790/xml/1/1"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            Log.d(TAG,"파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("NUM")){ //title 만나면 내용을 받을수 있게 하자
                            inNum = true;
                            Log.d(TAG,"num.");
                        }
                        if(parser.getName().equals("FOOD_CD")){ //address 만나면 내용을 받을수 있게 하자
                            inFoodCd = true;
                            Log.d(TAG,"food_cd.");
                        }
                        if(parser.getName().equals("SAMPLING_REGION_NAME")){ //mapx 만나면 내용을 받을수 있게 하자
                            inRegionName = true;
                        }
                        if(parser.getName().equals("SAMPLING_MONTH_NAME")){ //mapx 만나면 내용을 받을수 있게 하자
                            inMonthName = true;
                        }
                        if(parser.getName().equals("SAMPLING_REGION_CD")){ //mapy 만나면 내용을 받을수 있게 하자
                            inRegionCd = true;
                        }
                        if(parser.getName().equals("SAMPLING_MONTH_NAME")){ //mapy 만나면 내용을 받을수 있게 하자
                            inMonthCd = true;
                        }
                        if(parser.getName().equals("GROUP_NAME")){ //mapy 만나면 내용을 받을수 있게 하자
                            inGroupName = true;
                        }
                        if(parser.getName().equals("DESC_KOR")){ //mapy 만나면 내용을 받을수 있게 하자
                            inDeskKor = true;
                        }
                        if(parser.getName().equals("RESEARCH_YEAR")){ //mapy 만나면 내용을 받을수 있게 하자
                            inResearchYear = true;
                        }
                        if(parser.getName().equals("MAKER_NAME")){ //mapy 만나면 내용을 받을수 있게 하자
                            inMakerName = true;
                        }
                        if(parser.getName().equals("SUB_REF_NAME")){ //mapy 만나면 내용을 받을수 있게 하자
                            inSubRefName = true;
                        }
                        if(parser.getName().equals("SERVING_SIZE")){ //mapy 만나면 내용을 받을수 있게 하자
                            inServingSize = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inNum){ //isTitle이 true일 때 태그의 내용을 저장.
                            fsk.num = parser.getText();
                            inNum = false;
                        }
                        if(inFoodCd){ //isAddress이 true일 때 태그의 내용을 저장.
                            fsk.food_cd = parser.getText();
                            inFoodCd = false;
                        }
                        if(inRegionName){ //isMapx이 true일 때 태그의 내용을 저장.
                            fsk.region_name = parser.getText();
                            inRegionName = false;
                        }
                        if(inMonthName){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.month_name = parser.getText();
                            inMonthName = false;
                        }
                        if(inRegionCd){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.region_cd = parser.getText();
                            inRegionCd = false;
                        }
                        if(inMonthCd){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.month_cd = parser.getText();
                            inMonthCd = false;
                        }
                        if(inGroupName){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.group_name = parser.getText();
                            inGroupName = false;
                        }
                        if(inDeskKor){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.desk_kor = parser.getText();
                            inDeskKor = false;
                        }
                        if(inResearchYear){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.research_year = parser.getText();
                            inResearchYear = false;
                        }
                        if(inMakerName){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.maker_name = parser.getText();
                            inMakerName = false;
                        }
                        if(inSubRefName){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.sub_ref_name = parser.getText();
                            inSubRefName = false;
                        }
                        if(inServingSize){ //isMapy이 true일 때 태그의 내용을 저장.
                            fsk.serving_size = parser.getText();
                            inServingSize = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("NUM")){
                            status1.setText("번호 : "+ fsk.num +"\n 식품코드: "+ fsk.food_cd +"\n 지역명 : " + fsk.region_name
                                    +"\n 채취월 : " + fsk.month_name +  "\n 지역코드 : " + fsk.region_cd + "\n 채취월코드 : " + fsk.month_cd
                                    +"\n 식품군 : " + fsk.group_name  + "\n 식품이름 : " + fsk.desk_kor + "\n 조사년도 : " + fsk.research_year
                                    +"\n 제조사명 : " + fsk.maker_name  +"\n 자료출처 : " + fsk.sub_ref_name +"\n 총내용량 : "+ fsk.serving_size +"\n");
                            status1.setText("번호 : "+ fsk.num +"\n 식품코드: "+ fsk.food_cd +"\n 지역명 : " + fsk.region_name
                                    +"\n 채취월 : " + fsk.month_name +  "\n 지역코드 : " + fsk.region_cd + "\n 채취월코드 : " + fsk.month_cd
                                    +"\n 식품군 : " + fsk.group_name  + "\n 식품이름 : " + fsk.desk_kor + "\n 조사년도 : " + fsk.research_year
                                    +"\n 제조사명 : " + fsk.maker_name  +"\n 자료출처 : " + fsk.sub_ref_name +"\n 총내용량 : "+ fsk.serving_size +"\n");
                            tvFoodCd.setText(fsk.food_cd);
                            tvRegionName.setText(fsk.region_name);
                            tvMonthName.setText(fsk.month_name);
                            tvDeskKor.setText(fsk.desk_kor);
                            inNum = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            Log.i(TAG, "에러");
            *//*tvFoodCd.setText("에러가..났습니다...");
            tvFoodCd.setText("에러가..났습니다...");
        }
}
```

**6\. 로그에서 테스트**

```log
Log.i(TAG, fsk.num +"\n 식품코드: "+ fsk.food_cd +"\n 지역명 : " + fsk.region_name
	+"\n 채취월 : " + fsk.month_name +  "\n 지역코드 : " + fsk.region_cd + "\n 채취월코드 : " + fsk.month_cd
	+"\n 식품군 : " + fsk.group_name  + "\n 식품이름 : " + fsk.desk_kor + "\n 조사년도 : " + fsk.research_year
	+"\n 제조사명 : " + fsk.maker_name  +"\n 자료출처 : " + fsk.sub_ref_name +"\n 총내용량 : "+ fsk.serving_size +"\n");
```

- - -

## JSON 파싱
> 최초작성 : 2021.03.11

![구현 완성 이미지](./image/naver-shop-json-complete.gif)

#### **1\. 메인액티비티(cat/dog 선택화면)에서 두개의 리스트 생성**

```
public class MainActivity extends AppCompatActivity {

    static public ArrayList<list_item> DataList;
    static public ArrayList<list_item> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mContext = this;

        DataList = new ArrayList<list_item>();
        searchList = new ArrayList<list_item>();
    }
}
```

#### **2\. 위 리스트뷰를 사용할 java 클래스에 import**

```
import static com.example.feedproject.MainActivity.DataList;
import static com.example.feedproject.MainActivity.searchList;
```

#### **3\. Api파싱 java 클래스 생성 (아래 링크 참고)**

[developers.naver.com](https://developers.naver.com/docs/search/blog/)

``` java
private static void parseData(String responseBody) {
    String image, title, maker, brand, desc, category1, category2, category3, category4;
    JSONObject jsonObject = null;
    int i=0;
    try {
        jsonObject = new JSONObject(responseBody.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("items");

        while (i != 10) {
            JSONObject item = jsonArray.getJSONObject(i);
            image = item.optString("image");
            brand = item.optString("brand");
            category1 = item.optString("category1");
            category2 = item.optString("category2");
            category3 = item.optString("category3");
            category4 = item.optString("category4");
            final String finalImage = image;
            final String finalTitle = brand;
            final String finalDesc = category1 + " > " + category2 + " > " + category3 + " > " + category4;
            final Boolean[] check = {true};

            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataList.add(new list_item(finalImage, finalTitle, finalDesc));
                    searchList.add(new list_item(finalImage, finalTitle, finalDesc));
                }
            }, 0);
            i++;
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }
```

\- 10개씩 리스트뷰에 쌓는 방식!!

\- 네이버 api는 최대 100개까지 호출 가능 -> display=10으로 해놓고 리스트뷰에 계속 쌓아가는 방식으로 무한대 호출

\- Handler를 감싸주지 않으면 오류

#### **3\. DogMainActivity.java**

```
public void ListViewLoad() {    // 페이지를 처음 갱신할 경우 실행
    Log.d(TAG, "리스트뷰 뿌릴게요");
    Handler mHandler = new Handler(Looper.getMainLooper());
    mHandler.postDelayed(new Runnable() {
        public void run(){
            adapter = new ListViewAdapter(mContext, DataList);
            listview.setAdapter(adapter);
        }
    },0);
    Log.d(TAG, "리스트뷰 뿌렸어요");

    listViewClick();
    listViewScroll();
}

public void ListViewUpdate()    // 페이지가 1 이상일 경우 실행
{
    listViewClick();
    listViewScroll();
    adapter.notifyDataSetChanged();
}
```

\- Handler를 감싸주지 않으면 오류

#### **4\. 10개씩 호출한 API를 리스트뷰에 호출하면서 스크롤 기능하기**

\- DogMainActivity.java

```
public void listViewScroll() {
    ApiExamSearchShop.start += 10;
    listview.setOnScrollListener(new AbsListView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && !mLockListView) {
                progressBar.setVisibility(View.VISIBLE);
                mLockListView = true;
                page++;
                try {
                    Thread td = new Thread() {
                        public void run() {
                            ApiExamSearchShop api = new ApiExamSearchShop();
                            api.main(page, keyword);
                        }
                    };
                    td.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        mLockListView = false;
                    }
                },1000);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        }
    });
}
```

\- activity\_dog\_main.xml

```
<ProgressBar
    android:id="@+id/progressbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true"
    android:indeterminateTint="@color/colorAccent" />

<ListView
    android:id="@+id/listView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_above="@+id/progressbar"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintHorizontal_bias="0.0"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"/>
```