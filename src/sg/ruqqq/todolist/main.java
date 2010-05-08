package sg.ruqqq.todolist;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class main extends ListActivity {
	static final private int SORT_TASKS = Menu.FIRST;
	static final private int REMOVE_TASKS = Menu.FIRST + 1;
	static final private int EDIT_TASKS = Menu.FIRST + 2;
	
	// Declarations related to list
	ArrayList<String> todoTasks = new ArrayList<String>();
	ArrayAdapter<String> aa;
	
	// Keep track of which item we modifying
	int indexModifying = -1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get ListView widget
        ListView lv = getListView();
        
        // Add sample items to ArrayList
        todoTasks.add("Meeting FYP supervisor");
        todoTasks.add("Prepare for UT1");
        
        // Instantiate ArrayAdapter
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoTasks);
        
        // Assign Adapter to ListView
        lv.setAdapter(aa);
        
        // Enabled context menu for ListView
        registerForContextMenu(lv);
        
        // Get Add button
        Button btnAdd = (Button) findViewById(R.id.btnNewItem);
        // Assigned 'Add' method
        btnAdd.setOnClickListener(new OnClickListener() {
           	public void onClick(View v) {
           		saveItem(); // call saveItem (addNewItem) method
           	}
        });
        
        // Add trackball click listener to add item
        EditText tbNewItem = (EditText) findViewById(R.id.tbNewItem);
        tbNewItem.setOnKeyListener(new OnKeyListener() {
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
	        	if (event.getAction() == KeyEvent.ACTION_DOWN){
		        	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
		        		saveItem();		   
		        		return true;
		        	}
		        }
	        	return false;
        	}
        });
    }
    
    private void saveItem() {
    	// Get EditText & Read the value inside
    	EditText tbNewItem = (EditText) findViewById(R.id.tbNewItem);
    	String newItem = tbNewItem.getText().toString();
    	
    	// Don't proceed if EditText is empty
    	if (newItem.compareTo("") == 0) {
    		return;
    	}
    	
    	// Get Add button
        Button btnAdd = (Button) findViewById(R.id.btnNewItem);

    	if (btnAdd.getText().toString().compareTo("Add") != 0 && indexModifying != -1) {
    		// Modify the item based on remembered position
    		todoTasks.set(indexModifying, newItem);
    		
    		// Reset button to "Add"
    		btnAdd.setText(R.string.new_item_btn);
    		
    		// Reset modifying position
    		indexModifying = -1;
    	} else {
    		// Save newItem to ArrayList
        	todoTasks.add(newItem);
    	}

    	// Remove value from EditText
    	tbNewItem.setText("");
    	
    	// Notify ListView that data has changed
    	aa.notifyDataSetChanged();
    }
    
    // Create Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    // Create and add new menu items.
	    MenuItem itemSort = menu.add(0, SORT_TASKS, Menu.NONE, R.string.sort_all);
	    MenuItem itemRem = menu.add(0, REMOVE_TASKS, Menu.NONE, R.string.remove_all);
	    
	    itemSort.setIcon(R.drawable.sort_btn);
	    itemRem.setIcon(R.drawable.remove_btn);
	    
	    return true;
    }
    
    // Handles Option Menu Selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    super.onOptionsItemSelected(item);
	    switch (item.getItemId()) {
	    	case (SORT_TASKS):
	    		//Perform the necessary task to sort the items in the ArrayList
	    		Collections.sort(todoTasks);
	    		aa.notifyDataSetChanged();
	    		break;
	    	case (REMOVE_TASKS):
	    		//Perform the necessary actions to remove ALL the tasks in the 
	    		todoTasks.clear();
	    		aa.notifyDataSetChanged();
	    		break;
	    }
	    
	    // Prevent bug: When editing, if RemoveAll is called, change mode to Add mode
	    // Get Add button
        Button btnAdd = (Button) findViewById(R.id.btnNewItem);
	    indexModifying = -1;
	    btnAdd.setText(R.string.new_item_btn);
	    
	    return true;
    }
    
    // Create context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
       super.onCreateContextMenu(menu, v, menuInfo);
       menu.setHeaderTitle("Actions");
       menu.add(0, EDIT_TASKS, Menu.NONE, R.string.edit_item);
       menu.add(0, REMOVE_TASKS, Menu.NONE, R.string.remove_item);
    }
    
    // Handle context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    super.onContextItemSelected(item);
	    
	    // Get selected index
	    AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int index = menuInfo.position;
		
	    switch (item.getItemId()) {
	    	case (EDIT_TASKS):
	    		// Get EditText
	    		EditText tbNewItem = (EditText) findViewById(R.id.tbNewItem);
	    		tbNewItem.setText(todoTasks.get(index));
	    		
	    		// Get Button
	    		Button btnAdd = (Button) findViewById(R.id.btnNewItem);
	    		btnAdd.setText(R.string.edit_item_btn);
	    		
	    		// Remember editing position
	    		indexModifying = index;
	    		return true;
	    		
	    	case (REMOVE_TASKS):
	    		//Perform the necessary actions to remove the selected tasks in    
	    		todoTasks.remove(index);
	    		aa.notifyDataSetChanged();
	    		return true;
	    }
	    
	    return false;
    }
}