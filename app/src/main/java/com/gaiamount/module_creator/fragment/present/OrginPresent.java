package com.gaiamount.module_creator.fragment.present;

import android.content.Context;

import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.module_creator.fragment.OrganizationCreatorFrag;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by yukun on 17-3-10.
 */
public class OrginPresent implements PresentImpl{
    OrganizationCreatorFrag mView;
    Context context;
    public OrginPresent(OrganizationCreatorFrag mView,Context context) {
        this.mView = mView;
        this.context=context;
    }

    @Override
    public void getInfo(int pi) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(OrganizationCreatorFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                response.optJSONArray("a");
                Gson gson=new Gson();
                List<OrgBean> stringList = gson.fromJson(response.optJSONArray("a")+"", new TypeToken<List<OrgBean>>() {}.getType());
                mView.getInfo(stringList);
            }
        };
        PersonApiHelper.createOrngizationList(pi,"",context,handler);
    }
}
