package edu.uic.cs478.jamesklonowski.project4;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.sql.SQLException;
import java.util.List;


public class TransactionActivity extends ListActivity {
    private TransactionDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        dataSource = new TransactionDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Transaction> values = dataSource.getAllTransactions();
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<Transaction>(this, R.layout.textview, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume(){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }
}
