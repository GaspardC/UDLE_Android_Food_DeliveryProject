package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private TextView info;
    private static ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;
    private String user = "";
    private static String id = "";
    private static String name= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)findViewById(R.id.login_button2);
        info = (TextView)findViewById(R.id.info);
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);

        info.setText("Hello " + name + "");

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
            public void onSuccess(LoginResult loginResult) {
                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/
                Profile profile = Profile.getCurrentProfile();
                Log.d("Success", "" + profile);

                // Get User Name
                info.setText("Bonjour "+profile.getFirstName() + "");
                id = profile.getId();
                name = profile.getName();
                profilePictureView.setProfileId(id);



//                GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object,GraphResponse response) {
//                                try {
//                                    String  email=object.getString("email");
//                                    Log.d( "user email ", "email");
//                                } catch (JSONException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        });
//
//                request.executeAsync();


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

}
