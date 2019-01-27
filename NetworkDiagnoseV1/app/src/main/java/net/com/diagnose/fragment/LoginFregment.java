package net.com.diagnose.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import net.com.diagnose.R;
import net.com.diagnose.activity.MainActivity;
import net.com.diagnose.utils.Contans;
import net.com.diagnose.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFregment extends BaseFragment {
   
     static LoginFregment loginFregment;

   // @BindView(R.id.user_id)
  public   EditText userEdit;
  // @BindView(R.id.password_id)
  public  EditText passEdit;
    //@BindView(R.id.submit_id)
    public  Button submit;
   //@BindView(R.id.qq_id)
   public  Button qqbtn;
   // @BindView(R.id.wechat_id)
   public   Button wechatbtn;

    public static  LoginFregment getInstance(){
        if(loginFregment==null){
            loginFregment = new LoginFregment();
        }
        return loginFregment;
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
        View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        userEdit=(EditText)view.findViewById(R.id.user_id) ;
                passEdit=(EditText)view.findViewById(R.id.password_id);
        submit=(Button) view.findViewById(R.id.submit_id);
        qqbtn=(Button) view.findViewById(R.id.qq_id);
        wechatbtn=(Button) view.findViewById(R.id.wechat_id);
        
     setButtonAction();

    return view;
    }

    private void setButtonAction() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user =userEdit.getText().toString();
                if(user!=null && !user.isEmpty()){
                    if(getActivity() instanceof MainActivity){
                        SharedPreferenceUtils.putString(getContext(),"user_name",user);
                             SharedPreferenceUtils.putString(getContext(),"user_login","submit");
                         ((MainActivity) getActivity()).switchContent(WebViewFragment.getInstance());
                    }
                }
            }
        });
        qqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user =userEdit.getText().toString();
                if(user!=null && !user.isEmpty()){
                    if(getActivity() instanceof MainActivity){
                        SharedPreferenceUtils.putString(getContext(),"user_name",user);
                        SharedPreferenceUtils.putString(getContext(),"user_login","qq");
                        ((MainActivity) getActivity()).switchContent(WebViewFragment.getInstance());
                    }
                }
            }
        });
        wechatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user =userEdit.getText().toString();
                if(user!=null && !user.isEmpty()){
                    if(getActivity() instanceof MainActivity){
                        SharedPreferenceUtils.putString(getContext(),"user_name",user);
                        SharedPreferenceUtils.putString(getContext(),"user_login","wechat");
                        ((MainActivity) getActivity()).switchContent(WebViewFragment.getInstance());
                    }
                }
            }
        });

    }

    private void validtion(){

    }
}
