package cn.yshstudio.originalproduct.pages.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import cn.yshstudio.originalproduct.inc.Ini;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static Retrofit mRetrofit;
    private static APIService mApiService;

    public static Retrofit newInstence() {
        if(mRetrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(Ini._HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(Ini._HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS).build();
            mRetrofit = new Retrofit.Builder()
                    .client(client)//添加一个client,不然retrofit会自己默认添加一个
                    .baseUrl("http://www.salewell.com/pages/")
                    .addConverterFactory(new StringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static APIService getApiService(){
        newInstence();
        if(mApiService == null) {
            mApiService = mRetrofit.create(APIService.class);
        }
        return mApiService;
    }

    static class FileRequestBodyConverterFactory extends Converter.Factory {
        @Override
        public Converter<File, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new FileRequestBodyConverter();
        }
    }

    static class FileRequestBodyConverter implements Converter<File, RequestBody> {

        @Override
        public RequestBody convert(File file) throws IOException {
            return RequestBody.create(MediaType.parse("image/*"), file);
        }
    }

    public static class StringConverterFactory extends Converter.Factory {

        public static StringConverterFactory create() {
            return new StringConverterFactory();
        }

        private StringConverterFactory() {

        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            return new StringResponseBodyConverter();
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new StringRequestBodyConverter();
        }

    }

    public static class StringResponseBodyConverter implements Converter<ResponseBody, String> {
        @Override
        public String convert(ResponseBody value) throws IOException {
            try {
                return value.string();
            } finally {
                value.close();
            }
        }
    }

    public static class StringRequestBodyConverter  implements Converter<String, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        StringRequestBodyConverter() {

        }

        @Override public RequestBody convert(String value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            writer.write(value);
            writer.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

}
