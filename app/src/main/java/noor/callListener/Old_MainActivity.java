package noor.callListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Old_MainActivity {

    /*

    package noor.callListener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView prmsns;
    DatabaseHelper db;
    public final int PICK_CONTACT = 2015;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_BOOT_COMPLETED};

    TextView _num1, _num2, _num3;
    TextView _name1,_name2,_name3;
    Button edit1,edit2,edit3;

    Button delete1,delete2,delete3;

    String[] numbers = new String[3];
    String[] names = new String[3];

    Numbers num;
    int cas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        num = new Numbers(0, "Number","Name");

        prmsns = (TextView)findViewById(R.id.error);
        prmsns.setVisibility(View.INVISIBLE);

        _num1 = (TextView) findViewById(R.id.view_num_1);
        _num2 = (TextView) findViewById(R.id.view_num_2);
        _num3 = (TextView) findViewById(R.id.view_num_3);

        _name1 = (TextView)findViewById(R.id.name1);
        _name2 = (TextView)findViewById(R.id.name2);
        _name3 = (TextView)findViewById(R.id.name3);

        edit1 = (Button) findViewById(R.id.editBtn1);
        edit2 = (Button) findViewById(R.id.editBtn2);
        edit3 = (Button) findViewById(R.id.editBtn3);

        delete1 = (Button)findViewById(R.id.del1);
        delete2 = (Button)findViewById(R.id.del2);
        delete3 = (Button)findViewById(R.id.del3);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        if(db.isEmpty()) {
            //db.CreateTables();
            num.setId(1);
            db.InsertNumber(num);
            num.setId(2);
            db.InsertNumber(num);
            num.setId(3);
            db.InsertNumber(num);
        }
        if(db.isEmptyLocations()){
            Locations loc = new Locations(1, date,String.valueOf(0.000000),String.valueOf(0.000000));
            db.CreateLocations(loc);
        }

        ViewNumbers();

        edit1.setOnClickListener(this);
        edit2.setOnClickListener(this);
        edit3.setOnClickListener(this);

        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);

    }

    void ViewNumbers() {

        hasPermissions(this,PERMISSIONS);
        numbers = db.GetNumbers();
        names = db.GetNames();

        _num1.setText(numbers[0]);
        _num2.setText(numbers[1]);
        _num3.setText(numbers[2]);

        _name1.setText(names[0]);
        _name2.setText(names[1]);
        _name3.setText(names[2]);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (view.getId()){
            case R.id.editBtn1:
                cas=1;
                break;

            case R.id.editBtn2:
                cas=2;
                break;

            case R.id.editBtn3:
                cas=3;
                break;

            case R.id.del1:
                cas = 1;
                break;

            case R.id.del2:
                cas = 2;
                break;

            case R.id.del3:
                cas = 3;
                break;

            default:
                return;

        }

        Log.e("Onclick" ,"clicked button "+id+" "+cas);

        hasPermissions(this,PERMISSIONS);

        if(id == R.id.del1 || id == R.id.del2 || id == R.id.del3){
            db.UpdateNumbers(cas,"Number","Name");
            ViewNumbers();
        }
        else if(id == R.id.editBtn1 || id == R.id.editBtn2 || id ==R.id.editBtn3) {
            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(i, PICK_CONTACT);
        }

        ViewNumbers();

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    prmsns.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        }
        prmsns.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            contactPicked(data);
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            //Toast.makeText(getApplicationContext(),name+"  "+phoneNo,Toast.LENGTH_SHORT).show();
            Log.e("MainActivity"," selected contact "+name+"  "+phoneNo+"  "+cas);
            num.setId(cas);
            num.setNumber(phoneNo);
            db.UpdateNumbers(cas,phoneNo,name);
            ViewNumbers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hasPermissions(this,PERMISSIONS);
        cas=0;
    }

}




     */

    //LOCATIONSERVICE

    /*

    package noor.callListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class LocationService extends Service {

    DatabaseHelper db;

    static LocationManager locationManager;
    final Looper looper = null;
    private Location mlocation;
    Criteria criteria;
    static String incomingNumber = null;
    int smsStat = 0, smsReady = 0;
    String url = "http://maps.google.com/maps?q=";
    static String SMS;


    public LocationService() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        //Log.e("LocationService", "In Oncreate");
        db = new DatabaseHelper(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        //criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        //criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        smsStat = 0;
        smsReady = 0;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("Loc Provider Disabled", "Current location Unavailable");
            //Toast.makeText(getApplicationContext(),"  Provider disabled   ",Toast.LENGTH_SHORT).show();
            sendSMSfromDB();
        } else {
            Log.e("Loc Provider enabled", "requesting location....");
            Toast.makeText(getApplicationContext(), "Provider enabled requesting location....", Toast.LENGTH_SHORT).show();
            RequestLocation();
        }
    }

    private void RequestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate(criteria, locationListener, looper);
    }

    private void sendSMSfromDB() {
        Locations loc ;
        if(!db.isEmptyLocations()){
            loc = db.GetLocations();
            String msg = "Location disabled by user "+url+loc.lattitude+","+loc.longitude+" Last seen at: "+loc.getTime();
            Log.e("Provider disabled",msg);
            //Toast.makeText(getApplicationContext(),"  Provider disabled   "+msg,Toast.LENGTH_SHORT).show();
            SMS=msg;
            smsReady=1;
            sendSMS();
        }
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mlocation = location;
            //Toast.makeText(getApplicationContext(),"     "+location.getLongitude(),Toast.LENGTH_SHORT).show();

            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());

            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            Locations locations   = new Locations(1,date,lat,lon);
            db.UpdateLocations(locations);

            Log.e("Current Location:  ", location.toString());
            String sms ="My location "+ url+lat+","+lon+"   Last seen at: "+date;
            Log.e("message: ","  "+sms);
            SMS=sms;
            smsReady=1;
            sendSMS();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Status Changed", String.valueOf(status));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Provider Enabled", provider);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("Provider Disabled", provider);
            //Toast.makeText(getApplicationContext(),"  Provider disabled   ",Toast.LENGTH_SHORT).show();


        }
    };


    @SuppressLint("MissingPermission")
    void location() {
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        incomingNumber = intent.getStringExtra("key");
        //Toast.makeText(getApplicationContext(),"num:     "+incomingNumber,Toast.LENGTH_SHORT).show();
        Log.e("Incomming-LCTN_SRVC:",incomingNumber);
        if(smsStat==0 && smsReady==1)
            sendSMS();
        smsStat=0;
        return Service.START_STICKY;
    }

    public void sendSMS(){
        Log.e("sesndSMS","no "+incomingNumber);
        Toast.makeText(getApplicationContext(),"sesndSMS "+"no "+incomingNumber,Toast.LENGTH_SHORT).show();
        if(incomingNumber!=null && SMS != null  && smsReady ==1) {
            smsStat=1;
            smsReady=0;
            Toast.makeText(getApplicationContext(),"Sending sms... ",Toast.LENGTH_SHORT).show();
            Log.e("Sending sms:","  "+SMS+"   to: "+incomingNumber);
            SmsManager smsManager = SmsManager.getDefault();
            //msg= "http://maps.google.com/maps?q="+location.getLattitude()+","+location.getLongitude()+"       Last Seen: "+location.getTime();
            smsManager.sendTextMessage(incomingNumber, null, SMS, null, null);
            this.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        Log.e("OnDestroy","bfore super");
        super.onDestroy();
        Log.e("OnDestroy","after super");
    }
}


     */
}
