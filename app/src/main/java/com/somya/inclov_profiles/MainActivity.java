package com.somya.inclov_profiles;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Camera;
import android.hardware.Camera.CameraInfo;

//import java.sql.Date;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.id.toggle;
import static com.somya.inclov_profiles.R.id.thumbnail1;
import static com.somya.inclov_profiles.R.menu.activity_main_drawer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;
    //store id of profile imageview
    int imageview_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //To set title of the page
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Browse People");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // To slide left open the navigation drawer on the click of menu button on action bar
       else if (item.getItemId() == android.R.id.home) {
            if(drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            }else{
                drawer.openDrawer(Gravity.LEFT);
            }}


            return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Open the camera
    public void open_camera(View view)
    {
        //Getting id of Imageview
        imageview_id=view.getId();
        Log.d("id", String.valueOf(imageview_id));

        if(CAMERA_CAPTURE==1) {
            try {
                //open the back camera on click of image
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }


            catch (ActivityNotFoundException anfe) {
                //display an error message,if camera not supported
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode,Intent data){

            ImageView thumbnail1=(ImageView)findViewById(imageview_id);

            if(requestcode == 1){
                //get the Uri for the captured image
                Bitmap picUri=(Bitmap) data.getExtras().get("data");

                //display the returned cropped image
                thumbnail1.setImageBitmap(picUri);

                //Save image locally
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Log.d("timestamp",timeStamp);


                //folder stuff
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                imagesFolder.mkdirs();

                File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
                Uri uriSavedImage = Uri.fromFile(image);

                Toast toast = Toast.makeText(this,"Image saved at "+ String.valueOf(uriSavedImage), Toast.LENGTH_LONG);
                toast.show();

            }
    }

}
