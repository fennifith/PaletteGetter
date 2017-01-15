package james.palettegettersample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new AppDataAdapter(this, getPackageManager(), new ArrayList<AppData>());
        recyclerView.setAdapter(adapter);

        new Thread() {
            @Override
            public void run() {
                PackageManager manager = getPackageManager();
                if (manager == null) return;

                final List<AppData> apps = new ArrayList<>();
                List<ResolveInfo> infos = manager.queryIntentActivities(new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0);
                for (ResolveInfo info : infos) {
                    apps.add(new AppData(info.loadLabel(manager).toString(), info.activityInfo.packageName));
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setList(apps);
                    }
                });
            }
        }.start();
    }
}
