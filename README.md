# Android App manages a remote lwm2m device

*TODO*

Introduction
-------------

The blog post [*TODO*](https://blog.samsungsami.io/development/iot/mobile/rules/2016/03/23/develop-an-android-app-to-manage-sami-rules.html) at http://artik.io/blog/cloud describes what the system does and how it is implemented.

This repository contains the following software:

 - An Android application running on the Android phone

Demo
-------------

 1. On the terminal, run the following command to start a [LWM2M Client emulator](https://github.com/artikcloud/artikcloud-lwm2m-c):

   ```
   %> akc_client -n -u coaps://coaps-api.artik.cloud:5686 -d YOUR_DEVICE_ID -t YOUR_DEVICE_TOKEN
   ```
 The device (lwm2m client) is now connected to ARTIK Cloud. Once the device receives and responds to the LWM2M operations from ARTIK Cloud, it prints out the info in the terminal. 
 1. Launch the app on your Android phone. 
 2. Login using your account. <br />
![Login](./img/screen_1_login.png)
 3. Click "READ PROPERTIES FROM ARTIK Cloud" to get the properties of the device from [Device Mirror](https://developer.artik.cloud/documentation/advanced-features/device-management.html#device-mirror) in ARTIK Cloud. Some properties are shown on the screen as the following: <br />
![getProperties](./img/screen_2_readPropFromAKC.png)
 3. Click one of the three buttons on the top to request reading the properties from, writing a new timezone to, or rebooting the remote LWM2M device. The following shows the case of writing "America/Los_Angeles" timezone to the device. The remote LWM2M device acts on operation as following output in the terminal:
 
   ```
   > Resource Changed: /3/0/15
   ```
On the Andriod app, click "READ PROPERTIES FROM ARTIK Cloud" and observe that the response from ARTIK Cloud confirms that the timezone has changed on device and on Device Mirror of ARTIK Cloud.<br />
![writeToDevice](./img/screen_3_writePropToDevice.png)
 4. Go to Developer Dashboard and see the status of the tasks initiated by the Android app as the following:<br />
![Task status in Developer Dashboard](./img/devdashboard_task_result.png)

Setup / Installation
-------------
*TODO*

More about ARTIK Cloud
---------------------

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-spec

Check out advanced sample applications at https://developer.artik.cloud/documentation/samples/

To create and manage your services and devices on ARTIK Cloud, create an account at https://developer.artik.cloud

Also see the ARTIK Cloud blog for tutorials, updates, and more: http://artik.io/blog/cloud

License and Copyright
---------------------

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2017 Samsung Electronics Co., Ltd.
