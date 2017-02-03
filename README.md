# Android App manages manages a remote lwm2m device

*TODO*

Introduction
-------------

The blog post [*TODO*](https://blog.samsungsami.io/development/iot/mobile/rules/2016/03/23/develop-an-android-app-to-manage-sami-rules.html) at http://artik.io/blog/cloud describes what the system does and how it is implemented.

This repository contains the following software:

 - An Android application running on the Android phone

Demo
-------------

 1. Launch the app on your Android phone. 
 2. Login using your account. <br />
![Login](./img/screen_1_login.png)
 3. Click the button to get the properties of the device from ARTIK Cloud. Some properties are shown on UI as the following:
![getProperties](./img/screen_2_readPropFromAKC.png)
 3. Click one of the buttons to request reading the properties from, writing timezone property to, and reboot the remote LWM2M device. The following is the example of writing a new timezone property to the device and then confirm that the property has been updated from device:
![writeToDevice](./img/screen_3_writePropToDevice.png)

Android Application
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
