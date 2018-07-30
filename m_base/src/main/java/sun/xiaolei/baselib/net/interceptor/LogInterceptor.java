package sun.xiaolei.baselib.net.interceptor;

import com.centalineproperty.basemodule.BuildConfig;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (BuildConfig.DEBUG) {
            long t1 = System.nanoTime();
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            double time = (t2 - t1) / 1e6d;
            MediaType contentType = response.body().contentType();
            String bodyString = response.body().string();
            String msg = request.method() + "\nurl->" + request.url()
                    + "\ntime->" + time
                    + "ms\nheaders->" + request.headers()
                    + "\nresponse code->" + response.code()
                    + "\nresponse headers->" + response.headers()
                    + "\nbody->" + bodyString;
            if (request.method().equals("POST")) {
                Request copyRequest = request.newBuilder().build();
                Buffer buffer = new Buffer();
                copyRequest.body().writeTo(buffer);
                Logger.d("request params:" + buffer.readUtf8());
                Logger.d(msg);
            } else {
                Logger.d(msg);
            }
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return chain.proceed(request);
        }
    }
}
