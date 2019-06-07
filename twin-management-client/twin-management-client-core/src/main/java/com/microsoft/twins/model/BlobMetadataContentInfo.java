/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * BlobMetadataContentInfo
 */
@ToString
@EqualsAndHashCode
public class BlobMetadataContentInfo {
  @JsonProperty("type")
  private String type;
  @JsonProperty("sizeBytes")
  private Long sizeBytes;
  @JsonProperty("mD5")
  private String mD5;
  @JsonProperty("version")
  private String version;
  @JsonProperty("lastModifiedUtc")
  private LocalDateTime lastModifiedUtc;
  @JsonProperty("metadata")
  private Map<String, String> metadata;

  public BlobMetadataContentInfo type(final String type) {
    this.type = type;
    return this;
  }

  /**
   * Content type
   *
   * @return type
   **/
  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public BlobMetadataContentInfo sizeBytes(final Long sizeBytes) {
    this.sizeBytes = sizeBytes;
    return this;
  }

  /**
   * Content size
   *
   * @return sizeBytes
   **/
  public Long getSizeBytes() {
    return sizeBytes;
  }

  public void setSizeBytes(final Long sizeBytes) {
    this.sizeBytes = sizeBytes;
  }

  public BlobMetadataContentInfo mD5(final String mD5) {
    this.mD5 = mD5;
    return this;
  }

  /**
   * Content MD5
   *
   * @return mD5
   **/
  public String getMD5() {
    return mD5;
  }

  public void setMD5(final String mD5) {
    this.mD5 = mD5;
  }

  /**
   * Content version
   *
   * @return version
   **/
  public String getVersion() {
    return version;
  }

  public BlobMetadataContentInfo lastModifiedUtc(final LocalDateTime lastModifiedUtc) {
    this.lastModifiedUtc = lastModifiedUtc;
    return this;
  }

  /**
   * Content last modified
   *
   * @return lastModifiedUtc
   **/
  @Valid
  public LocalDateTime getLastModifiedUtc() {
    return lastModifiedUtc;
  }

  public void setLastModifiedUtc(final LocalDateTime lastModifiedUtc) {
    this.lastModifiedUtc = lastModifiedUtc;
  }

  public BlobMetadataContentInfo metadata(final Map<String, String> metadata) {
    this.metadata = metadata;
    return this;
  }

  public BlobMetadataContentInfo putMetadataItem(final String key, final String metadataItem) {
    if (this.metadata == null) {
      this.metadata = new java.util.HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Content metadata
   *
   * @return metadata
   **/
  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(final Map<String, String> metadata) {
    this.metadata = metadata;
  }
}
