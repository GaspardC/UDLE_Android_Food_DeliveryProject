package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    /** Called when the user clicks the LogInButton button */
    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);


    }

    /** Called when the user clicks the Register button */
    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);


    }

}
