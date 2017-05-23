# Groovy SDK

Version: profitbricks-sdk-groovy **1.0.0**

## Table of Contents

* [Description](#description)
* [Getting Started](#getting-started)
  * [Basic Objective](#basic-objective)
  * [Installation](#installation)
  * [Configuration](#configuration)
* [Reference](#reference)
  * [Data Centers](#data-centers)
    * [List Data Centers](#list-data-centers)
    * [Retrieve a Data Center](#retrieve-a-data-center)
    * [Create a Data Center](#create-a-data-center)
    * [Update a Data Center](#update-a-data-center)
    * [Delete a Data Center](#delete-a-data-center)
  * [Locations](#locations)
    * [List Locations](#list-locations)
    * [Get a Location](#get-a-location)
  * [Servers](#servers)
    * [List Servers](#list-servers)
    * [Retrieve a Server](#retrieve-a-server)
    * [Create a Server](#create-a-server)
    * [Update a Server](#update-a-server)
    * [Delete a Server](#delete-a-server)
    * [List Attached Volumes](#list-attached-volumes)
    * [Attach a Volume](#attach-a-volume)
    * [Retrieve an Attached Volume](#retrieve-an-attached-volume)
    * [Detach a Volume](#detach-a-volume)
    * [List Attached CD-ROMs](#list-attached-cd-roms)
    * [Attach a CD-ROM](#attach-a-cd-rom)
    * [Retrieve an Attached CD-ROM](#retrieve-an-attached-cd-rom)
    * [Detach a CD-ROM](#detach-a-cd-rom)
    * [Reboot a Server](#reboot-a-server)
    * [Start a Server](#start-a-server)
    * [Stop a Server](#stop-a-server)
  * [Images](#images)
    * [List Images](#list-images)
    * [Get an Image](#get-an-image)
  * [Volumes](#volumes)
    * [List Volumes](#list-volumes)
    * [Get a Volume](#get-a-volume)
    * [Create a Volume](#create-a-volume)
    * [Update a Volume](#update-a-volume)
    * [Delete a Volume](#delete-a-volume)
    * [Create a Volume Snapshot](#create-a-volume-snapshot)
    * [Restore a Volume Snapshot](#restore-a-volume-snapshot)
  * [Snapshots](#snapshots)
    * [List Snapshots](#list-snapshots)
    * [Get a Snapshot](#get-a-snapshot)
    * [Update a Snapshot](#update-a-snapshot)
    * [Delete a Snapshot](#delete-a-snapshot)
  * [IP Blocks](#ip-blocks)
    * [List IP Blocks](#list-ip-blocks)
    * [Get an IP Block](#get-an-ip-block)
    * [Create an IP Block](#create-an-ip-block)
    * [Delete an IP Block](#delete-an-ip-block)
  * [LANs](#lans)
    * [List LANs](#list-lans)
    * [Create a LAN](#create-a-lan)
    * [Get a LAN](#get-a-lan)
    * [Update a LAN](#update-a-lan)
    * [Delete a LAN](#delete-a-lan)
  * [Network Interfaces (NICs)](#network-interfaces)
    * [List NICs](#list-nics)
    * [Get a NIC](#get-a-nic)
    * [Create a NIC](#create-a-nic)
    * [Update a NIC](#update-a-nic)
    * [Delete a NIC](#delete-a-nic)
  * [Firewall Rules](#firewall-rules)
    * [List Firewall Rules](#list-firewall-rules)
    * [Get a Firewall Rule](#get-a-firewall-rule)
    * [Create a Firewall Rule](#create-a-firewall-rule)
    * [Update a Firewall Rule](#update-a-firewall-rule)
    * [Delete a Firewall Rule](#delete-a-firewall-rule)
  * [Load Balancers](#load-balancers)
    * [List Load Balancers](#list-load-balancers)
    * [Get a Load Balancer](#get-a-load-balancer)
    * [Create a Load Balancer](#create-a-load-balancer)
    * [Update a Load Balancer](#update-a-load-balancer)
    * [List Load Balanced NICs](#list-load-balanced-nics)
    * [Get a Load Balanced NIC](#get-a-load-balanced-nic)
    * [Associate NIC to a Load Balancer](#associate-nic-to-a-load-balancer)
    * [Remove a NIC Association](#remove-a-nic-association)
  * [Requests](#requests)
    * [List Requests](#list-requests)
    * [Get a Request Status](#get-a-request-status)
* [Example](#example)
* [Support](#support)
* [Testing](#testing)
* [Contributing](#contributing)

## Description

This Groovy library wraps the ProfitBricks Cloud API. All API operations are performed over a SSL/TLS secured connection and authenticated using your ProfitBricks portal credentials. The Cloud API can be accessed over the public Internet from any application that can send an HTTPS request and receive an HTTPS response.

This guide will show you how to programmatically perform common management tasks using the [Groovy SDK](https://github.com/profitbricks/profitbricks-sdk-groovy) for the ProfitBricks Cloud API.

Apache Groovy is an object-oriented programming language for the Java platform, it is dynamically compiled to Java virtual machine (JVM) bytecode, and interoperates with other Java code and libraries.

## Getting Started

### Basic Objective

There is basically just one pattern: Create an entity and then invoke one of the CRUD methods on it:

- `create()` - to effectively send a resource creation REST request over the wire
- `read()` - to fetch an existing REST resource by its UUID
- `update()` - to send an update to an existing REST resource
- `delete()` - to delete an existing resource
- `all` - this property implements a *list* operation to fetch UUIDs of existing REST resources

An entity must be sufficiently filled for the associated REST request to succeed. No extra validation
will take place. In case of error you would see an exception with the proper cause.
Take note, that for resources that exist within the context of another resource, that other resource
must also be properly instanciated and injected.

For example: to successfully operate on a network
interface, the entity (`NIC`) must also contain proper `Server` and `LAN` instances.

No extra modeling was made (e.g. no *parent* links or the like) - the entities just enclose the transported
JSON representations.

On a successfully delivered REST `POST`, `PUT` or `DELETE` request the Profitbricks API might send a
`Location` header instead of the final resulting response to the requested action. This SDK will block
on such a response and then keeps polling for the final result. If a successful result doesn't come
in time, an exception will be thrown.
This decision was made to allow for easy and agile scripting, as the result matters most of the time and
such a behaviour would be needed anyways. See also below (*open issues*)

For API resources not fitting into the CRUD scheme, an extra command facade is provided. This provides
functionality like attaching and detaching storage volumes to virtual servers or associating network
interfaces with load balancers. (see `com.profitbricks.sdk.Commands` facade)

This implementation is based on Groovy 2, so it technically runs on any JVM with at least 1.7

Before you begin you will need to have [signed-up](https://www.profitbricks.com/signup) for a ProfitBricks account. The credentials you setup during sign-up will be used to authenticate against the Cloud API.

### Installation

The official Groovy library is available from the [ProfitBricks GitHub account](https://github.com/profitbricks/profitbricks-sdk-groovy).

a) build yourself: `./gradlew install`

b) just add the proper maven dependency:
  - either in your pom.xml:

        <dependency>
          <groupId>com.profitbricks.sdk/groupId>
          <artifactId>groovy</artifactId>
          <version>1.0.0</version>
        </dependency>`

  - or in your build.gradle:

        dependencies {
          compile 'com.profitbricks.sdk:groovy:1.0.0'
        }

### Configuration

The current implementation relies solely on system properties (and proper defaults). The following
table lists all relevant system properties.



| name                         |      default                             |  notes                                                                                           |
-------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------|
| `api.URL`                    | https://api.profitbricks.com/cloudapi/v3 | the base API URL                                                                                 |
| `api.verifySSL`              | `true`                                   | ignores all SSL certificate issues if `false`                                                    |
| `api.user`                   |                                          | the API user name for basic auth                                                                 |
| `api.password`               |                                          | the API password for basic auth                                                                  |
| `api.wait.init.milliseconds` | 100                                      | if waiting for success, this is the inital time period between two checks                        |
| `api.wait.timeout.seconds`   | 120                                      | if waiting for success, this is the timeout                                                      |
| `api.wait.max.milliseconds`  | 1500                                     | if waiting for success, this is the max time period between two checks                           |
| `api.wait.factor`            | 1.87                                     | if waiting for success, this is the factor by which the current time period value is multiplied  |

**Note:** You can set the values inside the `main\resources\config.yml` configuration file.

## Reference

This section provides details on all the available operations and the parameters they accept. Brief code snippets demonstrating usage are also included.

### Data Centers

Virtual data centers (VDCs) are the foundation of the ProfitBricks platform. VDCs act as logical containers for all other objects you will be creating, e.g., servers. You can provision as many VDCs as you want. VDCs have their own private network and are logically segmented from each other to create isolation.

Create an instace of the api class:

```DataCenter dc = new DataCenter();```

#### List Data Centers

This operation will list all currently provisioned VDCs that your account credentials provide access to.

```
def list =dc.all
```

---

#### Retrieve a Data Center

Use this to retrieve details about a specific VDC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |

```
def datacenter =dc.read(datacenterId)
```

---

#### Create a Data Center

Use this operation to create a new VDC. You can create a "simple" VDC by supplying just the required *name* and *location* parameters.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| Name | **yes** | string | The name of the data center. |
| Location | **yes** | string | The physical ProfitBricks location where the VDC will be created. |
| Description | no | string | A description for the data center, e.g. staging, production. |
| Servers | no | collection | Details about creating one or more servers. See [create a server](#create-a-server). |
| Volumes | no | collection | Details about creating one or more volumes. See [create a volume](#create-a-volume). |
| Lans | no | collection | Details about creating one or more LANs. See [create a lan](#create-a-lan). |
| Loadbalancers | no | collection | Details about creating one or more load balancers. See [create a load balancer](#create-a-load- balancer). |

The following table outlines the locations currently supported:

| Value| Country | City |
|---|---|---|
| us/las | United States | Las Vegas |
| de/fra | Germany | Frankfurt |
| de/fkb | Germany | Karlsruhe |

**NOTES**:
- The value for `Name` cannot contain the following characters: (@, /, , |, ‘’, ‘).
- You cannot change the virtual data center `Location` once it has been provisioned.

```
def dc = new DataCenter(
	name: "name",
	location: 'de/fkb',
	description: 'desc'
).create()

```

---

#### Update a Data Center

After retrieving a data center, either by getting it by id, or as a create response object, you can change its properties by calling the `update`. Some parameters may not be changed using either of the update methods.

The following table describes the available request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| Name | no | string | The new name of the VDC. |
| Description | no | string | The new description of the VDC. |

```
def datacenter =dc.read(datacenterId)
dc.name = "updated name"
dc.description = "groovy new datacenter!"
dc.update()
```

---

#### Delete a Data Center

This will remove all objects within the virtual data center AND remove the virtual data center object itself.

**NOTE**: This is a highly destructive operation which should be used with extreme caution!

The following table describes the available request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC that you want to delete. |

```
def _dc = dc.read(datacenterId)
_dc.delete()
```

---

### Locations

Locations are the physical ProfitBricks data centers where you can provision your VDCs.

Create an instace of the api class:

```Location loc = new Location()```

#### List Locations

This operation will return the list of currently available locations.

There are no request parameters to supply.

```
loc.all()
```

---

#### Get a Location

Retrieves the attributes of a specific location.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| locationId | **yes** | string | The ID consisting of country/city. |

```
loc.read(locationId)
```

---

### Servers

Create an instace of these api classes:

```
Server s = new Server()
```

#### List Servers

You can retrieve a list of all the servers provisioned inside a specific VDC.


The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |

```
s.dataCenter=dc.read(datacenterId)
s.all()
```

---

#### Retrieve a Server

Returns information about a specific server such as its configuration, provisioning status, etc.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

```
s.dataCenter=dc.read(datacenterId)
s.read(serverId)
```

---

#### Create a Server

Creates a server within an existing VDC. You can configure additional properties such as specifying a boot volume and connecting the server to a LAN.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| name | **yes** | string | The name of the server. |
| cores | **yes** | int | The total number of cores for the server. |
| ram | **yes** | int | The amount of memory for the server in MB, e.g. 2048. Size must be specified in multiples of 256 MB with a minimum of 256 MB; however, if you set `ram_hot_plug` to *True* then you must use a minimum of 1024 MB. |
| availabilityZone | no | string | The availability zone in which the server should exist. |
| cpuFamily | no | string | Sets the CPU type. "AMD_OPTERON" or "INTEL_XEON". Defaults to "AMD_OPTERON". |


The following table outlines the server availability zones currently supported:

| Availability Zone | Comment |
|---|---|
| AUTO | Automatically Selected Zone |
| ZONE_1 | Fire Zone 1 |
| ZONE_2 | Fire Zone 2 |

```
Server s = new Server(
	dataCenter: dc.read(datacenterId),
	name: "server name",
	cores: 1,
	ram: 1024
).create()
```

---

#### Update a Server

Perform updates to the attributes of a server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| name | no | string | The name of the server. |
| cores | no | int | The number of cores for the server. |
| ram | no | int | The amount of memory in the server. |
| availabilityZone | no | string | The new availability zone for the server. |
| cpuFamily | no | string | Sets the CPU type. "AMD_OPTERON" or "INTEL_XEON". Defaults to "AMD_OPTERON". |


```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
s.name = "updated"
s.cores = 2
s.ram = 4
s.update()
```

---

#### Delete a Server

This will remove a server from a data center. **NOTE**: This will not automatically remove the storage volume(s) attached to a server. A separate operation is required to delete a storage volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `delete` method directly:

```
def _s = s.read(serverId)
_s.delete()
```

---

#### List Attached Volumes

Retrieves a list of volumes attached to the server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attachedVolumes` method directly:

```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
attachedVolumes(s)
```

---

#### Attach a Volume

This will attach a pre-existing storage volume to the server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| volumeId | **yes** | string | The ID of a storage volume. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attach` method directly.

```
Volume v = new Volume()
v=v.read(volumeId)
s.dataCenter=dc.read(datacenterId)
s.read(serverId)
attach(s, v)
```

---

#### Retrieve an Attached Volume

This will retrieve the properties of an attached volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| volumeId | **yes** | string | The ID of the attached volume. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attachedVolume` method directly.

```
Volume v = new Volume()
v=v.read(volumeId)
s.dataCenter=dc.read(datacenterId)
s.read(serverId)
attachedVolume(s,v)
```

---

#### Detach a Volume

This will detach the volume from the server. Depending on the volume `hot_unplug` settings, this may result in the server being rebooted.

This will NOT delete the volume from your virtual data center. You will need to make a separate request to delete a volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| volumeId | **yes** | string | The ID of the attached volume. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `detach` method directly.

```
Volume v = new Volume()
v=v.read(volumeId)
s.dataCenter=dc.read(datacenterId)
s.read(serverId)
detach(s, v)
```

---

#### List Attached CD-ROMs

Retrieves a list of CD-ROMs attached to a server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attachedCDROMs` method directly.

```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
attachedCDROMs(s)
```

---

#### Attach a CD-ROM

You can attach a CD-ROM to an existing server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| imageId | **yes** | string | The ID of a CD-ROM. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attach` method directly.

```
Image i = new Image()
i=i.read(cdROMimageId)
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
attach(s, i)
```

---

#### Retrieve an Attached CD-ROM

You can retrieve a specific CD-ROM attached to the server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| imageId | **yes** | string | The ID of the attached CD-ROM. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `attachedCDROM` method directly.

```
Image i = new Image()
i=i.read(cdROMimageId)
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
attachedCDROM(s,i)
```

---

#### Detach a CD-ROM

This will detach a CD-ROM from the server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| imageId | **yes** | string | The ID of the attached CD-ROM. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `detach` method directly.

```
Image i = new Image()
i=i.read(imageId)
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
attachedCDROM(s,i)
detach(s, i)
```

---

#### Reboot a Server

This will force a hard reboot of the server. Do not use this method if you want to gracefully reboot the machine. This is the equivalent of powering off the machine and turning it back on.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `Reboot` method directly.

```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
reboot(s)
```

---

#### Start a Server

This will start a server. If the server's public IP was deallocated then a new IP will be assigned.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `start` method directly.

```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
start(s)
```

---

#### Stop a Server

This will stop a server. The machine will be forcefully powered off, billing will cease, and the public IP, if one is allocated, will be deallocated.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

After retrieving a server, either by getting it by id, or as a create response object, you can call the `stop` method directly.

```
s.dataCenter=dc.read(datacenterId)
s=s.read(serverId)
stop(s)
```

---

### Images

Create an instace of the api class:

```Image i = new Image()```

#### List Images

Retrieve a list of images.

```
i.all
```

---

#### Get an Image

Retrieves the attributes of a specific image.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| imageId | **yes** | string | The ID of the image. |

```
i.read(imageId)
```

---

### Volumes

Create an instace of the api class:

```Volume v = new Volume()```

#### List Volumes

Retrieve a list of volumes within the virtual data center. If you want to retrieve a list of volumes attached to a server please see the [List Attached Volumes](#list-attached-volumes) entry in the Server section for details.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |

```
v.dataCenter=dc.read(datacenterId)
v.all
```

---

#### Get a Volume

Retrieves the attributes of a given volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| volumeId | **yes** | string | The ID of the volume. |

```
v.dataCenter=dc.read(datacenterId)
v=v.read(volumeId)
```

---

#### Create a Volume

Creates a volume within the virtual data center. This will NOT attach the volume to a server. Please see the [Attach a Volume](#attach-a-volume) entry in the Server section for details on how to attach storage volumes.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| name | **yes** | string | The name of the volume. |
| size | **yes** | int | The size of the volume in GB. |
| bus | no | string | The bus type of the volume (VIRTIO or IDE). Default: VIRTIO. |
| image | no | string | The image or snapshot ID. |
| type | no | string | The volume type, HDD or SSD. |
| licenceType | no | string | The licence type of the volume. Options: LINUX, WINDOWS, WINDOWS2016, UNKNOWN, OTHER |
| imagePassword | no | string | One-time password is set on the Image for the appropriate root or administrative account. This field may only be set in creation requests. When reading, it always returns *null*. The password has to contain 8-50 characters. Only these characters are allowed: [abcdefghjkmnpqrstuvxABCDEFGHJKLMNPQRSTUVX23456789] |
| availabilityZone | no | string | The storage availability zone assigned to the volume. Valid values: AUTO, ZONE_1, ZONE_2, or ZONE_3. This only applies to HDD volumes. Leave blank or set to AUTO when provisioning SSD volumes. |

The following table outlines the various licence types you can define:

| Licence Type | Comment |
|---|---|
| WINDOWS2016 | Use this for the Microsoft Windows Server 2016 operating system. |
| WINDOWS | Use this for the Microsoft Windows Server 2008 and 2012 operating systems. |
| LINUX |Use this for Linux distributions such as CentOS, Ubuntu, Debian, etc. |
| OTHER | Use this for any volumes that do not match one of the other licence types. |
| UNKNOWN | This value may be inherited when you've uploaded an image and haven't set the license type. Use one of the options above instead. |

The following table outlines the storage availability zones currently supported:

| Availability Zone | Comment |
|---|---|
| AUTO | Automatically Selected Zone |
| ZONE_1 | Fire Zone 1 |
| ZONE_2 | Fire Zone 2 |
| ZONE_3 | Fire Zone 3 |


```
Volume v = new Volume(
	dataCenter: dc,
	name: "name",
	size: 1
).create()
```

---

#### Update a Volume

You can update -- in full or partially -- various attributes on the volume; however, some restrictions are in place:

You can increase the size of an existing storage volume. You cannot reduce the size of an existing storage volume. The volume size will be increased without requiring a reboot if the relevant hot plug settings have been set to *true*. The additional capacity is not added automatically added to any partition, therefore you will need to handle that inside the OS afterwards. Once you have increased the volume size you cannot decrease the volume size.

Since an existing volume is being modified, none of the request parameters are specifically required as long as the changes being made satisfy the requirements for creating a volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| volumeId | **yes** | string | The ID of the volume. |
| name | no | string | The name of the volume. |
| size | no | int | The size of the volume in GB. Only increase when updating. |
| bus | no | string | The bus type of the volume (VIRTIO or IDE). Default: VIRTIO. |
| image | no | string | The image or snapshot ID. |
| type | no | string | The volume type, HDD or SSD. |
| licenceType | no | string | The licence type of the volume. Options: LINUX, WINDOWS, WINDOWS2016, UNKNOWN, OTHER |
| availabilityZone | no | string | The storage availability zone assigned to the volume. Valid values: AUTO, ZONE_1, ZONE_2, or ZONE_3. This only applies to HDD volumes. Leave blank or set to AUTO when provisioning SSD volumes. |

After retrieving a volume, either by getting it by id, or as a create response object, you can change its properties and call the `update` method:

```
v.dataCenter=dc.read(datacenterId)
v=v.read(volumeId)
v.name = "updated name"
v.size = 2
v.update()
```

---

#### Delete a Volume

Deletes the specified volume. This will result in the volume being removed from your data center. Use this with caution.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| volumeId | **yes** | string | The ID of the volume. |

After retrieving a volume, either by getting it by id, or as a create response object, you can call the `delete` method directly.

```
v.dataCenter=dc.read(datacenterId)
v=v.read(volumeId)
v.delete()
```

---
#### Create a Volume Snapshot

Creates a snapshot of a volume within the virtual data center. You can use a snapshot to create a new storage volume or to restore a storage volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| volumeId | **yes** | string | The ID of the volume. |
| Name | no | string | The name of the snapshot. |
| Description | no | string | The description of the snapshot. |

After retrieving a volume, either by getting it by id, or as a create response object, you can call the `CreateSnapshot` method directly.

```
Snapshot sn = new Snapshot()
v.dataCenter=dc.read(datacenterId)
v=v.read(volumeId)
sn = snapshot(v)
```

---

#### Restore a Volume Snapshot

This will restore a snapshot onto a volume. A snapshot is created as just another image that can be used to create new volumes or to restore an existing volume.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| volumeId | **yes** | string | The ID of the volume. |
| snapshotId | **yes** | string |  The ID of the snapshot. |

After retrieving a volume, either by getting it by id, or as a create response object, you can call the `restore` method directly.

```
Snapshot sn = new Snapshot()
v.dataCenter=dc.read(datacenterId)
v=v.read(volumeId)
restore(v, sn)
```

---

### Snapshots

Create an instace of the api class:

```Snapshot sn = new Snapshot()```

#### List Snapshots

You can retrieve a list of all available snapshots.

There are no request parameters to supply.

```
sn.all
```

---

#### Get a Snapshot

Retrieves the attributes of a specific snapshot.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| snapshotId | **yes** | string | The ID of the snapshot. |

```
sn.read(snapshotId)
```

---

#### Update a Snapshot

Perform updates to attributes of a snapshot.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| snapshotId | **yes** | string | The ID of the snapshot. |
| name | no | string | The name of the snapshot. |
| description | no | string | The description of the snapshot. |
| licenceType | no | string | The snapshot's licence type: LINUX, WINDOWS, WINDOWS2016, or OTHER. |
| cpuHotPlug | no | bool | This volume is capable of CPU hot plug (no reboot required) |
| cpuHotUnplug | no | bool | This volume is capable of CPU hot unplug (no reboot required) |
| ramHotPlug | no | bool |  This volume is capable of memory hot plug (no reboot required) |
| ramHotUnplug | no | bool | This volume is capable of memory hot unplug (no reboot required) |
| nicHotPlug | no | bool | This volume is capable of NIC hot plug (no reboot required) |
| nicHotUnplug | no | bool | This volume is capable of NIC hot unplug (no reboot required) |
| siscVirtioHotPlug | no | bool | This volume is capable of VirtIO drive hot plug (no reboot required) |
| discVirtioHotUnplug | no | bool | This volume is capable of VirtIO drive hot unplug (no reboot required) |
| discScsiHotPlug | no | bool | This volume is capable of SCSI drive hot plug (no reboot required) |
| discScsiHotUnplug | no | bool | This volume is capable of SCSI drive hot unplug (no reboot required) |

After retrieving a snapshot, either by getting it by id, or as a create response object, you can change its properties and call the `update` method:

```
sn=sn.read(snapshotId)
sn.name="updated"
sn.update()
```

---

#### Delete a Snapshot

Deletes the specified snapshot.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| snapshotId | **yes** | string | The ID of the snapshot. |

After retrieving a snapshot, either by getting it by id, or as a create response object, you can call the `Delete` method directly.

```
sn=sn.read(snapshotId)
sn.delete()
```

---

### IP Blocks

The IP block operations assist with managing reserved /static public IP addresses.

Create an instace of the api class:

```IPBlock ip = new IPBlock()```

#### List IP Blocks

Retrieve a list of available IP blocks.

```
ip.all
```

#### Get an IP Block

Retrieves the attributes of a specific IP block.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| ipBlockId | **yes** | string | The ID of the IP block. |

```
ip.read(ipBlockId)
```

---

#### Create an IP Block

Creates an IP block. IP blocks are attached to a location, so you must specify a valid `location` along with a `size` parameter indicating the number of IP addresses you want to reserve in the IP block. Servers or other resources using an IP address from an IP block must be in the same `location`.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| location | **yes** | string | This must be one of the locations: us/las, de/fra, de/fkb. |
| size | **yes** | int | The size of the IP block you want. |
| name | no | string | A descriptive name for the IP block |

The following table outlines the locations currently supported:

| Value| Country | City |
|---|---|---|
| us/las | United States | Las Vegas |
| de/fra | Germany | Frankfurt |
| de/fkb | Germany | Karlsruhe |

To create an IP block, establish the parameters and then call `create`.

```
IPBlock ip = new IPBlock(
	location: location,
	name: "name",
	size: 2
).create()
```

---

#### Delete an IP Block

Deletes the specified IP Block.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| ipblockId | **yes** | string | The ID of the IP block. |

After retrieving an IP block, either by getting it by id, or as a create response object, you can call the `Delete` method directly.

```
def _ip = ip.read(id) as IPBlock
_ip.delete()
```

---

### LANs

Create an instace of the api class:

```LAN l = new LAN()```

#### List LANs

Retrieve a list of LANs within the virtual data center.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |

```
l.dataCenter=dc.read(datacenterId)
l.all
```

---

#### Create a LAN

Creates a LAN within a virtual data center.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| name | no | string | The name of your LAN. |
| public | **Yes** | bool | Boolean indicating if the LAN faces the public Internet or not. |

```
LAN l = new LAN(
	dataCenter: dc.read(datacenterId),
	name: name,
	_public: true
).create()
```

---

#### Get a LAN

Retrieves the attributes of a given LAN.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| lanId | **yes** | int | The ID of the LAN. |

```
l.dataCenter=dc.read(datacenterId)
l.read(lanId)
```

---

#### Update a LAN

Perform updates to attributes of a LAN.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| lanId | **yes** | int | The ID of the LAN. |
| Name | no | string | A descriptive name for the LAN. |
| Public | no | bool | Boolean indicating if the LAN faces the public Internet or not. |

After retrieving a LAN, either by getting it by id, or as a create response object, you can change its properties and call the `update` method:

```
l.dataCenter=dc.read(datacenterId)
l.read(lanId)
l.name = "name"
l._public = false
l.update()
```

**NOTE**: You can also use `update()`, for that operation you will update all the properties.

---

#### Delete a LAN

Deletes the specified LAN.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| lanId | **yes** | string | The ID of the LAN. |

After retrieving a LAN, either by getting it by id, or as a create response object, you can call the `delete` method directly.

```
l.dataCenter=datacenterId
def _l = l.read(lanId)
_l.delete()
```

---

### Network Interfaces

Create an instance of the api class:

```NIC n = new NIC()```

#### List NICs

Retrieve a list of LANs within the virtual data center.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |

```
n.dataCenter=dc.read(datacenterId)
n.server=s.read(serverId)
n.all;
```

---

#### Get a NIC

Retrieves the attributes of a given NIC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |

```
n.dataCenter=dc.read(datacenterId)
n.server=s.read(serverId)
n.read(niceId)
```

---

#### Create a NIC

Adds a NIC to the target server.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string| The ID of the server. |
| name | no | string | The name of the NIC. |
| ips | no | string collection | IPs assigned to the NIC. This can be a collection. |
| dhcp | no | bool | Set to FALSE if you wish to disable DHCP on the NIC. Default: TRUE. |
| lan | **yes** | int | The LAN ID the NIC will sit on. If the LAN ID does not exist it will be created. |
| nat | no | bool | Indicates the private IP address has outbound access to the public internet. |
| firewallActive | no | bool | Once you add a firewall rule this will reflect a true value. |

```
n.dataCenter=dc.read(datacenterId)
n.server=s.read(serverId)
n = n.create()
```

---

#### Update a NIC

You can update -- in full or partially -- various attributes on the NIC; however, some restrictions are in place:

The primary address of a NIC connected to a load balancer can only be changed by changing the IP of the load balancer. You can also add additional reserved, public IPs to the NIC.

The user can specify and assign private IPs manually. Valid IP addresses for private networks are 10.0.0.0/8, 172.16.0.0/12 or 192.168.0.0/16.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string| The ID of the server. |
| niceId | **yes** | string| The ID of the NIC. |
| name | no | string | The name of the NIC. |
| ips | no | string collection | IPs assigned to the NIC represented as a collection. |
| dhcp | no | bool | Boolean value that indicates if the NIC is using DHCP or not. |
| lan | no | int | The LAN ID the NIC sits on. |
| nat | no | bool | Indicates the private IP address has outbound access to the public internet. |

After retrieving a NIC, either by getting it by id, or as a create response object, you can call the `update` method directly.

```
n.dataCenter=dc.read(datacenterId)
n.server=s.read(serverId)
n=n.read(niceId)
n.name = "name"
n.ips = ['192.168.0.2']
n.dhcp = false
n.nat = false
n.update()
```

---

#### Delete a NIC

Deletes the specified NIC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string| The ID of the server. |
| niceId | **yes** | string| The ID of the NIC. |

After retrieving a NIC, either by getting it by id, or as a create response object, you can call the `delete` method directly.

```
n.dataCenter=dc.read(datacenterId)
n.server=s.read(serverId)
n=n.read(niceId)
def _n = n.read(id)
_n.read()
```

---

### Firewall Rules

Create an instace of the api class:

```FirewallRule fw = new FirewallRule()```

#### List Firewall Rules

Retrieves a list of firewall rules associated with a particular NIC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |

```
fw.dataCenter=dc.read(datacenterId)
fw.server=s.read(serverId)
fw.nic=n.read(niceId)
fw.all
```

---

#### Get a Firewall Rule

Retrieves the attributes of a given firewall rule.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |
| fwId | **yes** | string | The ID of the firewall rule. |

```
fw.dataCenter=dc.read(datacenterId)
fw.server=new Server().read(serverId)
fw.nic=n.read(niceId)
fw.read(fwId)
```

---

#### Create a Firewall Rule

This will add a firewall rule to the NIC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |
| name | no | string | The name of the firewall rule. |
| protocol | **yes** | string | The protocol for the rule: TCP, UDP, ICMP, ANY. |
| sourceMac | no | string | Only traffic originating from the respective MAC address is allowed. Valid format: aa:bb:cc:dd:ee:ff. A *null* value allows all source MAC address. |
| sourceIp | no | string | Only traffic originating from the respective IPv4 address is allowed. A *null* value allows all source IPs. |
| targetIp | no | string | In case the target NIC has multiple IP addresses, only traffic directed to the respective IP address of the NIC is allowed. A *null* value allows all target IPs. |
| portRangeStart | no | string | Defines the start range of the allowed port (from 1 to 65534) if protocol TCP or UDP is chosen. Leave `PortRangeStart` and `PortRangeEnd` value as *null* to allow all ports. |
| portRangeEnd | no | string | Defines the end range of the allowed port (from 1 to 65534) if the protocol TCP or UDP is chosen. Leave `PortRangeStart` and `PortRangeEnd` value as *null* to allow all ports. |
| icmpType | no | string | Defines the allowed type (from 0 to 254) if the protocol ICMP is chosen. A *null* value allows all types. |
| icmpCode | no | string | Defines the allowed code (from 0 to 254) if protocol ICMP is chosen. A *null* value allows all codes. |

```
FirewallRule fw =new FirewallRule()
fw.dataCenter=dc.read(datacenterId)
fw.server=new Server().read(serverId)
fw= new FirewallRule(
	nic: n.read(niceId),
	protocol: 'UDP'
).create()
```

---

#### Update a Firewall Rule

Perform updates to attributes of a firewall rule.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |
| fwId | **yes** | string | The ID of the firewall rule. |
| name | no | string | The name of the firewall rule. |
| sourceMac | no | string | Only traffic originating from the respective MAC address is allowed. Valid format: aa:bb:cc:dd:ee:ff. A *null* value allows all source MAC address. |
| sourceIp | no | string | Only traffic originating from the respective IPv4 address is allowed. A *null* value allows all source IPs. |
| targetIp | no | string | In case the target NIC has multiple IP addresses, only traffic directed to the respective IP address of the NIC is allowed. A *null* value allows all target IPs. |
| portRangeStart | no | string | Defines the start range of the allowed port (from 1 to 65534) if protocol TCP or UDP is chosen. Leave `port_range_start` and `port_range_end` value as *null* to allow all ports. |
| portRangeEnd | no | string | Defines the end range of the allowed port (from 1 to 65534) if the protocol TCP or UDP is chosen. Leave `port_range_start` and `port_range_end` value as *null* to allow all ports. |
| icmpType | no | string | Defines the allowed type (from 0 to 254) if the protocol ICMP is chosen. A *null* value allows all types. |
| icmpCode | no | string | Defines the allowed code (from 0 to 254) if protocol ICMP is chosen. A *null* value allows all codes. |

After retrieving a firewall rule, either by getting it by id, or as a create response object, you can change its properties and call the `update` method:

```
n=new NIC().read(niceId)
fw.dataCenter=dc.read(datacenterId)
fw.server=new Server().read(serverId)
fw.name = "name"
fw.sourceMac = 'aa:bb:cc:dd:ee:ff'
fw.sourceIp = '23.23.23.23'
fw.targetIp = n.ips.first()
fw.portRangeStart = '1234'
fw.portRangeEnd = '4711'
fw.update()
```

**NOTE**: You can also use `Update()`, for that operation you will update all the properties.

---

#### Delete a Firewall Rule

Removes the specific firewall rule.

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| serverId | **yes** | string | The ID of the server. |
| niceId | **yes** | string | The ID of the NIC. |
| fwId | **yes** | string | The ID of the firewall rule. |

After retrieving a firewall rule, either by getting it by id, or as a create response object, you can call the `Delete` method directly.

```
n=new NIC().read(niceId)
fw.dataCenter=dc.read(datacenterId)
fw.server=new Server().read(serverId)
fw.nic=n
def _fw = fw.read(id)
_fw.delete()
```

---



### Load Balancers

Create an instace of the api class:

```LoadBalancer lb = new LoadBalancer()```

#### List Load Balancers

Retrieve a list of load balancers within the data center.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |

```
lb.dataCenter=new DataCenter().read(datacenterId)
lb.all
```

---

#### Get a Load Balancer

Retrieves the attributes of a given load balancer.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |

```
lb.dataCenter=new DataCenter().read(datacenterId)
lb.read(loadbalancerId)
```

---

#### Create a Load Balancer

Creates a load balancer within the virtual data center. Load balancers can be used for public or private IP traffic.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| name | **yes** | string | The name of the load balancer. |
| ip | no | string | IPv4 address of the load balancer. All attached NICs will inherit this IP. |
| dhcp | no | bool | Indicates if the load balancer will reserve an IP using DHCP. |

```
def dc=new DataCenter().read(datacenterId)
LoadBalancer lb = new LoadBalancer(
	dataCenter: dc,
	name: "loadbalancer name"
).create()
```

---

#### Update a Load Balancer

Perform updates to attributes of a load balancer.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |
| name | no | string | The name of the load balancer. |
| ip | no | string | The IP of the load balancer. |
| dhcp | no | bool | Indicates if the load balancer will reserve an IP using DHCP. |

After retrieving a load balancer, either by getting it by id, or as a create response object, you can change it's properties and call the `update` method:

```
def dc=new DataCenter().read(datacenterId)
lb=lb.read(loadbalancerId)
lb.dataCenter=dc
lb.name = "name"
lb.dhcp = false
lb.ip = '192.168.0.11'
lb.update()
```

---

#### Delete a Load Balancer

Deletes the specified load balancer.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |

After retrieving a load balancer, either by getting it by id, or as a create response object, you can call the `delete` method directly.

```
def dc=new DataCenter().read(datacenterId)
lb.dataCenter=dc
def _lb = lb.read(loadbalancerId)
_lb.delete()
```

---

#### List Load Balanced NICs

This will retrieve a list of NICs associated with the load balancer.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |

```
def dc=new DataCenter().read(datacenterId)
lb.dataCenter=dc
def loadBalancer=lb.read(loadbalancerId)
associatedNics(loadBalancer)
```

---

#### Get a Load Balanced NIC

Retrieves the attributes of a given load balanced NIC.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |
| niceId | **yes** | string | The ID of the NIC. |

After retrieving a load balancer, either by getting it by id, or as a create response object, you can call the `get_loadbalanced_nic` method directly.

```
def dc=new DataCenter().read(datacenterId)
lb.dataCenter=dc
def loadBalancer=lb.read(loadbalancerId)
associatedNic(loadBalancer,niceId)
```

---

#### Associate NIC to a Load Balancer

This will associate a NIC to a load balancer, enabling the NIC to participate in load-balancing.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |
| niceId | **yes** | string | The ID of the NIC. |

After retrieving a load balancer, either by getting it by id, or as a create response object, you can call the `AttachNic` method directly.

```
def dc=new DataCenter().read(datacenterId)
NIC n=new NIC()
n.dataCenter=dc
lb.dataCenter=dc
lb=lb.read(loadbalancerId)
n=n.read(niceId)
associate(lb, n);
```

---

#### Remove a NIC Association

Removes the association of a NIC with a load balancer.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| datacenterId | **yes** | string | The ID of the VDC. |
| loadbalancerId | **yes** | string | The ID of the load balancer. |
| niceId | **yes** | string | The ID of the NIC. |

After retrieving a load balancer, either by getting it by id, or as a create response object, you can call the `DetachNic` method directly.

```
def dc=new DataCenter().read(datacenterId)
NIC n=new NIC()
n.dataCenter=dc
lb.dataCenter=dc
lb=lb.read(loadbalancerId)
n=n.read(niceId)
dissociate(lb, n)
```

---

### Requests

Each call to the ProfitBricks Cloud API is assigned a request ID. These operations can be used to get information about the requests that have been submitted and their current status.

Create an instace of the api class:

```Request r=new Request()```

#### List Requests

Retrieve a list of requests.

```
r.all
```

---

#### Get a Request Status

Retrieves the status of a request.

The following table describes the request arguments:

| Name| Required | Type | Description |
|---|:-:|---|---|
| requestId | **yes** | string | The ID of the request. |

```
requestStatus(requestId)
```

---

## Examples

The following examples makes the assumption that the SDK has been configured properly, refer to [Configuration](#configuration) section.


### Component Build

This example covers creating a DataCenter with a server, it includes creating a public Lan and attaching it to an NIC, it also covers creating a volume and attaching it to the example server.

```groovy
import com.profitbricks.sdk.model.*
import static com.profitbricks.sdk.Commands.*

class SDKExample {
    final static void main(final String[] args) {
        
        //Creating a DataCenter
        println "Creating a DataCenter"
        
        DataCenter dc = new DataCenter(
            name: "example DC",
            location: 'de/fkb',
            description: 'desc'
        ).create() as DataCenter
        
        println "DataCenter Ready"
        // listing DataCenters
        println "Listing DataCenters"
        println dc.all
        

        //Add a lan
        println "Creating a public Lan"
        LAN lan = new LAN(
            dataCenter: dc,
            name: "public lan",
            _public: true
        ).create()
        
        println "Public Lan Ready"
        
        //Add a server
        println "Creating a Server"
        Server server = new Server(
            dataCenter: dc,
            name: "Example server",
            cores: 1,
            ram: 1024
        ).create()
        
        println "Server Ready"
        
        //reading Server
        println "Read server"
        println server.read()
        
        //Adding NIC to the example Server
        println "Adding NIC to server"
        NIC nic = new NIC(
            server: server,
            lan: lan,
            name: "example nic"
        ).create()
        println "NIC Ready"
        
        //Find a linux image to attach to volume
        println "Searching for a linux image"
        Image image = new Image()
        image = image.all.collect{image.read(it) as Image}.findAll{
            it._public &&
            it.location == dc.location &&
            it.licenceType =~ /(?i)linux/ &&
            it.imageType =~ /(?i)hdd/
        }.first()
        
        println "Linux Image found: $image"
        
        //Create a volume
        println "Creating a Volume"
        Volume volume = new Volume(
            dataCenter: dc,
            name: "os volume",
            size: 4,
            image: image.id,
            type:"HDD",
            imagePassword:"test1234"
            
        ).create()
        println "Volume Ready"
        
        
        //Attach the os Volume to the Example server
        println "Attaching the os Volume to the Example Server"
        attach(server, volume)
        println "Volume Attached"
        
        //List attached volumes
        println "Listing attached volumes"
        println attachedVolumes(server)

        //Example cleaning
        println "Started cleaning"
        def _dc = dc.read(dc.id) as DataCenter
        _dc.delete()
        println "Cleaning Complete"
    }
}
```



## Support

Not all CRUD functionality needs to be instance methods on the entities. Especially `all` (a.k.a. `list`) and `read()` should be
static members. This'll be done in an upcoming version.

The current rather simplistic configuration approach using system properties should actually be replaced by a properly
provided externalized configuration within the class path. There is already a draft for this in `src/main/resources/config.yaml`
but needs implementation and will also be done with an upcoming version.

In the long run, having proper lifecycle control over your entities proves to be more than helpful. The current (brutely blocking) 
approach to query for `/request` resources will be replaces by a future/promise based mechanism.

You can engage with us in the ProfitBricks [DevOps Central community](https://devops.profitbricks.com/community) and we'll be more than happy to answer any questions you might have about using this .NET library.

Please report any issues or bugs your encounter using the [GitHub Issue Tracker](https://github.com/profitbricks/profitbricks-sdk-groovy/issues).

## Testing

You can find a full list of tests inside the `SDKTester` class .You can run tests using this command `gradle run`

## Contributing

1. Fork it ( https://github.com/profitbricks/profitbricks-sdk-groovy/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create a new Pull Request