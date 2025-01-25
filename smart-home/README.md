# Group 13 - Lab H01
## Members:

#### Armaan Das (adas5)

#### Farhan Rashid (frashid2)

#### Lingfeng Zhu (lingfen1)

#### Michael Shi (wenjian2)


# tartan

The Tartan SmartHome Platform
---
This Dropwizard appllication is a RESTful service to control the Tartan SmartHome platform. 
This depends on the IoTController library.

How to start the tartan application
---

In order for this to run properly, MySQL and one or more House Simulator (Hubs) have to be running on the 
system.

1. Inside the Platform folder, run `./gradlew shadowJar` to build your application
1. Start application with `./gradlew run`
1. To check that your application is running enter url `http://localhost:8080/smarthome/state/mse`
