package uwosh.titan_alumni_card_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AlumniCardBackgroundSelectActivity extends AppCompatActivity {
    private static String userData;
    private RadioGroup cardBackgrounds;
    private RadioButton selectedBackground;
    private Button getSelectedBackground;
    private int alumniCardBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_card_background_select);

        userData = getIntent().getStringExtra("USER_DATA");
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        cardBackgrounds = (RadioGroup) findViewById(R.id.radioSex);
        getSelectedBackground = (Button) findViewById(R.id.done);

        getSelectedBackground.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = cardBackgrounds.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                selectedBackground = (RadioButton) findViewById(selectedId);

                switch (selectedId){
                    case R.id.clash:
                        alumniCardBackground = 1;
                        break;
                    case R.id.cob:
                        alumniCardBackground = 2;
                        break;
                    case R.id.scape:
                        alumniCardBackground = 3;
                        break;
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("USER_DATA",userData.concat("," + alumniCardBackground));
                startActivity(i);
            }
        });
    }
}
