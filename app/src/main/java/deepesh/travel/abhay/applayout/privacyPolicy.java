package deepesh.travel.abhay.applayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by deepesh on 21/06/17.
 */

public class privacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacypolicy);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,SettingTest.class);
        i.putExtra("key","3");
        startActivity(i);
    }
}
