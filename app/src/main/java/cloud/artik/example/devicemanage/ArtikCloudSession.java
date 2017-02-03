/*
 * Copyright (C) 2016 Samsung Electronics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.artik.example.devicemanage;

import android.util.Log;

import java.util.ArrayList;

import cloud.artik.api.DevicesManagementApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiClient;

public class ArtikCloudSession {
    private final static String TAG = ArtikCloudSession.class.getSimpleName();

    // Copy them from the corresponding application in the Developer Dashboard
    public static final String CLIENT_ID = "f64403a5d43846c1af86f4f27617a6b7";// public static final String CLIENT_ID = "<YOUR CLIENT ID>";
    public static final String REDIRECT_URL = "myapp://redirect";

    // Copy them from the Device Info screen in My ARTIK Cloud
    public static final String DEVICE_TYPE_ID = "dt1fb9d4f510f34806ae250bd15ed1b3b8";
    public static final String DEVICE_ID = "26464ec078b346dfbd7c5cdd1e92ec67";

    public static final String ARTIK_CLOUD_AUTH_BASE_URL = "https://accounts.artik.cloud";

    private static ArtikCloudSession ourInstance = new ArtikCloudSession();

    private ArrayList<String> mDeviceIDArray;

    private UsersApi mUsersApi = null;
    private DevicesManagementApi mDevicesManagementApi = null;
    private String mAccessToken = null;
    private String mUserId = null;

    public static ArtikCloudSession getInstance() {
        return ourInstance;
    }

    private ArtikCloudSession() {
        mDeviceIDArray = new ArrayList<>();
        mDeviceIDArray.add(0, DEVICE_ID);
    }

    public void setAccessToken(String token) {
        if (token == null || token.length() <= 0) {
            Log.e(TAG, "Attempt to set an invalid token");
            mAccessToken = null;
            return;
        }
        mAccessToken = token;
    }

    public void setupArtikCloudRestApis() {
        ApiClient apiClient = new ApiClient();
        apiClient.setAccessToken(mAccessToken);

        mUsersApi = new UsersApi(apiClient);
        mDevicesManagementApi = new DevicesManagementApi(apiClient);
    }

    public UsersApi getUsersApi() {
        return mUsersApi;
    }

    public DevicesManagementApi getDevicesManagementApi() {
        return mDevicesManagementApi;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getAuthorizationRequestUri() {
        //https://accounts.artik.cloud/authorize?client=mobile&client_id=xxxx&response_type=token&redirect_uri=android-app://redirect
        return ARTIK_CLOUD_AUTH_BASE_URL + "/authorize?client=mobile&response_type=token&" +
                "client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL;
    }

    public void reset() {
        mUsersApi = null;
        mAccessToken = null;
        mUserId = null;
        mDevicesManagementApi = null;
    }

    public void setUserId(String uid) {
        if (uid == null || uid.length() <= 0) {
            Log.w(TAG, "setUserId() get null uid");
        }
        mUserId = uid;
    }

    public ArrayList<String> getDeviceIDArray() {
        return mDeviceIDArray;
    }

    public boolean canCallAKCApis() {
        return mUserId != null && mAccessToken != null;
    }

}