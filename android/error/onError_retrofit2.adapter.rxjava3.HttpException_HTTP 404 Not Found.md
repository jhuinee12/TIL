# onError: retrofit2.adapter.rxjava3.HttpException: HTTP 404 Not Found
> 최초작성 : 2022.05.17

RxJava3와 Retrofit2을 이용하여 로또 당첨 번호를 조회하는 도중 해당 오류를 만났다.

404 오류는 주소가 틀렸을 때 나타나는 오류이다.

### 1\. retrofit init은 다음과 같다.
```kt
init {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.dhlottery.co.kr/common.do/")
        .client(OkHttpClient())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())   // 받은 응답을 observable 형태로 변환
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    api = retrofit.create(LottoApi::class.java)
}
```

### 2\. get 주소는 다음과 같다.
```kt
@GET("common.do")
fun getLottoWinnerNumber(
    @Query("method") method: String = "getLottoNumber",
    @Query("drwNo") drwNo: Int,
): Observable<LotteryNumber>
```

멍청한 결과지만 common.do를 동시에 적어서 발생한 오류였다...