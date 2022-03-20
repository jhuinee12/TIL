/**
  * 라인 버튼 클릭
  * LINE SDK에 내장 된 로그인 버튼 사용!!
  */
binding.buttonLine.setOnClickListener {
	// performClick : 해당 View를 Click한 것과 같은 효과
	// Call this view's OnClickListener, if it is defined.
	binding.comLineLogin.performClick()
}


// 라인 로그인
binding.comLineLogin.setFragment(this)	// 버튼이 fragment 안에 있으면 호출해야함.
binding.comLineLogin.setChannelId("1605200884")
binding.comLineLogin.enableLineAppAuthentication(true)	// 로그인을 라인 앱에서 수행하는지, 아니면 웹뷰에서 수행하는지 여부 구성
binding.comLineLogin.setAuthenticationParams(	// 필요 범위 및 비순서 설정
	LineAuthenticationParams.Builder()
		.scopes(Arrays.asList(Scope.PROFILE))
		.build()
)
binding.comLineLogin.setLoginDelegate(mLineLoginDelegate)
binding.comLineLogin.addLoginListener(object : LoginListener {
	override fun onLoginSuccess(result: LineLoginResult) {
		val authCode: String = result.lineCredential?.accessToken?.tokenString ?: ""
		requestAuthentication(authCode, SharedKey.LOGIN_TYPE_LINE)
	}

	override fun onLoginFailure(result: LineLoginResult?) {
		showToast(getString(R.string.error_message_3rd_parth_authentication, result?.responseCode))
	}
})



@SuppressLint("HardwareIds")
fun requestAuthentication(authCode: String, joinType: String) {
	val pushToken: String = SharedPrefUtil.getString(activity!!, SharedKey.PUSH_TOKEN, "")!!
	val fromMemberOauthId: String = SharedPrefUtil.getString(activity!!, SharedKey.FROM_MEMBER_OAUTH_ID, "")!!
	SharedPrefUtil.put(activity!!, SharedKey.FROM_MEMBER_OAUTH_ID, "")
	val androidId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
	RetrofitManager().create(UserApi::class.java)
		.authorize(Authorize(authCode, joinType, pushToken, fromMemberOauthId, androidId))
		.enqueue(object : Callback<User> {
			override fun onResponse(call: Call<User>, response: Response<User>) {
				val user = response.body()
				if (response.isSuccessful && user != null && context != null) {
					SharedPrefUtil.put(context!!, SharedKey.LOGIN_TYPE, user.provider)
					SharedPrefUtil.put(context!!, SharedKey.ACCESS_TOKEN, user.accessToken)
					MainApplication.instance.user = user
					if (user.agreement) {
						SharedPrefUtil.put(context!!, SharedKey.ACCESS_TIME_LASTEST, DateUtil.toTimeStamp(ZonedDateTime.now()))
						listener?.startMain()
					} else {
						listener?.requestJoinFragment()
					}
				} else {
					showToast(response.message())
				}
			}

			override fun onFailure(call: Call<User>, t: Throwable) {
				showToast(R.string.error_message_api_common)
			}

		})
}


/**
  * 라인 자동 로그인
  */
SharedKey.LOGIN_TYPE_LINE -> {
    Single
        .fromCallable(object : Callable<Boolean> {
            override fun call(): Boolean {
                val lineApiClient =
                    LineApiClientBuilder(this@SplashActivity, "1605200884").build()
                if(lineApiClient.currentAccessToken.isSuccess){
                    requestAuthentication(
                        lineApiClient.currentAccessToken.responseData.tokenString,
                        SharedKey.LOGIN_TYPE_LINE
                    )
                }else{
                    timesAfterLaunch(1, Runnable {
                        startLoginActivity(false)
                    })
                }
                return lineApiClient.currentAccessToken.isSuccess
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe()
}