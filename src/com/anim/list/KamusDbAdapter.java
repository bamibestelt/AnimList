package com.anim.list;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KamusDbAdapter {
	private static final String TAG = "AnyDBAdapter";
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;

    private static String DB_PATH = "/sdcard/suitkamus/";

    private static final String DATABASE_NAME = "kamus";

    private static final int DATABASE_VERSION = 3;

    private final Context adapterContext;

    public KamusDbAdapter(Context context) {
        this.adapterContext = context;
    }

    public KamusDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(adapterContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        
        return this;
    }
    

    public void close() {
        mDbHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        @SuppressWarnings("unused")
		Context helperContext;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database!!!!!");
            //db.execSQL("");
            onCreate(db);
        }

        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (dbExist) {
            } else {

                this.getReadableDatabase();
                copyDataBase();
            }
        }

        @SuppressWarnings("unused")
		public SQLiteDatabase getDatabase() {
            String myPath = DB_PATH + DATABASE_NAME;
            return SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;
            try {
                String myPath = DB_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) {
            }
            if (checkDB != null) {
                checkDB.close();
            }
            return checkDB != null ? true : false;
        }

        
        
        private void copyDataBase() throws IOException {

            // Open your local db as the input stream
            //InputStream myInput = adapterContext.getAssets().open(DATABASE_NAME);
        	
            // Path to the just created empty db
        	Log.d("3", "here");
            String outFileName = DB_PATH + DATABASE_NAME;
            InputStream myInput;
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            @SuppressWarnings("unused")
			int length;
            
            Log.d("1", "here");
            myInput = new FileInputStream("/sdcard/suitkamus/kamus1");
            			while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput.close();
	        Log.d("2", "here1");	       
	        myInput = new FileInputStream("/sdcard/suitkamus/kamus2");
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        Log.d("2", "here2");	        
	        myInput = new FileInputStream("/sdcard/suitkamus/kamus3");
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        Log.d("2", "here3");
	        myInput = new FileInputStream("/sdcard/suitkamus/kamus4");
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        Log.d("2", "here4");
	        myInput = new FileInputStream("/sdcard/suitkamus/kamus5");
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();Log.d("2", "here5");
            	        myOutput.flush();
            	        myOutput.close();
    	 } 
    	
        
        
        /*
        private void copyDataBase() throws IOException {

            Log.d("3", "here");
            String outFileName = DB_PATH + DATABASE_NAME;
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            @SuppressWarnings("unused")
			int length;
            Log.d("1", "here");
            myInput = 
            	adapterContext.getResources().openRawResource(com.anim.list.R.raw.kamus1);
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput.close();
	        Log.d("2", "here");	       
	        myInput = 
            	adapterContext.getResources().openRawResource(com.anim.list.R.raw.kamus2);
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        
	        myInput = 
            	adapterContext.getResources().openRawResource(com.anim.list.R.raw.kamus3);
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        
	        myInput = 
            	adapterContext.getResources().openRawResource(com.anim.list.R.raw.kamus4);
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	        
	        myInput = 
            	adapterContext.getResources().openRawResource(com.anim.list.R.raw.kamus5);
            	        while ( (length = myInput.read(buffer)) > 0 ) {
            	            myOutput.write(buffer);
            	        }
            	        myInput .close();
            	       
            	        myOutput.flush();
            	        myOutput.close();
    	 }
        */
        
        

        public void openDataBase() throws SQLException {
            String myPath = DB_PATH + DATABASE_NAME;
            mDb = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            mDb.execSQL("create table if not EXISTS state (id INTEGER PRIMARY KEY,lang text)");
            mDb.execSQL("INSERT INTO state (lang) VALUES ('en')");
        }

        @Override
        public synchronized void close() {

            if (mDb != null)
                mDb.close();

            super.close();

        }
    }
    
    public String getstatedb(){
    	String dState;
    	Cursor stat = mDb.rawQuery("SELECT * FROM state WHERE id='1'", null);
    	if (stat!=null){
    		stat.moveToFirst();
    		dState = stat.getString(1);
    	} else {dState = "en";}
    	stat.close();
    	Log.v("STATUS",dState);
    	return dState;
    }
    
    public String[] getwordday(String statue){
    	Random rnd = new Random();
    	Cursor cursor;
    	int random; String[] reply = new String[2];
    	random = rnd.nextInt(23616);
    	if(statue.equalsIgnoreCase("en")){
    		cursor = mDb.rawQuery("SELECT * FROM en_to_ina WHERE _id='"+random+"'", null);
    	}else{
    		cursor = mDb.rawQuery("SELECT * FROM ina_to_en WHERE _id='"+random+"'", null);
    	}
    	cursor.moveToFirst();
    	reply[0] = cursor.getString(1);
    	reply[1] = cursor.getString(2);
    	Log.d("random",String.valueOf(random));
    	cursor.close();
    	return reply;
    }
    
    public void updatestatedb(String lang){
    	Log.d("update",lang);
    	mDb.execSQL("delete from state");
    	mDb.execSQL("INSERT into state (lang) values ('"+lang+"')");
    }
    
        
	public Cursor getCall(String input, String select){
		Cursor ken;
		if(select == "en"){
			ken = mDb.rawQuery("SELECT * FROM en_to_ina WHERE word LIKE '"+input+"%' LIMIT 20", null);
			Temporary.Temp(ken);
		}else{
			ken = mDb.rawQuery("SELECT * FROM ina_to_en WHERE word LIKE '"+input+"%' LIMIT 20", null);
			Temporary.Temp(ken);
		}
		ken.moveToFirst();
    	return ken;
    }
	
	public void Words(int cho, String select){
		Cursor summ;
		if(select == "en"){
			summ = mDb.rawQuery("SELECT * FROM wordy", null);
			if(summ.getCount()>=10){
				mDb.execSQL("DELETE FROM wordy WHERE _id = (SELECT min(_id) FROM wordy)");
			}
			mDb.execSQL("INSERT INTO wordy(word, definition) SELECT word, meaning FROM en_to_ina WHERE _id='"+Temporary.pre[cho]+"'");
		}else{
			summ = mDb.rawQuery("SELECT * FROM wordy2", null);
			//Log.v("SUMM",String.valueOf(summ.getCount()));
			if(summ.getCount()>=10){
				mDb.execSQL("DELETE FROM wordy2 WHERE _id = (SELECT min(_id) FROM wordy2)");
			}
			mDb.execSQL("INSERT INTO wordy2(word, definition) SELECT word, meaning FROM ina_to_en WHERE _id='"+Temporary.pre[cho]+"'");
		}
	}
	
	public String[] Kwantung(int cho, String select){
		Cursor kk=null;
		String[]gg = new String[2];
		if(select == "en"){
			kk=mDb.rawQuery("SELECT word, meaning FROM en_to_ina WHERE _id='"+Temporary.pre[cho]+"'", null);
		}else{
			kk=mDb.rawQuery("SELECT word, meaning FROM ina_to_en WHERE _id='"+Temporary.pre[cho]+"'", null);
		}
		kk.moveToFirst();
		gg[0]=kk.getString(0);
		gg[1]=kk.getString(1);
		kk.close();
		return gg;
	}
	
	public String[] Kook(int che, String select){
		Cursor kk=null;
		String[]gg = new String[2];
		if(select == "en"){
			kk=mDb.rawQuery("SELECT word, definition FROM engf WHERE _id='"+Temporary.list[che]+"'", null);
		}else{
			kk=mDb.rawQuery("SELECT word, definition FROM inaf WHERE _id='"+Temporary.list[che]+"'", null);
		}
		kk.moveToFirst();
		gg[0]=kk.getString(0);
		gg[1]=kk.getString(1);
		kk.close();
		return gg;
	}
	
	/*
	public Cursor bla(String select){
		Cursor lijst;
		if(select.equalsIgnoreCase("en")){
			lijst = mDb.rawQuery("SELECT * FROM wordy", null);
			Temporary.Tempst(lijst);
		}else{
			lijst = mDb.rawQuery("SELECT * FROM wordy2", null);
			Temporary.Tempst(lijst);
		}
		lijst.moveToFirst();
		return lijst;
	}
	*/
	
	public void delWords(String input, String select){
		if(select.equalsIgnoreCase("en")){
			mDb.execSQL("DELETE FROM wordy WHERE _id="+input);
		}else{
			mDb.execSQL("DELETE FROM wordy2 WHERE _id="+input);
		}
	}
	
	public void FavQuery(String title, String select){
		mDb.execSQL("CREATE TABLE IF NOT EXISTS engf(_id INTEGER PRIMARY KEY, word TEXT, definition TEXT)");
		mDb.execSQL("CREATE TABLE IF NOT EXISTS inaf(_id INTEGER PRIMARY KEY, word TEXT, definition TEXT)");
		if(select.equalsIgnoreCase("en")){
			Log.v("SELECTION","English");
			mDb.execSQL("INSERT INTO engf(word, definition) SELECT word, meaning FROM en_to_ina WHERE _id="+title);
		}else{
			mDb.execSQL("INSERT INTO inaf(word, definition) SELECT word, meaning FROM ina_to_en WHERE _id="+title);
		}
	}
	
	public void GudangDel(String id, String select){
		if(select.equalsIgnoreCase("en")){
			mDb.execSQL("DELETE FROM engf WHERE _id="+id);
		}else{
			mDb.execSQL("DELETE FROM inaf WHERE _id="+id);
		}
	}
	
	public int CountRow(String select){
		mDb.execSQL("CREATE TABLE IF NOT EXISTS engf(_id INTEGER PRIMARY KEY, word TEXT, definition TEXT)");
		mDb.execSQL("CREATE TABLE IF NOT EXISTS inaf(_id INTEGER PRIMARY KEY, word TEXT, definition TEXT)");
		Cursor r = null;
		if(select.equalsIgnoreCase("en")){
			r = mDb.rawQuery("SELECT * FROM engf", null);
		}else{
			r = mDb.rawQuery("SELECT * FROM inaf", null);
		}
		int row=0;
		r.moveToFirst();
		row = r.getCount();
		Log.v("ROWCOUNT",String.valueOf(row));
		return row;
	}
	
	public Cursor takeFavs(String select){
		Cursor zz = null; 
		if(select.equalsIgnoreCase("en")){
			zz = mDb.rawQuery("SELECT * FROM engf", null);
			Temporary.Tempst(zz);
		}else{
			zz = mDb.rawQuery("SELECT * FROM inaf", null);
			Temporary.Tempst(zz);
		}
		zz.moveToFirst();
		Log.v("ZZZZ",String.valueOf(zz.getCount()));
		return zz;
	}

	
	public Cursor popUp(){
		Cursor opt = mDb.rawQuery("SELECT * FROM option", null);
		opt.moveToFirst();
		return opt;
	}
	/*
	public void saveToken(String getToken, String getTokenSecret) {
		mDb.execSQL("CREATE TABLE IF NOT EXISTS token(_id INTEGER PRIMARY KEY, token TEXT, secret TEXT)");
		mDb.execSQL("INSERT INTO token(token, secret) VALUES('"+getToken+"','"+getTokenSecret+"')");
	}
	
	public String[] fetchToken() {
		mDb.execSQL("CREATE TABLE IF NOT EXISTS token(_id INTEGER PRIMARY KEY, token TEXT, secret TEXT)");
		Cursor tokenc = mDb.rawQuery("SELECT * FROM token", null);
		String[]myToken = new String[2];
		if(tokenc.getCount()!=0){
			tokenc.moveToFirst();
			myToken[0]=tokenc.getString(1);
			myToken[1]=tokenc.getString(2);
			tokenc.close();
		}else{
			myToken=null;
		}
		return myToken;
	}
	
	public String[] fav2(int ixe){
		Cursor co = mDb.rawQuery("SELECT * FROM gudang WHERE _id='"+Temporary.ren[ixe]+"'", null);
		String[]fave2 = new String[3];
		co.moveToFirst();
		fave2[0]=co.getString(1);
		fave2[1]=co.getString(2);
		fave2[2]="favo";
		return fave2;
	}
	*/
	
}