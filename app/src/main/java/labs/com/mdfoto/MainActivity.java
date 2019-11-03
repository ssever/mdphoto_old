package labs.com.mdfoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
// import labs.com.mdfoto.dropbox.Dropbox;
// import labs.com.mdfoto.dropbox.DropboxClientFactory;
// import labs.com.mdfoto.dropbox.DropboxSync;
// import labs.com.mdfoto.dropbox.PicassoClient;
import labs.com.mdfoto.gson.MySharedPrefs;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.ui.activities.AddDoctorActivity;
import labs.com.mdfoto.ui.activities.SettingsActivity;
import labs.com.mdfoto.ui.fragments.LoginFragment;
import labs.com.mdfoto.ui.fragments.MainFragment;
import labs.com.mdfoto.ui.fragments.PatientFragment;

public class MainActivity extends FragmentActivity implements ClickCall {


    private static final String TAG = "MainActivity";
    private static final String KEY_OBJECT = "patient_v1";
    Fragment fragment;
    @Bind(R.id.button_2)
    ImageButton button2;
    int counter;
    private FragmentManager fragmentManager;
    private static PatientManager manager;

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

        // Check if has patient json file

        manager = MySharedPrefs.loadObject(KEY_OBJECT, PatientManager.class);

        if (manager == null) {

            String loadedJsonDataString = readFromFile(this);

            //String patients = getJsonData(this);

            Log.d(TAG, "Empty Manager");

            //File file = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_last.json");

            //manager = new PatientManager();
        }

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

    public String readFromFile(Context context) {

        String file = Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_last.json";

        String ret = "";

            try {

                InputStream inputStream = context.openFileInput(file);

                Log.e("readFromFile", "File path: " + context.getFilesDir().getAbsolutePath() + "/booking_slots.json");

                if (inputStream != null) {

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String receiveString = "";

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {

                        stringBuilder.append(receiveString);

                    }

                    inputStream.close();

                    ret = stringBuilder.toString();

                }
            } catch (FileNotFoundException e) {

                Log.e("readFromFile", "File not found: " + e.toString());

            } catch (IOException e) {

                Log.e("readFromFile", "Can not read file: " + e.toString());
            }
            return ret;
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
