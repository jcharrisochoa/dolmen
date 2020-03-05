package co.dolmen.sid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://www.dolmen.net.co";
        WebView myWebView = new WebView(Web.this);
        setContentView(myWebView);
        myWebView.loadUrl(url);
        WebSettings webSettings  = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new myWebClient());

    }
    public class myWebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
    }
    // To handle &quot;Back&quot; key press event for WebView to go back to previous screen.
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK) &amp;&amp; web.canGoBack()) {
        web.goBack();
        return true;
    }
        return super.onKeyDown(keyCode, event);
    }*/

}
