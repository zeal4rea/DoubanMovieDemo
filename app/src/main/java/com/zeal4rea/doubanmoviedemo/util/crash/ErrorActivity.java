package com.zeal4rea.doubanmoviedemo.util.crash;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zeal4rea.doubanmoviedemo.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        String stackTraceString = getIntent().getStringExtra(CustomOnCrash.EXTRA_STACK_TRACE);
        TextView tvStacktrace = (TextView) findViewById(R.id.error_activity$text_stack_trace);
        tvStacktrace.setText(stackTraceString);
        Button btnRestart = (Button) findViewById(R.id.error_activity$button_restart_application);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<? extends Activity> launcherActivity = CustomOnCrash.getLauncherActivity(ErrorActivity.this);
                if (launcherActivity != null) {
                    Intent intent = new Intent(ErrorActivity.this, launcherActivity);
                    CustomOnCrash.restartApplication(ErrorActivity.this, intent);
                } else {
                    CustomOnCrash.closeApplication(ErrorActivity.this);
                }
            }
        });
    }
}
