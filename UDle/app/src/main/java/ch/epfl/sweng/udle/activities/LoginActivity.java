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
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.ParseUserInformations;


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
    private  List<String> permissions;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private boolean stopTracking = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplication();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null && stopTracking == false){
                    //User logged out
                    profilePictureView.setProfileId(null);
                    info.setText("Welcome :)");}

            }
        };

        setContentView(R.layout.activity_log_in);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
        info = (TextView)findViewById(R.id.info);

        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        permissions = new ArrayList<>();
        permissions = Arrays.asList("user_friends","public_profile", "email");


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                stopTracking = true;
                boolean b =   retrieveFacebookInfoSecondAndAfterConnections();
                if (b == false){
                    retrieveFacebookInfoFirstConnection(loginResult);

                }
                goToMapActivityIn(1500);
            }

            @Override
            public void onCancel() {

                Log.d("faceLogin","cancel");
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("faceLogin","error");
                info.setText("Login attempt failed.");
            }
        });

        parseStuf();
        ParseUserInformations testNewUser = new ParseUserInformations();
        testNewUser.createNewUserWithoutFb("UserTest","0000","test@mail.com","0607080910");

    }

    private void parseStuf() {

//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9owjl8GmUsbfyoKtXhd5hK7QX8CUJVfuAvSLNoaY", "xd6XKHd9NxLfzFPbHQ5xaMHVzU1gfeLen0qCyI4F");


        ParseFacebookUtils.initialize(getApplicationContext());
//        ParseFacebookUtils.initialize(getApplicationContext(), 100)


        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    ParseUserInformations userInf = new ParseUserInformations();
                    userInf.fetcUserInfomation();

                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
//                    ParseUserInformations userInf = new ParseUserInformations();
//                    userInf.fetcUserInfomation();
                }
            }
        });



    }



    private void goToMapActivityIn(int i) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Go to Map Activity after i ms
                stopTracking = false;
                Intent intent = new Intent(context, MapActivity.class);
                startActivity(intent);
            }
        }, i);
    }

    private boolean retrieveFacebookInfoSecondAndAfterConnections() {
         /*Use for 2nd and next connections*/
        Profile profile = Profile.getCurrentProfile();
        if(profile == null){
            return false ;
        }
        Log.d("Success", "" + profile);
        setUserInformation(profile);
        accessTokenTracker.stopTracking();
        return true;

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
                else{
                    profilePictureView.setProfileId(null);
                    info.setText("Welcome :)");

                }
            }
        };
        mProfileTracker.startTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);


    }


    @Override
    public void onResume(){
        super.onResume();
        if(mProfileTracker != null){
            mProfileTracker.startTracking();
        }
        if(accessTokenTracker != null){
            accessTokenTracker.startTracking();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mProfileTracker.stopTracking();
    }
}
