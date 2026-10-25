package com.mymonkmindset.openingstock.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.mymonkmindset.openingstock.R;
import com.mymonkmindset.openingstock.database.AppDatabase;
import com.mymonkmindset.openingstock.interfaces.ParentSpecs;
import com.mymonkmindset.openingstock.models.Person;

import java.util.List;
import java.util.concurrent.Executors;

public abstract class BaseActivity extends AppCompatActivity implements ParentSpecs {

    private ImageView home_btn, bookmark_btn, account_btn;

    private static final String pref_name = "OPENING_STOCK_PREF";
    private SharedPreferences pref;

    @Override
    public void prepareParent(Context context) {
        initPrefs(context);
        initBottomNav();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initPrefs(Context context) {
        this.pref = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }

    protected void setPref(String key, String value) {
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    protected String getPref(String key) {
        if (pref == null) return "";
        return pref.getString(key, "");
    }

    protected void removePref(String key) {
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    protected void initBottomNav() {
        String curr_activity = getClass().getSimpleName();

        home_btn = findViewById(R.id.home_btn);
        bookmark_btn = findViewById(R.id.bookmark_btn);
        account_btn = findViewById(R.id.account_btn);

        View homeBox = findViewById(R.id.home_btn_box);
        View bookmarkBox = findViewById(R.id.bookmark_btn_box);
        View accountBox = findViewById(R.id.account_btn_box);

        setActiveNavItem(curr_activity);

        View.OnClickListener homeListener = view -> {
            if (!curr_activity.equals("MainActivity")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };

        View.OnClickListener filesListener = view -> {
            if (!curr_activity.equals("FileManagerActivity")) {
                Intent intent = new Intent(this, FileManagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };

        View.OnClickListener profileListener = view -> {
            if (!curr_activity.equals("ProfileActivity")) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };

        if (home_btn != null) home_btn.setOnClickListener(homeListener);
        if (homeBox != null) homeBox.setOnClickListener(homeListener);

        if (bookmark_btn != null) bookmark_btn.setOnClickListener(filesListener);
        if (bookmarkBox != null) bookmarkBox.setOnClickListener(filesListener);

        if (account_btn != null) account_btn.setOnClickListener(profileListener);
        if (accountBox != null) accountBox.setOnClickListener(profileListener);
    }

    private void setActiveNavItem(String curr_activity) {
        if (home_btn != null) {
            if (curr_activity.equals("MainActivity")) {
                Context context = home_btn.getContext();
                int color = ContextCompat.getColor(context, R.color.color_blue_dark);
                ImageViewCompat.setImageTintList(home_btn, ColorStateList.valueOf(color));
                home_btn.setImageResource(R.drawable.ic_home_filled);
            } else {
                ImageViewCompat.setImageTintList(home_btn, null);
                home_btn.setImageResource(R.drawable.ic_home);
            }
        }

        if (bookmark_btn != null) {
            if (curr_activity.equals("FileManagerActivity")) {
                Context context = bookmark_btn.getContext();
                int color = ContextCompat.getColor(context, R.color.color_blue_dark);
                ImageViewCompat.setImageTintList(bookmark_btn, ColorStateList.valueOf(color));
                bookmark_btn.setImageResource(R.drawable.ic_bookmark_filled);
            } else {
                ImageViewCompat.setImageTintList(bookmark_btn, null);
                bookmark_btn.setImageResource(R.drawable.ic_bookmark);
            }
        }

        if (account_btn != null) {
            if (curr_activity.equals("ProfileActivity")) {
                Context context = account_btn.getContext();
                int color = ContextCompat.getColor(context, R.color.color_blue_dark);
                ImageViewCompat.setImageTintList(account_btn, ColorStateList.valueOf(color));
                account_btn.setImageResource(R.drawable.ic_account_filled);
            } else {
                ImageViewCompat.setImageTintList(account_btn, null);
                account_btn.setImageResource(R.drawable.ic_account);
            }
        }
    }



}
