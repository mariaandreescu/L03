package ro.pub.cs.systems.eim.lab03.phonedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ro.pub.cs.systems.eim.lab03.phonedialer.R;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    HangUpListener hangUpListener = new HangUpListener();
    ButtonListener buttonListener = new ButtonListener();
    BackspaceListener backspaceListener = new BackspaceListener();
    CallListener callListener = new CallListener();

    private class HangUpListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private class CallListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private class BackspaceListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                phoneNumberEditText.setText(phoneNumber);
            }
        }
    }


    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            phoneNumberEditText.setText(phoneNumberEditText.getText().toString() + ((Button) view).getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = (EditText) findViewById(R.id.edit_text_image_button);
        ImageButton callImageButton = (ImageButton) findViewById(R.id.call_image_button);
        callImageButton.setOnClickListener(callListener);
        ImageButton hangUpImageButton = (ImageButton) findViewById(R.id.hang_up_image_button);
        hangUpImageButton.setOnClickListener(hangUpListener);
        ImageButton backspaceImageButton = (ImageButton) findViewById(R.id.backspace_image_button);
        backspaceImageButton.setOnClickListener(backspaceListener);
        for (int index = 0; index < Constants.buttonIds.length; index++) {
            Button button = (Button) findViewById(Constants.buttonIds[index]);
            button.setOnClickListener(buttonListener);
        }
    }
}