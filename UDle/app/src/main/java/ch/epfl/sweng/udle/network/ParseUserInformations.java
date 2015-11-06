package ch.epfl.sweng.udle.network;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gasp on 26/10/15.
 */
public class ParseUserInformations {

    String name = "";
    String email = "";
    String gender = "";
    long id = 0;

    public void ParseUserInformations(){
//        fetcUserInfomation();
    }

    public void fetcUserInfomation(){

        //Fetch Facebook user info if it is logged
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && currentUser.isAuthenticated()) {
            makeMeRequest();
        }
    }

    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();

                            try {
                                userProfile.put("facebookId", jsonObject.getLong("id"));
                                ParseUserInformations.this.id = jsonObject.getLong("id");
                                userProfile.put("name", jsonObject.getString("name"));
                                ParseUserInformations.this.name = jsonObject.getString("name");


                                if (jsonObject.getString("gender") != null) {
                                    userProfile.put("gender", jsonObject.getString("gender"));
                                    ParseUserInformations.this.gender = jsonObject.getString("gender");
                                }


                                if (jsonObject.getString("email") != null){
                                    userProfile.put("email", jsonObject.getString("email"));
                                    ParseUserInformations.this.email = jsonObject.getString("email");
                                    ParseUser.getCurrentUser().setEmail(jsonObject.getString("email"));

                                }


                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.setUsername(jsonObject.getString("name"));
                                currentUser.put("RestaurantOwner", "NO");

                                currentUser.saveInBackground();

                                // Show the user info
                              //  updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d("LoginFbParse",
                                        "Error parsing returned user data. " + e);
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    Log.d("LoginFbParse",
                                            "Authentication error: " + graphResponse.getError());
                                    break;

                                case TRANSIENT:
                                    Log.d("LoginFbParse",
                                            "Transient error. Try again. " + graphResponse.getError());
                                    break;

                                case OTHER:
                                    Log.d("LoginFbParse",
                                            "Some other error: " + graphResponse.getError());
                                    break;
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void createNewUserWithoutFb(String username, String password, String mail, String numberPhone){

        //To create a nex user without facebook api
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(mail);

// other fields can be set just like with ParseObject
        user.put("phone", numberPhone);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }


}
