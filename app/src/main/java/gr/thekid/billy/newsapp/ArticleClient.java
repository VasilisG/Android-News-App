package gr.thekid.billy.newsapp;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        view.loadUrl(url);
        return true;
    }
}
