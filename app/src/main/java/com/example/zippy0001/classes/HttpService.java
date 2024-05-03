package com.example.zippy0001.classes;

import com.example.zippy0001.FileModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface HttpService {
    @Multipart
    @POST("Android/uploadimg.php")
    Call<FileModel> callUploadApi(@Part MultipartBody.Part image, @Part("user_id") String idUsuario);

}
