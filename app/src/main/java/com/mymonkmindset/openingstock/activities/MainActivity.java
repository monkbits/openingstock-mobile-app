package com.mymonkmindset.openingstock.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.adapters.TabsPagerAdapter;
import com.mymonkmindset.openingstock.interfaces.HandlesUi;
import com.mymonkmindset.openingstock.widgets.OsWidgerProvider;

public class MainActivity extends BaseActivity implements HandlesUi {
    TabLayout tabLayout;
    ViewPager2 viewPager;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications disabled. Reminders won't show.", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        prepareParent(this);
        gatherResources();
        firstFunction();
        refreshWidget(this);
        checkAndRequestNotificationPermission();
    }

    private void checkAndRequestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    public void gatherResources() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    public void setOnClick() {

    }

    private void firstFunction() {
        gatherResources();
        setOnClick();

        // 1. Attach the Fragment adapter to ViewPager2
        TabsPagerAdapter adapter = new TabsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 2. Link TabLayout and ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Navigation");
                    break;
                case 1:
                    tab.setText("Reminders");
                    break;
                case 2:
                    tab.setText("Persons");
                    break;
                case 3:
                    tab.setText("Contacts");
                    break;
                case 4:
                    tab.setText("Call Logs");
                    break;
            }
        }).attach();

        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
//            Toast.makeText(this, "Front camera is available", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "oops",Toast.LENGTH_SHORT).show();
        }
    }

    public static void refreshWidget(Context context) {
        Intent intent = new Intent(context, OsWidgerProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, OsWidgerProvider.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

}
