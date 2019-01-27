package net.com.diagnose.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.com.diagnose.R;
import net.com.diagnose.bean.Logurl;
import net.com.diagnose.utils.Contans;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogDetailsActivity extends AppCompatActivity {

  //@BindView(R.id.ll_detailLayout)

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);
         linearLayout =(LinearLayout)findViewById(R.id.ll_detailLayout);
        setupActionBar();

        try {
            initHarLog((Logurl) getIntent().getExtras().get("pos"));
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    public void initHarLog(Logurl pos) {
        //HarLog harLog = ((ConstansProxy) getApplication()).proxy.getHar().getLog();
        //HarEntry harEntry = harLog.getEntries().get(pos);

       /* HarRequest harRequest = harEntry.getRequest();
        HarResponse harResponse = harEntry.getResponse();*/

        addItem(Contans.getString(getApplicationContext(), R.string.Overview), "");
        addItem(Contans.getString(getApplicationContext(), R.string.URL), pos.getUrl());
        addItem(Contans.getString(getApplicationContext(), R.string.PostData), DateFormat.format("yyyy-MM-dd HH:mm:ss",pos.getStartedDateTime()).toString());
        addItem(Contans.getString(getApplicationContext(), R.string.network_type), pos.getLogDetail().getNetworkType());
        addItem(Contans.getString(getApplicationContext(), R.string.dns), pos.getLogDetail().getDns());
        addItem(Contans.getString(getApplicationContext(), R.string.serverIPAddress), pos.getLogDetail().getServerIPAddress());
        addItem(Contans.getString(getApplicationContext(), R.string.ms), pos.getLogDetail().getRuniingTime());
    }
    public void addItem(String title, final String value) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_detail, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText(title);

        TextView valueTextView = (TextView) view.findViewById(R.id.tv_value);
        if (TextUtils.isEmpty(value)) {
            valueTextView.setText("");
        } else {
            valueTextView.setText(value.substring(0, value.length() > 50 ? 50 : value.length()));
        }

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value != null && value.length() > 10) {
                    View textEntryView = LayoutInflater.from(HarDetailActivity.this).inflate(R.layout.alert_textview, null);
                    TextView edtInput = (TextView) textEntryView.findViewById(R.id.tv_content);
                    edtInput.setText(value);

                    AlertDialog.Builder builder = new AlertDialog.Builder(HarDetailActivity.this);
                    builder.setCancelable(true);
                    builder.setView(textEntryView);
                    builder.setPositiveButton("确认", null);
                    builder.show();
                }
            }
        });*/


        linearLayout.addView(view);
    }

    private void setupActionBar() {
        setTitle(Contans.getString(getApplicationContext(),R.string.data_detail));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    }
