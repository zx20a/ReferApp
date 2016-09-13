package iatollion.com.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import iatollion.com.tw.PhysicianActivity;

/**
 * Created by ypwang on 2016/9/11.
 */
public class SkypeTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Context ctx = getApplicationContext();
        super.onCreate(savedInstanceState);
        if(isSkypeClientInstalled(ctx)){
            Log.v("SKYPE ","installed");
            Toast.makeText(SkypeTest.this, "SKYPE INSTALLED", Toast.LENGTH_SHORT).show();

            // Create the Intent from our Skype URI.
            // https://msdn.microsoft.com/zh-tw/library/office/dn745877.aspx
            //"com.skype.raider" Open Skype App
            //"skype:username?chat"  //chatting
            //"skype:username?call"
            //"skype:username?call&video=true"　


            Uri skypeUri = Uri.parse("skype:live:dnfg3?chat");

            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

            // Restrict the Intent to being handled by the Skype for Android client only.
            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Initiate the Intent. It should never fail because you've already established the
            // presence of its handler (although there is an extremely minute window where that
            // handler can go away).
            ctx.startActivity(myIntent);
        }
        else{
            Toast.makeText(SkypeTest.this, "SKYPE NOT INSTALLED", Toast.LENGTH_SHORT).show();
            goToMarket(ctx);
            Log.v("SKYPE ","Not installed");
        }
    }
    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);
        return;
    }
    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }
}