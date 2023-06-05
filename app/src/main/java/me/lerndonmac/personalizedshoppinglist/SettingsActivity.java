package me.lerndonmac.personalizedshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.lerndonmac.personalizedshoppinglist.connects.SqlLiteDbHelper;
import me.lerndonmac.personalizedshoppinglist.model.Buget;
import me.lerndonmac.personalizedshoppinglist.model.Wastes;

public class SettingsActivity extends AppCompatActivity {
    private EditText editTextBugetView;
    private Buget buget;
    private Spinner currenciesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTextBugetView = findViewById(R.id.editTextViewBuget);
        Button delAllWastes = findViewById(R.id.buttonDellAllWastest);
        delAllWastes.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Подтверждение").setMessage("Удалить Все Записи?").setPositiveButton("Да", (dialog, which) -> {
                delAllWastes();
                finish();
            });
            builder.create();
            builder.show();
        });
        currenciesSpinner = findViewById(R.id.currenciesSpiner);
        currenciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buget.setBugetText((String) currenciesSpinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this)){
            Dao<Buget, Integer> bugetDao = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper),Buget.class);
            buget = bugetDao.queryForId(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        editTextBugetView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if ((s.toString().split("")[s.toString().split("").length-1].equals(","))) {
                        StringBuilder s1 = new StringBuilder(s.toString());
                        s1.setCharAt(s1.length() - 1, '.');
                        s1.append('0');
                        editTextBugetView.setText(s1.toString());
                    }
                if((s.toString().equals(""))){
                    editTextBugetView.setText("0");
                }
                    buget.setBuget(Double.parseDouble(editTextBugetView.getText().toString()));
            }
        });
    }
    private void delAllWastes(){
        try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(this)){
            Dao<Wastes, Integer> wastesDao = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper), Wastes.class);
            List<Wastes> wastesList = new ArrayList<>(wastesDao.queryForAll());
            for(Wastes w:wastesList){wastesDao.delete(w);}
            finish();
        } catch (SQLException e) {e.printStackTrace();}
    }
    @Override
    protected void onDestroy() {
      DestroyThread dthread = new DestroyThread(this);
      dthread.start();
        startActivity(new Intent(this, MainActivity.class));
        super.onDestroy();
    }
    private class DestroyThread extends Thread {
        private final Context parThis;

        public DestroyThread(Context context){
            this.parThis = context;
        }
        @Override
        public void run() {
            try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(parThis)){
                Dao<Buget, Integer> bugetDao = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper),Buget.class);
                bugetDao.update(buget);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }

}
