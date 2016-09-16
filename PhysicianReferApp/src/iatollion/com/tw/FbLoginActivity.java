package iatollion.com.tw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
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
import com.facebook.HttpMethod;
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
        userName = "";
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_fb_login);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = AccessToken.getCurrentAccessToken();
                Log.v("accessToken changed:","token changed");
            }
        };
        AppEventsLogger.activateApp(this);
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(FbLoginActivity.this, "FB login OK", Toast.LENGTH_SHORT).show();
                Log.i("FB current user", loginResult.getAccessToken().getUserId());
                Log.i("FB auth", loginResult.getAccessToken().getToken());
                requestFbUserName();
            }
            @Override
            public void onCancel() {
                Toast.makeText(FbLoginActivity.this, "FB cancel", Toast.LENGTH_SHORT).show();
                userName = "";
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(FbLoginActivity.this, "FB login failed", Toast.LENGTH_SHORT).show();
            }
        });
        Button confirmLoginButton = (Button)findViewById(R.id.confirm_login_button);
        confirmLoginButton.setOnClickListener(confirmLoginClick);

        //Get current FB login status
        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null){
            Log.v("FBLogin OnCreate", "accessToken is not null.");
//            requestFbUserName();
        }
        else{
            Log.v("FBLogin OnCreate", "accessToken is null.");
        }
        fbLoginProcess(accessToken);
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
            fbLoginProcess(accessToken);
        }
    };
    private void fbLoginProcess(AccessToken token){
        if(token != null){
            Log.v("fbLoginProcess", "accessToken is not null.");
            GraphRequest request = GraphRequest.newMeRequest(
                    token,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                userName = object.getString("name");
                                Log.i("FB request completed","username: " + userName);
                                if (!userName.equals("")) {
                                    Log.v("ready to login", "userName is not null.");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
                                    builder.setTitle(R.string.fb_login)
                                            .setMessage("使用 " + userName + " 登入?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {                        // TODO
                                                    Log.v("fbLoginProcess", "Ok clicked.");
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.v("fbLoginProcess", "Cancel clicked.");
                                                    LoginManager.getInstance().logOut();
                                                    userName = "";
                                                    disconnectFromFacebook();
                                                }
                                            });
                                    builder.create();
                                    builder.show();
                                    builder = null;
                                }
                                else{
                                    Log.v("fbLoginProcess", "userName is null.");
                                }
                            }
                            catch(Exception e){
                                Log.e("FB request completed","Failed to get the specific filed data");
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
        }
        else{
            Log.v("fbLoginProcess", "accessToken is null.");
            LoginManager.getInstance().logInWithReadPermissions(FbLoginActivity.this, Arrays.asList("public_profile", "email"));
        }
    }
    private void requestFbUserName(){
        Log.v("requestFbUserName", "Start");
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            Log.i("FB request completed","username: " + object.getString("name"));
                        }
                        catch(Exception e){
                            Log.e("FB request completed","Failed to get the specific filed data");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }
}
