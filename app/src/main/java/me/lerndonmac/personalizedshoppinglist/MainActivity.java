package me.lerndonmac.personalizedshoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.lerndonmac.personalizedshoppinglist.connects.SqlLiteDbHelper;
import me.lerndonmac.personalizedshoppinglist.model.Buget;
import me.lerndonmac.personalizedshoppinglist.model.Wastes;
import me.lerndonmac.personalizedshoppinglist.model.WastesAdapter;

public class MainActivity extends AppCompatActivity {
    private final List<Wastes> wastesList = new ArrayList<>();
    private ListView listView;
    private TextView bugetTextView;
    private static double buget;
    private static String bugetText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        bugetTextView = findViewById(R.id.bugettextView);
        initList();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent editorIntent = new Intent(MainActivity.this, EditorActivity.class);
            editorIntent.putExtra("type", 1);
            editorIntent.putExtra("modelid", ((Wastes)listView.getItemAtPosition(position)).getId());
            editorIntent.putExtra("modelname", ((Wastes)listView.getItemAtPosition(position)).getName());
            editorIntent.putExtra("modelcost", ((Wastes)listView.getItemAtPosition(position)).getCost());
            startActivity(editorIntent);
            finish();
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Расширение пунктов меню из файла res / menu / menu_catalog.xml.
        // Это добавляет пункты меню на панель приложения.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settingsitem) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }else {
            listView.setAdapter(null);
            Intent editorIntent = new Intent(MainActivity.this, EditorActivity.class);
            editorIntent.putExtra("type", 0);
            startActivity(editorIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initList(){
        try (SqlLiteDbHelper modelSqlHelper = new SqlLiteDbHelper(this)){
            Dao<Wastes, Integer> model = DaoManager.createDao(new AndroidConnectionSource(modelSqlHelper), Wastes.class);
            wastesList.addAll(model.queryForAll());
                Dao<Buget, Integer> bugets = DaoManager.createDao(new AndroidConnectionSource(modelSqlHelper), Buget.class);
                Buget bugetModel = bugets.queryForId(1);
                MainActivity.buget = bugetModel.getBuget();
                MainActivity.bugetText = bugetModel.getBugetText();
                wastesList.forEach(wastes -> buget -= wastes.getCost()*wastes.getCount());
                if (buget < 0) {
                    bugetTextView.setTextColor(getColor(R.color.red));
                }
                if (buget == 0) {
                    bugetTextView.setTextColor(getColor(R.color.black));
                }
                if (buget > 0) {
                    bugetTextView.setTextColor(getColor(R.color.green));
                }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(wastesList.size()>0) {
            ArrayAdapter<Wastes> arrayAdapter = new WastesAdapter(this, wastesList);
            listView.setAdapter(arrayAdapter);
        }
            String formattedDouble = new DecimalFormat("#0.00").format(buget);
            bugetTextView.setText(formattedDouble + " " + bugetText);
    }

}