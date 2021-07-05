package com.gads.leaderboad.Services;

import com.gads.leaderboad.Model.Submit_info;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoogleFormsFetchService {
    @POST("1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse")
    @FormUrlEncoded
    Call<Submit_info> submit_form(
            @Field("entry.1824927963") String Email_Address,
            @Field("entry.1877115667") String First_Name,
            @Field("entry.261820127") String Last_Name,
            @Field("entry.284483984") String Link_to_project

    );
}
