/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type of the role assignment. With the exception of the DeviceId type, the types correspond
 * to properties of an AAD object. The DeviceId type corresponds to the device id in the topology.
 * The UserId type assigns a role to a user. The DeviceId type assigns a role to a device. The
 * DomainName type assigns a role to a domain name. Each user with the specified domain name will
 * have the access rights of the corresponding role. The TenantId type assigns a role to a tenant.
 * Each user belonging to the specified tenant id will have the access rights of the corresponding
 * role. The ServicePrincipalId type assigns a role to a service principal object id.
 */
public enum ObjectIdTypeEnum {
  USERID("UserId"), DEVICEID("DeviceId"), DOMAINNAME("DomainName"), TENANTID(
      "TenantId"), SERVICEPRINCIPALID(
          "ServicePrincipalId"), USERDEFINEDFUNCTIONID("UserDefinedFunctionId");
  private final String value;

  ObjectIdTypeEnum(final String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ObjectIdTypeEnum fromValue(final String text) {
    for (final ObjectIdTypeEnum b : ObjectIdTypeEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}