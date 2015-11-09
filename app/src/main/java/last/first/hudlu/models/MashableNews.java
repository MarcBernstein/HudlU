package last.first.hudlu.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MashableNews {
    @SerializedName("new")
    public List<MashableNewsItem> newsItems;
}
