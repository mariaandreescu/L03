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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhoneDialer extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private ImageButton contactsImageButton;
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

    private ContactsImageButtonClickListener contactsImageButtonClickListener = new ContactsImageButtonClickListener();

    private class ContactsImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                Intent intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CallListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialer.this,
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
        setContentView(R.layout.activity_phone_dialer);

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

        contactsImageButton = (ImageButton) findViewById(R.id.contact_manager);
        contactsImageButton.setOnClickListener(contactsImageButtonClickListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }
}