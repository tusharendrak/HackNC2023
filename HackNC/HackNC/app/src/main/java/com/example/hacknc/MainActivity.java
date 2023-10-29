package com.example.hacknc;

import static android.os.Build.VERSION_CODES.M;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.hacknc.model.MPothole;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int PERMISSION_ID = 44 ;
    private static final int CAMERA_REQUEST = 1888;
    private NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private Uri imageURI;
    private ImageView iv_img;
    private TextView tv_cameraHeader,tv_location,tv_description;
    private Button bt_submit;


    private StorageTask mUploadTask;
    private String sublocality = "";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private double lattitude;
    private double longitude;
    private ProgressBar mProgressBar;
    private String address;
    private String mydate;
    private FusedLocationProviderClient mFusedLocationClient;
    private AlertDialog.Builder builder;
    public DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        init();
        listener();




    }

    private void init() {

        //nav bar icon
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationView = findViewById(R.id.nav_view);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report Potholes");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_reports) {
                    Log.d("shreyash", "nav reports selected: ");

                    Intent intent = new Intent(MainActivity.this,ReportsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.nav_map) {
                    Log.d("shreyash", "maps activity selected: ");

                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(intent);
                    return true;
                }
                return  true;
            }
        });



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        iv_img = findViewById(R.id.iv_img);
        tv_cameraHeader = findViewById(R.id.tv_cameraHeader);
        tv_location = findViewById(R.id.tv_location);
        bt_submit = findViewById(R.id.bt_submit);
        tv_description = findViewById(R.id.tv_description);
        mProgressBar = findViewById(R.id.progress_bar);

        mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        Log.d("shreyash","Date:"+mydate);
        
         builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Report Successful")
                .setMessage("Your report has been successfully submitted. We appreciate your contribution to improving road safety.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle OK button click
                    }
                });

       


    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        Log.d("shreyash","getLastLocation()");

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    lattitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(addresses!=null && addresses.size()>0){

                                        address = ((Address)addresses.get(0)).getAddressLine(0);
                                        if(address !=null){
                                            tv_location.setText(address);

                                        }
                                        sublocality = ((Address)addresses.get(0)).getSubLocality();
                                        if (sublocality != null) {
                                            Log.d("shreyash","sublocality"+ sublocality);
                                            tv_location.append("\nSubLocality: \t"+sublocality);
                                        }else{
                                            String locality = ((Address)addresses.get(0)).getLocality();
                                            if (locality != null) {
                                                Log.d("shreyash","Locality"+ locality);
                                                tv_location.append("\ncity: \t"+locality);
                                            }
                                        }
                                    }


                                    Log.d("shreyash","getLastLocation() lattitude:"+location.getLongitude()+" longitude:"+location.getLongitude());

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        Log.d("shreyash","requestNewLocationData()");


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            Log.d("shreyash","LocationCallBack() lattitude:"+mLastLocation.getLatitude()+" longitude:"+mLastLocation.getLongitude());
        }
    };

    //is permission request granted or not
    private boolean checkPermissions() {
        Log.d("shreyash","checkPermission");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("shreyash","checkPermission PERMISSION_GRANTED");

            return true;
        }

        return false;
    }
    private void requestPermissions() {
        Log.d("shreyash","requestPermission()");

        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //is location enabled from setting
    private boolean isLocationEnabled() {
        Log.d("shreyash","isLocationEnabled()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }




    private void listener() {
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageURI==null){
                    Toast.makeText(MainActivity.this, "Image not selected", Toast.LENGTH_LONG).show();
                }
                else if(tv_description.getEditableText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Description can not be empty", Toast.LENGTH_LONG).show();
                }
                else{
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                    } else {


                        uploadFile();
                    }
                }




            }
        });

    }

    private void uploadFile() {
        mProgressBar.setVisibility(View.VISIBLE);
        bt_submit.setClickable(false);
        bt_submit.setVisibility(View.INVISIBLE);
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+".jpg");

        mUploadTask = fileReference.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setProgress(0);
                            }
                        }, 500);

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("shreyash","Success to to get url"+uri.toString());

                                mProgressBar.setVisibility(View.GONE);


                                Log.d("shreyash", "onSuccess: firebase download url: " + uri.toString());

                                MPothole pothole = new MPothole(tv_description.getText().toString().trim(),uri.toString(),sublocality,lattitude,longitude,address,mydate);

                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(pothole).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        reset();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Upload Failed ", Toast.LENGTH_LONG).show();
                                        reset();

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("shreyash","Failed to get url"+e.getMessage());
                            }
                        });








                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    }
                });

    }

    private void reset() {
        tv_description.setText("");
        iv_img.setImageResource(R.drawable.camera);
        imageURI = null;

        mProgressBar.setVisibility(View.INVISIBLE);
        bt_submit.setClickable(true);
        bt_submit.setVisibility(View.VISIBLE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }




    private void getImage() {

        Log.d("shreyash","inside getImage");



        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("shreyash","inside onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // you got your image uri
            iv_img.setImageBitmap(photo);

            try {
                imageURI = getUriFromBitmap(this,photo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Log.d("imageURI", "onActivityResult: "+imageURI);




            // Now, you can pass this bitmap to the cropping activity or save it directly to Firebase Storage.
        }


    }
    Uri getUriFromBitmap(Context context, Bitmap bitmap) throws IOException {
        File tempFile = new File(getCacheDir(), "temp.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
// Handle error
        }
        Uri uri = Uri.fromFile(tempFile);
        return uri;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.show_reports){
            Intent intent = new Intent(MainActivity.this,ReportsActivity.class);
            startActivity(intent);
            return true;
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }






    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("shreyash","OnRequestPermissionResult() PERMISSION_GRANTED");

                getLastLocation();
            }
        }
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}