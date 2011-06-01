package com.anim.list;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen extends Activity {
	private Button Search; 
	private String status;
	private String[] getWord = new String[2];
	private TextView kket, word1, word2;
	//String[] menu = {"Search", "Quiz"};		
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splashscreen);
      
      word1 = (TextView) findViewById(R.id.word1);
      word2 = (TextView) findViewById(R.id.word2);
      Search = (Button) findViewById(R.id.enter);
      
      //AdView ads = new AdView(this, AdSize.BANNER, "a14db3c224c8361");
      AdView adview = (AdView)findViewById(R.id.adView);
      AdRequest re = new AdRequest();
      re.setTesting(false);
      re.setGender(AdRequest.Gender.FEMALE);
      adview.loadAd(re);      
      
      kket = (TextView) findViewById(R.id.Ket);
      Typeface face=Typeface.createFromAsset(getAssets(),"fonts/1942.ttf");
      kket.setTypeface(face);
      word1.setTypeface(face);
      
      KamusDbAdapter dbHelper = new KamusDbAdapter(getApplicationContext());
      dbHelper.open();
      status = dbHelper.getstatedb();
      if (status==null){
    	  status="en";
      }
      getWord = dbHelper.getwordday(status);
      word1.setText(getWord[0]);
      word2.setText(getWord[1]);
      dbHelper.close();
      
      Search.setOnClickListener(new OnClickListener(){
    	@Override
		public void onClick(View arg0) {
			startActivity();
		}
      });
      
      /*          
      ArrayAdapter<String> menuList = new ArrayAdapter<String>(this, R.layout.row3, menu);
 	     mList2.setAdapter(menuList);
 	     mList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 	    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v("ITEM",String.valueOf(arg2));
				if(arg2==0){
					startActivity();
				}
				if(arg2==1){
					//startActivity2();
				}
			}
		});*/
 	}
	
	
	private void startActivity() {
		Intent next = new Intent();
        next.setClass(this, SuitAuto.class);
        startActivity(next);
        this.finish();
	}

}
