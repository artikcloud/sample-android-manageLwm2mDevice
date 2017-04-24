# Android App manages a remote lwm2m device

Let's build an Android application to manage remote devices that are connected to ARTIK Cloud via LWM2M.

After completing this sample, you will learn how to use [ARTIK Cloud device management task APIs](https://developer.artik.cloud/documentation/device-management/device-management-tasks.html) to:
- read device properties from a remote device 
- write device properties to a remote device
- execute on a remote device (for example reboot)

## Requirements
- Android Studio
- [ARTIK Cloud Java SDK](https://github.com/artikcloud/artikcloud-java)
- LWM2M Client Simulator from [LWM2M C Client SDK](https://github.com/artikcloud/artikcloud-lwm2m-c)

## Setup / Installation

### Setup at ARTIK Cloud

 1. [Create a device type](https://developer.artik.cloud/documentation/tools/web-tools.html#creating-a-device-type) (or use the one you already own) in the [Developer Dashboard](https://developer.artik.cloud/).   

 2. Enable [Device Management Device Properties](https://developer.artik.cloud/documentation/device-management/manage-devices-using-lwm2m.html#enable-device-properties) for your device type. You do this in the [Device Type Dashboard](https://developer.artik.cloud/dashboard/devicetypes)—> Select Your Device Type —> Select Device Management —> Click "Enable Device Properties".

 3. Follow [these instructions](https://developer.artik.cloud/documentation/tools/web-tools.html#creating-an-application) to create an application. For this Android app, select the following:
  - Under “AUTHORIZATION METHODS”, check “Client credentials, auth code, implicit”.
  - Set “AUTH REDIRECT URL” to myapp://redirect.
  - Under “PERMISSIONS”, check “Read” for “Profile”.
 
 Get the [client ID](https://developer.artik.cloud/documentation/tools/web-tools.html#how-to-find-your-application-id), which you will need later.

 4. At [My ARTIK Cloud](https://my.artik.cloud/), [Connect a device](https://developer.artik.cloud/documentation/tools/web-tools.html#connecting-a-device) (or use the one you already own) of the above device type. Get the [device ID, device type ID, and token](https://developer.artik.cloud/documentation/tools/web-tools.html#managing-a-device-token), which you will need later.

### Setup LWM2M client simulator

- Clone [LWM2M C Client SDK](https://github.com/artikcloud/artikcloud-lwm2m-c).
- Follow the instructions there to build **akc_client**. 

### Setup Android project

 1. Clone this sample repository
 2. In Android Studio, import this project. 
 3. Update Config.java file. In this file, replace the placeholders with your own device ID, device type ID, and client ID, which you have obtained when [setting up in ARTIK Cloud](#setup-at-artik-cloud).

Now build the project, which will download the ARTIK Cloud SDK JAR from [Maven Central Repository](http://search.maven.org/). Deploy the APK to an Android phone. You can play with the app and LWM2M client simulator like in the demo. 

## Demo

Read [this blog post](https://www.artik.io/blog/2017/04/managing-factory-devices-artik-cloud/) for the full details.

## More about ARTIK Cloud

If you are not familiar with ARTIK Cloud, we have extensive documentation at https://developer.artik.cloud/documentation

The full ARTIK Cloud API specification can be found at https://developer.artik.cloud/documentation/api-spec

Check out advanced sample applications at https://developer.artik.cloud/documentation/samples/

To create and manage your services and devices on ARTIK Cloud, create an account at https://developer.artik.cloud

Also see the ARTIK Cloud blog for tutorials, updates, and more: http://artik.io/blog/cloud

## License and Copyright

Licensed under the Apache License. See [LICENSE](LICENSE).

Copyright (c) 2017 Samsung Electronics Co., Ltd.
