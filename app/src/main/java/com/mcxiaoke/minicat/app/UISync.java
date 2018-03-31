package com.mcxiaoke.minicat.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mcxiaoke.minicat.R;
import com.mcxiaoke.minicat.fragment.OptionFragment;

/**
 * Created by gebitang on 31/03/2018.
 *
 */

public class UISync extends UIBaseSupport {

    EditText sync_token, sync_site;
    Button syncSet;
    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_container_sync);
        setProgressBarIndeterminateVisibility(false);
        getActionBar().setTitle("同步");
        getFragmentManager().beginTransaction().replace(R.id.content, OptionFragment.newInstance()).commit();
        updateUI();
    }

    private void updateUI() {
        sync_token = (EditText) findViewById(R.id.sync_token);
        sync_site = (EditText) findViewById(R.id.sync_site);
        syncSet = (Button) findViewById(R.id.syncSet);
        temp = (TextView) findViewById(R.id.tempView);
        syncSet.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveToken();
            }
        });
    }

    private void saveToken() {
        String token = sync_token.getText().toString();
        String domain = sync_site.getText().toString();
        SharedPreferences settings = getSharedPreferences(getString(R.string.tokenRef), Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.token_value), token);
        editor.putString(getString(R.string.domain_value), domain);
        editor.apply();
        temp.setText(String.format("%s %s", domain, token));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onMenuHomeClick() {
        onBackPressed();
    }
}
