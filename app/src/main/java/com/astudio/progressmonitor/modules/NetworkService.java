package com.astudio.progressmonitor.modules;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;

@Keep
public class NetworkService {

    //пагинация RecyclerView с Retrofit https://dajver.blogspot.com/2017/06/blog-post.html
    //TODO: авторизация с Retrofit http://javaway.info/ispolzovanie-retrofit-2-v-prilozheniyah-android/
    // Аутентификация с помощью OkHttp перехватчиков  https://habr.com/ru/post/428736/
    //TODO: типы запросов https://fastandroids.wordpress.com/2016/07/28/retrofit-2-query/

    // RxJava и Retrofit на Android, учитывая поворот экрана https://habr.com/ru/post/305478/

    // https://habr.com/ru/post/429058/
    // https://ru.stackoverflow.com/questions/601082/android-%D0%BC%D0%B5%D1%82%D0%BE%D0%B4-findviewbyidint
    // https://dajver.blogspot.com/2013/02/json.html

    //JSON-RPC https://habr.com/ru/post/150803/
    // https://github.com/foglcz/JSONRpc2
    // https://www.codeforest.net/android-json-rpc-client-and-php-zend-framework-server
    // https://meline.lviv.ua/development/other/rest-%D0%B8-rpc-%D0%B4%D0%BB%D1%8F-http-api/

    private static NetworkService mInstance;
    private static final String BASE_URL = "https://pmapi.femalescript.ru";
    public Retrofit mRetrofit;
    //private OnConnectionTimeoutListener mTimeoutListener;


    //public void setTimeoutListener(OnConnectionTimeoutListener timeoutListener) {
    //    mTimeoutListener = timeoutListener;
    //}

    //public interface OnConnectionTimeoutListener {
    //    void onConnectionTimeout();
    //}

    @Keep
    private NetworkService() {

        //TODO: уровень логгирования BODY - максимальный, на релиз изменить! 17-04-2021 из-за неподдерживаемых API на релиз нужно полностью отключать логгер-перехватчик
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) // на ситуацию когда истек таймаут соединения сервера, добавил 09-10-2020
                .addInterceptor(interceptor);
                //;

        //client.connectTimeout(60, TimeUnit.SECONDS)
        //       .writeTimeout(60, TimeUnit.SECONDS)
        //       .readTimeout(60, TimeUnit.SECONDS);

        // добавить перехватчика IOException, не работает вроде пока
//        client.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(@NonNull Chain chain) throws IOException {
//                return onOnIntercept(chain);
//            }
//        });


        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(client.build())
                .build();
    }


    @Keep
    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }


    private Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        // https://stackoverflow.com/questions/29921667/retrofit-2-catch-connection-timeout-exception
        try {
            Response response = chain.proceed(chain.request());
            //String content = UtilityMethods.convertResponseToString(response);
            String content = Objects.requireNonNull(response.body()).string();
            //Log.e(TAG, "lastCalledMethodName" + " - " + content);
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
        }
        catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            //if(mTimeoutListener != null)
                //mTimeoutListener.onConnectionTimeout();
        }
        return chain.proceed(chain.request());
    }


    // для исключения при конвертировании из модели полей без @expose
    private Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }


    public JSONPlaceHolderApi getJSONApi() {
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }




}

/*
private OkHttpClient createCachedClient(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "cache_file");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.interceptors().add(
                new Interceptor() {
                    @Override
                    public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String cacheHeaderValue = isOnline(context)
                                ? "public, max-age=2419200"
                                : "public, only-if-cached, max-stale=2419200" ;
                        Request request = originalRequest.newBuilder().build();
                        com.squareup.okhttp.Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        );
        okHttpClient.networkInterceptors().add(
                new Interceptor() {
                    @Override
                    public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String cacheHeaderValue = isOnline(context)
                                ? "public, max-age=2419200"
                                : "public, only-if-cached, max-stale=2419200" ;
                        Request request = originalRequest.newBuilder().build();
                        com.squareup.okhttp.Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        );
        return okHttpClient;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;


        }

        OkHttpClient okHttpClient = createCachedClient(TabHostActivity.this);
Retrofit retrofit=new Retrofit.Builder()
         .client(okHttpClient)
         .baseUrl(API)
         .addConverterFactory(GsonConverterFactory
         .create()).build();


         Добавьте это разрешение в манифест

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
