package vienan.app.gaodemap.activity.fragment.outlayer.loginlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import vienan.app.gaodemap.R;
import vienan.app.gaodemap.activity.MainActivity;


/**
 * 登录
 */
public class LoginFragment extends Fragment {
    View view;
    EditText et_phone,et_psw;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_new_login, null);
        et_phone= (EditText) view.findViewById(R.id.et_phone);
        et_psw= (EditText) view.findViewById(R.id.et_psw);
        TextView tv_down = (TextView)view.findViewById(R.id.tv_down);
        tv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRankMe();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * 下载正式版小红书
     */
    private void gotoRankMe() {
      /*  try {
            Uri uri = Uri.parse("market://details?id=com.xingin.xhs");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }*/
        String username=et_phone.getText().toString();
        String psw=et_psw.getText().toString();
        if (username.equals("")||psw.equals("")){
            Toast.makeText(getActivity(),"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        AVUser.logInInBackground(username, psw, new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    // 登录成功
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    // 登录失败
                    Toast.makeText(getActivity(),"用户不存在，请注册新用户！",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
