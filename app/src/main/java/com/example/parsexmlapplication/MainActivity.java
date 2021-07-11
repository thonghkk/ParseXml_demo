package com.example.parsexmlapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtAge;
    private ListView listViewParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        try {
            parseXml();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        listViewParse = findViewById(R.id.listViewParseXml);
    }

    private void parseXml() throws XmlPullParserException, IOException {

        InputStream isStream = getAssets().open("data.xml");

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

        parser.setInput(isStream, null);

        setData(parser);
    }

    private void setData(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        HashMap user = new HashMap<String, String>();
        int event = parser.getEventType();
        String tag = "";
        String text = null;

        while (event != XmlPullParser.END_DOCUMENT) {
            tag = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (tag.equals("user")) user = new HashMap();
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    switch (tag) {
                        case "name":
                            user.put("name", text);
                            break;
                        case "age":
                            user.put("age", text);
                            break;
                        case "user":
                            userList.add(user);
                            break;
                    }
                    break;
            }
            event = parser.next();
        }
        printUser(userList);
    }

    public void printUser(ArrayList<HashMap<String, String>> users) {
        String[] arr = {"name", "age"};
        int[] arrView = {R.id.txtName, R.id.txtAge};
        SimpleAdapter adapter = new SimpleAdapter(this, users, R.layout.item_parse_xml, arr, arrView);
        listViewParse.setAdapter(adapter);
    }
}