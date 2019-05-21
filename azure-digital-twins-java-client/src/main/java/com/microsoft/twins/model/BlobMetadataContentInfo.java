/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BlobMetadataContentInfo
 */
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
  private OffsetDateTime lastModifiedUtc;
  @JsonProperty("metadata")
  private java.util.Map<String, String> metadata;

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

  public BlobMetadataContentInfo lastModifiedUtc(final OffsetDateTime lastModifiedUtc) {
    this.lastModifiedUtc = lastModifiedUtc;
    return this;
  }

  /**
   * Content last modified
   *
   * @return lastModifiedUtc
   **/
  @Valid
  public OffsetDateTime getLastModifiedUtc() {
    return lastModifiedUtc;
  }

  public void setLastModifiedUtc(final OffsetDateTime lastModifiedUtc) {
    this.lastModifiedUtc = lastModifiedUtc;
  }

  public BlobMetadataContentInfo metadata(final java.util.Map<String, String> metadata) {
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
  public java.util.Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(final java.util.Map<String, String> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final BlobMetadataContentInfo blobMetadataContentInfo = (BlobMetadataContentInfo) o;
    return Objects.equals(this.type, blobMetadataContentInfo.type)
        && Objects.equals(this.sizeBytes, blobMetadataContentInfo.sizeBytes)
        && Objects.equals(this.mD5, blobMetadataContentInfo.mD5)
        && Objects.equals(this.version, blobMetadataContentInfo.version)
        && Objects.equals(this.lastModifiedUtc, blobMetadataContentInfo.lastModifiedUtc)
        && Objects.equals(this.metadata, blobMetadataContentInfo.metadata);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(type, sizeBytes, mD5, version, lastModifiedUtc, metadata);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class BlobMetadataContentInfo {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    sizeBytes: ").append(toIndentedString(sizeBytes)).append("\n");
    sb.append("    mD5: ").append(toIndentedString(mD5)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    lastModifiedUtc: ").append(toIndentedString(lastModifiedUtc)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(final java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
