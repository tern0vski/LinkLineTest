package com.ternovski.linklinetest.websocket;


import android.os.AsyncTask;

import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Vadim on 2017.
 */

public class WebSocketClient {

    private static String WSS_URL = "wss://linkline.com.ua:4000/wss";
    private WebSocket webSocket = null;
    private ConnectionListener connectionListener;

    public WebSocketClient(ConnectionListener connectionListener, String login, String password) {
        this.connectionListener = connectionListener;
        createSocket(login, password);
    }

    private void createSocket(String login, String password) {
        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            WebSocketFactory webSocketFactory = new WebSocketFactory();
            webSocketFactory.setSSLSocketFactory(sslContext.getSocketFactory());
            webSocketFactory.setSSLContext(sslContext);

            webSocket = webSocketFactory.createSocket(WSS_URL);
            webSocket.setUserInfo(login, password);
            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) throws Exception {

                }

                @Override
                public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
                    connectionListener.onError();
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    connectionListener.onConnect();
                }


                @Override
                public void onError(WebSocket websocket, WebSocketException cause) {
                    connectionListener.onError();
                }

                @Override
                public void onDisconnected(WebSocket websocket,
                                           WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                           boolean closedByServer) {

                }

                @Override
                public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
                    connectionListener.onError();
                }

                @Override
                public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

                }
            });

        } catch (
                NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
        }

    }

    private class WebsocketConnection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... webSockets) {
            try {
                webSocket.connect();
                webSocket.sendText("hi");

            } catch (OpeningHandshakeException e) {
                e.printStackTrace();
                return false;
            } catch (HostnameUnverifiedException e) {
                e.printStackTrace();
                return false;
            } catch (WebSocketException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                connectionListener.onError();
            }
        }
    }

    public void connect() {
        WebsocketConnection wsConnection = new WebsocketConnection();
        wsConnection.execute();
    }
}
