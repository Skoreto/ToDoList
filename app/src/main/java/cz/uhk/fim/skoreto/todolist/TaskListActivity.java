package cz.uhk.fim.skoreto.todolist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import cz.uhk.fim.skoreto.todolist.model.DataModel;
import cz.uhk.fim.skoreto.todolist.model.Task;
import cz.uhk.fim.skoreto.todolist.utils.TaskAdapter;

public class TaskListActivity extends AppCompatActivity {

    Toolbar tlbTaskListActivity;
    ActionBar actionBar;
    ListView listView;
    ArrayAdapter<Task> arrayAdapter;
    DataModel dataModel;
    EditText etTaskName;
    int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        dataModel = new DataModel(this);

        // Implementace ActionBaru.
        tlbTaskListActivity = (Toolbar) findViewById(R.id.tlbTaskListActivity);
        if (tlbTaskListActivity != null) {
            setSupportActionBar(tlbTaskListActivity);

            // Ziskani podpory ActionBaru korespondujiciho s Toolbarem.
            actionBar = getSupportActionBar();
            // Povoleni tlacitka Zpet.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        listView = (ListView) findViewById(R.id.lvTasksList);

        Intent anyIntent = getIntent();
        // Nastaveni listId pro filtraci ukolu v seznamu.
        // Ve vychozim pripade 1 (Inbox) - pokud IntExtra neprijde ze zadneho intentu.
        listId = anyIntent.getIntExtra("listId", 1);

        arrayAdapter = new TaskAdapter(TaskListActivity.this, dataModel.getTasksByListId(listId));
        listView.setAdapter(arrayAdapter);

        // Editace ukolu po klepnuti v seznamu ukolu.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Po klepnuti na polozku seznamu ziskej instanci zvoleneho ukolu.
                Task task = (Task) listView.getItemAtPosition(position);
                Intent editTaskIntent = new Intent(getApplication(), TaskDetailActivity.class);
                // Predej ID ukolu do intentu editTaskIntent.
                editTaskIntent.putExtra("taskId", task.getId());
                // Predej ID seznamu pro prechod do aktivity TaskDetailActivity.
                editTaskIntent.putExtra("listId", listId);
                editTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(editTaskIntent);
            }
        });



        // Pridani noveho ukolu.
        FloatingActionButton btnAddTask = (FloatingActionButton) findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTaskName = (EditText) findViewById(R.id.etTaskName);

                // Pokud neni prazdny nazev noveho ukolu.
                if (!etTaskName.getText().toString().equals("")){
                    // Ve vychozim pripade pridej novy ukol s prazdnym popisem do Inboxu jako nesplneny.
                    dataModel.addTask(etTaskName.getText().toString(), "", listId, 0, "", "");

                    // Vyprazdneni pole po pridani ukolu.
                    etTaskName.setText("");
                    etTaskName.clearFocus();
                    etTaskName.clearComposingText();

                    // Aktualizace seznamu ukolu.
                    arrayAdapter.clear();
                    arrayAdapter.addAll(dataModel.getTasksByListId(listId));

                    // Informovani uzivatele o uspesnem pridani ukolu.
                    Toast.makeText(TaskListActivity.this, "Úkol přidán", Toast.LENGTH_SHORT).show();
                } else {
                    // Informovani uzivatele o nutnosti vyplnit název úkolu.
                    Toast.makeText(TaskListActivity.this, "Prázdný název úkolu!" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Metoda pro inicializaci layoutu ActionBaru.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_list_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Metoda pro obluhu tlacitek v ActionBaru.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // implementace nastavení
                return true;

            case R.id.action_sort:
                // TODO implementace řazení
                return true;

            default:
                // Vyvolani superclass pro obsluhu nerozpoznane akce.
                return super.onOptionsItemSelected(item);
        }
    }

}