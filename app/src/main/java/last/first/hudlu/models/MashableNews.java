package last.first.hudlu.models;

import com.google.gson.annotations.SerializedName;

public class MashableNews {
    @SerializedName("new")
    public MashableNewsItem[] newsItems;
}
