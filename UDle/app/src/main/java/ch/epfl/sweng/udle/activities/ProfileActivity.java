/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package ch.epfl.sweng.udle.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Shows the user profile. This simple activity can function regardless of whether the user
 * is currently logged in.
 */
public class ProfileActivity extends SlideMenuActivity {
  private static final int LOGIN_REQUEST = 0;

  private TextView titleTextView;
  private TextView emailTextView;
  private TextView nameTextView;
  private Button loginOrLogoutButton;
  private Button orderNowButton;
  private SeekBar seekBarRestaurantDistance;
  private ParseUser currentUser;
  private  TextView seekBarValue;
  private static ProfilePictureView avatar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_profile);
    titleTextView = (TextView) findViewById(R.id.profile_title);
    emailTextView = (TextView) findViewById(R.id.profile_email);
    nameTextView = (TextView) findViewById(R.id.profile_name);
    loginOrLogoutButton = (Button) findViewById(R.id.login_or_logout_button);
    orderNowButton = (Button) findViewById(R.id.goToHome_Button);
    seekBarRestaurantDistance = (SeekBar) findViewById(R.id.seekBarRestaurantDistance);
    seekBarValue = (TextView)findViewById(R.id.value_distance_restaurant);
    titleTextView.setText(R.string.profile_title_logged_in);

    avatar = (ProfilePictureView) findViewById(R.id.avatar);
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    if (accessToken != null) {
      avatar.setProfileId(accessToken.getUserId());
    }

    loginOrLogoutButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (currentUser != null) {
          // User clicked to log out.
          ParseUser.logOut();
          currentUser = null;
          showProfileLoggedOut();
        } else {
          // User clicked to log in.
          ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                  ProfileActivity.this);
          startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
        }
      }
    });
  }

  /**
   * Show either the login/logout layout in function of the status of the user
   */
  @Override
  protected void onStart() {
    super.onStart();

    currentUser = ParseUser.getCurrentUser();
    if (currentUser != null) {
      showProfileLoggedIn();
    } else {
      showProfileLoggedOut();
    }
  }

  /**
   * Shows the profile of the given user.
   */
  private void showProfileLoggedIn() {
    titleTextView.setText(R.string.profile_title_logged_in);
    emailTextView.setText(currentUser.getEmail());
    String fullName = currentUser.getString("username");
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    if (accessToken != null) {
      avatar.setProfileId(accessToken.getUserId());
    }
    if (fullName != null) {
      nameTextView.setText(fullName);
    }
    loginOrLogoutButton.setText(R.string.profile_logout_button_label);
    final ParseUser currentUser = DataManager.getUser();
    if(currentUser == null){
      return;
    }
    else{
      if(currentUser.getBoolean("RestaurantOwner")){

        setSeekBar();


  }
      else{
        seekBarRestaurantDistance.setVisibility(View.GONE);
        seekBarValue.setVisibility(View.GONE);
        orderNowButton.setVisibility(View.VISIBLE);

      }
    }
  }

  /**
   * Use to display, init the seekbar which is used by the restaurant to set the radius of their delivery area
   * It can be an integer between 0 and 30 km and it is automatically sent to the server
   */
  private void setSeekBar() {

    orderNowButton.setVisibility(View.GONE);
    seekBarRestaurantDistance.setVisibility(View.VISIBLE);
    seekBarValue.setVisibility(View.VISIBLE);
    int seekbarValueInit = currentUser.getInt("maxDeliveryDistanceKm");
    if(0 != seekbarValueInit){
      seekBarRestaurantDistance.setProgress(seekbarValueInit);
      seekBarValue.setText("Radius of delivery : " + String.valueOf(seekbarValueInit) + " km");
    }

    final int[] seekvalue = {0};

    seekBarRestaurantDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
                                    boolean fromUser) {
        seekBarValue.setText("Radius of delivery : "+String.valueOf(progress) + " km");
        seekvalue[0] = progress;
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        currentUser.put("maxDeliveryDistanceKm", seekvalue[0]);
        currentUser.saveInBackground();
      }
    });
  }

  /**
   * @param view Button Begin
   *             Use to begin when the user has logged in successfully
   */
  public void goToMapActivity(View view){
    Intent intent = new Intent(this, MapActivity.class);
    startActivity(intent);
  }

  /**
   * Show a message asking the user to log in, toggle login/logout button text.
   */
  private void showProfileLoggedOut() {
    titleTextView.setText(R.string.profile_title_logged_out);
    emailTextView.setText("");
    nameTextView.setText("");
    seekBarRestaurantDistance.setVisibility(View.GONE);
    seekBarValue.setVisibility(View.GONE);
    orderNowButton.setVisibility(View.GONE);
    loginOrLogoutButton.setText(R.string.profile_login_button_label);
    avatar.setProfileId(null);

  }
}
