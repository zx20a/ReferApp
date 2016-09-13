package iatollion.com.tw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import iatollion.com.WebApi.ApiConnect;
import iatollion.com.framework.BaseActivity;

public class RegisterUserActivity extends BaseActivity {
	
	private static final String TAG = RegisterUserActivity.class.getSimpleName();
	private View regView;
	private final Activity _activity = this;
	private final String apiUrl = "http://220.133.185.190:8889";
	private String gender;

	RadioButton manRadio;
	RadioButton womanRadio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		regView = LayoutInflater.from(this).inflate(R.layout.register_user_layout, null);
		setContentView(regView);
		initViewObject();
	}

	protected void initViewObject() {
		Button registerBtn = (Button)findViewById(R.id.createUser_Ok_Button);
		Button cancelRegisterBtn = (Button)findViewById(R.id.createUser_Cancel_Button);

		manRadio = (RadioButton)regView.findViewById(R.id.manRadioButton);
		womanRadio = (RadioButton)regView.findViewById(R.id.womanRadioButton);

		manRadio.setChecked(true);
		womanRadio.setChecked(false);
		gender = "1";

		RadioGroup radioGroup = (RadioGroup)regView.findViewById(R.id.genderRadioGroup);

		registerBtn.setOnClickListener(registerClick);
		cancelRegisterBtn.setOnClickListener(registerCancel);
		radioGroup.setOnCheckedChangeListener(radioButtonClick);
	}

	private Button.OnClickListener registerClick = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.v("Button Click", "Register");
			EditText name = (EditText)regView.findViewById(R.id.userNameText);
			EditText account = (EditText)regView.findViewById(R.id.eAccountEditText);
			EditText passwd = (EditText)regView.findViewById(R.id.passwordEditText);
			EditText phone = (EditText)regView.findViewById(R.id.telephoneEditText);
			EditText birthday = (EditText)regView.findViewById(R.id.birthdayEditText);

				Log.v("Logging","Account format valid");
				JSONObject params = new JSONObject();
				try {
					params.put("URL", apiUrl + "/registeredApi");
					params.put("METHOD", "POST");
					params.put("AccountId", account.getText().toString());
					params.put("AliasName", name.getText().toString());
					params.put("Sex", gender);
					params.put("Birthday", birthday.getText().toString());
					params.put("Phone", phone.getText().toString());
					params.put("Email", account.getText().toString());
					params.put("Password", passwd.getText().toString());
					new ApiConnect(_activity).execute(params);
				}
				catch(Exception e){
					Log.e("ERROR", e.getMessage(), e);
				}

		}
	};

	private Button.OnClickListener registerCancel = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.v("Button Click", "Register canceled");
		}
	};

	private RadioGroup.OnCheckedChangeListener radioButtonClick = new RadioGroup.OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int p = group.indexOfChild(findViewById(checkedId));
			int count = group.getChildCount();
			switch (checkedId) {
				case R.id.manRadioButton:
					Log.v("RadioButton", "man");
					gender = "1";
					break;
				case R.id.womanRadioButton:
					Log.v("RadioButton", "woman");
					gender = "2";
					break;
			}
		}
	};
	
	
}
