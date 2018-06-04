package zfp.activity;

import zfp.mycar.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**进程序后第一个访问的类,三个选项.配置,测试和连接
 * @author zfp
 *
 *
 *
 */
public class Main extends AppCompatActivity  {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide(); getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		//=========================================
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏代码
		setMainBtnAffairs();//配置按钮功能
//		loadActivity(Main.this, Set.class);//载入set的Activity-仅测试
//		loadActivity(Main.this, Ctr.class);//载入ctr的Activity-仅测试
//		loadActivity(Main.this, TestAct.class);//载入新的Activity-仅测试

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//================================================================================
 
	
	/**载入其他Activity的通用方法
	 * @param context 当前上下文
	 * @param c 要跳转到哪个Activity类
	 */
	private void loadActivity(Context context,Class<?> c){
		Intent intent=new Intent();	     		 
        intent.setClass(context,c);
	    startActivity(intent);
	    overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out); //随便加的切换效果.请无视它
	}
	
	
	/**
	 * 配置按钮功能
	 */
	private void setMainBtnAffairs() {
		// 主界面按钮
		final Button btnConn = (Button) this.findViewById(R.id.btnConn);//进入控制小车界面的按钮
		final Button btnSet = (Button) this.findViewById(R.id.btnSet);//进入配置
		final Button  imageButtonTest =(Button)this.findViewById(R.id.btnLoadTest);//进入测试
		//final ImageButton imageButtonTest = (ImageButton) this.findViewById(R.id.btnLoadTest);//进入测试
		btnConn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Ctr.class);//载入控制小车界面

			}
		});
		btnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadActivity(Main.this, Set.class);//载入配置界面,注意配置界面没有写好.
				
			}
		});
		
		imageButtonTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				loadActivity(Main.this, TestAct.class);//载入测试界面
			}
		});
		
		
		
	}
	
	
}
