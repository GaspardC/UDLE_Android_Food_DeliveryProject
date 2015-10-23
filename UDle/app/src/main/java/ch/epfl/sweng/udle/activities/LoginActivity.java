package ch.epfl.sweng.udle.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.udle.R;


public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private TextView info;
    private static ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;
    private String mail = "";
    private static String id = "";
    private static String name= "";
    private ProfileTracker mProfileTracker;
    private Context context = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplication();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "9owjl8GmUsbfyoKtXhd5hK7QX8CUJVfuAvSLNoaY", "xd6XKHd9NxLfzFPbHQ5xaMHVzU1gfeLen0qCyI4F");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        FacebookSdk.sdkInitialize(getApplicationContext());



        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_log_in);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
        info = (TextView)findViewById(R.id.info);


        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                if (newAccessToken==null){
                    profilePictureView.setProfileId(null);
                    info.setText("Welcome :)");
                }

            }
        };

        Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            setUserInformation(profile);
            info.setText("Hi again " + profile.getFirstName() + "");
            goToMapActivityIn(2000);

        }



        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                retrieveFacebookInfoFirstConnection(loginResult);
                retrieveFacebookInfoSecondAndAfterConnections();

                goToMapActivityIn(2000);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }

    private void goToMapActivityIn(int i) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Go to Map Activity after i ms
                Intent intent = new Intent(context, MapActivity.class);
                startActivity(intent);

            }
        }, i);
    }

    private void retrieveFacebookInfoSecondAndAfterConnections() {
         /*Use for 2nd and next connections*/
        Profile profile = Profile.getCurrentProfile();
        if(profile == null){
            return ;
        }
        Log.d("Success", "" + profile);
        setUserInformation(profile);

    }

    private void setUserInformation(Profile profile) {
        // Get User Name..
        info.setText("Hello " + profile.getFirstName() + "");
        id = profile.getId();
        name = profile.getName();
        profilePictureView.setProfileId(id);
        setUserMail();
    }

    private void setUserMail() {

        //             Use it to get the mail
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.d("response", "response" + object.toString());
                        try {
                            object.getJSONObject("picture").getJSONObject("data").getString("url");
                            mail = object.getString("email");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture.width(300)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void retrieveFacebookInfoFirstConnection(final LoginResult loginResult) {
         /* Use for the first connection */
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                if(profile2!=null){
                    mProfileTracker.stopTracking();
                    setUserInformation(profile2);
                }
            }
        };
        mProfileTracker.startTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mProfileTracker.stopTracking();
    }
}
