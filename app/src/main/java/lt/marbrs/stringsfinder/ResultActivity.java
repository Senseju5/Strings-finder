package lt.marbrs.stringsfinder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ResultActivity extends AppCompatActivity implements ResultAdapter.ItemClickListener {

    private static final String TAG = "ResultActivity";
    protected ResultAdapter resultAdapter;
    protected ArrayList<String> filteredResult;
    public static ArrayList<Integer> selections = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        selections = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filteredResult = initializeResultsData("results.log", getApplicationContext());

        resultAdapter = new ResultAdapter(filteredResult);
        resultAdapter.setClickListener(this);
        recyclerView.setAdapter(resultAdapter);

        Button copy = (Button) findViewById(R.id.copy);
        copy.setOnClickListener(view -> {
            Log.d(TAG, "copying " + selections.size() + " lines");
            try {
                StringBuilder stringBuilder = new StringBuilder();
                Collections.sort(selections);
                for (Integer selection : selections) {
                    stringBuilder.append(filteredResult.get(selection) + System.lineSeparator());
                }
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("SelectedLines", stringBuilder.toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), R.string.copied_successfully, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.copying_error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (selections.contains(position)) {
            selections.remove(position);
            view.setBackgroundColor(Color.WHITE);
        } else {
            selections.add(position);
            view.setBackgroundColor(Color.LTGRAY);
        }
    }

    public ArrayList<String> initializeResultsData(String fileName, Context context) {
        ArrayList<String> result = new ArrayList();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = bufferedReader.readLine()) != null) result.add(line);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return result;
    }
}