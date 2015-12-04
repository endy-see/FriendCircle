package com.zhym.myapp.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhym.myapp.R;


public class RegisterActivity extends Activity {
	String TAG = "RegisterActivity";
	EditText username, password;
	TextView isSuccess;
	Button register;
	Handler handler;
	String result;
	
    @SuppressLint("ServiceCast") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        init();

        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isSuccess.setText(R.string.registering);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Log.e(TAG + ":onCreate:before register()", "begin register()");
                        register();
                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        
        handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
                switch(result) {
                    case "1":
                        Toast.makeText(getApplicationContext(), R.string.regist_success, Toast.LENGTH_SHORT).show();
                        Log.e("RegisterActivity:handleMessage:result","1");
                        isSuccess.setText(R.string.regist_success);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        break;
                    case "0":
                        Log.e("RegisterActivity:handleMessage:result","0");
                        Toast.makeText(getApplicationContext(), R.string.regist_failure, Toast.LENGTH_SHORT).show();
                        isSuccess.setText(R.string.regist_failure);
                        startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                        break;
                    case "-1":
                        Log.e("RegisterActivity:handleMessage:result","-1");
                        Toast.makeText(getApplicationContext(), R.string.regist_exist, Toast.LENGTH_SHORT).show();
                        isSuccess.setText(R.string.regist_exist);
                        startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                        break;
                    default:
                        break;
                }
				
				super.handleMessage(msg);
			}
        	
        };
        
    }
    
    private void init() {
    	username = (EditText) findViewById(R.id.username);
    	password = (EditText) findViewById(R.id.password);
    	isSuccess =  (TextView) findViewById(R.id.isSuccess);
    	register = (Button) findViewById(R.id.submit);
    }

    public void register() {
    	HttpClient client = new DefaultHttpClient();
    	HttpPost request = new HttpPost("http://10.108.217.56/myown/register.php");
    	try {
//        	JSONObject params = new JSONObject();
//			params.put("username", username.getText().toString());
//	    	params.put("password", password.getText().toString());
//	    	
//	    	StringEntity entity = new StringEntity(params.toString());
//	    	request.setEntity(entity);
	    	
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("param", "post"));
    		params.add(new BasicNameValuePair("username", username.getText().toString()));
    		params.add(new BasicNameValuePair("password", password.getText().toString()));
    		
    		request.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
	    	
	    	HttpResponse response = client.execute(request);
	    	if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	    		result = EntityUtils.toString(response.getEntity());
	    		Log.e(TAG+":register():result", result);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
