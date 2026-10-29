package com.mymonkmindset.openingstock.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.interfaces.HandlesUi;
import com.mymonkmindset.openingstock.interfaces.ParentSpecs;

public class ProfileActivity extends BaseActivity implements ParentSpecs, HandlesUi {

    private TextView tvProfileName, tvProfileHandle, tvProfileRating, tvProfileBio, tvLanguages, tvInterests;
    private TextView tvStatCalls, tvStatTalkTime, tvStatFollowers;

    private EditText backendUrl;
    private MaterialButton setBackendBtn;
    private Button btnEditProfile, btnShareProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prepareParent(this);
        gatherResources();
        loadProfileData();
        setOnClick();
    }

    private void loadProfileData() {
        if (backendUrl          != null)        backendUrl.setText(getPref("backend_url"));
        if (tvProfileName       != null) tvProfileName.setText("Ankur Goyal");
        if (tvProfileHandle     != null) tvProfileHandle.setText("@ankurgoyal • Monk Mindset");
        if (tvProfileRating     != null) tvProfileRating.setText("★ 4.9 Rating • Top Speaker");

        if (tvStatCalls         != null) tvStatCalls.setText("142");
        if (tvStatTalkTime      != null) tvStatTalkTime.setText("18h 45m");
        if (tvStatFollowers     != null) tvStatFollowers.setText("1.2k");

        if (tvProfileBio != null) {
            tvProfileBio.setText("Founder @ Monk Mindset. Passionate about building native Android applications, AI tools, and productivity solutions.");
        }
        if (tvLanguages != null) tvLanguages.setText("English • Hindi • Sanskrit");
        if (tvInterests != null) tvInterests.setText("Android Native • AI/ML • Startups • Philosophy");
    }

    @Override
    public void gatherResources() {
        tvProfileName   = findViewById(R.id.tvProfileName);
        tvProfileHandle = findViewById(R.id.tvProfileHandle);
        tvProfileRating = findViewById(R.id.tvProfileRating);
        tvProfileBio    = findViewById(R.id.tvProfileBio);
        tvLanguages     = findViewById(R.id.tvLanguages);
        tvInterests     = findViewById(R.id.tvInterests);
        backendUrl      = findViewById(R.id.backendUrl);
        setBackendBtn   = findViewById(R.id.setBackendBtn);

        tvStatCalls     = findViewById(R.id.tvStatCalls);
        tvStatTalkTime  = findViewById(R.id.tvStatTalkTime);
        tvStatFollowers = findViewById(R.id.tvStatFollowers);

        btnEditProfile  = findViewById(R.id.btnEditProfile);
        btnShareProfile = findViewById(R.id.btnShareProfile);
    }

    @Override
    public void setOnClick() {

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v ->
                    Toast.makeText(ProfileActivity.this, "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnShareProfile != null) {
            btnShareProfile.setOnClickListener(v ->
                    Toast.makeText(ProfileActivity.this, "Share Profile Link Copied!", Toast.LENGTH_SHORT).show()
            );
        }
        if(setBackendBtn != null){
            setBackendBtn.setOnClickListener(view -> {
               setPref("backend_url", backendUrl.getText().toString().trim());
               Toast.makeText(this, "Backend URL saved", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
