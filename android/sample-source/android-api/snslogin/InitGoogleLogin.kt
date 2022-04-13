/**
  * 구글 버튼 클릭
  */
binding.buttonGoogle.setOnClickListener {
	// 구글 로그인 버튼 선택 시 계정 정보가 intent
	val signInIntent = mGoogleSignInClient?.getSignInIntent()
	startActivityForResult(signInIntent, RequestCodes.GOOGLE_LOGIN.ordinal)
	
	// Acitivy 화면 전환 애니메이션 : 새로운 액티비티 열 때
	// (fadeout, fadein)
	// 0을 하면 가려지는 액티비티가 검은 화면이 됨
	// 닫을 때 : overridePendingTransition(fadein, fadeout)
	activity!!.overridePendingTransition(0, 0)
}

/**
  * GoogleSignInOptions 객체를 생성하고 requestServerAuthCode 호출
  */
private fun initGoogleLogin() {
	val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
		.requestServerAuthCode(getString(R.string.server_client_id))
		.requestEmail()
		.build()
	mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)

	updateUI(null)
}




override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	super.onActivityResult(requestCode, resultCode, data)
	if (requestCode == RequestCodes.GOOGLE_LOGIN.ordinal) { // 구글로 로그인 되어있으면
		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		handleSignInResult(task)
	} else if (mLineLoginDelegate.onActivityResult(requestCode, resultCode, data)) { // 라인으로 로그인 되어있으면
		return
	}
}

private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
	try {
		val account = task?.getResult(ApiException::class.java)
		updateUI(account)
	} catch (e: ApiException) {
		showToast(getString(R.string.error_message_3rd_parth_authentication, e.statusCode.toString()))
		updateUI(null)
	}

}

private fun updateUI(account: GoogleSignInAccount?) {
	if (account != null) {
		val authCode = account.serverAuthCode
		requestAuthentication(authCode ?: "", SharedKey.LOGIN_TYPE_GOOGLE)
	}
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
  * 구글 자동 로그인
  */
private fun initGoogleLogin() {
	Log.i(SIGNIN_TAG, "initGoogleLogin")
	val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
		.requestServerAuthCode(getString(R.string.server_client_id))
		.requestEmail()
		.build()
	val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

	try {
		mGoogleSignInClient!!.silentSignIn()
			.addOnCompleteListener(OnCompleteListener {
				Log.i(SIGNIN_TAG, "google silentSignIn: isSuccessful: $it.isSuccessful")
				if (it.isSuccessful) {
					val account = it.getResult(ApiException::class.java)
					requestAuthentication(
						account!!.serverAuthCode ?: "",
						SharedKey.LOGIN_TYPE_GOOGLE
					)
				} else {
					timesAfterLaunch(1, Runnable {
						startLoginActivity(false)
					})
				}
			})
	} catch (e: UserRecoverableAuthException) {
		Log.i(SIGNIN_TAG, "google silentSignIn: UserRecoverableAuthException")
		timesAfterLaunch(1, Runnable {
			startLoginActivity(false)
		})
	} catch (e: Exception) {
		Log.i(SIGNIN_TAG, "google silentSignIn: Exception")
		timesAfterLaunch(1, Runnable {
			startLoginActivity(false)
		})
	}
}