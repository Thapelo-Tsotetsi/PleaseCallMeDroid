package tsotetsi.thapelo.pleasecallmedroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DroidMainActivity extends Activity {

    protected static final int CONTACT_PICKER_RESULT = 0;
	protected String numToCall="0";
	protected String providerPrefix;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_main);
        
        final EditText txtArea = (EditText)findViewById(R.id.cellNunberText);
        Button btnSelectContact = (Button)findViewById(R.id.btnContact);
        btnSelectContact.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, CONTACT_PICKER_RESULT);
			}
        	
        });
        
        Button btnsendCallback = (Button)findViewById(R.id.btnSend);
        btnsendCallback.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				numToCall = txtArea.getText().toString();
				Toast.makeText(getApplicationContext(), numToCall, Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:*"+providerPrefix+"*"+numToCall+Uri.encode("#")));
				Toast.makeText(getApplicationContext(), providerPrefix, Toast.LENGTH_LONG).show();
				startActivityForResult(intent,1);
			}
        	
        });
        
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        
        if(providerPrefix == null){
        	MenuItem menuItem = null;
			selectProvider(menuItem);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("providerPrefix", providerPrefix);
        	editor.commit();
        }
    }
	
	
    public void selectProvider(MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_provider);
        builder.setItems(R.array.provider_names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TypedArray providerPrefixes = getResources().obtainTypedArray(R.array.provider_prefixes);
                providerPrefix = providerPrefixes.getString(which);
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString("providerPrefix", providerPrefix);
                editor.commit();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			switch (requestCode){
			case CONTACT_PICKER_RESULT:
				final EditText phoneInput = (EditText)findViewById(R.id.cellNunberText);
				Cursor cursor = null;
				String phoneNumber ="";
				List<String> allNumbers = new ArrayList<String>();
				int phoneIdx = 0;
				
				try{
	                 Uri result = data.getData();  
	                 String id = result.getLastPathSegment();  
	                 cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] { id }, null);  
	                 phoneIdx = cursor.getColumnIndex(Phone.DATA);
	                 
	                 if(cursor.moveToFirst()){
	                	 while(cursor.isAfterLast() == false){
                         phoneNumber = cursor.getString(phoneIdx);
                         allNumbers.add(phoneNumber);
                         cursor.moveToNext();
	                	 }
	                 }
	                 else{
	                	 //no results
	                 }
				}
				catch(Exception e){
					System.out.println(e);
				}finally{
					if(cursor != null){
						cursor.close();
					}
	                 final CharSequence[] items = allNumbers.toArray(new String[allNumbers.size()]);
	                 AlertDialog.Builder builder = new AlertDialog.Builder(DroidMainActivity.this);
	                 builder.setTitle("Choose a number");
	                 builder.setItems(items, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int item) {
	                         String selectedNumber = items[item].toString();
	                         selectedNumber = selectedNumber.replace("-", "");
	                         System.out.println(selectedNumber);
	                         phoneInput.setText(selectedNumber);
	                         numToCall = selectedNumber;
							
						}
					});
	                 AlertDialog alert = builder.create();
	                 if(allNumbers.size() > 1){
	                	 alert.show();
	                 }
	                 else{
	                     String selectedNumber = phoneNumber.toString();
	                     selectedNumber = selectedNumber.replace("-", "");
	                     phoneInput.setText(selectedNumber);
	                     numToCall = selectedNumber;
	                 }
	                 if(phoneNumber.length() == 0){
	                	 //no numbers found
	                 }
				}
				break;
			}
		}else{
			//activity result error actions
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("Select Providerr");
    	
    	//getMenuInflater().inflate(R.menu.options, menu);
        Toast.makeText(getApplicationContext(), "createMenu", Toast.LENGTH_LONG).show();
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	selectProvider(item);
    	Toast.makeText(getApplicationContext(), "onOptionsItem", Toast.LENGTH_LONG).show();
    	return true;
    }
}
