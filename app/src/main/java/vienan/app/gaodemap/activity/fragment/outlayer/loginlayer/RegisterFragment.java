package vienan.app.gaodemap.activity.fragment.outlayer.loginlayer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vienan.app.gaodemap.R;
import vienan.app.gaodemap.activity.MainActivity;


/**
 * 注册
 */
public class RegisterFragment extends Fragment {
    @Bind(R.id.et_write_phone)
    EditText etWritePhone;
    @Bind(R.id.et_put_identify)
    EditText etPutIdentify;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.et_put_psw)
    EditText etPutPsw;
    private AVUser user;
    private String phoneNum;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_register, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_next)
    public void sendCode() {
        final String phone = etWritePhone.getText().toString();
        final String psw=etPutPsw.getText().toString();
        if (phone.equals("") || phone.length() != 11) {
            Toast.makeText(getActivity(), "手机号码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw.length() <6||psw.length()>10) {
            Toast.makeText(getActivity(), "密码长度在6-10之间", Toast.LENGTH_SHORT).show();
            return;
        }
        user = new AVUser();
        new AsyncTask<Void, Void, Void>() {
            boolean res;

            @Override
            protected Void doInBackground(Void... params) {
                user.setUsername(phone);
                user.setPassword(psw);
                user.setMobilePhoneNumber(phone);
                try {
                    user.signUp();
                    res = true;
                } catch (AVException e) {
                    e.printStackTrace();
                    res = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (res) {
                    Toast.makeText(getActivity(), "短信发送成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "短信发送失败", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @OnClick(R.id.btn_login)
    public void verifyCode() {
        final String code = etPutIdentify.getText().toString();
        AVUser.verifyMobilePhoneInBackground(code,
                new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "短信验证成功", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "短信验证失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
