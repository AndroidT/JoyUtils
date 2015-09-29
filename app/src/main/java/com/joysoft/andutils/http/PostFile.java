package com.joysoft.andutils.http;

import android.content.Context;
import android.os.AsyncTask;

import com.joysoft.andutils.common.StreamUtils;
import com.joysoft.andutils.lg.Lg;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传:
 * <br>没有使用volley
 * @author fengmiao
 *
 */
public abstract class PostFile extends AsyncTask<HashMap<String, String>, Void, String>{

	Context context;
	String path;
	
	@SuppressWarnings("unchecked")
	public PostFile(Context context, String path, HashMap<String, String> params){
		this.context = context;
		this.path = path;
		this.execute(params);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		onPostRurn(result);
	}
	
	@Override
	protected String doInBackground(HashMap<String, String>... params) {
		return postFile(path, params[0]);
	}
	
	
	public abstract void onPostRurn(String result);
	
	
	/**
	 * 上传文件:
	 * @param url
	 * @param params
	 *            上传图片时使用key+后缀"IMAGEPATH"
	 * @return
	 */
	public String postFile(String url, HashMap<String, String> params) {
		try {
			
			//文件上传使用httpMime而没有使用volley
			MultipartEntity multipartEntity = getMultipartEntity(params);

			HttpPost post = new HttpPost(url);
			post.setEntity(multipartEntity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				InputStream in = response.getEntity().getContent();
				return StreamUtils.inToStr(in);
			} else {
				Lg.e("response.code != 200");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public MultipartEntity getMultipartEntity(Map<String, String> params) {
		try {

			MultipartEntity multipartEntity = new MultipartEntity();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				if (!key.endsWith("IMAGEPATH")) {
					StringBody body = new StringBody(entry.getValue(),
							Charset.forName("utf-8"));
					multipartEntity.addPart(key, body);
				} else {
					key = key.replace("IMAGEPATH", "").trim();
					File imgPath = new File(entry.getValue());
					FileBody fileBody = new FileBody(imgPath, "image/*",
							"utf-8");
					multipartEntity.addPart(key, fileBody);
				}

				StringBody body = new StringBody(entry.getValue(),
						Charset.forName("utf-8"));
				multipartEntity.addPart(entry.getKey(), body);
			}
			return multipartEntity;

		} catch (Exception e) {
			Lg.e(e.toString());
		}

		return null;

	}

}