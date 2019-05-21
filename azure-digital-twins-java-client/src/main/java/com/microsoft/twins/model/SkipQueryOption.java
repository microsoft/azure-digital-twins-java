/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
/*
 * Digital Twins Service Management APIs The Digital Twins Service allows for managing IoT devices
 * within spaces on Microsoft Azure. This guide describes the REST APIs to manage these devices. For
 * more documentation, please follow [this link](https://docs.microsoft.com/azure/digital-twins/).
 * ## How to use the APIs ### Authentication All API calls must be authenticated using OAuth. ###
 * HTTP request headers 1. Most APIs expect the `Content-Type` header to be set to
 * `application/json`. 2. Optional: Provide Correlation ID (GUID) by using the following header -
 * `X-Ms-Client-Request-Id`. ### HTTP response headers 1. Create APIs set the `Location` header. 2.
 * APIs set the `X-Ms-Request-Id` header. ### Calling conventions The APIs follow
 * [these](https://github.com/Microsoft/api-guidelines/blob/master/Guidelines.md) conventions. ###
 * HTTP response status codes The standard
 * [codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) are used. In particular, - `2xx`
 * codes are returned on success. In particular: - `200` for requests querying data (GET). - `201`
 * for requests creating objects (POST). - `204` for requests performing data modification (PATCH,
 * PUT, DELETE). - `4xx` codes are returned on failure for issues owned by the caller. In
 * particular: - `400` for input validation issues; the response body typically provides details
 * regarding the issue. - `401` for authentication failures. - `403` for authorization failures when
 * trying to use a specific object without permission. For example: - directly accessing an object
 * by id, e.g. `api/v1.0/spaces/id-of-a-space-that-current-user-cannot-view` **Note**: this does not
 * apply to collections; for these filtering is used instead of erroring out. e.g.
 * `api/v1.0/spaces?type=Tenant` will return all spaces of type `Tenant` that the current user has
 * access to, possibly an empty collection. - in a body payload when performing a change. For
 * example creating or modifying an object under a space without permission. - `404` when: - using
 * an incorrect route (e.g. `api/v1.0/incorrect`) or - specifying a query string that's [too
 * long](https://docs.microsoft.com/en-us/iis/configuration/system.webserver/security/
 * requestfiltering/requestlimits) (thousands of chars) - specifying an object that does not exist
 * in a route (e.g. `api/v1.0/devices/invalid-device-id`) or - using an object that does not exist
 * in a body payload. For example: an invalid spaceId when creating a device, an invalid property
 * key name when assigning a property value to an object, etc. - `5xx` codes are returned on failure
 * for issues that are probably owned by the service. ## Object Model ### Spaces [Spaces](#!/Spaces)
 * correspond to physical locations. Each space has a type (for example: `Region`) and a name (for
 * example: `Puget Sound`). A given space may belong to another space, e.g. a neighborhood may
 * belong to a floor, that in turn belongs to a venue, region, customer, etc. ### Resources
 * [Resources](#!/Resources) are attached to a [space](#!/Spaces). Such as an IoTHub used by objects
 * in the space's hierarchy. ### Devices [Devices](#!/Devices) are attached to a [space](#!/Spaces).
 * They are entities (physical or virtual) that manage a number of [sensors](#!/Sensors). For
 * example, a device could be a user's phone, a Raspberry Pi sensor pod, Lora gateway, etc. ###
 * Sensors [Sensors](#!/Sensors) are attached to a [device](#!/Device) and a [space](#!/Spaces).
 * They record values. For example: battery level, temperature, noise, motion, etc. ### Blobs Blobs
 * are attached to objects (such as spaces, devices, sensors and users). They are the equivalent of
 * files (for example: pictures) with a mime type (ex: `image/jpeg`) and metadata (name,
 * description, type, etc.). Multipart requests are used to upload blobs. ### Extended Types Most
 * entities have one or more associated [Types](#!/Types) (see list below). Extended types are
 * extensible enumerations that augment entities with specific characteristics, which can be used to
 * filter or group entities by their type's name within the UI or within the data processor. An
 * example of extended type is the Space's type, which can have names such as Region, Customer,
 * Floor, Room, etc. Extended types have the following characteristics: - When creating an entity
 * that exposes the type, their name can be optionally specified and default to
 * \"None\" otherwise.  - Their names only allow for alpha-numeric characters.  - Their \"friendlyName\" allow for any character.  - They can be disabled. A disabled extended type cannot be referenced.  - They have a logical order that can be used (instead of e.g. alphabetical ordering) to populate dropdowns in UI.     The system provides default seed names for extended types with ontologies (see next section), but users can extend the set of available names to suit their needs.    Extended types offer the capability to model any type of vertical solution, including connected office, connected building, smart city, smart farming, etc.    Here are the supported types and their definitions:    - Devices:      - `DeviceType` : defined as manufacturer of the device, i.e. \"Contoso Device Manufacturer\"      - `DeviceSubtype` : defined as underlying hardware function, i.e. \"Gateway\", \"Controller\", \"Battery\", \"Switch\", \"AirHandlerUnit\", \"VariableAirVolume\", etc.     - DeviceBlobs:      - `DeviceBlobType` : defined as category of blobs that could be attached to a device, i.e. \"Model\", \"Specification\", etc.      - `DeviceBlobSubtype` : defined as subcategory of blobs that gives more granularity on the DeviceBlobType, i.e. \"PhysicalModel\", \"LogicalModel\", \"KitSpecification\", \"FunctionalSpecification\", etc.    - Sensors:      - `SensorType` : defined as the manufacturer of the sensor, i.e. \"Contoso Sensor Manufacturer\"      - `SensorDataType` : defined as category of data the sensor collects, i.e. \"Motion\", \"Temperature\", \"Humidity\", \"Sound\", \"Light\", etc.      - `SensorDataSubtype` : defined as subcategory of data the sensor collects to give more granularity to SensorDataType, i.e. \"OutsideAirTemperature\", \"ReturnAirTemperature\", \"InsideTemperature\", \"MicrophoneSound\", \"SoundImpact\", \"VoiceSound\", etc.      - `SensorDataUnitType` : defined as unit of measurement for data the sensor collects, i.e. \"Celsius\", \"Fahrenheit\", \"Decibels\", \"Pascal\", \"CubicFeetPerMinute\", \"Seconds\", etc.      - `SensorPortType` : defined as type of communication for the sensor, i.e. \"AnalogInput\", \"AnalogOutput\", \"BinaryInput\", \"BinaryOutput\", etc.    - Spaces:      - `SpaceType` : defined as category of spaces specific to vertical solution. Space category might be physical locations or logical, i.e. \"Tenant\", \"Region\", \"Customer\", \"Venue\", \"Floor\", \"Area\", \"Workstation\", \"Desk\", \"Chair\", etc.      - `SpaceSubtype` : defined as subcategory of spaces that gives more granularity on the SpaceType, i.e. \"StoreVenue\", \"SalesVenue\", \"MarketingVenue\", \"NeighborhoodArea\", \"ConfRoomArea\", \"PhoneRoomArea\", \"TeamRoomArea\", \"FocusRoomArea\", etc.      - `SpaceStatus` : allows for defining current state of a space. e.g. \"Active\", \"Disabled\"    - SpaceBlobs      - `SpaceBlobType` : defined as category of blobs that could be attached to a space, i.e. \"Map\", \"Image\", etc.       - `SpaceBlobSubtype` : defined as subtype that gives more granularity on the SpaceBlobType, i.e. \"GenericMap\", \"ElectricalMap\", \"SatelliteMap\", \"WayfindingMap\", etc.    - SpaceResources      - `SpaceResourceType` : defined as Azure resource type that could be associated to a space to be provisioned by this system, i.e. \"IoTHub\".    - UserBlobs:      - `UserBlobType` : defined as category of blobs that could be attached to a user, i.e. \"Image\", \"Video\", etc.      - `UserBlobSubtype` : defined as subcategory of spaces that gives more granularity on the UserBlobType, i.e. \"ProfessionalImage\", \"VacationImage\", \"CommercialVideo\", etc.     ### Ontologies    [Ontologies](#!/Ontologies) represent a set of extended types.    For example:    - The Default ontology provides generic names for most types (ex: Temperature for SensorDataType, Map for SpaceBlobType, Active for SpaceStatus, etc.) and space types (ex: Space Type Room and Space Subtypes FocusRoom, ConferenceRoom, BathRoom, etc.).  - The BACnet ontology provides names for sensor types/datatypes/datasubtypes/dataunittypes.    Ontologies are managed by the system and new ontologies or new type names are regularly added.  Users can load or unload ontologies. When an ontology is loaded, all of its associated type names have their `Disabled` state reset to false, unless they were already associated with a different loaded ontology.   When an ontology is unloaded, all of its associated type names are switched to `Disabled` unless they are associated with another loaded ontology.  Types associated with ontologies are available system wide (all spaces).    By default, the following ontologies are loaded: Default, Required.    All the existing ontologies are still a work in progress, and they will be improved over time.    ### Property keys and values    Some objects such as [spaces](#!/Spaces), [devices](#!/Device), [Users](#!/Users) and [sensors](#!/Sensors) can have custom properties defined in addition to the built-in ones.    Associating a custom property to an object is a two steps process:    1. Creating the [property key](#!/PropertyKeys): a key exists for a given space tree/hierarchy (the space and it's children) and scope (eg. spaces, devices, users or sensors).    An example of property key could be `BasicTemperatureDeltaProcessingRefreshTime` of type `uint` for scope Sensors and Space \"Contoso customer\".  2. Assigning a value for the given key to an object of the space hierarchy: using the previous example, [assigning the value](#!/Sensors/Sensors_AddProperty) `10` to the `BasicTemperatureDeltaProcessingRefreshTime`    property for a sensor attached to `/Contoso customer/Building 1/Floor 10/Room 13`.    Property keys' authors can optionally specify constraints on the associated property value, for example:    - `PrimitiveDataType`: when setting `PrimitiveDataType`=`bool` for the HasWindows property key, only the `true` and `false` values are allowed.  - `Min` and `Max`: when setting `PrimitiveDataType`=int, `Min`=0, `Max`=120 for the age property key, only numbers between 0 and 120 are allowed.  - `ValidationData`: when setting `PrimitiveDataType`=enum, `ValidationData`=purple;blue;green for the haircolor property key, only purple, blue or green are allowed.    Note: constraints are enforced on new values and not on existing ones.    | `PrimitiveDataType` | `Min` & `Max`          | `ValidationData`  | Notes                            |  | --------------------|------------------------|-------------------|----------------------------------|  | None                | N/A                    | N/A               |                                  |  | bool                | N/A                    | N/A               | Value is converted to lower case |  | string              | string length range    | Optional [regex](https://docs.microsoft.com/en-us/dotnet/standard/base-types/regular-expression-language-quick-reference) | Regex is single line case insensitive |  | long,int,uint       | allowed value range    | N/A               | White spaces are trimmed         |  | datetime            | ISO8601 datetime range | optional [format specifier](https://docs.microsoft.com/en-us/dotnet/standard/base-types/standard-date-and-time-format-strings), for example \"yyyy-MM-dd\". Defaults to \"o\"
 * if not specified| | set | nb elements range | val1;val2;... | Values are converted to the case
 * specified in the key | | enum | N/A | val1;val2;... | Values is converted to the case specified
 * in the key | | json | string length range | optional [schema](http://json-schema.org/) | | ###
 * Users [Users](#!/Users) are attached to [spaces](#!/Spaces). They are used to locate individuals,
 * and not for permissioning. ### Roles [Roles](#!/System/System_RetrieveRoles) correspond to user
 * roles. Each role has a set of permissions (for example: `Read, Write, Update`) that defines the
 * privileges granted to a user. ### Role Assignments [Role Assignments](#!/RoleAssignments)
 * correspond to the association between identifiers and roles. Each role assignment includes an
 * identifier (such as user id, domain name, etc.), an identifier type, a role id, a tenant id and a
 * path that associates the corresponding identifier with a role. The path defines the upper limit
 * of the resource that the identifier can access with the permissions granted. The tenant id is not
 * allowed for gateway devices. ### Keys A [space](#!/Spaces) can have a number of [security key
 * stores](#!/KeyStores). These stores holds a collection of security keys and allow for easily
 * retrieving the [latest valid](#!/KeyStores/KeyStores_RetrieveKeysLast) one. ### System The
 * [system](#!/System) APIs allow for managing system wide settings, such as the various available
 * types of [spaces](#!/Spaces) and [sensors](#!/Sensors). ## Tree navigation The main routes (ex:
 * `/spaces`, `/devices`, `/users`, `/sensors`,...) support tree filtering/navigation with the
 * following optional parameters: - `spaceId`: if specified, this space node is used for the
 * filtering. - `/spaces` route only: the `useParentSpace` boolean flag indicates if `spaceId`
 * refers to the parent space or the current space. - `traverse`: - If `None` (the default) then
 * only the specified `spaceId` is returned. - If `Down`, the specified `spaceId` and its
 * descendants are returned. - If `Up`, the specified `spaceId` and its ancestors are returned. - If
 * `Span`, `spaceId` is used to select a horizontal portion of the tree relative to the specified
 * space node. In this case at least one of `minRelative` or `maxRelative` must be set to true. -
 * `minLevel`, `maxLevel`, `minRelative` and `maxRelative` - `minLevel` and `maxLevel` both default
 * to not set. - `minLevel` and `maxLevel` are both inclusive when set. - `minRelative` and
 * `maxRelative` both default to false, meaning by default levels are absolute. - Root spaces
 * (spaces with no parent) are of level 1, and spaces with a parent space of level n are of level n
 * + 1. - Devices, sensors, etc. are of the same level than their closest space. - To get all
 * objects of a given level, set both `minLevel` and `maxLevel` to the same value. - When
 * `minRelative` or `maxRelative` is set, the corresponding level is relative to the level of the
 * specified `spaceId`. - Relative level 0 represents spaces at the same level than the specified
 * space. - Relative level 1 represents spaces of the same level as the children of the specified
 * space. Relative level n represents spaces lower than the specified space by n levels. - Relative
 * level -1 represents spaces of the same level as the parent space of the specified spaceId, etc.
 * Examples with the `devices` route: - `devices?maxLevel=1`: returns all devices attached to root
 * spaces. - `devices?minLevel=2&maxLevel=4`: returns all devices attached to spaces of levels 2, 3
 * or 4. - `devices?spaceId=mySpaceId`: returns all devices directly attached to `mySpaceId`. -
 * `devices?spaceId=mySpaceId&traverse=Down`: returns all devices attached to `mySpaceId` or one of
 * its descendants. - `devices?spaceId=mySpaceId&traverse=Down&minLevel=1&minRelative=true`: returns
 * all devices attached to descendants of `mySpaceId`, `mySpaceId` excluded. -
 * `devices?spaceId=mySpaceId&traverse=Down&minLevel=1&minRelative=true&maxLevel=1&maxRelative=true`
 * : returns all devices attached to direct children of `mySpaceId`. -
 * `devices?spaceId=mySpaceId&traverse=Up&maxLevel=-1&maxRelative=true`: returns all devices
 * attached to one of the ancestors of `mySpaceId`. -
 * `devices?spaceId=mySpaceId&traverse=Down&maxLevel=5`: returns all devices attached to descendants
 * of `mySpaceId` that are of level smaller than or equal to 5. -
 * `devices?spaceId=mySpaceId&traverse=Span&minLevel=0&minRelative=true&maxLevel=0&maxRelative=true`
 * : returns all devices attached to spaces that are of the same level than `mySpaceId` ## OData
 * support Most of the APIs that return collections (for example a GET on `/api/v1.0/spaces`,
 * `/api/v1.0/spaces/{id}/parents`, `/api/v1.0/spaces/{id}`, etc.) support a subset of the generic
 * [OData](http://www.odata.org/getting-started/basic-tutorial/#queryData) system query options: -
 * `$filter`. - `$orderby`. **Note:** unless it's being used in combination with `$top` the
 * recommendation is to order on the client instead. - `$top` - `$skip`. **Note:** the server cost
 * of requesting n times a 1/n subset of a collection with `$skip` is quadratic (instead of linear).
 * Therefore a client that intends on displaying the entire collection should request it whole (in a
 * single call) and perform paging client-side. - Other options (`$count`, `$expand`, `$search`,
 * etc.) are not supported. Below some examples of queries using OData's system query options: -
 * /api/v1.0/devices?$top=3&$orderby=Name desc -
 * /api/v1.0/keystores?$filter=endswith(Description,'space') - /api/v1.0/propertykeys?$filter=Scope
 * ne 'Spaces' - /api/v1.0/resources?$filter=Size gt 'M' -
 * /api/v1.0/users?$top=4&$filter=endswith(LastName,'k')&$orderby=LastName -
 * /api/v1.0/spaces?$orderby=Name desc&$top=3&$filter=substringof('Floor',Name) ## Last Updated
 * 2018-12-12T18:06:34.0000000Z ## API List
 *
 * OpenAPI spec version: V1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git Do not edit the class manually.
 */
package com.microsoft.twins.model;

import java.util.Objects;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SkipQueryOption
 */
public class SkipQueryOption {
  @JsonProperty("context")
  private ODataQueryContext context;
  @JsonProperty("rawValue")
  private String rawValue;
  @JsonProperty("value")
  private Integer value;
  @JsonProperty("validator")
  private SkipQueryValidator validator;

  public SkipQueryOption context(ODataQueryContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   *
   * @return context
   **/
  @Valid
  public ODataQueryContext getContext() {
    return context;
  }

  public void setContext(ODataQueryContext context) {
    this.context = context;
  }

  /**
   * Get rawValue
   *
   * @return rawValue
   **/
  public String getRawValue() {
    return rawValue;
  }

  /**
   * Get value
   *
   * @return value
   **/
  public Integer getValue() {
    return value;
  }

  public SkipQueryOption validator(SkipQueryValidator validator) {
    this.validator = validator;
    return this;
  }

  /**
   * Get validator
   *
   * @return validator
   **/
  @Valid
  public SkipQueryValidator getValidator() {
    return validator;
  }

  public void setValidator(SkipQueryValidator validator) {
    this.validator = validator;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final SkipQueryOption skipQueryOption = (SkipQueryOption) o;
    return Objects.equals(this.context, skipQueryOption.context)
        && Objects.equals(this.rawValue, skipQueryOption.rawValue)
        && Objects.equals(this.value, skipQueryOption.value)
        && Objects.equals(this.validator, skipQueryOption.validator);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(context, rawValue, value, validator);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class SkipQueryOption {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    rawValue: ").append(toIndentedString(rawValue)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    validator: ").append(toIndentedString(validator)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
