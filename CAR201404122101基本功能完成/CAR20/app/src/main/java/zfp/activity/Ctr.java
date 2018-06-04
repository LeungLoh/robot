package zfp.activity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


import zfp.config.PaintVedio;
import zfp.mycar.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.value;
import static android.R.string.ok;
import static zfp.config.PaintVedio.jpurl;
import static zfp.mycar.R.drawable.route;
import static zfp.mycar.R.layout.ctr;
import static zfp.mycar.R.mipmap.a;
import static zfp.mycar.R.mipmap.b;
import static zfp.mycar.R.mipmap.e;


//import static android.R.attr.delay;
//import static java.security.AccessController.getContext;
//import static zfp.mycar.R.id.icon;
//import static zfp.mycar.R.id.iv3;
//import static zfp.mycar.R.id.iv8;

/**小车控制类
 * @author zfp
 *
 */
public class Ctr extends AppCompatActivity{

	private Socket socket = null;
	// 指令发出 数据缓存
	private static PrintWriter printWriter = null;
	private static BufferedReader mBufferedReaderClient = null;//把内容放进来后可以用来判断连接是否异常,不必要就给注释了

	private boolean isConnect = false;//是否连接小车.用于销毁时处理事件的判断条件
	private Thread thread = null;//控制小车线程
	private Thread recthread = null;//接受信息线程
	private String recvMessageClient;//接受的数据数组
	private RadioButton radioConn;//连接控制按钮的变量,只是为了以后方便
	private Button btnForward;//前
	private Button btnBack;//后
	private Button btnLeft;//左
	private Button btnRight;//右
	private Button btnjp;//截屏
	private Button btnLearn;//完成学习按钮
	private Button btnLroll;//向左转舵机按钮
	private Button btnRroll;//向右转舵机按钮
	private int rollMain = 2;

	private Button btntest;//测试按钮
	private TextView modeText;//模式文本
	private LinearLayout qiti;//毒气文本
	private LinearLayout renming;//生命文本
	private RadioGroup startor;//开始自动
	private RadioButton startBtn;//开始
	private RadioButton stopBtn;//暂停
	private String commendstr = "e" ;
	private String tttips = "停";
	private int numjpeg=1;
	private FrameLayout content_main;
	private ImageView iv8_jieping;
	private Boolean flagAuto = false;
	//    private ImageView iv7;
	//    private ImageView iv6;
	//    private ImageView iv5;
	private ImageView iv4;
	private ImageView iv3_kaideng;
	//    private ImageView iv2;
	private ImageView iv1;
	private List<ImageView> imageViews = new ArrayList<>();
	Timer timer;
	private final String TAG = "CircleMenu";
	private final int radius1 = 150;
	static int routeWhich = 0;
	private String[] routes = new String[4];
	private Queue<String> route_1 = new LinkedList<String>();
	private  Queue<String> route_2 = new LinkedList<String>();
	private  Queue<String> route_3 = new LinkedList<String>();
	private  Queue<String> route_4 = new LinkedList<String>();
	private  Queue<String> route_all = new LinkedList<String>();
	private  Queue<String> route_send = new LinkedList<String>();
	private  Queue<String> route_null = new LinkedList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getSupportActionBar().hide(); getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ctr);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		// =========================================
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏代码
		// SharedPreferences sp = this.getSharedPreferences("set",
		// MODE_PRIVATE);
		readRoute();
		setBtnAffairs();
		assignViews();
//		resetRoll();
//		timer = new Timer();



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// ======================================================
	/**
	 * 配置按钮功能
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void setBtnAffairs() {
		// 主界面按钮
		radioConn = (RadioButton) this.findViewById(R.id.radioConn);//点击后单选按钮后才可以控制小车

		btnForward = (Button) this.findViewById(R.id.btnForward);

		btnBack = (Button) this.findViewById(R.id.btnBack);

		btnLeft = (Button) this.findViewById(R.id.btnLeft);

		btnRight = (Button) this.findViewById(R.id.btnRight);

		btnLearn = (Button) this.findViewById(R.id.btnLearn);

		modeText = (TextView) this.findViewById(R.id.modeText);

		qiti = (LinearLayout) this.findViewById(R.id.layqiti);

		renming = (LinearLayout) this.findViewById(R.id.layrenm);

		startor = (RadioGroup) this.findViewById(R.id.radioGroup);

		startBtn = (RadioButton) this.findViewById(R.id.btnStart);

		stopBtn = (RadioButton) this.findViewById(R.id.btnStop);

		btnLroll = (Button) this.findViewById(R.id.btnleftroll);
		btnRroll = (Button) this.findViewById(R.id.btnrightroll);

//		btntest = (Button) this.findViewById(R.id.testbutton);
//
//		btntest.setOnClickListener(new View.OnClickListener(){
//			public void onClick(View v){
//
//
//			}
//		});


		btnLearn.setOnClickListener(new View.OnClickListener (){
			public void onClick(View v){
				//点击这个按钮后 将路径保存在全局变量中
				btnLearn.setVisibility(View.INVISIBLE);
				Toast.makeText(getBaseContext(), "路径"+(routeWhich+1)+"学习成果", Toast.LENGTH_SHORT).show();
				if(!route_all.equals(route_null)){
					switch (routeWhich){
						case 0:
							route_1 = route_all;
							writeRoute(0,route_1);
							break;
						case 1:
							route_2 = route_all;
							writeRoute(1,route_2);
							break;
						case 2:
							route_3 = route_all;
							writeRoute(2,route_3);
							break;
						case 3:
							route_4 = route_all;
							writeRoute(3,route_4);
							break;
					}
				}
				route_all = route_null;
			}
		});

		startor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == startBtn.getId()){
					if(route_all.equals(route_null)){
						Toast.makeText(getApplicationContext(),"你选择的路径没有记录数据",Toast.LENGTH_SHORT).show();//开始
					}
					else{
						Toast.makeText(getApplicationContext(),"开始发车咯",Toast.LENGTH_SHORT).show();//开始
						//				printWriter.print("t");
						//				printWriter.flush();
					}


				}else if(checkedId == stopBtn.getId()){
					Toast.makeText(getApplicationContext(),"停止发车咯",Toast.LENGTH_SHORT).show();//暂停
					//				printWriter.print("p");
					//				printWriter.flush();
				}
			}
		});
//		btnjp = (Button) this.findViewById(R.id.btnjp);
//		btnjp .getBackground().setAlpha(90);
		radioConn.setOnClickListener(new View.OnClickListener() {// 控制启动
					@Override
					public void onClick(View v) {
						isConnect = true;
						// 开启控制线程(注意视频连接会在进入控制界面时直接开启,这里只响应控制小车的线程)
						thread = new Thread(runnable);
						thread.start();
//						timer.schedule(task,500);
					}
				});

		btnForward.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchHandle(event, "a", "前");
				return true;
			}
		});
		btnBack.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchHandle(event, "b", "后");
				return true;
			}
		});
		btnLeft.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchHandle(event, "c", "左");
				return true;
			}
		});
		btnRight.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchHandle(event, "d", "右");
				return true;
			}
		});
		btnLroll.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				rollMain--;
				if(rollMain <= 1)
				{
					printWriter.print("1");
					printWriter.flush();
					rollMain = 1;
				}
				else{
					printWriter.print(""+rollMain);
					printWriter.flush();
				}

			}
		});
		btnRroll.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				rollMain++;
				if(rollMain >= 3){
					printWriter.print("3");
					printWriter.flush();
					rollMain = 3;
				}
				else{
					printWriter.print(""+rollMain);
					printWriter.flush();
				}

			}
		});
	}

	private void assignViews() {
		content_main = (FrameLayout) findViewById(R.id.content_main);
		iv8_jieping = (ImageView) findViewById(R.id.iv8_jieping);
		imageViews.add(iv8_jieping);
//      iv7 = (ImageView) findViewById(R.id.iv7);
//      imageViews.add(iv7);
//      iv6 = (ImageView) findViewById(R.id.iv6);
//      imageViews.add(iv6);
//      iv5 = (ImageView) findViewById(R.id.iv5);
//      imageViews.add(iv5);
		iv4 = (ImageView) findViewById(R.id.iv4);
		imageViews.add(iv4);
		iv3_kaideng = (ImageView) findViewById(R.id.iv3_kaideng);
		imageViews.add(iv3_kaideng);
//      iv2 = (ImageView) findViewById(R.id.iv2);
//      imageViews.add(iv2);
		iv1 = (ImageView) findViewById(R.id.iv1);
	}

	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	public void onClick(View v) {
		if (v.getId() == iv1.getId()) {
			Boolean isShowing = (Boolean) iv1.getTag();
			if (null == isShowing || isShowing == false) {
				ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 0, 45);
				objectAnimator.setDuration(500);
				objectAnimator.start();
				iv1.setTag(true);
				showSectorMenu();
			} else {
				iv1.setTag(false);
				ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 45, 0);
				objectAnimator.setDuration(500);
				objectAnimator.start();
				closeSectorMenu();
			}
		}
		else if(v.getId() == iv8_jieping.getId())
		{
			getImg();
		}
/**
* 模式选择按钮
*
 **/
		else if(v.getId() == iv3_kaideng.getId())
		{

			flagAuto = !flagAuto;
// 自动模式下，route_all就被赋值了所选择的路径队列
			//下位机传来查询字符？ route_all就把该队列传给route_send ，当route_send空了之后
			if(flagAuto) {

				modeText.setText("自动模式");
				modeText.setTextColor(Color.WHITE);
				startor.setVisibility(View.VISIBLE);
				new AlertDialog.Builder(this)
						.setTitle("请选择路径")
						.setIcon(route)
						.setSingleChoiceItems(new String[] {"路径1","路径2","路径3","路径4"}, routeWhich,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										switch (which){
											case 0:
												Ctr.routeWhich = which;
												route_all = route_1;
												break;
											case 1:
												Ctr.routeWhich = which;
												route_all = route_2;
												break;
											case 2:
												Ctr.routeWhich = which;
												route_all = route_3;
												break;
											case 3:
												Ctr.routeWhich = which;
												route_all = route_4;
												break;

										}
										dialog.dismiss();
									}
								}
						)
						.setNegativeButton("取消", null)
						.show();
				printWriter.print("z");
				printWriter.flush();


			}
			else {

				modeText.setText("手动模式");
				modeText.setTextColor(Color.BLUE);
				stopBtn.setChecked(true);
				route_all = route_null;//手动模式下将routeall清空
				startor.setVisibility(View.INVISIBLE);
				printWriter.print("s");
				printWriter.flush();

			}


		}
/**
* 开始路径学习按钮
*
 **/
		else if(v.getId() == iv4.getId())
		{
//
			route_all = route_null;
			new AlertDialog.Builder(this)
					.setTitle("请选择路径")
					.setIcon(route)
					.setSingleChoiceItems(new String[] {"路径1","路径2","路径3","路径4"}, routeWhich,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									switch (which){
										case 0:
											Ctr.routeWhich = which;
//											route_all = route_1; 这个好像是不必要的
											break;
										case 1:
											Ctr.routeWhich = which;
//											route_all = route_2;
											break;
										case 2:
											Ctr.routeWhich = which;
//											route_all = route_3;
											break;
										case 3:
											Ctr.routeWhich = which;
//											route_all = route_4;
											break;

									}
									dialog.dismiss();
								}
							}
					)
					.setNegativeButton("取消", null)
					.show();
//			Log.v("msg",""+routeWhich);
			btnLearn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 显示扇形菜单
	 */
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	private void showSectorMenu() {
		/***第一步，遍历所要展示的菜单ImageView*/
		for (int i = 0; i < imageViews.size(); i++) {
			PointF point = new PointF();
			/***第二步，根据菜单个数计算每个菜单之间的间隔角度*/
			int avgAngle = (90 / (imageViews.size() - 1));
			/**第三步，根据间隔角度计算出每个菜单相对于水平线起始位置的真实角度**/
			int angle = avgAngle * i;
			Log.d(TAG, "angle=" + angle);
			/**
			 * ﻿﻿
			 * 圆点坐标：(x0,y0)
			 * 半径：r
			 * 角度：a0
			 * 则圆上任一点为：（x1,y1）
			 * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
			 * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
			 */
			/**第四步，根据每个菜单真实角度计算其坐标值**/
			point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius1;
			point.y = (float) -Math.sin(angle * (Math.PI / 180)) * radius1;
			Log.d(TAG, point.toString());

			/**第五步，根据坐标执行位移动画**/
			/**
			 * 第一个参数代表要操作的对象
			 * 第二个参数代表要操作的对象的属性
			 * 第三个参数代表要操作的对象的属性的起始值
			 * 第四个参数代表要操作的对象的属性的终止值
			 */
			ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews.get(i), "translationX", 0, point.x);
			ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews.get(i), "translationY", 0, point.y);
			/**动画集合，用来编排动画**/
			AnimatorSet animatorSet = new AnimatorSet();
			/**设置动画时长**/
			animatorSet.setDuration(500);
			/**设置同时播放x方向的位移动画和y方向的位移动画**/
			animatorSet.play(objectAnimatorX).with(objectAnimatorY);
			/**开始播放动画**/
			animatorSet.start();
		}
	}


	/**
	 * 关闭扇形菜单
	 */
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	private void closeSectorMenu() {
		for (int i = 0; i < imageViews.size(); i++) {
			PointF point = new PointF();
			int avgAngle = (90 / (imageViews.size() - 1));
			int angle = avgAngle * i;
			Log.d(TAG, "angle=" + angle);
			point.x = (float) Math.cos(angle * (Math.PI / 180)) * radius1;
			point.y = (float) -Math.sin(angle * (Math.PI / 180)) * radius1;
			Log.d(TAG, point.toString());

			ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageViews.get(i), "translationX", point.x, 0);
			ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageViews.get(i), "translationY", point.y, 0);
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.setDuration(200);
			animatorSet.play(objectAnimatorX).with(objectAnimatorY);
			animatorSet.start();
		}
	}
	/**
	 * @param event
	 */
	private void touchHandle(MotionEvent event, String orderStr, String tips) {

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
//			commendstr = orderStr;
//			tttips = tips;
			printWriter.print(orderStr);
			printWriter.flush();
			break;
		case MotionEvent.ACTION_UP:
//			commendstr = "e";
//			tttips = "停";
			printWriter.print("e");
			printWriter.flush();
			break;
		default:
			break;
		}
	}


//	TimerTask task = new TimerTask(){
//		public void run(){
//			ctrOrder(commendstr, tttips);
//		}
//	};
	/**
	 * 给小车发送指令,
	 * 
	 * @param orderStr
	 *            发"a",则单片机接收到"a"
	 * @param tips
	 *            提示
	 */
	private void ctrOrder(String orderStr, String tips) {
				 //执行的方法
				printWriter.print(orderStr);
				printWriter.flush();

		// if(tips != null && !"".equals(tips)){
		// Tools.tips(Ctr.this, tips);
		// }

	}

	// ============================================
	// 线程mRunnable启动
	private Runnable runnable = new Runnable() {
		public void run() {
			try {
				socket = new Socket("192.168.8.1",2001);

			} catch (Exception e){
				Toast.makeText(getApplicationContext(),"e1失败连接",Toast.LENGTH_SHORT).show();

			}
			if(socket.isConnected()){
				try{
					OutputStream os = socket.getOutputStream();
					printWriter = new PrintWriter(os, true);
				}
				catch (Exception e2){
					Toast.makeText(getApplicationContext(),"e2"+e2.getMessage(),Toast.LENGTH_SHORT).show();
					if(socket != null){
						try {
							socket.close();
							if(printWriter != null){
								printWriter = null;
							}

						}
						catch (Exception e3){
							Toast.makeText(getApplicationContext(),"e3"+e3.getMessage(),Toast.LENGTH_SHORT).show();
						}

					}
				}
				try{
					recthread = new Thread(recvRunnable);
					recthread.start();
				}
				catch (Exception e4){
					Toast.makeText(getApplicationContext(),"e4"+e4.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}

//			try {
//				socket = new Socket(); //小车ip,端口
//				InetSocketAddress e = new InetSocketAddress("192.168.8.1", 2001);
//				socket.connect(e,500);
//				OutputStream os = socket.getOutputStream();
//				printWriter = new PrintWriter(os, true);
//				Toast.makeText(getApplicationContext(),"成功连接",Toast.LENGTH_SHORT).show();//出现岔路口
////				recthread = new Thread(recvRunnable);
////				recthread.start();
//
//			} catch (Exception e) {
////				Tools.tips(Ctr.this, "连接错误,请检查网络");
//				Toast.makeText(getApplicationContext(),"失败连接",Toast.LENGTH_SHORT).show();
//				return;
//			}

		}
	};

	private Runnable recvRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String es = null;
				mBufferedReaderClient = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
				while((es= mBufferedReaderClient.readLine()) != null)
				{
					if(es != "")
					{
						recvMessageClient = es;
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);

					}
				}

			} catch (Exception e) {

//				Toast.makeText(getApplicationContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessage(msg);
				e.printStackTrace();
				try{
					mBufferedReaderClient.close();
					socket.close();
				}
				catch (IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
	};
	//添加用来处理接受数据的
	Handler mHandler = new Handler()
	{

		public void handleMessage(Message msg)
		{
//			Queue<String> queue = new LinkedList<String>();
//			Map<String, Object> map=new HashMap<String, Object>();
			super.handleMessage(msg);
			if (msg.what == 0)
			{
//				Toast.makeText(getApplicationContext(),recvMessageClient,Toast.LENGTH_SHORT).show();//刷新消息机制
//				printWriter.print("sorry_man");
//				printWriter.flush();
			}
			else if(msg.what == 1 )
			{
//				Toast.makeText(getApplicationContext(),recvMessageClient,Toast.LENGTH_SHORT).show();//刷新消息机制
//				printWriter.print("ok");
//				printWriter.flush();
				try
				{
					//格式为： &a=b&x=1
					String[] results = recvMessageClient.split("&");//这里就用来刷新显示
					//接下来我要处理数据啦
					for(int i=1;i<results.length;i++){
						String[] mapVal = results[i].split("=");
						String key = mapVal[0];
						String val = mapVal[1];
//						queue.offer("&");
//						queue.offer(key);
//						queue.offer("=");
//						queue.offer(val);
						//这里开始就要判断传来的数据啦
						switch (key){
							//
							case "x":
//								Toast.makeText(getApplicationContext(),"出现岔路口",Toast.LENGTH_SHORT).show();//出现岔路口
								switch (val){
									case "1":
//										Toast.makeText(getApplicationContext(),"左边有条岔路口",Toast.LENGTH_SHORT).show();//出现岔路口
//										showAlert(Ctr.this);
										new AlertDialog.Builder(Ctr.this)
												.setTitle("请选择方向")
												.setIcon(R.drawable.left_route)
//												.setView(R.drawable.left_route)
												.setPositiveButton("左转", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
//														Toast.makeText(getBaseContext(), "左路",Toast.LENGTH_LONG).show();
														route_all.offer("1");
													}
												})
												.setNegativeButton("直行", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
//														Toast.makeText(getBaseContext(), "直行",Toast.LENGTH_LONG).show();
														route_all.offer("2");
													}
												})
												.show();
										break;
									case "2":
//										Toast.makeText(getApplicationContext(),"右边有条岔路口",Toast.LENGTH_SHORT).show();//出现岔路口
										new AlertDialog.Builder(Ctr.this)
												.setTitle("请选择方向")
												.setIcon(R.drawable.right_route)
//												.setView(R.drawable.right_route)
												.setPositiveButton("右转", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
														Toast.makeText(getBaseContext(), "右转",Toast.LENGTH_LONG).show();
														route_all.offer("3");
													}
												})
												.setNegativeButton("直行", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
														Toast.makeText(getBaseContext(), "直行",Toast.LENGTH_LONG).show();
														route_all.offer("2");
													}
												})
												.show();
										break;
									case "3":
//										Toast.makeText(getApplicationContext(),"两边都有岔路口",Toast.LENGTH_SHORT).show();//出现岔路口
										new AlertDialog.Builder(Ctr.this)
												.setTitle("请选择方向")
												.setIcon(R.drawable.three_route)
//												.setIcon(R.drawable.branch)
//												.setView(R.drawable.three_route)
												.setPositiveButton("左转", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
//														Toast.makeText(getBaseContext(), "左路",Toast.LENGTH_LONG).show();
														route_all.offer("1");
													}
												})
												.setNegativeButton("直行", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
//														Toast.makeText(getBaseContext(), "直行",Toast.LENGTH_LONG).show();
														route_all.offer("2");
													}
												})
												.setNeutralButton("右转", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialogInterface, int i) {
//														Toast.makeText(getBaseContext(), "右转",Toast.LENGTH_LONG).show();
														route_all.offer("3");
													}
												})
												.show();
										break;
									case "?":
										String send_k = "";
										if(route_all.isEmpty()){
											new AlertDialog.Builder(Ctr.this)
													.setTitle("还没有设置该路径")
													.setIcon(R.drawable.branch)
													.setView(R.drawable.left_route)
													.setPositiveButton("知道了",null)
													.show();
										}
										else{
											//这个地方有点小诀窍，假设某路径还没有走完的话就被改变了路径，他会把当前路径走完后才开始下一个路径
											if(route_send.isEmpty()){
												route_send = route_all;
												send_k = route_send.poll();
//												printWriter.print(send_k);
//												printWriter.flush();
											}
											else {
												send_k = route_send.poll();
//												printWriter.print(send_k);
//												printWriter.flush();
											}
										}
										break;


									default:
										Toast.makeText(getApplicationContext(),"你丫数据又传错了，只有123",Toast.LENGTH_SHORT).show();//出现岔路口
								}
								break;
							case "r":
								if(val.contains("1"))
								{
//									Toast.makeText(getApplicationContext(),"有人出现",Toast.LENGTH_SHORT).show();//出现人
									renming.setVisibility(View.VISIBLE);
//									printWriter.print("ok man");
//									printWriter.flush();
								}
								else{
									renming.setVisibility(View.INVISIBLE);
								}
								break;
							case "q":
								if(val.contains("1"))
								{
//									Toast.makeText(getApplicationContext(),"有气体出现",Toast.LENGTH_SHORT).show();//出现气体
									qiti.setVisibility(View.VISIBLE);
//									printWriter.print("ok man");
//									printWriter.flush();
								}
								else{
									qiti.setVisibility(View.INVISIBLE);
								}
								break;
							default:
								Toast.makeText(getApplicationContext(),"你传入的是什么数据，我不知道类型",Toast.LENGTH_SHORT).show();//出现气体
						}
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	};
/**
*没使用该方法接收数据了
 **/
	public void setRecvMessage(){
		char[] buffer = new char [256];
		int count = 0;
		String es = null;

			try{
				while((es = mBufferedReaderClient.readLine()) != null)
				{
						recvMessageClient = es;
						Thread.sleep(300);
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
						Thread.sleep(200);
				}
			}
			catch (Exception e)
			{
				recvMessageClient = "接收异常:" + e.getMessage() + "\n";//消息换行
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}

	}

	private String getInfoBuff(char[] buff, int count)
	{
		char[] temp = new char[count];
		for(int i=0; i<count; i++)
		{
			temp[i] = buff[i];

		}

		return new String(temp);
	}


	public void onDestroy() {//视频占用的资源会在退出此类时自动销毁,配置在surfaceview的子类PaintVedio中,这里只端口小车的控制连接
		super.onDestroy();
		if (isConnect) {
			isConnect = false;
			try {
				if (socket != null) {
					socket.close();
					socket = null;
					printWriter.close();
					printWriter = null;
					mBufferedReaderClient.close();
					mBufferedReaderClient = null ;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			recthread.interrupt();
			thread.interrupt();
			timer.cancel();
		}

	}

	/**
		*截图，保存资源
		* 无参数
		* 就是这样 恩
	 **/
//	public void getImg()
//	{
//
//		String state = Environment.getExternalStorageState();//如果状态不是mounted，无法读写
//		if (!state.equals(Environment.MEDIA_MOUNTED)) {
//			Toast.makeText(this, "无法访问内存卡，请设置权限", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/gong");
//		if(!file.exists()){
//
//			file.mkdirs();
//			Toast.makeText(this, "目录创建成功", Toast.LENGTH_SHORT).show();
//		}
//
//		try {
//
//			Bitmap urlPath = jpurl;
//			if(urlPath == null)
//			{
//				Toast.makeText(this, "保存失败，没有可用资源", Toast.LENGTH_SHORT).show();
//			}
//			else
//			{
//				File fil = new File(file.getPath()+"/" + "test"+".png");
//				if(!fil.exists()){
//
//					fil.createNewFile();
//					Toast.makeText(this, "二级文件创建成功", Toast.LENGTH_SHORT).show();
//				}
////				FileOutputStream fos = new FileOutputStream(file.getPath() + "/prtsc_"+String.valueOf(numjpeg)+".png");
//				FileOutputStream fos = new FileOutputStream(fil.getPath());
//				numjpeg++;
//				urlPath.compress(Bitmap.CompressFormat.PNG, 100, fos);
//				fos.flush();
//				fos.close();
//				Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//			}
//		}
//		catch (Exception e)
//		{
//			Toast.makeText(this, "呀，出错啦"+e.getMessage(), Toast.LENGTH_SHORT).show();
//			e.printStackTrace();
//		}
//	}

	public void getImg()
	{

		String state = Environment.getExternalStorageState();//如果状态不是mounted，无法读写
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "无法访问内存卡，请设置权限", Toast.LENGTH_SHORT).show();
			return;
		}
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/finder/");
		if(!file.exists())
			file.mkdirs();
		try {

			Bitmap urlPath = jpurl;
			if(urlPath == null)
			{
				Toast.makeText(this, "保存失败，没有可用资源", Toast.LENGTH_SHORT).show();
			}
			else
			//FileOutputStream fos = new FileOutputStream(file.getPath()+String.valueOf(numjpeg)+".png");
			{
				FileOutputStream fos = new FileOutputStream(file.getPath() + "/prtsc_"+String.valueOf(numjpeg)+".png");
				numjpeg++;
				urlPath.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, "呀，出错啦", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	public  void creatRoute(){
		String state = Environment.getExternalStorageState();//如果状态不是mounted，无法读写
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "无法访问内存卡，请设置权限", Toast.LENGTH_SHORT).show();
		}
		for(int i=0;i<4;i++){
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/finder/"+"route_"+i+".txt");
			if(file.exists()){
				Toast.makeText(this, "存在哦", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(this, "不存在哦，但是给你创建了。", Toast.LENGTH_SHORT).show();
				try{
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()),"GB2312"));
					String s = "";
					out.write(s);
					out.flush();
					out.close();
				}
				catch (Exception e)
				{
					Toast.makeText(this, "呀，出错啦", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}


			}
		}
	}
	/**
	 *将设置的路径参数保存在本地
	 * whichRoute ：路径索引值
	 * queue 路径值队列
	 **/
	public void writeRoute(int whichRoute,Queue queue){
		Queue <String> route = queue;
		String s="route=";
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/finder/"+"route_"+whichRoute+".txt");
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()),"GB2312"));
			while (!route.isEmpty()){
				s = s + route.poll();
				if(!route.isEmpty())
					s= s+"=";
			}
			out.write(s);
			out.flush();
			out.close();

		}
		catch (Exception e){
			Toast.makeText(this, "呀，写入路径出错啦", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 *将保存在本地的路径参数读取
	 *
	 **/
	public void readRoute(){
		String state = Environment.getExternalStorageState();//如果状态不是mounted，无法读写
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "无法访问内存卡，请设置权限", Toast.LENGTH_SHORT).show();
		}
		for(int i=0;i<4;i++){
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/finder/"+"route_"+i+".txt");
			try{
				if(file.exists()){
					BufferedReader in = new BufferedReader(new FileReader(file));
					String route;
					try{
							while((route = in.readLine()) != null){
								routes[i] += route;
							}
					}
					catch (Exception e){
						System.out.println("文件写入失败！！！");
						Toast.makeText(this, "呀，文件写入失败啦，这里报错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}
			catch (Exception e){
				Toast.makeText(this, "呀，无法读取路径啦，这里报错", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}
		if(routes[0] != null) route_1 = splitString(routes[0]);
		if(routes[1] != null) route_2 = splitString(routes[1]);
		if(routes[2] != null) route_3 = splitString(routes[2]);
		if(routes[3] != null) route_4 = splitString(routes[3]);
	}
	/**
	 *分隔路径参数字符串，然后将其入队列
	 *route 从本地读取的路径参数字符串
	 * 返回的是一个路径参数队列
	 **/

	public Queue<String> splitString(String route){
		String[] s;
		Queue<String> dui = new LinkedList<>();
		s=route.split("=");
		for(int i=1;i<s.length;i++){
			dui.offer(s[i]);
		}
		return dui;
	}

}

