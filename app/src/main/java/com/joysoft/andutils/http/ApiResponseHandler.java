package com.joysoft.andutils.http;

import com.joysoft.andutils.http.base.ResponseHandler;
import com.joysoft.andutils.http.base.ResponseState;
import com.joysoft.andutils.lg.Lg;

import org.json.JSONObject;

/**
 *
 * Created by fengmiao on 15/9/7.
 */
public abstract class ApiResponseHandler extends ResponseHandler<JSONObject,JSONObject> {

    int resultCode;

    @Override
    public void setResultData(JSONObject jsonObject) {
        try{
           resultCode = Integer.parseInt(jsonObject.getString("status"));

            JSONObject content = (JSONObject)jsonObject.get("rows");

            if(content == null){
                onError((isOk() && content == null) ? ResponseState.ERROR_DATA_NULL : ResponseState.ERROR_DATA_PARSE);
            }else
                onCompleteParse(content);

        }catch (Exception e){
            Lg.e(e.toString());
            onError(ResponseState.ERROR_DATA_PARSE);
        }

    }

    @Override
    public boolean isOk() {
        return resultCode == 200;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }


}
