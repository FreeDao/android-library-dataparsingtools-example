package com.xcj.demo.dataanalyticaldemo;

import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.EncodingUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.xcj.android.dat.xml.domparse.AndroidXML;
import com.xcj.android.dat.xml.domparse.XMLElement;
import com.xcj.android.dat.xml.parse.Node;
import com.xcj.android.dat.xml.parse.TreeBuilder;
import com.xcj.android.dat.xml.parse.XMLUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainAcivity extends Activity {
	private static final String TAG = MainAcivity.class.getSimpleName();
	
	public static final String ENCODING = "UTF-8";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}

	
	private void initView(){
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = getFromAssets("data.xml");
//				Log.d(TAG, "---------------->content: "+content);
				parse(content);  //效率更高
				domparse(content);
				
			}
		});
	}
	
	/**
	 * PULL解析方式的思路
	 * @param content
	 */
	private void parse(String content){
		long startTime = System.currentTimeMillis();
		Node node = TreeBuilder.parseTree(content);
//		Node headerNode = node.getFirstChildrenByName("breakfast_menu");
		if(node != null){
//			Log.d(TAG, "--------->node is not null");
			Vector<Node> vec = node.getChildrenByName("food");
			int count  = vec.size();
			for(int i=0;i<count;i++){
				Node subNode = vec.elementAt(i);
				String name = XMLUtil.getChildText(subNode, "name");
				String price = XMLUtil.getChildText(subNode, "price");
				String description = XMLUtil.getChildText(subNode, "description");
				int calories = XMLUtil.getChildInt(subNode, "calories");
//				Log.d(TAG, "---------------->name: "+name);
//				Log.d(TAG, "---------------->price: "+price);
//				Log.d(TAG, "---------------->description: "+description);
//				Log.d(TAG, "---------------->calories: "+calories);
			}
		}
		Log.d(TAG, "--------->parse spend time: "+(System.currentTimeMillis()-startTime)); 
	}
	
	
	private void domparse(String content){
		long startTime = System.currentTimeMillis();
		AndroidXML aXml = AndroidXML.parse(content);
		XMLElement rootElement = aXml.getRootElement();
		if(rootElement != null){
			XMLElement xe = rootElement.getElementByPath("breakfast_menu");
			int count = (xe == null) ? 0 : xe.getElementCount();
			for (int i = 0; i < count; i++) {
				XMLElement foodElement = xe.getElement(i);
				String name = foodElement.getTextByPath("food/name");
				String price = foodElement.getTextByPath("food/price");
				String description = foodElement.getTextByPath("food/description");
				int calories = foodElement.getIntByPath("food/calories");
//				Log.d(TAG, "---------------->name: "+name);
//				Log.d(TAG, "---------------->price: "+price);
//				Log.d(TAG, "---------------->description: "+description);
//				Log.d(TAG, "---------------->calories: "+calories);
			}
		}
		Log.d(TAG, "--------->domparse spend time: "+(System.currentTimeMillis()-startTime)); 
	}
	
	/**
	 * 从assets 文件夹中获取文件并读取数据  
	 * @param fileName
	 * @return
	 */
	public String getFromAssets(String fileName){  
		String result = "";  
		try {  
			InputStream in = getResources().getAssets().open(fileName);  
			//获取文件的字节数  
			int lenght = in.available();  
			//创建byte数组  
			byte[]  buffer = new byte[lenght];  
			//将文件中的数据读到byte数组中  
			in.read(buffer);  
			result = EncodingUtils.getString(buffer, ENCODING);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return result;  
	}  
	
	
	
	 /**
	  * 从resources中的raw 文件夹中获取文件并读取数据  
	  * @return
	  */
    public String getFromRaw(int rawId){  
        String result = "";  
            try {  
                InputStream in = getResources().openRawResource(rawId);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = EncodingUtils.getString(buffer, ENCODING);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return result;  
    }  
}
