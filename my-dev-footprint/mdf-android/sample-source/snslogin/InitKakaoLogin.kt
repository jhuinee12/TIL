/**
  * 카카오 버튼 클릭
  */
binding.buttonKakao.setOnClickListener {
	Session.getCurrentSession().close()	// 세션 날려버림
	binding.comKakaoLogin.performClick()
}

/**
  * ISessionCallback : 세션의 상태 변경에 따른 콜백, 세션이 오픈되었을 때, 세션이 만료되어 닫혔을 때 세션 콜백을 넘김
  */
fun initKakaoLogin() {
	val callback = object : ISessionCallback {
		// 네트워크 등으로 로그인 실패
		override fun onSessionOpenFailed(exception: KakaoException?) {
			if (exception != null) {
				showToast(getString(R.string.error_message_3rd_parth_authentication, exception.message))
			}
		}

		// access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태
		override fun onSessionOpened() {
			kakaoSessionOpened()
		}
	}
	// getCurrentSession() 메서드는 트랜잭션이 종료되기 전까지 동일한 Session 객체를 리턴
	Session.getCurrentSession().addCallback(callback)
}

private fun kakaoSessionOpened() {
	requestAuthentication(
		Session.getCurrentSession().tokenInfo.accessToken ?: "",
		SharedKey.LOGIN_TYPE_KAKAO
	)
}

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
  * 카카오 자동 로그인
  */
fun initKakaoLogin() {
	Log.i(SIGNIN_TAG, "initKakaoLogin")
	kakaoCallback = object : ISessionCallback {
		override fun onSessionOpenFailed(exception: KakaoException?) {
			Log.i(SIGNIN_TAG, "kakao onSessionOpenFailed")
			if (exception != null) {
				Log.e("", exception.toString())
			}
			timesAfterLaunch(1, Runnable {
				startLoginActivity(false)
			})
		}

		override fun onSessionOpened() {
			Log.i(SIGNIN_TAG, "kakao onSessionOpened")
			requestAuthentication(
				Session.getCurrentSession().tokenInfo.accessToken ?: "",
				SharedKey.LOGIN_TYPE_KAKAO
			)
		}

	}
	Session.getCurrentSession().addCallback(kakaoCallback)
	Log.i(SIGNIN_TAG, "kakao current session isClosed = ${Session.getCurrentSession().isClosed}")
	if (!Session.getCurrentSession().isClosed) {
		Session.getCurrentSession().checkAndImplicitOpen()
	} else {
		timesAfterLaunch(1, Runnable {
			startLoginActivity(false)
		})
	}
}