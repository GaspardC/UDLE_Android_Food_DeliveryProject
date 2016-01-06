package ch.epfl.sweng.udle.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

public class AboutActivity extends SlideMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView versionTextView = (TextView) findViewById(R.id.versionApp);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        versionTextView.setText(version);
    }
}
