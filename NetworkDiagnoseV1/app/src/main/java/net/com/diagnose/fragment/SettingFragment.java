package net.com.diagnose.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.security.KeyChain;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import net.com.diagnose.R;
import net.com.diagnose.activity.MainActivity;


import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

import butterknife.ButterKnife;

public class SettingFragment  extends BaseFragment{

    public Button urlbut;
    public EditText urlEx;

    static  SettingFragment settingFragment;
    public static  SettingFragment getInstance(){
        if(settingFragment==null){
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        ButterKnife.bind(this, view);
        RadioButton engRadioButton = (RadioButton) view.findViewById(R.id.englis_but);
        RadioButton chRadioButton = (RadioButton) view.findViewById(R.id.chinese_but);


        urlEx =(EditText) view.findViewById(R.id.url_setting);
        urlbut =(Button)view.findViewById(R.id.go_but);


        engRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("lang", "en");
                getActivity().startActivity(intent);
            }
        });
        chRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(":>>>zzzz<<<<<:");
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("lang", "zh");
                getActivity().startActivity(intent);
            }
        });

        urlbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* WebViewFragment we = WebViewFragment.getInstance(urlEx.getText().toString());*/
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("urlP", urlEx.getText().toString());
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    public void installCert() {
        final String CERTIFICATE_RESOURCE = Environment.getExternalStorageDirectory() + "/har/littleproxy-mitm.pem";
        Toast.makeText(getActivity(), "必须安装证书才可实现HTTPS抓包", Toast.LENGTH_LONG).show();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] keychainBytes;
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(CERTIFICATE_RESOURCE);
                        keychainBytes = new byte[is.available()];
                        is.read(keychainBytes);
                    } finally {
                        IOUtils.closeQuietly(is);
                    }

                    Intent intent = KeyChain.createInstallIntent();
                    intent.putExtra(KeyChain.EXTRA_CERTIFICATE, keychainBytes);
                    intent.putExtra(KeyChain.EXTRA_NAME, "NetworkDiagnosis CA Certificate");
                    startActivityForResult(intent, 3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

       // FileUtil.checkPermission(getActivity(),runnable);
    }

}
