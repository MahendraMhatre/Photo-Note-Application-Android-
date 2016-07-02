package edu.scu.mmhatre.mhatremahendrapa3;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.graphics.Bitmap;
import java.io.File;
import android.provider.MediaStore;
import java.io.IOException;
import android.net.Uri;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.util.Date;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import android.app.Activity;

public class ImageActivity extends AppCompatActivity {
    Button b1;
    ImageView iv;
    static int TAKE_PICTURE = 1;
    static int ACTION_TAKE_PICTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    private File imageFile;
    Button save;
    String caption;
    String path;
    MoviesAdapter helper;
    EditText editCaption;
    Bitmap bitMap;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        b1=(Button)findViewById(R.id.button);
        save = (Button)findViewById(R.id.button2);
        iv=(ImageView)findViewById(R.id.imageView);
        editCaption= (EditText)findViewById(R.id.editText);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process(v);
            }
        });
        helper = new MoviesAdapter(this);
        context=this;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption = editCaption.getText().toString();
                if(caption.isEmpty())
                {
                    editCaption.setError("Please enter the caption!");
                }
                else if(path == null){
                    AlertDialog alertDialog = new AlertDialog.Builder(ImageActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Alert!");

                    // Setting Dialog Message
                    alertDialog.setMessage("Please Capture an Image!");

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
                else{
                    long id = helper.insertData(path, caption);
                    Toast.makeText(context,"Id = "+id,Toast.LENGTH_LONG).show();
//                    Intent returnIntent = new Intent();
//                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }


    public void process(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            imageFile = null;
            try{
                imageFile = createFile();
            }catch (IOException e){
                //ERROR OCCURRED
            }
            if(imageFile!=null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(imageFile));
                startActivityForResult(intent, 0);
            }
        }


    }

    public File createFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getStorageDir("Photos");
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return image;
    }

    public File getStorageDir(String albumName){
        File file = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName);
        if (!file.mkdirs()) {
            Log.e("", "Directory not created");
        }
        return file;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        path = imageFile.getAbsolutePath().toString();
                        File img = new File(path);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;  // Experiment with different sizes
                        Bitmap b = BitmapFactory.decodeFile(img.getAbsolutePath());
                        iv.setImageBitmap(b);

                    }

                    else{
                        Toast.makeText(this,"Not saved",Toast.LENGTH_LONG).show();
                    }
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this,"Not saved",Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }



    protected void onDestroy() {
        super.onDestroy();
    }
}
