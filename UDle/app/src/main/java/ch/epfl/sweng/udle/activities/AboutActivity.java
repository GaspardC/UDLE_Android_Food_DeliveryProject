package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;

public class AboutActivity extends SlideMenuActivity {

    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        versionTextView = (TextView) findViewById(R.id.versionApp);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*String version = pInfo.versionName;
        versionTextView.setText("v: " + version);*/


        LinearLayout llContactUs = (LinearLayout) findViewById(R.id.llContactUs);
        LinearLayout llWebsite = (LinearLayout) findViewById(R.id.llWebsite);
        LinearLayout llBlog = (LinearLayout) findViewById(R.id.llBlog);
        LinearLayout llCvg = (LinearLayout) findViewById(R.id.llCvg);

        llWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressSite = "http://www.udle-app.com";
                goToPageView(addressSite);
            }
        });
        llBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressSite = "http://www.udle-blog.com";
                goToPageView(addressSite);
            }
        });
        llCvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressSite = "http://www.udle-app.com/cgv/";
                goToPageView(addressSite);
            }
        });

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"contact@udle.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Contact Client, " + DataManager.getUser().getUsername());

/* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });


    }

    private void goToPageView(String addressSite) {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("address",addressSite);
        startActivity(intent);
    }
}
