package passwordcom.mima;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private EditText edit_menpaihao;
    private EditText edit_userID;
    private EditText edit_time;
    private RadioGroup group_time;
    private RadioGroup group_fugaipassword;
    private RadioButton radioButton_time;
    private RadioButton radioButton_secret;

    private TextView tv_jiamiqian;
    private TextView tv_shunxu;


    private EditText edit_password;

    private Button btn_creatsecret;

    private String menpaihao;
    private String userID;
    private String userTime;

    private int radioButton_time_value = 0;//单选按钮：按日结算值为1，按时结算值为0（单选按钮默认选中的是按小时结算）
    private int radioButton_secret_value = 0;//单选按钮：覆盖之前的密码值为1，不覆盖之前的密码的值为0（默认选中的是不覆盖之前的密码）

    private String TAG = "MainActivity";
    private Context context;

    private List<String> list;

    private int RECYCLE_NUMBER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        list = new ArrayList<String>();
        initView();

    }

    private void initView() {
        tv_jiamiqian = (TextView) findViewById(R.id.tv_jiami);
        tv_shunxu = (TextView) findViewById(R.id.tv_shunxu);


        edit_menpaihao = (EditText) findViewById(R.id.edit_menpaihao);
        edit_userID = (EditText) findViewById(R.id.edit_userID);
        edit_time = (EditText) findViewById(R.id.edit_userTime);

        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_password.setEnabled(false);

        group_fugaipassword = (RadioGroup) findViewById(R.id.group_fugaiPassword);
        group_time = (RadioGroup) findViewById(R.id.group_time);

        radioButton_time = (RadioButton) group_time.findViewById(group_time.getCheckedRadioButtonId());
        radioButton_secret = (RadioButton) group_fugaipassword.findViewById(group_fugaipassword.getCheckedRadioButtonId());

        group_fugaipassword.setOnCheckedChangeListener(listener);
        group_time.setOnCheckedChangeListener(listener);

        btn_creatsecret = (Button) findViewById(R.id.btn_creatSecret);
    }

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            radioGroup.getId();

            switch (radioGroup.getId()) {
                case R.id.group_fugaiPassword:

                    radioButton_secret = (RadioButton) findViewById(checkId);
                    String mess = radioButton_secret.getText() + "";

                    if (checkId == R.id.group_rb_bufugai) {
                        radioButton_secret_value = 0;
                    } else {
                        radioButton_secret_value = 1;
                    }
//                    Toast.makeText(getApplicationContext(), radioButton_secret_value+"", Toast.LENGTH_SHORT).show();
//                    edit_password.setText(mess);
                    break;
                case R.id.group_time:
                    radioButton_time = (RadioButton) findViewById(checkId);
                    String mess2 = radioButton_time.getText() + "";

                    if (checkId == R.id.group_rb_hour) {
                        radioButton_time_value = 0;
                    } else {
                        radioButton_time_value = 1;
                    }
//                    Toast.makeText(getApplicationContext(), radioButton_time_value+"", Toast.LENGTH_SHORT).show();
//                    edit_password.setText(mess2);
                    break;
            }
        }
    };

    public void click(View view) {
        initData();
    }

    private void print(){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<oldsecret.length;i++){
            Log.i("MainActivity","----------"+oldsecret[i]+",");
            sb.append(oldsecret[i]);
        }
        tv_jiamiqian.setText(sb.toString());
    }


    private int[] oldsecret;


    private void initData() {
        menpaihao = edit_menpaihao.getText()+"";
        userID = edit_userID.getText()+"";
        userTime = edit_time.getText()+"";
        String message = "输入不能为空";

        if(menpaihao.equals("")){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            return;
        }else{
            if(menpaihao.length() != 4){
                Toast.makeText(context,"请输入一个四位数",Toast.LENGTH_LONG).show();
                return;
            }
        }

        if(userID.equals("")){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            return;
        }else{
            int in = Integer.parseInt(userID);
            if(in < 10000 || in > 99999){
                Toast.makeText(context,"请输入一个五位数",Toast.LENGTH_LONG).show();
                return;
            }
        }

        if(userTime.equals("")){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            return;
        }else{
            if(userTime.length() > 2){
                Toast.makeText(context,"最多输入两位数",Toast.LENGTH_LONG).show();
                return;
            }
        }

        oldsecret = new int[8];

        String s_menpai_12 = menpaihao.substring(0,2);
        String s_userId_12 = userID.substring(0,2);
        int new_45 = (Integer.parseInt(s_menpai_12))^(Integer.parseInt(s_userId_12));
        if(new_45 < 10){
            oldsecret[4] = 0;
            oldsecret[5] = new_45;
        }else{
            oldsecret[4] = new_45/10;
            oldsecret[5] = new_45%10;
        }

        String s_menpai_34 = menpaihao.substring(2,4);
        String s_userId_34 = userID.substring(2,4);

        int new_67 = (Integer.parseInt(s_menpai_34))^(Integer.parseInt(s_userId_34));
        if(new_67 < 10){
            oldsecret[6] = 0;
            oldsecret[7] = new_67;
        }else{
            oldsecret[6] = new_67/10;
            oldsecret[7] = new_67%10;
        }


        int i_userTime = Integer.parseInt(userTime);
        if(radioButton_time_value == 0){//单选按钮：按日结算值为1，按时结算值为0（编辑框输入的最大值是23）
            if (i_userTime >23){
                Toast.makeText(MainActivity.this,"输入的小时不能超过23小时",Toast.LENGTH_LONG).show();
                return;
            }
            if (radioButton_secret_value == 0){//单选按钮：覆盖之前的密码值为1，不覆盖之前的密码的值为0（默认选中的是不覆盖之前的密码）
                oldsecret[0] = 7;
            }else{
                oldsecret[0] = 3;
            }


        }else  if(radioButton_time_value == 1){//单选按钮：按日结算值为1，按时结算值为0（编辑框输入的最大值是23）
            if (i_userTime >95){
                Toast.makeText(MainActivity.this,"输入的天数不能超过95小时",Toast.LENGTH_LONG).show();
                return;
            }
            if (radioButton_secret_value == 0){//单选按钮：覆盖之前的密码值为1，不覆盖之前的密码的值为0（默认选中的是不覆盖之前的密码）
                oldsecret[0] = 5;
            }else{
                oldsecret[0] = 1;
            }
        }


        if(i_userTime<10){
            oldsecret[1] = oldsecret[0];
        }else{
            int m = i_userTime/10;//这是十位
            int n = i_userTime%10;//这是个位
            if(m>n){//十位大于个位
                oldsecret[1] = oldsecret[0]+1;
            }else{
                oldsecret[1] = oldsecret[0];
            }
        }

        userID = edit_userID.getText()+"";
        String end = userID.substring(4);
        int endNum = Integer.parseInt(end);

        int time = i_userTime^endNum;
        if(time<10){
            oldsecret[2] = 0;
            oldsecret[3] = time;
        }else{
            oldsecret[2] = time/10;
            oldsecret[3] = time%10;
        }


        print();
        String newPassword = changSecret(oldsecret);

        checkPassword(newPassword);

    }

    /**
     * 产生1~7的随机数
     * @return
     */
    private int random(){
        return (int)(Math.random()*7+1);
    }

    private String changSecret(int[] old){
        List<Integer> list = new ArrayList<Integer>();
        int[] newpasswor = new int[8];
        StringBuffer sb = new StringBuffer();
        int flag = 0;//获取随机数循环的次数
        while(true){
            int temp = random();
            if(flag==0){
                newpasswor[0] = temp;
                sb.append(temp);
            }
            flag++;

            if(!list.contains(temp)){
                list.add(temp);
            }
            if(list.size()>=7){
                break;
            }
        }

        tv_shunxu.setText(list.toString());

        for(int i=0;i<list.size();i++){
            newpasswor[i+1] = old[list.get(i)];
            sb.append(newpasswor[i+1]);
        }
        return sb.toString();
    }

    /**
     * 让生成的密码在number次内不重复
     * @param newpassword 生成的新密码
     * @param number      超过number次可以重复
     */
    private void checkPassword(String newpassword){
        int temp = list.size();
        if(list.contains(newpassword)){
            for(int i=0;i<temp;i++){
                String value = list.get(i);
                if (newpassword.equals(value)){
                    if (temp - i < RECYCLE_NUMBER){
                        initData();
                    }
                }
            }
        }else{
            list.add(newpassword);
            edit_password.setText(newpassword);
        }

    }

}
