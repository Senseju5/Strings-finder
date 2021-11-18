package lt.marbrs.stringsfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadTask extends AsyncTask<String, String, ArrayList<String>> {
    ProgressDialog progressDialog;
    private Context context;
    private static final String TAG = "DownloadTask";
    private static final String FILENAME = "results.log";

    public DownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> response = new ArrayList<String>();

        Log.wtf(TAG, "doInBackground " + params.length);
        if (params.length < 2) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
        } else {
            String uri = params[0];//https://dpaste.com/FLVWNPG9Q.txt
            String filter = params[1]; //https://cutt.ly/4TQUBiY
            BufferedReader bufferedReader = null;
            OutputStreamWriter outputStreamWriter = null;
            try {
                URL url = new URL(uri);
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
                bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                Pattern pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        response.add(line);
                        outputStreamWriter.write(line + System.lineSeparator());
                    }
                }
            } catch (MalformedURLException e) {
                Looper.prepare();
                Toast.makeText(context, R.string.malformed_url, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Looper.prepare();
                Toast.makeText(context, R.string.io_exception, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (outputStreamWriter != null) {
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStreamWriter != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.downloading);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result.size() > 0) {
            Intent intent = new Intent(context, ResultActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.no_result, Toast.LENGTH_SHORT).show();
        }
    }
}