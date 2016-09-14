package iatollion.com.tw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;


public class FbLoginActivity extends Activity {
    private  CallbackManager callbackManager;
    private  AccessTokenTracker accessTokenTracker;
    private  AccessToken accessToken;
    private String userName;
    final Activity _activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_fb_login);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        AppEventsLogger.activateApp(this);
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(FbLoginActivity.this, "FB login OK", Toast.LENGTH_SHORT).show();
                Log.i("FB current user", loginResult.getAccessToken().getUserId());
                Log.i("FB auth", loginResult.getAccessToken().getToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(FbLoginActivity.this, "FB cancel", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(FbLoginActivity.this, "FB login failed", Toast.LENGTH_SHORT).show();
            }
        });

        //Get current FB login status
        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null){
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            try {
                                userName = object.getString("name");
                                Log.i("FB current user name", userName);
                            }
                            catch(Exception e){
                                Log.e("FB current status","Failed to get the specific filed data");
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
        }

        Button confirmLoginButton = (Button)findViewById(R.id.confirm_login_button);
        confirmLoginButton.setOnClickListener(confirmLoginClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    private Button.OnClickListener confirmLoginClick = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if (!userName.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
                builder.setTitle(R.string.fb_login)
                        .setMessage("使用 " + userName + " 登入?")
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {                        // TODO

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginManager.getInstance().logOut();
                                LoginManager.getInstance().logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("public_profile", "email"));
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
                builder = null;
            }
            else{
                LoginManager.getInstance().logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("public_profile", "email"));
            }
        }
    };
}
