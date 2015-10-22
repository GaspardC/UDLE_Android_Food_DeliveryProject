package ch.epfl.sweng.udle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    private LoginButton loginButton;
    private TextView info;
    private static ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;
    private String user = "";
    private static String id = "";
    private static String name= "";
    private ProfileTracker mProfileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            info.setText("Hi again " + profile.getFirstName() + "");
            profilePictureView.setProfileId(profile.getId());
        }



        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                /* Use for the frist connection */
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                       if(profile2!=null){
                           Log.v("facebook - profile", profile2.getFirstName());
                           mProfileTracker.stopTracking();

                           AccessToken accessToken = loginResult.getAccessToken();

                           // Get User Name
                           info.setText("Hello "+profile2.getFirstName() + "");
                           id = profile2.getId();
                           name = profile2.getName();
                           profilePictureView.setProfileId(id);
                       }

                    }
                };
                mProfileTracker.startTracking();


                /*Use for 2nd and next connections*/
                Profile profile = Profile.getCurrentProfile();
                if(profile == null){
                    return ;
                }
                Log.d("Success", "" + profile);

                // Get User Name
                info.setText("Hello "+profile.getFirstName() + "");
                id = profile.getId();
                name = profile.getName();
                profilePictureView.setProfileId(id);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /** Called when the user clicks the MapActivity button */
    public void goToMapActivity(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);


    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        profileTracker.stopTracking();
//    }
}
