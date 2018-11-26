package tn.duoes.esprit.cookmania.utils;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

public final class ImageUtils {

    public static Drawable LoadImageFromWebOperations(String uri) {
        try {
            String url = Constants.UPLOAD_FOLDER_URL+"/"+uri;
            System.out.println(url);
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
