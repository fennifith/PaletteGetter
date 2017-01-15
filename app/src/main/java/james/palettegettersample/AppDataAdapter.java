package james.palettegettersample;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import james.palettegetter.PaletteGetter;

public class AppDataAdapter extends RecyclerView.Adapter<AppDataAdapter.ViewHolder> {

    private List<AppData> list;
    private PackageManager manager;
    private Activity activity;

    public AppDataAdapter(final Activity activity, PackageManager manager, List<AppData> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);

        this.manager = manager;
        this.activity = activity;
    }

    public void setList(List<AppData> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public AppDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(final AppDataAdapter.ViewHolder holder, int position) {
        if (holder.t != null && holder.t.isAlive()) holder.t.interrupt();

        AppData app = list.get(position);

        holder.v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        TextView title = (TextView) holder.v.findViewById(R.id.name);
        title.setText(app.label);

        TextView subtitle = (TextView) holder.v.findViewById(R.id.extra);
        subtitle.setText(app.name);

        if (app.icon != null)
            ((ImageView) holder.v.findViewById(R.id.image)).setImageDrawable(app.icon);
        else {
            holder.t = new Thread() {
                @Override
                public void run() {
                    try {
                        list.get(holder.getAdapterPosition()).icon = manager.getApplicationIcon(list.get(holder.getAdapterPosition()).name);
                    } catch (PackageManager.NameNotFoundException e) {
                        return;
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((ImageView) holder.v.findViewById(R.id.image)).setImageDrawable(list.get(holder.getAdapterPosition()).icon);
                            } catch (Exception ignored) {
                            }
                        }
                    });
                }
            };
            holder.t.start();
        }

        List<Integer> colors = PaletteGetter.getPalette(activity, app.name);

        if (colors.size() > 0) {
            holder.v.findViewById(R.id.color).setBackgroundColor(colors.get(0));

            LinearLayout layout = (LinearLayout) holder.v.findViewById(R.id.colors);
            layout.removeAllViews();
            layout.setVisibility(View.VISIBLE);
            for (int color : colors) {
                View v = LayoutInflater.from(activity).inflate(R.layout.item_color, layout, false);
                v.setBackgroundColor(color);
                layout.addView(v);
            }
        } else {
            holder.v.findViewById(R.id.color).setBackgroundColor(Color.TRANSPARENT);
            holder.v.findViewById(R.id.colors).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        Thread t;

        ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
