package tsotetsi.thapelo.pleasecallmedroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DroidMainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_droid_main, menu);
        return true;
    }
}
