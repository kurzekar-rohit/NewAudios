package com.example.hp.newaudio;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording;
    private static String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.button);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) findViewById(R.id.button4);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = new Date();
//                    String strDate = formatter.format(date);
//
//                    AudioSavePathInDevice =
//                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
//                    File.separator  + strDate + ".3gp";

//                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
//                    Date now = new Date();
//                    String strDate = sdfDate.format(now);
//                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    // mFileName += "/AudioRecording.3gp";
//                    AudioSavePathInDevice = AudioSavePathInDevice +  File.separator + "/" + strDate + ".3gp";

//                     File root = Environment.getExternalStorageDirectory();
//                    File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    AudioSavePathInDevice =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".3gp");

                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    Date n = new Date();
                    String fn = f.format(n);
//                    File root = Environment.getExternalStorageDirectory();
//                    File file = new File(root.getAbsolutePath() + fn);

                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory + File.separator + fn + File.separator);

                    if (!file.exists()) {
                        file.mkdirs();
                        Log.d(LOG_TAG, "In if else");
                    }
//                    File outputFile = new File(file, fileName);
//                    try {
//                        FileOutputStream fos = new FileOutputStream(outputFile);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath();
                    // mFileName += "/AudioRecording.3gp";
                    AudioSavePathInDevice = AudioSavePathInDevice + File.separator + fn + File.separator + strDate + ".3gp";
                    MediaRecorderReady();


                    try {
                        mediaRecorder.prepare();


                        mediaRecorder.start();
                        Log.d(LOG_TAG, "Start again");
                        Toast.makeText(MainActivity.this, "Recording started",
                                Toast.LENGTH_LONG).show();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);


                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                mediaRecorder.release();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(MainActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Log.d(LOG_TAG, "Going to start");
                Toast.makeText(MainActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();

            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if (mediaPlayer != null) {
                    Log.e(LOG_TAG, "Recording Stop playing click");
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                    Toast.makeText(getApplicationContext(), "Playing Audio Stopped", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void MediaRecorderReady() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.VOICE_COMMUNICATION);  //working
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);  //not working
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        Log.d(LOG_TAG, "In");
    }

//    public String CreateRandomAudioFileName(int string) {
//        StringBuilder stringBuilder = new StringBuilder(string);
//        int i = 0;
//        while (i < string) {
//            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
//
//            i++;
//        }
//        return stringBuilder.toString();
//    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}