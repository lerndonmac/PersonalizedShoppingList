package me.lerndonmac.personalizedshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

import me.lerndonmac.personalizedshoppinglist.connects.SqlLiteDbHelper;
import me.lerndonmac.personalizedshoppinglist.model.Wastes;

public class EditorActivity extends AppCompatActivity {
    private EditText textName;
    private EditText textCost;
    private TextView textCount;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        textName = findViewById(R.id.textVName);
        textCost = findViewById(R.id.textVCost);
        Button confBut = findViewById(R.id.button);
        Button delBut = findViewById(R.id.button2);
        textCount = findViewById(R.id.countTextView);
        Button incrBut = findViewById(R.id.incrBut);
        Button dicrBut = findViewById(R.id.dicrBut);
        Bundle exstras = getIntent().getExtras();
        if (exstras.get("type").equals(0)){
            confBut.setOnClickListener(v -> addWast());
        }
        incrBut.setOnClickListener(v -> {
            count++;
            textCount.setText(String.valueOf(count));
        });
        dicrBut.setOnClickListener(v -> {
            count--;
            textCount.setText(String.valueOf(count));
        });
        if (exstras.get("type").equals(1)){
            delBut.setVisibility(View.VISIBLE);
            textName.setText(exstras.getString("modelname"));
            textCost.setText(String.valueOf(exstras.getDouble("modelcost")));
            delBut.setOnClickListener(v->dellWast(exstras.getInt("modelid")));
            confBut.setOnClickListener(v->updWast(exstras.getInt("modelid")));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Расширение пунктов меню из файла res / menu / menu_catalog.xml.
        // Это добавляет пункты меню на панель приложения.
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Пользователь щелкнул пункт меню в меню переполнения панели приложения
        // Отвечаем на нажатие кнопки со стрелкой «Вверх» на панели приложения
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addWast() {
        Wastes localModel = new Wastes();
        localModel.setName(textName.getText().toString());
        localModel.setCost(Double.parseDouble(textCost.getText().toString()));
        localModel.setCount(count);

        CreateThread createThread = new CreateThread(this);
        createThread.setLocalModel(localModel);
        createThread.start();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void dellWast(int wastesFoDell){
        DeleteThread deleteThread = new DeleteThread(this);
        deleteThread.setLocalModel(wastesFoDell);
        deleteThread.start();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void updWast(int wastesid){
        Wastes corentWaste = new Wastes();
        corentWaste.setName(textName.getText().toString());
        corentWaste.setCost(Double.parseDouble(textCost.getText().toString()));
        corentWaste.setCount(count);
        UpdateThread updateThread = new UpdateThread(this);
        updateThread.setLocalModel(wastesid,corentWaste);
        updateThread.start();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        startActivity(new Intent(this,MainActivity.class));
        super.onDestroy();
    }

    private static class CreateThread extends Thread{
        private Wastes localModel;
        private final Context context;
        public CreateThread(Context context){
            this.context = context;
        }
        public void setLocalModel(Wastes newModel) {
            this.localModel = newModel;
        }
        @Override
        public void run() {
            try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context)) {
                Dao<Wastes, Integer> model = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper), Wastes.class);
                model.create(localModel);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            super.run();
        }
    }
    private static class DeleteThread extends Thread{
        private int localModel;
        private final Context context;
        public DeleteThread(Context context){
            this.context = context;
        }
        public void setLocalModel(int localModel) {
            this.localModel = localModel;
        }
        @Override
        public void run() {
            try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context)){
                Dao<Wastes, Integer> model = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper),Wastes.class);
                model.deleteById(this.localModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            super.run();
        }
    }
    private static class UpdateThread extends Thread{
        private int localModel;
        private Wastes corentWaste;
        private final Context context;
        public UpdateThread(Context context){
            this.context = context;
        }
        public void setLocalModel(int newModel,Wastes corentWaste) {
            this.localModel = newModel;
            this.corentWaste= corentWaste;
        }
        @Override
        public void run() {
            try (SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context)) {
                Dao<Wastes, Integer> model = DaoManager.createDao(new AndroidConnectionSource(sqlLiteDbHelper), Wastes.class);
                corentWaste.setId(localModel);
                model.update(corentWaste);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            super.run();
        }
    }


}