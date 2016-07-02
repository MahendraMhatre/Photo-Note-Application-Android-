package edu.scu.mmhatre.mhatremahendrapa3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.io.File;

/**
 * Created by Mj on 19-May-16.
 */
public class ViewActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    VivzHelper helper;
    Context context;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        context =this;
        Bundle bundle = getIntent().getExtras();
     MoviesAdapter m = new MoviesAdapter(context);
        VivzHelper helper = new VivzHelper(context);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.View);
        ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        if(bundle.getString("Position")!= null)
        {
            int pos = Integer.parseInt(bundle.getString("Position"));
            //TODO here get the string stored in the string variable and do
            // setText() on userName

            // Call function to retrieve from database.
            Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_SHORT).show();
            textView.setText(m.getText(pos+1));
            path = m.getPath(pos+1);
            File img = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;  // Experiment with different sizes
            Bitmap b = BitmapFactory.decodeFile(img.getAbsolutePath());;
            // Put Image.
            imageView.setImageBitmap(b);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    Log.v(TAG, "index=");
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;

            case R.id.uninstall:
                Uri packageUri = Uri.parse("package:edu.scu.mparihar.zoodirectory");
                Intent uninstallIntent =
                        new Intent(Intent.ACTION_DELETE, packageUri);
                startActivity(uninstallIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
