package lt.marbrs.stringsfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String filter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button process = (Button) findViewById(R.id.process);
        process.setOnClickListener(view -> {

            TextView url = findViewById(R.id.url);
            TextView regex = findViewById(R.id.filter);
            Log.d("MainActivity", url.getText() + " inputs" + regex.getText());
            try{
                URL path = new URL(url.getText().toString());
                final DownloadTask downloadTask = new DownloadTask(MainActivity.this);
                filter = regex.getText().toString();
                filter = filter.replace("?",".");
                while (filter.contains("**")) filter = filter.replace("**","*");
                filter = filter.replace("*",".*?");
                downloadTask.execute(path.toString(), filter);
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), R.string.malformed_url, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });

    }
}