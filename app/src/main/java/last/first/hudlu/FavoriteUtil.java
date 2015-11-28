package last.first.hudlu;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import last.first.hudlu.models.Favorite;
import last.first.hudlu.models.MashableNewsItem;

/**
 * Created by connorhansen on 11/20/15.
 */
public class FavoriteUtil {

    public static void addFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Favorite favorite = realm.createObject(Favorite.class);
        favorite.setName(newsItem.title);
        favorite.setLink(newsItem.link);
        favorite.setAuthor(newsItem.author);
        favorite.setImage(newsItem.image);
        realm.commitTransaction();
    }

    public static void removeFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Favorite favorite = realm.where(Favorite.class).equalTo("link", newsItem.link).findFirst();
        favorite.removeFromRealm();
        realm.commitTransaction();
    }

    public static boolean isFavorite(Context context, MashableNewsItem newsItem) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Favorite favorite = realm.where(Favorite.class).equalTo("link", newsItem.link).findFirst();
        realm.commitTransaction();

        return favorite != null;
    }

    public static RealmResults<Favorite> getAllFavorites(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmResults<Favorite> favorites = realm.where(Favorite.class).findAll();
        realm.commitTransaction();
        return favorites;
    }
}
