/*
 * Copyright (C) 2017 Samsung Electronics Co., Ltd.
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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiException;
import cloud.artik.model.MetadataEnvelope;
import cloud.artik.model.Task;
import cloud.artik.model.TaskEnvelope;
import cloud.artik.model.TaskParameters;
import cloud.artik.model.TaskRequest;
import cloud.artik.model.User;
import cloud.artik.model.UserEnvelope;


public class DeviceManagementActivity extends Activity {
    private static final String TAG = DeviceManagementActivity.class.getSimpleName();

    private Button mReadBtn;
    private Button mWriteBtn;
    private Button mRebootBtn;
    private Button mGetPropertiesBtn;
    private TextView mGetPropertiesCallResponse;
    private TextView mDMTaskAPICallResponse;

    private ApiCallback<TaskEnvelope> mCallback = null;

    private int mTimezoneIdx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);

        mDMTaskAPICallResponse = (TextView)findViewById(R.id.dm_apicall_response);
        mGetPropertiesCallResponse = (TextView)findViewById(R.id.get_properties_call_response);

        setTitle(R.string.device_management_screen_title);

        mWriteBtn = (Button)findViewById(R.id.writeBtn);
        mWriteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.v(TAG, ": \"Write device property\" button is clicked.");
                    createWriteTask();
                } catch (Exception e) {
                    Log.v(TAG, "Run into Exception");
                    e.printStackTrace();
                }
            }
        });

        mReadBtn = (Button)findViewById(R.id.readBtn);
        mReadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.v(TAG, ": \"Read device property\" button is clicked.");
                    createReadTask();
                } catch (Exception e) {
                    Log.v(TAG, "Run into Exception");
                    e.printStackTrace();
                }
            }

            }
        );

        mRebootBtn = (Button)findViewById(R.id.rebootBtn);
        mRebootBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.v(TAG, ": \"Reboot device\" button is clicked.");
                    createRebootTask();
                } catch (Exception e) {
                    Log.v(TAG, "Run into Exception");
                    e.printStackTrace();
                }
            }
        });

        mGetPropertiesBtn = (Button)findViewById(R.id.getPropertiesBtn);
        mGetPropertiesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.v(TAG, ": \"Get Properties\" button is clicked.");
                    getPropertiesFromAKC();
                } catch (Exception e) {
                    Log.v(TAG, "Run into Exception");
                    e.printStackTrace();
                }
            }
        });

        mCallback = new ApiCallback<TaskEnvelope>() {

            @Override
            public void onFailure(ApiException arg0, int arg1, Map<String, List<String>> arg2) {
                processFailure("task API callback onFailure", arg0);
            }

            @Override
            public void onSuccess(TaskEnvelope arg0, int arg1, Map<String, List<String>> arg2) {
                handleTaskAPISuccessOnUIThread(arg0);
            }

            @Override
            public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
            }

            @Override
            public void onUploadProgress(long arg0, long arg1, boolean arg2) {
            }

        };

        getUserInfo();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ArtikCloudSession.getInstance().canCallAKCApis()) {
            enableAllButtons();
        }
    }


    @Override
    public void onBackPressed()
    {
        // Disable going back to the previous screen
    }

    private void getUserInfo()
    {
        final String tag = TAG + " getSelfAsync";
        try {
            ArtikCloudSession.getInstance().getUsersApi().getSelfAsync(new ApiCallback<UserEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> map) {
                    processFailure(tag, exc);
                }

                @Override
                public void onSuccess(UserEnvelope result, int statusCode, Map<String, List<String>> map) {
                    Log.v(tag, " onSuccess() self name = " + result.getData().getFullName());
                    handleGetUserInfoSuccessOnUIThread(result.getData());
                }

                @Override
                public void onUploadProgress(long bytes, long contentLen, boolean done) {
                }

                @Override
                public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                }
            });
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

    private void handleGetUserInfoSuccessOnUIThread(final User user) {
        if (user == null) {
            return;
        }
        ArtikCloudSession.getInstance().setUserId(user.getId());

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ArtikCloudSession.getInstance().canCallAKCApis()) {
                    enableAllButtons();
                }
            }
        });
    }

////// Device Management TASK APIs
    private void createReadTask() {
        final String tag = TAG + " createReadTaskAsync";

        TaskRequest readTask = new TaskRequest();
        readTask.dtid(Config.DEVICE_TYPE_ID);
        readTask.dids(ArtikCloudSession.getInstance().getDeviceIDArray());
        readTask.taskType("R");
        readTask.setProperty("deviceProperties.device"); //set to read all properties from device
        try {
            ArtikCloudSession.getInstance().getDevicesManagementApi().createTasksAsync(readTask, mCallback);  // async sample call
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

    private void createWriteTask() {
        final String tag = TAG + " createWriteTaskAsync";

        TaskRequest writeDeviceTaskRequest = new TaskRequest();
        writeDeviceTaskRequest.dtid(Config.DEVICE_TYPE_ID);
        writeDeviceTaskRequest.dids(ArtikCloudSession.getInstance().getDeviceIDArray());
        writeDeviceTaskRequest.taskType("W");
        writeDeviceTaskRequest.setProperty("deviceProperties.device.timezone");
        writeDeviceTaskRequest.taskParameters(new TaskParameters().value(getNewTimezone()));
        try {
            ArtikCloudSession.getInstance().getDevicesManagementApi().createTasksAsync(writeDeviceTaskRequest, mCallback);  // async sample call
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

///// Device management APIs other than TASK APIs
    private void createRebootTask() {
        final String tag = TAG + " createRebootTaskAsync";

        TaskRequest rebootTaskRequest = new TaskRequest();
        rebootTaskRequest.dtid(Config.DEVICE_TYPE_ID);
        rebootTaskRequest.dids(ArtikCloudSession.getInstance().getDeviceIDArray());
        rebootTaskRequest.taskType("E");
        rebootTaskRequest.setProperty("deviceProperties.device.reboot");
        try {
            ArtikCloudSession.getInstance().getDevicesManagementApi().createTasksAsync(rebootTaskRequest, mCallback);  // async sample call
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

    private void getPropertiesFromAKC() {
        final String tag = TAG + " getPropertiesAsync";
        String did = ArtikCloudSession.getInstance().getDeviceIDArray().get(0);
        try {
            ArtikCloudSession.getInstance().getDevicesManagementApi().getPropertiesAsync(did, true, new ApiCallback<MetadataEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> responseHeaders) {
                    processFailure(tag, exc);
                }

                @Override
                public void onSuccess(MetadataEnvelope result, int statusCode, Map<String, List<String>> responseHeaders) {
                    handleGetPropertiesSuccessOnUIThread(result);
                }

                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                }

                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                }
            });
        } catch (ApiException exc) {
                processFailure(tag, exc);
        }
    }

///// Helpers
    private void processFailure(final String context, Exception exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showErrorOnUIThread(context+errorDetail, DeviceManagementActivity.this);
    }

    static void showErrorOnUIThread(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

    private void handleTaskAPISuccessOnUIThread(final TaskEnvelope taskEnvelope) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, taskEnvelope.toString());
                mDMTaskAPICallResponse.setText(null);//clean previous text

                String prettyResponse = "";
                try {
                    Task t = taskEnvelope.getData();

                    // selectively print out some of the fields in the response
                    prettyResponse = "taskType: " + t.getTaskType() + "\n"
                            + "property: " + t.getProperty() + "\n"
                            + "dids: " + t.getDids().toString() + "\n"
                            + "taskParameters: " + t.getTaskParameters().toString() + "\n"
                            + "createdOn: " + t.getCreatedOn() + "\n"
                            + "status: " + t.getStatus() + "\n";

                } catch (Exception exc) {
                    processFailure(TAG + " handleTaskAPISuccessOnUIThread ", exc);

                }
                mDMTaskAPICallResponse.setText(prettyResponse);
            }
        });
    }

    private void enableAllButtons() {
        mReadBtn.setEnabled(true);
        mWriteBtn.setEnabled(true);
        mRebootBtn.setEnabled(true);
        mGetPropertiesBtn.setEnabled(true);
    }

    private String getNewTimezone() {
        String timezone = "America/Los_Angeles";
        switch (mTimezoneIdx % 3) {
            case 1:
                timezone = "Asia/Shanghai";
                break;
            case 2:
                timezone = "Europe/Paris";
                break;
            default:
                break;
        }
        mTimezoneIdx++;
        return timezone;
    }

    /* Example of device properties JSON
    {
    "device": {
        "memoryTotal": {
            "value": 128.0,
            "ts": 1.485974853303E12
        },
        "batteryStatus": {
            "value": 6.0,
            "ts": 1.485974853303E12
        },
        "softwareVersion": {
            "value": "Software C SDK",
            "ts": 1.485974853303E12
        },
        "hardwareVersion": {
            "value": "Hardware C SDK",
            "ts": 1.485974853303E12
        },
        "deviceType": {
            "value": "DeviceType C SDK",
            "ts": 1.485974853303E12
        },
        "supportedBindingAndModes": {
            "value": "U",
            "ts": 1.485974853303E12
        },
        "timezone": {
            "value": "Pacific/California",
            "ts": 1.485974853303E12
        },
        "utcOffset": {
            "value": "+02:00",
            "ts": 1.485974853303E12
        },
        "currentTime": {
            "value": 1.485974852E12,
            "ts": 1.485974853303E12
        },
        "errorCode": {
            "value": [0.0],
            "ts": 1.485974853303E12
        },
        "memoryFree": {
            "value": 15.0,
            "ts": 1.485974853303E12
        },
        "batteryLevel": {
            "value": 100.0,
            "ts": 1.485974853303E12
        },
        "powerSourceCurrent": {
            "value": [125.0, 900.0],
            "ts": 1.485974853303E12
        },
        "powerSourceVoltage": {
            "value": [3800.0, 5000.0],
            "ts": 1.485974853303E12
        },
        "availablePowerSources": {
            "value": [1.0, 5.0],
            "ts": 1.485974853303E12
        },
        "firmwareVersion": {
            "value": "1.0",
            "ts": 1.485974853303E12
        },
        "serialNumber": {
            "value": "345000123",
            "ts": 1.485974853303E12
        },
        "modelNumber": {
            "value": "Lightweight M2M Client",
            "ts": 1.485974853303E12
        },
        "manufacturer": {
            "value": "SAMSUNG",
            "ts": 1.485974853303E12
        }
    }
    */
   private void handleGetPropertiesSuccessOnUIThread(final MetadataEnvelope metadataEnvelope) {
       Log.d(TAG, metadataEnvelope.toString());
       this.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               String resultStr = "";
               String TIMEZONE = "timezone";
               String BATTERY_LEVEL = "batteryLevel";
               String FIRMWARE_VERSION = "firmwareVersion";
               String SERIAL_NUMBER = "serialNumber";
               try {
                   Object deviceProperties = metadataEnvelope.getData().get("deviceProperties");
                   Gson gson = new Gson();
                   String dpStr = gson.toJson(deviceProperties);
                   JSONObject dpJson = new JSONObject(dpStr);
                   Object d = dpJson.get("device");
                   JSONObject dJson = new JSONObject(d.toString());
                   Object timezoneProp = dJson.get(TIMEZONE);
                   Object batteryLevel = dJson.get(BATTERY_LEVEL);
                   Object firmwareVersionProp = dJson.get(FIRMWARE_VERSION);
                   Object serialNumberProp = dJson.get(SERIAL_NUMBER);
                   resultStr = resultStr + TIMEZONE + ":" + timezoneProp.toString() + "\n"
                           + BATTERY_LEVEL + ":" + batteryLevel.toString() + "\n"
                           + FIRMWARE_VERSION + ":" + firmwareVersionProp.toString() + "\n"
                           +  SERIAL_NUMBER + ":" + serialNumberProp + "\n";
               } catch (Exception e) {
                   Log.v("", "handleGetProperties run into Exception");
                   Log.v("", "resultStr " + resultStr);
                   e.printStackTrace();
               }
               mGetPropertiesCallResponse.setText(null);//clean previous text
               mGetPropertiesCallResponse.setText(resultStr);
            }
       });
   }

}
