package james.palettegettersample;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppData implements Parcelable {

    public static final Creator<AppData> CREATOR = new Creator<AppData>() {
        @Override
        public AppData createFromParcel(Parcel in) {
            return new AppData(in);
        }

        @Override
        public AppData[] newArray(int size) {
            return new AppData[size];
        }
    };

    public String label, name;
    public Drawable icon;

    public AppData(String label, String name) {
        this.label = label;
        this.name = name;
    }

    private String getKey(String key) {
        return name + "-" + key;
    }

    protected AppData(Parcel in) {
        label = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(label);
        out.writeString(name);
    }
}
