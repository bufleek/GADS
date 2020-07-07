package com.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var databaseHelper: DatabaseHelper;
    private val employeeListAdapter = EmployeeListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        recyclerView.adapter = employeeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))

        fab.setOnClickListener{
            val addEmployee = Intent(this, AddEmployeeActivity::class.java)
            startActivityForResult(addEmployee , 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_deleteAll->{
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.action_delete_all)
                builder.setPositiveButton(R.string.yes){dialog, delAll->
                    val result = DataManager.deleteAllEmployees(databaseHelper)

                    Toast.makeText(applicationContext, "$result Record(s) Deleted", Toast.LENGTH_SHORT).show()
                    employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))
                }
                    .setNegativeButton(R.string.no){dialog, all->
                        dialog.dismiss()
                    }
                builder.setTitle("Are you sure")
                builder.show()

             true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
