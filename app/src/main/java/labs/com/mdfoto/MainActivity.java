package labs.com.mdfoto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dropbox.core.android.Auth;

import butterknife.Bind;
import butterknife.ButterKnife;
// import labs.com.mdfoto.dropbox.Dropbox;
// import labs.com.mdfoto.dropbox.DropboxClientFactory;
// import labs.com.mdfoto.dropbox.DropboxSync;
// import labs.com.mdfoto.dropbox.PicassoClient;
import labs.com.mdfoto.gson.MySharedPrefs;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.ui.activities.AddDoctorActivity;
import labs.com.mdfoto.ui.activities.SettingsActivity;
import labs.com.mdfoto.ui.fragments.LoginFragment;
import labs.com.mdfoto.ui.fragments.MainFragment;
import labs.com.mdfoto.ui.fragments.PatientFragment;

public class MainActivity extends FragmentActivity implements ClickCall {


    private static final String TAG = "MainActivity";
    Fragment fragment;
    @Bind(R.id.button_2)
    ImageButton button2;
    int counter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // DropboxSync dropboxSync = new DropboxSync(this);

        // PicassoClient.init(this, dropboxSync.getUserDropboxClient());

        MySharedPrefs.getInstance(this);

        ButterKnife.bind(this);

        counter = 0;

        fragmentManager = getSupportFragmentManager();

        new UserManager(getApplicationContext());


        Utils.toWhite(button2);


        MainFragment.setClickCall(this);
        LoginFragment.setClickCall(this);
        PatientFragment.setClickCall(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

//        Database.exportDB(this);
        // TODO: 2/16/17 burda export et
        footerClick(null);


    }

    @Override
    protected void onDestroy() {
        if (!UserManager.isEnabled()) {
            UserManager.removeDoctor();
        }
        super.onDestroy();

    }

    public void footerClick(View view) {

        if (UserManager.getStoredDoctor() != null) {
            Log.d(TAG, "onResume: Doctor  " + UserManager.getStoredDoctor().getAndroidId());
        } else {
            Log.d(TAG, "onResume: no doctor");
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }
        counter = 1;
        if (view != null) {

            switch (view.getId()) {

                case R.id.button_0:
                    startActivity(new Intent(this, PatientActivity.class));
                    fragment = null;
                    break;


                case R.id.button_1:
                   /* fragment = new DropboxFragment();*/

                    /*

                    DropboxSync dropboxSync = new DropboxSync(this);
                    dropboxSync.syncFolderToDropbox();
                    Context context = this;

                    SharedPreferences prefs = context.getSharedPreferences(Dropbox.prefName, Context.MODE_PRIVATE);
                    String accessToken = prefs.getString(Dropbox.mdphoto_acces_token, null);

                    if (accessToken == null) {
                        accessToken = Auth.getOAuth2Token();
                        if (accessToken != null) {
                            prefs.edit().putString(Dropbox.mdphoto_acces_token, accessToken).apply();
                            Toast.makeText(context, "Login Successfull", Toast.LENGTH_SHORT).show();

                        } else {
                                Toast.makeText(context, "Login Failed!!", Toast.LENGTH_SHORT).show();
                            fragment = new DropboxFragment();
                        }

                    }else {
                                         }

                    */

                    break;


                case R.id.button_2:
                    SettingsActivity.setClickCall(this);
                    startActivity(new Intent(this, SettingsActivity.class));
                    fragment = null;
                    break;

                case R.id.register_button:
                    SettingsActivity.setClickCall(this);
                    startActivity(new Intent(this, AddDoctorActivity.class));
                    fragment = null;
                    break;

                case R.id.login_button:

                    fragment = new LoginFragment();
                    break;


                case R.id.fab:

//                    fragment = new AddPatientFragment();
                    break;

                default:
                    fragment = new MainFragment();
            }
        } else {
            fragment = new MainFragment();
            counter = 15;
        }


        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container1, fragment);

            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    }


    @Override
    public void call(View view) {
        footerClick(view);
    }

    @Override
    public void onTick(String s) {

    }

    @Override
    public void onBackPressed() {


        if (counter == 15) {
            finish();
        } else {
            footerClick(null);
        }
        Log.d(TAG, "onBackPressed() called with: " + "");

    }
}
