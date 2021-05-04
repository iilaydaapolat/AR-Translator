package com.example.artranslator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button mTranslatedBtn;
    TextView mTranslatedText2;
    String sourceText2;
    Button saveBtn;
    Bitmap bitmap;
    //Uri imageData;
   FirebaseFirestore firebaseFirestore;

    //private FirebaseStorage firebaseStorage;
    //private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //find imageview
        imageView = findViewById(R.id.imageId);

        saveBtn = findViewById(R.id.storageBtn);
        //find textview
        textView = findViewById(R.id.textId);
        mTranslatedBtn = findViewById(R.id.translateBtn);
        mTranslatedText2 = findViewById(R.id.translatedText2);
        //firebaseStorage = FirebaseStorage.getInstance();
        //storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //check app level permission is granted for using Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }

        mTranslatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage2();
            }


        });
    }

    private void identifyLanguage2() {
        sourceText2 = textView.getText().toString();
        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        identifier.identifyLanguage(sourceText2).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                    Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();

                }
                else {
                    getLanguageCode(s);
                }
            }
        });


    }

    public void saveImage(View view){


       /* storageReference.child("images").child("images2").putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/

        String originalText = textView.getText().toString();
        String translateText = mTranslatedText2.getText().toString();

        HashMap<String, Object> documentData = new HashMap<>();
        documentData.put("originalText",originalText);
        documentData.put("translateText",translateText);
        documentData.put("date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("Documents").add(documentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

   /* public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,4);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 3){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,4);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            /*imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    bitmap = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(bitmap);
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(bitmap);
                }



            } catch (IOException e) {
                e.printStackTrace();
            }*/


        super.onActivityResult(requestCode, resultCode, data);



            Bundle bundle = data.getExtras();
            //from bundle, extarck the image
            bitmap = (Bitmap) bundle.get("data");
            //set image in imageview

            imageView.setImageBitmap(bitmap);




        //process the image
        //create a FirebaseVisionImage object from a Bitmap object:
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        //2. get instance of FirebaseVision
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        //3. Create an instance of FirebaseVisionTextRecognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        //4. Create a task to process the image
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        //5. if task is success

        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String s = firebaseVisionText.getText();
                textView.setText(s);
            }
        });
        //6. if task is failure
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }


    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "tr":
                langCode = FirebaseTranslateLanguage.TR;
                //mSourceLang.setText("Türkçe");
                break;
            case "hi":
                langCode = FirebaseTranslateLanguage.HI;
                //mSourceLang.setText("Hindi");
                break;
            case "ar":
                langCode = FirebaseTranslateLanguage.AR;
                //mSourceLang.setText("Arabic");

                break;
            case "ur":
                langCode = FirebaseTranslateLanguage.UR;
                //mSourceLang.setText("Urdu");

                break;
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        //mTranslatedText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage(FirebaseTranslateLanguage.EN)
                .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(sourceText2).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mTranslatedText2.setText(s);
                    }
                });
            }
        });
    }
    public void doProcess(View view) {
        //open the camera =>
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);


    }


}
