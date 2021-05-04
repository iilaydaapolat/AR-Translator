package com.example.artranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView mSourceLang;
    private EditText mSourcetext;
    private Button mTranslateBtn;
    private TextView mTranslatedText;
    private String sourceText;
    FloatingActionButton floatingActionButton;
    FloatingActionButton copybutton;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.translator_options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.camera){
            Intent intentToCamera = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intentToCamera);
        }

        else if (item.getItemId() == R.id.userProfile){
            Intent intentToUserProfile = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intentToUserProfile);
        }
        else if (item.getItemId() == R.id.signOut){

            firebaseAuth.signOut();
            Intent intentToFirstActivity = new Intent(MainActivity.this, MainActivityGiris.class);
            startActivity(intentToFirstActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copybutton = findViewById(R.id.copybutton);


        copybutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String getstring =  mTranslatedText.getText().toString();
                ClipData clip = ClipData.newPlainText("TextView",mTranslatedText.getText().toString() );
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this,"Copyed",Toast.LENGTH_SHORT).show();

                //Help to continue :)

            }
        });



        mSourceLang = findViewById(R.id.sourceLang);
        mSourcetext = findViewById(R.id.sourceText);
        mTranslateBtn = findViewById(R.id.translate);
        mTranslatedText = findViewById(R.id.translatedText);
        firebaseAuth = FirebaseAuth.getInstance();
        floatingActionButton= findViewById(R.id.floatingaction);
        mTranslatedText.setMovementMethod(new ScrollingMovementMethod());
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage();
            }
        });

    }
    private void identifyLanguage() {
        sourceText = mSourcetext.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
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

    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "tr":
                langCode = FirebaseTranslateLanguage.TR;
                mSourceLang.setText("Türkçe");
                break;
            case "hi":
                langCode = FirebaseTranslateLanguage.HI;
                mSourceLang.setText("Hindi");
                break;
            case "ar":
                langCode = FirebaseTranslateLanguage.AR;
                mSourceLang.setText("Arabic");

                break;
            case "ur":
                langCode = FirebaseTranslateLanguage.UR;
                mSourceLang.setText("Urdu");

                break;
            case "ja":
                langCode = FirebaseTranslateLanguage.JA;
                mSourceLang.setText("Japan");
                break;
            case "fr":
                langCode = FirebaseTranslateLanguage.FR;
                mSourceLang.setText("French");
                break;
            case "de":
                langCode = FirebaseTranslateLanguage.DE;
                mSourceLang.setText("German");
                break;
            case "sk":
                langCode = FirebaseTranslateLanguage.SK;
                mSourceLang.setText("Slovak");
                break;
            case "sl":
                langCode = FirebaseTranslateLanguage.SL;
                mSourceLang.setText("Slovenian");
                break;
            case "es":
                langCode = FirebaseTranslateLanguage.ES;
                mSourceLang.setText("Spanish");
                break;
            case "sv":
                langCode = FirebaseTranslateLanguage.SV;
                mSourceLang.setText("Swedish");
                break;
            case "uk":
                langCode = FirebaseTranslateLanguage.UK;
                mSourceLang.setText("Ukrainian");
                break;
            case "pl":
                langCode = FirebaseTranslateLanguage.PL;
                mSourceLang.setText("Polish");
                break;
            case "ko":
                langCode = FirebaseTranslateLanguage.KO;
                mSourceLang.setText("Korean");
                break;
            case "kn":
                langCode = FirebaseTranslateLanguage.KN;
                mSourceLang.setText("Kannada");
                break;
            case "it":
                langCode = FirebaseTranslateLanguage.IT;
                mSourceLang.setText("Italian");
                break;
            case "id":
                langCode = FirebaseTranslateLanguage.ID;
                mSourceLang.setText("Indonesian");
                break;
            case "is":
                langCode = FirebaseTranslateLanguage.IS;
                mSourceLang.setText("Icelandic");
                break;
            case "hu":
                langCode = FirebaseTranslateLanguage.HU;
                mSourceLang.setText("hungarian");
                break;
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                mSourceLang.setText("English");
                break;
            case "el":
                langCode = FirebaseTranslateLanguage.EL;
                mSourceLang.setText("Greek");
                break;
            case "fi":
                langCode = FirebaseTranslateLanguage.FI;
                mSourceLang.setText("Finnish");
                break;
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        mTranslatedText.setText("Translating..");
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
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mTranslatedText.setText(s);
                    }
                });
            }
        });
    }
}