package iatollion.com.tw;

import iatollion.com.Dialog.Notice;
import iatollion.com.WebApi.ApiConnect;
import iatollion.com.framework.BaseActivity;
import iatollion.com.test.SkypeTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * @category main class about first display view
 * @author Wade.Chen
 * @version 1.0.0
 */

public class PhysicianActivity extends BaseActivity {
	
	private final String TAG = PhysicianActivity.class.getSimpleName();
	private View rootView;
	private final Activity _activity = this;
	final String apiUrl = "http://220.133.185.190:8889";

	/*
	 * (non-Javadoc)
	 * @see iatollion.com.framework.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rootView = LayoutInflater.from(this).inflate(R.layout.activity_physician_layout, null);

		setContentView(rootView);
		initViewObject();
	}

	protected void initViewObject() {
		Button loginBtn = (Button)findViewById(R.id.loginButton);
		loginBtn.setOnClickListener(loginClick);
		
		ImageView imgView = (ImageView)findViewById(R.id.fbAuthLink);
		imgView.setOnClickListener(fbAuthorizeClick);
		
		TextView registerLink = (TextView)findViewById(R.id.registerLink);
		registerLink.setOnClickListener(registerClick);
	}
	
	/**
	 * @category Button on click event
	 * @descript User account、password login check and authorization
	 */

	private Button.OnClickListener loginClick = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			EditText accountField = (EditText)rootView.findViewById(R.id.accountTextField);
			EditText passwdField = (EditText)rootView.findViewById(R.id.passwordTextField);

			Log.i(TAG, "[Account field content length] : " + accountField.getText().length());
			Log.i(TAG, "[Password field content length] : " + passwdField.getText().length());
			if(isAccountFormatValid(accountField.getText().toString(), passwdField.getText().toString())){
				Log.v("Logging","Account format valid");
				JSONObject params = new JSONObject();
				try {
					params.put("URL", apiUrl + "/login");
					params.put("METHOD", "POST");
					params.put("email", accountField.getText().toString()+'\0');
					params.put("password", passwdField.getText().toString()+'\0');
					new ApiConnect(_activity).execute(params);
				}
				catch(Exception e){
					Log.e("ERROR", e.getMessage(), e);
				}
			}
		}
	};
	
	/**
	 * @category FB ImageView on click event
	 * @descript fb authorization and register
	 */
	private ImageView.OnClickListener fbAuthorizeClick = new ImageView.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(view.getContext(), FbLoginActivity.class);
			startActivity(intent);
		}

	};

	
	/**
	 * @category register textview on click event
	 * @descript create user
	 */
	private TextView.OnClickListener registerClick = new TextView.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO
			Intent intent = new Intent();
			intent.setClass(view.getContext(), RegisterUserActivity.class);
			startActivity(intent);
		}
	};

	private boolean isAccountFormatValid(String email, String passwd){
		Pattern emailPattern = Pattern.compile("^([^@\\s]+)@([^@\\s]+)\\s?$");
		if(email.length() == 0 || passwd.length() == 0){
			Log.v("Login ","Empty email address");
			Notice.setHandler(rootView.getContext());
			Notice.popAuthWarningMessage();
			return false;
		}
		else {
			Matcher m = emailPattern.matcher(email);
			if(m.find()){
				Log.v("Login:user ", m.group());
				Log.v("Login:password ",passwd);
				return true;
			}
			else{
				Notice.setHandler(rootView.getContext());
				Notice.popAuthFormatWarningMessage();
				return false;
			}
		}
	}
	
}
