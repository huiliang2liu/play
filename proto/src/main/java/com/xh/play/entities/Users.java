// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: user.proto

package com.xh.play.entities;

public final class Users {
  private Users() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface UserOrBuilder extends
      // @@protoc_insertion_point(interface_extends:proto_user.User)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string name = 1;</code>
     * @return The name.
     */
    java.lang.String getName();
    /**
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <code>int32 sex = 2;</code>
     * @return The sex.
     */
    int getSex();

    /**
     * <code>int32 age = 3;</code>
     * @return The age.
     */
    int getAge();

    /**
     * <code>.proto_app.Update update = 4;</code>
     * @return Whether the update field is set.
     */
    boolean hasUpdate();
    /**
     * <code>.proto_app.Update update = 4;</code>
     * @return The update.
     */
    com.xh.play.entities.Entities.Update getUpdate();
    /**
     * <code>.proto_app.Update update = 4;</code>
     */
    com.xh.play.entities.Entities.UpdateOrBuilder getUpdateOrBuilder();
  }
  /**
   * Protobuf type {@code proto_user.User}
   */
  public static final class User extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:proto_user.User)
      UserOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use User.newBuilder() to construct.
    private User(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private User() {
      name_ = "";
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new User();
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private User(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              java.lang.String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
            case 16: {

              sex_ = input.readInt32();
              break;
            }
            case 24: {

              age_ = input.readInt32();
              break;
            }
            case 34: {
              com.xh.play.entities.Entities.Update.Builder subBuilder = null;
              if (update_ != null) {
                subBuilder = update_.toBuilder();
              }
              update_ = input.readMessage(com.xh.play.entities.Entities.Update.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(update_);
                update_ = subBuilder.buildPartial();
              }

              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.xh.play.entities.Users.internal_static_proto_user_User_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.xh.play.entities.Users.internal_static_proto_user_User_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.xh.play.entities.Users.User.class, com.xh.play.entities.Users.User.Builder.class);
    }

    public static final int NAME_FIELD_NUMBER = 1;
    private volatile java.lang.Object name_;
    /**
     * <code>string name = 1;</code>
     * @return The name.
     */
    @java.lang.Override
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SEX_FIELD_NUMBER = 2;
    private int sex_;
    /**
     * <code>int32 sex = 2;</code>
     * @return The sex.
     */
    @java.lang.Override
    public int getSex() {
      return sex_;
    }

    public static final int AGE_FIELD_NUMBER = 3;
    private int age_;
    /**
     * <code>int32 age = 3;</code>
     * @return The age.
     */
    @java.lang.Override
    public int getAge() {
      return age_;
    }

    public static final int UPDATE_FIELD_NUMBER = 4;
    private com.xh.play.entities.Entities.Update update_;
    /**
     * <code>.proto_app.Update update = 4;</code>
     * @return Whether the update field is set.
     */
    @java.lang.Override
    public boolean hasUpdate() {
      return update_ != null;
    }
    /**
     * <code>.proto_app.Update update = 4;</code>
     * @return The update.
     */
    @java.lang.Override
    public com.xh.play.entities.Entities.Update getUpdate() {
      return update_ == null ? com.xh.play.entities.Entities.Update.getDefaultInstance() : update_;
    }
    /**
     * <code>.proto_app.Update update = 4;</code>
     */
    @java.lang.Override
    public com.xh.play.entities.Entities.UpdateOrBuilder getUpdateOrBuilder() {
      return getUpdate();
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
      }
      if (sex_ != 0) {
        output.writeInt32(2, sex_);
      }
      if (age_ != 0) {
        output.writeInt32(3, age_);
      }
      if (update_ != null) {
        output.writeMessage(4, getUpdate());
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
      }
      if (sex_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, sex_);
      }
      if (age_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, age_);
      }
      if (update_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, getUpdate());
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.xh.play.entities.Users.User)) {
        return super.equals(obj);
      }
      com.xh.play.entities.Users.User other = (com.xh.play.entities.Users.User) obj;

      if (!getName()
          .equals(other.getName())) return false;
      if (getSex()
          != other.getSex()) return false;
      if (getAge()
          != other.getAge()) return false;
      if (hasUpdate() != other.hasUpdate()) return false;
      if (hasUpdate()) {
        if (!getUpdate()
            .equals(other.getUpdate())) return false;
      }
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (37 * hash) + SEX_FIELD_NUMBER;
      hash = (53 * hash) + getSex();
      hash = (37 * hash) + AGE_FIELD_NUMBER;
      hash = (53 * hash) + getAge();
      if (hasUpdate()) {
        hash = (37 * hash) + UPDATE_FIELD_NUMBER;
        hash = (53 * hash) + getUpdate().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.xh.play.entities.Users.User parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.xh.play.entities.Users.User parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.xh.play.entities.Users.User parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.xh.play.entities.Users.User parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.xh.play.entities.Users.User parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.xh.play.entities.Users.User parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.xh.play.entities.Users.User prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code proto_user.User}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:proto_user.User)
        com.xh.play.entities.Users.UserOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.xh.play.entities.Users.internal_static_proto_user_User_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.xh.play.entities.Users.internal_static_proto_user_User_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.xh.play.entities.Users.User.class, com.xh.play.entities.Users.User.Builder.class);
      }

      // Construct using com.xh.play.entities.Users.User.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        name_ = "";

        sex_ = 0;

        age_ = 0;

        if (updateBuilder_ == null) {
          update_ = null;
        } else {
          update_ = null;
          updateBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.xh.play.entities.Users.internal_static_proto_user_User_descriptor;
      }

      @java.lang.Override
      public com.xh.play.entities.Users.User getDefaultInstanceForType() {
        return com.xh.play.entities.Users.User.getDefaultInstance();
      }

      @java.lang.Override
      public com.xh.play.entities.Users.User build() {
        com.xh.play.entities.Users.User result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.xh.play.entities.Users.User buildPartial() {
        com.xh.play.entities.Users.User result = new com.xh.play.entities.Users.User(this);
        result.name_ = name_;
        result.sex_ = sex_;
        result.age_ = age_;
        if (updateBuilder_ == null) {
          result.update_ = update_;
        } else {
          result.update_ = updateBuilder_.build();
        }
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.xh.play.entities.Users.User) {
          return mergeFrom((com.xh.play.entities.Users.User)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.xh.play.entities.Users.User other) {
        if (other == com.xh.play.entities.Users.User.getDefaultInstance()) return this;
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          onChanged();
        }
        if (other.getSex() != 0) {
          setSex(other.getSex());
        }
        if (other.getAge() != 0) {
          setAge(other.getAge());
        }
        if (other.hasUpdate()) {
          mergeUpdate(other.getUpdate());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.xh.play.entities.Users.User parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.xh.play.entities.Users.User) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object name_ = "";
      /**
       * <code>string name = 1;</code>
       * @return The name.
       */
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string name = 1;</code>
       * @return The bytes for name.
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 1;</code>
       * @param value The name to set.
       * @return This builder for chaining.
       */
      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearName() {
        
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <code>string name = 1;</code>
       * @param value The bytes for name to set.
       * @return This builder for chaining.
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        name_ = value;
        onChanged();
        return this;
      }

      private int sex_ ;
      /**
       * <code>int32 sex = 2;</code>
       * @return The sex.
       */
      @java.lang.Override
      public int getSex() {
        return sex_;
      }
      /**
       * <code>int32 sex = 2;</code>
       * @param value The sex to set.
       * @return This builder for chaining.
       */
      public Builder setSex(int value) {
        
        sex_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 sex = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearSex() {
        
        sex_ = 0;
        onChanged();
        return this;
      }

      private int age_ ;
      /**
       * <code>int32 age = 3;</code>
       * @return The age.
       */
      @java.lang.Override
      public int getAge() {
        return age_;
      }
      /**
       * <code>int32 age = 3;</code>
       * @param value The age to set.
       * @return This builder for chaining.
       */
      public Builder setAge(int value) {
        
        age_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 age = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearAge() {
        
        age_ = 0;
        onChanged();
        return this;
      }

      private com.xh.play.entities.Entities.Update update_;
      private com.google.protobuf.SingleFieldBuilderV3<
          com.xh.play.entities.Entities.Update, com.xh.play.entities.Entities.Update.Builder, com.xh.play.entities.Entities.UpdateOrBuilder> updateBuilder_;
      /**
       * <code>.proto_app.Update update = 4;</code>
       * @return Whether the update field is set.
       */
      public boolean hasUpdate() {
        return updateBuilder_ != null || update_ != null;
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       * @return The update.
       */
      public com.xh.play.entities.Entities.Update getUpdate() {
        if (updateBuilder_ == null) {
          return update_ == null ? com.xh.play.entities.Entities.Update.getDefaultInstance() : update_;
        } else {
          return updateBuilder_.getMessage();
        }
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public Builder setUpdate(com.xh.play.entities.Entities.Update value) {
        if (updateBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          update_ = value;
          onChanged();
        } else {
          updateBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public Builder setUpdate(
          com.xh.play.entities.Entities.Update.Builder builderForValue) {
        if (updateBuilder_ == null) {
          update_ = builderForValue.build();
          onChanged();
        } else {
          updateBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public Builder mergeUpdate(com.xh.play.entities.Entities.Update value) {
        if (updateBuilder_ == null) {
          if (update_ != null) {
            update_ =
              com.xh.play.entities.Entities.Update.newBuilder(update_).mergeFrom(value).buildPartial();
          } else {
            update_ = value;
          }
          onChanged();
        } else {
          updateBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public Builder clearUpdate() {
        if (updateBuilder_ == null) {
          update_ = null;
          onChanged();
        } else {
          update_ = null;
          updateBuilder_ = null;
        }

        return this;
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public com.xh.play.entities.Entities.Update.Builder getUpdateBuilder() {
        
        onChanged();
        return getUpdateFieldBuilder().getBuilder();
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      public com.xh.play.entities.Entities.UpdateOrBuilder getUpdateOrBuilder() {
        if (updateBuilder_ != null) {
          return updateBuilder_.getMessageOrBuilder();
        } else {
          return update_ == null ?
              com.xh.play.entities.Entities.Update.getDefaultInstance() : update_;
        }
      }
      /**
       * <code>.proto_app.Update update = 4;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          com.xh.play.entities.Entities.Update, com.xh.play.entities.Entities.Update.Builder, com.xh.play.entities.Entities.UpdateOrBuilder> 
          getUpdateFieldBuilder() {
        if (updateBuilder_ == null) {
          updateBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              com.xh.play.entities.Entities.Update, com.xh.play.entities.Entities.Update.Builder, com.xh.play.entities.Entities.UpdateOrBuilder>(
                  getUpdate(),
                  getParentForChildren(),
                  isClean());
          update_ = null;
        }
        return updateBuilder_;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:proto_user.User)
    }

    // @@protoc_insertion_point(class_scope:proto_user.User)
    private static final com.xh.play.entities.Users.User DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.xh.play.entities.Users.User();
    }

    public static com.xh.play.entities.Users.User getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<User>
        PARSER = new com.google.protobuf.AbstractParser<User>() {
      @java.lang.Override
      public User parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new User(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<User> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<User> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.xh.play.entities.Users.User getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_user_User_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_user_User_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nuser.proto\022\nproto_user\032\rmessage.proto\"" +
      "Q\n\004User\022\014\n\004name\030\001 \001(\t\022\013\n\003sex\030\002 \001(\005\022\013\n\003ag" +
      "e\030\003 \001(\005\022!\n\006update\030\004 \001(\0132\021.proto_app.Upda" +
      "teB\035\n\024com.xh.play.entitiesB\005Usersb\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.xh.play.entities.Entities.getDescriptor(),
        });
    internal_static_proto_user_User_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_proto_user_User_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_user_User_descriptor,
        new java.lang.String[] { "Name", "Sex", "Age", "Update", });
    com.xh.play.entities.Entities.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}