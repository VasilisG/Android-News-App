package gr.thekid.billy.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewHeadlineActivity extends AppCompatActivity {

    private WebView webView;
    private ArticleClient articleClient;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_headline);

        extras = getIntent().getExtras();
        String url = extras.getString(Constants.ARTICLE_URL_KEY);

        articleClient = new ArticleClient();

        webView = (WebView)findViewById(R.id.headlineWebView);
        webView.setWebViewClient(articleClient);
        webView.loadUrl(url);

    }
}
