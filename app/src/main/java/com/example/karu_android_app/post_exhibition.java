package com.example.karu_android_app;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class post_exhibition extends AppCompatActivity {
    private ImageButton back;
    private EditText Event_name, Event_place, Event_date, Event_price, Event_host, Host_nid, Payment_num;
    private Button addPhoto;

    public static final String Key_event_name = "eventName";
    public static final String Key_event_place = "eventPlace";
    public static final String Key_event_date = "eventDate";
    public static final String Key_event_price = "ticketPrice";
    public static final String Key_event_host = "eventHost";
    public static final String Key_host_nid = "hostNID";
    public static final String Key_payment_num = "paymentNumber";
    private ImageView view_uploadedImage;
    private static final int Pick_image_request = 1;
    private Uri imageUri;
    private StorageTask mUploadTask;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("Exhibition");
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_exibition);

        back = findViewById(R.id.backBTN);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),showExhibition.class);
                startActivity(intent);
            }
        });

        Event_name = findViewById(R.id.event_name);
        Event_place = findViewById(R.id.event_place);
        Event_date = findViewById(R.id.event_date);
        Event_host = findViewById(R.id.event_host);
        Host_nid = findViewById(R.id.host_nid);
        Event_price = findViewById(R.id.event_price);
        Payment_num = findViewById(R.id.payment_num);
        view_uploadedImage = findViewById(R.id.eventLogo);
        addPhoto = findViewById(R.id.addPhotoBTN);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        Event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        post_exhibition.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month+1;
                String date = day+"/"+month+"/"+year;
                Event_date.setText(date);
            }
        };
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_image_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_image_request && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(view_uploadedImage);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    String downloadUrl;
    public void publishExhibition(View view) {
        String eventName = Event_name.getText().toString();
        String eventPlace = Event_place.getText().toString();
        String eventDate = Event_date.getText().toString();
        String eventHost = Event_host.getText().toString();
        double price = Double.parseDouble(Event_price.getText().toString());
        double nid = Double.parseDouble(Host_nid.getText().toString());
        double payment = Double.parseDouble(Payment_num.getText().toString());


        //uploading image to fireStorage

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                                    System.out.println(downloadUrl);

                                    Map<String, Object> postInfo = new HashMap<>();
                                    postInfo.put(Key_event_name, eventName);
                                    postInfo.put(Key_event_place, eventPlace);
                                    postInfo.put(Key_event_date, eventDate);
                                    postInfo.put(Key_event_host, eventHost);
                                    postInfo.put(Key_host_nid, nid);
                                    postInfo.put(Key_event_price, price);
                                    postInfo.put(Key_payment_num, payment);
                                    postInfo.put("userUid", user.getUid());
                                    postInfo.put("eventLogo", downloadUrl);
                                    postInfo.put("search", eventName.toLowerCase());

                                    DocumentReference documentReference = root.collection("Exhibition").document();

                                    documentReference.set(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Exhibition hosted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        };
                    });
        }
                    else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();

        }
    }
}