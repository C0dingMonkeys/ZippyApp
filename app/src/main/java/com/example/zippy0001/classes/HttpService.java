package com.example.zippy0001.classes;

import com.example.zippy0001.FileModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface HttpService {
    @Multipart
    @POST("Android/uploadimg.php")
    Call<FileModel> callUploadApi(@Part MultipartBody.Part image, @Part("user_id") String idUsuario);

    @Multipart
    @POST("Android/criarPostagem.php")
    Call<FileModel> callUploadApiPost(@Part MultipartBody.Part image,
                                       @Part ("idCliente")RequestBody idCliente,
                                       @Part ("nomeProduto")RequestBody nomeProduto,
                                       @Part ("valorProduto")RequestBody precoProd,
                                       @Part ("linkReferencia")RequestBody linkProd,
                                       @Part ("paisOrigem")RequestBody paisOrigem,
                                       @Part ("cidadeOrigem")RequestBody cidadeOrigem,
                                       @Part ("estadoOrigem")RequestBody estadoOrigem,
                                       @Part ("paisDestino")RequestBody paisDestino,
                                       @Part ("cidadeDestino")RequestBody cidadeDestino,
                                       @Part ("estadoDestino")RequestBody estadoDestino,
                                       @Part ("caixa")RequestBody caixa);
}
