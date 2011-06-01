package com.anim.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class SuitAuto extends Activity implements TextWatcher, OnInitListener{
	private AutoCompleteTextView auto; 
	private ListView history;
	private Button right,left;
	static String selection;
	String[]kwantung = new String[2];
	String[] kook = new String[2];
	Cursor cur;	int cc;
	TextToSpeech tts;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        tts = new TextToSpeech(this, this);
        
        //Our search suggestion using AutoCompleteTextView object
        auto = (AutoCompleteTextView)findViewById(R.id.auto);
        auto.addTextChangedListener(this);
        auto.setClickable(true);
        //list below the search box
        history = (ListView)findViewById(R.id.history);
        history.setCacheColorHint(0);
        history.setDivider(null);
        history.setItemsCanFocus(true);
        
        //Initialize our selection buttons
        right = (Button)findViewById(R.id.right);
        left = (Button)findViewById(R.id.left);
        
        //Our ads
        AdView adview = (AdView)findViewById(R.id.adView);
        AdRequest re = new AdRequest();
        re.setTesting(false);
        re.setGender(AdRequest.Gender.FEMALE);
        adview.loadAd(re);
        
        //get language status from db
        final KamusDbAdapter dbHelper = new KamusDbAdapter(getApplicationContext());
        dbHelper.open();
        String status = dbHelper.getstatedb();
        selection = status;
        dbHelper.close();
		
        //case if any language have been selected already                
		if (selection.equalsIgnoreCase("en")){
			selection = "en";
			startAdding();
			left.setBackgroundResource(R.drawable.enterp);
		} else {
			selection = "ina";
			startAdding();
			right.setBackgroundResource(R.drawable.enterp);
		}
		
		//right selection Button
		right.setOnClickListener(new OnClickListener(){
        	@Override
			public void onClick(View v) {
        		selection = "ina";
				updateDb();
				startAdding();
				right.setBackgroundResource(R.drawable.enterp);
				left.setBackgroundResource(R.drawable.enter);
			}
        });
		
		//left selection Button
		left.setOnClickListener(new OnClickListener(){
        	@Override
			public void onClick(View v) {
        		selection = "en";
				updateDb();
				startAdding();
				left.setBackgroundResource(R.drawable.enterp);
				right.setBackgroundResource(R.drawable.enter);
			}
        });
		
		//AutoCompleteTextView
		auto.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
        		dbHelper.open();
        		kwantung = dbHelper.Kwantung(arg2, selection);
        		showOptions();
        		dbHelper.close();
        		auto.setText("");
        		imm.hideSoftInputFromWindow(auto.getWindowToken(), 0);
			}
        });
    }
   
    protected void showOptions() {
    	//Instantiate new dialog box
    	AlertDialog alert = new AlertDialog.Builder(this).create();
    	
    	//a list that would be use to show menus inside the dialog box
    	ListView options = new ListView(this);
    	KamusDbAdapter z = new KamusDbAdapter(getApplicationContext());
    	z.open();
    	Cursor opt = z.popUp();
    	z.close();
    	
        String[] displayFields = new String[] {"main", "sub"};
        int[] displayViews = new int[] { R.id.p1,R.id.p2 };
        OptionsIcons ico = new OptionsIcons(this, R.layout.options, opt,displayFields, displayViews);
    	options.setAdapter(ico);
    	
    	options.setOnItemClickListener(new OnItemClickListener(){
    		@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
    			System.out.println(arg2);
			}
    	});
    	//add our list to our dialog box
    	alert.setView(options);
    	alert.setButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//defAccesToken(myPin.getText().toString());
				System.out.println(kwantung[0]+"---"+kwantung[1]);
			}
		});
    	alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}

	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.feed:FeedBack();
        	break;
		case R.id.share: ShareMe();
    		break;
		}
		return true;
	}
    
	//our favorite list
	protected void startAdding() {
		final KamusDbAdapter z = new KamusDbAdapter(getApplicationContext());
        z.open();
        cc = z.CountRow(selection);
        //check if there's any records
        if(cc!=0){
        	//instantiate the list and adapter
        	Cursor cursor = z.takeFavs(selection);
        	z.close();
            String[] displayFields = new String[] {"word", "definition"};
            int[] displayViews = new int[] { R.id.texxt1,R.id.texxt2 };
            
            MyAdapter2 simple = new MyAdapter2(this, R.layout.mylist2, cursor,displayFields, displayViews);
            history.setAdapter(simple);
            //registerForContextMenu(history);
            
            history.setOnItemClickListener(new OnItemClickListener(){
            	@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
            		z.open();
            		kwantung = z.Kook(arg2, selection);
            		z.close();
            		showOptions();
            	}
            });
        }else{
        	String[] def = {"No word has been added"};
        	history.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, def));
        }
        
    }
	
	/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.history) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    madaminu(info.position);
	  }
	}
	*/
	
	
	/*
	private void madaminu(int position) {
		new AlertDialog.Builder(this)
    	.setMessage("Do you want to delete this word?")
    	.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dlg, int sumthin) {
    		
		}
    	})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dlg, int sumthin) {
	    		
			}
	    }).show();
	}
	*/
	
	protected void toastie(int ch) {
		Toast bakar = Toast.makeText(this, "", 3000);
		switch(ch){
			case 1:
				bakar.setText("limit reached! please delete some words!");
				bakar.show();
			break;
			case 2:
				bakar.setText("A word has been added to your Favourite");
				bakar.show();
			break;
		}
		
	}
	
	private void ShareMe() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Suitmedia Kamus App");
    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hai, apps ini mesti dimiliki!"+"\n"+
    			"[BlackBerry, iOS, Android]"+"\n"+"kunjungi:"+"http://mobile.suitmedia.com/KamDict/");
    	emailIntent.setType("text/plain");
    	startActivity(Intent.createChooser(emailIntent, "Share!"));
	}

	private void FeedBack() {
		String[]specf = new String[4];
		specf = getDeviceSpec();
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String[] recipients = new String[]{"mobile@suitmedia.com", "",};
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback Suitmedia Kamus");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, specf[0]+"\n"+specf[1]+"\n"+
				specf[2]+"\n"+specf[3]+"\n"+"[Please provide other information here]");
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Send feedback"));
	}

	private String[] getDeviceSpec() {
		String[]lay = new String[4];
		lay[0] = Build.BRAND;
		lay[1] = Build.MODEL;
		lay[2] = Build.ID;
		lay[3] = "version 1.0";
		return lay;
	}

	protected void SpeakTheWord(String sprog) {
		tts.speak(sprog, TextToSpeech.QUEUE_ADD, null);
	}

	protected void updateDb() {
		KamusDbAdapter save = new KamusDbAdapter(getApplicationContext());
		save.open();
		save.updatestatedb(selection);
		save.close();
	}


	@Override
	public void afterTextChanged(Editable arg0) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		KamusDbAdapter x = new KamusDbAdapter(getApplicationContext());
        x.open();
        cur = x.getCall(String.valueOf(arg0), selection);
        x.close();
        
        String[] displayFields = new String[] {"word", "meaning"};
        int[] displayViews = new int[] { R.id.text1,R.id.text2 };
        MyAdapter apt = new MyAdapter(this, R.layout.adapter, cur,displayFields, displayViews);
        auto.setAdapter(apt);
    }
	
	
	//our list adapter for search suggestion
    class MyAdapter extends SimpleCursorAdapter{
    	
    	private Cursor c;
        private Context context;
        
        public MyAdapter(Context context, int layout, Cursor c, String[] from,
				int[] to) {
			super(context, layout, c, from, to);
			this.c = c;
			this.context = context;
		}
		
		protected class RowViewHolder {
	        public TextView text1;
	        public TextView text2;
	        public String id;
	        public Button butt;
	    }

		@Override
		public View getView(int pos, View inView, ViewGroup parent) {
		       View vix = inView;
		       
		       RowViewHolder holder;
		       		       
		       if (vix == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            vix = inflater.inflate(R.layout.adapter, null);
		       }    
		            holder = new RowViewHolder();
		            
		            this.c.moveToPosition(pos);		
	       		    
				    holder.id = this.c.getString(this.c.getColumnIndex("_id"));
		            String title = this.c.getString(this.c.getColumnIndex("word"));
				    String cont = this.c.getString(this.c.getColumnIndex("meaning"));
				    				    
				    holder.text1 = (TextView) vix.findViewById(R.id.text1);
				    holder.text1.setText(title);
			               
				    holder.text2 = (TextView) vix.findViewById(R.id.text2);
				    holder.text2.setText(cont);
			               
				    holder.butt = (Button) vix.findViewById(R.id.add);
				    holder.butt.setTag(holder.id);
				    holder.butt.setOnClickListener(mButt);
			        
				    vix.setTag(holder);
		                      
		       return vix;
		}

		private OnClickListener mButt = new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(cc<20){
	        		String url = (v.getTag()).toString();
	        		Log.v("XXX", url);
	        		KamusDbAdapter ne = new KamusDbAdapter(getApplicationContext());
	        		ne.open();
	        		ne.FavQuery(url, selection);
	        		ne.close();
	        		toastie(2);
	        		startAdding();
		        }else{
		        	toastie(1);
		        }
	        }
	    };
		
	}
    
    
    //another list adapter for our favorite
    class MyAdapter2 extends SimpleCursorAdapter{
    	
    	private Cursor c;
        private Context context;
        
        
		public MyAdapter2(Context context, int layout, Cursor c, String[] from,
				int[] to) {
			super(context, layout, c, from, to);
			this.c = c;
			this.context = context;
		}
		
		protected class RowViewHolder {
	        public TextView tekst1;
	        public TextView tekst2;
	        public String ids;
	        public Button butts;
	    }

		@Override
		public View getView(int pos, View inView, ViewGroup parent) {
		       View vix = inView;
		       
		       RowViewHolder holder;
		       		       
		       if (vix == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            vix = inflater.inflate(R.layout.mylist2, null);
		       }    
		            holder = new RowViewHolder();
		            
		            this.c.moveToPosition(pos);		
	       		    
		            holder.ids = this.c.getString(this.c.getColumnIndex("_id"));
		            String title = this.c.getString(this.c.getColumnIndex("word"));
				    String cont = this.c.getString(this.c.getColumnIndex("definition"));
				    				    				    
				    holder.tekst1 = (TextView) vix.findViewById(R.id.texxt1);
				    holder.tekst1.setText(title);
			               
				    holder.tekst2 = (TextView) vix.findViewById(R.id.texxt2);
				    holder.tekst2.setText(cont);
			               
				    holder.butts = (Button) vix.findViewById(R.id.erase);
				    holder.butts.setTag(holder.ids);
				    holder.butts.setOnClickListener(mBtn);
			               
				    vix.setTag(holder);
		        	               
		       return vix;
		}

		private OnClickListener mBtn = new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				String url = (v.getTag()).toString();
				Log.v("XXX", url);
				KamusDbAdapter ne = new KamusDbAdapter(getApplicationContext());
		        ne.open();
		        ne.GudangDel(url, selection);
		        ne.close();
		        startAdding();
	        }
	    };
	    	    
	}


class OptionsIcons extends SimpleCursorAdapter{
    	
    	private Cursor c;
        private Context context;
        
        public OptionsIcons(Context context, int layout, Cursor c, String[] from,
				int[] to) {
			super(context, layout, c, from, to);
			this.c = c;
			this.context = context;
		}
		
		protected class RowViewHolder {
	        public TextView text1;
	        public TextView text2;
	        public ImageView icc;
	    }

		@Override
		public View getView(int pos, View inView, ViewGroup parent) {
		       View vix = inView;
		       
		       RowViewHolder holder;
		       		       
		       if (vix == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            vix = inflater.inflate(R.layout.options, null);
		       }    
		            holder = new RowViewHolder();
		            
		            this.c.moveToPosition(pos);		
	       		    
				    String main = this.c.getString(this.c.getColumnIndex("main"));
				    String sub = this.c.getString(this.c.getColumnIndex("sub"));
				    byte[] favicon = this.c.getBlob(this.c.getColumnIndex("icon"));
				    
				    holder.text1 = (TextView) vix.findViewById(R.id.p1);
				    holder.text1.setText(main);
			               
				    holder.text2 = (TextView) vix.findViewById(R.id.p2);
				    holder.text2.setText(sub);
			               
				    if (favicon != null) {
						   holder.icc = (ImageView) vix.findViewById(R.id.icon);
						   holder.icc.setImageBitmap(BitmapFactory.decodeByteArray(favicon, 0, favicon.length));
					}
				    vix.setTag(holder);
		                      
		       return vix;
		}

				
	}
    
    
	@Override
	public void onInit(int arg0) {
		//SpeakTheWord("Hi");
	}

	@Override
	 protected void onDestroy() {
	  super.onDestroy();
	  //tts.shutdown();
	 }
	
}